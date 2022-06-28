package com.mrcrayfish.filters;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;

/**
 * Author: MrCrayfish feat. justAm0dd3r
 */
@SuppressWarnings("unused") // Coremod hooks.
public class Hooks
{
    private static int animation = 48;

    public static int getPotionEffectOffset(@SuppressWarnings("rawtypes") EffectRenderingInventoryScreen screen)
    {
        if (screen instanceof CreativeModeInventoryScreen && !Filters.get().events.noFilters) {
            animation = 48;
            return 172;
        }
        if (animation == 0) return 124;
        else {
            animation -= 3;
            return 124 + animation;
        }
    }

    public static int getEffectsGuiOffset(@SuppressWarnings("rawtypes") EffectRenderingInventoryScreen screen)
    {
        if (screen instanceof CreativeModeInventoryScreen) {
            return 182;
        }
        return 160;
    }
}
