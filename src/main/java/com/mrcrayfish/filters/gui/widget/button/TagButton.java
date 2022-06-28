package com.mrcrayfish.filters.gui.widget.button;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mrcrayfish.filters.FilterEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Author: MrCrayfish
 */
public class TagButton extends Button
{
    private static final ResourceLocation TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    private final FilterEntry category;
    private final ItemStack stack;
    private boolean toggled;
    public final int x;
    public final int y;

    public TagButton(int x, int y, FilterEntry filter, OnPress onPress)
    {
        super(x, y, 32 /* width */, 28, new TranslatableComponent(""), onPress);
        this.category = filter;
        this.stack = filter.getIcon();
        this.toggled = filter.isEnabled();
        this.x = x;
        this.y = y;
    }

    public FilterEntry getFilter()
    {
        return category;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @SuppressWarnings("unused")
    public boolean getActive() {
        return active;
    }

    @Override
    public void renderButton(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderButton();
        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
    }

    /**
     * @see net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen#renderTabButton
     */
    @SuppressWarnings("JavadocReference")
    public void renderButton()
    {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindForSetup(TABS);

        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, alpha /* this.alpha */);
        RenderSystem.disableTexture(); // disableLighting?
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.ordinal(), GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.ordinal(), GlStateManager.SourceFactor.ONE.ordinal(), GlStateManager.DestFactor.ZERO.ordinal());
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.ordinal(), GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.ordinal());

        int width = this.toggled ? 32 : 28;
        int textureX = 28;
        int textureY = this.toggled ? 32 : 0;
        this.drawRotatedTexture(this.x, this.y, textureX, textureY, width, 28);

        //RenderSystem.scale???(); // enableRescaleNormal()

        //RenderHelper.enableStandardItemLighting(); //RenderHelper.enableGUIStandardItemLighting(); // RenderHelper
        RenderSystem.enableBlend();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.blitOffset = 100.0F; // zLevel
        renderer.renderAndDecorateItem(this.stack, x + 8, y + 6);
        renderer.renderGuiItemDecorations(mc.font, this.stack, x + 8, y + 6);
        renderer.blitOffset = 0.0F; // zLevel
    }

    private void drawRotatedTexture(int x, int y, int textureX, int textureY, int width, @SuppressWarnings("SameParameterValue") int height)
    {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS/*7*/, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, y + height, 0.0).uv((float)(textureX + height) * scaleX, (float)(textureY) * scaleY).endVertex(); // .pos(...).tex(...)
        bufferbuilder.vertex(x + width, y + height, 0.0).uv((float)(textureX + height) * scaleX, (float)(textureY + width) * scaleY).endVertex();
        bufferbuilder.vertex(x + width, y, 0.0).uv((float)(textureX) * scaleX, (float)(textureY + width) * scaleY).endVertex();
        bufferbuilder.vertex(x, y, 0.0).uv((float)(textureX) * scaleX, (float)(textureY) * scaleY).endVertex();
        bufferbuilder.vertex(x, y, 0.0).uv((float)(textureX) * scaleX, (float)(textureY) * scaleY).endVertex();
        tesselator.end();
    }

    public void updateState()
    {
        this.toggled = category.isEnabled();
    }
}
