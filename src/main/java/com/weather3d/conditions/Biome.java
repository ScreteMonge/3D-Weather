package com.weather3d.conditions;

import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Getter
public enum Biome
{
    ARCTIC("Arctic", "/Biome - Arctic.png", "/Biome - Arctic - Mini.png"),
    BARRENS("Barrens", "/Biome - Barrens.png", "/Biome - Barrens - Mini.png"),
    CAVE("Caves", "/Biome - Cave.png", "/Biome - Cave - Mini.png"),
    COSMOS("Cosmos", "/Biome - Cosmos.png", "/Biome - Cosmos - Mini.png"),
    DESERT("Desert", "/Biome - Desert.png", "/Biome - Desert - Mini.png"),
    FOREST("Woodland", "/Biome - Forest.png", "/Biome - Forest - Mini.png"),
    GRASSLAND("Grassland", "/Biome - Grassland.png", "/Biome - Grassland - Mini.png"),
    LAVA_CAVE("Lava Caves", "/Biome - Lava Cave.png", "/Biome - Lava Cave - Mini.png"),
    MOUNTAIN("Mountains", "/Biome - Mountain.png", "/Biome - Mountain - Mini.png"),
    SWAMP("Swamp", "/Biome - Swamp.png", "/Biome - Swamp - Mini.png"),
    TROPICAL("Tropics", "/Biome - Tropical.png", "/Biome - Tropical - Mini.png"),
    WILDERNESS("Wilderness", "/Biome - Wilderness.png", "/Biome - Wilderness - Mini.png")
    ;

    private final String name;
    private final BufferedImage conditionImage;
    private final BufferedImage miniConditionImage;

    Biome(String name, String imageFile, String miniImageFile)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
        this.miniConditionImage = ImageUtil.loadImageResource(getClass(), miniImageFile);
    }
}
