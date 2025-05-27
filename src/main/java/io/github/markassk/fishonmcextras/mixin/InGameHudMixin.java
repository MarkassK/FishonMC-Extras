package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.FishCatchHandler;
import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void injectRenderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(config.scoreboardTracker.hideScoreboard && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void injectRenderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
        if(config.barHUD.showBar && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void injectRenderExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(config.barHUD.showBar && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }

    @Inject(method = "setTitle", at = @At("HEAD"))
    private void injectSetTitle(Text title, CallbackInfo ci) {
        FishCatchHandler.instance().catchTitle(title);
    }

    @Inject(method = "setSubtitle", at = @At("HEAD"))
    private void injectSetSubtitle(Text title, CallbackInfo ci) {
        FishCatchHandler.instance().catchSubtitle(title);
    }
}
