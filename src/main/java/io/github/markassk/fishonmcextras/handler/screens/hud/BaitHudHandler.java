package io.github.markassk.fishonmcextras.handler.screens.hud;

import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import io.github.markassk.fishonmcextras.handler.FishingRodHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BaitHudHandler {
    private static BaitHudHandler INSTANCE = new BaitHudHandler();

    public static BaitHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BaitHudHandler();
        }
        return INSTANCE;
    }


    public Text assembleBaitText() {
        return !FishingRodHandler.instance().fishingRod.tacklebox.isEmpty() ? TextHelper.concat(
                FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Bait bait ?
                        Text.literal(TextHelper.upperCaseAllFirstCharacter(bait.name)).formatted(Formatting.WHITE) :
                        FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Lure lure ?
                                Text.literal(TextHelper.upperCaseAllFirstCharacter(lure.name)).formatted(Formatting.WHITE) :
                                Text.empty(),
                Text.literal(": ").formatted(Formatting.GRAY),
                FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Bait bait ?
                        Text.literal(String.valueOf(bait.counter)).formatted(Formatting.WHITE) :
                        FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Lure lure ?
                                Text.literal(String.valueOf(lure.counter)).formatted(Formatting.WHITE) :
                                Text.empty(),
                Text.literal("x").formatted(Formatting.GRAY)
        ) : Text.literal("");
    }

    public CustomModelDataComponent getModelData() {
        return !FishingRodHandler.instance().fishingRod.tacklebox.isEmpty() ? FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Bait bait ?
                bait.customModelData :
                    FishingRodHandler.instance().fishingRod.tacklebox.getFirst() instanceof Lure lure ?
                            lure.customModelData :
                            CustomModelDataComponent.DEFAULT
                : CustomModelDataComponent.DEFAULT;
    }
}
