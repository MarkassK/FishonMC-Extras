package io.github.markassk.fishonmcextras.handler;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class LookTickHandler {
    private static LookTickHandler INSTANCE = new LookTickHandler();

    public ItemStack targetedItemInItemFrame = null;

    public static LookTickHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new LookTickHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        HitResult hitResult = RayTracingHandler.instance().getTarget();

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
                    targetedItemInItemFrame = itemStack;
                } else {
                    targetedItemInItemFrame = null;
                }
            }
        } else {
            targetedItemInItemFrame = null;
        }
    }
}
