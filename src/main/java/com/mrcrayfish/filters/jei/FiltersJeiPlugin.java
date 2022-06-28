package com.mrcrayfish.filters.jei;

import com.mrcrayfish.filters.Filters;
import com.mrcrayfish.filters.Reference;
import com.mrcrayfish.filters.gui.widget.button.IconButton;
import com.mrcrayfish.filters.gui.widget.button.TagButton;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Ocelot feat. justAm0dd3r
 */
@JeiPlugin
public class FiltersJeiPlugin implements IModPlugin
{
    @Nonnull
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addGuiContainerHandler(CreativeModeInventoryScreen.class, new IGuiContainerHandler<>() {
            @Nonnull
            @Override
            public List<Rect2i> getGuiExtraAreas(@Nonnull CreativeModeInventoryScreen screen) {
                if (Filters.get().hasFilters(CreativeModeTab.TABS[screen.getSelectedTab()])) {
                    List<Rect2i> areas = new ArrayList<>();

                    /* Tabs */
                    areas.add(new Rect2i(screen.getGuiLeft() - 28, screen.getGuiTop() + 10, 56, 230));

                    /* Buttons */
                    for (GuiEventListener child : screen.children()) {
                        if (child instanceof IconButton button) {
                            areas.add(new Rect2i(button.x, button.y, button.getWidth(), button.getHeight()));
                        }

                        if (child instanceof TagButton button) {
                            areas.add(new Rect2i(button.x, button.y, button.getWidth(), button.getHeight()));
                        }
                    }

                    return areas;
                }
                return Collections.emptyList();
            }
        });
    }
}
