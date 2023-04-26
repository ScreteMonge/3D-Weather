package com.weather3d;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.RuneLiteObject;

@Getter
@AllArgsConstructor
public class WeatherObject
{
    private RuneLiteObject runeLiteObject;
    private int objVariant;
}
