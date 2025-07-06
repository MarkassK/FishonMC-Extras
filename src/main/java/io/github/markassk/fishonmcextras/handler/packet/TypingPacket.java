package io.github.markassk.fishonmcextras.handler.packet;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.PlayerStatusHandler;
import me.enderkill98.proxlib.ProxPacketIdentifier;
import me.enderkill98.proxlib.client.ProxLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class TypingPacket {
    private static final ProxPacketIdentifier START_TYPING_PACKED_ID = ProxPacketIdentifier.of(PacketHandler.VENDOR_ID, PacketHandler.PacketID.START_TYPING_PACKED_ID.ID);
    private static final ProxPacketIdentifier STOP_TYPING_PACKED_ID = ProxPacketIdentifier.of(PacketHandler.VENDOR_ID, PacketHandler.PacketID.STOP_TYPING_PACKED_ID.ID);
    private static final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    protected static void addHandler() {
        receiveStartTypingPacket();
        receiveStopTypingPacket();
    }

    protected TypingPacket() {}

    public void sendStartTypingPacket(UUID player) {
        // Send Player ID
        if(MinecraftClient.getInstance().player != null) {
            try {
                PacketHandler.instance().sendPacket(START_TYPING_PACKED_ID, player.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendStopTypingPacket(UUID player) {
        // Send Player ID
        if(MinecraftClient.getInstance().player != null) {
            try {
                PacketHandler.instance().sendPacket(STOP_TYPING_PACKED_ID, player.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void receiveStartTypingPacket() {
        ProxLib.addHandlerFor(START_TYPING_PACKED_ID, (sender, identifier, data) -> {
            try {
                if (config.playerStatus.showIsTyping) {
                    DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
                    PacketHandler.PacketType type = PacketHandler.getPacketType(dataIn.readUnsignedByte());
                    if(type == PacketHandler.PacketType.STRING) {
                        UUID uuid = UUID.fromString(dataIn.readUTF());
                        System.out.println("Packet Start received from " + uuid);

                        if(!PlayerStatusHandler.instance().typingPlayers.containsKey(uuid)) {
                            PlayerStatusHandler.instance().typingPlayers.put(uuid, null);
                        }
                    } else {
                        PacketHandler.instance().sendWrongTypeWarn(type);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void receiveStopTypingPacket() {
        ProxLib.addHandlerFor(STOP_TYPING_PACKED_ID, (sender, identifier, data) -> {
            try {
                if (config.playerStatus.showIsTyping) {
                    DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
                    PacketHandler.PacketType type = PacketHandler.getPacketType(dataIn.readUnsignedByte());
                    if(type == PacketHandler.PacketType.STRING) {
                        UUID uuid = UUID.fromString(dataIn.readUTF());
                        System.out.println("Packet Stop received from " + uuid);

                        if (MinecraftClient.getInstance().world != null) {
                            if (PlayerStatusHandler.instance().typingPlayers.get(uuid) != null) {
                                MinecraftClient.getInstance().world.removeEntity(PlayerStatusHandler.instance().typingPlayers.get(uuid).getId(), Entity.RemovalReason.DISCARDED);
                            }
                        }
                        PlayerStatusHandler.instance().typingPlayers.remove(uuid);
                    } else {
                        PacketHandler.instance().sendWrongTypeWarn(type);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
