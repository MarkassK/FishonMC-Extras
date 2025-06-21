package io.github.markassk.fishonmcextras.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.markassk.fishonmcextras.commands.argument.PlayerArgumentType;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.CrewHandler;
import io.github.markassk.fishonmcextras.handler.OtherPlayerHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class CommandRegistry {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("foe")
                .then(ClientCommandManager.literal("resetsession")
                        .executes(context -> {
                            ProfileDataHandler.instance().resetStats();
                            context.getSource().sendFeedback(
                                    TextHelper.concat(
                                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                            Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                            Text.literal("Session Fish Tracker reset")
                                    )
                            );
                            return 1;
                        })
                )
                .then(ClientCommandManager.literal("resetdrystreak")
                        .executes(context -> {
                            ProfileDataHandler.instance().resetDryStreak();
                            context.getSource().sendFeedback(
                                    TextHelper.concat(
                                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                            Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                            Text.literal("Dry-streak reset")
                                    )
                            );
                            return 1;
                        })
                )
                .then(ClientCommandManager.literal("reload")
                        .executes(context -> {
                            ProfileDataHandler.instance().loadStats();
                            AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).load();
                            context.getSource().sendFeedback(
                                    TextHelper.concat(
                                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                            Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                            Text.literal("Config/Stats Reloaded")
                                    )
                            );
                            return 1;
                        })
                )
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, MinecraftClient.getInstance().currentScreen).get());
                            return 1;
                        })
                ).then(ClientCommandManager.literal("nocrew")
                        .executes(context -> {
                            context.getSource().sendFeedback(
                                    TextHelper.concat(
                                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                            Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                            Text.literal("Set to No Crew")
                                    )
                            );
                            CrewHandler.instance().setNoCrew();
                            return 1;
                        })
                ).then(ClientCommandManager.literal("resettimer")
                        .executes(context -> {
                            ProfileDataHandler.instance().resetTimer();
                            context.getSource().sendFeedback(
                                    TextHelper.concat(
                                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                            Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                            Text.literal("Fish Timer reset")
                                    )
                            );
                            return 1;
                        })
                ).then(ClientCommandManager.literal("cancelimport")
                        .executes(context -> {
                            if(!ProfileDataHandler.instance().profileData.isStatsInitialized) {
                                ProfileDataHandler.instance().profileData.isStatsInitialized = true;
                                ProfileDataHandler.instance().saveStats();
                                TextHelper.concat(
                                        Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                        Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                        Text.literal("Removed import notification")
                                );

                            }
                            return 1;
                        })
                ).then(ClientCommandManager.literal("highlightuser")
                        .then(argument("username", PlayerArgumentType.getPlayerArgumentType())
                                .executes(context -> {
                                    PlayerListEntry playerListEntry = PlayerArgumentType.getPlayer(context, "username");
                                    if (playerListEntry != null) {
                                        OtherPlayerHandler.instance().highlightedPlayer = playerListEntry;
                                        OtherPlayerHandler.instance().highlightStartTime = System.currentTimeMillis();
                                        context.getSource().sendFeedback(
                                                TextHelper.concat(
                                                        Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                                        Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                                        Text.literal("Highlighted "),
                                                        playerListEntry.getDisplayName(),
                                                        Text.literal(" for 5 minutes")
                                                )
                                        );
                                    } else {
                                        context.getSource().sendError(
                                                TextHelper.concat(
                                                        Text.literal("FoE ").formatted(Formatting.RED, Formatting.BOLD),
                                                        Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                                        Text.literal("Could not find Player")
                                                )
                                        );
                                    }
                                    return 1;
                                })
                        )
                ).then(ClientCommandManager.literal("stophighlight")
                        .executes(context -> {
                            OtherPlayerHandler.instance().isHighlighted = false;
                            OtherPlayerHandler.instance().highlightedPlayer = null;
                            context.getSource().sendFeedback(
                                    TextHelper.concat(
                                            Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                            Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                            Text.literal("Stop Highlight")
                                    )
                            );
                            return 1;
                        })
                )
        );
    }

    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerCommands(dispatcher));
    }
}
