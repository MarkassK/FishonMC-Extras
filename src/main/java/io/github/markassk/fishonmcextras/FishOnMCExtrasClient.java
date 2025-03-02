package io.github.markassk.fishonmcextras;

import io.github.markassk.fishonmcextras.commands.CommandRegistration;
import io.github.markassk.fishonmcextras.common.Tooltip.TooltipPetRating;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.hud.HudRenderer;
import io.github.markassk.fishonmcextras.trackers.FishTracker;
import io.github.markassk.fishonmcextras.trackers.EquippedPetTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class FishOnMCExtrasClient implements ClientModInitializer {
    public static FishOnMCExtrasConfig CONFIG;
    public static KeyBinding openConfigKeybind;


    public static final HudRenderer HUD_RENDERER = new HudRenderer();
    public static final FishTracker fishTracker = new FishTracker(HUD_RENDERER);
    private static boolean menuOpened = false;
    private static long lastMenuCloseTime = 0L;

    @Override
    public void onInitializeClient() {
        // Setup config screen, reads correct data to load IMPORTANT MUST BE FIRST
        AutoConfig.register(FishOnMCExtrasConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).getConfig();
        // Setup keybind to open config
        openConfigKeybind = new KeyBinding("key.fishonmcextras.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.fishonmcextras.general");
        KeyBindingHelper.registerKeyBinding(openConfigKeybind);
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.player != null && minecraftClient.currentScreen == null)
                while (openConfigKeybind.wasPressed()){
                    minecraftClient.setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, minecraftClient.currentScreen).get());
                }
        });

        EquippedPetTracker.initialize();
        CommandRegistration.initialize();
        HudRenderCallback.EVENT.register(HUD_RENDERER);
        HUD_RENDERER.loadStats();

        ClientPlayConnectionEvents.JOIN.register(this::onServerJoin);
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        ScreenEvents.BEFORE_INIT.register(this::onScreenOpen);

        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> lines = TooltipPetRating.appendTooltipRating(lines));

        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> TooltipPetRating.appendTooltipRating(message));
    }

    private void onClientTick(MinecraftClient client) {
        fishTracker.tick(client, menuOpened, lastMenuCloseTime);
    }

    // Corrected method signature
    private void onServerJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        // Delay scan by 1 tick to ensure player is initialized
        client.execute(() -> fishTracker.updateWorldContext(client));
    }

    private void onScreenOpen(
            MinecraftClient client,
            net.minecraft.client.gui.screen.Screen screen,
            int scaledWidth,
            int scaledHeight
    ) {
        menuOpened = true;
        ScreenEvents.remove(screen).register(removedScreen -> {
            menuOpened = false;
            lastMenuCloseTime = System.currentTimeMillis();
        });
    }
}