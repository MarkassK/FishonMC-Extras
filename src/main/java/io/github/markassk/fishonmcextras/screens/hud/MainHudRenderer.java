package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.config.ConfigConstants;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.LoadingHandler;
import io.github.markassk.fishonmcextras.handler.LocationHandler;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MainHudRenderer implements HudRenderCallback {
    FishTrackerHud fishTrackerHud = new FishTrackerHud();
    PetEquipHud petEquipHud = new PetEquipHud();
    NotificationHud notificationHud = new NotificationHud();
    TitleHud titleHud = new TitleHud();
    ItemFrameTooltipHud itemFrameTooltipHud = new ItemFrameTooltipHud();
    BarHud barHud = new BarHud();
    ContestHud contestHud = new ContestHud();
    BaitHud baitHud = new BaitHud();
    EquipmentHud equipmentHud = new EquipmentHud();
    CrewHud crewHud = new CrewHud();
    QuestHud questHud = new QuestHud();

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        if(!MinecraftClient.getInstance().options.hudHidden && LoadingHandler.instance().isOnServer && LoadingHandler.instance().isLoadingDone) {
            this.notificationHud.render(drawContext, MinecraftClient.getInstance());

            if(config.titlePopup.useNewTitleSystem) {
                this.titleHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(config.itemFrameTooltip.showTooltip) {
                this.itemFrameTooltipHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(config.barHUD.showBar) {
                this.barHud.render(drawContext, MinecraftClient.getInstance());
            }

            if(LocationHandler.instance().currentLocation != Constant.CREW_ISLAND) {

                if(config.fishTracker.showFishTrackerHUD) {
                    this.fishTrackerHud.render(drawContext, MinecraftClient.getInstance());
                }

                if(config.petEquipTracker.showPetEquipTrackerHUD) {
                    this.petEquipHud.render(drawContext, MinecraftClient.getInstance());
                }

                if(config.contestTracker.showContest) {
                    this.contestHud.render(drawContext, MinecraftClient.getInstance());
                }

                if(config.baitTracker.showBaitHud) {
                    this.baitHud.render(drawContext, MinecraftClient.getInstance());
                }

                if(config.equipmentTracker.showEquipmentHud) {
                    this.equipmentHud.render(drawContext, MinecraftClient.getInstance());
                }

                if(config.crewTracker.showCrewNearby) {
                    this.crewHud.render(drawContext, MinecraftClient.getInstance());
                }

                if(config.questTracker.showQuestHud) {
                    this.questHud.render(drawContext, MinecraftClient.getInstance());
                }
            }

            // Dev
            if(ConfigConstants.DEV) {
                Text dev = Text.literal("Development version, do not distribute").formatted(Formatting.RED);
                drawContext.drawText(
                        MinecraftClient.getInstance().textRenderer, dev,
                        0,
                        MinecraftClient.getInstance().getWindow().getScaledHeight() - MinecraftClient.getInstance().textRenderer.fontHeight, 0xFFFFFF, true);
            }
        }
    }
}
