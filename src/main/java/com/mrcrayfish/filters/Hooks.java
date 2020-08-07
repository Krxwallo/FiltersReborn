package com.mrcrayfish.filters;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: MrCrayfish feat. justAm0dd3r
 */
@SuppressWarnings("unused") // Coremod hooks.
public class Hooks
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static int getPotionEffectOffset(@SuppressWarnings("rawtypes") DisplayEffectsScreen screen)
    {
        LOGGER.debug("getPotionEffectOffset() called.");
        if(screen instanceof CreativeScreen)
        {
            return 172;
        }
        return 124;
    }

    public static int getEffectsGuiOffset(@SuppressWarnings("rawtypes") DisplayEffectsScreen screen)
    {
        LOGGER.debug("getEffectsGuiOffset() called.");
        if(screen instanceof CreativeScreen)
        {
            return 182;
        }
        return 160;
    }
}
