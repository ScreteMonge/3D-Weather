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
		EXTREME
	}

	enum SeasonType {
		DYNAMIC,
		SPRING,
		SUMMER,
		AUTUMN,
		WINTER
	}

	@ConfigItem(
		keyName = "weatherType",
		name = "Weather Type",
		description = "Determines the current Weather (Dynamic automatically cycles based on Season and Biome)",
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
			position = 3
	)
	default WeatherDensity weatherDensity()
	{
		return WeatherDensity.MEDIUM;
	}

	@ConfigItem(
			keyName = "toggleOverlay",
			name = "Toggle Overlay",
			description = "Displays an overlay indicating Weather, Biome, and Season",
			position = 4
	)
	default boolean toggleOverlay()
	{
		return true;
	}

	@ConfigItem(
			keyName = "toggleAmbience",
			name = "Toggle Ambience",
			description = "Toggles ambient Weather sounds on/off",
			position = 5
	)
	default boolean toggleAmbience()
	{
		return true;
	}

	@ConfigItem(
			keyName = "ambientVolume",
			name = "Ambient Volume",
			description = "Sets the volume of ambient Weather sounds",
			position = 6
	)
	@Units(Units.PERCENT)
	@Range(max = 100)
	default int ambientVolume()
	{
		return 50;
	}

	@ConfigItem(
			keyName = "disableWeatherUnderground",
			name = "Disable Weather Underground",
			description = "Prevents Weather from occurring while in Cave or Lava Cave Biomes, regardless of the set Weather Type",
			position = 7
	)
	default boolean disableWeatherUnderground()
	{
		return true;
	}

	@ConfigItem(
			keyName = "disableIndoorMuffling",
			name = "Disable Indoor Muffling",
			description = "Prevents ambience sound from being muffled while standing indoors.",
			position = 8
	)
	default boolean disableIndoorMuffling()
	{
		return false;
	}

	@ConfigItem(
			keyName = "enableClouds",
			name = "Enable Clouds",
			description = "Allows Cloud objects to appear while Cloudy or Partly Cloudy",
			position = 9
	)
	default boolean enableClouds()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableFog",
			name = "Enable Fog",
			description = "Allows Fog objects to appear while Foggy",
			position = 10
	)
	default boolean enableFog()
	{
		return false;
	}

	@ConfigItem(
			keyName = "enableStars",
			name = "Enable Stars",
			description = "Allows Star objects to appear while in other Realms",
			position = 11
	)
	default boolean enableStars()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableWintertodtSnow",
			name = "Enable Wintertodt Snow",
			description = "Allows Snow objects to appear at Wintertodt while Snowing (may make it difficult to see incoming attacks)",
			position = 12
	)
	default boolean enableWintertodtSnow()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableLightning",
			name = "Enable Lightning",
			description = "PHOTOSENSITIVITY WARNING - Allows Lightning flashes to occur during Stormy weather",
			position = 13
	)
	default boolean enableLightning()
	{
		return false;
	}

	@ConfigItem(
			keyName = "winterTheme",
			name = "Allow 117 Winter Theme Override",
			description = "Allows 117HD Winter Theme to override current Season",
			position = 14
	)
	default boolean winterTheme()
	{
		return true;
	}
}
