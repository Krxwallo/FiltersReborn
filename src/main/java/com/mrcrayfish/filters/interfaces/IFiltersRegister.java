package com.mrcrayfish.filters.interfaces;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IFiltersRegister {
    void register(ItemGroup group, @Nonnull ResourceLocation tag, ItemStack icon);
}
