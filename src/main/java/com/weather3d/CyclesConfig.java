package com.weather3d;

import net.runelite.client.config.*;

@ConfigGroup("3Dweather")
public interface CyclesConfig extends Config {

	enum WeatherType {
		DYNAMIC,
		ASHFALL,
		CLEAR,
		CLOUDY,
		FOGGY,
		PARTLY_CLOUDY,
		RAINY,
		SNOWY,
		STARRY,
		STORMY
	}

	enum SeasonType {
		DYNAMIC,
		HD_117,
		SPRING,
		SUMMER,
		AUTUMN,
		WINTER
	}

	@ConfigSection(
			name = "General",
			description = "General settings",
			position = 0
	)
	String generalSettings = "generalSettings";

	@ConfigItem(
			keyName = "weatherType",
			name = "Weather Type",
			description = "Determines the current Weather (Dynamic automatically cycles based on Season and Biome)",
			section = generalSettings,
			position = 1
	)
	default WeatherType weatherType()
	{
		return WeatherType.DYNAMIC;
	}

	@ConfigItem(
			keyName = "seasonType",
			name = "Season Type",
			description = "Determines the current Season and its Weather cycle (only relevant if Weather Type is Dynamic)",
			section = generalSettings,
			position = 2
	)
	default SeasonType seasonType()
	{
		return SeasonType.DYNAMIC;
	}

	@ConfigItem(
			keyName = "toggleOverlay",
			name = "Enable Overlay",
			description = "Displays an overlay indicating Weather, Biome, and Season",
			section = generalSettings,
			position = 3
	)
	default boolean toggleOverlay()
	{
		return true;
	}

	@ConfigItem(
			keyName = "miniOverlay",
			name = "Enable Mini Overlay",
			description = "Displays a mini overlay indicating Weather, Biome, and Season",
			section = generalSettings,
			position = 4
	)
	default boolean miniOverlay()
	{
		return false;
	}

	@ConfigItem(
			keyName = "disableWeatherUnderground",
			name = "Disable Weather Underground",
			description = "Prevents Weather from occurring while in Cave or Lava Cave Biomes, regardless of the set Weather Type",
			section = generalSettings,
			position = 5
	)
	default boolean disableWeatherUnderground()
	{
		return true;
	}

	@ConfigSection(
			name = "Audio",
			description = "Audio settings",
			position = 6
	)
	String audioSettings = "audioSettings";

	@ConfigItem(
			keyName = "toggleAmbience",
			name = "Enable Ambience",
			description = "Toggles ambient Weather sounds on/off",
			section = audioSettings,
			position = 7
	)
	default boolean toggleAmbience()
	{
		return true;
	}

	@ConfigItem(
			keyName = "ambientVolume",
			name = "Ambient Volume",
			description = "Sets the volume of ambient Weather sounds",
			section = audioSettings,
			position = 8
	)
	@Units(Units.PERCENT)
	@Range(max = 100)
	default int ambientVolume()
	{
		return 50;
	}

	@ConfigItem(
			keyName = "muffledVolume",
			name = "Muffled Volume",
			description = "Sets the volume of muffled Weather sounds while indoors",
			section = audioSettings,
			position = 9
	)
	@Units(Units.PERCENT)
	@Range(max = 100)
	default int muffledVolume()
	{
		return 50;
	}

	@ConfigItem(
			keyName = "disableIndoorMuffling",
			name = "Disable Indoor Muffling",
			description = "Prevents ambience sound from being muffled while standing indoors.",
			section = audioSettings,
			position = 10
	)
	default boolean disableIndoorMuffling()
	{
		return false;
	}

	@ConfigSection(
			name = "Weathers",
			description = "Weather Options",
			position = 11
	)
	String weatherToggles = "weatherToggles";

	@ConfigItem(
			keyName = "enableRain",
			name = "Rain Enabled",
			description = "Allows Rain objects to appear while Rainy or Stormy",
			section = weatherToggles,
			position = 12
	)
	default boolean enableRain()
	{
		return true;
	}

	//set mins?

	@ConfigItem(
			keyName = "rainDensity",
			name = "Rain Density",
			description = "Sets the number of Rain objects that spawn while Rainy",
			section = weatherToggles,
			position = 13
	)
	@Range(max = 2000)
	default int rainDensity()
	{
		return 400;
	}

	@ConfigItem(
			keyName = "stormDensity",
			name = "Storm Density",
			description = "Sets the number of Rain objects that spawn while Stormy",
			section = weatherToggles,
			position = 14
	)
	@Range(max = 3000)
	default int stormDensity()
	{
		return 600;
	}

	@ConfigItem(
			keyName = "enableSnow",
			name = "Snow Enabled",
			description = "Allows Snow objects to appear while Snowy",
			section = weatherToggles,
			position = 15
	)
	default boolean enableSnow()
	{
		return true;
	}

	@ConfigItem(
			keyName = "snowDensity",
			name = "Snow Density",
			description = "Sets the number of Snow objects that spawn while Snowy",
			section = weatherToggles,
			position = 16
	)
	@Range(max = 2000)
	default int snowDensity()
	{
		return 400;
	}

	@ConfigItem(
			keyName = "enableClouds",
			name = "Clouds Enabled",
			description = "Allows Cloud objects to appear while Cloudy or Partly Cloudy",
			section = weatherToggles,
			position = 17
	)
	default boolean enableClouds()
	{
		return true;
	}

	@ConfigItem(
			keyName = "cloudyDensity",
			name = "Cloud Density",
			description = "Sets the number of Cloud objects that spawn while Cloudy",
			section = weatherToggles,
			position = 18
	)
	@Range(max = 1000)
	default int cloudyDensity()
	{
		return 200;
	}

	@ConfigItem(
			keyName = "partlyCloudyDensity",
			name = "Partly Cloudy Density",
			description = "Sets the number of Cloud objects that spawn while Partly Cloudy",
			section = weatherToggles,
			position = 19
	)
	@Range(max = 300)
	default int partlyCloudyDensity()
	{
		return 50;
	}

	@ConfigItem(
			keyName = "enableAsh",
			name = "Ash Enabled",
			description = "Allows Ash objects to appear while in Ashfall",
			section = weatherToggles,
			position = 20
	)
	default boolean enableAsh()
	{
		return false;
	}

	@ConfigItem(
			keyName = "ashfallDensity",
			name = "Ashfall Density",
			description = "Sets the number of Ash objects that spawn while in Ashfall",
			section = weatherToggles,
			position = 21
	)
	@Range(max = 1200)
	default int ashfallDensity()
	{
		return 200;
	}

	@ConfigItem(
			keyName = "enableFog",
			name = "Fog Enabled",
			description = "Allows Fog objects to appear while Foggy",
			section = weatherToggles,
			position = 22
	)
	default boolean enableFog()
	{
		return false;
	}

	@ConfigItem(
			keyName = "foggyDensity",
			name = "Fog Density",
			description = "Sets the number of Fog objects that spawn while Foggy",
			section = weatherToggles,
			position = 23
	)
	@Range(max = 1800)
	default int foggyDensity()
	{
		return 500;
	}

	@ConfigItem(
			keyName = "enableStars",
			name = "Stars Enabled",
			description = "Allows Star objects to appear while in other Realms",
			section = weatherToggles,
			position = 24
	)
	default boolean enableStars()
	{
		return true;
	}

	@ConfigItem(
			keyName = "starryDensity",
			name = "Stars Density",
			description = "Sets the number of Star objects that spawn while Starry",
			section = weatherToggles,
			position = 25
	)
	@Range(max = 2000)
	default int starryDensity()
	{
		return 400;
	}

	@ConfigItem(
			keyName = "enableWintertodtSnow",
			name = "Enable Wintertodt Snow",
			description = "Allows Snow objects to appear at Wintertodt while Snowing (may make it difficult to see incoming attacks)",
			section = weatherToggles,
			position = 26
	)
	default boolean enableWintertodtSnow()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableLightning",
			name = "Enable Lightning",
			description = "PHOTOSENSITIVITY WARNING - Allows Lightning flashes to occur during Stormy weather",
			section = weatherToggles,
			position = 27
	)
	default boolean enableLightning()
	{
		return false;
	}
}
