package com.mrcrayfish.filters.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.filters.FilterEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

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

    public TagButton(int x, int y, FilterEntry filter, IPressable pressable)
    {
        super(x, y, 32 /* width */, 28, new TranslationTextComponent(""), pressable);
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
        this.field_230694_p_ = active;
    }

    @SuppressWarnings("unused")
    public boolean getActive() {
        return field_230694_p_;
    }

    @Override
    public void func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // equals "renderButton()"
        // this.matrixStack = matrixStack;
        renderButton();
        super.func_230431_b_(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderButton()
    {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(TABS);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, field_230695_q_ /* this.alpha */);
        RenderSystem.disableLighting();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.ordinal(), GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.ordinal(), GlStateManager.SourceFactor.ONE.ordinal(), GlStateManager.DestFactor.ZERO.ordinal());
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.ordinal(), GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.ordinal());

        int width = this.toggled ? 32 : 28;
        int textureX = 28;
        int textureY = this.toggled ? 32 : 0;
        this.drawRotatedTexture(this.x, this.y, textureX, textureY, width, 28);

        RenderSystem.enableRescaleNormal();
        RenderHelper.enableStandardItemLighting(); //RenderHelper.enableGUIStandardItemLighting();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.zLevel = 100.0F;
        renderer.renderItemAndEffectIntoGUI(this.stack, x + 8, y + 6);
        renderer.renderItemOverlays(mc.fontRenderer, this.stack, x + 8, y + 6);
        renderer.zLevel = 100.0F;
    }

    private void drawRotatedTexture(int x, int y, int textureX, int textureY, int width, @SuppressWarnings("SameParameterValue") int height)
    {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex((float)(textureX + height) * scaleX, (float)(textureY) * scaleY).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((float)(textureX + height) * scaleX, (float)(textureY + width) * scaleY).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((float)(textureX) * scaleX, (float)(textureY + width) * scaleY).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex((float)(textureX) * scaleX, (float)(textureY) * scaleY).endVertex();
        tessellator.draw();
    }

    public void updateState()
    {
        this.toggled = category.isEnabled();
    }
}
