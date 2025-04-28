package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Unique
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void injectRender(DrawContext context, CallbackInfo ci) {
        if(config.bossBarTracker.hideBossBar && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }
}
