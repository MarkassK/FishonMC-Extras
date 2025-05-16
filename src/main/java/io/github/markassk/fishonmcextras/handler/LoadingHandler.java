package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Objects;

public class LoadingHandler {
    private static LoadingHandler INSTANCE = new LoadingHandler();

    public boolean isLoadingDone = false;
    public boolean isOnServer = false;

    public static LoadingHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new LoadingHandler();
        }
        return INSTANCE;
    }

    public void init() {
        isLoadingDone = false;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player != null && !isLoadingDone) {
            for (int slot = 0; slot < minecraftClient.player.getInventory().getMainStacks().size(); slot++) {
                ItemStack stack = minecraftClient.player.getInventory().getMainStacks().get(slot);
                if(stack.get(DataComponentTypes.CUSTOM_DATA) != null && stack.getItem() == Items.FISHING_ROD) {
                    isLoadingDone = true;
                    FishOnMCExtras.LOGGER.info("[FoE] Loading Done");

                    PetEquipHandler.instance().startScanTime = System.currentTimeMillis();
                }
            }
        }
    }

    public boolean checkAddress(MinecraftClient minecraftClient) {
        return Objects.requireNonNull(minecraftClient.getCurrentServerEntry()).address.equalsIgnoreCase("play.fishonmc.net") || Objects.requireNonNull(minecraftClient.getCurrentServerEntry()).address.equalsIgnoreCase("fishonmc.net");
    }
}
