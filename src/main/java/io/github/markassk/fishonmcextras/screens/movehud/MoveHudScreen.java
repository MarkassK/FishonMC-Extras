package io.github.markassk.fishonmcextras.screens.movehud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.screens.hud.ContestHudHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.FishTrackerHudHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.PetEquipHudHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.QuestTrackerHudHandler;
import io.github.markassk.fishonmcextras.screens.widget.movablebox.MovableBoxWidget;
import io.github.markassk.fishonmcextras.util.TextHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class MoveHudScreen extends Screen {
    private final Screen parent;
    FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public MoveHudScreen(MinecraftClient minecraftClient, Screen parent) {
        super(Text.literal("Move Hud Elements"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.renderWidgets();
    }

    private void renderWidgets() {
        List<MovableBoxWidget> movableBoxWidgetList = new ArrayList<>();

        // Fish Tracker HUD
        List<Text> fishTrackerTextList = FishTrackerHudHandler.instance().assembleFishText();
        int fishTrackerX = config.fishTracker.hudX;
        int fishTrackerY = config.fishTracker.hudY;
        boolean fishTrackerRightAlignment = config.fishTracker.rightAlignment;
        int fishTrackerFontSize = config.fishTracker.fontSize;
        int fishTrackerMaxLength = fishTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, fishTrackerX, fishTrackerY, fishTrackerRightAlignment, Text.literal("Fish Tracker"), fishTrackerFontSize, fishTrackerTextList.size(), fishTrackerMaxLength, (xPercent, yPercent) -> {
            config.fishTracker.hudX = xPercent;
            config.fishTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
        }));

        // Contest Tracker HUD
        List<Text> contestTrackerTextList = ContestHudHandler.instance().assembleContestText();
        int contestTrackerX = config.contestTracker.hudX;
        int contestTrackerY = config.contestTracker.hudY;
        boolean contestTrackerRightAlignment = config.contestTracker.rightAlignment;
        int contestTrackerFontSize = config.contestTracker.fontSize;
        int contestTrackerMaxLength = contestTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, contestTrackerX, contestTrackerY, contestTrackerRightAlignment, Text.literal("Contest Tracker"), contestTrackerFontSize, contestTrackerTextList.size(), contestTrackerMaxLength, (xPercent, yPercent) -> {
            config.contestTracker.hudX = xPercent;
            config.contestTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
        }));

        // Pet Tracker HUD
        List<Text> petTrackerTextList = PetEquipHudHandler.instance().assemblePetText();
        int petTrackerX = config.petEquipTracker.activePetHUDOptions.hudX;
        int petTrackerY = config.petEquipTracker.activePetHUDOptions.hudY;
        boolean petTrackerRightAlignment = config.petEquipTracker.activePetHUDOptions.rightAlignment;
        int petTrackerFontSize = config.petEquipTracker.activePetHUDOptions.fontSize;
        int petTrackerMaxLength = petTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, petTrackerX, petTrackerY, petTrackerRightAlignment, Text.literal("Pet Tracker"), petTrackerFontSize, petTrackerTextList.size(), petTrackerMaxLength + 16 + 8, (xPercent, yPercent) -> {
            config.petEquipTracker.activePetHUDOptions.hudX = xPercent;
            config.petEquipTracker.activePetHUDOptions.hudY = yPercent;
            AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
        }));

        // Quest Tracker HUD
        List<Text> questTrackerTextList = QuestTrackerHudHandler.instance().assembleQuestText();
        int questTrackerX = config.questTracker.hudX;
        int questTrackerY = config.questTracker.hudY;
        boolean questTrackerRightAlignment = config.questTracker.rightAlignment;
        int questTrackerFontSize = config.questTracker.fontSize;
        int questTrackerMaxLength = questTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, questTrackerX, questTrackerY, questTrackerRightAlignment, Text.literal("Quest Tracker"), questTrackerFontSize, questTrackerTextList.size(), questTrackerMaxLength, (xPercent, yPercent) -> {
            config.questTracker.hudX = xPercent;
            config.questTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
        }));

        movableBoxWidgetList.forEach(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int alphaInt = (int) ((40 / 100f) * 255f) << 24;
        context.fill(0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), 24, alphaInt);
        Text barHudText = TextHelper.concat(Text.literal("Bar HUD "), Text.literal("Not movable").formatted(Formatting.GRAY, Formatting.ITALIC));
        context.drawText(textRenderer, barHudText, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - textRenderer.getWidth(barHudText) / 2, 8, 0xFFFFFF, true);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
