package com.weather3d;

import com.weather3d.conditions.Biomes;
import com.weather3d.conditions.Seasons;
import com.weather3d.conditions.Weathers;
import lombok.Getter;

public enum WeatherForecast {
    SPRING_ARCTIC(Seasons.SPRING, Biomes.ARCTIC, new Weathers[]
            {Weathers.CLOUDY, Weathers.SNOWING, Weathers.SNOWING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.FOGGY, Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.CLOUDY}),
    SPRING_BARRENS(Seasons.SPRING, Biomes.BARRENS, new Weathers[]
            {Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.STORM, Weathers.PARTLY_CLOUDY, Weathers.SUNNY,  Weathers.PARTLY_CLOUDY}),
    SPRING_CAVE(Seasons.SPRING, Biomes.CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    SPRING_COSMOS(Seasons.SPRING, Biomes.COSMOS, new Weathers[]
            {Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY}),
    SPRING_DESERT(Seasons.SPRING, Biomes.DESERT, new Weathers[]
            {Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY}),
    SPRING_FOREST(Seasons.SPRING, Biomes.FOREST, new Weathers[]
            {Weathers.SUNNY, Weathers.FOGGY, Weathers.RAINING, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.RAINING, Weathers.STORM, Weathers.CLOUDY}),
    SPRING_GRASSLAND(Seasons.SPRING, Biomes.GRASSLAND, new Weathers[]
            {Weathers.RAINING, Weathers.STORM, Weathers.SUNNY, Weathers.CLOUDY, Weathers.FOGGY, Weathers.SUNNY, Weathers.RAINING, Weathers.CLOUDY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.SUNNY}),
    SPRING_LAVA_CAVE(Seasons.SPRING, Biomes.LAVA_CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    SPRING_MOUNTAIN(Seasons.SPRING, Biomes.MOUNTAIN, new Weathers[]
            {Weathers.STORM, Weathers.SNOWING, Weathers.CLOUDY, Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.CLOUDY, Weathers.SNOWING, Weathers.SNOWING, Weathers.STORM, Weathers.SUNNY, Weathers.CLOUDY}),
    SPRING_SWAMP(Seasons.SPRING, Biomes.SWAMP, new Weathers[]
            {Weathers.FOGGY, Weathers.RAINING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.SUNNY, Weathers.RAINING, Weathers.CLOUDY, Weathers.FOGGY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY}),
    SPRING_TROPICAL(Seasons.SPRING, Biomes.TROPICAL, new Weathers[]
            {Weathers.RAINING, Weathers.STORM, Weathers.RAINING, Weathers.CLOUDY, Weathers.STORM, Weathers.RAINING, Weathers.RAINING, Weathers.SUNNY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.CLOUDY}),
    SPRING_WILDERNESS(Seasons.SPRING, Biomes.WILDERNESS, new Weathers[]
            {Weathers.ASHFALL, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.ASHFALL, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.ASHFALL, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.ASHFALL, Weathers.SUNNY}),

    SUMMER_ARCTIC(Seasons.SUMMER, Biomes.ARCTIC, new Weathers[]
            {Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.RAINING, Weathers.FOGGY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.STORM, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.STORM}),
    SUMMER_BARRENS(Seasons.SUMMER, Biomes.BARRENS, new Weathers[]
            {Weathers.SUNNY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.STORM, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.STORM, Weathers.CLOUDY}),
    SUMMER_CAVE(Seasons.SUMMER, Biomes.CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    SUMMER_COSMOS(Seasons.SUMMER, Biomes.COSMOS, new Weathers[]
            {Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY}),
    SUMMER_DESERT(Seasons.SUMMER, Biomes.DESERT, new Weathers[]
            {Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY}),
    SUMMER_FOREST(Seasons.SUMMER, Biomes.FOREST, new Weathers[]
            {Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.RAINING, Weathers.STORM, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.RAINING}),
    SUMMER_GRASSLAND(Seasons.SUMMER, Biomes.GRASSLAND, new Weathers[]
            {Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.RAINING, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.STORM, Weathers.RAINING, Weathers.PARTLY_CLOUDY}),
    SUMMER_LAVA_CAVE(Seasons.SUMMER, Biomes.LAVA_CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    SUMMER_MOUNTAIN(Seasons.SUMMER, Biomes.MOUNTAIN, new Weathers[]
            {Weathers.RAINING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.STORM, Weathers.RAINING, Weathers.CLOUDY, Weathers.FOGGY, Weathers.RAINING, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY}),
    SUMMER_SWAMP(Seasons.SUMMER, Biomes.SWAMP, new Weathers[]
            {Weathers.SUNNY, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.FOGGY, Weathers.RAINING, Weathers.CLOUDY, Weathers.RAINING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.RAINING}),
    SUMMER_TROPICAL(Seasons.SUMMER, Biomes.TROPICAL, new Weathers[]
            {Weathers.STORM, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.STORM, Weathers.RAINING, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.RAINING}),
    SUMMER_WILDERNESS(Seasons.SUMMER, Biomes.WILDERNESS, new Weathers[]
            {Weathers.ASHFALL, Weathers.ASHFALL, Weathers.ASHFALL, Weathers.SUNNY, Weathers.SUNNY, Weathers.ASHFALL, Weathers.SUNNY, Weathers.ASHFALL, Weathers.ASHFALL, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY}),

    AUTUMN_ARCTIC(Seasons.AUTUMN, Biomes.ARCTIC, new Weathers[]
            {Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.SNOWING, Weathers.SUNNY, Weathers.FOGGY, Weathers.FOGGY, Weathers.CLOUDY, Weathers.SNOWING, Weathers.CLOUDY}),
    AUTUMN_BARRENS(Seasons.AUTUMN, Biomes.BARRENS, new Weathers[]
            {Weathers.RAINING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.STORM, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY}),
    AUTUMN_CAVE(Seasons.AUTUMN, Biomes.CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    AUTUMN_COSMOS(Seasons.AUTUMN, Biomes.COSMOS, new Weathers[]
            {Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY}),
    AUTUMN_DESERT(Seasons.AUTUMN, Biomes.DESERT, new Weathers[]
            {Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY}),
    AUTUMN_FOREST(Seasons.AUTUMN, Biomes.FOREST, new Weathers[]
            {Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.RAINING, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.SUNNY}),
    AUTUMN_GRASSLAND(Seasons.AUTUMN, Biomes.GRASSLAND, new Weathers[]
            {Weathers.FOGGY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.STORM, Weathers.SUNNY, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.SUNNY}),
    AUTUMN_LAVA_CAVE(Seasons.AUTUMN, Biomes.LAVA_CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    AUTUMN_MOUNTAIN(Seasons.AUTUMN, Biomes.MOUNTAIN, new Weathers[]
            {Weathers.FOGGY, Weathers.SNOWING, Weathers.CLOUDY, Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.STORM, Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.STORM}),
    AUTUMN_SWAMP(Seasons.AUTUMN, Biomes.SWAMP, new Weathers[]
            {Weathers.FOGGY, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.RAINING, Weathers.FOGGY, Weathers.STORM, Weathers.FOGGY, Weathers.RAINING, Weathers.RAINING, Weathers.FOGGY, Weathers.CLOUDY}),
    AUTUMN_TROPICAL(Seasons.AUTUMN, Biomes.TROPICAL, new Weathers[]
            {Weathers.RAINING, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.STORM, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.RAINING, Weathers.STORM, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.STORM}),
    AUTUMN_WILDERNESS(Seasons.AUTUMN, Biomes.WILDERNESS, new Weathers[]
            {Weathers.PARTLY_CLOUDY, Weathers.ASHFALL, Weathers.SUNNY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.ASHFALL, Weathers.ASHFALL, Weathers.PARTLY_CLOUDY, Weathers.ASHFALL, Weathers.SUNNY, Weathers.SUNNY, Weathers.ASHFALL}),

    WINTER_ARCTIC(Seasons.WINTER, Biomes.ARCTIC, new Weathers[]
            {Weathers.SNOWING, Weathers.SNOWING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SNOWING, Weathers.SNOWING, Weathers.FOGGY, Weathers.SNOWING, Weathers.CLOUDY, Weathers.SNOWING, Weathers.FOGGY}),
    WINTER_BARRENS(Seasons.WINTER, Biomes.BARRENS, new Weathers[]
            {Weathers.FOGGY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.CLOUDY, Weathers.STORM, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.STORM, Weathers.CLOUDY, Weathers.STORM, Weathers.SUNNY}),
    WINTER_CAVE(Seasons.WINTER, Biomes.CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    WINTER_COSMOS(Seasons.WINTER, Biomes.COSMOS, new Weathers[]
            {Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.COSMOS, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY, Weathers.SUNNY}),
    WINTER_DESERT(Seasons.WINTER, Biomes.DESERT, new Weathers[]
            {Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.SUNNY, Weathers.SUNNY, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.PARTLY_CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY}),
    WINTER_FOREST(Seasons.WINTER, Biomes.FOREST, new Weathers[]
            {Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.FOGGY, Weathers.SUNNY, Weathers.CLOUDY, Weathers.SNOWING, Weathers.SNOWING, Weathers.CLOUDY, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SUNNY}),
    WINTER_GRASSLAND(Seasons.WINTER, Biomes.GRASSLAND, new Weathers[]
            {Weathers.SNOWING, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.FOGGY, Weathers.SNOWING, Weathers.SNOWING, Weathers.PARTLY_CLOUDY, Weathers.SUNNY, Weathers.SNOWING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY}),
    WINTER_LAVA_CAVE(Seasons.WINTER, Biomes.LAVA_CAVE, new Weathers[]
            {Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED, Weathers.COVERED}),
    WINTER_MOUNTAIN(Seasons.WINTER, Biomes.MOUNTAIN, new Weathers[]
            {Weathers.SNOWING, Weathers.SNOWING, Weathers.STORM, Weathers.SNOWING, Weathers.CLOUDY, Weathers.STORM, Weathers.SNOWING, Weathers.FOGGY, Weathers.SUNNY, Weathers.STORM, Weathers.STORM, Weathers.CLOUDY}),
    WINTER_SWAMP(Seasons.WINTER, Biomes.SWAMP, new Weathers[]
            {Weathers.SNOWING, Weathers.FOGGY, Weathers.RAINING, Weathers.FOGGY, Weathers.PARTLY_CLOUDY, Weathers.SNOWING, Weathers.SUNNY, Weathers.FOGGY, Weathers.SNOWING, Weathers.RAINING, Weathers.SNOWING, Weathers.FOGGY}),
    WINTER_TROPICAL(Seasons.WINTER, Biomes.TROPICAL, new Weathers[]
            {Weathers.RAINING, Weathers.CLOUDY, Weathers.PARTLY_CLOUDY, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.RAINING, Weathers.CLOUDY, Weathers.STORM, Weathers.RAINING, Weathers.PARTLY_CLOUDY, Weathers.STORM, Weathers.RAINING}),
    WINTER_WILDERNESS(Seasons.WINTER, Biomes.WILDERNESS, new Weathers[]
            {Weathers.STORM, Weathers.SUNNY, Weathers.ASHFALL, Weathers.ASHFALL, Weathers.SUNNY, Weathers.STORM, Weathers.ASHFALL, Weathers.ASHFALL, Weathers.SUNNY, Weathers.STORM, Weathers.STORM, Weathers.ASHFALL}),

    ;

    @Getter
    final Seasons seasonCondition;
    @Getter
    final Biomes biomeCondition;
    @Getter
    final Weathers[] forecastArray;

    WeatherForecast(Seasons seasonCondition, Biomes biomeCondition, Weathers[] forecastArray)
    {
        this.seasonCondition = seasonCondition;
        this.biomeCondition = biomeCondition;
        this.forecastArray = forecastArray;
    }
}
