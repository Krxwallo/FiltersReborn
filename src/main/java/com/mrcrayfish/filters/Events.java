package com.mrcrayfish.filters;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.filters.gui.widget.button.IconButton;
import com.mrcrayfish.filters.gui.widget.button.TagButton;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish & justAm0dd3r
 */
public class Events
{
    private static final Logger LOGGER = LogManager.getLogger();

    /*
    - func_238654_b_(MatrixStack p_238654_1_, List<? extends ITextProperties> p_238654_2_, int p_238654_3_, int p_238654_4_)
      equals renderToolTip()
    - func_238652_a_(stack, ITextProperties properties, int, int) equals renderToolTip()
     */
    private static final ResourceLocation ICONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/icons.png");
    private static final Map<ItemGroup, Integer> scrollMap = new HashMap<>();

    private boolean updatedFilters;
    private final List<TagButton> buttons = new ArrayList<>();
    private final Map<ItemGroup, FilterEntry> miscFilterMap = new HashMap<>();
    private IconButton btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll;
    private boolean viewingFilterTab;
    public boolean noFilters;

    @SubscribeEvent
    public void onPlayerLogout(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.updatedFilters = false;
    }

    @SubscribeEvent
    public void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            if(!this.updatedFilters)
            {
                this.updateFilters();
                this.updatedFilters = true;
            }

            this.viewingFilterTab = false;
            int guiCenterX = ((CreativeScreen) event.getGui()).getGuiLeft();
            int guiCenterY = ((CreativeScreen) event.getGui()).getGuiTop();

            event.addWidget(this.btnScrollUp = new IconButton(new TranslationTextComponent("gui.button.filters.scroll_filters_up"),
                    guiCenterX - 22, guiCenterY - 12, 16, 16, 0, 0,   0, ICONS, button -> scrollUp()));

            event.addWidget(this.btnScrollDown = new IconButton(new TranslationTextComponent("gui.button.filters.scroll_filters_down"),
                    guiCenterX - 22, guiCenterY + 127, 16, 16, 16, 0, 0, ICONS, button -> scrollDown()));

            event.addWidget(this.btnEnableAll = new IconButton(new TranslationTextComponent("gui.button.filters.enable_filters"),
                    guiCenterX - 50, guiCenterY + 10, 16, 16, 32, 0,  0, ICONS, button -> enableAllFilters()));

            event.addWidget(this.btnDisableAll = new IconButton(new TranslationTextComponent("gui.button.filters.disable_filters"),
                    guiCenterX - 50, guiCenterY + 32, 16, 16, 48, 0,  0, ICONS, button -> disableAllFilters()));

            this.hideButtons();

            CreativeScreen screen = (CreativeScreen) event.getGui();
            this.updateTagButtons(screen);

            ItemGroup group = this.getGroup(screen.getSelectedTabIndex());
            if(Filters.get().hasFilters(group))
            {
                this.showButtons();
                this.viewingFilterTab = true;
                this.updateItems(screen);
            }
        }
    }

    @SubscribeEvent
    public void onScreenClick(GuiScreenEvent.MouseClickedEvent.Pre event)
    {
        if(event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT)
            return;

        if(event.getGui() instanceof CreativeScreen)
        {
            for(TagButton button : this.buttons)
            {

                if(button.func_231047_b_(event.getMouseX(), event.getMouseY()))
                {
                   if(button.func_231048_c_(event.getMouseX(), event.getMouseY(), event.getButton()))
                    {
                        FilterEntry entry = button.getFilter();
                        entry.setEnabled(!entry.isEnabled());
                        button.updateState();
                        updateItems(((CreativeScreen) event.getGui()));
                        return;
                    }
                }

            }
        }
    }

    @SuppressWarnings("unused") // Gets called by javascript
    public void onCreativeTabChange(CreativeScreen screen, ItemGroup group)
    {
        if(Filters.get().hasFilters(group))
        {
            noFilters = false;
            this.updateItems(screen);
        }
        else {
            noFilters = true;
        }
        this.updateTagButtons(screen);
    }

    @SubscribeEvent
    public void onScreenDrawPre(GuiScreenEvent.DrawScreenEvent.Pre event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            CreativeScreen screen = (CreativeScreen) event.getGui();
            ItemGroup group = this.getGroup(screen.getSelectedTabIndex());

            if(Filters.get().hasFilters(group))
            {
                if(!this.viewingFilterTab)
                {
                    this.updateItems(screen);
                    this.viewingFilterTab = true;
                }
            }
            else
            {
                this.viewingFilterTab = false;
            }
        }
    }

    @SubscribeEvent
    public void onScreenDrawBackground(GuiContainerEvent.DrawBackground event)
    {
        if(event.getGuiContainer() instanceof CreativeScreen)
        {
            CreativeScreen screen = (CreativeScreen) event.getGuiContainer();
            ItemGroup group = this.getGroup(screen.getSelectedTabIndex());

            if(Filters.get().hasFilters(group))
            {
                /* Render buttons */
                this.buttons.forEach(TagButton::renderButton);
            }

            if(!noFilters) {
                //noinspection ConstantConditions // Color code is not null!
                Minecraft.getInstance().fontRenderer.func_238407_a_(event.getMatrixStack(), new TranslationTextComponent("gui.filters.message.main", Reference.NAME, Reference.VERSION), 3, 3, TextFormatting.WHITE.getColor());
            }
        }
    }

    @SubscribeEvent
    public void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            CreativeScreen screen = (CreativeScreen) event.getGui();
            ItemGroup group = this.getGroup(screen.getSelectedTabIndex());

            if(Filters.get().hasFilters(group))
            {
                /* Render tooltips after so it renders above buttons */

                this.buttons.forEach(button ->
                {
                    //if(button.isMouseOver(event.getMouseX(), event.getMouseY()))
                    if(button.func_231047_b_(event.getMouseX(), event.getMouseY()))
                    {
                        // screen.renderTooltip(button.getFilter().getName(), event.getMouseX(), event.getMouseY())
                        screenRenderToolTip(screen, /*button.getMatrixStack(), */button.getFilter().getName(), event.getMouseX(), event.getMouseY());
                    }
                });

                if(this.btnEnableAll.func_231047_b_(event.getMouseX(), event.getMouseY()))
                {
                    screenRenderToolTip(screen, this.btnEnableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }

                if(this.btnDisableAll.func_231047_b_(event.getMouseX(), event.getMouseY()))
                {
                    screenRenderToolTip(screen, this.btnDisableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }


            }
        }
    }

    private void screenRenderToolTip(CreativeScreen screen, String name, int mouseX, int mouseY) {
        // screen.func_238652_a_(stack, ITextProperties.func_240652_a_(name), mouseX, mouseY);
        screen.func_238652_a_(new MatrixStack(), ITextProperties.func_240652_a_(name), mouseX, mouseY);
    }

    @SubscribeEvent
    public void onMouseScroll(GuiScreenEvent.MouseScrollEvent.Pre event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            CreativeScreen creativeScreen = (CreativeScreen) event.getGui();
            int guiLeft = creativeScreen.getGuiLeft();
            int guiTop = creativeScreen.getGuiTop();
            int startX = guiLeft - 32;
            int startY = guiTop + 10;
            //noinspection UnnecessaryLocalVariable
            int endX = guiLeft;
            int endY = startY + 28 * 4 + 3;
            if(event.getMouseX() >= startX && event.getMouseX() < endX && event.getMouseY() >= startY && event.getMouseY() < endY)
            {
                if(event.getScrollDelta() > 0)
                {
                    this.scrollUp();
                }
                else
                {
                    this.scrollDown();
                }
                event.setCanceled(true);
            }
        }
    }

    private void updateTagButtons(CreativeScreen screen)
    {
        if(!this.updatedFilters)
            return;

        this.buttons.clear();
        ItemGroup group = this.getGroup(screen.getSelectedTabIndex());
        if(Filters.get().hasFilters(group))
        {
            List<FilterEntry> entries = this.getFilters(group);
            int scroll = scrollMap.computeIfAbsent(group, group1 -> 0);
            for(int i = scroll; i < scroll + 4 && i < entries.size(); i++)
            {
                TagButton button = new TagButton(screen.getGuiLeft() - 28, screen.getGuiTop() + 29 * (i - scroll) + 10, entries.get(i), button1 -> this.updateItems(screen));
                this.buttons.add(button);
            }

            this.btnScrollUp  .setEnabled(scroll > 0);
            this.btnScrollDown.setEnabled(scroll <= entries.size() - 4 - 1);
            this.showButtons();


        }
        else
        {
            this.hideButtons();
        }
    }

    private void updateItems(CreativeScreen screen)
    {
        LOGGER.debug("updateItems() called.");

        CreativeScreen.CreativeContainer container = screen.getContainer();
        Set<Item> filteredItems = new LinkedHashSet<>();
        ItemGroup group = this.getGroup(screen.getSelectedTabIndex());
        if(group != null)
        {
            if(Filters.get().hasFilters(group))
            {
                List<FilterEntry> entries = Filters.get().getFilters(group);
                if(entries != null)
                {
                    for(FilterEntry filter : this.getFilters(group))
                    {
                        if(filter.isEnabled())
                        {
                            filteredItems.addAll(filter.getItems());
                        }
                    }
                    container.itemList.clear();
                    filteredItems.forEach(item -> item.fillItemGroup(group, container.itemList));
                    container.itemList.sort(Comparator.comparingInt(o -> Item.getIdFromItem(o.getItem())));
                    container.scrollTo(0);
                }
            }
        }
    }

    private void updateFilters()
    {
        LOGGER.debug("updateFilters() called.");

        Filters.get().getGroups().forEach(group ->
        {
            List<FilterEntry> entries = Filters.get().getFilters(group);
            entries.forEach(FilterEntry::clear);

            Set<Item> removed = new HashSet<>();
            List<Item> items = ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item.getGroup() == group || item == Items.ENCHANTED_BOOK)
                .collect(Collectors.toList());
            items.forEach(item ->
            {
                for(ResourceLocation location : item.getTags())
                {
                    for(FilterEntry filter : entries)
                    {
                        if(location.equals(filter.getTag()))
                        {
                            filter.add(item);
                            removed.add(item);
                        }
                    }
                }
            });
            items.removeAll(removed);

            if(group.getRelevantEnchantmentTypes().length == 0)
            {
                items.remove(Items.ENCHANTED_BOOK);
            }

            if(!items.isEmpty())
            {
                FilterEntry entry = new FilterEntry(new ResourceLocation("miscellaneous"), new ItemStack(Blocks.BARRIER));
                items.forEach(entry::add);
                this.miscFilterMap.put(group, entry);
            }
        });
    }

    private ItemGroup getGroup(int index)
    {
        if(index < 0 || index >= ItemGroup.GROUPS.length)
            return null;
        return ItemGroup.GROUPS[index];
    }

    private List<FilterEntry> getFilters(ItemGroup group)
    {
        if(Filters.get().hasFilters(group))
        {
            List<FilterEntry> filters = new ArrayList<>(Filters.get().getFilters(group));
            if(this.miscFilterMap.containsKey(group))
            {
                filters.add(this.miscFilterMap.get(group));
            }
            return filters;
        }
        return Collections.emptyList();
    }

    private void showButtons()
    {
        LOGGER.debug("showButtons() called.");

        this.btnScrollUp.setActive(true);
        this.btnScrollDown.setActive(true);
        this.btnEnableAll.setActive(true);
        this.btnDisableAll.setActive(true);
        this.buttons.forEach(button -> button.setActive(true));
    }

    private void hideButtons()
    {
        LOGGER.debug("hideButtons() called.");

        this.btnScrollUp.setActive(false);
        this.btnScrollDown.setActive(false);
        this.btnEnableAll.setActive(false);
        this.btnDisableAll.setActive(false);
        this.buttons.forEach(button -> button.setActive(false));
    }

    private void scrollUp()
    {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof CreativeScreen)
        {
            CreativeScreen creativeScreen = (CreativeScreen) screen;
            ItemGroup group = this.getGroup(creativeScreen.getSelectedTabIndex());
            int scroll = scrollMap.computeIfAbsent(group, group1 -> 0);
            if(scroll > 0)
            {
                scrollMap.put(group, scroll - 1);
                this.updateTagButtons(creativeScreen);
            }
        }
    }

    private void scrollDown()
    {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof CreativeScreen)
        {
            CreativeScreen creativeScreen = (CreativeScreen) screen;
            ItemGroup group = this.getGroup(creativeScreen.getSelectedTabIndex());
            List<FilterEntry> entries = this.getFilters(group);
            //if(entries != null)
            //{
            int scroll = scrollMap.computeIfAbsent(group, group1 -> 0);
            if(scroll <= entries.size() - 4 - 1)
            {
                scrollMap.put(group, scroll + 1);
                this.updateTagButtons(creativeScreen);
            }
            //}
        }
    }

    private void enableAllFilters()
    {
        LOGGER.debug("enableAllFilters() called.");

        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof CreativeScreen)
        {
            CreativeScreen creativeScreen = (CreativeScreen) screen;
            ItemGroup group = this.getGroup(creativeScreen.getSelectedTabIndex());
            List<FilterEntry> entries = this.getFilters(group);
            //if(entries != null)
            //{
            entries.forEach(entry -> entry.setEnabled(true));
            this.buttons.forEach(TagButton::updateState);
            this.updateItems(creativeScreen);
            //}
        }
    }

    private void disableAllFilters()
    {
        LOGGER.debug("disableAllFilters() called.");

        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof CreativeScreen)
        {
            CreativeScreen creativeScreen = (CreativeScreen) screen;
            ItemGroup group = this.getGroup(creativeScreen.getSelectedTabIndex());
            List<FilterEntry> entries = this.getFilters(group);
            //if(entries != null)
            //{
            entries.forEach(filters -> filters.setEnabled(false));
            this.buttons.forEach(TagButton::updateState);
            this.updateItems(creativeScreen);
            //}
        }
    }
}
