package io.github.markassk.fishonmcextras.handler;

public class ChatScreenHandler {
    private static ChatScreenHandler INSTANCE = new ChatScreenHandler();

    public boolean screenInit = false;

    public static ChatScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatScreenHandler();
        }
        return INSTANCE;
    }
}
