package com.weather3d;

import com.weather3d.conditions.Biome;
import com.weather3d.conditions.Season;
import com.weather3d.conditions.Weather;
import net.runelite.api.Client;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CyclesOverlay extends OverlayPanel
{
    private final Client client;
    private final CyclesPlugin plugin;
    private final CyclesConfig config;

    @Inject
    private CyclesOverlay(Client client, CyclesPlugin plugin, CyclesConfig config)
    {
        super(plugin);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.toggleOverlay() || panelComponent == null)
        {
            return null;
        }

        Weather weather = plugin.getCurrentWeather();
        Biome biome = plugin.getCurrentBiome();
        Season season = plugin.getCurrentSeason();

        if (config.miniOverlay())
        {
            renderMiniOverlay(weather, biome, season);
        }
        else
        {
            renderOverlay(weather, biome, season);
        }

        return super.render(graphics);
    }

    private void renderOverlay(Weather weather, Biome biome, Season season)
    {
        panelComponent.getChildren().add(new ImageComponent(weather.getConditionImage()));
        panelComponent.getChildren().add(new ImageComponent(biome.getConditionImage()));
        panelComponent.getChildren().add(new ImageComponent(season.getConditionImage()));
    }

    private void renderMiniOverlay(Weather weather, Biome biome, Season season)
    {
        panelComponent.getChildren().add(new ImageComponent(weather.getMiniConditionImage()));
        panelComponent.getChildren().add(new ImageComponent(biome.getMiniConditionImage()));
        panelComponent.getChildren().add(new ImageComponent(season.getMiniConditionImage()));
    }
}
