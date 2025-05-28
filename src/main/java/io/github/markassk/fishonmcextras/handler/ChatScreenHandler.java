package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;

public class ChatScreenHandler {
    private static ChatScreenHandler INSTANCE = new ChatScreenHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public boolean screenInit = false;

    public static ChatScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatScreenHandler();
        }
        return INSTANCE;
    }
}
