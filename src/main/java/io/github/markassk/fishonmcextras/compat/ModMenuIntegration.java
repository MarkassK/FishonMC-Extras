package io.github.markassk.fishonmcextras.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.markassk.fishonmcextras.screens.main.MainScreen;
import net.minecraft.client.MinecraftClient;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new MainScreen(MinecraftClient.getInstance(), parent);
    }
}
