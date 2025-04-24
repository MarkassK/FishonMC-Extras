package io.github.markassk.fishonmcextras;

import io.github.markassk.fishonmcextras.commands.CommandRegistry;
import io.github.markassk.fishonmcextras.handler.*;
import io.github.markassk.fishonmcextras.screens.hud.MainHudRenderer;
import io.github.markassk.fishonmcextras.handler.LookTickHandler;
import io.github.markassk.fishonmcextras.screens.petCalculator.PetCalculatorScreen;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Objects;

public class FishOnMCExtrasClient implements ClientModInitializer {
    public static FishOnMCExtrasConfig CONFIG;
    public static KeyBinding openConfigKeybind;

    public static final MainHudRenderer MAIN_HUD_RENDERER = new MainHudRenderer();

    @Override
    public void onInitializeClient() {
        // Setup config screen, reads correct data to load IMPORTANT MUST BE FIRST
        AutoConfig.register(FishOnMCExtrasConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).getConfig();
        // Setup keybind to open config
        openConfigKeybind = new KeyBinding("key.fishonmcextras.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.fishonmcextras.general");
        KeyBindingHelper.registerKeyBinding(openConfigKeybind);

        //TODO Change screen to new FoE Menu
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.player != null && minecraftClient.currentScreen == null)
                while (openConfigKeybind.wasPressed()){
                    minecraftClient.setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, minecraftClient.currentScreen).get());
                }
        });

        CommandRegistry.initialize();

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
            if(Objects.equals(minecraftClient.getCurrentServerEntry().address, "play.fishonmc.net")) {
                LoadingHandler.instance().tick(minecraftClient);
                FishCatchHandler.instance().tick(minecraftClient);
                PetEquipHandler.instance().tick(minecraftClient);
                FullInventoryHandler.instance().tick(minecraftClient);
                WarningHandler.instance().tick(minecraftClient);
                RayTracingHandler.instance().tick(minecraftClient);
                LookTickHandler.instance().tick();
             }
        }
    }

    private void onJoin(ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender packetSender, MinecraftClient minecraftClient) {
        LoadingHandler.instance().init();
        FishCatchHandler.instance().init();
        PetEquipHandler.instance().init();
        WarningHandler.instance().init();

        if(minecraftClient.getCurrentServerEntry() != null ) {
            if(Objects.equals(minecraftClient.getCurrentServerEntry().address, "play.fishonmc.net")) {
                FishOnMCExtras.LOGGER.info("[FoE] Loading Start");
                minecraftClient.execute(() -> {
                    assert minecraftClient.player != null;
                    FishCatchHandler.instance().onJoinServer(minecraftClient.player);
                });
            }
        }
    }

    private void receiveGameMessage(Text text, boolean b) {
        PetEquipHandler.instance().onReceiveMessage(text);
    }

    private Text modifyGameMessage(Text text, boolean b) {
        return PetTooltipHandler.instance().appendTooltip(text);
    }

    private void onLeave(ClientPlayNetworkHandler clientPlayNetworkHandler, MinecraftClient minecraftClient) {
        FishCatchHandler.instance().onLeaveServer();
    }

    private void onItemTooltipCallback(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> textList) {
        PetTooltipHandler.instance().appendTooltip(textList, itemStack);
    }

    private void afterScreenInit(MinecraftClient minecraftClient, Screen screen, int scaledWidth, int scaledHeight) {
        // Pet Menu핑
        if(Objects.equals(screen.getTitle().getString(), "Pet Menu\uEEE6\uEEE5\uEEE3핑")) {
            Screens.getButtons(screen).add(ButtonWidget.builder(Text.literal("Pet Merge Calculator"), button -> {
                        assert minecraftClient.player != null;
                        minecraftClient.setScreen(new PetCalculatorScreen(minecraftClient.player, minecraftClient.currentScreen));
                    })
                    .dimensions(scaledWidth / 2 - (130 / 2), scaledHeight / 2 + 120, 130, 20)
                    .tooltip(Tooltip.of(Text.literal("Open up the screen to calculate pet merging.")))
                    .build());
        }
    }
}