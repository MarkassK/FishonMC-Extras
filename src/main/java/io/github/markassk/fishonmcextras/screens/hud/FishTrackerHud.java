package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.common.FlairDecor;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.FishTrackerHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FishTrackerHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        List<Text> textList = FishTrackerHudHandler.instance().assembleFishText();

        drawContext.getMatrices().push();
        try {
            // Get screen size
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            boolean rightAlignment = config.fishTracker.rightAlignment;

            // Convert percentage config values to screen coordinates
            float xPercent = rightAlignment ?  1f - (config.fishTracker.hudX / 100f) : config.fishTracker.hudX / 100f;
            float yPercent = config.fishTracker.hudY / 100f;

            // Calculate base positions relative to screen size
            int baseX = (int) (screenWidth * xPercent);
            int baseY = (int) (screenHeight * yPercent);

            // Scaling setup
            int fontSize = config.fishTracker.fontSize;
            float scale = fontSize / 10.0f;
            drawContext.getMatrices().scale(scale, scale, 1f);

            // Alpha
            int alphaInt = (int) ((config.fishTracker.backgroundOpacity / 100f) * 255f) << 24;

            int lineSpacing = 2;
            int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
            int scaledX = (int) (baseX / scale);
            int scaledY = (int) (baseY / scale);
            int padding = 8;
            AtomicInteger count = new AtomicInteger(0);

            int maxLength = textList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
            int heightClampTranslation = (int) ((padding * 2 + textList.size() * lineHeight) * yPercent);
            heightClampTranslation -= (int) ((padding * 3) * (1 - yPercent));

            if (rightAlignment) {
                drawContext.fill(scaledX, scaledY - heightClampTranslation, scaledX - maxLength - padding * 2, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);
            } else {
                drawContext.fill(scaledX, scaledY - heightClampTranslation, scaledX + maxLength + padding * 2, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);
            }

            // Theming
            FlairDecor flairDecor = ThemingHandler.instance().flairDecorFishTracker;
            int rightAlignmentOffset = (rightAlignment ? padding * 2 + maxLength : 0);

            if(config.theme.themeType != Theming.ThemeType.OFF) {
                Theming theme = ThemingHandler.instance().currentTheme;
                int colorOverlay = config.theme.colorOverlay;
                int themeTextColor = ThemingHandler.instance().currentThemeType.TEXT_COLOR;
                int alphaOverlay = (int) ((config.theme.opacity / 100f) * 255f) << 24;

                // Corners
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_LEFT, scaledX - padding - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_LEFT, scaledX - padding - rightAlignmentOffset, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);

                // Sides
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_LEFT, scaledX - padding - rightAlignmentOffset, scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset, scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP, scaledX + padding - rightAlignmentOffset, scaledY - padding - heightClampTranslation, maxLength, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM, scaledX + padding - rightAlignmentOffset, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, maxLength, 16, alphaOverlay | colorOverlay);

                // Title
                Text title = FishTrackerHudHandler.instance().getTitle().copy().withColor(ThemingHandler.instance().currentThemeType.TEXT_COLOR);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_LEFT, scaledX + (maxLength + padding * 2) / 2 - textRenderer.getWidth(title) / 2 - 16 - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_MIDDLE, scaledX  + (maxLength + padding * 2) / 2 - textRenderer.getWidth(title) / 2 - rightAlignmentOffset, scaledY - padding - heightClampTranslation, textRenderer.getWidth(title), 16, alphaOverlay | colorOverlay);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_RIGHT, scaledX + (maxLength + padding * 2) / 2 + textRenderer.getWidth(title) / 2 - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                drawContext.drawText(textRenderer, title, scaledX + (maxLength + padding * 2) / 2 - textRenderer.getWidth(title) / 2 - rightAlignmentOffset, scaledY - textRenderer.fontHeight / 2 - heightClampTranslation - 1, themeTextColor, false);
            }

            // Flair
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_TOP_LEFT, scaledX - padding - rightAlignmentOffset - 24, scaledY - padding - heightClampTranslation - 24, 64, 64);
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_TOP_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset - 24, scaledY - padding - heightClampTranslation - 24, 64, 64);
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_BOTTOM_LEFT, scaledX - padding - rightAlignmentOffset - 24, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_BOTTOM_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset - 24, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);


            int finalHeightClampTranslation = heightClampTranslation;
            textList.forEach(text -> drawContext.drawText(textRenderer, text, rightAlignment ? scaledX - textRenderer.getWidth(text) - padding: scaledX + padding, scaledY + (count.getAndIncrement() * lineHeight) + padding - finalHeightClampTranslation, 0xFFFFFF, true));
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
