package io.github.markassk.fishonmcextras;

import io.github.markassk.fishonmcextras.commands.CommandRegistry;
import io.github.markassk.fishonmcextras.handler.*;
import io.github.markassk.fishonmcextras.screens.hud.FishTrackerHud;
import io.github.markassk.fishonmcextras.screens.hud.MainHudRenderer;
import io.github.markassk.fishonmcextras.screens.petCalculator.PetCalculatorScreen;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.screens.widget.CustomButtonWidget;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class FishOnMCExtrasClient implements ClientModInitializer {
    public static FishOnMCExtrasConfig CONFIG;

    public static final MainHudRenderer MAIN_HUD_RENDERER = new MainHudRenderer();

    @Override
    public void onInitializeClient() {
        // Setup config screen, reads correct data to load IMPORTANT MUST BE FIRST
        AutoConfig.register(FishOnMCExtrasConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).getConfig();
        CommandRegistry.initialize();
        KeybindHandler.instance().init();

        ClientPlayConnectionEvents.JOIN.register(this::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onLeave);
        ClientTickEvents.END_CLIENT_TICK.register(this::onEndClientTick);
        ClientReceiveMessageEvents.GAME.register(this::receiveGameMessage);
        ClientReceiveMessageEvents.MODIFY_GAME.register(this::modifyGameMessage);
        ItemTooltipCallback.EVENT.register(this::onItemTooltipCallback);
        ScreenEvents.AFTER_INIT.register(this::afterScreenInit);

        HudRenderCallback.EVENT.register(MAIN_HUD_RENDERER);
    }

    private void onEndClientTick(MinecraftClient minecraftClient) {
        if(minecraftClient.getCurrentServerEntry() != null ) {
            if(LoadingHandler.instance().checkAddress(minecraftClient) && LoadingHandler.instance().isOnServer) {
                LoadingHandler.instance().tick(minecraftClient);
                if(LoadingHandler.instance().isLoadingDone) {
                    FishCatchHandler.instance().tick(minecraftClient);
                    PetEquipHandler.instance().tick(minecraftClient);
                    FullInventoryHandler.instance().tick(minecraftClient);
                    NotificationSoundHandler.instance().tick(minecraftClient);
                    RayTracingHandler.instance().tick(minecraftClient);
                    LookTickHandler.instance().tick();
                    LocationHandler.instance().tick(minecraftClient);
                    ScoreboardHandler.instance().tick(minecraftClient);
                    ContestHandler.instance().tick();
                    TabHandler.instance().tick(minecraftClient);
                    BossBarHandler.instance().tick(minecraftClient);
                    QuestHandler.instance().tick(minecraftClient);
                    ArmorHandler.instance().tick(minecraftClient);
                    FishingRodHandler.instance().tick(minecraftClient);
                    CrewHandler.instance().tick(minecraftClient);
                    StatsImportHandler.instance().tick(minecraftClient);
                    DiscordHandler.instance().tick();
                    KeybindHandler.instance().tick(minecraftClient);
                    InventoryButtonHandler.instance().tick(minecraftClient);
                    ThemingHandler.instance().tick();
                }
             }
        }
    }

    private void onJoin(ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender packetSender, MinecraftClient minecraftClient) {
        LoadingHandler.instance().init();
        PetEquipHandler.instance().init();
        NotificationSoundHandler.instance().init();
        DiscordHandler.instance().init();

        if(minecraftClient.getCurrentServerEntry() != null ) {
            if(LoadingHandler.instance().checkAddress(minecraftClient)) {
                FishOnMCExtras.LOGGER.info("[FoE] On server. (play.fishonmc.net)");
                FishOnMCExtras.LOGGER.info("[FoE] Loading Start");
                minecraftClient.execute(() -> {
                    if (minecraftClient.player != null) {
                        ProfileDataHandler.instance().onJoinServer(minecraftClient.player);
                        FishCatchHandler.instance().onJoinServer();
                        DiscordHandler.instance().connect();
                        LoadingHandler.instance().isOnServer = true;
                    }
                });
            } else {
                FishOnMCExtras.LOGGER.info("[FoE] Not on server. (play.fishonmc.net)");
                LoadingHandler.instance().isOnServer = false;
                DiscordHandler.instance().disconnect();
            }
        }
    }

    private void receiveGameMessage(Text text, boolean b) {
        if(LoadingHandler.instance().isOnServer) {
            PetEquipHandler.instance().onReceiveMessage(text);
            ContestHandler.instance().onReceiveMessage(text);
            CrewHandler.instance().onReceiveMessage(text);
            FishCatchHandler.instance().onReceiveMessage(text);
            StaffHandler.instance().onReceiveMessage(text);
        }
    }

    private Text modifyGameMessage(Text text, boolean b) {
        if(LoadingHandler.instance().isOnServer) {
            return PetTooltipHandler.instance().appendTooltip(text);
        }
        return text;
    }

    private void onLeave(ClientPlayNetworkHandler clientPlayNetworkHandler, MinecraftClient minecraftClient) {
        LoadingHandler.instance().init();
        FishCatchHandler.instance().onLeaveServer();
        ContestHandler.instance().onLeaveServer();
        LoadingHandler.instance().isOnServer = false;

    }

    private void onItemTooltipCallback(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> textList) {
        if(LoadingHandler.instance().isOnServer) {
            PetTooltipHandler.instance().appendTooltip(textList, itemStack);
            ArmorHandler.instance().appendTooltip(textList, itemStack);
            FishingStatsHandler.instance().appendTooltip(textList, itemStack);
        }
    }

    private void afterScreenInit(MinecraftClient minecraftClient, Screen screen, int scaledWidth, int scaledHeight) {
        if(LoadingHandler.instance().isOnServer) {
            if(Objects.equals(screen.getTitle().getString(), "Pet Menu\uEEE6\uEEE5\uEEE3핑")) {
//                 Pet Menu핑
                Screens.getButtons(screen).add(CustomButtonWidget.builder(Text.literal("Pet Merge Calculator"), button -> {
                            minecraftClient.setScreen(new PetCalculatorScreen(minecraftClient.player, minecraftClient.currentScreen));
                        })
                        .position(scaledWidth / 2 + 100, scaledHeight / 2 - 100)
                        .tooltip(Tooltip.of(Text.literal("Open up the screen to calculate pet merging.")))
                        .itemIcon(Items.TURTLE_EGG.getDefaultStack())
                        .build());
            } else if (Objects.equals(screen.getTitle().getString(), "\uEEE4픹")) {
                // Quest Menu : 픹
                QuestHandler.instance().questMenuState = true;
            } else if(Objects.equals(screen.getTitle().getString() , "\uEEE4핒")) {
                // Crew Menu: 핒
                CrewHandler.instance().crewMenuState = true;
            } else if (Objects.equals(screen.getTitle().getString() , "\uEEE4픲")) {
                // Stats Menu: 픲
                StatsImportHandler.instance().screenInit = true;
                StatsImportHandler.instance().isOnScreen = true;
            } else if (screen instanceof InventoryScreen) {
                InventoryButtonHandler.instance().screenInit = true;
            }
        }
        ScreenEvents.remove(screen).register(this::onRemoveScreen);
    }

    private void onRemoveScreen(Screen screen) {
        if(LoadingHandler.instance().isOnServer) {
            if (Objects.equals(screen.getTitle().getString(), "\uEEE4픹")) {
                // Quest Menu : 픹
                QuestHandler.instance().questMenuState = false;
                QuestHandler.instance().onScreenClose();
            } else if(Objects.equals(screen.getTitle().getString() , "\uEEE4핒")) {
                // Crew Menu:
                CrewHandler.instance().crewMenuState = false;
                CrewHandler.instance().onScreenClose();
            }
        }
    }
}