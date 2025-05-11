package io.github.markassk.fishonmcextras.handler;

import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.*;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.ExtendedRichPresence;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;

public class DiscordHandler {
    private static DiscordHandler INSTANCE = new DiscordHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
    private IPCClient ipcClient;
    private RichPresence currentRichPresence;
    private long offsetTime;

    public static DiscordHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new DiscordHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        RichPresence richPresence = buildPresence();

        if(currentRichPresence != richPresence) {
            currentRichPresence = richPresence;

            this.processState();
        }
    }

    public void init() {
        if(this.ipcClient == null) {
            this.ipcClient = new IPCClient(config.discordIPC.clientId);

            this.offsetTime = System.currentTimeMillis();

            this.ipcClient.setListener(new IPCListener() {
                @Override
                public void onPacketSent(IPCClient ipcClient, Packet packet) {

                }

                @Override
                public void onPacketReceived(IPCClient ipcClient, Packet packet) {

                }

                @Override
                public void onActivityJoin(IPCClient ipcClient, String s) {

                }

                @Override
                public void onActivitySpectate(IPCClient ipcClient, String s) {

                }

                @Override
                public void onActivityJoinRequest(IPCClient ipcClient, String s, User user) {

                }

                @Override
                public void onReady(IPCClient client) {
                    FishOnMCExtras.LOGGER.info("Discord client ready");
                }

                @Override
                public void onClose(IPCClient ipcClient, JsonObject jsonObject) {

                }

                @Override
                public void onDisconnect(IPCClient ipcClient, Throwable throwable) {

                }
            });

            this.currentRichPresence = new ExtendedRichPresence.ExtendedBuilder()
                    .setState("Loading")
                    .build();
        }
    }

    public void connect() {
        if(this.ipcClient.getStatus() != PipeStatus.CONNECTED && config.discordIPC.isEnabled) {
            try {
                this.ipcClient.connect();
            } catch (NoDiscordClientException e) {
                FishOnMCExtras.LOGGER.error("Unable to connect to the discord client", e);
            }
        }
    }

    public void disconnect() {
        ipcClient.close();
    }

    private void processState() {
        if(this.ipcClient == null || this.ipcClient.getStatus() != PipeStatus.CONNECTED) {
            return;
        }


        this.ipcClient.sendRichPresence(currentRichPresence, new Callback((success) -> {}, (error) -> FishOnMCExtras.LOGGER.error("Failed to send state to discord: {}", error)));
    }

    private RichPresence buildPresence() {
        String state = TabHandler.instance().isInstance ? "Fishing at: " + LocationHandler.instance().currentLocation.TAG.getString() + " (i" + TabHandler.instance().instance + ")" : "At: " + LocationHandler.instance().currentLocation.TAG.getString();
        ExtendedRichPresence.ExtendedBuilder presence = new ExtendedRichPresence.ExtendedBuilder()
                .setActivity(ActivityType.Playing)
                .setState(state)
                .setStartTimestamp(offsetTime)
                .setLargeImage("small_logo")
                .setDetails(
                        Objects.requireNonNull(MinecraftClient.getInstance().player).getName().getString()
                                + " [" + ScoreboardHandler.instance().level + "]");
        return presence.build();
    }
}
