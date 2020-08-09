package com.mrcrayfish.filters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mrcrayfish.filters.client.Client;
import com.mrcrayfish.filters.interfaces.IFiltersRegister;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author: MrCrayfish feat. justAm0dd3r
 */
@Mod(Reference.MOD_ID)
public class Filters implements IFiltersRegister
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static Filters instance;

    private final Map<ItemGroup, Set<FilterEntry>> filterMap = new HashMap<>();
    public Events events;

    public Filters()
    {
        LOGGER.info(Reference.NAME + " Version " + Reference.VERSION + " by " + Reference.AUTHOR + " started up.");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.register(this.events = new Events());
        Filters.instance = this;
    }

    private void onClientSetup(FMLClientSetupEvent event) { Client.setup(this); }

    public static Filters get() { return instance; }

    @Override
    public void register(ItemGroup group, @Nonnull ResourceLocation tag, ItemStack icon)
    {
        Set<FilterEntry> entries = this.filterMap.computeIfAbsent(group, itemGroup -> new LinkedHashSet<>());
        entries.add(new FilterEntry(tag, icon));
    }

    public Set<ItemGroup>             getGroups()                 { return ImmutableSet.copyOf(this.filterMap.keySet()); }
    public ImmutableList<FilterEntry> getFilters(ItemGroup group) { return ImmutableList.copyOf(this.filterMap.get(group)); }
    public boolean                    hasFilters(ItemGroup group) { return this.filterMap.containsKey(group); }
}
