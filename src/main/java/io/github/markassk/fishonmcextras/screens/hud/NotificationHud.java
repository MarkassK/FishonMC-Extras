package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.common.FlairDecor;
import io.github.markassk.fishonmcextras.common.Theming;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ThemingHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.NotificationHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        List<Text> textList = NotificationHudHandler.instance().assembleNotificationText();

        drawContext.getMatrices().push();
        try {
            if(!textList.isEmpty()) {
                // Get screen size
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // Convert percentage config values to screen coordinates
                float xPercent = 50 / 100f;
                float yPercent = config.notifications.hudY / 100f;

                // Calculate base positions relative to screen size
                int baseX = (int) (screenWidth * xPercent);
                int baseY = (int) (screenHeight * yPercent);

                // Scaling setup
                int fontSize = config.notifications.fontSize;
                float scale = fontSize / 10.0f;
                drawContext.getMatrices().scale(scale, scale, 1f);

                // Alpha
                int alphaInt = (int) ((config.notifications.backgroundOpacity / 100f) * 255f) << 24;

                int lineSpacing = 2;
                int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
                int scaledX = (int) (baseX / scale);
                int scaledY = (int) (baseY / scale);
                int padding = 8;
                AtomicInteger count = new AtomicInteger(0);

                int maxLength = textList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();
                int heightClampTranslation = (int) ((padding * 2 + textList.size() * lineHeight) * yPercent);
                heightClampTranslation -= (int) ((padding * 3) * (1 - yPercent));


                // Draw Background
                drawContext.fill(scaledX - maxLength / 2 - padding, scaledY - heightClampTranslation, scaledX + maxLength / 2 + padding, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);

                // Theming
                FlairDecor flairDecor = ThemingHandler.instance().flairDecorNotification;

                if(config.theme.themeType != Theming.ThemeType.OFF) {
                    Theming theme = ThemingHandler.instance().currentTheme;
                    int colorOverlay = config.theme.colorOverlay;


                    // Corners
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_LEFT, scaledX - maxLength / 2 - padding * 2, scaledY - padding - heightClampTranslation, 16, 16, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP_RIGHT, scaledX + maxLength / 2, scaledY - padding - heightClampTranslation, 16, 16, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_LEFT, scaledX - maxLength / 2 - padding * 2, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM_RIGHT, scaledX + maxLength / 2, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16, colorOverlay);

                    // Sides
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_LEFT, scaledX - maxLength / 2 - padding * 2, scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_RIGHT, scaledX + maxLength / 2, scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TOP, scaledX - maxLength / 2, scaledY - padding - heightClampTranslation, maxLength, 16, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_BOTTOM, scaledX - maxLength / 2, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, maxLength, 16, colorOverlay);

                    // Title
                    Text title = NotificationHudHandler.instance().getTitle().copy().withColor(ThemingHandler.instance().currentThemeType.TEXT_COLOR);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_LEFT, scaledX - textRenderer.getWidth(title) / 2 - 16, scaledY - padding - heightClampTranslation, 16, 16, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_MIDDLE, scaledX  - textRenderer.getWidth(title) / 2, scaledY - padding - heightClampTranslation, textRenderer.getWidth(title), 16, colorOverlay);
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, theme.GUI_TEXT_RIGHT, scaledX + textRenderer.getWidth(title) / 2, scaledY - padding - heightClampTranslation, 16, 16, colorOverlay);
                    drawContext.drawText(textRenderer, title, scaledX - textRenderer.getWidth(title) / 2, scaledY - textRenderer.fontHeight / 2 - heightClampTranslation - 1, 0xFFFFFF, false);
                }

                // Flair
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_TOP_LEFT, scaledX - maxLength / 2 - padding * 2 - 24, scaledY - padding - heightClampTranslation - 24, 64, 64);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_TOP_RIGHT, scaledX + maxLength / 2 - 24, scaledY - padding - heightClampTranslation - 24, 64, 64);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_BOTTOM_LEFT, scaledX - maxLength / 2 - padding * 2 - 24, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, flairDecor.GUI_FLAIR_BOTTOM_RIGHT, scaledX + maxLength / 2 - 24, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);

                int finalHeightClampTranslation = heightClampTranslation;
                textList.forEach(text -> drawContext.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2, scaledY + padding + (count.getAndIncrement() * lineHeight) - finalHeightClampTranslation, 0xFFFFFF, true));
            }
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
