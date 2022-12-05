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
	private OverlayManager overlayManager;
	@Inject
	private CyclesOverlay cyclesOverlay;
	@Inject
	private LightningOverlay lightningOverlay;

	private final ArrayList<WeatherManager> weatherManagerList = new ArrayList<>();
	private Model ashModel;
	private Model ashModel2;
	private Model ashModel3;
	private Model cloudModel;
	private Model cloudModel2;
	private Model cloudModel3;
	private Model fogModel;
	private Model partlyCloudyModel;
	private Model partlyCloudyModel2;
	private Model partlyCloudyModel3;
	private Model rainModel;
	private Model rainModel2;
	private Model rainModel3;
	private Model sandModel;
	private Model snowModel;
	private Model snowModel2;
	private Model snowModel3;
	private Model starModel;
	private Model starModel2;
	private Model starModel3;
	private Model stormModel;
	private Model stormModel2;
	private Model stormModel3;
	private Animation ashAnimation;
	private Animation cloudAnimation;
	private Animation fogAnimation;
	private Animation rainAnimation;
	private Animation sandAnimation;
	private Animation snowAnimation;
	private Animation starAnimation;
	private final int ASH_MODEL = 27835;
	private final int ASH_ANIMATION = 7000;
	private final int CLOUD_MODEL = 4086;
	private final int CLOUD_ANIMATION = 6470;
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
	public boolean flashLightning = false;
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
			overlayManager.add(cyclesOverlay);
			overlayManager.add(lightningOverlay);
		}

		if (client.getLocalPlayer() != null)
		{
			int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();
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
			syncBiome();
			syncSeason();
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
			return;
		}

		if (gameState != GameState.LOGGED_IN)
		{
			return;
		}

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
		{
			return;
		}

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

		if (event.getKey().equals("disableWeatherUnderground"))
		{
			if (!config.disableWeatherUnderground())
			{
				return;
			}

			if (currentBiome == Condition.BIOME_CAVE || currentBiome == Condition.BIOME_LAVA_CAVE)
			{
				clientThread.invoke(this::clearAllWeatherManagers);
			}
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
			{
				return;
			}

			for (WeatherManager weatherManager : weatherManagerList)
			{
				if (weatherManager.getWeatherType() == Condition.WEATHER_CLOUDY || weatherManager.getWeatherType() == Condition.WEATHER_PARTLY_CLOUDY)
				{
					clientThread.invoke(() -> clearWeatherObjects(weatherManager));
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

		if (!weatherCondition.isHasSound() || !config.toggleAmbience())
		{
			return;
		}

		if (config.weatherType() != CyclesConfig.WeatherType.DYNAMIC && config.disableWeatherUnderground() && (currentBiome == Condition.BIOME_CAVE || currentBiome == Condition.BIOME_LAVA_CAVE))
		{
			return;
		}

		int soundTimer = weatherManager.getSoundPlayerTimer();
		soundTimer++;
		weatherManager.setSoundPlayerTimer(soundTimer);
		SoundPlayer primarySoundPlayer = weatherManager.getPrimarySoundPlayer();

		if (soundTimer >= 230)
		{
			weatherManager.setSoundPlayerTimer(0);
			primarySoundPlayer.setLoopFading(true);
			weatherManager.switchSoundPlayerPriority();
		}

		if (weatherCondition == Condition.WEATHER_STORM && (soundTimer == 90 || soundTimer == 138))
		{
			flashLightning = true;
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
					soundPlayer.stopClip();
					soundPlayer.setLoopFading(false);
					endVolume = 0;
				}
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
					soundPlayer.setLoopFading(false);
					soundPlayer.stopClip();
				}

				soundPlayer.setVolumeLevel(endVolume);
				continue;
			}

			int finalVolume = currentVolume + changeRate;
			if (finalVolume > volumeGoal)
			{
					finalVolume = volumeGoal;
			}

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
			clearWeatherObjects(weatherManager);
			weatherManager.stopManagerSoundPlayers();
		}
		weatherManagerList.clear();
	}

	public void handleWeatherChanges(WeatherManager weatherManager)
	{
		Condition weather = weatherManager.getWeatherType();

		if (config.weatherType() != CyclesConfig.WeatherType.DYNAMIC && config.disableWeatherUnderground() && (currentBiome == Condition.BIOME_CAVE || currentBiome == Condition.BIOME_LAVA_CAVE))
		{
			return;
		}

		if ((weather == Condition.WEATHER_CLOUDY || weather == Condition.WEATHER_PARTLY_CLOUDY) && !config.enableClouds())
		{
			return;
		}

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
		int alternate = 1;

		for (int i = 0; i < objects; i++)
		{
			int roll = random.nextInt(availableTiles.size());
			Tile openTile = availableTiles.get(roll);

			Model weatherModel = getWeatherModel(weatherCondition, alternate);
			alternate += 1;
			if (alternate > weatherCondition.getModelVariety())
			{
				alternate = 1;
			}

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
		Condition weather = weatherManager.getWeatherType();

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
			runeLiteObject.setAnimation(getWeatherAnimation(weather));
		}

		weatherManager.setStartRotation(beginRotation + numToRelocate);

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
		if (config.weatherType() != CyclesConfig.WeatherType.DYNAMIC && config.disableWeatherUnderground() && (currentBiome == Condition.BIOME_CAVE || currentBiome == Condition.BIOME_LAVA_CAVE))
		{
			clientThread.invoke(this::clearAllWeatherManagers);
			return;
		}

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
			case CLOUDY:
				currentWeather = Condition.WEATHER_CLOUDY;
				break;
			case CLEAR:
				currentWeather = Condition.WEATHER_SUNNY;
				break;
			case FOGGY:
				currentWeather = Condition.WEATHER_FOGGY;
				break;
			case PARTLY_CLOUDY:
				currentWeather = Condition.WEATHER_PARTLY_CLOUDY;
				break;
			case RAINY:
				currentWeather = Condition.WEATHER_RAINING;
				break;
			case SNOWY:
				currentWeather = Condition.WEATHER_SNOWING;
				break;
			case STARRY:
				currentWeather = Condition.WEATHER_COSMOS;
				break;
			case STORMY:
				currentWeather = Condition.WEATHER_STORM;
				break;
		}
	}

	public void loadModelsAnimations()
	{
		ModelData ashModelData = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices();
		ModelData ashModelData2 = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices();
		ModelData ashModelData3 = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices();
		short[] ashFaceColours = ashModelData.getFaceColors();
		short[] ashFaceColours2 = ashModelData2.getFaceColors();
		short[] ashFaceColours3 = ashModelData3.getFaceColors();
		short ashColour1 = JagexColor.packHSL(39, 1, 40);
		short ashColour2 = JagexColor.packHSL(39, 1, 40);
		ashModel = ashModelData.scale(128, 192, 128).translate(0, 180, 0).recolor(ashFaceColours[0], ashColour1).recolor(ashFaceColours[2], ashColour2).light();
		ashModel2 = ashModelData2.scale(128, 192, 128).translate(0, 180, 0).recolor(ashFaceColours2[0], ashColour1).recolor(ashFaceColours2[2], ashColour2).rotateY90Ccw().light();
		ashModel3 = ashModelData3.scale(128, 192, 128).translate(0, 180, 0).recolor(ashFaceColours3[0], ashColour1).recolor(ashFaceColours3[2], ashColour2).rotateY270Ccw().light();

		ModelData cloudModelData = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		ModelData cloudModelData2 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		ModelData cloudModelData3 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		short cloudFaceColour = cloudModelData.getFaceColors()[0];
		short cloudReplaceColour = JagexColor.packHSL(54, 0, 90);
		cloudModel = cloudModelData.scale(90, 90, 90).translate(0, -800, 0).recolor(cloudFaceColour, cloudReplaceColour).light();
		cloudModel2 = cloudModelData2.scale(100, 100, 100).translate(0, -900, 0).recolor(cloudFaceColour, cloudReplaceColour).rotateY90Ccw().light();
		cloudModel3 = cloudModelData3.scale(80, 80, 80).translate(0, -700, 0).recolor(cloudFaceColour, cloudReplaceColour).rotateY180Ccw().light();

		ModelData partlyCloudModelData = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		ModelData partlyCloudModelData2 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		ModelData partlyCloudModelData3 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		short partlyCloudFaceColour = partlyCloudModelData.getFaceColors()[0];
		short partlyCloudReplaceColour = JagexColor.packHSL(54, 0, JagexColor.LUMINANCE_MAX);
		partlyCloudyModel = partlyCloudModelData.scale(90, 90, 90).translate(0, -800, 0).recolor(partlyCloudFaceColour, partlyCloudReplaceColour).light();
		partlyCloudyModel2 = partlyCloudModelData2.scale(100, 100, 100).translate(0, -900, 0).recolor(partlyCloudFaceColour, partlyCloudReplaceColour).rotateY90Ccw().light();
		partlyCloudyModel3 = partlyCloudModelData3.scale(80, 80, 80).translate(0, -700, 0).recolor(partlyCloudFaceColour, partlyCloudReplaceColour).rotateY180Ccw().light();

		ModelData fogModelData = client.loadModelData(FOG_MODEL).cloneVertices().cloneColors().cloneTransparencies();
		short fogFaceColour = fogModelData.getFaceColors()[0];
		short fogReplaceColour = JagexColor.packHSL(54, 0, 77);
		fogModel = fogModelData.scale(512, 256, 512).recolor(fogFaceColour, fogReplaceColour).light(200, ModelData.DEFAULT_CONTRAST, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);

		ModelData sandModelData = client.loadModelData(SAND_MODEL).cloneVertices();
		sandModel = sandModelData.scale(256, 256, 256).light(ModelData.DEFAULT_AMBIENT, 1200, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);

		ModelData snowModelData = client.loadModelData(SNOW_MODEL).cloneVertices();
		ModelData snowModelData2 = client.loadModelData(SNOW_MODEL).cloneVertices();
		ModelData snowModelData3 = client.loadModelData(SNOW_MODEL).cloneVertices();
		snowModel = snowModelData.scale(128, 192, 128).translate(0, 190, 0).light();
		snowModel2 = snowModelData2.scale(128, 192, 128).translate(0, 190, 0).rotateY90Ccw().light();
		snowModel3 = snowModelData3.scale(128, 192, 128).translate(0, 190, 0).rotateY270Ccw().light();

		ModelData rainModelData = client.loadModelData(RAIN_MODEL).cloneVertices();
		ModelData rainModelData2 = client.loadModelData(RAIN_MODEL).cloneVertices();
		ModelData rainModelData3 = client.loadModelData(RAIN_MODEL).cloneVertices();
		short[] rainFaceColours = rainModelData.getFaceColors();
		short[] rainFaceColours2 = rainModelData2.getFaceColors();
		short[] rainFaceColours3 = rainModelData3.getFaceColors();
		short rainRippleColour = JagexColor.packHSL(32, 1, JagexColor.LUMINANCE_MAX);
		short rainDropColour = JagexColor.packHSL(32, 1, 120);
		rainModel = rainModelData.scale(100, 256, 100).recolor(rainFaceColours[0], rainRippleColour).recolor(rainFaceColours[23], rainDropColour).light();
		rainModel2 = rainModelData2.scale(90, 256, 90).recolor(rainFaceColours2[0], rainRippleColour).recolor(rainFaceColours2[23], rainDropColour).rotateY90Ccw().light();
		rainModel3 = rainModelData3.scale(110, 256, 110).recolor(rainFaceColours3[0], rainRippleColour).recolor(rainFaceColours3[23], rainDropColour).rotateY270Ccw().light();

		ModelData stormModelData = client.loadModelData(RAIN_MODEL).cloneColors().cloneVertices();
		ModelData stormModelData2 = client.loadModelData(RAIN_MODEL).cloneColors().cloneVertices();
		ModelData stormModelData3 = client.loadModelData(RAIN_MODEL).cloneColors().cloneVertices();
		short[] stormFaceColours = stormModelData.getFaceColors();
		short[] stormFaceColours2 = stormModelData2.getFaceColors();
		short[] stormFaceColours3 = stormModelData3.getFaceColors();
		short stormRippleColour = JagexColor.packHSL(38, 1, 110);
		short stormDropColour = JagexColor.packHSL(38, 2, 105);
		stormModel = stormModelData.scale(110, 410, 110).recolor(stormFaceColours[0], stormRippleColour).recolor(stormFaceColours[23], stormDropColour).light();
		stormModel2 = stormModelData2.scale(100, 410, 100).recolor(stormFaceColours2[0], stormRippleColour).recolor(stormFaceColours2[23], stormDropColour).rotateY90Ccw().light();
		stormModel3 = stormModelData3.scale(120, 410, 120).recolor(stormFaceColours3[0], stormRippleColour).recolor(stormFaceColours3[23], stormDropColour).rotateY90Ccw().light();

		ModelData starModelData = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
		ModelData starModelData2 = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
		ModelData starModelData3 = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
		short[] starFaceColours = starModelData.getFaceColors();
		short[] starFaceColours2 = starModelData2.getFaceColors();
		short[] starFaceColours3 = starModelData3.getFaceColors();
		short starShellReplaceColour = JagexColor.packHSL(10, 2, 60);
		short starInsideReplaceColour = JagexColor.packHSL(10, 4, 80);
		starModel = starModelData.scale(80, 80, 80).translate(0, -800, 0).recolor(starFaceColours[0], starShellReplaceColour).recolor(starFaceColours[45], starInsideReplaceColour).light();
		starModel2 = starModelData2.scale(65, 65, 65).translate(0, -900, 0).recolor(starFaceColours2[0], starShellReplaceColour).recolor(starFaceColours2[45], starInsideReplaceColour).light();
		starModel3 = starModelData3.scale(95, 95, 95).translate(0, -700, 0).recolor(starFaceColours3[0], starShellReplaceColour).recolor(starFaceColours3[45], starInsideReplaceColour).light();

		ashAnimation = client.loadAnimation(ASH_ANIMATION);
		cloudAnimation = client.loadAnimation(CLOUD_ANIMATION);
		fogAnimation = client.loadAnimation(CLOUD_ANIMATION);
		rainAnimation = client.loadAnimation(RAIN_ANIMATION);
		sandAnimation = client.loadAnimation(SAND_ANIMATION);
		snowAnimation = client.loadAnimation(SNOW_ANIMATION);
		starAnimation = client.loadAnimation(STAR_ANIMATION);
	}

	private Model getWeatherModel(Condition currentWeather, int alternative)
	{
		switch (currentWeather)
		{
			case WEATHER_ASHFALL:
				switch(alternative)
				{
					default:
					case 1:
						return ashModel;
					case 2:
						return ashModel2;
					case 3:
						return ashModel3;
				}
			case WEATHER_CLOUDY:
				switch(alternative)
				{
					default:
					case 1:
						return cloudModel;
					case 2:
						return cloudModel2;
					case 3:
						return cloudModel3;
				}
			case WEATHER_COSMOS:
				switch(alternative)
				{
					default:
					case 1:
						return starModel;
					case 2:
						return starModel2;
					case 3:
						return starModel3;
				}
			case WEATHER_FOGGY:
				return fogModel;
			case WEATHER_PARTLY_CLOUDY:
				switch (alternative)
				{
					default:
					case 1:
						return partlyCloudyModel;
					case 2:
						return partlyCloudyModel2;
					case 3:
						return partlyCloudyModel3;
				}
			case WEATHER_SANDSTORM:
				return sandModel;
			case WEATHER_SNOWING:
				switch (alternative)
				{
					default:
					case 1:
						return snowModel;
					case 2:
						return snowModel2;
					case 3:
						return snowModel3;
				}
			case WEATHER_RAINING:
				switch (alternative)
				{
					default:
					case 1:
						return rainModel;
					case 2:
						return rainModel2;
					case 3:
						return rainModel3;
				}
			case WEATHER_STORM:
				switch (alternative)
				{
					default:
					case 1:
						return stormModel;
					case 2:
						return stormModel2;
					case 3:
						return stormModel3;
				}
			default:
			case WEATHER_COVERED:
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
			case WEATHER_CLOUDY:
			case WEATHER_PARTLY_CLOUDY:
				return cloudAnimation;
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
			case WEATHER_COVERED:
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

	private void syncBiome()
	{
		WorldPoint wp = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation(), client.getPlane());

		int playerChunk = wp.getRegionID();
		if (savedChunk != playerChunk)
		{
			currentBiome = BiomeChunkMap.checkBiome(playerChunk);
			savedChunk = playerChunk;
		}
	}

	private void syncSeason()
	{
		switch (config.seasonType())
		{
			default:
			case DYNAMIC:
				switch ((CyclesClock.getTimeDays() / 7) % 4)
				{
					default:
					case 0:
						currentSeason = Condition.SEASON_SPRING;
						return;
					case 1:
						currentSeason = Condition.SEASON_SUMMER;
						return;
					case 2:
						currentSeason = Condition.SEASON_AUTUMN;
						return;
					case 3:
						currentSeason = Condition.SEASON_WINTER;
						return;
				}
			case SPRING:
				currentSeason =  Condition.SEASON_SPRING;
				return;
			case SUMMER:
				currentSeason =  Condition.SEASON_SUMMER;
				return;
			case AUTUMN:
				currentSeason =  Condition.SEASON_AUTUMN;
				return;
			case WINTER:
				currentSeason =  Condition.SEASON_WINTER;
		}
	}

	@Provides
    CyclesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CyclesConfig.class);
	}
}