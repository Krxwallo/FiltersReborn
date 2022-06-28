package com.mrcrayfish.filters.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// FIXME not working?
@Mixin(CreativeModeInventoryScreen.class)
public class CreativeInventoryMixin {
    @Inject(method = "selectTab", at = @At("TAIL"))
    private void selectTab(CreativeModeTab tab, CallbackInfo ci) {
        assert Minecraft.getInstance().player != null;
        Minecraft.getInstance().player.chat("I selected tab " + tab.getDisplayName());
    }
}
