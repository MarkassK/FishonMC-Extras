package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;

public class KeybindHandler {
    private static KeybindHandler INSTANCE = new KeybindHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public boolean showExtraInfo = false;

    public static KeybindHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new KeybindHandler();
        }
        return INSTANCE;
    }
}
