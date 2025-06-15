package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.TitleHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TitleHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        long showedAt = TitleHandler.instance().showedAt;
        float alpha = 0.0f;

        if(System.currentTimeMillis() - showedAt < 1000L) {
            long timeLeft = System.currentTimeMillis() - showedAt;
            alpha = timeLeft / 1000f;
        } else if (System.currentTimeMillis() - showedAt < TitleHandler.instance().time) {
            alpha = 1.0f;
        } else if (System.currentTimeMillis() - showedAt < 1000L + TitleHandler.instance().time) {
            long timeLeft = System.currentTimeMillis() - showedAt - TitleHandler.instance().time;
            alpha = 1.0f - timeLeft / 1000f;
        }

        if(System.currentTimeMillis() - showedAt < 3000L) {
            client.inGameHud.setTitle(Text.empty());
            client.inGameHud.setSubtitle(Text.empty());
        }

        // Alpha
        int alphaInt = (int) (alpha * 255f) << 24;


        if(System.currentTimeMillis() - showedAt < 950L + TitleHandler.instance().time && System.currentTimeMillis() - showedAt > 50L) {
            // Assemble all text lines
            List<Text> title = TitleHandler.instance().title;
            List<Text> subtitle = TitleHandler.instance().subtitle;

            // Title
            drawContext.getMatrices().push();
            try {
                // Get screen size
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // Calculate base positions relative to screen size
                int baseX = (int) (screenWidth * 0.5f);
                int baseY = (int) (screenHeight * 0.5f);

                // Scaling setup
                float fontSize = config.titlePopup.scale * 2f;
                float scale = fontSize / 10.0f;
                drawContext.getMatrices().scale(scale, scale, 1f);

                int lineSpacing = 4;
                int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
                int scaledX = (int) (baseX / scale);
                int scaledY = (int) (baseY / scale);
                AtomicInteger count = new AtomicInteger(0);

                title.forEach(text -> drawContext.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2, scaledY - ((count.getAndIncrement() + 1) * lineHeight), alphaInt, true));
            } finally {
                drawContext.getMatrices().pop();
            }

            // subtitle
            drawContext.getMatrices().push();
            try {
                // Get screen size
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // Calculate base positions relative to screen size
                int baseX = (int) (screenWidth * 0.5f);
                int baseY = (int) (screenHeight * 0.5f);

                // Scaling setup
                float fontSize = config.titlePopup.scale * 1f;
                float scale = fontSize / 10.0f;
                drawContext.getMatrices().scale(scale, scale, 1f);

                int lineSpacing = 4;
                int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
                int scaledX = (int) (baseX / scale);
                int scaledY = (int) (baseY / scale);
                AtomicInteger count = new AtomicInteger(0);

                subtitle.forEach(text -> drawContext.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2, scaledY + (count.getAndIncrement() * lineHeight) + lineHeight, alphaInt, true));
            } finally {
                drawContext.getMatrices().pop();
            }
        }
    }
}
