package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.RarityMarkerHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void injectDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer) {
            RarityMarkerHandler.instance().renderRarityMarker(context, slot);
        }
    }
}
