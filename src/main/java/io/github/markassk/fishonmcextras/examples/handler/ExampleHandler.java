package io.github.markassk.fishonmcextras.examples.handler;

import io.github.markassk.fishonmcextras.handler.TitleHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ExampleHandler {
    /*
    * Any feature logic must be put into a Handler.
    *
    * Any screen logic (e.g. collecting text notably) must be put into a ScreenHandler.
    * These should not have any tick logic.
    *
    * Important, handlers do not access any Screens or ScreenHandlers.
    * Screens can access handlers.
    * Handlers can access other handlers.
    *
    * You should NOT EDIT other people's handlers unless it permitted by that author.
    * */

    // !! Required. INSTANCE Field. Must be static
    private static ExampleHandler INSTANCE = new ExampleHandler();

    // Fields are always private
    private String helloWorld = "Hello World";
    private final boolean shouldTick = false;

    // !! Required. Initiate INSTANCE and return INSTANCE. Must be static.
    public static ExampleHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ExampleHandler();
        }
        return INSTANCE;
    }

    // Ticker, this should not be static as this is part of the Handler Object.
    // Call the tick method in any of the ClientTickEvents registries.
    public void tick(MinecraftClient minecraftClient) {
        minecraftClient.inGameHud.setOverlayMessage(
                Text.literal(helloWorld), true
        );
    }

    // Other functions that you want to expose.
    public void otherFunction() {

        // You can also access other handlers
        long time = TitleHandler.instance().time;
    }

    // Make properties to access fields
    public boolean isShouldTick() {
        return shouldTick;
    }

    // Make properties to set fields
    public void setHelloWorld(String value) {
        this.helloWorld = value;
    }
}
