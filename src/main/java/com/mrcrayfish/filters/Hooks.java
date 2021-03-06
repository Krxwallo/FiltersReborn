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
    private static int animation = 48;

    public static int getPotionEffectOffset(@SuppressWarnings("rawtypes") DisplayEffectsScreen screen)
    {
        if(screen instanceof CreativeScreen && !Filters.get().events.noFilters)
        {
            animation = 48;
            return 172;
        }
        if (animation == 0) return 124;
        else {
            animation -= 3;
            return 124 + animation;
        }
    }

    public static int getEffectsGuiOffset(@SuppressWarnings("rawtypes") DisplayEffectsScreen screen)
    {
        if(screen instanceof CreativeScreen)
        {
            return 182;
        }
        return 160;
    }
}
