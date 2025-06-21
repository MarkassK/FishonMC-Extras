package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.*;

public class HiderHandler {
    private static HiderHandler INSTANCE = new HiderHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private Map<Integer, ItemStack> otherEntities = new HashMap<>();
    private Map<Integer, ItemStack> ownEntities = new HashMap<>();

    public static HiderHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new HiderHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(PetEquipHandler.instance().petStatus != PetEquipHandler.PetStatus.LOADING
        ) {
            if(config.petFollower.ownPet != FollowingPetState.HIDE_ALL) {
                if(!ownEntities.isEmpty()){
                    ownEntities.forEach((integer, stack) -> {
                        if(minecraftClient.world != null) {
                            Entity foundEntity = minecraftClient.world.getEntityById(integer);
                            if (foundEntity != null) {
                                ((ArmorStandEntity) foundEntity).setStackInHand(Hand.MAIN_HAND, stack);
                            }
                        }
                    });
                    ownEntities.clear();
                }
            }

            if(config.petFollower.otherPets != FollowingPetState.HIDE_ALL) {
                if(!otherEntities.isEmpty()) {
                    otherEntities.forEach((integer, stack) -> {
                        if(minecraftClient.world != null) {
                            Entity foundEntity = minecraftClient.world.getEntityById(integer);
                            if (foundEntity != null) {
                                ((ArmorStandEntity) foundEntity).setStackInHand(Hand.MAIN_HAND, stack);
                            }
                        }
                    });
                    otherEntities.clear();
                }
            }
        }
    }

    public void tickEntities(Entity entity, MinecraftClient minecraftClient) {
        if ((config.petFollower.ownPet != FollowingPetState.OFF
                || config.petFollower.otherPets != FollowingPetState.OFF)
                && PetEquipHandler.instance().petStatus != PetEquipHandler.PetStatus.LOADING
                && entity instanceof ArmorStandEntity armorStandEntity) {
            ItemStack mainHandStack = armorStandEntity.getMainHandStack();
            if (mainHandStack.getItem() != Items.AIR) {
                if (minecraftClient.player != null) {
//                    Pet pet = Pet.getPet(mainHandStack);
                    if (ProfileDataHandler.instance().profileData.equippedPet != null
//                            && Objects.equals(pet.id, ProfileDataHandler.instance().profileData.equippedPet.id)
//                            && Objects.equals(minecraftClient.player.getInventory().getStack(ProfileDataHandler.instance().profileData.equippedPetSlot), mainHandStack)
                            && minecraftClient.player.getInventory().contains(mainHandStack)
                    ) {
                        checkOwnPet(armorStandEntity, mainHandStack);
                    } else {
                        checkOtherPet(armorStandEntity, mainHandStack);
                    }
                }
            }
        }
    }

    private void checkOwnPet(ArmorStandEntity armorStandEntity, ItemStack mainHandStack) {
        if(config.petFollower.ownPet == FollowingPetState.HIDE_ALL) {
            armorStandEntity.setStackInHand(Hand.MAIN_HAND, Items.AIR.getDefaultStack());
            if(!ownEntities.containsKey(armorStandEntity.getId())) {
                ownEntities.put(armorStandEntity.getId(), mainHandStack);
            }
        }
    }

    private void checkOtherPet(ArmorStandEntity armorStandEntity, ItemStack mainHandStack) {
        if(config.petFollower.otherPets == FollowingPetState.HIDE_ALL) {
            armorStandEntity.setStackInHand(Hand.MAIN_HAND, Items.AIR.getDefaultStack());
            if(!otherEntities.containsKey(armorStandEntity.getId())) {
                otherEntities.put(armorStandEntity.getId(), mainHandStack);
            }
        }
    }

    public enum FollowingPetState {
        OFF,
        HIDE_NAME,
        HIDE_ALL,
    }
}
