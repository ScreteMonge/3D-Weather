package com.weather3d;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class LightningOverlay extends Overlay
{
    private final Client client;
    private final CyclesPlugin plugin;
    private final CyclesConfig config;

    @Inject
    private LightningOverlay(Client client, CyclesPlugin plugin, CyclesConfig config)
    {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.enableLightning() || !plugin.flashLightning)
        {
            return null;
        }

        Dimension dimensions = client.getRealDimensions();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, dimensions.width, dimensions.height);
        plugin.flashLightning = false;
        return null;
    }
}
