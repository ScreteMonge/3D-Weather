package com.weather3d;

import lombok.Getter;

public enum SoundEffect {
    ASHFALL("https://github.com/ScreteMonge/WeatherAmbience/raw/main/147661__herbertboland__rumble_edited.mp3"),
    COSMOS("https://github.com/ScreteMonge/WeatherAmbience/raw/main/1105__freed__241102h_edited.mp3"),
    RAIN("https://github.com/ScreteMonge/WeatherAmbience/raw/main/617078__mikaelacampbell18__rain-forest-steady_edited.mp3"),
    THUNDERSTORM("https://github.com/ScreteMonge/WeatherAmbience/raw/main/180327__aeonemi__lighting-strike-and-thunder_edited.mp3"),
    WIND("https://github.com/ScreteMonge/WeatherAmbience/raw/main/201208__rivv3t__raw-wind_edited.mp3")

    ;

    @Getter
    private final String soundFile;

    SoundEffect(String soundFile)
    {
        this.soundFile = soundFile;
    }
}
