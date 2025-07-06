package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.ItemMarkerHandler;
import io.github.markassk.fishonmcextras.handler.PersonalVaultScreenHandler;
import io.github.markassk.fishonmcextras.handler.SearchBarContainerHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow public abstract void close();

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void injectDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer) {
            ItemMarkerHandler.instance().renderItemMarker(context, slot);
        }
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;close()V"), cancellable = true)
    private void injectKeypressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (SearchBarContainerHandler.instance().searchBar != null
                && SearchBarContainerHandler.instance().searchBar.isFocused()) {
            cir.setReturnValue(true);
        }
    }
}
