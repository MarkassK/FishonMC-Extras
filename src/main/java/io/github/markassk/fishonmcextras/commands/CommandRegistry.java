package io.github.markassk.fishonmcextras.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.CrewHandler;
import io.github.markassk.fishonmcextras.handler.ProfileDataHandler;
import io.github.markassk.fishonmcextras.handler.QuestHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                            if(ProfileDataHandler.instance().profileData.crewState == CrewHandler.CrewState.NOTINITIALIZED) {
                                context.getSource().sendFeedback(
                                        TextHelper.concat(
                                                Text.literal("FoE ").formatted(Formatting.DARK_GREEN, Formatting.BOLD),
                                                Text.literal("» ").formatted(Formatting.DARK_GRAY),
                                                Text.literal("Set to No Crew")
                                        )
                                );
                                CrewHandler.instance().setNoCrew();
                            }
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
                )
        );
    }

    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerCommands(dispatcher);
        });
    }
}
