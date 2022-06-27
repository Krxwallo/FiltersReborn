package com.mrcrayfish.filters.helper;

import com.mrcrayfish.filters.Filters;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.GuiScreenEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ItemGroups {
    public static boolean getHasFilters(GuiScreenEvent event) {
        return Filters.get().hasFilters(getGroup(event));
    }

    @Nonnull
    public static CreativeModeTab getGroup(GuiScreenEvent event) {
        return requireItemGroup(((CreativeModeInventoryScreen) event.getGui()).getSelectedTab());
    }

    @Nullable
    public static CreativeModeTab getGroup(int index)
    {
        if(index < 0 || index >= CreativeModeTab.TABS.length)
            return null;
        return CreativeModeTab.TABS[index];
    }

    @Nonnull
    public static CreativeModeTab requireItemGroup(int index) {
        return Objects.requireNonNull(getGroup(index));
    }

    public static boolean getHasFilters(CreativeModeTab group) {
        return Filters.get().hasFilters(group);
    }
}
