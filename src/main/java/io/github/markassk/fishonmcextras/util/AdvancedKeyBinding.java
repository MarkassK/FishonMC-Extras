package io.github.markassk.fishonmcextras.util;

import net.minecraft.client.option.KeyBinding;

public class AdvancedKeyBinding extends KeyBinding {
    private final int code;
    public AdvancedKeyBinding(String translationKey, int code, String category) {
        super(translationKey, code, category);
        this.code = code;
    }

    public void onPressed(Runnable runTrue) {
        while (this.wasPressed()) {
            runTrue.run();
        }
    }

    public int getCode() {
        return this.code;
    }
}
