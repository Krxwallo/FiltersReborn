package com.mrcrayfish.filters;

import com.mojang.datafixers.types.templates.List;
import com.mrcrayfish.filters.gui.widget.button.IconButton;
import com.mrcrayfish.filters.gui.widget.button.TagButton;
import com.mrcrayfish.filters.helper.ItemGroups;
import com.mrcrayfish.filters.web.LinkManager;
import mezz.jei.events.BookmarkOverlayToggleEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private static final Map<CreativeModeTab, Integer> scrollMap = new HashMap<>();

    private boolean updatedFilters;
    private final List<TagButton> buttons = new ArrayList<>();
    private final Map<ItemGroup, FilterEntry> miscFilterMap = new HashMap<>();
    private IconButton btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll;
    public boolean noFilters;
    private boolean bookMarkOverlayEnabled = true; // Jei Bookmark Overlay

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

            if(ItemGroups.getHasFilters(event))
            {
                this.showButtons();
                this.updateItems(screen);
            }
        }
    }

    @SubscribeEvent
    public void onScreenClick(GuiScreenEvent.MouseClickedEvent.Pre event)
    {
        if(event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;

        if(event.getGui() instanceof CreativeScreen)
        {
            if (!noFilters && isMouseOverText(((int) event.getMouseX()), ((int) event.getMouseY())))
                LinkManager.openCurseforgeLink();

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

    @SuppressWarnings("unused") // Gets called by coremod
    public void onCreativeTabChange(CreativeScreen screen, ItemGroup group)
    {
        if(ItemGroups.getHasFilters(group))
        {
            noFilters = false;
            this.updateItems(screen);
        }
        else {
            noFilters = true;
        }
        this.updateTagButtons(screen);
    }

    /**
     * Update the screen items when the gui doesn't have filters.
     * @param event the subscribe event
     */
    @SubscribeEvent
    public void onScreenDrawPre(GuiScreenEvent.DrawScreenEvent.Pre event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            if(!ItemGroups.getHasFilters(event))
            {
                CreativeScreen screen = (CreativeScreen) event.getGui();
                this.updateItems(screen);
            }
        }
    }

    /**
     * Used for rendering the buttons (calling the TagButton::renderButton method on each one) and for rendering the
     * "Filters Reborn vX.X.X" text. All this is only done for creative tabs with filters available.
     * @param event the corresponding subscribe event from forge
     */
    @SubscribeEvent
    public void onScreenDrawBackground(GuiContainerEvent.DrawBackground event)
    {
        if(event.getGuiContainer() instanceof CreativeScreen)
        {
            CreativeScreen screen = (CreativeScreen) event.getGuiContainer();

            ItemGroup group = ItemGroups.getGroup(screen.getSelectedTabIndex());

            if(Filters.get().hasFilters(group))
            {
                // Filters are available for this creative tab. Now we can render the buttons and the text.

                // Render Buttons
                this.buttons.forEach(TagButton::renderButton);

                // Get the right text x -> check if the jei bookmark overlay is enabled
                int x = 3;
                if (ModList.get().isLoaded("jei")) {
                    if (bookMarkOverlayEnabled) {
                        // Overlay is enabled. "Move" the text to the right to avoid overlapping
                        x = 140;
                    }
                }

                GL11.glPushMatrix();
                // Change the text size
                GL11.glScalef(1, 1, 1);
                // Render the text
                //noinspection ConstantConditions // TextFormatting.WHITE.getColor() will never cause NullPointerException
                Minecraft.getInstance().fontRenderer.func_238407_a_(event.getMatrixStack(), new TranslationTextComponent("gui.filters.message.main", Reference.NAME, Reference.VERSION), x, 3, TextFormatting.WHITE.getColor());
                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Used for rendering the tooltips when the mouse is over a button.
     * @param event The subscribe event from forge. contains the screen.
     */
    @SubscribeEvent
    public void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            CreativeScreen screen = (CreativeScreen) event.getGui();
            ItemGroup group = ItemGroups.getGroup(screen.getSelectedTabIndex());

            if(Filters.get().hasFilters(group))
            {
                /* Render tooltips after so it renders above buttons */

                this.buttons.forEach(button ->
                {
                    // Is the mouse over the button?
                    if(button.func_231047_b_(event.getMouseX(), event.getMouseY()))
                    {
                        // Render the tooltip
                        screenRenderToolTip(screen, button.getFilter().getName(), event.getMouseX(), event.getMouseY());
                    }
                });

                if(this.btnEnableAll.func_231047_b_(event.getMouseX(), event.getMouseY()))
                {
                    // Render the "Enable all filters" tooltip
                    screenRenderToolTip(screen, this.btnEnableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }

                if(this.btnDisableAll.func_231047_b_(event.getMouseX(), event.getMouseY()))
                {
                    // Render the "Disable all filters" tooltip
                    screenRenderToolTip(screen, this.btnDisableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }

                if (isMouseOverText(event.getMouseX(), event.getMouseY())) {
                    // Render the "Open Curseforge Website" tooltip
                    screenRenderToolTip(((CreativeScreen) event.getGui()),
                        new TranslationTextComponent("gui.filters.message.main.tooltip").getString(), event.getMouseX(), event.getMouseY());
                }
            }
        }
    }

    private boolean isMouseOverText(int mouseX, int mouseY) {
        int textStartX = 1;
        int textStartY = 1;
        if (bookMarkOverlayEnabled) textStartX = 140;
        int textLengthX = 110;
        int textLengthY = 10;

        int textEndX = textStartX + textLengthX;
        int textEndY = textStartY + textLengthY;

        return mouseX > textStartX && mouseX < textEndX && mouseY > textStartY && mouseY < textEndY;
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
        ItemGroup group = ItemGroups.getGroup(screen.getSelectedTabIndex());
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
        ItemGroup group = ItemGroups.getGroup(screen.getSelectedTabIndex());
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
            ItemGroup group = ItemGroups.getGroup(creativeScreen.getSelectedTabIndex());
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
            ItemGroup group = ItemGroups.getGroup(creativeScreen.getSelectedTabIndex());
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
            ItemGroup group = ItemGroups.getGroup(creativeScreen.getSelectedTabIndex());
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
            ItemGroup group = ItemGroups.getGroup(creativeScreen.getSelectedTabIndex());
            List<FilterEntry> entries = this.getFilters(group);
            //if(entries != null)
            //{
            entries.forEach(filters -> filters.setEnabled(false));
            this.buttons.forEach(TagButton::updateState);
            this.updateItems(creativeScreen);
            //}
        }
    }

    @SubscribeEvent
    public void onBookMarkOverlayToggle(BookmarkOverlayToggleEvent event) {
        LOGGER.debug("onBookMarkOverlayToggle() called.");
        bookMarkOverlayEnabled = event.isBookmarkOverlayEnabled();
    }
}
