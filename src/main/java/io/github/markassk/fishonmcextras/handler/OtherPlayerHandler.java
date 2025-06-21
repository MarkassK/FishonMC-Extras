package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types.*;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.VectorHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.*;

public class OtherPlayerHandler {
    private static OtherPlayerHandler INSTANCE = new OtherPlayerHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public PlayerListEntry highlightedPlayer = null;
    public long highlightStartTime = 0L;
    public boolean isHighlighted = false;

    private PlayerEntity targetedPlayer = null;
    private BlockPos previousPos = BlockPos.ofFloored(0, 0, 0);
    private BlockState previousBlockState = Blocks.AIR.getDefaultState();
    // First Double is Index, Second Double is Distance, Third Double is Angle
    private final Map<DisplayEntity, List<Double>> displayEntityList = new HashMap<>();

    private final double DISTANCE = 1.5;
    private final double verticalOffset = 1.2;
    private final double lineHeight = 0.3;

    public static OtherPlayerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new OtherPlayerHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if (config.hoverOverPlayerStats.showPlayerEquipment) {
            PlayerEntity target = null;
            if(minecraftClient.player != null && minecraftClient.player.isSneaking()) {
                target = LookTickHandler.instance().targetedPlayerEntity;
            }

            if(target != null && targetedPlayer != target) {
                this.targetedPlayer = target;
                removeTextDisplayEntities(minecraftClient);

                if(target.getDisplayName() != null && !Objects.equals(target.getDisplayName().getString(), "")) {
                    this.spawnDisplayEntities(minecraftClient, target);
                }

            } else if(target == targetedPlayer) {
                this.updateTextDisplayEntities(minecraftClient.player, targetedPlayer);
            }

            if(target == null) {
                if(!displayEntityList.isEmpty()) {
                    this.removeTextDisplayEntities(minecraftClient);
                }

                if(targetedPlayer != null) {
                    this.targetedPlayer = null;
                }
            }

            // Light
            spawnLight(minecraftClient, targetedPlayer);
        }

        if(System.currentTimeMillis() - highlightStartTime <= 300000L && highlightedPlayer != null) {
            this.isHighlighted = true;
        } else if (this.isHighlighted && highlightedPlayer != null) {
            highlightedPlayer = null;
            this.isHighlighted = false;
        }
    }

    private void removeTextDisplayEntities(MinecraftClient minecraftClient){
        if (minecraftClient.world == null) return;
        displayEntityList.forEach((displayEntity, entry) -> minecraftClient.world.removeEntity(displayEntity.getId(), Entity.RemovalReason.DISCARDED));
        displayEntityList.clear();
    }

    private void spawnDisplayEntities(MinecraftClient minecraftClient, PlayerEntity targetedPlayer) {
        // Left Side
        targetedPlayer.getArmorItems().forEach(itemStack -> {
            if(FOMCItem.isFOMCItem(itemStack)) {
                Armor armor = Armor.getArmor(itemStack);
                if(armor != null) {
                    if(itemStack.getItem() == Items.LEATHER_CHESTPLATE)
                        showItemStats(minecraftClient, targetedPlayer, itemStack, armor.rarity.TAG, 270, 1, DISTANCE);
                    else if (itemStack.getItem() == Items.LEATHER_LEGGINGS)
                        showItemStats(minecraftClient, targetedPlayer, itemStack, armor.rarity.TAG, 270, 2.1, DISTANCE);
                    else if (itemStack.getItem() == Items.LEATHER_BOOTS)
                        showItemStats(minecraftClient, targetedPlayer, itemStack, armor.rarity.TAG, 270, 3.2, DISTANCE);
                }
            }
        });

        // Right Side
        ItemStack mainhandStack = targetedPlayer.getMainHandStack().copy();
        if(FOMCItem.isFOMCItem(mainhandStack)) {
            FishingRod fishingRod = FishingRod.getFishingRod(mainhandStack);
            if(fishingRod != null) {
                spawnItemDisplay(minecraftClient, mainhandStack, targetedPlayer.getPos(), 90, 2.3, DISTANCE - .6, 1.0f, ModelTransformationMode.FIRST_PERSON_RIGHT_HAND);
                spawnTextDisplay(minecraftClient, mainhandStack.getName(), targetedPlayer.getPos(), 90, 0, DISTANCE, 0.8f);

                if(fishingRod.reel != null) {
                    ItemStack itemStack = Items.FLINT.getDefaultStack().copy();
                    itemStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, fishingRod.reel.customModelData);
                    showItemStats(minecraftClient, targetedPlayer, itemStack, fishingRod.reel.rarity.TAG, 90, 1, DISTANCE + .4);
                }

                if(fishingRod.pole != null) {
                    ItemStack itemStack = Items.BLAZE_ROD.getDefaultStack().copy();
                    itemStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, fishingRod.pole.customModelData);
                    showItemStats(minecraftClient, targetedPlayer, itemStack, fishingRod.pole.rarity.TAG, 90, 2.1, DISTANCE + .4);
                }

                if(fishingRod.line != null) {
                    ItemStack itemStack = Items.FEATHER.getDefaultStack().copy();
                    itemStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, fishingRod.line.customModelData);
                    showItemStats(minecraftClient, targetedPlayer, itemStack, fishingRod.line.rarity.TAG, 90, 3.2, DISTANCE + .4);
                }
            }
        }

        // Below
        spawnTextDisplay(minecraftClient, Text.literal("ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴛʀᴀᴅᴇ").formatted(Formatting.YELLOW), targetedPlayer.getPos(), 180, 3.5, .4, .5f, DisplayEntity.BillboardMode.CENTER);
    }

    private void spawnLight(MinecraftClient minecraftClient, PlayerEntity targetedPlayer) {
        if(targetedPlayer != null && minecraftClient.world != null && !Objects.equals(this.previousPos, BlockPos.ofFloored(targetedPlayer.getPos()).up())) {
            minecraftClient.world.setBlockState(this.previousPos, this.previousBlockState);
            this.previousBlockState = minecraftClient.world.getBlockState(BlockPos.ofFloored(targetedPlayer.getPos()).up());
            this.previousPos = BlockPos.ofFloored(targetedPlayer.getPos()).up();
            if(minecraftClient.world.getBlockState(this.previousPos).getBlock() == Blocks.WATER) {
                BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
                minecraftClient.world.setBlockState(this.previousPos, Blocks.LIGHT.getDefaultState().with(WATERLOGGED, Boolean.TRUE));
            } else {
                minecraftClient.world.setBlockState(this.previousPos, Blocks.LIGHT.getDefaultState());
            }
        } else if(minecraftClient.world != null && targetedPlayer == null && !Objects.equals(this.previousPos, BlockPos.ofFloored(0, 0, 0))) {
            minecraftClient.world.setBlockState(this.previousPos, this.previousBlockState);
            this.previousPos = BlockPos.ofFloored(0, 0, 0);
            this.previousBlockState = Blocks.AIR.getDefaultState();
        }
    }

    private void showItemStats(MinecraftClient minecraftClient, PlayerEntity targetedPlayer, ItemStack itemStack, Text text, double angle, double index, double distance) {
        spawnItemDisplay(minecraftClient, itemStack, targetedPlayer.getPos(), angle, index, distance - .6, .7f);
        spawnTextDisplay(minecraftClient, text, targetedPlayer.getPos(), angle, index, distance + .15, .7f);
    }

    private void updateTextDisplayEntities(PlayerEntity player, PlayerEntity targetedPlayer) {
        displayEntityList.forEach((displayEntity, entry) -> displayEntity.setPosition(VectorHelper.getPoint(player.getPos(), targetedPlayer.getPos(), entry.get(1), entry.getLast()).add(0, verticalOffset - entry.getFirst() * lineHeight, 0)));
    }

    private void spawnTextDisplay(MinecraftClient minecraftClient, Text text, Vec3d position, double angle, double index, double distance, float scale, DisplayEntity.BillboardMode billboardMode) {
        if (minecraftClient.world == null || minecraftClient.player == null) return;
        DisplayEntity.TextDisplayEntity textDisplayEntity = Objects.requireNonNull(EntityType.TEXT_DISPLAY.create(minecraftClient.world, SpawnReason.TRIGGERED));
        minecraftClient.world.addEntity(textDisplayEntity);

        displayEntityList.put(textDisplayEntity, List.of(index, distance, angle));

        textDisplayEntity.setPosition(VectorHelper.getPoint(minecraftClient.player.getPos(), position, distance, angle).add(0, verticalOffset - index * lineHeight, 0));
        textDisplayEntity.setBillboardMode(billboardMode);
        textDisplayEntity.setBackground(0x00000000);
        textDisplayEntity.setTransformation(new AffineTransformation(null, null, new Vector3f(scale, scale, scale), null));

        textDisplayEntity.setText(text);
    }

    private void spawnTextDisplay(MinecraftClient minecraftClient, Text text, Vec3d position, double angle, double index, double distance, float scale) {
        spawnTextDisplay(minecraftClient, text, position, angle, index, distance, scale, DisplayEntity.BillboardMode.VERTICAL);
    }

    private void spawnTextDisplay(MinecraftClient minecraftClient, Text text, Vec3d position, double angle, double index, double distance) {
        spawnTextDisplay(minecraftClient, text, position, angle, index, distance, 1.0f, DisplayEntity.BillboardMode.VERTICAL);
    }

    private void spawnItemDisplay(MinecraftClient minecraftClient, ItemStack itemStack, Vec3d position, double angle, double index, double distance, float scale, ModelTransformationMode modelTransformationMode) {
        if (minecraftClient.world == null || minecraftClient.player == null) return;
        DisplayEntity.ItemDisplayEntity itemDisplayEntity = Objects.requireNonNull(EntityType.ITEM_DISPLAY.create(minecraftClient.world, SpawnReason.TRIGGERED));
        minecraftClient.world.addEntity(itemDisplayEntity);

        displayEntityList.put(itemDisplayEntity, List.of(index, distance, angle));

        itemDisplayEntity.setItemStack(itemStack);
        itemDisplayEntity.setPosition(VectorHelper.getPoint(minecraftClient.player.getPos(), position, distance, angle).add(0, verticalOffset - index * lineHeight, 0));
        itemDisplayEntity.setBillboardMode(DisplayEntity.BillboardMode.VERTICAL);
        itemDisplayEntity.setTransformationMode(modelTransformationMode);
        itemDisplayEntity.setTransformation(new AffineTransformation(null, null, new Vector3f(scale, scale, scale), null));
    }

    private void spawnItemDisplay(MinecraftClient minecraftClient, ItemStack itemStack, Vec3d position, double angle, double index, double distance, float scale) {
        spawnItemDisplay(minecraftClient, itemStack, position, angle, index, distance, scale, ModelTransformationMode.GROUND);
    }

    private void spawnItemDisplay(MinecraftClient minecraftClient, ItemStack itemStack, Vec3d position, double angle, double index, double distance) {
        spawnItemDisplay(minecraftClient, itemStack, position, angle, index, distance, 1.0f, ModelTransformationMode.GROUND);
    }
}
