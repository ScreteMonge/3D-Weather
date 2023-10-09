package com.weather3d.conditions;

import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Getter
public enum Season
{
    SPRING("Spring", "/Season - Spring.png", "/Season - Spring - Mini.png"),
    SUMMER("Summer", "/Season - Summer.png", "/Season - Summer - Mini.png"),
    AUTUMN("Autumn", "/Season - Autumn.png", "/Season - Autumn - Mini.png"),
    WINTER("Winter", "/Season - Winter.png", "/Season - Winter - Mini.png")
    ;

    private final String name;
    private final BufferedImage conditionImage;
    private final BufferedImage miniConditionImage;

    Season(String name, String imageFile, String miniImageFile)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
        this.miniConditionImage = ImageUtil.loadImageResource(getClass(), miniImageFile);
    }
}
