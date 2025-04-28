package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.PetEquipHandler;
import io.github.markassk.fishonmcextras.handler.screens.hud.PetEquipHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PetEquipHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        List<Text> textList = PetEquipHudHandler.instance().assemblePetText();
        ItemStack activePet = PetEquipHandler.instance().currentPetItem;

        drawContext.getMatrices().push();
        try {
            // Get screen size
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            boolean rightAlignment = config.petEquipTracker.activePetHUDOptions.rightAlignment;

            // Convert percentage config values to screen coordinates
            float xPercent = rightAlignment ?  1f - (config.petEquipTracker.activePetHUDOptions.hudX / 100f) : config.petEquipTracker.activePetHUDOptions.hudX / 100f;
            float yPercent = config.petEquipTracker.activePetHUDOptions.hudY / 100f;


            // Calculate base positions relative to screen size
            int baseX = (int) (screenWidth * xPercent);
            int baseY = (int) (screenHeight * yPercent);

            // Scaling setup
            int fontSize = config.petEquipTracker.activePetHUDOptions.fontSize;
            float scale = fontSize / 10.0f;
            drawContext.getMatrices().scale(scale, scale, 1f);

            // Alpha
            int alphaInt = (int) ((config.petEquipTracker.activePetHUDOptions.backgroundOpacity / 100f) * 255f) << 24;

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
            if(rightAlignment) {
                drawContext.fill(scaledX, scaledY - heightClampTranslation, scaledX - maxLength - padding * 2 - 16 - padding, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);
                drawContext.drawBorder(scaledX - padding + padding / 2, scaledY + padding + 1 - padding / 2 - heightClampTranslation, - 16 - padding, 16 + padding, alphaInt | 0xFFFFFF);
            } else {
                drawContext.fill(scaledX, scaledY - heightClampTranslation, scaledX + maxLength + padding * 2 + 16 + padding, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);
                drawContext.drawBorder(scaledX + padding - padding / 2, scaledY + padding + 1 - padding / 2 - heightClampTranslation, 16 + padding, 16 + padding, alphaInt | 0xFFFFFF);
            }

            if(PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.HAS_PET) {
                // Draw Pet
                if(rightAlignment) {
                    drawContext.drawItem(activePet, scaledX - padding - 16, scaledY + padding + 1 - heightClampTranslation);
                } else {
                    drawContext.drawItem(activePet, scaledX + padding, scaledY + padding + 1 - heightClampTranslation);
                }
            }

            // Draw Text
            int finalHeightClampTranslation = heightClampTranslation;
            textList.forEach(text -> {
                drawContext.drawText(textRenderer, text, rightAlignment ? scaledX - padding - 16 - padding - textRenderer.getWidth(text) : scaledX + padding + 16 + padding, scaledY + (count.getAndIncrement() * lineHeight) + padding - finalHeightClampTranslation, 0xFFFFFF, true);
            });
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
