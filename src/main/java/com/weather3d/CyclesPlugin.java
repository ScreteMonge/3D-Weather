package com.weather3d;

import com.google.inject.Provides;
import javax.inject.Inject;

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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
	private CyclesOverlay overlay;
	@Inject
	private SoundPlayer soundPlayer0 = new SoundPlayer();
	@Inject
	private SoundPlayer soundPlayer1 = new SoundPlayer();
	@Inject
	private SoundPlayer soundPlayer2 = new SoundPlayer();

	private ArrayList<RuneLiteObject> weatherObjectsArray = new ArrayList<>();
	private Model ashModel;
	private Model fogModel;
	private Model rainModel;
	private Model snowModel;
	private Model starModel;
	private Animation ashAnimation;
	private Animation fogAnimation;
	private Animation rainAnimation;
	private Animation snowAnimation;
	private Animation starAnimation;
	private final int ASH_MODEL = 27835;
	private final int ASH_ANIMATION = 7000;
	private final int FOG_MODEL = 29290;
	private final int FOG_ANIMATION = 4516;
	private final int RAIN_MODEL = 15524;
	private final int RAIN_ANIMATION = 7001;
	private final int SNOW_MODEL = 27835;
	private final int SNOW_ANIMATION = 7000;
	private final int STAR_MODEL = 16374;
	private final int STAR_ANIMATION = 7971;

	private boolean loadedAnimsModels = false;
	private int startRotation = 0;
	private final int CHANGE_CONSTANT = 50;
	private int relocationNum;
	private static final ZoneId JAGEX = ZoneId.of("Europe/London");
	private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("DDD:hh:mm");
	private int savedChunk = 0;
	private int savedZPlane = -1;
	private boolean conditionsSynced = false;
	@Getter
	private Condition currentSeason = Condition.SEASON_SPRING;
	@Getter
	private Condition currentBiome = Condition.BIOME_GRASSLAND;
	@Getter
	private Condition currentWeather = null;
	private boolean weatherTransitioning = false;
	private boolean startWeatherTransition = false;
	private final int VOL_CHANGE_CONSTANT = 2;
	private SoundPlayer[] soundPlayers = new SoundPlayer[3];

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
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invoke(this::clearWeatherObjects);
		overlayManager.remove(overlay);
		for (SoundPlayer soundPlayer : soundPlayers)
		{
			if (soundPlayer == null)
			{
				continue;
			}
			soundPlayer.stopClip();
		}
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
			conditionsSynced = true;

			soundPlayers[0] = soundPlayer0;
			soundPlayers[1] = soundPlayer1;
			soundPlayers[2] = soundPlayer2;
		}

		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (savedZPlane == -1)
		{
			savedZPlane = client.getPlane();
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
				weatherTransitioning = true;
				startWeatherTransition = true;
				currentWeather = nextWeather;
				handleAmbienceChanges();
			}

			conditionsSynced = true;
		}

		relocationNum = getMaxWeatherObjects(currentWeather) / 10;

		if (savedZPlane != client.getPlane())
		{
			transitionZPlane();
			savedZPlane = client.getPlane();
		}
		else if (weatherTransitioning)
		{
			handleWeatherTransition();
		}
		else
		{
			handleWeatherChanges();
		}

		if (!config.toggleAmbience())
		{
			return;
		}

		// Accounts for random errors causing drops in sound, restarting sound after logging out and relogging back in or toggling Toggle Ambience on
		if (currentWeather.isHasSound() && config.toggleAmbience() && config.ambientVolume() != 0)
		{
			boolean isPlaying = false;
			for (SoundPlayer soundPlayer : soundPlayers)
			{
				if (soundPlayer.isPlaying())
				{
					isPlaying = true;
				}
			}

			if (!isPlaying)
			{
				handleSoundPlayers(false, currentWeather.getSoundEffect(), 0);
			}
		}

		for (SoundPlayer soundPlayer : soundPlayers)
		{
			if (!soundPlayer.isPlaying())
			{
				continue;
			}

			if (!currentWeather.isHasSound() || currentWeather.getSoundEffect() != soundPlayer.getSavedSound())
			{
				soundPlayer.setFading(true);
			}

			int ticks = client.getTickCount();

			//Prevent soundplayers from starting before other things have been initialized, then be sure to loop them every 2 minutes or so/125 ticks
			if (soundPlayer.isLoop() && ticks > 3 && ticks % 125 == 0)
			{
				if (soundPlayer.isFading())
				{
					handleSoundPlayers(true, soundPlayer.getSavedSound(), soundPlayer.getCurrentVolume());
				}
				else
				{
					//Start next loop at low volume for natural build up as last soundplayer regresses - except at low volumes, where the jump is too noticeable
					int restartVolume = soundPlayer.getCurrentVolume() / 2;
					if (soundPlayer.getCurrentVolume() < 10)
					{
						restartVolume = soundPlayer.getCurrentVolume();
					}
					handleSoundPlayers(false, soundPlayer.getSavedSound(), restartVolume);
					break;
				}
			}
		}

		for (SoundPlayer soundPlayer : soundPlayers)
		{
			if (soundPlayer.isFading())
			{
				fadeVolume(soundPlayer);
			}
			else if (soundPlayer.getCurrentVolume() != soundPlayer.getEndVolume())
			{
				transitionVolume(soundPlayer);
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState gameState = event.getGameState();
		if (gameState == GameState.LOGIN_SCREEN || gameState == GameState.LOGIN_SCREEN_AUTHENTICATOR || gameState == GameState.STARTING)
		{
			for (SoundPlayer soundPlayer : soundPlayers)
			{
				if (soundPlayer == null)
				{
					continue;
				}
				soundPlayer.stopClip();
			}
		}

		if (gameState != GameState.LOGGED_IN)
		{
			return;
		}

		int playerChunk = client.getLocalPlayer().getWorldLocation().getRegionID();
		currentBiome = BiomeChunkMap.checkBiome(playerChunk);
		currentSeason = syncSeason();
		setConfigWeather();

		if (!currentWeather.isHasPrecipitation())
		{
			clearWeatherObjects();
			return;
		}

		handleAmbienceChanges();

		if (!weatherObjectsArray.isEmpty())
		{
			int size = weatherObjectsArray.size() / 2;
			clearWeatherObjects();
			renderWeather(size, getConfigModel(), getConfigAnimation(), weatherObjectsArray);
		}
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
			startRotation = 0;
			weatherTransitioning = true;
			startWeatherTransition = true;

			handleAmbienceChanges();
			return;
		}

		if (event.getKey().equals("seasonType"))
		{
			currentSeason = syncSeason();
		}

		if (event.getKey().equals("toggleOverlay"))
		{
			if (config.toggleOverlay())
			{
				overlayManager.add(overlay);
				return;
			}

			overlayManager.remove(overlay);
		}

		if (event.getKey().equals("toggleAmbience"))
		{
			if (!config.toggleAmbience())
			{
				for (SoundPlayer soundPlayer : soundPlayers)
				{
					soundPlayer.stopClip();
					soundPlayer.setLoop(false);
				}
			}
		}

		if (event.getKey().equals("ambientVolume"))
		{
			if (!config.toggleAmbience())
			{
				return;
			}

			for (SoundPlayer soundPlayer : soundPlayers)
			{
				if (!soundPlayer.isPlaying())
				{
					continue;
				}

				if (soundPlayer.isFading())
				{
					soundPlayer.stopClip();
					continue;
				}

				int endVolume = soundPlayer.getEndVolume();
				soundPlayer.setVolumeLevel(endVolume);
			}
		}
	}

	public void handleWeatherChanges()
	{
		if (client.getTickCount() % 2 != 0)
		{
			return;
		}

		if (currentWeather.isHasPrecipitation())
		{
			int maxObjects = getMaxWeatherObjects(currentWeather);
			if (weatherObjectsArray.size() < maxObjects)
			{
				renderWeather(CHANGE_CONSTANT, getConfigModel(), getConfigAnimation(), weatherObjectsArray);
			}
			else if (weatherObjectsArray.size() == maxObjects)
			{
				if (client.getTickCount() % currentWeather.getChangeRate() == 0)
				{
					relocateObjects(relocationNum);
					if (startRotation >= getMaxWeatherObjects(currentWeather))
					{
						startRotation = 0;
					}
				}
			}
			else
			{
				trimWeatherArray(maxObjects, maxObjects + CHANGE_CONSTANT);
			}

		}
		else if (!weatherObjectsArray.isEmpty())
		{
			trimWeatherArray(0, CHANGE_CONSTANT);
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
			case HIGHEST:
				return weatherCondition.getObjHighest();
		}
	}

	private void renderWeather(int objects, Model weatherModel, Animation weatherAnimation, ArrayList<RuneLiteObject> weatherArray)
	{
		ArrayList<Tile> availableTiles = getAvailableTiles();
		int z = client.getPlane();

		for (int i = 0; i < objects; i++)
		{
			int rollTile = (int) (Math.random() * availableTiles.size());
			Tile openTile = availableTiles.get(rollTile);

			RuneLiteObject runeLiteObject = createWeatherObject(weatherModel, weatherAnimation, openTile.getLocalLocation(), z);
			weatherArray.add(runeLiteObject);
		}
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

	public void clearWeatherObjects()
	{
		for (RuneLiteObject runeLiteObject : weatherObjectsArray)
		{
			runeLiteObject.setActive(false);
		}
		weatherObjectsArray.clear();
	}

	public void trimWeatherArray(int start, int end)
	{
		for (int i = start; i < end; i++)
		{
			removeWeatherObject(start, weatherObjectsArray);
		}
	}

	public void relocateObjects(int numToRelocate)
	{
		int z = client.getPlane();

		for (int i = startRotation; i < startRotation + numToRelocate; i++)
		{
			ArrayList<Tile> availableTiles = getAvailableTiles();
			int roll = (int) (Math.random() * availableTiles.size());
			Tile nextTile = availableTiles.get(roll);

			if (i >= weatherObjectsArray.size())
			{
				break;
			}

			RuneLiteObject runeLiteObject = weatherObjectsArray.get(i);
			runeLiteObject.setLocation(nextTile.getLocalLocation(), z);

			if (currentWeather.isHasPrecipitation())
			{
				runeLiteObject.setAnimation(getConfigAnimation());
				runeLiteObject.setModel(getConfigModel());
			}
		}
		startRotation += numToRelocate;
	}

	public void handleWeatherTransition()
	{
		if (startWeatherTransition)
		{
			startRotation = 0;
			startWeatherTransition = false;
		}

		if (client.getTickCount() % 2 == 1)
		{
			return;
		}

		int maxObjects = getMaxWeatherObjects(currentWeather);

		if (currentWeather.isHasPrecipitation())
		{
			if (startRotation == maxObjects || startRotation >= weatherObjectsArray.size())
			{
				weatherTransitioning = false;
				startRotation = 0;
				return;
			}

			relocateObjects(CHANGE_CONSTANT);
		}
		else
		{
			weatherTransitioning = false;
		}
	}

	public void transitionZPlane()
	{
		if (!currentWeather.isHasPrecipitation())
		{
			clearWeatherObjects();
			return;
		}

		if (!weatherObjectsArray.isEmpty())
		{
			int size = weatherObjectsArray.size();
			clearWeatherObjects();
			renderWeather(size, getConfigModel(), getConfigAnimation(), weatherObjectsArray);
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

	public void handleSoundPlayers(boolean isFading, SoundEffect soundEffect, int volume)
	{
		fadeSoundPlayers();

		if (soundPlayer0.isPlaying())
		{
			if (soundPlayer1.isPlaying())
			{
				if (soundPlayer2.isPlaying())
				{
					soundPlayer0.playClip(soundEffect, volume);
					soundPlayer0.setFading(isFading);
					soundPlayer0.setLoop(true);
				}
				else
				{
					soundPlayer2.playClip(soundEffect, volume);
					soundPlayer2.setFading(isFading);
					soundPlayer2.setLoop(true);
				}
			}
			else
			{
				soundPlayer1.playClip(soundEffect, volume);
				soundPlayer1.setFading(isFading);
				soundPlayer1.setLoop(true);
			}
		}
		else
		{
			soundPlayer0.playClip(soundEffect, volume);
			soundPlayer0.setFading(isFading);
			soundPlayer0.setLoop(true);
		}
	}

	public void handleAmbienceChanges()
	{
		if (!config.toggleAmbience())
		{
			return;
		}

		if (!currentWeather.isHasSound())
		{
			fadeSoundPlayers();
			return;
		}

		handleSoundPlayers(false, currentWeather.getSoundEffect(), 0);
	}

	public void fadeSoundPlayers()
	{
		if (!conditionsSynced)
		{
			return;
		}

		for (SoundPlayer soundPlayer : soundPlayers)
		{
			soundPlayer.setFading(true);
		}
	}

	public void transitionVolume(SoundPlayer soundPlayer)
	{
		if (!soundPlayer.isPlaying())
		{
			return;
		}

		int currentVol = soundPlayer.getCurrentVolume();
		int endVol = soundPlayer.getEndVolume();
		int change = endVol - currentVol;

		if (change == 0)
		{
			return;
		}

		if (change > 1)
		{
			soundPlayer.setVolumeLevel(currentVol + VOL_CHANGE_CONSTANT);
		}
		else if (change == 1)
		{
			soundPlayer.setVolumeLevel(currentVol + 1);
		}
		else if (change < -1)
		{
			soundPlayer.setVolumeLevel(currentVol - VOL_CHANGE_CONSTANT);
		}
		else
		{
			soundPlayer.setVolumeLevel(currentVol - 1);
		}
	}

	public void fadeVolume(SoundPlayer soundPlayer)
	{
		int currentVol = soundPlayer.getCurrentVolume();

		if (currentVol == 0)
		{
			soundPlayer.stopClip();
			soundPlayer.setFading(false);
			soundPlayer.setLoop(false);
			return;
		}

		soundPlayer.setVolumeLevel(currentVol - VOL_CHANGE_CONSTANT);
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

		ModelData ashModelData = client.loadModelData(ASH_MODEL).cloneColors();
		short[] ashFaceColours = ashModelData.getFaceColors();
		short[] ashReplaceColours = new short[384];
		short ashColour1 = JagexColor.packHSL(39, 1, 40);
		ashReplaceColours[0] = ashColour1;
		short ashColour2 = JagexColor.packHSL(39, 1, 40);
		ashReplaceColours[2] = ashColour2;
		ashModel = client.loadModel(ASH_MODEL, ashFaceColours, ashReplaceColours);

		rainModel = client.loadModel(RAIN_MODEL);
		snowModel = client.loadModel(SNOW_MODEL);
		starModel = client.loadModel(STAR_MODEL);

		ashAnimation = client.loadAnimation(ASH_ANIMATION);
		fogAnimation = client.loadAnimation(FOG_ANIMATION);
		rainAnimation = client.loadAnimation(RAIN_ANIMATION);
		snowAnimation = client.loadAnimation(SNOW_ANIMATION);
		starAnimation = client.loadAnimation(STAR_ANIMATION);
	}

	public Model getConfigModel()
	{
		Model weatherModel;
		switch (config.weatherType())
		{
			default:
			case DYNAMIC:
				weatherModel = getDynamicWeatherModel(currentWeather);
				break;
			case ASHFALL:
				weatherModel = ashModel;
				break;
			case FOG:
				weatherModel = fogModel;
				break;
			case RAIN:
			case STORM:
				weatherModel = rainModel;
				break;
			case SNOW:
				weatherModel = snowModel;
				break;
			case STARS:
				weatherModel = starModel;
				break;
		}
		return weatherModel;
	}

	private Model getDynamicWeatherModel(Condition currentWeather)
	{
		switch (currentWeather)
		{
			case WEATHER_ASHFALL:
				return ashModel;
			case WEATHER_COSMOS:
				return starModel;
			case WEATHER_FOGGY:
				return fogModel;
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

	public Animation getConfigAnimation()
	{
		Animation weatherAnimation;
		switch (config.weatherType())
		{
			default:
			case DYNAMIC:
				weatherAnimation = getDynamicWeatherAnimation(currentWeather);
				break;
			case ASHFALL:
				weatherAnimation = ashAnimation;
				break;
			case FOG:
				weatherAnimation = fogAnimation;
				break;
			case RAIN:
			case STORM:
				weatherAnimation = rainAnimation;
				break;
			case SNOW:
				weatherAnimation = snowAnimation;
				break;
			case STARS:
				weatherAnimation = starAnimation;
				break;
		}
		return weatherAnimation;
	}

	private Animation getDynamicWeatherAnimation(Condition currentWeather)
	{
		switch (currentWeather)
		{
			case WEATHER_ASHFALL:
				return ashAnimation;
			case WEATHER_COSMOS:
				return starAnimation;
			case WEATHER_FOGGY:
				return fogAnimation;
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
		int totalMin = getTimeHours() * 60 + getTimeMinutes();
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
				switch ((getTimeDays() / 7) % 4)
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

	private int getTimeMinutes()
	{
		String currentTime = getJagexTime();
		String[] currentTimeArray = currentTime.split(":");
		return Integer.valueOf(currentTimeArray[2]);
	}

	private int getTimeHours()
	{
		String currentTime = getJagexTime();
		String[] currentTimeArray = currentTime.split(":");
		return Integer.valueOf(currentTimeArray[1]);
	}

	private int getTimeDays()
	{
		String currentTime = getJagexTime();
		String[] currentTimeArray = currentTime.split(":");
		return Integer.valueOf(currentTimeArray[0]);
	}

	public String getJagexTime()
	{
		LocalDateTime time = LocalDateTime.now(JAGEX);
		return time.format(TIME_FORMAT);
	}

	@Provides
    CyclesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CyclesConfig.class);
	}
}
