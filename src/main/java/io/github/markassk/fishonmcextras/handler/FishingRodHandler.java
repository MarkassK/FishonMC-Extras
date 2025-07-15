package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LocationInfo;
import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.FishingRod;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.mixin.InGameHudAccessor;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.*;

public class FishingRodHandler {
    private static FishingRodHandler INSTANCE = new FishingRodHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    private ItemStack fishingRodStack = null;
    private Map<Integer, Integer> baitDisplay = new HashMap<>();

    private BlockPos previousPos = BlockPos.ofFloored(0, 0, 0);
    private BlockState previousBlockState = Blocks.AIR.getDefaultState();
    private boolean isBobberOut = false;

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
                this.fishingRodStack = minecraftClient.player.getInventory().main.getFirst();
                FishingRod fishingRod = FishingRod.getFishingRod(minecraftClient.player.getInventory().main.getFirst());
                if(fishingRod != null) {
                    this.fishingRod = fishingRod;
                }
            }
        }

        if(this.fishingRod != null) {
            if(!this.fishingRod.tacklebox.isEmpty()) {
                // Bait
                if(this.fishingRod.tacklebox.getFirst() instanceof Bait bait && bait.water != Constant.ANY_WATER) {
                    this.isWrongBait = bait.water != LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER;
                } else if (this.fishingRod.tacklebox.getFirst() instanceof Lure lure && lure.water != Constant.ANY_WATER) {
                    this.isWrongLure = lure.water != LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER;
                } else {
                    this.isWrongBait = false;
                    this.isWrongLure = false;
                }
            } else {
                this.isWrongBait = false;
                this.isWrongLure = false;
            }

            if(this.fishingRod.reel != null && this.fishingRod.reel.water != Constant.GLOBAL_WATER) {
                this.isWrongReel = this.fishingRod.reel.water != LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER;
            } else {
                this.isWrongReel = false;
            }

            if(this.fishingRod.pole != null && this.fishingRod.pole.water != Constant.GLOBAL_WATER) {
                this.isWrongPole = this.fishingRod.pole.water != LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER;
            } else {
                this.isWrongPole = false;
            }

            if(this.fishingRod.line != null && this.fishingRod.line.water != Constant.GLOBAL_WATER) {
                this.isWrongLine = this.fishingRod.line.water != LocationInfo.valueOfId(BossBarHandler.instance().currentLocation.ID).WATER;
            } else {
                this.isWrongLine = false;
            }
        }

        List<Integer> entityToRemove = new ArrayList<>();
        baitDisplay.forEach((entity, bait) -> {
            if(minecraftClient.world != null) {
                Entity bobberEntity = minecraftClient.world.getEntityById(entity);
                if(bobberEntity != null) {
                    Entity baitEntity = minecraftClient.world.getEntityById(bait);
                    if (baitEntity != null) baitEntity.setPosition(bobberEntity.getPos().add(0, -0.32, 0));
                } else {
                    entityToRemove.add(entity);
                }
            }
        });
        entityToRemove.forEach(id -> {
            if (minecraftClient.world != null) {
                minecraftClient.world.removeEntity(baitDisplay.get(id), Entity.RemovalReason.DISCARDED);
            }
            baitDisplay.remove(id);
        });
    }

    public void tickEntities(Entity entity, MinecraftClient minecraftClient) {
        if(entity instanceof FishingBobberEntity fishingBobberEntity) {
            PlayerEntity player = fishingBobberEntity.getPlayerOwner();
            if(minecraftClient.player != null && player != null && Objects.equals(minecraftClient.player.getUuid(), player.getUuid())) {
                this.isBobberOut = true;
                List<Text> textList = new ArrayList<>();
                int remaining = ((InGameHudAccessor) minecraftClient.inGameHud).getOverlayRemaining();
                InGameHudAccessor inGameHudAccessor = ((InGameHudAccessor) minecraftClient.inGameHud);

                // Add Text
                if(config.bobberTracker.skyLightWarning
                        && minecraftClient.world != null
                        && (inGameHudAccessor.getTitle() == null || !Objects.equals(inGameHudAccessor.getTitle().getString(), "BITE!"))
                        && !fishingBobberEntity.getWorld().isSkyVisible(fishingBobberEntity.getBlockPos().up())
                        && minecraftClient.world.getBlockState(fishingBobberEntity.getBlockPos().up()).getBlock() != Blocks.WATER
                        && remaining <= 0
                ) this.addText(textList, Text.literal("ʙᴏʙʙᴇʀ ᴜɴᴅᴇʀ ᴀ ʙʟᴏᴄᴋ").formatted(Formatting.RED));

                if (config.bobberTracker.showWaitingTime) {
                    float seconds = fishingBobberEntity.age / 20f;
                    this.addText(textList, TextHelper.concat(Text.literal(String.format("%.1f", seconds)).formatted(Formatting.WHITE, Formatting.BOLD), Text.literal(" sec.").formatted(Formatting.GRAY)));
                }

                if((config.fun.immersionMode || config.fun.biteBobber)
                        && inGameHudAccessor.getTitle() != null
                        && Objects.equals(inGameHudAccessor.getTitle().getString(), "BITE!")
                        && inGameHudAccessor.getTitleRemainTicks() > 0
                ) {
                    this.addText(textList, inGameHudAccessor.getTitle());
                }

                // Render Text
                if (config.fun.minigameOnBobber && remaining > 0) {
                    Text message = ((InGameHudAccessor) minecraftClient.inGameHud).getOverlayMessage();

                    fishingBobberEntity.setCustomName(message);
                    fishingBobberEntity.setCustomNameVisible(true);
                } else if(!textList.isEmpty()) {
                    Text concatText = TextHelper.concat(textList.toArray(new Text[0]));
                    fishingBobberEntity.setCustomName(concatText);
                    fishingBobberEntity.setCustomNameVisible(true);
                } else {
                    fishingBobberEntity.setCustomNameVisible(false);
                }

                if(config.fun.lightOnBobber) {
                    this.spawnLight(minecraftClient, fishingBobberEntity);
                }
            }

            // Bait Display
            if(config.bobberTracker.showBait && player != null && !baitDisplay.containsKey(entity.getId())) {
                ItemStack itemStack = player.getMainHandStack();
                FishingRod rod = FishingRod.getFishingRod(itemStack);
                if(rod != null && !rod.tacklebox.isEmpty()) {
                    if (minecraftClient.world == null || minecraftClient.player == null) return;
                    DisplayEntity.ItemDisplayEntity itemDisplayEntity = Objects.requireNonNull(EntityType.ITEM_DISPLAY.create(minecraftClient.world, SpawnReason.TRIGGERED));
                    minecraftClient.world.addEntity(itemDisplayEntity);

                    baitDisplay.put(entity.getId(), itemDisplayEntity.getId());

                    ItemStack baitStack = Items.COOKED_COD.getDefaultStack().copy();
                    baitStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, rod.tacklebox.getFirst() instanceof Bait bait ?
                            bait.customModelData : rod.tacklebox.getFirst() instanceof Lure lure ? lure.customModelData : CustomModelDataComponent.DEFAULT);

                    itemDisplayEntity.setItemStack(baitStack);
                    itemDisplayEntity.setPosition(entity.getPos().add(0, -0.32, 0));
                    itemDisplayEntity.setBillboardMode(DisplayEntity.BillboardMode.VERTICAL);
                    itemDisplayEntity.setTransformationMode(ModelTransformationMode.GROUND);
                    itemDisplayEntity.setTransformation(new AffineTransformation(null, null, new Vector3f(0.75f, 0.75f, 0.75f), null));
                }
            }
        }
    }

    public void beforeTickEntitiess() {
        isBobberOut = false;
    }

    public void afterTickEntities(MinecraftClient minecraftClient) {
        if(!isBobberOut) {
            if(minecraftClient.world != null && !Objects.equals(this.previousPos, BlockPos.ofFloored(0, 0, 0))) {
                minecraftClient.world.setBlockState(this.previousPos, this.previousBlockState);
                this.previousPos = BlockPos.ofFloored(0, 0, 0);
                this.previousBlockState = Blocks.AIR.getDefaultState();
            }
        }
    }

    private void spawnLight(MinecraftClient minecraftClient, FishingBobberEntity fishingBobberEntity) {
        if(fishingBobberEntity != null && minecraftClient.world != null && !Objects.equals(this.previousPos, BlockPos.ofFloored(fishingBobberEntity.getPos().add(0, .40, 0)))) {
            minecraftClient.world.setBlockState(this.previousPos, this.previousBlockState);
            this.previousBlockState = minecraftClient.world.getBlockState(BlockPos.ofFloored(fishingBobberEntity.getPos().add(0, .40, 0)));
            this.previousPos = BlockPos.ofFloored(fishingBobberEntity.getPos().add(0, .40, 0));
            if(minecraftClient.world.getBlockState(this.previousPos).getBlock() == Blocks.WATER) {
                BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
                IntProperty LEVEL_15 = Properties.LEVEL_15;
                minecraftClient.world.setBlockState(this.previousPos, Blocks.LIGHT.getDefaultState().with(WATERLOGGED, Boolean.TRUE).with(LEVEL_15, config.fun.lightLevel));
            } else {
                IntProperty LEVEL_15 = Properties.LEVEL_15;
                minecraftClient.world.setBlockState(this.previousPos, Blocks.LIGHT.getDefaultState().with(LEVEL_15, config.fun.lightLevel));
            }
        }
    }

    private void addText(List<Text> textList, Text text) {
        if(textList.isEmpty()) {
            textList.add(text);
        } else {
            textList.add(TextHelper.concat(Text.literal(" | ").formatted(Formatting.WHITE), text));
        }
    }
}
