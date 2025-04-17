package io.github.markassk.fishonmcextras.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.ProfileStatsHandler;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CommandRegistry {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("foe")
                .then(ClientCommandManager.literal("reset")
                        .executes(context -> {
                            ProfileStatsHandler.instance().resetStats();
                            context.getSource().sendFeedback(
                                    Text.literal("Fish Tracker reset locally.")
                            );
                            return 1;
                        })
                )
                .then(ClientCommandManager.literal("reload")
                        .executes(context -> {
                            ProfileStatsHandler.instance().loadStats();
                            context.getSource().sendFeedback(
                                    Text.literal("Fish Tracker stats reloaded from disk.")
                            );
                            AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).load();
                            context.getSource().sendFeedback(
                                    Text.literal("Reloaded FoE config.")
                            );
                            return 1;
                        })
                )
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, MinecraftClient.getInstance().currentScreen).get());
                            return 1;
                        })
                )
        );
    }

    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerCommands(dispatcher);
        });
    }
}
