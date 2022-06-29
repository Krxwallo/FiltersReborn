package com.mrcrayfish.filters;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;

/**
 * Author: MrCrayfish feat. justAm0dd3r
 */
// Mixin Hooks
public class Hooks
{
    private static int animation = 48;

    public static int getPotionEffectOffset(@SuppressWarnings("rawtypes") EffectRenderingInventoryScreen screen)
    {
        if (!(screen instanceof CreativeModeInventoryScreen)) return 172;
        if (!Filters.get().events.filtersEnabled) {
            if (animation == 48) return 172;
            else animation += 3;
        }
        else {
            if (animation == 0) return 124;
            else animation -= 3;
        }
        return 124 + animation;
    }

    public static int getEffectsGuiOffset(@SuppressWarnings("rawtypes") EffectRenderingInventoryScreen screen)
    {
        if (screen instanceof CreativeModeInventoryScreen) {
            return 182;
        }
        return 160;
    }
}
