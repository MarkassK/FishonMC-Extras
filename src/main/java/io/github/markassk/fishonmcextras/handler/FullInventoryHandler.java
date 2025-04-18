package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;

public class FullInventoryHandler {
    private static FullInventoryHandler INSTANCE = new FullInventoryHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public boolean isOverThreshold = false;
    public int slotsLeft = 0;

    public static FullInventoryHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FullInventoryHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        int emptySlots = 0;
        if(minecraftClient.player != null) {
            for (int i = 0; i < 36; i++) { //  0-8 are hotbar slots / 9-35 are main inventory slots
                if (minecraftClient.player.getInventory().getStack(i).isEmpty()) {
                    emptySlots++;
                }
            }

            if (emptySlots <= config.fullInventoryTracker.fullInventoryWarningThreshold) {
                isOverThreshold = true;
            } else {
                isOverThreshold = false;
            }

            slotsLeft = emptySlots;
        }
    }
}
