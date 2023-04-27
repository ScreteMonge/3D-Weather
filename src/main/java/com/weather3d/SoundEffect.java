package com.weather3d;

import lombok.Getter;

@Getter
public enum SoundEffect {
    RAIN("https://github.com/ScreteMonge/WeatherAmbience/raw/main/617078__mikaelacampbell18__rain-forest-steady_edited.mp3", false),
    THUNDERSTORM("https://github.com/ScreteMonge/WeatherAmbience/raw/main/180327__aeonemi__lighting-strike-and-thunder_edited.mp3", false),
    WIND("https://github.com/ScreteMonge/WeatherAmbience/raw/main/201208__rivv3t__raw-wind_edited.mp3", false),
    RAIN_MUFFLED("https://github.com/ScreteMonge/WeatherAmbience/raw/main/617078__mikaelacampbell18__rain-forest-steady_edited_MUFFLED.mp3", true),
    THUNDERSTORM_MUFFLED("https://github.com/ScreteMonge/WeatherAmbience/raw/main/180327__aeonemi__lighting-strike-and-thunder_edited_MUFFLED.mp3", true),
    WIND_MUFFLED("https://github.com/ScreteMonge/WeatherAmbience/raw/main/201208__rivv3t__raw-wind_edited_MUFFLED.mp3", true)
    ;

    private final String soundFile;
    private final boolean muffled;

    SoundEffect(String soundFile, boolean muffled)
    {
        this.soundFile = soundFile;
        this.muffled = muffled;
    }
}
