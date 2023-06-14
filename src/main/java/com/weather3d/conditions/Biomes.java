package com.weather3d.conditions;

import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Getter
public enum Biomes
{
    ARCTIC("Arctic", "/Biome - Arctic.png"),
    BARRENS("Barrens", "/Biome - Barrens.png"),
    CAVE("Caves", "/Biome - Cave.png"),
    COSMOS("Cosmos", "/Biome - Cosmos.png"),
    DESERT("Desert", "/Biome - Desert.png"),
    FOREST("Woodland", "/Biome - Forest.png"),
    GRASSLAND("Grassland", "/Biome - Grassland.png"),
    LAVA_CAVE("Lava Caves", "/Biome - Lava Cave.png"),
    MOUNTAIN("Mountains", "/Biome - Mountain.png"),
    SWAMP("Swamp", "/Biome - Swamp.png"),
    TROPICAL("Tropics", "/Biome - Tropical.png"),
    WILDERNESS("Wilderness", "/Biome - Wilderness.png")
    ;

    private String name;
    private BufferedImage conditionImage;

    Biomes(String name, String imageFile)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
    }
}
