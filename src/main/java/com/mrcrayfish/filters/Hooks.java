package com.mrcrayfish.filters;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;

/**
 * Author: MrCrayfish
 */
public class Hooks
{
    public static int getPotionEffectOffset(@SuppressWarnings("rawtypes") DisplayEffectsScreen screen)
    {
        if(screen instanceof CreativeScreen)
        {
            return 172;
        }
        return 124;
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
