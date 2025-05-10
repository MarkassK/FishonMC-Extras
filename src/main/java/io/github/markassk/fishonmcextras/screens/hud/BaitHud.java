package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.screens.hud.BaitHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.Objects;

public class BaitHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        Text baitText = BaitHudHandler.instance().assembleBaitText();
        CustomModelDataComponent modelData = BaitHudHandler.instance().getModelData();
        ItemStack baitStack = Items.COOKED_COD.getDefaultStack().copy();
        baitStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, modelData);

        drawContext.getMatrices().push();
        try {
            if(!Objects.equals(baitText.getString(), ""))  {
                // Get screen size
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                boolean rightAlignment = config.baitTracker.rightAlignment;

                // Convert percentage config values to screen coordinates
                float xPercent = 50 / 100f;
                float yPercent = config.baitTracker.hudY / 100f;

                // Calculate base positions relative to screen size
                int baseX = (int) (screenWidth * xPercent);
                int baseY = (int) (screenHeight * yPercent);

                // Scaling setup
                int fontSize = config.baitTracker.fontSize;
                float scale = fontSize / 10.0f;
                drawContext.getMatrices().scale(scale, scale, 1f);

                // Alpha
                int alphaInt = (int) ((config.baitTracker.backgroundOpacity / 100f) * 255f) << 24;

                int scaledX = (int) (baseX / scale);
                int scaledY = (int) (baseY / scale);
                int padding = 8;

                int maxLength = textRenderer.getWidth(baitText);
                int heightClampTranslation = (int) (24 * yPercent);
                heightClampTranslation -= (int) ((padding * 3) * (1 - yPercent));
                int offset = config.baitTracker.offsetFromMiddle;

                // Draw Background
                if(rightAlignment) {
                    drawContext.fill(scaledX + offset, scaledY - heightClampTranslation, scaledX + offset + padding * 2  + 4 + 16 + maxLength, scaledY - heightClampTranslation + 24, alphaInt);
                    drawContext.drawBorder(scaledX + offset + 2, scaledY - heightClampTranslation + 2, 20, 20,  alphaInt | 0xFFFFFF);
                } else {
                    drawContext.fill(scaledX - offset - padding * 2 - 4 - 16 - maxLength, scaledY - heightClampTranslation, scaledX - offset, scaledY - heightClampTranslation + 24, alphaInt);
                    drawContext.drawBorder(scaledX - offset - 2 - 20, scaledY - heightClampTranslation + 2, 20, 20,  alphaInt | 0xFFFFFF);
                }

                // Draw Item
                if(rightAlignment) {
                    drawContext.drawItem(baitStack, scaledX + offset + 4, scaledY + 4 - heightClampTranslation);
                } else {
                    drawContext.drawItem(baitStack, scaledX - offset - 4 - 16, scaledY + 4 - heightClampTranslation);
                }

                // Draw Text
                if(rightAlignment) {
                    drawContext.drawText(textRenderer, baitText, scaledX + offset + padding + 4 + 16, scaledY + 12 - fontSize / 2 - heightClampTranslation, 0xFFFFFF, true);
                } else {
                    drawContext.drawText(textRenderer, baitText, scaledX - offset - padding - 4 - 16 - maxLength, scaledY + 12 - fontSize / 2 - heightClampTranslation, 0xFFFFFF, true);
                }
            }
        } finally {
            drawContext.getMatrices().pop();
        }
    }
}
