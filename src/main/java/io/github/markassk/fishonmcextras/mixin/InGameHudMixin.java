package io.github.markassk.fishonmcextras.mixin;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.FishCatchHandler;
import io.github.markassk.fishonmcextras.handler.ItemMarkerHandler;
import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.OwnPlayerHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private @Nullable Text title;
    @Shadow private int titleRemainTicks;
    @Unique
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void injectRenderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(config.scoreboardTracker.hideScoreboard && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }

    @Inject(method = "renderTitleAndSubtitle", at = @At("HEAD"), cancellable = true)
    private void injectRenderTitleAndSubtitle(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if((LoadingHandler.instance().isOnServer
                && System.currentTimeMillis() - FishCatchHandler.instance().lastTimeUsedRod < 1000L
                && config.titlePopup.useNewTitleSystem)
                || config.fun.immersionMode
                || (
                        config.titlePopup.useNewTitleSystem
                                && config.fishTracker.fishTrackerToggles.otherToggles.useNewTitle
                                && this.title != null
                                && !this.title.getString().isEmpty()
                                && this.titleRemainTicks > 0
                                && (int) this.title.getString().charAt(0) > 0xE000
                                && (int) this.title.getString().charAt(0) < 0xE999
                   )
                || (
                        config.fun.biteBobber
                                && this.title != null
                                && !this.title.getString().isEmpty()
                                && this.titleRemainTicks > 0
                                && Objects.equals(this.title.getString(), "BITE!")
                )
        ) {
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
        if(LoadingHandler.instance().isOnServer) {
            FishCatchHandler.instance().catchTitle(title);
        }
    }

    @Inject(method = "setSubtitle", at = @At("HEAD"))
    private void injectSetSubtitle(Text title, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer) {
            FishCatchHandler.instance().catchSubtitle(title);
        }
    }

    @Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void injectRenderOverlayMessage(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer && config.fun.minigameOnBobber) {
            ci.cancel();
        }
    }

    @Inject(method = "renderMainHud", at = @At("HEAD"), cancellable = true)
    private void injectRenderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer && config.fun.immersionMode && System.currentTimeMillis() - OwnPlayerHandler.instance().changedSlotTime > 5000L) {
            ci.cancel();
        }
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void injectRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer && config.fun.immersionMode) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbarItem", at = @At("TAIL"))
    private void injectDrawHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        if(LoadingHandler.instance().isOnServer && LoadingHandler.instance().isLoadingDone) {
            ItemMarkerHandler.instance().renderHotBarSelectedPet(context, x, y, stack);
        }
    }
}
