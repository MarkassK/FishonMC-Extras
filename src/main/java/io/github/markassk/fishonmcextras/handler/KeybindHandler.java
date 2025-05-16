package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.mixin.KeyBindingAccessor;
import io.github.markassk.fishonmcextras.screens.main.MainScreen;
import io.github.markassk.fishonmcextras.util.AdvancedKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static KeybindHandler INSTANCE = new KeybindHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public final AdvancedKeyBinding openConfigKeybind = new AdvancedKeyBinding("key.fishonmcextras.openconfig", GLFW.GLFW_KEY_O, "category.fishonmcextras.general");
    public final AdvancedKeyBinding openExtraInfoKeybind = new AdvancedKeyBinding("key.fishonmcextras.openextrainfo", GLFW.GLFW_KEY_Z, "category.fishonmcextras.general");

    public boolean showExtraInfo = false;

    public static KeybindHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new KeybindHandler();
        }
        return INSTANCE;
    }

    public void init() {
        KeybindHandler.register(
                this.openConfigKeybind,
                this.openExtraInfoKeybind
        );
    }

    public void tick(MinecraftClient minecraftClient) {
        this.openConfigKeybind.onPressed(() -> minecraftClient.setScreen(new MainScreen(minecraftClient, minecraftClient.currentScreen)));

        if(minecraftClient.currentScreen != null) {
            this.showExtraInfo = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingAccessor) openExtraInfoKeybind).getBoundKey().getCode());
        }
    }

    private static void register(KeyBinding... keyBindings) {
        for (KeyBinding keyBinding : keyBindings) {
            KeyBindingHelper.registerKeyBinding(keyBinding);
        }
    }
}
