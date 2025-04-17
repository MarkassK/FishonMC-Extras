package io.github.markassk.fishonmcextras.v1.handler;

import io.github.markassk.fishonmcextras.v1.overlay.RayTracing;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class LookTickHandler {
    private static LookTickHandler INSTANCE = new LookTickHandler();
    public ItemStack targetedItem = null;

    public static LookTickHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new LookTickHandler();
        }
        return INSTANCE;
    }

    public void tickClient() {
        RayTracing.INSTANCE.fire();
        HitResult hitResult = RayTracing.INSTANCE.getTarget();

        if(hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();

            if (entity instanceof ItemFrameEntity itemFrame) {
                ItemStack itemStack = itemFrame.getHeldItemStack();

                // Only allow Items from FishOnMC
                if (
                        (itemStack.getItem() == Items.PLAYER_HEAD
                                || itemStack.getItem() == Items.COD
                                || itemStack.getItem() == Items.WHITE_DYE
                                || itemStack.getItem() == Items.BLACK_DYE
                                || itemStack.getItem() == Items.GOLD_INGOT
                                || itemStack.getItem() == Items.ROTTEN_FLESH
                                || itemStack.getItem() == Items.LEATHER_BOOTS
                                || itemStack.getItem() == Items.LEATHER_LEGGINGS
                                || itemStack.getItem() == Items.LEATHER_CHESTPLATE
                                || itemStack.getItem() == Items.COOKED_COD
                                || itemStack.getItem() == Items.SLIME_BALL) &&
                                itemStack.contains(DataComponentTypes.CUSTOM_DATA)
                ) {
                    targetedItem = itemStack;
                } else {
                    targetedItem = null;
                }
            }
        } else {
            targetedItem = null;
        }
    }
}
