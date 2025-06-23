package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.mixin.PlayerListHudAccessor;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class TabHandler {
    private static TabHandler INSTANCE = new TabHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public Text player = Text.empty();
    public Constant rank = Constant.DEFAULT;
    public String instance = "";
    public boolean isInstance = false;

    private Collection<PlayerListEntry> playerListEntries = new ArrayList<>();

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


                if (config.crewTracker.notifyCrewOnJoin
                        && Objects.requireNonNull(minecraftClient.getNetworkHandler()).getListedPlayerListEntries().size() > playerListEntries.size()
                        && !playerListEntries.isEmpty()) {
                    List<PlayerListEntry> differences = new ArrayList<>(Objects.requireNonNull(minecraftClient.getNetworkHandler()).getListedPlayerListEntries());
                    differences.removeAll(playerListEntries);

                    if(differences.size() == 1) {
                        differences.forEach(playerListEntry -> {
                            if(ProfileDataHandler.instance().profileData.crewMembers.contains(playerListEntry.getProfile().getId())) {
                                Text displayName = playerListEntry.getDisplayName();
                                if(displayName != null && Objects.equals(playerListEntry.getProfile().getId(), UUID.fromString("b5a9bbb7-42b4-4a6a-9ebe-bdf6697c8ee0"))) {
                                    if(config.fun.isFoeTagPrefix) {
                                        displayName = Text.literal("\uE00B ").formatted(Formatting.WHITE).append(Text.literal(playerListEntry.getProfile().getName()).withColor(0x00AF0E));
                                    } else {
                                        displayName = displayName.copy().append(Text.literal(" \uE00B").formatted(Formatting.WHITE));
                                    }
                                }
                                minecraftClient.inGameHud.getChatHud().addMessage(TextHelper.concat(
                                        Text.literal("CREWS ").withColor(0x70aa6e).formatted(Formatting.BOLD),
                                        Text.literal("» ").withColor(0x545454),
                                        Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                        Text.literal("| ").formatted(Formatting.DARK_GRAY),
                                        displayName,
                                        Text.literal(" joined").formatted(Formatting.GREEN),
                                        Text.literal(" the server").withColor(0xa8a8a8)
                                ));
                            }
                        });
                    }
                } else if (config.crewTracker.notifyCrewOnLeave
                        && Objects.requireNonNull(minecraftClient.getNetworkHandler()).getListedPlayerListEntries().size() < playerListEntries.size()
                ) {
                    List<PlayerListEntry> differences = new ArrayList<>(playerListEntries);
                    differences.removeAll(Objects.requireNonNull(minecraftClient.getNetworkHandler()).getListedPlayerListEntries());

                    if(differences.size() == 1) {
                        differences.forEach(playerListEntry -> {
                            if(ProfileDataHandler.instance().profileData.crewMembers.contains(playerListEntry.getProfile().getId())) {
                                Text displayName = playerListEntry.getDisplayName();
                                if(displayName != null && Objects.equals(playerListEntry.getProfile().getId(), UUID.fromString("b5a9bbb7-42b4-4a6a-9ebe-bdf6697c8ee0"))) {
                                    if(config.fun.isFoeTagPrefix) {
                                        displayName = Text.literal("\uE00B ").formatted(Formatting.WHITE).append(Text.literal(playerListEntry.getProfile().getName()).withColor(0x00AF0E));
                                    } else {
                                        displayName = displayName.copy().append(Text.literal(" \uE00B").formatted(Formatting.WHITE));
                                    }
                                }
                                minecraftClient.inGameHud.getChatHud().addMessage(TextHelper.concat(
                                        Text.literal("CREWS ").withColor(0x70aa6e).formatted(Formatting.BOLD),
                                        Text.literal("» ").withColor(0x545454),
                                        Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                        Text.literal("| ").formatted(Formatting.DARK_GRAY),
                                        displayName,
                                        Text.literal(" left").formatted(Formatting.RED),
                                        Text.literal(" the server").withColor(0xa8a8a8)
                                ));
                            }
                        });
                    }
                }

                // CREWS » Crew Chat has been enabled (/crew chat)
                if(Objects.requireNonNull(minecraftClient.getNetworkHandler()).getListedPlayerListEntries().size() != playerListEntries.size()) {
                    playerListEntries = new ArrayList<>(Objects.requireNonNull(minecraftClient.getNetworkHandler()).getListedPlayerListEntries());
                }
            } catch (Exception e) {
                FishOnMCExtras.LOGGER.error("TabHandler: {}", e.getMessage());
            }
        }
    }

    public Text getPlayer(UUID uuid) {
        PlayerListHud playerListHud = MinecraftClient.getInstance().inGameHud.getPlayerListHud();
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            PlayerListEntry playerListEntry = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(uuid);
            return playerListEntry != null ? playerListHud.getPlayerName(playerListEntry) : null;


        }
        return null;
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
