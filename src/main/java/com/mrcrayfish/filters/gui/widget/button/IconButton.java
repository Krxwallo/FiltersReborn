package com.mrcrayfish.filters.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * Author: MrCrayfish & justAm0dd3r
 */
@OnlyIn(Dist.CLIENT)
public class IconButton extends ImageButton
{

    private final ITextComponent message;
    private boolean enabled = true;
    public final int x,y;

    public IconButton(ITextComponent message, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        this.message = message;
        this.x = xIn;
        this.y = yIn;
    }

    public void setActive(boolean active) {
        this.field_230694_p_ = active;
    }

    public String getMessage() {
        return message.getString();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /*
    Widget:
        - func_230996_d_(boolean p_230996_1_) equals setFocused(boolean focused)
        - func_230999_j_() equals getFocused()
        - func_230458_i_() equals getMessage()
        - field_230689_k_ equals height
        - field_230688_j_ (probably) equals width
        - func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) equals "renderButton()"
        - func_238472_a_(MatrixStack p_238472_1_, FontRenderer p_238472_2_, ITextProperties p_238472_3_, int p_238472_4_, int p_238472_5_, int p_238472_6_)
          equals blit(x + 2, y + 2, iconU, iconV, (prob.) width, (prob.) height)
        - func_230989_a_(focused) equals getYImage(focused)
        - field_230694_p_ equals active (prob.)
     */

    @Override
    public void func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Render the button, if enabled is true!
        if (enabled) super.func_230431_b_(matrixStack, mouseX, mouseY, partialTicks);
    }
}
