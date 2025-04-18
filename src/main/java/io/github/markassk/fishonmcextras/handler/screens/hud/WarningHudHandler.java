package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.FullInventoryHandler;
import io.github.markassk.fishonmcextras.handler.PetEquipHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class WarningHudHandler {
    private static WarningHudHandler INSTANCE = new WarningHudHandler();

    public static WarningHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new WarningHudHandler();
        }
        return INSTANCE;
    }

    public List<Text> assembleWarningText() {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        List<Text> textList = new ArrayList<>();

        if(config.petEquipTracker.warningOptions.showPetEquipWarningHUD
                && PetEquipHandler.instance().petStatus == PetEquipHandler.PetStatus.NO_PET
        ) {
            textList.add(Text.literal("No pet equipped").formatted(Formatting.RED));
        }

        // Full Inventory Warning Sound
        if(config.fullInventoryTracker.showFullInventoryWarningHUD
                && FullInventoryHandler.instance().isOverThreshold
        ) {
            textList.add(TextHelper.concat(
                    Text.literal("Inventory almost full. Slots left: ").formatted(Formatting.RED),
                    Text.literal(String.valueOf(FullInventoryHandler.instance().slotsLeft)).formatted(Formatting.WHITE)
            ));
        }

        if(!textList.isEmpty()) {
            textList.addFirst(Text.literal("ᴡᴀʀɴɪɴɢѕ").formatted(Formatting.GRAY));
        }

        return textList;
    }
}
