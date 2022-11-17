package com.weather3d;

import javax.inject.Inject;

import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.callback.ClientThread;

import java.net.URL;

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
    private Thread handlePlayThread = null;

    public void playClip(SoundEffect soundEffect, int volume)
    {
        trackPlayer.getPlayList().clear();

        handlePlayThread = new Thread(() -> {
            try
            {
                String soundLink = soundEffect.getSoundFile();
                savedSound = soundEffect;
                trackPlayer.addToPlayList(new URL(soundLink));
                trackPlayer.setVolume(volume);
                trackPlayer.play();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        handlePlayThread.start();

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
        double volDensity;
        switch(config.weatherDensity())
        {
            case LOW:
                volDensity = 0.25;
                break;
            default:
            case MEDIUM:
                volDensity = 0.5;
                break;
            case HIGH:
                volDensity = 0.75;
                break;
            case EXTREME:
                volDensity = 1;
                break;
        }

        double configVolume = config.ambientVolume();
        int endVolume = (int) (configVolume * volDensity);
        return endVolume;
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
