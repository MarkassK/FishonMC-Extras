package io.github.markassk.fishonmcextras.handler;

import net.minecraft.client.MinecraftClient;

public class OwnPlayerHandler {
    private static OwnPlayerHandler INSTANCE = new OwnPlayerHandler();

    private int prevSelectedSlot = -1;

    public long changedSlotTime = 0L;

    public static OwnPlayerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new OwnPlayerHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player != null
                && prevSelectedSlot != minecraftClient.player.getInventory().getSelectedSlot()
        ) {
            prevSelectedSlot = minecraftClient.player.getInventory().getSelectedSlot();
            changedSlotTime = System.currentTimeMillis();
        }
    }
}
