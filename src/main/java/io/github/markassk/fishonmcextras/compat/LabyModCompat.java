package io.github.markassk.fishonmcextras.compat;

public class LabyModCompat {
    private static LabyModCompat INSTANCE = new LabyModCompat();

    public boolean isLabyMod = false;

    public static LabyModCompat instance() {
        if (INSTANCE == null) {
            INSTANCE = new LabyModCompat();
        }
        return INSTANCE;
    }
}
