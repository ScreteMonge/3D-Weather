package com.weather3d;

import net.runelite.client.config.*;

@ConfigGroup("3Dweather")
public interface CyclesConfig extends Config {

	enum WeatherType {
		DYNAMIC,
		ASHFALL,
		CLEAR,
		FOG,
		RAIN,
		SNOW,
		STARS,
		STORM
	}

	enum WeatherDensity {
		LOW,
		MEDIUM,
		HIGH,
		HIGHEST
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

}
