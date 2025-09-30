package io.github.markassk.fishonmcextras.handler.packet;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import me.enderkill98.proxlib.ProxPacketIdentifier;
import me.enderkill98.proxlib.client.ProxLib;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketHandler {
    private static PacketHandler INSTANCE = new PacketHandler();

    public static final TypingPacket TYPING_PACKET = new TypingPacket();
    public static final ContestPBPacket CONTEST_PB_PACKET = new ContestPBPacket();

    public static final int VENDOR_ID = 219;

    public static PacketHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PacketHandler();
        }
        return INSTANCE;
    }

    public void init() {
        this.addHandlers();
    }

    public void addHandlers() {
        TypingPacket.addHandler();
        ContestPBPacket.addHandler();
    }

    //region Packet Sender
    protected void sendPacket(ProxPacketIdentifier packet, String s) throws IOException {
        sendPacket(packet, s, PacketType.STRING);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Byte b) throws IOException {
        sendPacket(packet, b.toString(), PacketType.BYTE);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Short s) throws IOException {
        sendPacket(packet, s.toString(), PacketType.SHORT);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Integer i) throws IOException {
        sendPacket(packet, i.toString(), PacketType.INTEGER);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Long l) throws IOException {
        sendPacket(packet, l.toString(), PacketType.LONG);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Float f) throws IOException {
        sendPacket(packet, f.toString(), PacketType.FLOAT);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Double f) throws IOException {
        sendPacket(packet, f.toString(), PacketType.DOUBLE);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Boolean b) throws IOException {
        sendPacket(packet, b.toString(), PacketType.BOOLEAN);
    }

    protected void sendPacket(ProxPacketIdentifier packet, Character b) throws IOException {
        sendPacket(packet, b.toString(), PacketType.CHARACTER);
    }

    private void sendPacket(ProxPacketIdentifier packet, String d, PacketType type) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = getDataOutputStream(type, bytesOut);
        dataOut.writeUTF(d);

        PacketID packetID = PacketID.valueOfId(packet.packetId());
        if(packetID != null) {
            int packets = ProxLib.sendPacket(MinecraftClient.getInstance(), packet, bytesOut.toByteArray());
            FishOnMCExtras.LOGGER.info("[FoE] Sent packet:{} using {} packets!", packetID.name(), packets);
        }
    }

    private @NotNull DataOutputStream getDataOutputStream(PacketType type, ByteArrayOutputStream bytesOut) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(bytesOut);
        switch (type) {
            case STRING -> dataOut.writeByte(0);
            case BYTE -> dataOut.writeByte(1);
            case SHORT -> dataOut.writeByte(2);
            case INTEGER -> dataOut.writeByte(3);
            case LONG -> dataOut.writeByte(4);
            case FLOAT -> dataOut.writeByte(5);
            case DOUBLE -> dataOut.writeByte(6);
            case BOOLEAN -> dataOut.writeByte(7);
            case CHARACTER -> dataOut.writeByte(8);
        }
        return dataOut;
    }

    protected static PacketType getPacketType(int byteType) {
        return PacketType.values()[byteType];
    }

    protected enum PacketType {
        STRING,
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN,
        CHARACTER
    }
    //endregion

    protected void sendWrongTypeWarn(PacketHandler.PacketType type) {
        FishOnMCExtras.LOGGER.warn("Received unsupported type: {}", type);
    }

    public enum PacketID {
        START_TYPING_PACKED_ID(0),
        STOP_TYPING_PACKED_ID(1),
        CONTEST_PB_NOTIFICATION_ID(2);

        final int ID;
        PacketID(int id) {
            this.ID = id;
        }

        public static PacketID valueOfId(int id) {
            for (PacketID c : values()) {
                if (c.ID == id) {
                    return c;
                }
            }
            return null;
        }
    }
}
