package com.weather3d;

import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

public enum Condition {

    // Seasons
    SEASON_SPRING("Spring", "/Season - Spring.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    SEASON_SUMMER("Summer", "/Season - Summer.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    SEASON_AUTUMN("Autumn", "/Season - Autumn.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    SEASON_WINTER("Winter", "/Season - Winter.png", false, 0, false, null,
            0, 0, 0, 0, 1),

    // Weather
    WEATHER_ASHFALL("Ashfall", "/Weather - Ashfall.png", true, 3, false, SoundEffect.ASHFALL,
            100, 200, 400, 800, 4),
    WEATHER_CLOUDY("Cloudy", "/Weather - Cloudy.png", true, 3, false, null,
            100, 200, 400, 600, 20),
    WEATHER_COSMOS("Otherworldly", "/Weather - Cosmos.png", true, 3, false, SoundEffect.COSMOS,
            100, 400, 700, 1000, 8),
    WEATHER_COVERED("Sheltered", "/Weather - Covered.png", false, 1, false, null,
            0, 0, 0, 0, 1),
    WEATHER_FOGGY("Foggy", "/Weather - Foggy.png", true, 1, false, null,
            200, 600, 1000, 1400, 14),
    WEATHER_PARTLY_CLOUDY("Partly Cloudy", "/Weather - Partly Cloudy.png", true, 3, false, null,
            25, 50, 100, 200, 20),
    WEATHER_RAINING("Raining", "/Weather - Raining.png", true, 3,true, SoundEffect.RAIN,
            200, 400, 800, 1200, 2),
    WEATHER_SANDSTORM("Sandstorm", "/Weather - Snow.png", true, 1, true, SoundEffect.WIND,
            5, 10, 15, 20, 8),
    WEATHER_SNOWING("Snowing", "/Weather - Snow.png", true, 3, true, SoundEffect.WIND,
            200, 400, 800, 1200, 4),
    WEATHER_STORM("Stormy", "/Weather - Stormy.png", true, 3, true, SoundEffect.THUNDERSTORM,
            300, 600, 1000, 1400, 1),
    WEATHER_SUNNY("Clear", "/Weather - Sunny.png", false, 1, false, null,
            0, 0, 0, 0, 1),

    // Biomes
    BIOME_ARCTIC("Arctic", "/Biome - Arctic.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_BARRENS("Barrens", "/Biome - Barrens.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_CAVE("Caves", "/Biome - Cave.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_COSMOS("Cosmos", "/Biome - Cosmos.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_DESERT("Desert", "/Biome - Desert.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_FOREST("Woodland", "/Biome - Forest.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_GRASSLAND("Grassland", "/Biome - Grassland.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_LAVA_CAVE("Lava Caves", "/Biome - Lava Cave.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_MOUNTAIN("Mountains", "/Biome - Mountain.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_SWAMP("Swamp", "/Biome - Swamp.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_TROPICAL("Tropics", "/Biome - Tropical.png", false, 0, false, null,
            0, 0, 0, 0, 1),
    BIOME_WILDERNESS("Wilderness", "/Biome - Wilderness.png", false, 0, false, null,
            0, 0, 0, 0, 1),

    ;

    @Getter
    private final String name;
    @Getter
    private final BufferedImage conditionImage;
    @Getter
    private final boolean hasPrecipitation;
    @Getter
    private final int modelVariety;
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
    private final int objExtreme;
    @Getter
    private final int changeRate;

    Condition(String name, String imageFile, boolean hasPrecipitation, int modelVariety, boolean hasSound, SoundEffect soundEffect, int objLow, int objMed, int objHigh, int objExtreme, int changeRate)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
        this.hasPrecipitation = hasPrecipitation;
        this.modelVariety = modelVariety;
        this.hasSound = hasSound;
        this.soundEffect = soundEffect;
        this.objLow = objLow;
        this.objMed = objMed;
        this.objHigh = objHigh;
        this.objExtreme = objExtreme;
        this.changeRate = changeRate;
    }
}
