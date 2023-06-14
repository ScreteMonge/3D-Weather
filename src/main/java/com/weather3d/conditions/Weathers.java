package com.weather3d.conditions;

import com.weather3d.audio.SoundEffect;
import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Getter
public enum Weathers {
    ASHFALL("Ashfall", "/Weather - Ashfall.png", true, 3, 60, false, null,
            100, 200, 400, 800, 1200, 4),
    CLOUDY("Cloudy", "/Weather - Cloudy.png", true, 3, 300, false, null,
            100, 200, 400, 600, 1000, 200),
    COSMOS("Otherworldly", "/Weather - Cosmos.png", true, 3, 60, false, null,
            100, 400, 700, 1000, 2000, 8),
    COVERED("Sheltered", "/Weather - Covered.png", false, 1, 60, false, null,
            0, 0, 0, 0, 0, 1),
    FOGGY("Foggy", "/Weather - Foggy.png", true, 3, 60, false, null,
            200, 500, 800, 1100, 1800, 200),
    PARTLY_CLOUDY("Partly Cloudy", "/Weather - Partly Cloudy.png", true, 3, 300, false, null,
            25, 50, 100, 200, 300, 200),
    RAINING("Raining", "/Weather - Raining.png", true, 3, 60, true, SoundEffect.RAIN,
            200, 400, 800, 1200, 2000, 2),
    SNOWING("Snowing", "/Weather - Snow.png", true, 3, 60, true, SoundEffect.WIND,
            200, 400, 800, 1200, 1800, 4),
    STORM("Stormy", "/Weather - Stormy.png", true, 3, 60, true, SoundEffect.THUNDERSTORM,
            300, 600, 1000, 1400, 3000, 1),
    SUNNY("Clear", "/Weather - Sunny.png", false, 1, 60, false, null,
            0, 0, 0, 0, 0, 1),
    ;

    private final String name;
    private final BufferedImage conditionImage;
    private final boolean hasPrecipitation;
    private final int modelVariety;
    private final int objRadius;
    private final boolean hasSound;
    private final SoundEffect soundEffect;
    private final int objLow;
    private final int objMed;
    private final int objHigh;
    private final int objExtreme;
    private final int objGameCrashing;
    private final int changeRate;

    Weathers(String name, String imageFile, boolean hasPrecipitation, int modelVariety, int objRadius, boolean hasSound, SoundEffect soundEffect, int objLow, int objMed, int objHigh, int objExtreme, int objGameCrashing, int changeRate)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
        this.hasPrecipitation = hasPrecipitation;
        this.modelVariety = modelVariety;
        this.objRadius = objRadius;
        this.hasSound = hasSound;
        this.soundEffect = soundEffect;
        this.objLow = objLow;
        this.objMed = objMed;
        this.objHigh = objHigh;
        this.objExtreme = objExtreme;
        this.objGameCrashing = objGameCrashing;
        this.changeRate = changeRate;
    }
}
