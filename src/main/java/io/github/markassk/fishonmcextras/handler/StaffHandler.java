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
        if(text.getString().startsWith("You are now invisible!")) {
            this.isVanished = true;
        } else if (text.getString().startsWith("You are no longer invisible!")) {
            this.isVanished = false;
        }
    }
}
