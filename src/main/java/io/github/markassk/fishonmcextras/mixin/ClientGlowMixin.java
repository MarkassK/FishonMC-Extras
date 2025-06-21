package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.OtherPlayerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Entity.class)
public class ClientGlowMixin {
    @Inject(method = "isGlowing()Z", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity)(Object)this;
        if (OtherPlayerHandler.instance().isHighlighted
                && LoadingHandler.instance().isOnServer
                && self.getWorld().isClient
                && self instanceof PlayerEntity otherPlayer
                && Objects.equals(otherPlayer.getUuid(), OtherPlayerHandler.instance().highlightedPlayer.getProfile().getId())
        ) {
            cir.setReturnValue(true);
        }
    }
}
