package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.mixin.PlayerListHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;

import java.util.Objects;

public class TabHandler {
    private static TabHandler INSTANCE = new TabHandler();

    public Text player = Text.empty();
    public Constant rank = Constant.DEFAULT;
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
                    this.rank = getRank(this.player.getString());
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

    private Constant getRank(String player) {
        if (player.contains(Constant.ANGLER.TAG.getString())) return Constant.ANGLER;
        if (player.contains(Constant.SAILOR.TAG.getString())) return Constant.SAILOR;
        if (player.contains(Constant.MARINER.TAG.getString())) return Constant.MARINER;
        if (player.contains(Constant.CAPTAIN.TAG.getString())) return Constant.CAPTAIN;
        if (player.contains(Constant.ADMIRAL.TAG.getString())) return Constant.ADMIRAL;
        if (player.contains(Constant.STAFF.TAG.getString())) return Constant.STAFF;
        if (player.contains(Constant.DESIGNER.TAG.getString())) return Constant.DESIGNER;
        if (player.contains(Constant.BUILDER.TAG.getString())) return Constant.BUILDER;
        if (player.contains(Constant.MANAGER.TAG.getString())) return Constant.MANAGER;
        if (player.contains(Constant.ADMIN.TAG.getString())) return Constant.ADMIN;
        if (player.contains(Constant.OWNER.TAG.getString())) return Constant.OWNER;
        if (player.contains(Constant.COMMUNITYMANAGER.TAG.getString())) return Constant.COMMUNITYMANAGER;
        return Constant.DEFAULT;
    }
}
