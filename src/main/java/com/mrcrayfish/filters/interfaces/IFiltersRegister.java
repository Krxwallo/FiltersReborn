package com.mrcrayfish.filters.interfaces;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public interface IFiltersRegister {
    void register(CreativeModeTab tab, @Nonnull ResourceLocation tag, ItemStack icon);
}
