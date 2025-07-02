package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Fish;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ItemMarkerHandler {
    private static ItemMarkerHandler INSTANCE = new ItemMarkerHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private final Identifier rarityMarker = Identifier.of(FishOnMCExtras.MOD_ID, "icons/rarity");
    private final Identifier petItemMarker = Identifier.of(FishOnMCExtras.MOD_ID, "icons/pet_item");
    private final Identifier fishSizeMarker = Identifier.of(FishOnMCExtras.MOD_ID, "icons/fish_size");
    private final Identifier selectedSlotMarker = Identifier.of(FishOnMCExtras.MOD_ID, "icons/selected_slot");


    public static ItemMarkerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemMarkerHandler();
        }
        return INSTANCE;
    }

    public void renderItemMarker(DrawContext drawContext, Slot slot) {
        if(config.itemMarker.showItemMarker && !slot.getStack().isEmpty()) {
            renderItemMarker(drawContext, slot.getStack(), slot.x, slot.y);
        }
    }

    public void renderItemMarker(DrawContext drawContext, ItemStack itemStack, int x, int y) {
        Constant rarity = FOMCItem.getRarity(itemStack);
        if(FOMCItem.isFish(itemStack)
                && rarity != Constant.DEFAULT
                && (config.itemMarker.showFishRarityMarker || config.itemMarker.showFishSizeMarker != FishSizeMarkerToggle.OFF)
        ) {
            Constant size = Fish.getSize(itemStack);

            int alpha =  ((int) 255f << 24);
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 290);
                if (config.itemMarker.showFishRarityMarker) {
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, rarityMarker, x, y, 16, 16, alpha | rarity.COLOR);
                }

                if (config.itemMarker.showFishSizeMarker == FishSizeMarkerToggle.CHARACTER) {
                    Text sizeChar = Text.literal(size.TAG.getString().substring(0, 1)).withColor(size.COLOR);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, sizeChar, x + 16 - MinecraftClient.getInstance().textRenderer.getWidth(sizeChar), y + 16 - MinecraftClient.getInstance().textRenderer.fontHeight + 1, 0xFFFFFF, true);
                } else if(config.itemMarker.showFishSizeMarker == FishSizeMarkerToggle.MARKER) {
                    drawContext.drawGuiTexture(RenderLayer::getGuiTextured, fishSizeMarker, x, y, 16, 16, alpha | size.COLOR);
                }
            } finally {
                drawContext.getMatrices().pop();
            }
        }

        if(config.itemMarker.showOtherRarityMarker
                && rarity != Constant.DEFAULT
        ) {
            int alpha =  ((int) 255f << 24);
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 290);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, rarityMarker, x, y, 16, 16, alpha | rarity.COLOR);
            } finally {
                drawContext.getMatrices().pop();
            }
        }

        boolean[] pet = FOMCItem.isPet(itemStack);
        if(pet[0] && (pet[1] || pet[2] || pet[3])) {
            int alpha =  ((int) 255f << 24);
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 290);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, petItemMarker, x, y, 16, 16, alpha | 0xFFFFFF);
            } finally {
                drawContext.getMatrices().pop();
            }
        }

        if(MinecraftClient.getInstance().player != null
                && ProfileDataHandler.instance().profileData.equippedPet != null
                && itemStack.equals(MinecraftClient.getInstance().player.getInventory().getStack(ProfileDataHandler.instance().profileData.equippedPetSlot))) {
            int alpha =  ((int) 175f << 24);
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 100);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, selectedSlotMarker, x, y, 16, 16, alpha | 0xFFAA00);
            } finally {
                drawContext.getMatrices().pop();
            }
        }
    }

    public void renderHotBarSelectedPet(DrawContext drawContext, int x, int y, ItemStack itemStack) {
        if(MinecraftClient.getInstance().player != null
                && !itemStack.isEmpty()
                && ProfileDataHandler.instance().profileData.equippedPet != null
                && itemStack.equals(MinecraftClient.getInstance().player.getInventory().getStack(ProfileDataHandler.instance().profileData.equippedPetSlot))
        ) {
            int alpha =  ((int) 175f << 24);
            drawContext.getMatrices().push();
            try {
                drawContext.getMatrices().translate(0, 0, 100);
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, selectedSlotMarker, x, y, 16, 16, alpha | 0xFFAA00);
            } finally {
                drawContext.getMatrices().pop();
            }
        }
    }

    public enum FishSizeMarkerToggle {
        OFF,
        CHARACTER,
        MARKER
    }
}
