package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.OtherPlayerHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
            if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getPos().distanceTo(otherPlayer.getPos()) < 3d) {
                OtherPlayerHandler.instance().isHighlighted = false;
                OtherPlayerHandler.instance().highlightedPlayer = null;
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(TextHelper.concat(
                        Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                        Text.literal("» ").formatted(Formatting.DARK_GRAY),
                        Text.literal("Found Player. Stopping Highlight")
                ));
            }
            cir.setReturnValue(true);
        }
    }
}
