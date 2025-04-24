package io.github.markassk.fishonmcextras.screens.hud;

import io.github.markassk.fishonmcextras.handler.LookTickHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class ItemFrameTooltipHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        //

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        if(LookTickHandler.instance().targetedItemInItemFrame != null) {
            drawContext.drawItemTooltip(textRenderer, LookTickHandler.instance().targetedItemInItemFrame, screenWidth / 2, screenHeight / 2);
        }
    }
}
