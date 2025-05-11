package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
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
                        FOMCItem.getFOMCItem(itemStack) instanceof FOMCItem
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
