package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.mixin.PlayerListHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;

import java.util.Objects;

public class TabHandler {
    private static TabHandler INSTANCE = new TabHandler();

    public Text player = Text.empty();
    public String instance = "";
    public boolean isInstance = false;

    public static TabHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new TabHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(LoadingHandler.instance().isLoadingDone) {
            try {
                PlayerListHud playerListHud = minecraftClient.inGameHud.getPlayerListHud();
                if (minecraftClient.player != null) {
                    this.player = playerListHud.getPlayerName(Objects.requireNonNull(minecraftClient.getNetworkHandler()).getPlayerListEntry(minecraftClient.player.getUuid()));
                }

                if(((PlayerListHudAccessor) playerListHud).getFooter() != null) {
                    if(((PlayerListHudAccessor) playerListHud).getFooter().getString().contains("ɪɴꜱᴛᴀɴᴄᴇ")) {
                        this.isInstance = true;
                        String footer = ((PlayerListHudAccessor) playerListHud).getFooter().getString();
                        this.instance = footer.substring(footer.indexOf("ɪɴꜱᴛᴀɴᴄᴇ") + 8, footer.lastIndexOf("(")).trim();
                    } else {
                        this.isInstance = false;
                    }
                }
            } catch (Exception e) {
                FishOnMCExtras.LOGGER.error(e.getMessage());
            }
        }
    }
}
