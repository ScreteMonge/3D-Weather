package com.weather3d.audio;

import com.weather3d.CyclesConfig;
import com.weather3d.CyclesPlugin;
import com.weather3d.audio.SoundEffect;
import jaco.mp3.player.MP3Player;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

@Getter
@Setter
@Slf4j
public class SoundPlayer
{
    @Inject
    private CyclesPlugin plugin;
    @Inject
    private CyclesConfig config;
    @Inject
    private SourceDataLine sourceDataLine;
    @Inject
    private AudioSystem audioSystem;

    private boolean isFading = false;
    private boolean primarySoundPlayer = false;
    private int timer = 0;
    private SoundEffect currentTrack;
    private boolean ambienceError = false;
    private final MP3Player trackPlayer = new MP3Player();
    private Thread handlePlayThread = null;
    private Thread volumeChangeHandler = null;

    public void playClip(SoundEffect soundEffect)
    {
        setTimer(0);
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

        trackPlayer.clearPlayList();
        handlePlayThread = new Thread(() -> {
            try
            {
                String soundLink = soundEffect.getSoundFile();
                if (soundLink.toLowerCase().startsWith("http"))
                    trackPlayer.add(new URL(soundLink));
                else
                    trackPlayer.add(new File(soundLink)); //this is here for local testing
                trackPlayer.play();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        currentTrack = soundEffect;
        handlePlayThread.start();
    }

    public void stopClip()
    {
        log.debug(currentTrack + " SoundPlayer STOPPING!");
        trackPlayer.stop();
        setTimer(0);
    }

    public boolean isPlaying()
    {
        return trackPlayer.isPlaying();
    }

    public int getCurrentVolume()
    {
        return trackPlayer.getVolume();
    }

    public void smoothVolumeChange(int endVolume, int milliseconds)
    {
        if (volumeChangeHandler != null )
            volumeChangeHandler.interrupt();

        volumeChangeHandler = new Thread(() ->
        {
            try
            {
                int startVolume = getCurrentVolume();
                long startTime = System.currentTimeMillis();
                long endTime = startTime + milliseconds;
                while (System.currentTimeMillis() < endTime)
                {
                    double percentProgress = ((double)(System.currentTimeMillis() - startTime))/ (endTime - startTime);
                    int newVolume = (int)(startVolume +  sigmoid(percentProgress) * (endVolume - startVolume));
                    setVolumeLevel(newVolume);
                    Thread.sleep(100);
                }
                setVolumeLevel(endVolume);
                if (endVolume == 0)
                {
                    setFading(false);
                    stopClip();
                }
            }
            catch (InterruptedException e)
            {
                return;
            }
        });
        volumeChangeHandler.start();
    }

    private double sigmoid(double percentProgress)
    {
        percentProgress = percentProgress * Math.E * 4;
        return (Math.exp(percentProgress - Math.E * 2))/(Math.exp(percentProgress - Math.E * 2) + 1);
    }

    public void setVolumeLevel(int volume)
    {
        if (volume < 0)
            volume = 0;

        if (volume > 100)
            volume = 100;

        trackPlayer.setVolume(volume);
    }
}
