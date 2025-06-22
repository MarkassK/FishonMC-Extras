package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

public class WeatherHandler {
    private static WeatherHandler INSTANCE = new WeatherHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public String currentWeather = "";
    public long weatherChangedAtTime = 0L;

    public static WeatherHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(config.weatherTracker.showAlertHUD && !Objects.equals(currentWeather, BossBarHandler.instance().weather)) {
            this.currentWeather = BossBarHandler.instance().weather;
            this.checkWeatherEvent(BossBarHandler.instance().weather, minecraftClient);
        } else if(!config.weatherTracker.showAlertHUD && !Objects.equals(currentWeather, "")) this.currentWeather = "";
    }

    private void checkWeatherEvent(String weather, MinecraftClient minecraftClient) {
        if(Objects.equals(weather, Constant.THUNDERSTORM.ID)) {
            NotificationSoundHandler.instance().playSoundWarning(config.weatherTracker.alertSoundType, minecraftClient);
            TitleHandler.instance().setTitleHud(List.of(
                    Text.literal("Thunderstorm Started!").formatted(Formatting.YELLOW)
            ), 3000L, minecraftClient);
            this.weatherChangedAtTime = System.currentTimeMillis();
        }
    }

    public void onLeaveServer() {
        this.currentWeather = "";
    }
}
