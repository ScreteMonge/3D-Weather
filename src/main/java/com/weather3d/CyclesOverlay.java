package com.weather3d;

import com.weather3d.conditions.Biomes;
import com.weather3d.conditions.Seasons;
import com.weather3d.conditions.Weathers;
import net.runelite.api.Client;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CyclesOverlay extends OverlayPanel
{
    private final Client client;
    private final CyclesPlugin plugin;
    private final CyclesConfig config;
    private final Font RUNESCAPE_SMALL_FONT = FontManager.getRunescapeSmallFont();
    private final BufferedImage CONDITIONS_BACKGROUND = ImageUtil.loadImageResource(getClass(), "/Conditions Background.png");
    private final BufferedImage BLANK_BACKGROUND = ImageUtil.loadImageResource(getClass(), "/Metrics_Background.png");

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
        if (!config.toggleOverlay())
        {
            return null;
        }

        Weathers currentWeather = plugin.getCurrentWeather();
        Biomes currentBiome = plugin.getCurrentBiome();
        Seasons currentSeason = plugin.getCurrentSeason();

        panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));
        graphics.setFont(RUNESCAPE_SMALL_FONT);

        if (currentWeather == null || currentBiome == null || currentSeason == null)
        {
            return super.render(graphics);
        }

        int panelDivisionWidth = panelComponent.getPreferredSize().width / 3;
        int xShift = 10;
        int yShift = 9;

        panelComponent.getChildren().add(new ImageComponent(BLANK_BACKGROUND));
        graphics.drawImage(CONDITIONS_BACKGROUND, 0, 0, null);
        graphics.drawImage(currentWeather.getConditionImage(), xShift, yShift, null);
        graphics.drawImage(currentBiome.getConditionImage(), xShift + panelDivisionWidth, yShift, null);
        graphics.drawImage(currentSeason.getConditionImage(), xShift + panelDivisionWidth * 2, yShift, null);

        graphics.setColor(Color.BLACK);
        graphics.drawString("Weather", 9, 62);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Weather", 8, 61);

        graphics.setColor(Color.BLACK);
        graphics.drawString("Biome", 58, 62);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Biome", 57, 61);

        graphics.setColor(Color.BLACK);
        graphics.drawString("Season", 97, 62);
        graphics.setColor(Color.WHITE);
        graphics.drawString("Season", 96, 61);

        return super.render(graphics);
    }
}
