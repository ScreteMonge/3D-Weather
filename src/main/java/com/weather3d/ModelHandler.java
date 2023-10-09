package com.weather3d;

import com.weather3d.conditions.Weather;
import lombok.Getter;
import net.runelite.api.*;

import javax.inject.Inject;
import java.util.Arrays;

@Getter
public class ModelHandler
{
    @Inject
    private Client client;

    private Model ashModel;
    private Model ashModel2;
    private Model ashModel3;
    private Model cloudModel;
    private Model cloudModel2;
    private Model cloudModel3;
    private Model cloudModelTP;
    private Model cloudModelTP2;
    private Model cloudModelTP3;
    private Model fogModel;
    private Model fogModel2;
    private Model fogModel3;
    private Model rainModel;
    private Model rainModel2;
    private Model rainModel3;
    private Model snowModel;
    private Model snowModel2;
    private Model snowModel3;
    private Model starModel;
    private Model starModel2;
    private Model starModel3;
    private Model starModelTP;
    private Model starModelTP2;
    private Model starModelTP3;
    private Model stormModel;
    private Model stormModel2;
    private Model stormModel3;
    private Animation ashAnimation;
    private Animation cloudAnimation;
    private Animation fogAnimation;
    private Animation rainAnimation;
    private Animation snowAnimation;
    private Animation starAnimation;
    private final int ASH_MODEL = 27835;
    private final int ASH_ANIMATION = 7000;
    private final int CLOUD_MODEL = 4086;
    private final int CLOUD_ANIMATION = 6470;
    private final int FOG_MODEL = 29290;
    private final int FOG_ANIMATION = 4516;
    private final int RAIN_MODEL = 15524;
    private final int RAIN_ANIMATION = 7001;
    private final int SNOW_MODEL = 27835;
    private final int SNOW_ANIMATION = 7000;
    private final int STAR_MODEL = 16374;
    private final int STAR_ANIMATION = 7971;

    public void loadModels()
    {
        ModelData ashModelData = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices();
        ModelData ashModelData2 = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices();
        ModelData ashModelData3 = client.loadModelData(ASH_MODEL).cloneColors().cloneVertices();
        short[] ashFaceColours = ashModelData.getFaceColors();
        short[] ashFaceColours2 = ashModelData2.getFaceColors();
        short[] ashFaceColours3 = ashModelData3.getFaceColors();
        short ashColour1 = JagexColor.packHSL(39, 1, 40);
        short ashColour2 = JagexColor.packHSL(39, 1, 40);
        ashModel = ashModelData.scale(128, 192, 128).translate(0, 180, 0).recolor(ashFaceColours[0], ashColour1).recolor(ashFaceColours[2], ashColour2).light();
        ashModel2 = ashModelData2.scale(128, 192, 128).translate(0, 180, 0).recolor(ashFaceColours2[0], ashColour1).recolor(ashFaceColours2[2], ashColour2).rotateY90Ccw().light();
        ashModel3 = ashModelData3.scale(128, 192, 128).translate(0, 180, 0).recolor(ashFaceColours3[0], ashColour1).recolor(ashFaceColours3[2], ashColour2).rotateY270Ccw().light();

        ModelData cloudModelData = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        ModelData cloudModelData2 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        ModelData cloudModelData3 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        short cloudFaceColour = cloudModelData.getFaceColors()[0];
        short cloudReplaceColour = JagexColor.packHSL(54, 0, 110);
        cloudModel = cloudModelData.scale(650, 325, 650).translate(0, -1000, 0).recolor(cloudFaceColour, cloudReplaceColour).light();
        cloudModel2 = cloudModelData2.scale(1000, 500, 1000).translate(0, -1400, 0).recolor(cloudFaceColour, cloudReplaceColour).rotateY90Ccw().light();
        cloudModel3 = cloudModelData3.scale(800, 400, 800).translate(0, -1200, 0).recolor(cloudFaceColour, cloudReplaceColour).rotateY180Ccw().light();

        ModelData cloudTPModelData = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        ModelData cloudTPModelData2 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        ModelData cloudTPModelData3 = client.loadModelData(CLOUD_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        cloudModelTP = cloudTPModelData.scale(650, 325, 650).translate(0, -1000, 0).recolor(cloudFaceColour, cloudReplaceColour).light();
        cloudModelTP2 = cloudTPModelData2.scale(1000, 500, 1000).translate(0, -1400, 0).recolor(cloudFaceColour, cloudReplaceColour).rotateY90Ccw().light();
        cloudModelTP3 = cloudTPModelData3.scale(800, 400, 800).translate(0, -1200, 0).recolor(cloudFaceColour, cloudReplaceColour).rotateY180Ccw().light();
        byte[] cloudMDTP = cloudTPModelData.getFaceTransparencies();
        byte[] cloudMDTP2 = cloudTPModelData2.getFaceTransparencies();
        byte[] cloudMDTP3 = cloudTPModelData3.getFaceTransparencies();
        Arrays.fill(cloudMDTP, (byte) -23);
        Arrays.fill(cloudMDTP2, (byte) -23);
        Arrays.fill(cloudMDTP3, (byte) -23);

        ModelData fogModelData = client.loadModelData(FOG_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        ModelData fogModelData2 = client.loadModelData(FOG_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        ModelData fogModelData3 = client.loadModelData(FOG_MODEL).cloneVertices().cloneColors().cloneTransparencies();
        short fogFaceColour = fogModelData.getFaceColors()[0];
        short fogReplaceColour = JagexColor.packHSL(54, 0, 77);
        fogModel = fogModelData.scale(190, 110, 190).recolor(fogFaceColour, fogReplaceColour).translate(0, -70, 0).light(200, ModelData.DEFAULT_CONTRAST, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        fogModel2 = fogModelData2.scale(190, 110, 190).recolor(fogFaceColour, fogReplaceColour).translate(0, -100, 0).light(200, ModelData.DEFAULT_CONTRAST, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        fogModel3 = fogModelData3.scale(190, 110, 190).recolor(fogFaceColour, fogReplaceColour).translate(0, -85, 0).light(200, ModelData.DEFAULT_CONTRAST, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        byte[] fogTransparency = fogModelData.getFaceTransparencies();
        byte[] fogTransparency2 = fogModelData2.getFaceTransparencies();
        byte[] fogTransparency3 = fogModelData3.getFaceTransparencies();
        Arrays.fill(fogTransparency, (byte) -15);
        Arrays.fill(fogTransparency2, (byte) -15);
        Arrays.fill(fogTransparency3, (byte) -15);

        ModelData snowModelData = client.loadModelData(SNOW_MODEL).cloneVertices();
        ModelData snowModelData2 = client.loadModelData(SNOW_MODEL).cloneVertices();
        ModelData snowModelData3 = client.loadModelData(SNOW_MODEL).cloneVertices();
        snowModel = snowModelData.scale(128, 192, 128).translate(0, 190, 0).light();
        snowModel2 = snowModelData2.scale(128, 192, 128).translate(0, 190, 0).rotateY90Ccw().light();
        snowModel3 = snowModelData3.scale(128, 192, 128).translate(0, 190, 0).rotateY270Ccw().light();

        ModelData rainModelData = client.loadModelData(RAIN_MODEL).cloneVertices();
        ModelData rainModelData2 = client.loadModelData(RAIN_MODEL).cloneVertices();
        ModelData rainModelData3 = client.loadModelData(RAIN_MODEL).cloneVertices();
        short[] rainFaceColours = rainModelData.getFaceColors();
        short[] rainFaceColours2 = rainModelData2.getFaceColors();
        short[] rainFaceColours3 = rainModelData3.getFaceColors();
        short rainRippleColour = JagexColor.packHSL(32, 1, JagexColor.LUMINANCE_MAX);
        short rainDropColour = JagexColor.packHSL(32, 1, 120);
        rainModel = rainModelData.scale(100, 256, 100).recolor(rainFaceColours[0], rainRippleColour).recolor(rainFaceColours[23], rainDropColour).light();
        rainModel2 = rainModelData2.scale(90, 256, 90).recolor(rainFaceColours2[0], rainRippleColour).recolor(rainFaceColours2[23], rainDropColour).rotateY90Ccw().light();
        rainModel3 = rainModelData3.scale(110, 256, 110).recolor(rainFaceColours3[0], rainRippleColour).recolor(rainFaceColours3[23], rainDropColour).rotateY270Ccw().light();

        ModelData stormModelData = client.loadModelData(RAIN_MODEL).cloneColors().cloneVertices();
        ModelData stormModelData2 = client.loadModelData(RAIN_MODEL).cloneColors().cloneVertices();
        ModelData stormModelData3 = client.loadModelData(RAIN_MODEL).cloneColors().cloneVertices();
        short[] stormFaceColours = stormModelData.getFaceColors();
        short[] stormFaceColours2 = stormModelData2.getFaceColors();
        short[] stormFaceColours3 = stormModelData3.getFaceColors();
        short stormRippleColour = JagexColor.packHSL(38, 1, 110);
        short stormDropColour = JagexColor.packHSL(38, 2, 105);
        stormModel = stormModelData.scale(110, 410, 110).recolor(stormFaceColours[0], stormRippleColour).recolor(stormFaceColours[23], stormDropColour).light();
        stormModel2 = stormModelData2.scale(100, 410, 100).recolor(stormFaceColours2[0], stormRippleColour).recolor(stormFaceColours2[23], stormDropColour).rotateY90Ccw().light();
        stormModel3 = stormModelData3.scale(120, 410, 120).recolor(stormFaceColours3[0], stormRippleColour).recolor(stormFaceColours3[23], stormDropColour).rotateY90Ccw().light();

        ModelData starModelData = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
        ModelData starModelData2 = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
        ModelData starModelData3 = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
        short[] starFaceColours = starModelData.getFaceColors();
        short[] starFaceColours2 = starModelData2.getFaceColors();
        short[] starFaceColours3 = starModelData3.getFaceColors();
        short starShellReplaceColour = JagexColor.packHSL(10, 4, 60);
        short starInsideReplaceColour = JagexColor.packHSL(10, 6, 80);
        starModel = starModelData.scale(80, 80, 80).translate(0, -1400, 0).recolor(starFaceColours[0], starShellReplaceColour).recolor(starFaceColours[45], starInsideReplaceColour).light(ModelData.DEFAULT_AMBIENT, 1400, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        starModel2 = starModelData2.scale(65, 65, 65).translate(0, -1500, 0).recolor(starFaceColours2[0], starShellReplaceColour).recolor(starFaceColours2[45], starInsideReplaceColour).light(ModelData.DEFAULT_AMBIENT, 1400, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        starModel3 = starModelData3.scale(95, 95, 95).translate(0, -1300, 0).recolor(starFaceColours3[0], starShellReplaceColour).recolor(starFaceColours3[45], starInsideReplaceColour).light(ModelData.DEFAULT_AMBIENT, 1400, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);

        ModelData starModelDataTP = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
        ModelData starModelDataTP2 = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
        ModelData starModelDataTP3 = client.loadModelData(STAR_MODEL).cloneColors().cloneVertices().cloneTransparencies();
        short[] starFaceColoursTP = starModelDataTP.getFaceColors();
        short[] starFaceColoursTP2 = starModelDataTP2.getFaceColors();
        short[] starFaceColoursTP3 = starModelDataTP3.getFaceColors();
        starModelTP = starModelDataTP.scale(80, 80, 80).translate(0, -1400, 0).recolor(starFaceColoursTP[0], starShellReplaceColour).recolor(starFaceColoursTP[45], starInsideReplaceColour).light(ModelData.DEFAULT_AMBIENT, 1400, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        starModelTP2 = starModelDataTP2.scale(65, 65, 65).translate(0, -1500, 0).recolor(starFaceColoursTP2[0], starShellReplaceColour).recolor(starFaceColoursTP2[45], starInsideReplaceColour).light(ModelData.DEFAULT_AMBIENT, 1400, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        starModelTP3 = starModelDataTP3.scale(95, 95, 95).translate(0, -1300, 0).recolor(starFaceColoursTP3[0], starShellReplaceColour).recolor(starFaceColoursTP3[45], starInsideReplaceColour).light(ModelData.DEFAULT_AMBIENT, 1400, ModelData.DEFAULT_X, ModelData.DEFAULT_Y, ModelData.DEFAULT_Z);
        byte[] starMDTP = starModelDataTP.getFaceTransparencies();
        byte[] starMDTP2 = starModelDataTP2.getFaceTransparencies();
        byte[] starMDTP3 = starModelDataTP3.getFaceTransparencies();
        Arrays.fill(starMDTP, (byte) -80);
        Arrays.fill(starMDTP2, (byte) -80);
        Arrays.fill(starMDTP3, (byte) -80);

        ashAnimation = client.loadAnimation(ASH_ANIMATION);
        cloudAnimation = client.loadAnimation(CLOUD_ANIMATION);
        fogAnimation = client.loadAnimation(CLOUD_ANIMATION);
        rainAnimation = client.loadAnimation(RAIN_ANIMATION);
        snowAnimation = client.loadAnimation(SNOW_ANIMATION);
        starAnimation = client.loadAnimation(STAR_ANIMATION);
    }

    public Model getWeatherModel(Weather currentWeather, int alternative)
    {
        switch (currentWeather)
        {
            case ASHFALL:
                switch(alternative)
                {
                    default:
                    case 1:
                        return ashModel;
                    case 2:
                        return ashModel2;
                    case 3:
                        return ashModel3;
                }
            case CLOUDY:
            case PARTLY_CLOUDY:
                switch(alternative)
                {
                    default:
                    case 1:
                        return cloudModel;
                    case 2:
                        return cloudModel2;
                    case 3:
                        return cloudModel3;
                }
            case STARRY:
                switch(alternative)
                {
                    default:
                    case 1:
                        return starModel;
                    case 2:
                        return starModel2;
                    case 3:
                        return starModel3;
                }
            case FOGGY:
                return fogModel;
            case SNOWY:
                switch (alternative)
                {
                    default:
                    case 1:
                        return snowModel;
                    case 2:
                        return snowModel2;
                    case 3:
                        return snowModel3;
                }
            case RAINY:
                switch (alternative)
                {
                    default:
                    case 1:
                        return rainModel;
                    case 2:
                        return rainModel2;
                    case 3:
                        return rainModel3;
                }
            case STORMY:
                switch (alternative)
                {
                    default:
                    case 1:
                        return stormModel;
                    case 2:
                        return stormModel2;
                    case 3:
                        return stormModel3;
                }
            default:
            case COVERED:
            case SUNNY:
                return null;
        }
    }

    public Animation getWeatherAnimation(Weather currentWeather)
    {
        switch (currentWeather)
        {
            case ASHFALL:
                return ashAnimation;
            case CLOUDY:
            case PARTLY_CLOUDY:
                return cloudAnimation;
            case STARRY:
                return starAnimation;
            case FOGGY:
                return fogAnimation;
            case SNOWY:
                return snowAnimation;
            case RAINY:
            case STORMY:
                return rainAnimation;
            default:
            case COVERED:
            case SUNNY:
                return null;
        }
    }

    public Model getTransparentModel(Weather weatherType, int objectVariant)
    {
        switch (weatherType)
        {
            default:
            case CLOUDY:
            case PARTLY_CLOUDY:
                switch (objectVariant)
                {
                    default:
                    case 1:
                        return cloudModelTP;
                    case 2:
                        return cloudModelTP2;
                    case 3:
                        return cloudModelTP3;
                }
            case STARRY:
                switch (objectVariant)
                {
                    default:
                    case 1:
                        return starModelTP;
                    case 2:
                        return starModelTP2;
                    case 3:
                        return starModelTP3;
                }
        }
    }

    public Model getRegularModel(Weather weatherType, int objectVariant)
    {
        switch (weatherType)
        {
            default:
            case CLOUDY:
            case PARTLY_CLOUDY:
                switch (objectVariant)
                {
                    default:
                    case 1:
                        return cloudModel;
                    case 2:
                        return cloudModel2;
                    case 3:
                        return cloudModel3;
                }
            case STARRY:
                switch (objectVariant)
                {
                    default:
                    case 1:
                        return starModel;
                    case 2:
                        return starModel2;
                    case 3:
                        return starModel3;
                }
        }
    }

    public int getModelRadius(Weather weatherType)
    {
        switch (weatherType)
        {
            default:
            case ASHFALL:
            case STARRY:
            case RAINY:
            case STORMY:
            case SNOWY:
                return 60;
            case CLOUDY:
            case PARTLY_CLOUDY:
                //Size should be 500 but auto-disappear makes it irrelevant
                return 60;
            case FOGGY:
                return 90;
        }
    }
}
