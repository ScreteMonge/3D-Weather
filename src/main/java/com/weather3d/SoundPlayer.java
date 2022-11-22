package com.weather3d;

import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class SoundPlayer
{
    private boolean trueFading = false;
    private boolean loopFading = false;
    private boolean primarySoundPlayer = false;
    private final MP3Player trackPlayer = new MP3Player();
    private Thread handlePlayThread = null;

    public void playClip(SoundEffect soundEffect)
    {
        trackPlayer.getPlayList().clear();

        handlePlayThread = new Thread(() -> {
            try
            {
                String soundLink = soundEffect.getSoundFile();
                trackPlayer.addToPlayList(new URL(soundLink));
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
        trackPlayer.pause();
    }

    public boolean isPlaying()
    {
        return trackPlayer.isPlaying();
    }

    public int getCurrentVolume()
    {
        return trackPlayer.getVolume();
    }

    public void setVolumeLevel(int volume)
    {
        if (volume < 0)
        {
            volume = 0;
            System.out.println("Volume tried to be less than 0");
        }

        if (volume > 100)
        {
            volume = 100;
            System.out.println("Volume tried to be greater than 100");
        }

        trackPlayer.setVolume(volume);
    }
}
