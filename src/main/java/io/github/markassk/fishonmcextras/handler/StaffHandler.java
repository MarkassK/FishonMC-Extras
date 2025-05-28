package io.github.markassk.fishonmcextras.handler;

import net.minecraft.text.Text;

public class StaffHandler {
    private static StaffHandler INSTANCE = new StaffHandler();

    public boolean isVanished = false;

    public static StaffHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new StaffHandler();
        }
        return INSTANCE;
    }


    public void onReceiveMessage(Text text) {
        if(text.getString().startsWith("SayanVanish | Your vanish state has been updated to ON")) {
            this.isVanished = true;
        } else if (text.getString().startsWith("SayanVanish | Your vanish state has been updated to OFF")) {
            this.isVanished = false;
        }
    }
}
