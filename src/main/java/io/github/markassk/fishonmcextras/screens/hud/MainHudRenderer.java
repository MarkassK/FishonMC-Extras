package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class MainHudRenderer implements HudRenderCallback {
    FishTrackerHud fishTrackerHud = new FishTrackerHud();
    PetEquipHud petEquipHud = new PetEquipHud();
    WarningHud warningHud = new WarningHud();
    TitleHud titleHud = new TitleHud();
    ItemFrameTooltipHud itemFrameTooltipHud = new ItemFrameTooltipHud();

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        if(!MinecraftClient.getInstance().options.hudHidden) {
            if(config.fishTracker.showFishTrackerHUD) {
                this.fishTrackerHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(config.petEquipTracker.showPetEquipTrackerHUD) {
                this.petEquipHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(config.warning.showWarningHud) {
                this.warningHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(config.titlePopup.useNewTitleSystem) {
                this.titleHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(config.itemFrameTooltip.showTooltip) {
                this.itemFrameTooltipHud.render(drawContext, MinecraftClient.getInstance());
            }
        }

    }
}
