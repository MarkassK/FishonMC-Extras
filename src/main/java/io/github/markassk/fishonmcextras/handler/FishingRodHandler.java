package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LocationInfo;
import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.FishingRod;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class FishingRodHandler {
    private static FishingRodHandler INSTANCE = new FishingRodHandler();

    private ItemStack fishingRodStack = null;

    public FishingRod fishingRod = null;
    public boolean isWrongBait = false;
    public boolean isWrongLure = false;
    public boolean isWrongPole = false;
    public boolean isWrongReel = false;
    public boolean isWrongLine = false;


    public static FishingRodHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishingRodHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.player != null && minecraftClient.player.getInventory().main.getFirst().getItem() == Items.FISHING_ROD) {
            if(this.fishingRodStack == null || !this.fishingRodStack.equals(minecraftClient.player.getInventory().main.getFirst())) {
                if(FOMCItem.getFOMCItem(minecraftClient.player.getInventory().main.getFirst()) instanceof FishingRod rod) {
                    this.fishingRodStack = minecraftClient.player.getInventory().main.getFirst();
                    this.fishingRod = rod;
                }
            }
        }

        if(this.fishingRod != null) {
            if(!this.fishingRod.tacklebox.isEmpty()) {
                // Bait
                if(this.fishingRod.tacklebox.getFirst() instanceof Bait bait && bait.water != Constant.ANY_WATER) {
                    this.isWrongBait = bait.water != LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).WATER;
                } else if (this.fishingRod.tacklebox.getFirst() instanceof Lure lure && lure.water != Constant.ANY_WATER) {
                    this.isWrongLure = lure.water != LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).WATER;
                }
            } else {
                this.isWrongBait = false;
                this.isWrongLure = false;
            }

            if(this.fishingRod.reel != null && this.fishingRod.reel.water != Constant.GLOBAL_WATER) {
                this.isWrongReel = this.fishingRod.reel.water != LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).WATER;
            } else {
                this.isWrongReel = false;
            }

            if(this.fishingRod.pole != null && this.fishingRod.pole.water != Constant.GLOBAL_WATER) {
                this.isWrongPole = this.fishingRod.pole.water != LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).WATER;
            } else {
                this.isWrongPole = false;
            }

            if(this.fishingRod.line != null && this.fishingRod.line.water != Constant.GLOBAL_WATER) {
                this.isWrongLine = this.fishingRod.line.water != LocationInfo.valueOfId(LocationHandler.instance().currentLocation.ID).WATER;
            } else {
                this.isWrongLine = false;
            }
        }
    }
}
