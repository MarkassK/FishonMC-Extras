package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.screens.hud.EquipmentHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EquipmentHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        ItemStack chestplate = EquipmentHudHandler.instance().getChestPlate();
        ItemStack leggings = EquipmentHudHandler.instance().getLeggings();
        ItemStack boots = EquipmentHudHandler.instance().getBoots();
        ItemStack reel = EquipmentHudHandler.instance().getReel();
        ItemStack pole = EquipmentHudHandler.instance().getPole();
        ItemStack line = EquipmentHudHandler.instance().getLine();

        drawContext.getMatrices().push();
        try {
            // Get screen size
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            // Convert percentage config values to screen coordinates
            float xPercent = 50 / 100f;
            float yPercent = 100 / 100f;

            // Calculate base positions relative to screen size
            int baseX = (int) (screenWidth * xPercent);
            int baseY = (int) (screenHeight * yPercent);

            // Scaling setup
            int fontSize = config.equipmentTracker.fontSize;
            float scale = fontSize / 10.0f;
            drawContext.getMatrices().scale(scale, scale, 1f);

            // Alpha
            int alphaInt = (int) ((config.equipmentTracker.backgroundOpacity / 100f) * 255f) << 24;

            int scaledX = (int) (baseX / scale);
            int scaledY = (int) (baseY / scale);
            int padding = 8;


            int offsetMiddleArmor = config.equipmentTracker.armorHUDOptions.offsetFromMiddle;
            int offsetMiddleRodParts = config.equipmentTracker.rodPartsHUDOptions.offsetFromMiddle;
            int offsetBottomArmor = config.equipmentTracker.armorHUDOptions.offsetFromBottom;
            int offsetBottomRodParts = config.equipmentTracker.rodPartsHUDOptions.offsetFromBottom;

            // Chestplate
            renderBox(drawContext, textRenderer, scaledX - 64 + offsetMiddleArmor, scaledY - offsetBottomArmor - 20, alphaInt, chestplate, "ᴄ");
            // Leggings
            renderBox(drawContext, textRenderer, scaledX - 42 + offsetMiddleArmor, scaledY - offsetBottomArmor - 20, alphaInt, leggings, "ʟ");
            // Boots
            renderBox(drawContext, textRenderer, scaledX - 20 + offsetMiddleArmor, scaledY - offsetBottomArmor - 20, alphaInt, boots, "ʙ");

            // Reel
            renderBox(drawContext, textRenderer, scaledX + offsetMiddleRodParts, scaledY - offsetBottomRodParts - 20, alphaInt, reel, "ʀ");
            // Pole
            renderBox(drawContext, textRenderer, scaledX + 22 + offsetMiddleRodParts, scaledY - offsetBottomRodParts - 20, alphaInt, pole, "ᴘ");
            // Line
            renderBox(drawContext, textRenderer, scaledX + 44 + offsetMiddleRodParts, scaledY - offsetBottomRodParts - 20, alphaInt, line, "ʟ");

        } finally {
            drawContext.getMatrices().pop();
        }
    }

    private void renderBox(DrawContext drawContext, TextRenderer textRenderer, int x, int y, int alpha, ItemStack itemStack, String character) {
        drawContext.fill(x, y, x + 20, y + 20, alpha);
        drawContext.drawBorder(x, y + 20, 20, 1, alpha | 0xFFFFFF);
        if(itemStack.getItem() != Items.AIR) {
            drawContext.drawItem(itemStack, x + 2, y + 2);
        } else {
            drawContext.drawText(textRenderer, Text.literal(character).formatted(Formatting.GRAY), x + 10 - textRenderer.getWidth(character) / 2, y + 10 - textRenderer.fontHeight / 2, alpha | 0xFFFFFF, true);
        }
    }
}
