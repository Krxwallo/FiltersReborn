package com.mrcrayfish.filters.mixin.client;

import com.mrcrayfish.filters.Hooks;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


@SuppressWarnings("rawtypes")
@Mixin(EffectRenderingInventoryScreen.class)
public class EffectRenderingMixin {
    @Redirect(method = "renderEffects", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;leftPos:I", opcode = Opcodes.GETFIELD))
    private int renderEffects(EffectRenderingInventoryScreen screen) {
        var offset = Hooks.getPotionEffectOffset(screen);
        LogManager.getLogger().info("Mixin: renderEffects: returning offset " + offset);
        return offset;
    }

    @ModifyConstant(method = "checkEffectRendering", constant = @Constant(intValue = 160))
    private int checkEffectRendering(int constant) {
        var offset = Hooks.getEffectsGuiOffset((EffectRenderingInventoryScreen)(Object) this);
        LogManager.getLogger().info("Mixin: checkEffectRendering: returning offset " + offset);
        return offset;
    }
}
