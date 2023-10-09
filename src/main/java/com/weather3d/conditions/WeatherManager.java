package com.weather3d.conditions;

import com.weather3d.WeatherObject;
import com.weather3d.audio.SoundPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class WeatherManager
{
    private Weather weatherType;
    private SoundPlayer[] soundPlayers;
    private int soundPlayerTimer;
    private ArrayList<WeatherObject> weatherObjArray;
    private int startRotation;
    private boolean isFading;

    public SoundPlayer getPrimarySoundPlayer()
    {
        if (soundPlayers[1].isPrimarySoundPlayer())
        {
            soundPlayers[1].setPrimarySoundPlayer(true);
            soundPlayers[0].setPrimarySoundPlayer(false);
            return soundPlayers[1];
        }

        soundPlayers[0].setPrimarySoundPlayer(true);
        soundPlayers[1].setPrimarySoundPlayer(false);
        return soundPlayers[0];
    }

    public void switchSoundPlayerPriority()
    {
        SoundPlayer primSoundPlayer = getPrimarySoundPlayer();
        SoundPlayer secSoundPlayer;

        if (primSoundPlayer == soundPlayers[0])
        {
            secSoundPlayer = soundPlayers[1];
        }
        else
        {
            secSoundPlayer = soundPlayers[0];
        }

        primSoundPlayer.setPrimarySoundPlayer(false);

        secSoundPlayer.setPrimarySoundPlayer(true);
    }

    public void stopManagerSoundPlayers()
    {
        setSoundPlayerTimer(0);
        for (SoundPlayer soundPlayer : soundPlayers)
        {
            soundPlayer.stopClip();
        }
    }
}
