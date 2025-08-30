package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerStatusHandler {
    private static PlayerStatusHandler INSTANCE = new PlayerStatusHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    // Typing Indicator
    public Map<UUID, DisplayEntity.TextDisplayEntity> typingPlayers = new HashMap<>();

    // Typed Text
    private Map<UUID, TextBubble> textBubbles = new HashMap<>();

    public static PlayerStatusHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerStatusHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(minecraftClient.world != null) {
            AtomicReference<UUID> notPresent = new AtomicReference<>();
            typingPlayers.forEach((uuid, textDisplayEntity) -> {
                Optional<AbstractClientPlayerEntity> player = minecraftClient.world.getPlayers().stream().filter(abstractClientPlayerEntity -> abstractClientPlayerEntity.getUuid().equals(uuid)).findFirst();
                if(player.isPresent()) {
                    if(typingPlayers.get(uuid) == null) {
                        typingPlayers.put(uuid, spawnTypeStatusDisplay(minecraftClient, player.get().getPos()));
                    }
                } else {
                    if(textDisplayEntity != null) {
                        minecraftClient.world.removeEntity(textDisplayEntity.getId(), Entity.RemovalReason.DISCARDED);
                    }
                    notPresent.set(uuid);
                }
            });
            if(notPresent.get() != null) {
                typingPlayers.remove(notPresent.get());
            }


            AtomicReference<UUID> removeBubble = new AtomicReference<>();
            textBubbles.forEach((uuid, textBubble) -> {
                Optional<AbstractClientPlayerEntity> player = minecraftClient.world.getPlayers().stream().filter(abstractClientPlayerEntity -> abstractClientPlayerEntity.getUuid().equals(uuid)).findFirst();
                if(player.isPresent() && System.currentTimeMillis() - textBubble.spawnTime < 10000L) {
                    textBubble.bubble.setPosition(player.get().getPos().add(0, player.get().isSneaking() ? 2.8 : player.get().isInSwimmingPose() ? 1.9 : 3.1, 0));
                } else {
                    minecraftClient.world.removeEntity(textBubbles.get(uuid).bubble.getId(), Entity.RemovalReason.DISCARDED);
                    removeBubble.set(uuid);
                }
            });
            textBubbles.remove(removeBubble.get());
        }
    }

    public void onReceiveMessage(Text text) {
        if(config.playerStatus.showIsTyping && MinecraftClient.getInstance().world != null && !MinecraftClient.getInstance().options.hudHidden) {
            String textString = text.getString();
            if(textString.startsWith("!")) {
                sendBubble(textString, Text.literal(textString.substring(textString.indexOf("»") + 2)).formatted(Formatting.WHITE));
            } else if (textString.startsWith("[CREW CHAT] ")) {
                sendBubble(textString, Text.literal(textString.substring(textString.indexOf("»") + 2)).withColor(0xbcf4bc));
            }
        }
    }


    private void sendBubble(String textString, MutableText message) {
        String name = textString.substring(0, textString.indexOf("»"));
        Optional<AbstractClientPlayerEntity> player = MinecraftClient.getInstance().world.getPlayers().stream().filter(abstractClientPlayerEntity -> !abstractClientPlayerEntity.getGameProfile().getName().isBlank() && name.contains(abstractClientPlayerEntity.getGameProfile().getName())).findFirst();
        player.ifPresent(abstractClientPlayerEntity -> {
            if(textBubbles.containsKey(abstractClientPlayerEntity.getUuid())) {
                textBubbles.get(abstractClientPlayerEntity.getUuid()).spawnTime = System.currentTimeMillis();
                textBubbles.get(abstractClientPlayerEntity.getUuid()).bubble.setText(message.append(Text.literal("\n⏷").formatted(Formatting.WHITE)));
            } else {
                textBubbles.put(abstractClientPlayerEntity.getUuid(), new TextBubble(System.currentTimeMillis(), spawnMessageStatusDisplay(MinecraftClient.getInstance(), player.get().getPos(), message.append(Text.literal("\n⏷").formatted(Formatting.WHITE)))));
            }
        });
    }

    private DisplayEntity.TextDisplayEntity spawnTypeStatusDisplay(MinecraftClient minecraftClient, Vec3d position) {
        DisplayEntity.TextDisplayEntity textDisplayEntity = Objects.requireNonNull(EntityType.TEXT_DISPLAY.create(minecraftClient.world, SpawnReason.TRIGGERED));
        Objects.requireNonNull(minecraftClient.world).addEntity(textDisplayEntity);

        textDisplayEntity.setPosition(position.add(0, 2.9, 0));
        textDisplayEntity.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
        textDisplayEntity.setBackground(0x00000000);

        textDisplayEntity.setText(TextHelper.concat(Text.literal("⌛ ").formatted(Formatting.YELLOW), Text.literal("ᴛʏᴘɪɴɢ...").formatted(Formatting.BOLD)));
        return textDisplayEntity;
    }

    private DisplayEntity.TextDisplayEntity spawnMessageStatusDisplay(MinecraftClient minecraftClient, Vec3d position, Text text) {
        DisplayEntity.TextDisplayEntity textDisplayEntity = Objects.requireNonNull(EntityType.TEXT_DISPLAY.create(minecraftClient.world, SpawnReason.TRIGGERED));
        Objects.requireNonNull(minecraftClient.world).addEntity(textDisplayEntity);

        textDisplayEntity.setPosition(position.add(0, 3.1, 0));
        textDisplayEntity.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
        textDisplayEntity.setBackground(0x00000000);
        textDisplayEntity.setDisplayFlags((byte) 1);

        textDisplayEntity.setText(text);
        return textDisplayEntity;
    }

    private static class TextBubble {
        long spawnTime;
        DisplayEntity.TextDisplayEntity bubble;

        TextBubble(long spawnTime, DisplayEntity.TextDisplayEntity bubble) {
            this.spawnTime = spawnTime;
            this.bubble = bubble;
        }
    }
}
