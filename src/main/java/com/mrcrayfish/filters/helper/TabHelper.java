package com.mrcrayfish.filters.helper;

import com.mrcrayfish.filters.Filters;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.GuiScreenEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class TabHelper {
    public static boolean hasFilters(GuiScreenEvent event) {
        return Filters.get().hasFilters(getTab(event));
    }

    @Nonnull
    public static CreativeModeTab getTab(GuiScreenEvent event) {
        return requireItemGroup(((CreativeModeInventoryScreen) event.getGui()).getSelectedTab());
    }

    @Nullable
    public static CreativeModeTab getTab(int index)
    {
        if (index < 0 || index >= CreativeModeTab.TABS.length)
            return null;
        return CreativeModeTab.TABS[index];
    }

    @Nonnull
    public static CreativeModeTab requireItemGroup(int index) {
        return Objects.requireNonNull(getTab(index));
    }

    public static boolean hasFilters(CreativeModeTab group) {
        return Filters.get().hasFilters(group);
    }
}
