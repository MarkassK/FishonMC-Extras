package io.github.markassk.fishonmcextras.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class CommandRegistry {
    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerCommands(dispatcher));
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(command("foe")
                .then(command("config").executes(Command::config)
                .then(command("resetsession").executes(Command::resetSession))
                .then(command("resetdrystreak").executes(Command::resetDryStreak))
                .then(command("reload").executes(Command::reload)))
                .then(command("nocrew").executes(Command::noCrew))
                .then(command("resettimer").executes(Command::resetTimer))
                .then(command("cancelimport").executes(Command::cancelImport))
                .then(command("highlightuser").then(argument("username", PlayerArgumentType.getPlayerArgumentType()).executes(Command::highlightUser)))
                .then(command("stophighlight").executes(Command::stopHighlight))
                .then(command("immersionmode").executes(Command::immersionMode))
                .then(command("armorvisibility").executes(Command::armorVisibility))
        );
    }

    private static class Command {
        private static int config(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(() -> {
                MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, MinecraftClient.getInstance().currentScreen).get());
            });
        }

        private static int resetSession(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Session Fish Tracker reset", () -> ProfileDataHandler.instance().resetStats());
        }

        private static int resetDryStreak(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Dry-streak reset", () -> ProfileDataHandler.instance().resetDryStreak());
        }

        private static int reload(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Config/Stats Reloaded", () -> {
                ProfileDataHandler.instance().loadStats();
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).load();
            });
        }

        private static int noCrew(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Set to No Crew", () -> CrewHandler.instance().setNoCrew());
        }

        private static int resetTimer(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Fish Timer reset", () -> ProfileDataHandler.instance().resetTimer());
        }

        private static int cancelImport(CommandContext<FabricClientCommandSource> context) {
            if(!ProfileDataHandler.instance().profileData.isStatsInitialized) {
                ProfileDataHandler.instance().profileData.isStatsInitialized = true;
                return sendFeedback(context, Text.literal("Removed import notification"));
            }
            return 1;
        }

        private static int highlightUser(CommandContext<FabricClientCommandSource> context) {
            PlayerListEntry playerListEntry = PlayerArgumentType.getPlayer(context, "username");
            if (playerListEntry != null && playerListEntry.getDisplayName() != null) {
                return executeCommand(context, List.of(
                        Text.literal("Highlighted "),
                        playerListEntry.getDisplayName(),
                        Text.literal(" for 5 minutes")
                ), () -> {
                    OtherPlayerHandler.instance().highlightedPlayer = playerListEntry;
                    OtherPlayerHandler.instance().highlightStartTime = System.currentTimeMillis();
                });
            }
            return sendFeedback(context, Text.literal("Could not find Player"));
        }

        private static int stopHighlight(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Stopping Highlight", () -> {
                OtherPlayerHandler.instance().isHighlighted = false;
                OtherPlayerHandler.instance().highlightedPlayer = null;
            });
        }

        private static int immersionMode(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Immersion Mode Toggled", () -> {
                FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
                config.fun.immersionMode = !config.fun.immersionMode;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
            });
        }

        private static int armorVisibility(CommandContext<FabricClientCommandSource> context) {
            return executeCommand(context, "Armor Visibility Toggled", () -> {
                FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
                config.fun.hideArmor = !config.fun.hideArmor;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
            });
        }
    }


    //region Command Builder
    private static LiteralArgumentBuilder<FabricClientCommandSource> command(String command) {
        return ClientCommandManager.literal(command);
    }

    private static int executeCommand(CommandContext<FabricClientCommandSource> context, List<Text> feedback, ExecuteCallback executeCallback) {
        return executeCommand(context, TextHelper.concat(feedback.toArray(new Text[]{})), executeCallback);
    }

    private static int executeCommand(CommandContext<FabricClientCommandSource> context, String feedback, ExecuteCallback executeCallback) {
        return executeCommand(context, Text.literal(feedback), executeCallback);
    }

    private static int executeCommand(ExecuteCallback executeCallback) {
        executeCallback.execute();
        return 1;
    }

    private static int executeCommand(CommandContext<FabricClientCommandSource> context, Text feedback, ExecuteCallback executeCallback) {
        executeCallback.execute();
        return sendFeedback(context, feedback);
    }

    private static int sendFeedback(CommandContext<FabricClientCommandSource> context, Text feedback) {
        context.getSource().sendFeedback(
                TextHelper.concat(
                        Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                        Text.literal("» ").formatted(Formatting.DARK_GRAY),
                        feedback
                )

        );
        return 1;
    }

    private interface ExecuteCallback {
        void execute();
    }
    //endregion
}
