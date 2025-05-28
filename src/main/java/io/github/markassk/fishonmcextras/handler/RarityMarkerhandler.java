package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;


public class RarityMarkerhandler {
    private static RarityMarkerhandler INSTANCE = new RarityMarkerhandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private final Identifier rarityMarker = Identifier.of(FishOnMCExtras.MOD_ID, "icons/rarity");


    public static RarityMarkerhandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new RarityMarkerhandler();
        }
        return INSTANCE;
    }

    public void renderRarityMarker(DrawContext drawContext, Slot slot) {
        if(config.rarityMarker.showRarityMarker) {
            renderRarityMarker(drawContext, slot.getStack(), slot.x, slot.y);
        }
    }

    public void renderRarityMarker(DrawContext drawContext, ItemStack itemStack, int x, int y) {
        Constant rarity = FOMCItem.getRarity(itemStack);
        if(FOMCItem.isFish(itemStack) && !config.rarityMarker.showFishRarityMarker) {
            return;
        } else if (!FOMCItem.isFish(itemStack) && !config.rarityMarker.showOtherRarityMarker) {
            return;
        }

        if(rarity != Constant.DEFAULT) {
            int alpha =  ((int) 255f << 24);
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 290);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, rarityMarker, x, y, 16, 16, alpha | rarity.COLOR);
            } finally {
                drawContext.getMatrices().pop();
            }
        }
    }
}
