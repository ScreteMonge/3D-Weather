package com.weather3d.conditions;

import lombok.Getter;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Getter
public enum Seasons
{
    SPRING("Spring", "/Season - Spring.png"),
    SUMMER("Summer", "/Season - Summer.png"),
    AUTUMN("Autumn", "/Season - Autumn.png"),
    WINTER("Winter", "/Season - Winter.png")
    ;

    private String name;
    private BufferedImage conditionImage;

    Seasons(String name, String imageFile)
    {
        this.name = name;
        this.conditionImage = ImageUtil.loadImageResource(getClass(), imageFile);
    }
}
