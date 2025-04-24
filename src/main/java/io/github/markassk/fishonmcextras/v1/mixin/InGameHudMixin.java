package io.github.markassk.fishonmcextras.v1.mixin;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.FishCatchHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    @Inject(method = "renderTitleAndSubtitle", at = @At("HEAD"), cancellable = true)
    private void injectRenderTitleAndSubtitle(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(System.currentTimeMillis() - FishCatchHandler.instance().lastTimeUsedRod < 2000 && config.fishTracker.fishTrackerToggles.otherToggles.useNewTitle) {
            System.out.println(System.currentTimeMillis() - FishCatchHandler.instance().lastTimeUsedRod + "");
            ci.cancel();
        }
    }
}
