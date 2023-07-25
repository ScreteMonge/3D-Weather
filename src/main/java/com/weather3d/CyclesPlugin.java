package com.weather3d;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.*;

import com.weather3d.audio.SoundEffect;
import com.weather3d.audio.SoundPlayer;
import com.weather3d.conditions.WeatherManager;
import com.weather3d.conditions.Biomes;
import com.weather3d.conditions.Seasons;
import com.weather3d.conditions.Weathers;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static com.weather3d.audio.SoundEffect.*;

@Slf4j
@PluginDescriptor(
	name = "3D Weather",
	description = "Creates immersive 3D Weather with dynamic Weather cycles and ambience",
	tags = {"immersion,", "weather", "ambience", "audio", "graphics"}
)
public class CyclesPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private CyclesConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private CyclesOverlay cyclesOverlay;
	@Inject
	private LightningOverlay lightningOverlay;
	@Inject
	private ConfigManager configManager;
	@Inject
	private PluginManager pluginManager;
	@Inject
	private ModelHandler modelHandler;

	private final Random random = new Random();
	private final ArrayList<WeatherManager> weatherManagerList = new ArrayList<>();
	private ArrayList<Tile> availableFogTiles = new ArrayList<>();
	private ArrayList<Tile> availableTiles = new ArrayList<>();
	private boolean loadedAnimsModels = false;
	private boolean conditionsSynced = false;
	private boolean isPlayerIndoors = false;
	public boolean flashLightning = false;
	private boolean winter117 = false;
	private int savedChunk = 0;
	private int savedZPlane = -1;
	private int zoneObjRecovery = 0;
	private final int WINTERTODT_CHUNK = 6462;
	private final int OBJ_ROTATION_CONSTANT = 20;
	private final int MODEL_TRANSPARENT_SWAP_DISTANCE = 3000;
	private final int MODEL_DISAPPEAR_DISTANCE = 2500;
	private final int FOG_RADIUS = 100;

	@Getter
	private Seasons currentSeason = Seasons.SPRING;
	@Getter
	private Biomes currentBiome = Biomes.GRASSLAND;
	@Getter
	private Weathers currentWeather;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(cyclesOverlay);
		overlayManager.add(lightningOverlay);

		if (client.getLocalPlayer() != null)
		{
			syncBiome();
			syncSeason();
			setConfigWeather();
			handleWeatherManagers();
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invoke(this::clearAllWeatherManagers);
		overlayManager.remove(cyclesOverlay);
		overlayManager.remove(lightningOverlay);
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		for (WeatherManager weatherManager : weatherManagerList)
		{
			Weathers weatherType = weatherManager.getWeatherType();

			if (weatherType == Weathers.CLOUDY
					|| weatherType == Weathers.PARTLY_CLOUDY
					|| weatherType == Weathers.COSMOS)
			{
				LocalPoint localPoint = new LocalPoint(client.getCameraX(), client.getCameraY());

				for (WeatherObject weatherObject : weatherManager.getWeatherObjArray())
				{
					RuneLiteObject runeLiteObject = weatherObject.getRuneLiteObject();
					int objectVariant = weatherObject.getObjVariant();
					int distance = runeLiteObject.getLocation().distanceTo(localPoint);

					if (distance < MODEL_DISAPPEAR_DISTANCE)
					{
						runeLiteObject.setActive(false);
						continue;
					}

					runeLiteObject.setActive(true);

					if (distance < MODEL_TRANSPARENT_SWAP_DISTANCE)
					{
						runeLiteObject.setModel(modelHandler.getTransparentModel(weatherType, objectVariant));
						continue;
					}

					runeLiteObject.setModel(modelHandler.getRegularModel(weatherType, objectVariant));
				}
			}
		}
	}


	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!loadedAnimsModels)
		{
			modelHandler.loadModels();
			loadedAnimsModels = true;
		}

		if (!conditionsSynced)
		{
			setConfigWeather();
			if (currentWeather.isHasPrecipitation())
			{
				handleWeatherManagers();
			}
			conditionsSynced = true;
		}

		if (client.getGameState() != GameState.LOGGED_IN)
			return;

		syncSeason();
		syncBiome();

		isPlayerIndoors = true;
		WorldPoint playerLoc = client.getLocalPlayer().getWorldLocation();
		for (Tile t : availableTiles)
		{
			if (t.getWorldLocation().getX() == playerLoc.getX() && t.getWorldLocation().getY() == playerLoc.getY())
			{
				isPlayerIndoors = false;
			}
		}

		if (config.weatherType() == CyclesConfig.WeatherType.DYNAMIC)
		{
			Weathers nextWeather = syncWeather(currentSeason, currentBiome);

			if (nextWeather != currentWeather)
			{
				setConfigWeather();
				handleWeatherManagers();
			}
			conditionsSynced = true;
		}

		for (WeatherManager weatherManager : weatherManagerList)
		{
			if (weatherManager.isFading())
			{
				fadeWeatherManager(weatherManager);
			}
			else
			{
				handleWeatherChanges(weatherManager);
			}

			handleSoundChanges(weatherManager);
		}

		clearFadedWeatherManagers();

		if (savedZPlane != client.getPlane())
		{
			transitionZPlane();
			savedZPlane = client.getPlane();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState gameState = event.getGameState();
		if (gameState == GameState.LOGIN_SCREEN || gameState == GameState.LOGIN_SCREEN_AUTHENTICATOR || gameState == GameState.STARTING)
		{
			clientThread.invoke(this::clearAllWeatherManagers);
			return;
		}

		if (gameState != GameState.LOGGED_IN)
			return;

		updateAvailableTiles();
		syncBiome();
		syncSeason();
		setConfigWeather();
		handleWeatherManagers();
		handleZoneTransition();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("3Dweather"))
			return;

		if (client.getGameState() != GameState.LOGGED_IN)
			return;

		if (event.getKey().equals("weatherType"))
		{
			setConfigWeather();
			handleWeatherManagers();
			return;
		}

		if (event.getKey().equals("disableWeatherUnderground"))
		{
			if (!config.disableWeatherUnderground())
				return;

			if (currentBiome == Biomes.CAVE || currentBiome == Biomes.LAVA_CAVE)
				clientThread.invoke(this::clearAllWeatherManagers);
		}

		if (event.getKey().equals("seasonType"))
		{
			syncSeason();
			return;
		}

		if (event.getKey().equals("weatherDensity"))
		{
			for (WeatherManager weatherManager : weatherManagerList)
			{
				Weathers weatherType = weatherManager.getWeatherType();

				if (weatherType.isHasPrecipitation())
				{
					ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();

					clientThread.invoke(() -> {
						while (array.size() > getMaxWeatherObjects(weatherType))
						{
							removeWeatherObject(0, array);
						}
					});
				}

				if (weatherType.isHasSound())
				{
					for (SoundPlayer soundPlayer : weatherManager.getSoundPlayers())
					{
						SoundEffect currentTrack = soundPlayer.getCurrentTrack();
						if (currentTrack == null)
							continue;

						int volumeMax = getMaxVolume(currentTrack.isMuffled());
						if (soundPlayer.getCurrentVolume() > volumeMax)
							soundPlayer.setVolumeLevel(volumeMax);
					}
				}
			}
			return;
		}

		if (event.getKey().equals("toggleOverlay"))
		{
			if (config.toggleOverlay())
			{
				overlayManager.add(cyclesOverlay);
				return;
			}

			overlayManager.remove(cyclesOverlay);
			return;
		}

		if (event.getKey().equals("toggleAmbience"))
		{
			for (WeatherManager weatherManager : weatherManagerList)
			{
				if (weatherManager.getWeatherType().isHasSound())
				{
					if (config.toggleAmbience())
					{
						handleSoundChanges(weatherManager);
						return;
					}

					weatherManager.stopManagerSoundPlayers();
				}
			}
			return;
		}

		if (event.getKey().equals("enableClouds"))
		{
			if (config.enableClouds())
				return;

			for (WeatherManager weatherManager : weatherManagerList)
			{
				if (weatherManager.getWeatherType() == Weathers.CLOUDY || weatherManager.getWeatherType() == Weathers.PARTLY_CLOUDY)
					clientThread.invoke(() -> clearWeatherObjects(weatherManager));
			}
			return;
		}

		if (event.getKey().equals("enableFog"))
		{
			if (config.enableFog())
				return;

			for (WeatherManager weatherManager : weatherManagerList)
			{
				if (weatherManager.getWeatherType() == Weathers.FOGGY)
				{
					clientThread.invoke(() -> clearWeatherObjects(weatherManager));
				}
			}
			return;
		}

		if (event.getKey().equals("enableStars"))
		{
			if (config.enableStars())
			{
				return;
			}

			for (WeatherManager weatherManager : weatherManagerList)
			{
				if (weatherManager.getWeatherType() == Weathers.COSMOS)
				{
					clientThread.invoke(() -> clearWeatherObjects(weatherManager));
					clientThread.invoke(weatherManager::stopManagerSoundPlayers);
				}
			}
			return;
		}

		if (event.getKey().equals("enableWintertodtSnow"))
		{
			if (config.enableWintertodtSnow())
			{
				return;
			}

			int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();

			if (playerChunk == WINTERTODT_CHUNK)
			{
				for (WeatherManager weatherManager : weatherManagerList)
				{
					if (weatherManager.getWeatherType() == Weathers.SNOWING)
					{
						clientThread.invoke(() -> clearWeatherObjects(weatherManager));
						clientThread.invoke(weatherManager::stopManagerSoundPlayers);
					}
				}
			}
		}
	}

	public void handleWeatherManagers()
	{
		boolean activeManager = false;
		for (WeatherManager weatherManager : weatherManagerList)
		{
			weatherManager.setFading(true);
			if (weatherManager.getWeatherType() == currentWeather)
			{
				weatherManager.setFading(false);
				activeManager = true;
			}
		}

		if (!activeManager && currentWeather.isHasPrecipitation())
			SwingUtilities.invokeLater(this::createWeatherManager);
	}

	public void handleSoundChanges(WeatherManager weatherManager)
	{
		//Update soundplayer timers
		for (SoundPlayer sp : weatherManager.getSoundPlayers())
		{
			if (sp.isPlaying())
			{
				sp.setTimer(sp.getTimer() + 1);
			}
		}

		Weathers weatherCondition = weatherManager.getWeatherType();

		if (!weatherCondition.isHasSound() || !config.toggleAmbience())
			return;

		if (config.weatherType() != CyclesConfig.WeatherType.DYNAMIC && config.disableWeatherUnderground() && (currentBiome == Biomes.CAVE || currentBiome == Biomes.LAVA_CAVE))
			return;

		//Fade out inappropriate weathermanager soundplayers
		if (weatherCondition != currentWeather)
		{
			for (SoundPlayer sp : weatherManager.getSoundPlayers())
			{
				if (sp.isPlaying())
				{
					if (!sp.isFading())
					{
						sp.setFading(true);
						sp.smoothVolumeChange(0, 6000);
					}
				}
			}
			return;
		}
		else
		{
			//This happens when you switch from weather condition A to weather condition B and then back to A again before the first transition was finished
			if (weatherManager.getPrimarySoundPlayer().isFading())
			{
				weatherManager.getPrimarySoundPlayer().setFading(false);
				weatherManager.getPrimarySoundPlayer().getVolumeChangeHandler().interrupt(); //stop fading oot
			}
		}

		//The following code of this function only runs on the appropriate weathermanager

		SoundEffect appropriateSound;
		boolean muffled = false;
		SoundEffect outdoorSound = weatherManager.getWeatherType().getSoundEffect();
		if (isPlayerIndoors && !config.disableIndoorMuffling())
		{
			if (outdoorSound == RAIN)
			{
				appropriateSound = RAIN_MUFFLED;
				muffled = true;
			}
			else if (outdoorSound == THUNDERSTORM)
			{
				appropriateSound = THUNDERSTORM_MUFFLED;
				muffled = true;
			}
			else if (outdoorSound == WIND)
			{
				appropriateSound = WIND_MUFFLED;
				muffled = true;
			}
			else
			{
				appropriateSound = outdoorSound;
			}
		}
		else
		{
			appropriateSound = outdoorSound;
		}


		double maxWeatherObjects = getMaxWeatherObjects(weatherCondition);
		double volumeDouble = (weatherManager.getWeatherObjArray().size() / maxWeatherObjects) * getMaxVolume(muffled);
		int volumeGoal = (int) volumeDouble;

		// Make sure the primary soundplayer is at the right volume, if it's not already fading in or whatever
		SoundPlayer primary = weatherManager.getPrimarySoundPlayer();
		if (primary.getCurrentTrack() != null && primary.getCurrentVolume() != volumeGoal)
		{
			//If the volume change handler is uninitialized, or is initialized and isn't currently changing the volume
			if (primary.getVolumeChangeHandler() == null || !primary.getVolumeChangeHandler().isAlive())
			{
				log.debug("Primary at wrong volume. Setting back to " + volumeGoal);
				primary.smoothVolumeChange(volumeGoal, 6000);
			}
		}

		// Initialize the primary soundplayer if it ain't initialized yet, or is not playing for some reason
		if (weatherManager.getPrimarySoundPlayer().getCurrentTrack() == null || !weatherManager.getPrimarySoundPlayer().isPlaying())
		{
			log.debug("Initializing soundplayer at volume " + (int)(config.ambientVolume() * getWeatherDensityFactor()));
			weatherManager.getPrimarySoundPlayer().setVolumeLevel(0);
			weatherManager.getPrimarySoundPlayer().smoothVolumeChange(getMaxVolume(muffled), 12000);
			weatherManager.getPrimarySoundPlayer().playClip(appropriateSound);
		}
		//Handle looping, as well as muffling/unmuffling of sound when player walks indoors/outdoors
		else if (weatherManager.getPrimarySoundPlayer().getCurrentTrack() != appropriateSound || weatherManager.getPrimarySoundPlayer().getTimer() > 230)
		{
			log.debug("Looping because " + weatherManager.getPrimarySoundPlayer().getCurrentTrack() + " != " + appropriateSound + " or it was just time to loop");
			weatherManager.getPrimarySoundPlayer().smoothVolumeChange(0, 6000);
			weatherManager.switchSoundPlayerPriority();
			if (!weatherManager.getPrimarySoundPlayer().isPlaying())
			{
				weatherManager.getPrimarySoundPlayer().setVolumeLevel(0);
				weatherManager.getPrimarySoundPlayer().playClip(appropriateSound);
			}
			weatherManager.getPrimarySoundPlayer().smoothVolumeChange(volumeGoal, 6000);
		}

		if (weatherCondition == Weathers.STORM && (weatherManager.getPrimarySoundPlayer().getTimer() == 90 || weatherManager.getPrimarySoundPlayer().getTimer() == 138))
			flashLightning = true;
	}

	public double getWeatherDensityFactor()
	{
		switch (config.weatherDensity())
		{
			case LOW:
				return 0.5;
			default:
			case MEDIUM:
				return 0.65;
			case HIGH:
				return 0.85;
			case EXTREME:
			case GAMEBREAKING:
				return 1;
		}
	}

	public int getMaxVolume(boolean muffled)
	{
		double weatherDensityFactor = getWeatherDensityFactor();
		double volumeDouble = config.ambientVolume() * weatherDensityFactor;

		if (muffled)
			volumeDouble = config.muffledVolume() * weatherDensityFactor;

		return (int) volumeDouble;
	}

	public void createWeatherManager()
	{
		SoundPlayer[] soundPlayers = new SoundPlayer[]{new SoundPlayer(), new SoundPlayer()};
		WeatherManager weatherManager = new WeatherManager(currentWeather, soundPlayers, 0, new ArrayList<>(), 0, false);
		weatherManagerList.add(weatherManager);
	}

	public void fadeWeatherManager(WeatherManager weatherManager)
	{
		int trimNumber = getMaxWeatherObjects(weatherManager.getWeatherType()) / OBJ_ROTATION_CONSTANT;
		ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();

		if (trimNumber < array.size() / OBJ_ROTATION_CONSTANT)
			trimNumber = array.size() / OBJ_ROTATION_CONSTANT;

		if (trimNumber == 0)
			trimNumber = 1;

		trimWeatherArray(weatherManager, 0, trimNumber);
	}

	public void clearFadedWeatherManagers()
	{
		for (int i = 0; i < weatherManagerList.size(); i++)
		{
			WeatherManager weatherManager = weatherManagerList.get(i);

			if (weatherManager.isFading() && weatherManager.getWeatherObjArray().isEmpty())
			{
				boolean readyToRemove = true;

				for (SoundPlayer soundPlayer : weatherManager.getSoundPlayers())
				{
					if (soundPlayer.isPlaying())
					{
						readyToRemove = false;
					}
				}

				if (readyToRemove)
				{
					weatherManagerList.remove(weatherManager);
					i--;
				}
			}
		}
	}

	public void clearAllWeatherManagers()
	{
		for (WeatherManager weatherManager : weatherManagerList)
		{
			clearWeatherObjects(weatherManager);
			weatherManager.stopManagerSoundPlayers();
		}
		weatherManagerList.clear();
	}

	public void handleWeatherChanges(WeatherManager weatherManager)
	{
		Weathers weather = weatherManager.getWeatherType();

		if (config.weatherType() != CyclesConfig.WeatherType.DYNAMIC && config.disableWeatherUnderground() && (currentBiome == Biomes.CAVE || currentBiome == Biomes.LAVA_CAVE))
			return;

		if ((weather == Weathers.CLOUDY || weather == Weathers.PARTLY_CLOUDY) && !config.enableClouds())
			return;

		if (weather == Weathers.FOGGY && !config.enableFog())
			return;

		if (weather == Weathers.COSMOS && !config.enableStars())
			return;

		if (!config.enableWintertodtSnow() && weather == Weathers.SNOWING)
		{
			int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();
			if (playerChunk == WINTERTODT_CHUNK)
			{
				return;
			}
		}

		int maxWeatherObj = getMaxWeatherObjects(weather);
		ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();

		if (array.size() < maxWeatherObj)
		{
			if (zoneObjRecovery > 0)
			{
				relocateObjects(weatherManager, maxWeatherObj / (OBJ_ROTATION_CONSTANT / 2));
				zoneObjRecovery--;
			}
			renderWeather(maxWeatherObj / OBJ_ROTATION_CONSTANT, weatherManager);
		}
		else if (array.size() == maxWeatherObj && client.getTickCount() % weather.getChangeRate() == 0)
		{
			relocateObjects(weatherManager, maxWeatherObj / OBJ_ROTATION_CONSTANT);
		}
	}

	public int getMaxWeatherObjects(Weathers weatherCondition)
	{
		switch(config.weatherDensity())
		{
			case LOW:
				return weatherCondition.getObjLow();
			default:
			case MEDIUM:
				return weatherCondition.getObjMed();
			case HIGH:
				return weatherCondition.getObjHigh();
			case EXTREME:
				return weatherCondition.getObjExtreme();
			case GAMEBREAKING:
				return weatherCondition.getObjGameCrashing();
		}
	}

	private void renderWeather(int objects, WeatherManager weatherManager)
	{
		Weathers weatherCondition = weatherManager.getWeatherType();
		int z = client.getPlane();
		ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();
		Animation weatherAnimation = modelHandler.getWeatherAnimation(weatherCondition);
		int alternate = 1;
		if (availableTiles.isEmpty())
			updateAvailableTiles();

		for (int i = 0; i < objects; i++)
		{
			int roll;
			Tile openTile;
			switch (weatherCondition)
			{
				case FOGGY:
					roll = random.nextInt(availableFogTiles.size());
					openTile = availableFogTiles.get(roll);
					break;
				default:
					roll = random.nextInt(availableTiles.size());
					openTile = availableTiles.get(roll);
			}

			WeatherObject weatherObject = createWeatherObject(weatherCondition, weatherAnimation, openTile.getLocalLocation(), z, alternate);
			alternate += 1;
			if (alternate > weatherCondition.getModelVariety())
				alternate = 1;

			array.add(weatherObject);
			if (array.size() == getMaxWeatherObjects(weatherManager.getWeatherType()))
				return;
		}
	}

	public WeatherObject createWeatherObject(Weathers weatherCondition, Animation weatherAnimation, LocalPoint lp, int plane, int objectVariant)
	{
		RuneLiteObject runeLiteObject = client.createRuneLiteObject();
		Model weatherModel = modelHandler.getWeatherModel(weatherCondition, objectVariant);
		int radius = modelHandler.getModelRadius(weatherCondition);

		runeLiteObject.setModel(weatherModel);
		runeLiteObject.setRadius(radius);
		runeLiteObject.setDrawFrontTilesFirst(true);
		runeLiteObject.setAnimation(weatherAnimation);
		runeLiteObject.setLocation(lp, plane);
		runeLiteObject.setShouldLoop(true);
		runeLiteObject.setActive(true);
		return new WeatherObject(runeLiteObject, objectVariant);
	}

	public void removeWeatherObject(int index, ArrayList<WeatherObject> weatherArray)
	{
		if (index >= weatherArray.size())
			return;

		WeatherObject weatherObject = weatherArray.get(index);
		weatherObject.getRuneLiteObject().setActive(false);
		weatherArray.remove(index);
	}

	public void clearWeatherObjects(WeatherManager weatherManager)
	{
		ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();

		for (WeatherObject weatherObject : array)
			weatherObject.getRuneLiteObject().setActive(false);

		array.clear();
	}

	public void trimWeatherArray(WeatherManager weatherManager, int start, int end)
	{
		for (int i = start; i < end; i++)
			removeWeatherObject(start, weatherManager.getWeatherObjArray());
	}

	public void relocateObjects(WeatherManager weatherManager, int numToRelocate)
	{
		int z = client.getPlane();
		int beginRotation = weatherManager.getStartRotation();
		ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();
		Weathers weather = weatherManager.getWeatherType();

		for (int i = beginRotation; i < beginRotation + numToRelocate; i++)
		{
			int roll;
			Tile nextTile;
			switch (weather)
			{
				case FOGGY:
					roll = random.nextInt(availableFogTiles.size());
					nextTile = availableFogTiles.get(roll);
					break;
				default:
					roll = random.nextInt(availableTiles.size());
					nextTile = availableTiles.get(roll);
			}

			if (i >= array.size())
			{
				break;
			}

			WeatherObject weatherObject = array.get(i);
			RuneLiteObject runeLiteObject = weatherObject.getRuneLiteObject();
			runeLiteObject.setLocation(nextTile.getLocalLocation(), z);
			runeLiteObject.setAnimation(modelHandler.getWeatherAnimation(weather));
		}

		weatherManager.setStartRotation(beginRotation + numToRelocate);

		int maxWeatherObj = getMaxWeatherObjects(weather);
		if (beginRotation > maxWeatherObj)
			weatherManager.setStartRotation(0);
	}

	public void transitionZPlane()
	{
		if (!currentWeather.isHasPrecipitation())
		{
			clearAllWeatherManagers();
			return;
		}

		handleZoneTransition();
	}

	public void handleZoneTransition()
	{
		if (config.weatherType() != CyclesConfig.WeatherType.DYNAMIC && config.disableWeatherUnderground() && (currentBiome == Biomes.CAVE || currentBiome == Biomes.LAVA_CAVE))
		{
			clientThread.invoke(this::clearAllWeatherManagers);
			return;
		}

		for (WeatherManager weatherManager : weatherManagerList)
		{
			Weathers weatherType = weatherManager.getWeatherType();
			ArrayList<WeatherObject> array = weatherManager.getWeatherObjArray();
			int size = (int) (array.size() * 0.8);
			clearWeatherObjects(weatherManager);
			if (weatherType == currentWeather)
			{
				renderWeather(size, weatherManager);
				zoneObjRecovery = 4;
			}
		}
	}

	public void updateAvailableTiles()
	{
		availableTiles.clear();
		availableFogTiles.clear();
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		byte[][][] settings = client.getTileSettings();
		int zLayer = client.getPlane();

		for (int z = 0; z <= zLayer; z++)
		{
			for (int x = 0; x < Constants.SCENE_SIZE; ++x)
			{
				for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
					Tile tile = tiles[z][x][y];

					if (tile == null)
						continue;

					int flag = settings[z][x][y];

					if ((flag & Constants.TILE_FLAG_UNDER_ROOF) != 0)
						continue;

					availableTiles.add(tile);

					if (wallCollision(tile))
						continue;

					availableFogTiles.add(tile);
				}
			}
		}
	}

	private boolean wallCollision(Tile targetTile)
	{
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		int zLayer = client.getPlane();

		for (int z = 0; z <= zLayer; z++)
		{
			for (int x = 0; x < Constants.SCENE_SIZE; ++x)
			{
				for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
					Tile tile = tiles[z][x][y];

					if (tile == null)
						continue;

					if (tile.getWallObject() == null)
						continue;

					if (tile.getLocalLocation().distanceTo(targetTile.getLocalLocation()) < FOG_RADIUS)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public void setConfigWeather()
	{
		switch(config.weatherType())
		{
			case ASHFALL:
				currentWeather = Weathers.ASHFALL;
				break;
			default:
			case DYNAMIC:
				currentWeather = syncWeather(currentSeason, currentBiome);
				break;
			case CLOUDY:
				currentWeather = Weathers.CLOUDY;
				break;
			case CLEAR:
				currentWeather = Weathers.SUNNY;
				break;
			case FOGGY:
				currentWeather = Weathers.FOGGY;
				break;
			case PARTLY_CLOUDY:
				currentWeather = Weathers.PARTLY_CLOUDY;
				break;
			case RAINY:
				currentWeather = Weathers.RAINING;
				break;
			case SNOWY:
				currentWeather = Weathers.SNOWING;
				break;
			case STARRY:
				currentWeather = Weathers.COSMOS;
				break;
			case STORMY:
				currentWeather = Weathers.STORM;
				break;
		}
	}

	private Weathers syncWeather(Seasons seasonCondition, Biomes biomeCondition)
	{
		int totalMin = CyclesClock.getTimeHours() * 60 + CyclesClock.getTimeMinutes();
		int cycleSegment = (totalMin / 15) % 12;
		for (WeatherForecast forecast : WeatherForecast.values())
		{
			if (forecast.getSeasonCondition() == seasonCondition && forecast.getBiomeCondition() == biomeCondition)
			{
				return forecast.getForecastArray()[cycleSegment];
			}
		}
		return Weathers.COVERED;
	}

	private void syncBiome()
	{
		WorldPoint wp = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation(), client.getPlane());
		int playerChunk = wp.getRegionID();

		if (savedChunk != playerChunk)
		{
			currentBiome = BiomeChunkMap.checkBiome(playerChunk);
			savedChunk = playerChunk;
		}

		if (winter117 && currentBiome != Biomes.CAVE && currentBiome != Biomes.LAVA_CAVE)
		{
			currentBiome = Biomes.ARCTIC;
			savedChunk = -1;
		}
	}

	private void syncSeason()
	{
		if (config.winterTheme())
		{
			Collection<Plugin> plugins = pluginManager.getPlugins();

			for (Plugin plugin : plugins)
			{
				if (plugin.getName().equals("117 HD"))
				{
					if (pluginManager.isPluginEnabled(plugin))
					{
						boolean winterTheme = configManager.getConfiguration("hd", "winterTheme0", Boolean.TYPE);
						if (winterTheme)
						{
							currentSeason = Seasons.WINTER;
							winter117 = true;
							return;
						}
					}
				}
			}
		}

		winter117 = false;

		switch (config.seasonType())
		{
			default:
			case DYNAMIC:
				switch ((CyclesClock.getTimeDays() / 7) % 4)
				{
					default:
					case 0:
						currentSeason = Seasons.SPRING;
						return;
					case 1:
						currentSeason = Seasons.SUMMER;
						return;
					case 2:
						currentSeason = Seasons.AUTUMN;
						return;
					case 3:
						currentSeason = Seasons.WINTER;
						return;
				}
			case SPRING:
				currentSeason =  Seasons.SPRING;
				return;
			case SUMMER:
				currentSeason =  Seasons.SUMMER;
				return;
			case AUTUMN:
				currentSeason =  Seasons.AUTUMN;
				return;
			case WINTER:
				currentSeason =  Seasons.WINTER;
		}
	}

	@Provides
    CyclesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CyclesConfig.class);
	}
}