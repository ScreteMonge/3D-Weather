package com.weather3d.conditions;

import com.weather3d.audio.SoundEffect;
import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Getter
public enum Weather {
    ASHFALL("Ashfall", "/Weather - Ashfall.png", "/Weather - Ashfall - Mini.png",true, 3, 60, false, null,
            1200, 800, 4),
    CLOUDY("Cloudy", "/Weather - Cloudy.png", "/Weather - Cloudy - Mini.png",true, 3, 300, false, null,
            1000, 600, 200),
    STARRY("Otherworldly", "/Weather - Cosmos.png", "/Weather - Cosmos - Mini.png", true, 3, 60, false, null,
            2000, 1000, 8),
    COVERED("Sheltered", "/Weather - Covered.png", "/Weather - Covered - Mini.png", false, 1, 60, false, null,
            0, 0, 1),
    FOGGY("Foggy", "/Weather - Foggy.png", "/Weather - Foggy - Mini.png", true, 3, 60, false, null,
            1800, 1100, 200),
    PARTLY_CLOUDY("Partly Cloudy", "/Weather - Partly Cloudy.png", "/Weather - Partly Cloudy - Mini.png", true, 3, 300, false, null,
            300, 200, 200),
    RAINY("Raining", "/Weather - Raining.png", "/Weather - Raining - Mini.png", true, 3, 60, true, SoundEffect.RAIN,
            2000, 1200, 2),
    SNOWY("Snowing", "/Weather - Snow.png", "/Weather - Snow - Mini.png", true, 3, 60, true, SoundEffect.WIND,
            1800, 1200, 4),
    STORMY("Stormy", "/Weather - Stormy.png", "/Weather - Stormy - Mini.png", true, 3, 60, true, SoundEffect.THUNDERSTORM,
            3000, 1400, 1),
    SUNNY("Clear", "/Weather - Sunny.png", "/Weather - Sunny - Mini.png", false, 1, 60, false, null,
            0, 0, 1),
    ;

    private final String name;
    private final BufferedImage conditionImage;
    private final BufferedImage miniConditionImage;
    private final boolean hasPrecipitation;
    private final int modelVariety;
    private final int objRadius;
    private final boolean hasSound;
    private final SoundEffect soundEffect;
    private final int maxObjects;
    private final int maxObjectVolume;
    private final int changeRate;

    Weather(String name, String imageFile, String miniImageFile, boolean hasPrecipitation, int modelVariety, int objRadius, boolean hasSound, SoundEffect soundEffect, int maxObjects, int maxObjectVolume, int changeRate)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
        this.miniConditionImage = ImageUtil.loadImageResource(getClass(), miniImageFile);
        this.hasPrecipitation = hasPrecipitation;
        this.modelVariety = modelVariety;
        this.objRadius = objRadius;
        this.hasSound = hasSound;
        this.soundEffect = soundEffect;
        this.maxObjects = maxObjects;
        this.maxObjectVolume = maxObjectVolume;
        this.changeRate = changeRate;
    }
}
