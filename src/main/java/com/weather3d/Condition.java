package com.weather3d;

import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

public enum Condition {

    // Seasons
    SEASON_SPRING("Spring", "/Season - Spring.png", false, false, null,
            0, 0, 0, 0, 1),
    SEASON_SUMMER("Summer", "/Season - Summer.png", false, false, null,
            0, 0, 0, 0, 1),
    SEASON_AUTUMN("Autumn", "/Season - Autumn.png", false, false, null,
            0, 0, 0, 0, 1),
    SEASON_WINTER("Winter", "/Season - Winter.png", false, false, null,
            0, 0, 0, 0, 1),

    // Weather
    WEATHER_ASHFALL("Ashfall", "/Weather - Ashfall.png", true, true, SoundEffect.ASHFALL,
            100, 200, 400, 800, 4),
    WEATHER_CLOUDY("Cloudy", "/Weather - Cloudy.png", false, false, null,
            0, 0, 0, 0, 1),
    WEATHER_COSMOS("Otherworldly", "/Weather - Cosmos.png", true, true, SoundEffect.COSMOS,
            50, 100, 200, 400, 8),
    WEATHER_COVERED("Sheltered", "/Weather - Covered.png", false, false, null,
            0, 0, 0, 0, 1),
    WEATHER_FOGGY("Foggy", "/Weather - Foggy.png", true, false, null,
            200, 600, 1000, 1400, 14),
    WEATHER_PARTLY_CLOUDY("Partly Cloudy", "/Weather - Partly Cloudy.png", false, false, null,
            0, 0, 0, 0, 1),
    WEATHER_RAINING("Raining", "/Weather - Raining.png", true, true, SoundEffect.RAIN,
            200, 400, 800, 1200, 2),
    WEATHER_SANDSTORM("Sandstorm", "/Weather - Snow.png", true, true, SoundEffect.WIND,
            5, 10, 15, 20, 8),
    WEATHER_SNOWING("Snowing", "/Weather - Snow.png", true, true, SoundEffect.WIND,
            200, 400, 800, 1200, 4),
    WEATHER_STORM("Stormy", "/Weather - Stormy.png", true, true, SoundEffect.THUNDERSTORM,
            300, 600, 1000, 4000, 1),
    WEATHER_SUNNY("Clear", "/Weather - Sunny.png", false, false, null,
            0, 0, 0, 0, 1),

    // Biomes
    BIOME_ARCTIC("Arctic", "/Biome - Arctic.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_BARRENS("Barrens", "/Biome - Barrens.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_CAVE("Caves", "/Biome - Cave.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_COSMOS("Cosmos", "/Biome - Cosmos.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_DESERT("Desert", "/Biome - Desert.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_FOREST("Woodland", "/Biome - Forest.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_GRASSLAND("Grassland", "/Biome - Grassland.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_LAVA_CAVE("Lava Caves", "/Biome - Lava Cave.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_MOUNTAIN("Mountains", "/Biome - Mountain.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_SWAMP("Swamp", "/Biome - Swamp.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_TROPICAL("Tropics", "/Biome - Tropical.png", false, false, null,
            0, 0, 0, 0, 1),
    BIOME_WILDERNESS("Wilderness", "/Biome - Wilderness.png", false, false, null,
            0, 0, 0, 0, 1),

    ;

    @Getter
    private final String name;
    @Getter
    private final BufferedImage conditionImage;
    @Getter
    private final boolean hasPrecipitation;
    @Getter
    private final boolean hasSound;
    @Getter
    private final SoundEffect soundEffect;
    @Getter
    private final int objLow;
    @Getter
    private final int objMed;
    @Getter
    private final int objHigh;
    @Getter
    private final int objHighest;
    @Getter
    private final int changeRate;

    Condition(String name, String imageFile, boolean hasPrecipitation, boolean hasSound, SoundEffect soundEffect, int objLow, int objMed, int objHigh, int objHighest, int changeRate)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
        this.hasPrecipitation = hasPrecipitation;
        this.hasSound = hasSound;
        this.soundEffect = soundEffect;
        this.objLow = objLow;
        this.objMed = objMed;
        this.objHigh = objHigh;
        this.objHighest = objHighest;
        this.changeRate = changeRate;
    }
}
