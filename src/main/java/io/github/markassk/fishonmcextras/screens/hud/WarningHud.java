package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constants;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.screens.hud.PetEquipHudHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.WarningHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WarningHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        List<Text> textList = WarningHudHandler.instance().assembleWarningText();

        drawContext.getMatrices().push();
        try {
           if(!textList.isEmpty()) {
               // Get screen size
               int screenWidth = client.getWindow().getScaledWidth();
               int screenHeight = client.getWindow().getScaledHeight();

               // Convert percentage config values to screen coordinates
               float xPercent = 50 / 100f;
               float yPercent = config.warning.hudY / 100f;

               // Calculate base positions relative to screen size
               int baseX = (int) (screenWidth * xPercent);
               int baseY = (int) (screenHeight * yPercent);

               // Scaling setup
               int fontSize = config.petEquipTracker.activePetHUDOptions.fontSize;
               float scale = fontSize / 10.0f;
               drawContext.getMatrices().scale(scale, scale, 1f);

               // Alpha
               int alphaInt = (int) ((config.warning.backgroundOpacity / 100f) * 255f) << 24;

               int lineSpacing = 2;
               int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
               int scaledX = (int) (baseX / scale);
               int scaledY = (int) (baseY / scale);
               int padding = 8;
               AtomicInteger count = new AtomicInteger(0);

               int maxLength = textList.stream().map(textRenderer::getWidth).max(Integer::compareTo).get();

               // Draw Background
               drawContext.fill(scaledX - maxLength / 2 - padding, scaledY, scaledX + maxLength / 2 + padding, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3, alphaInt);

               textList.forEach(text -> {
                   drawContext.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2, scaledY + padding + (count.getAndIncrement() * lineHeight), Constants.Color.DEFAULT, true);
               });
           }
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
