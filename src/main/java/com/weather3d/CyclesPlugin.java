package com.weather3d;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.*;

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
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.Random;

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
	private ConfigManager configManager;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private CyclesOverlay overlay;

	private final ArrayList<WeatherManager> weatherManagerList = new ArrayList<>();
	private Model ashModel;
	private Model fogModel;
	private Model rainModel;
	private Model sandModel;
	private Model snowModel;
	private Model starModel;
	private Animation ashAnimation;
	private Animation fogAnimation;
	private Animation rainAnimation;
	private Animation sandAnimation;
	private Animation snowAnimation;
	private Animation starAnimation;
	private final int ASH_MODEL = 27835;
	private final int ASH_ANIMATION = 7000;
	private final int FOG_MODEL = 29290;
	private final int FOG_ANIMATION = 4516;
	private final int RAIN_MODEL = 15524;
	private final int RAIN_ANIMATION = 7001;
	private final int SAND_MODEL = 9994;
	private final int SAND_ANIMATION = 2882;
	private final int SNOW_MODEL = 27835;
	private final int SNOW_ANIMATION = 7000;
	private final int STAR_MODEL = 16374;
	private final int STAR_ANIMATION = 7971;

	private boolean loadedAnimsModels = false;
	private boolean conditionsSynced = false;
	private final Random random = new Random();
	private int savedChunk = 0;
	private int savedZPlane = -1;
	private int zoneObjRecovery = 0;
	private final int WINTERTODT_CHUNK = 6462;
	private final int OBJ_ROTATION_CONSTANT = 20;
	@Getter
	private Condition currentSeason = Condition.SEASON_SPRING;
	@Getter
	private Condition currentBiome = Condition.BIOME_GRASSLAND;
	@Getter
	private Condition currentWeather;

	@Override
	protected void startUp() throws Exception
	{
		if (config.toggleOverlay())
		{
			overlayManager.add(overlay);
		}

		if (client.getLocalPlayer() != null)
		{
			int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();
			currentBiome = BiomeChunkMap.checkBiome(playerChunk);
			currentSeason = syncSeason();
			setConfigWeather();
			handleWeatherManagers();
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invoke(this::clearAllWeatherManagers);
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!loadedAnimsModels)
		{
			loadModelsAnimations();
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
		{
			return;
		}

		if (config.weatherType() == CyclesConfig.WeatherType.DYNAMIC)
		{
			WorldPoint wp = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation(), client.getPlane());

			int playerChunk = wp.getRegionID();
			if (savedChunk != playerChunk)
			{
				currentBiome = BiomeChunkMap.checkBiome(playerChunk);
				savedChunk = playerChunk;
			}

			currentSeason = syncSeason();
			Condition nextWeather = syncWeather(currentSeason, currentBiome);

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
		}

		if (gameState != GameState.LOGGED_IN)
		{
			return;
		}

		int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();
		currentBiome = BiomeChunkMap.checkBiome(playerChunk);
		currentSeason = syncSeason();
		setConfigWeather();
		handleWeatherManagers();
		handleZoneTransition();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (event.getKey().equals("weatherType"))
		{
			setConfigWeather();
			handleWeatherManagers();
			return;
		}

		if (event.getKey().equals("seasonType"))
		{
			currentSeason = syncSeason();
			return;
		}

		if (event.getKey().equals("weatherDensity"))
		{
			System.out.println("WD change");
			for (WeatherManager weatherManager : weatherManagerList)
			{
				Condition weatherType = weatherManager.getWeatherType();

				if (weatherType.isHasPrecipitation())
				{
					ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

					clientThread.invoke(() -> {
						while (array.size() > getMaxWeatherObjects(weatherType))
						{
							removeWeatherObject(0, array);
						}
					});
				}

				if (weatherType.isHasSound())
				{
					double volumeDouble = config.ambientVolume() * getWeatherDensityFactor();
					int volumeMax = (int) volumeDouble;

					for (SoundPlayer soundPlayer : weatherManager.getSoundPlayers())
					{
						if (soundPlayer.getCurrentVolume() > volumeMax)
						{
							soundPlayer.setVolumeLevel(volumeMax);
							System.out.println("Setting VolumeGoal for WD to: " + volumeMax);
						}
					}
				}
			}
			return;
		}

		if (event.getKey().equals("toggleOverlay"))
		{
			if (config.toggleOverlay())
			{
				overlayManager.add(overlay);
				return;
			}

			overlayManager.remove(overlay);
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

		if (event.getKey().equals("enableFog"))
		{
			if (config.enableFog())
			{
				return;
			}

			for (WeatherManager weatherManager : weatherManagerList)
			{
				if (weatherManager.getWeatherType() == Condition.WEATHER_FOGGY)
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
				if (weatherManager.getWeatherType() == Condition.WEATHER_COSMOS)
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
					if (weatherManager.getWeatherType() == Condition.WEATHER_SNOWING)
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
		{
			SwingUtilities.invokeLater(this::createWeatherManager);
		}
	}

	public void handleSoundChanges(WeatherManager weatherManager)
	{
		Condition weatherCondition = weatherManager.getWeatherType();
		if (!weatherCondition.isHasSound() || !config.toggleAmbience() || (weatherCondition == Condition.WEATHER_COSMOS && !config.enableStars()))
		{
			return;
		}

		int soundTimer = weatherManager.getSoundPlayerTimer();
		soundTimer++;
		weatherManager.setSoundPlayerTimer(soundTimer);
		SoundPlayer primarySoundPlayer = weatherManager.getPrimarySoundPlayer();

		if (soundTimer == 230)
		{
			weatherManager.setSoundPlayerTimer(0);
			primarySoundPlayer.setLoopFading(true);
			weatherManager.switchSoundPlayerPriority();
		}

		double maxWeatherObjects = getMaxWeatherObjects(weatherCondition);

		double volumeDouble = (weatherManager.getWeatherObjArray().size() / maxWeatherObjects) * config.ambientVolume() * getWeatherDensityFactor();
		int volumeGoal = (int) volumeDouble;

		int changeRate = config.ambientVolume() / 10;
		if (changeRate < 1)
		{
			changeRate = 1;
		}

		SoundEffect soundEffect = weatherCondition.getSoundEffect();

		for (SoundPlayer soundPlayer : weatherManager.getSoundPlayers())
		{
			if (weatherCondition != currentWeather)
			{
				soundPlayer.setTrueFading(true);
				if (!soundPlayer.isPlaying())
				{
					continue;
				}
			}
			else
			{
				soundPlayer.setTrueFading(false);
			}

			if (!soundPlayer.isPlaying())
			{
				soundPlayer.setTrueFading(false);

				if (soundPlayer == primarySoundPlayer)
				{
					System.out.println(soundPlayer.toString() + ": Starting clip");
					soundPlayer.playClip(soundEffect);
					soundPlayer.setVolumeLevel(volumeGoal / 2);
				}
				continue;
			}

			int currentVolume = soundPlayer.getCurrentVolume();

			if (soundPlayer.isLoopFading() && !soundPlayer.isTrueFading())
			{
				int endVolume = currentVolume - changeRate;
				if (endVolume < 1)
				{
					System.out.println(soundPlayer.toString() + ": stopping looping clip");
					soundPlayer.stopClip();
					soundPlayer.setLoopFading(false);
					endVolume = 0;
				}

				System.out.println(soundPlayer.toString() + ": LoopFading to: " + endVolume);
				soundPlayer.setVolumeLevel(endVolume);
				continue;
			}

			if (soundPlayer.isTrueFading())
			{
				int endVolume = currentVolume - changeRate;
				if (endVolume < volumeGoal && soundPlayer == primarySoundPlayer)
				{
					endVolume = volumeGoal;
				}

				if (endVolume < 1)
				{
					endVolume = 0;
					System.out.println(soundPlayer.toString() + ": stopping Truefading clip");
					soundPlayer.setLoopFading(false);
					soundPlayer.stopClip();
				}

				System.out.println(soundPlayer.toString() + ": TrueFading to: " + endVolume);
				soundPlayer.setVolumeLevel(endVolume);
				continue;
			}

			int finalVolume = currentVolume + changeRate;
			if (finalVolume > volumeGoal)
			{
					finalVolume = volumeGoal;
			}

			System.out.println(soundPlayer.toString() + ": Setting Vol to: " + finalVolume);
			soundPlayer.setVolumeLevel(finalVolume);
		}
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
				return 1;
		}
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
		ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

		if (trimNumber < array.size() / OBJ_ROTATION_CONSTANT)
		{
			trimNumber = array.size() / OBJ_ROTATION_CONSTANT;
		}

		if (trimNumber == 0)
		{
			trimNumber = 1;
		}

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
			ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

			clearWeatherObjects(weatherManager);
			weatherManager.stopManagerSoundPlayers();
		}

		weatherManagerList.clear();
	}

	public void handleWeatherChanges(WeatherManager weatherManager)
	{
		Condition weather = weatherManager.getWeatherType();

		if (weather == Condition.WEATHER_FOGGY && !config.enableFog())
		{
			return;
		}

		if (weather == Condition.WEATHER_COSMOS && !config.enableStars())
		{
			return;
		}

		if (!config.enableWintertodtSnow() && weather == Condition.WEATHER_SNOWING)
		{
			int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();
			if (playerChunk == WINTERTODT_CHUNK)
			{
				return;
			}
		}

		int maxWeatherObj = getMaxWeatherObjects(weather);
		ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

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

	public int getMaxWeatherObjects(Condition weatherCondition)
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
		}
	}

	private void renderWeather(int objects, WeatherManager weatherManager)
	{
		ArrayList<Tile> availableTiles = getAvailableTiles();
		int z = client.getPlane();
		ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

		Condition weatherCondition = weatherManager.getWeatherType();
		Animation weatherAnimation = getWeatherAnimation(weatherCondition);
		Model weatherModel = getWeatherModel(weatherCondition);

		for (int i = 0; i < objects; i++)
		{
			int roll = random.nextInt(availableTiles.size());
			Tile openTile = availableTiles.get(roll);

			RuneLiteObject runeLiteObject = createWeatherObject(weatherModel, weatherAnimation, openTile.getLocalLocation(), z);
			array.add(runeLiteObject);
			if (array.size() == getMaxWeatherObjects(weatherManager.getWeatherType()))
			{
				return;
			}
		}
	}

	public RuneLiteObject createWeatherObject(Model weatherModel, Animation weatherAnimation, LocalPoint lp, int z)
	{
		RuneLiteObject runeLiteObject = client.createRuneLiteObject();
		runeLiteObject.setModel(weatherModel);
		runeLiteObject.setAnimation(weatherAnimation);
		runeLiteObject.setLocation(lp, z);
		runeLiteObject.setShouldLoop(true);
		runeLiteObject.setActive(true);
		return runeLiteObject;
	}

	public void removeWeatherObject(int index, ArrayList<RuneLiteObject> weatherArray)
	{
		if (index >= weatherArray.size())
		{
			return;
		}

		RuneLiteObject runeLiteObject = weatherArray.get(index);
		runeLiteObject.setActive(false);
		weatherArray.remove(index);
	}

	public void clearWeatherObjects(WeatherManager weatherManager)
	{
		ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

		for (RuneLiteObject runeLiteObject : array)
		{
			runeLiteObject.setActive(false);
		}

		array.clear();
	}

	public void trimWeatherArray(WeatherManager weatherManager, int start, int end)
	{
		for (int i = start; i < end; i++)
		{
			removeWeatherObject(start, weatherManager.getWeatherObjArray());
		}
	}

	public void relocateObjects(WeatherManager weatherManager, int numToRelocate)
	{
		int z = client.getPlane();
		int beginRotation = weatherManager.getStartRotation();
		ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();

		for (int i = beginRotation; i < beginRotation + numToRelocate; i++)
		{
			ArrayList<Tile> availableTiles = getAvailableTiles();
			int roll = random.nextInt(availableTiles.size());
			Tile nextTile = availableTiles.get(roll);

			if (i >= array.size())
			{
				break;
			}

			RuneLiteObject runeLiteObject = array.get(i);
			runeLiteObject.setLocation(nextTile.getLocalLocation(), z);
			runeLiteObject.setAnimation(getWeatherAnimation(weatherManager.getWeatherType()));
		}

		weatherManager.setStartRotation(beginRotation + numToRelocate);
		Condition weather = weatherManager.getWeatherType();
		int maxWeatherObj = getMaxWeatherObjects(weather);
		if (beginRotation > maxWeatherObj)
		{
			weatherManager.setStartRotation(0);
		}
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
		for (WeatherManager weatherManager : weatherManagerList)
		{
			Condition weatherType = weatherManager.getWeatherType();
			ArrayList<RuneLiteObject> array = weatherManager.getWeatherObjArray();
			int size = (int) (array.size() * 0.8);
			clearWeatherObjects(weatherManager);
			if (weatherType == currentWeather)
			{
				renderWeather(size, weatherManager);
				zoneObjRecovery = 4;
			}
		}
	}

	public ArrayList<Tile> getAvailableTiles()
	{
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		byte[][][] settings = client.getTileSettings();
		int zLayer = client.getPlane();

		ArrayList<Tile> availableTiles = new ArrayList<>();
		for (int z = 0; z <= zLayer; z++)
		{
			for (int x = 0; x < Constants.SCENE_SIZE; ++x)
			{
				for (int y = 0; y < Constants.SCENE_SIZE; ++y)
				{
					Tile tile = tiles[z][x][y];

					if (tile == null)
					{
						continue;
					}

					int flag = settings[z][x][y];

					if ((flag & Constants.TILE_FLAG_UNDER_ROOF) == 0)
					{
						availableTiles.add(tile);
					}
				}
			}
		}
		return availableTiles;
	}

	public void setConfigWeather()
	{
		switch(config.weatherType())
		{
			case ASHFALL:
				currentWeather = Condition.WEATHER_ASHFALL;
				break;
			default:
			case DYNAMIC:
				currentWeather = syncWeather(currentSeason, currentBiome);
				break;
			case CLEAR:
				currentWeather = Condition.WEATHER_SUNNY;
				break;
			case FOG:
				currentWeather = Condition.WEATHER_FOGGY;
				break;
			case RAIN:
				currentWeather = Condition.WEATHER_RAINING;
				break;
			case SANDSTORM:
				currentWeather = Condition.WEATHER_SANDSTORM;
				break;
			case SNOW:
				currentWeather = Condition.WEATHER_SNOWING;
				break;
			case STARS:
				currentWeather = Condition.WEATHER_COSMOS;
				break;
			case STORM:
				currentWeather = Condition.WEATHER_STORM;
				break;
		}
	}

	public void loadModelsAnimations()
	{
		ModelData fogModelData = client.loadModelData(FOG_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		short fogFaceColour = fogModelData.getFaceColors()[0];
		short fogReplaceColour = JagexColor.packHSL(54, 0, 77);
		fogModel = fogModelData.scale(64, 128, 64).recolor(fogFaceColour, fogReplaceColour).light(200, ModelData.DEFAULT_CONTRAST, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);

		ModelData ashModelData = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices().scale(128, 160, 128).translate(0, 180, 0);
		short[] ashFaceColours = ashModelData.getFaceColors();
		short ashColour1 = JagexColor.packHSL(39, 1, 40);
		short ashColour2 = JagexColor.packHSL(39, 1, 40);
		ashModelData.recolor(ashFaceColours[0], ashColour1).recolor(ashFaceColours[2], ashColour2);
		ashModel = ashModelData.scale(192, 256, 192).translate(0, 420, 0).light();

		ModelData sandModelData = client.loadModelData(SAND_MODEL).cloneVertices();
		sandModel = sandModelData.scale(256, 256, 256).light(ModelData.DEFAULT_AMBIENT, 1200, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);

		ModelData snowModelData = client.loadModelData(SNOW_MODEL).cloneVertices();
		snowModel = snowModelData.scale(128, 192, 128).translate(0, 190, 0).light();

		ModelData rainModelData = client.loadModelData(RAIN_MODEL);
		rainModel = rainModelData.light();

		starModel = client.loadModel(STAR_MODEL);

		ashAnimation = client.loadAnimation(ASH_ANIMATION);
		fogAnimation = client.loadAnimation(FOG_ANIMATION);
		rainAnimation = client.loadAnimation(RAIN_ANIMATION);
		sandAnimation = client.loadAnimation(SAND_ANIMATION);
		snowAnimation = client.loadAnimation(SNOW_ANIMATION);
		starAnimation = client.loadAnimation(STAR_ANIMATION);
	}

	private Model getWeatherModel(Condition currentWeather)
	{
		switch (currentWeather)
		{
			case WEATHER_ASHFALL:
				return ashModel;
			case WEATHER_COSMOS:
				return starModel;
			case WEATHER_FOGGY:
				return fogModel;
			case WEATHER_SANDSTORM:
				return sandModel;
			case WEATHER_SNOWING:
				return snowModel;
			case WEATHER_RAINING:
			case WEATHER_STORM:
				return rainModel;
			default:
			case WEATHER_CLOUDY:
			case WEATHER_COVERED:
			case WEATHER_PARTLY_CLOUDY:
			case WEATHER_SUNNY:
				return null;
		}
	}

	private Animation getWeatherAnimation(Condition currentWeather)
	{
		switch (currentWeather)
		{
			case WEATHER_ASHFALL:
				return ashAnimation;
			case WEATHER_COSMOS:
				return starAnimation;
			case WEATHER_FOGGY:
				return fogAnimation;
			case WEATHER_SANDSTORM:
				return sandAnimation;
			case WEATHER_SNOWING:
				return snowAnimation;
			case WEATHER_RAINING:
			case WEATHER_STORM:
				return rainAnimation;
			default:
			case WEATHER_CLOUDY:
			case WEATHER_COVERED:
			case WEATHER_PARTLY_CLOUDY:
			case WEATHER_SUNNY:
				return null;
		}
	}

	private Condition syncWeather(Condition seasonCondition, Condition biomeCondition)
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
		return Condition.WEATHER_COVERED;
	}

	private Condition syncSeason()
	{
		switch (config.seasonType())
		{
			default:
			case DYNAMIC:
				switch ((CyclesClock.getTimeDays() / 7) % 4)
				{
					default:
					case 0:
						return Condition.SEASON_SPRING;
					case 1:
						return Condition.SEASON_SUMMER;
					case 2:
						return Condition.SEASON_AUTUMN;
					case 3:
						return Condition.SEASON_WINTER;
				}
			case SPRING:
				return Condition.SEASON_SPRING;
			case SUMMER:
				return Condition.SEASON_SUMMER;
			case AUTUMN:
				return Condition.SEASON_AUTUMN;
			case WINTER:
				return Condition.SEASON_WINTER;
		}
	}

	@Provides
    CyclesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CyclesConfig.class);
	}
}