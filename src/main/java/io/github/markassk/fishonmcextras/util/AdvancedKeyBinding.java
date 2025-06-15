package io.github.markassk.fishonmcextras.util;

import net.minecraft.client.option.KeyBinding;

public class AdvancedKeyBinding extends KeyBinding {
    public AdvancedKeyBinding(String translationKey, int code, String category) {
        super(translationKey, code, category);
    }

    public void onPressed(Runnable runTrue) {
        while (this.wasPressed()) {
            runTrue.run();
        }
    }
}
