package com.mrcrayfish.filters;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.filters.gui.widget.button.IconButton;
import com.mrcrayfish.filters.gui.widget.button.TagButton;
import com.mrcrayfish.filters.helper.TabHelper;
import com.mrcrayfish.filters.web.LinkManager;
import mezz.jei.events.BookmarkOverlayToggleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.*;

/**
 * Author: MrCrayfish & justAm0dd3r
 */
public class Events {
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
    private final Map<CreativeModeTab, FilterEntry> miscFilterMap = new HashMap<>();
    private IconButton btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll;
    public boolean filtersEnabled;
    private boolean bookMarkOverlayEnabled = true; // Jei Bookmark Overlay

    @SubscribeEvent
    public void onPlayerLogout(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        this.updatedFilters = false;
    }

    @SubscribeEvent
    public void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof CreativeModeInventoryScreen screen) {
            if (!this.updatedFilters) {
                this.updateFilters();
                this.updatedFilters = true;
            }

            int guiCenterX = ((CreativeModeInventoryScreen) event.getGui()).getGuiLeft();
            int guiCenterY = ((CreativeModeInventoryScreen) event.getGui()).getGuiTop();

            event.addWidget(this.btnScrollUp = new IconButton(new TranslatableComponent("gui.button.filters.scroll_filters_up"),
                    guiCenterX - 22, guiCenterY - 12, 16, 16, 0, 0,   0, ICONS, button -> scrollUp()));

            event.addWidget(this.btnScrollDown = new IconButton(new TranslatableComponent("gui.button.filters.scroll_filters_down"),
                    guiCenterX - 22, guiCenterY + 127, 16, 16, 16, 0, 0, ICONS, button -> scrollDown()));

            event.addWidget(this.btnEnableAll = new IconButton(new TranslatableComponent("gui.button.filters.enable_filters"),
                    guiCenterX - 50, guiCenterY + 10, 16, 16, 32, 0,  0, ICONS, button -> enableAllFilters()));

            event.addWidget(this.btnDisableAll = new IconButton(new TranslatableComponent("gui.button.filters.disable_filters"),
                    guiCenterX - 50, guiCenterY + 32, 16, 16, 48, 0,  0, ICONS, button -> disableAllFilters()));

            this.hideButtons();

            this.updateTagButtons(screen);

            if (TabHelper.hasFilters(event)) {
                this.showButtons();
                this.updateItems(screen);
            }
        }
    }

    @SubscribeEvent
    public void onScreenClick(GuiScreenEvent.MouseClickedEvent.Pre event) {
        if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;

        if (event.getGui() instanceof CreativeModeInventoryScreen)
        {
            if (filtersEnabled && isMouseOverText(((int) event.getMouseX()), ((int) event.getMouseY())))
                LinkManager.openCurseforgeLink();

            for (TagButton button : this.buttons)
            {

                if (button.isMouseOver(event.getMouseX(), event.getMouseY()))
                {
                   if (button.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton()))
                    {
                        FilterEntry entry = button.getFilter();
                        entry.setEnabled(!entry.isEnabled());
                        button.updateState();
                        updateItems(((CreativeModeInventoryScreen) event.getGui()));
                        return;
                    }
                }

            }
        }
    }

    @SuppressWarnings("unused") // Gets called by coremod // TODO call by mixin xd
    public void onCreativeTabChange(CreativeModeInventoryScreen screen, CreativeModeTab tab) {
        if (TabHelper.hasFilters(tab))
        {
            filtersEnabled = true;
            this.updateItems(screen);
        }
        else filtersEnabled = false;
        this.updateTagButtons(screen);
    }

    /**
     * Update the screen items when the gui doesn't have filters.
     * @param event the subscribe event
     */
    @SubscribeEvent
    public void onScreenDrawPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (event.getGui() instanceof CreativeModeInventoryScreen)
        {
            if (!TabHelper.hasFilters(event))
            {
                CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) event.getGui();
                this.updateItems(screen);
            }
        }
    }

    /**
     * Used for rendering the buttons (calling the TagButton::renderButton method on each one) and for rendering the
     * "Filters Reborn vX.X.X" text. All this is only done for creative tabs with filters available.
     * @param event the corresponding {@code SubscribeEvent} from forge
     */
    @SubscribeEvent
    public void onScreenDrawBackground(GuiContainerEvent.DrawBackground event) {
        if (event.getGuiContainer() instanceof CreativeModeInventoryScreen screen)
        {

            CreativeModeTab tab = TabHelper.getTab(screen.getSelectedTab());

            if (Filters.get().hasFilters(tab))
            {
                // Filters are available for this creative tab. Now we can render the buttons and the text.

                // Render Buttons FIXME when on page 2 one button gets rendered correctly xd
                this.buttons.forEach(TagButton::renderButton);

                // Get the right text x -> check if the jei bookmark overlay is enabled
                int x = 3;
                if (ModList.get().isLoaded("jei")) {
                    if (bookMarkOverlayEnabled) {
                        // Overlay is enabled. "Move" the text to the right to avoid overlapping
                        x = 140;
                    }
                }

                /*GL11.glPushMatrix();
                // Change the text size
                GL11.glScalef(1, 1, 1);
                // Render the text
                //noinspection ConstantConditions // TextFormatting.WHITE.getColor() will never cause NullPointerException
                Minecraft.getInstance().font.draw(event.getMatrixStack(),
                        new TranslatableComponent("gui.filters.message.main", Reference.NAME, Reference.VERSION),
                        x, 3, ChatFormatting.WHITE.getColor());
                GL11.glPopMatrix();*/
            }
        }
    }

    /**
     * Used for rendering the tooltips when the mouse is over a button.
     * @param event The subscribe event from forge. contains the screen.
     */
    @SubscribeEvent
    public void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof CreativeModeInventoryScreen screen)
        {
            var tab = TabHelper.getTab(screen.getSelectedTab());

            if (Filters.get().hasFilters(tab)) {
                // Render tooltips after so they render above buttons

                this.buttons.forEach(button -> {
                    // Is the mouse over the button?
                    if (button.isMouseOver(event.getMouseX(), event.getMouseY())) {
                        // Render the tooltip
                        screenRenderToolTip(screen, button.getFilter().getName(), event.getMouseX(), event.getMouseY());
                    }
                });

                if (this.btnEnableAll.isMouseOver(event.getMouseX(), event.getMouseY())) {
                    // Render the "Enable all filters" tooltip
                    screenRenderToolTip(screen, this.btnEnableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }

                if (this.btnDisableAll.isMouseOver(event.getMouseX(), event.getMouseY())) {
                    // Render the "Disable all filters" tooltip
                    screenRenderToolTip(screen, this.btnDisableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }

                if (isMouseOverText(event.getMouseX(), event.getMouseY())) {
                    // Render the "Open Curseforge Website" tooltip
                    screenRenderToolTip(((CreativeModeInventoryScreen) event.getGui()),
                        new TranslatableComponent("gui.filters.message.main.tooltip"), event.getMouseX(), event.getMouseY());
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

    private void screenRenderToolTip(CreativeModeInventoryScreen screen, Component name, int mouseX, int mouseY) {
        // screen.func_238652_a_(stack, ITextProperties.func_240652_a_(name), mouseX, mouseY);
        screen.renderTooltip(new PoseStack(), name, mouseX, mouseY);
    }

    @SubscribeEvent
    public void onMouseScroll(GuiScreenEvent.MouseScrollEvent.Pre event) {
        if (event.getGui() instanceof CreativeModeInventoryScreen creativeScreen)
        {
            int guiLeft = creativeScreen.getGuiLeft();
            int guiTop = creativeScreen.getGuiTop();
            int startX = guiLeft - 32;
            int startY = guiTop + 10;
            //noinspection UnnecessaryLocalVariable
            int endX = guiLeft;
            int endY = startY + 28 * 4 + 3;
            if (event.getMouseX() >= startX && event.getMouseX() < endX && event.getMouseY() >= startY && event.getMouseY() < endY)
            {
                if (event.getScrollDelta() > 0) this.scrollUp();
                else this.scrollDown();

                event.setCanceled(true);
            }
        }
    }

    private void updateTagButtons(CreativeModeInventoryScreen screen) {
        if (!this.updatedFilters) return;

        this.buttons.clear();
        var tab = TabHelper.getTab(screen.getSelectedTab());
        if (Filters.get().hasFilters(tab)) {
            List<FilterEntry> entries = getFilters(tab);
            int scroll = scrollMap.computeIfAbsent(tab, tab1 -> 0);
            for (int i = scroll; i < scroll + 4 && i < entries.size(); i++) {
                TagButton button = new TagButton(screen.getGuiLeft() - 28, screen.getGuiTop() + 29 * (i - scroll) + 10, entries.get(i), button1 -> this.updateItems(screen));
                this.buttons.add(button);
            }

            this.btnScrollUp  .setEnabled(scroll > 0);
            this.btnScrollDown.setEnabled(scroll <= entries.size() - 4 - 1);
            this.showButtons();


        }
        else {
            this.hideButtons();
        }
    }

    private void updateItems(CreativeModeInventoryScreen screen) {
        LOGGER.debug("updateItems() called.");

        CreativeModeInventoryScreen.ItemPickerMenu menu = screen.getMenu();
        Set<Item> filteredItems = new LinkedHashSet<>();
        CreativeModeTab tab = TabHelper.getTab(screen.getSelectedTab());
        if (tab != null) {
            if (Filters.get().hasFilters(tab)) {
                List<FilterEntry> entries = Filters.get().getFilters(tab);
                if (entries != null) {
                    for (FilterEntry filter : this.getFilters(tab)) {
                        if (filter.isEnabled()) {
                            filteredItems.addAll(filter.getItems());
                        }
                    }
                    menu.items.clear();
                    filteredItems.forEach(item -> item.fillItemCategory(tab, menu.items));
                    menu.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem())));
                    menu.scrollTo(0);
                }
            }
        }
    }

    private void updateFilters() {
        LOGGER.debug("updateFilters() called.");
        for (CreativeModeTab tab : Filters.get().getTabs()) {
            List<FilterEntry> entries = Filters.get().getFilters(tab);
            entries.forEach(FilterEntry::clear);

            Set<Item> removed = new HashSet<>();
            ForgeRegistries.ITEMS.forEach(item -> {
                var tabs = item.getCreativeTabs();
                if (tabs.contains(tab) || item == Items.ENCHANTED_BOOK) {
                    for (var id : item.getTags()) {
                        for (var filter : entries) {
                            if (id.equals(filter.getTag())) {
                                filter.add(item);
                                removed.add(item);
                            }
                        }
                    }
                }
            });

            List<Item> items = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
            items.removeAll(removed);
            //Collection<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(i -> !removed.contains(i)).toList();

            if (tab.getEnchantmentCategories().length == 0) items.remove(Items.ENCHANTED_BOOK);

            if (!items.isEmpty()) {
                FilterEntry entry = new FilterEntry(new ResourceLocation("miscellaneous"), new ItemStack(Blocks.BARRIER));
                items.forEach(entry::add);
                this.miscFilterMap.put(tab, entry);
            }
        }
    }

    private List<FilterEntry> getFilters(CreativeModeTab tab) {
        if (Filters.get().hasFilters(tab)) {
            List<FilterEntry> filters = new ArrayList<>(Filters.get().getFilters(tab));
            if (this.miscFilterMap.containsKey(tab)) {
                filters.add(this.miscFilterMap.get(tab));
            }
            return filters;
        }
        return Collections.emptyList();
    }

    private void showButtons() {
        LOGGER.debug("showButtons() called.");

        this.btnScrollUp.setActive(true);
        this.btnScrollDown.setActive(true);
        this.btnEnableAll.setActive(true);
        this.btnDisableAll.setActive(true);
        this.buttons.forEach(button -> button.setActive(true));
    }

    private void hideButtons() {
        LOGGER.debug("hideButtons() called.");

        this.btnScrollUp.setActive(false);
        this.btnScrollDown.setActive(false);
        this.btnEnableAll.setActive(false);
        this.btnDisableAll.setActive(false);
        this.buttons.forEach(button -> button.setActive(false));
    }

    private void scrollUp() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = TabHelper.getTab(creativeScreen.getSelectedTab());
            int scroll = scrollMap.computeIfAbsent(tab, tab1 -> 0);
            if (scroll > 0) {
                scrollMap.put(tab, scroll - 1);
                this.updateTagButtons(creativeScreen);
            }
        }
    }

    private void scrollDown() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = TabHelper.getTab(creativeScreen.getSelectedTab());
            List<FilterEntry> entries = this.getFilters(tab);
            int scroll = scrollMap.computeIfAbsent(tab, tab1 -> 0);
            if (scroll <= entries.size() - 4 - 1) {
                scrollMap.put(tab, scroll + 1);
                this.updateTagButtons(creativeScreen);
            }
        }
    }

    private void enableAllFilters() {
        LOGGER.debug("enableAllFilters() called.");

        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = TabHelper.getTab(creativeScreen.getSelectedTab());
            List<FilterEntry> entries = this.getFilters(tab);
            entries.forEach(entry -> entry.setEnabled(true));
            this.buttons.forEach(TagButton::updateState);
            this.updateItems(creativeScreen);
        }
    }

    private void disableAllFilters() {
        LOGGER.debug("disableAllFilters() called.");

        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
            CreativeModeTab tab = TabHelper.getTab(creativeScreen.getSelectedTab());
            List<FilterEntry> entries = this.getFilters(tab);
            entries.forEach(filters -> filters.setEnabled(false));
            this.buttons.forEach(TagButton::updateState);
            this.updateItems(creativeScreen);
        }
    }

    @SubscribeEvent
    public void onBookMarkOverlayToggle(BookmarkOverlayToggleEvent event) {
        LOGGER.debug("onBookMarkOverlayToggle() called.");
        bookMarkOverlayEnabled = event.isBookmarkOverlayEnabled();
    }
}
