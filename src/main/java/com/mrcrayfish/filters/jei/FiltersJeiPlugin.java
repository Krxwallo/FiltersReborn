package com.mrcrayfish.filters.jei;

import com.mrcrayfish.filters.Filters;
import com.mrcrayfish.filters.Reference;
import com.mrcrayfish.filters.gui.widget.button.IconButton;
import com.mrcrayfish.filters.gui.widget.button.TagButton;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Ocelot
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
        registration.addGuiContainerHandler(CreativeScreen.class, new IGuiContainerHandler<CreativeScreen>()
        {
            @Nonnull
            @Override
            public List<Rectangle2d> getGuiExtraAreas(@Nonnull CreativeScreen screen)
            {
                if (Filters.get().hasFilters(ItemGroup.GROUPS[screen.getSelectedTabIndex()]))
                {
                    List<Rectangle2d> areas = new ArrayList<>();

                    /* Tabs */
                    areas.add(new Rectangle2d(screen.getGuiLeft() - 28, screen.getGuiTop() + 10, 56, 230));

                    /* Buttons */
                    for (IGuiEventListener child : screen.func_231039_at__()) // screen.children()
                    {
                        if (child instanceof IconButton)
                        {
                            IconButton button = (IconButton) child;
                            areas.add(new Rectangle2d(button.x, button.y, button.func_230998_h_()/*.getWidth()*/, button.getHeight()));
                        }

                        if (child instanceof TagButton)
                        {
                            TagButton button = (TagButton) child;
                            areas.add(new Rectangle2d(button.x, button.y, button.func_230998_h_()/*.getWidth()*/, button.getHeight()));
                        }
                    }

                    return areas;
                }
                return Collections.emptyList();
            }
        });
    }
}
