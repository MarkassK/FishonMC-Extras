package io.github.markassk.fishonmcextras.handler.packet;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ContestHandler;
import me.enderkill98.proxlib.ProxPacketIdentifier;
import me.enderkill98.proxlib.client.ProxLib;
import net.minecraft.client.MinecraftClient;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ContestPBPacket {
    private static final ProxPacketIdentifier CONTEST_PB_NOTIFICATION_ID = ProxPacketIdentifier.of(PacketHandler.VENDOR_ID, PacketHandler.PacketID.CONTEST_PB_NOTIFICATION_ID.ID);

    protected static void addHandler() {
        receiveContestPBPacket();
    }

    protected ContestPBPacket() {}

    public void sendContestPBPacket(String fishGroupId, String userName) {
        if(MinecraftClient.getInstance().player != null) {
            try {
                // Send notification with fish group ID and user name
                String packetData = "contest_pb:" + fishGroupId + ":" + userName;
                PacketHandler.instance().sendPacket(CONTEST_PB_NOTIFICATION_ID, packetData);
                FishOnMCExtras.LOGGER.info("[FoE] Sent contest PB notification packet with groupId: {} and user: {}", fishGroupId, userName);
            } catch (IOException e) {
                FishOnMCExtras.LOGGER.error("[FoE] Failed to send contest PB packet", e);
            }
        }
    }

    private static void receiveContestPBPacket() {
        ProxLib.addHandlerFor(CONTEST_PB_NOTIFICATION_ID, (sender, identifier, data) -> {
            if(!FishOnMCExtrasConfig.getConfig().contestTracker.recieveLocalPBs) return;
            try {
                DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
                PacketHandler.PacketType type = PacketHandler.getPacketType(dataIn.readUnsignedByte());
                if(type == PacketHandler.PacketType.STRING) {
                    String message = dataIn.readUTF();
                    if(message.startsWith("contest_pb:")) {
                        // Parse the packet data: contest_pb:groupId:userName
                        String[] parts = message.split(":", 3);
                        if(parts.length >= 3) {
                            String fishGroupId = parts[1];
                            String userName = parts[2];
                            
                            FishOnMCExtras.LOGGER.info("[FoE] Received contest PB notification from {} for fish group: {}", userName, fishGroupId);
                            
                            // Check if the fish group matches the current contest type
                            ContestHandler contestHandler = ContestHandler.instance();
                            if(contestHandler.isContest) {
                                String contestType = contestHandler.type.replace("Heaviest", "").trim().toLowerCase();
                                if(contestType.contains(fishGroupId.toLowerCase())) {
                                    // Refresh contest stats when receiving PB notification for matching fish type
                                    if(MinecraftClient.getInstance().player != null) {
                                        ContestHandler.instance().setRefreshReason("other_player_pb:" + userName);
                                        MinecraftClient.getInstance().player.networkHandler.sendChatCommand("contest");
                                        FishOnMCExtras.LOGGER.info("[FoE] Refreshed contest stats due to PB notification from {} for matching fish group: {}", userName, fishGroupId);
                                    }
                                } else {
                                    FishOnMCExtras.LOGGER.info("[FoE] Contest PB notification from {} for fish group {} does not match current contest type: {}", userName, fishGroupId, contestType);
                                }
                            }
                        } else {
                            FishOnMCExtras.LOGGER.warn("[FoE] Received malformed contest PB packet: {}", message);
                        }
                    }
                } else {
                    PacketHandler.instance().sendWrongTypeWarn(type);
                }
            } catch (IOException e) {
                FishOnMCExtras.LOGGER.error("[FoE] Failed to process contest PB packet", e);
            }
        });
    }
}
