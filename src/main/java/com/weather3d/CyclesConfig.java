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

	enum WeatherDensity {
		LOW,
		MEDIUM,
		HIGH,
		EXTREME,
		GAMEBREAKING
	}

	enum SeasonType {
		DYNAMIC,
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
			keyName = "weatherDensity",
			name = "Weather Density",
			description = "Determines density of Weather objects (lower is better for performance)",
			section = generalSettings,
			position = 3
	)
	default WeatherDensity weatherDensity()
	{
		return WeatherDensity.MEDIUM;
	}

	@ConfigItem(
			keyName = "toggleOverlay",
			name = "Enable Overlay",
			description = "Displays an overlay indicating Weather, Biome, and Season",
			section = generalSettings,
			position = 4
	)
	default boolean toggleOverlay()
	{
		return true;
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
			description = "Weather toggles",
			position = 11
	)
	String weatherToggles = "weatherToggles";

	@ConfigItem(
			keyName = "enableClouds",
			name = "Enable Clouds",
			description = "Allows Cloud objects to appear while Cloudy or Partly Cloudy",
			section = weatherToggles,
			position = 12
	)
	default boolean enableClouds()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableFog",
			name = "Enable Fog",
			description = "Allows Fog objects to appear while Foggy",
			section = weatherToggles,
			position = 13
	)
	default boolean enableFog()
	{
		return false;
	}

	@ConfigItem(
			keyName = "enableStars",
			name = "Enable Stars",
			description = "Allows Star objects to appear while in other Realms",
			section = weatherToggles,
			position = 14
	)
	default boolean enableStars()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableWintertodtSnow",
			name = "Enable Wintertodt Snow",
			description = "Allows Snow objects to appear at Wintertodt while Snowing (may make it difficult to see incoming attacks)",
			section = weatherToggles,
			position = 15
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
			position = 16
	)
	default boolean enableLightning()
	{
		return false;
	}

	@ConfigSection(
			name = "Other",
			description = "Other Settings",
			position = 17
	)
	String otherSettings = "otherSettings";

	@ConfigItem(
			keyName = "winterTheme",
			name = "Allow 117 Winter Theme Override",
			description = "Allows 117HD Winter Theme to override current Season",
			section = otherSettings,
			position = 18
	)
	default boolean winterTheme()
	{
		return true;
	}
}
