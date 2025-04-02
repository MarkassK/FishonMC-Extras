package io.github.markassk.fishonmcextras.trackers;

import io.github.markassk.fishonmcextras.hud.HudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import java.util.*;

public class FishTracker {
    private final Map<Integer, ItemStack> previousInventory = new HashMap<>();
    private final Set<String> trackedFishUids = new HashSet<>();
    private final HudRenderer hudRenderer;
    private UUID playerUUID;
    private String currentWorldId = "";
    private int retryScansRemaining = 8;

    public FishTracker(HudRenderer hudRenderer) {
        this.hudRenderer = hudRenderer;
    }

    public void updateWorldContext(MinecraftClient client) {
        String newWorldId = getWorldIdentifier(client);
        if (!newWorldId.equals(currentWorldId)) {
            //System.out.println("[DEBUG] New world detected: " + newWorldId);
            currentWorldId = newWorldId;
            trackedFishUids.clear();
            previousInventory.clear();
            retryScansRemaining = 8;
            playerUUID = client.player != null ? client.player.getUuid() : null;
        }
    }

    private String getWorldIdentifier(MinecraftClient client) {
        if (client.isInSingleplayer()) {
            var server = client.getServer();
            return server != null ?
                    "local:" + server.getSaveProperties().getLevelName() :
                    "local:unknown";
        }
        var serverInfo = client.getCurrentServerEntry();
        return serverInfo != null ?
                "remote:" + serverInfo.address.replace(":", "_") :
                "remote:unknown";
    }

    private void scanInventory(PlayerEntity player) {
        System.out.println("[DEBUG] Scanning inventory");
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (isFishItem(stack)) {
                NbtCompound nbt = getFishNbt(stack);
                if (nbt != null && isPlayerCaught(nbt)) {
                    String uid = createFishUid(nbt);
                    if (!trackedFishUids.contains(uid)) {
                        trackedFishUids.add(uid);
                        System.out.println("[DEBUG] Tracked UID: " + uid);
                    }
                }
            }
        }
    }

    public void tick(MinecraftClient client, boolean menuOpened, long lastMenuCloseTime) {
        PlayerEntity player = client.player;
        if (player == null) return;

        //handle initial retries
        if (retryScansRemaining > 0){
           retryScansRemaining--;
           if (retryScansRemaining == 8 - 1){
               scanInventory(player);
               System.out.println("[DEBUG] Initial inventory scan completed");
           } else if (retryScansRemaining % 5 == 0) {
               System.out.println("[DEBUG] Retrying inventory scan (" + retryScansRemaining + " left)");
               scanInventory(player);
           }
        }

        // Scan all inventory slots for fish
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (isFishItem(stack) && isNewFish(stack)) {
                // Track the fish
                trackFish(stack);

                System.out.println("[DEBUG] Processing new fish. Total tracked: " + trackedFishUids.size());
                if (!(menuOpened || System.currentTimeMillis() - lastMenuCloseTime < 200)) {
                    processFish(stack);
                }
            }
        }
    }

    private boolean isFishItem(ItemStack stack) {
        //System.out.println("[DEBUG] Checking if fish: " + stack);
        return !stack.isEmpty() &&
                (stack.getItem() == Items.COD ||
                stack.getItem() == Items.WHITE_DYE ||
                stack.getItem() == Items.BLACK_DYE ||
                stack.getItem() == Items.GOLD_INGOT ||
                stack.getItem() == Items.ROTTEN_FLESH) &&
                stack.contains(DataComponentTypes.CUSTOM_DATA);
    }

    private NbtCompound getFishNbt(ItemStack stack) {
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        return component != null ? component.getNbt() : null;
    }

    private boolean isPlayerCaught(NbtCompound nbt) {
        if (!nbt.contains("catcher") || playerUUID == null) return false;
        int[] catcher = nbt.getIntArray("catcher");
        if (catcher.length != 4) return false;

        try {
            long mostSigBits = ((long) catcher[0] << 32) | (catcher[1] & 0xFFFFFFFFL);
            long leastSigBits = ((long) catcher[2] << 32) | (catcher[3] & 0xFFFFFFFFL);
            return new UUID(mostSigBits, leastSigBits).equals(playerUUID);
        } catch (Exception e) {
            return false;
        }
    }

    private String createFishUid(NbtCompound nbt) {
        return Arrays.toString(nbt.getIntArray("id")) + "|" + currentWorldId;
    }

    private void trackFish(ItemStack stack) {
        NbtCompound nbt = getFishNbt(stack);
        if (nbt != null && isPlayerCaught(nbt)) {
            trackedFishUids.add(createFishUid(nbt));
        }
    }

    private boolean isNewFish(ItemStack stack) {
        NbtCompound nbt = getFishNbt(stack);
        return nbt != null && isPlayerCaught(nbt) && !trackedFishUids.contains(createFishUid(nbt));
    }

    private void processFish(ItemStack stack) {
        NbtCompound nbt = getFishNbt(stack);
        if (nbt == null) return;

        hudRenderer.updateFishHUD(
                nbt.getFloat("xp"),
                nbt.getFloat("value"),
                nbt.getString("variant"),
                nbt.getString("rarity"),
                nbt.getString("size")
        );
    }

    public void reset() {
        trackedFishUids.clear();
        previousInventory.clear();
    }
}