package com.weather3d;

import com.weather3d.conditions.Biome;
import com.weather3d.conditions.Season;
import com.weather3d.conditions.Weather;
import lombok.Getter;

public enum WeatherForecast {
    SPRING_ARCTIC(Season.SPRING, Biome.ARCTIC, new Weather[]
            {Weather.CLOUDY, Weather.SNOWY, Weather.SNOWY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.FOGGY, Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.CLOUDY}),
    SPRING_BARRENS(Season.SPRING, Biome.BARRENS, new Weather[]
            {Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.STORMY, Weather.PARTLY_CLOUDY, Weather.SUNNY,  Weather.PARTLY_CLOUDY}),
    SPRING_CAVE(Season.SPRING, Biome.CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    SPRING_COSMOS(Season.SPRING, Biome.COSMOS, new Weather[]
            {Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY}),
    SPRING_DESERT(Season.SPRING, Biome.DESERT, new Weather[]
            {Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY}),
    SPRING_FOREST(Season.SPRING, Biome.FOREST, new Weather[]
            {Weather.SUNNY, Weather.FOGGY, Weather.RAINY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.RAINY, Weather.STORMY, Weather.CLOUDY}),
    SPRING_GRASSLAND(Season.SPRING, Biome.GRASSLAND, new Weather[]
            {Weather.RAINY, Weather.STORMY, Weather.SUNNY, Weather.CLOUDY, Weather.FOGGY, Weather.SUNNY, Weather.RAINY, Weather.CLOUDY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.SUNNY}),
    SPRING_LAVA_CAVE(Season.SPRING, Biome.LAVA_CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    SPRING_MOUNTAIN(Season.SPRING, Biome.MOUNTAIN, new Weather[]
            {Weather.STORMY, Weather.SNOWY, Weather.CLOUDY, Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.CLOUDY, Weather.SNOWY, Weather.SNOWY, Weather.STORMY, Weather.SUNNY, Weather.CLOUDY}),
    SPRING_SWAMP(Season.SPRING, Biome.SWAMP, new Weather[]
            {Weather.FOGGY, Weather.RAINY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.SUNNY, Weather.RAINY, Weather.CLOUDY, Weather.FOGGY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.FOGGY}),
    SPRING_TROPICAL(Season.SPRING, Biome.TROPICAL, new Weather[]
            {Weather.RAINY, Weather.STORMY, Weather.RAINY, Weather.CLOUDY, Weather.STORMY, Weather.RAINY, Weather.RAINY, Weather.SUNNY, Weather.CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.CLOUDY}),
    SPRING_WILDERNESS(Season.SPRING, Biome.WILDERNESS, new Weather[]
            {Weather.ASHFALL, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.ASHFALL, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.ASHFALL, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.ASHFALL, Weather.SUNNY}),

    SUMMER_ARCTIC(Season.SUMMER, Biome.ARCTIC, new Weather[]
            {Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.RAINY, Weather.FOGGY, Weather.SUNNY, Weather.CLOUDY, Weather.STORMY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.STORMY}),
    SUMMER_BARRENS(Season.SUMMER, Biome.BARRENS, new Weather[]
            {Weather.SUNNY, Weather.SUNNY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.STORMY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.STORMY, Weather.CLOUDY}),
    SUMMER_CAVE(Season.SUMMER, Biome.CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    SUMMER_COSMOS(Season.SUMMER, Biome.COSMOS, new Weather[]
            {Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY}),
    SUMMER_DESERT(Season.SUMMER, Biome.DESERT, new Weather[]
            {Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY}),
    SUMMER_FOREST(Season.SUMMER, Biome.FOREST, new Weather[]
            {Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.RAINY, Weather.STORMY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.RAINY}),
    SUMMER_GRASSLAND(Season.SUMMER, Biome.GRASSLAND, new Weather[]
            {Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.RAINY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.STORMY, Weather.RAINY, Weather.PARTLY_CLOUDY}),
    SUMMER_LAVA_CAVE(Season.SUMMER, Biome.LAVA_CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    SUMMER_MOUNTAIN(Season.SUMMER, Biome.MOUNTAIN, new Weather[]
            {Weather.RAINY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.STORMY, Weather.RAINY, Weather.CLOUDY, Weather.FOGGY, Weather.RAINY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SUNNY}),
    SUMMER_SWAMP(Season.SUMMER, Biome.SWAMP, new Weather[]
            {Weather.SUNNY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.FOGGY, Weather.RAINY, Weather.CLOUDY, Weather.RAINY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.RAINY}),
    SUMMER_TROPICAL(Season.SUMMER, Biome.TROPICAL, new Weather[]
            {Weather.STORMY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.STORMY, Weather.RAINY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.RAINY}),
    SUMMER_WILDERNESS(Season.SUMMER, Biome.WILDERNESS, new Weather[]
            {Weather.ASHFALL, Weather.ASHFALL, Weather.ASHFALL, Weather.SUNNY, Weather.SUNNY, Weather.ASHFALL, Weather.SUNNY, Weather.ASHFALL, Weather.ASHFALL, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY}),

    AUTUMN_ARCTIC(Season.AUTUMN, Biome.ARCTIC, new Weather[]
            {Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.SNOWY, Weather.SUNNY, Weather.FOGGY, Weather.FOGGY, Weather.CLOUDY, Weather.SNOWY, Weather.CLOUDY}),
    AUTUMN_BARRENS(Season.AUTUMN, Biome.BARRENS, new Weather[]
            {Weather.RAINY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.STORMY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.PARTLY_CLOUDY}),
    AUTUMN_CAVE(Season.AUTUMN, Biome.CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    AUTUMN_COSMOS(Season.AUTUMN, Biome.COSMOS, new Weather[]
            {Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY}),
    AUTUMN_DESERT(Season.AUTUMN, Biome.DESERT, new Weather[]
            {Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY}),
    AUTUMN_FOREST(Season.AUTUMN, Biome.FOREST, new Weather[]
            {Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.RAINY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.CLOUDY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.SUNNY}),
    AUTUMN_GRASSLAND(Season.AUTUMN, Biome.GRASSLAND, new Weather[]
            {Weather.FOGGY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.SUNNY, Weather.CLOUDY, Weather.STORMY, Weather.SUNNY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.SUNNY}),
    AUTUMN_LAVA_CAVE(Season.AUTUMN, Biome.LAVA_CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    AUTUMN_MOUNTAIN(Season.AUTUMN, Biome.MOUNTAIN, new Weather[]
            {Weather.FOGGY, Weather.SNOWY, Weather.CLOUDY, Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.STORMY, Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.STORMY}),
    AUTUMN_SWAMP(Season.AUTUMN, Biome.SWAMP, new Weather[]
            {Weather.FOGGY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.RAINY, Weather.FOGGY, Weather.STORMY, Weather.FOGGY, Weather.RAINY, Weather.RAINY, Weather.FOGGY, Weather.CLOUDY}),
    AUTUMN_TROPICAL(Season.AUTUMN, Biome.TROPICAL, new Weather[]
            {Weather.RAINY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.STORMY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.RAINY, Weather.STORMY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.STORMY}),
    AUTUMN_WILDERNESS(Season.AUTUMN, Biome.WILDERNESS, new Weather[]
            {Weather.PARTLY_CLOUDY, Weather.ASHFALL, Weather.SUNNY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.ASHFALL, Weather.ASHFALL, Weather.PARTLY_CLOUDY, Weather.ASHFALL, Weather.SUNNY, Weather.SUNNY, Weather.ASHFALL}),

    WINTER_ARCTIC(Season.WINTER, Biome.ARCTIC, new Weather[]
            {Weather.SNOWY, Weather.SNOWY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SNOWY, Weather.SNOWY, Weather.FOGGY, Weather.SNOWY, Weather.CLOUDY, Weather.SNOWY, Weather.FOGGY}),
    WINTER_BARRENS(Season.WINTER, Biome.BARRENS, new Weather[]
            {Weather.FOGGY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.CLOUDY, Weather.STORMY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.STORMY, Weather.CLOUDY, Weather.STORMY, Weather.SUNNY}),
    WINTER_CAVE(Season.WINTER, Biome.CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    WINTER_COSMOS(Season.WINTER, Biome.COSMOS, new Weather[]
            {Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.STARRY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY, Weather.SUNNY}),
    WINTER_DESERT(Season.WINTER, Biome.DESERT, new Weather[]
            {Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.CLOUDY, Weather.SUNNY, Weather.SUNNY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.PARTLY_CLOUDY, Weather.PARTLY_CLOUDY, Weather.SUNNY}),
    WINTER_FOREST(Season.WINTER, Biome.FOREST, new Weather[]
            {Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.FOGGY, Weather.SUNNY, Weather.CLOUDY, Weather.SNOWY, Weather.SNOWY, Weather.CLOUDY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SUNNY}),
    WINTER_GRASSLAND(Season.WINTER, Biome.GRASSLAND, new Weather[]
            {Weather.SNOWY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.FOGGY, Weather.SNOWY, Weather.SNOWY, Weather.PARTLY_CLOUDY, Weather.SUNNY, Weather.SNOWY, Weather.CLOUDY, Weather.PARTLY_CLOUDY}),
    WINTER_LAVA_CAVE(Season.WINTER, Biome.LAVA_CAVE, new Weather[]
            {Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED, Weather.COVERED}),
    WINTER_MOUNTAIN(Season.WINTER, Biome.MOUNTAIN, new Weather[]
            {Weather.SNOWY, Weather.SNOWY, Weather.STORMY, Weather.SNOWY, Weather.CLOUDY, Weather.STORMY, Weather.SNOWY, Weather.FOGGY, Weather.SUNNY, Weather.STORMY, Weather.STORMY, Weather.CLOUDY}),
    WINTER_SWAMP(Season.WINTER, Biome.SWAMP, new Weather[]
            {Weather.SNOWY, Weather.FOGGY, Weather.RAINY, Weather.FOGGY, Weather.PARTLY_CLOUDY, Weather.SNOWY, Weather.SUNNY, Weather.FOGGY, Weather.SNOWY, Weather.RAINY, Weather.SNOWY, Weather.FOGGY}),
    WINTER_TROPICAL(Season.WINTER, Biome.TROPICAL, new Weather[]
            {Weather.RAINY, Weather.CLOUDY, Weather.PARTLY_CLOUDY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.RAINY, Weather.CLOUDY, Weather.STORMY, Weather.RAINY, Weather.PARTLY_CLOUDY, Weather.STORMY, Weather.RAINY}),
    WINTER_WILDERNESS(Season.WINTER, Biome.WILDERNESS, new Weather[]
            {Weather.STORMY, Weather.SUNNY, Weather.ASHFALL, Weather.ASHFALL, Weather.SUNNY, Weather.STORMY, Weather.ASHFALL, Weather.ASHFALL, Weather.SUNNY, Weather.STORMY, Weather.STORMY, Weather.ASHFALL}),

    ;

    @Getter
    final Season seasonCondition;
    @Getter
    final Biome biomeCondition;
    @Getter
    final Weather[] forecastArray;

    WeatherForecast(Season seasonCondition, Biome biomeCondition, Weather[] forecastArray)
    {
        this.seasonCondition = seasonCondition;
        this.biomeCondition = biomeCondition;
        this.forecastArray = forecastArray;
    }
}
