package com.weather3d;

import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.sound.sampled.*;
import java.net.URL;

@Getter
@Setter
public class SoundPlayer
{
    @Inject
    private CyclesPlugin plugin;
    @Inject
    private SourceDataLine sourceDataLine;
    @Inject
    private AudioSystem audioSystem;

    private boolean trueFading = false;
    private boolean loopFading = false;
    private boolean primarySoundPlayer = false;
    private boolean ambienceError = false;
    private final MP3Player trackPlayer = new MP3Player();
    private Thread handlePlayThread = null;

    public void playClip(SoundEffect soundEffect)
    {
        //A PC having no audio output seems to cause massive issues. This appears to fix it by testing whether the audio system is active
        AudioFormat format = new AudioFormat(1000, 16, 2, true, false);
        try
        {
            AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, format));
        }
        catch (Exception e)
        {
            return;
        }

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

    public void setVolumeLevel(int volume)
    {
        if (volume < 0)
        {
            volume = 0;
        }

        if (volume > 100)
        {
            volume = 100;
        }

        trackPlayer.setVolume(volume);
    }
}
