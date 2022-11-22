package com.weather3d;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CyclesClock
{
    private static final ZoneId JAGEX = ZoneId.of("Europe/London");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("DDD:hh:mm");

    public static int getTimeMinutes()
    {
        String currentTime = getJagexTime();
        String[] currentTimeArray = currentTime.split(":");
        return Integer.valueOf(currentTimeArray[2]);
    }

    public static int getTimeHours()
    {
        String currentTime = getJagexTime();
        String[] currentTimeArray = currentTime.split(":");
        return Integer.valueOf(currentTimeArray[1]);
    }

    public static int getTimeDays()
    {
        String currentTime = getJagexTime();
        String[] currentTimeArray = currentTime.split(":");
        return Integer.valueOf(currentTimeArray[0]);
    }

    public static String getJagexTime()
    {
        LocalDateTime time = LocalDateTime.now(JAGEX);
        return time.format(TIME_FORMAT);
    }
}
