package com.mrcrayfish.filters.helper;

import com.mrcrayfish.filters.Filters;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.GuiScreenEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ItemGroups {
    public static boolean getHasFilters(GuiScreenEvent event) {
        return Filters.get().hasFilters(getGroup(event));
    }

    @Nonnull
    public static ItemGroup getGroup(GuiScreenEvent event) {
        return requireItemGroup(((CreativeScreen) event.getGui()).getSelectedTabIndex());
    }

    @Nullable
    public static ItemGroup getGroup(int index)
    {
        if(index < 0 || index >= ItemGroup.GROUPS.length)
            return null;
        return ItemGroup.GROUPS[index];
    }

    @Nonnull
    public static ItemGroup requireItemGroup(int index) {
        return Objects.requireNonNull(getGroup(index));
    }

    public static boolean getHasFilters(ItemGroup group) {
        return Filters.get().hasFilters(group);
    }
}
