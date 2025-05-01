package io.github.markassk.fishonmcextras.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;

import java.util.Objects;

public class TabHandler {
    private static TabHandler INSTANCE = new TabHandler();

    public Text player = Text.empty();

    public static TabHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new TabHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(LoadingHandler.instance().isLoadingDone) {
            PlayerListHud playerListHud = minecraftClient.inGameHud.getPlayerListHud();
            assert minecraftClient.player != null;
            player = playerListHud.getPlayerName(Objects.requireNonNull(minecraftClient.getNetworkHandler()).getPlayerListEntry(minecraftClient.player.getUuid()));
        }
    }
}
