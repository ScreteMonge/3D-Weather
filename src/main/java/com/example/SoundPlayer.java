package com.example;

import javax.inject.Inject;
import javax.sound.midi.Track;

import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.callback.ClientThread;

import java.io.*;
import java.net.URI;

public class SoundPlayer
{
    @Inject
    private CyclesConfig config;
    @Inject
    private ClientThread clientThread;

    @Setter
    @Getter
    private boolean isFading = false;
    @Setter
    @Getter
    private boolean loop = false;
    @Getter
    private SoundEffect savedSound;
    private final MP3Player trackPlayer = new MP3Player();

    public void playClip(SoundEffect soundEffect, int volume)
    {
        try
        {
            String soundLink = soundEffect.getSoundFile();

            clientThread.invoke(() -> {
                try
                {
                    savedSound = soundEffect;
                    trackPlayer.getPlayList().clear();
                    trackPlayer.addToPlayList(new URI(soundLink).toURL());
                    trackPlayer.setVolume(volume);
                    trackPlayer.play();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopClip()
    {
        trackPlayer.stop();
    }

    public boolean isPlaying()
    {
        return trackPlayer.isPlaying();
    }

    public int getCurrentVolume()
    {
        return trackPlayer.getVolume();
    }

    public int getEndVolume()
    {
        int volDensity;
        switch(config.weatherDensity())
        {
            case LOW:
                volDensity = 1;
                break;
            default:
            case MEDIUM:
                volDensity = 2;
                break;
            case HIGH:
                volDensity = 3;
                break;
            case HIGHEST:
                volDensity = 4;
                break;
        }

        int volAmbience;
        switch(config.ambientVolume())
        {
            case LOW:
                volAmbience = 10;
                break;
            default:
            case MEDIUM:
                volAmbience = 15;
                break;
            case HIGH:
                volAmbience = 20;
                break;
            case HIGHEST:
                volAmbience = 25;
                break;
        }

        return volDensity * volAmbience;
    }

    public void setVolumeLevel(int volumeLevel)
    {
        if (volumeLevel < 0)
        {
            trackPlayer.setVolume(0);
            return;
        }
        trackPlayer.setVolume(volumeLevel);
    }

}
