package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.CrewHandler;
import io.github.markassk.fishonmcextras.handler.LocationHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.CrewHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class CrewHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        Text text = CrewHudHandler.instance().assembleCrewNearbyText();

        drawContext.getMatrices().push();
        try {
            if(CrewHandler.instance().isCrewInRenderDistance && LocationHandler.instance().currentLocation != Constant.SPAWNHUB && ProfileDataHandler.instance().profileData.crewState == CrewHandler.CrewState.HASCREW) {
                // Get screen size
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // Convert percentage config values to screen coordinates
                float xPercent = 50 / 100f;
                float yPercent = 0 / 100f;

                // Calculate base positions relative to screen size
                int baseX = (int) (screenWidth * xPercent);
                int baseY = (int) (screenHeight * yPercent);

                // Scaling setup
                int fontSize = config.crewTracker.fontSize;
                float scale = fontSize / 10.0f;
                drawContext.getMatrices().scale(scale, scale, 1f);

                // Alpha
                int alphaInt = (int) ((config.crewTracker.backgroundOpacity / 100f) * 255f) << 24;

                int lineSpacing = 2;
                int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
                int scaledX = (int) (baseX / scale);
                int scaledY = (int) (baseY / scale);
                int padding = 8;
                int paddingHeight = 4;

                int maxLength = textRenderer.getWidth(text);
                int heightClampTranslation = (int) ((paddingHeight * 2 + lineHeight) * yPercent);
                heightClampTranslation -= (int) ((padding * 3) * (1 - yPercent));

                drawContext.fill(scaledX - maxLength / 2 - padding, scaledY - heightClampTranslation, scaledX + maxLength / 2 + padding, scaledY + paddingHeight * 2 + lineHeight - heightClampTranslation, alphaInt);

                drawContext.drawText(textRenderer, text, scaledX - maxLength / 2, scaledY + paddingHeight - heightClampTranslation, 0xFFFFFF, true);
            }
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
