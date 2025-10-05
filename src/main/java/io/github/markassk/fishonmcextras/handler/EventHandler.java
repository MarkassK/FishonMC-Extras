package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;


public class EventHandler {
    private static EventHandler INSTANCE = new EventHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public Map<WeatherEvent, Long> weatherEvents = new HashMap<>();
    public long weatherEventAlertTime = 0L;

    public String fabledLocation = "";
    public long fabledEventAlertTime = 0L;

    public static EventHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new EventHandler();
        }
        return INSTANCE;
    }

    public boolean onReceiveMessage(Text text) {
        processOtherEvents(text);
        processWeatherEvents(text);
        
        return false; // Don't suppress any messages
    }

    private void processOtherEvents(Text text) {
        String textString = text.getString();
        //Fabled
        if(textString.startsWith("Visit the ") && textString.contains(" to")) {
            fabledEventAlertTime = System.currentTimeMillis();
            fabledLocation = textString.substring(textString.indexOf("Visit the ") + 10, textString.lastIndexOf(" "));
            if(config.eventTracker.otherEventOptions.fabledOptions.useAlertWarningSound) {
                NotificationSoundHandler.instance().playSoundWarning(config.eventTracker.otherEventOptions.fabledOptions.alertSoundType, MinecraftClient.getInstance());
            }
        }
    }

    private void processWeatherEvents(Text text) {
        WeatherEvent foundEvent = Arrays.stream(WeatherEvent.values()).filter(weatherEvent -> text.getString().contains(weatherEvent.TEXT)).findFirst().orElse(null);

        if(!config.eventTracker.weatherEventOptions.toggleOptions.rainbow && foundEvent == WeatherEvent.RAINBOW) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.thunderstorm && foundEvent == WeatherEvent.THUNDERSTORM) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.supercell && foundEvent == WeatherEvent.SUPERCELL) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.goldRush && foundEvent == WeatherEvent.GOLD_RUSH) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.rain && foundEvent == WeatherEvent.RAIN) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.bloomingOasis && foundEvent == WeatherEvent.BLOOMING_OASIS) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.fullMoon && foundEvent == WeatherEvent.FULL_MOON) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.blueMoon && foundEvent == WeatherEvent.BLUE_MOON) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.superMoon && foundEvent == WeatherEvent.SUPER_MOON) foundEvent = null;
        if(!config.eventTracker.weatherEventOptions.toggleOptions.bloodMoon && foundEvent == WeatherEvent.BLOOD_MOON) foundEvent = null;

        // Check moon alerts mute override
        boolean isMoon = foundEvent == WeatherEvent.FULL_MOON || foundEvent == WeatherEvent.BLUE_MOON ||
                         foundEvent == WeatherEvent.SUPER_MOON || foundEvent == WeatherEvent.BLOOD_MOON;
        if (config.eventTracker.weatherEventOptions.muteMoonAlerts && isMoon) {
            foundEvent = null;
        }

        if(foundEvent != null) {
            weatherEvents.put(foundEvent, System.currentTimeMillis());

            weatherEventAlertTime = System.currentTimeMillis();
            if(config.eventTracker.weatherEventOptions.useAlertWarningSound) {
                NotificationSoundHandler.instance().playSoundWarning(config.eventTracker.weatherEventOptions.alertSoundType, MinecraftClient.getInstance());
            }
        }
    }

    public enum WeatherEvent {
        RAINBOW("rainbow", "RAINBOW » A Rainbow has appeared!", TextHelper.concat(
                Text.literal("A "),
                Text.literal("R").withColor(0xFC5454).formatted(Formatting.BOLD),
                Text.literal("a").withColor(0xFCA800).formatted(Formatting.BOLD),
                Text.literal("i").withColor(0xFCFC54).formatted(Formatting.BOLD),
                Text.literal("n").withColor(0x54FC54).formatted(Formatting.BOLD),
                Text.literal("b").withColor(0x54FCFC).formatted(Formatting.BOLD),
                Text.literal("o").withColor(0xFC54FC).formatted(Formatting.BOLD),
                Text.literal("w ").withColor(0xA800A8).formatted(Formatting.BOLD),
                Text.literal("has appeared").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("500 ").formatted(Formatting.WHITE),
                        Text.literal("Luck").withColor(0x5454fc)
                )
        ),
        THUNDERSTORM("thunderstorm", "STORM » A thunderstorm has formed!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Thunderstorm ").formatted(Formatting.BOLD, Formatting.YELLOW),
                Text.literal("has formed!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("100 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a)
                )
        ),
        SUPERCELL("supercell", "STORM » A supercell has formed!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Supercell ").formatted(Formatting.BOLD).withColor(0xFCE211),
                Text.literal("has formed!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("150 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a)
                )
        ),
        GOLD_RUSH("gold_rush", "GOLD RUSH » A Gold Rush has begun!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Gold Rush ").formatted(Formatting.BOLD).withColor(0xECD65A),
                Text.literal("has begun!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("Base Shard Chance → ").formatted(Formatting.WHITE),
                        Text.literal("1/25").withColor(0xfca800)
                )
        ),
        RAIN("rain", "RAIN » A rain shower has begun!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Rain Shower ").formatted(Formatting.BOLD).withColor(0x63B6EC),
                Text.literal("has begun!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("50 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a)
                )
        ),
        BLOOMING_OASIS("blooming_oasis", "BLOOMING OASIS » A Blooming Oasis has appeared!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Blooming Oasis ").formatted(Formatting.BOLD).withColor(0xFC54FC),
                Text.literal("has appeared!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("100% ").formatted(Formatting.WHITE),
                        Text.literal("Pet Luck").withColor(0xfa93f1)
                )
        ),
        FULL_MOON("full_moon", "FULL MOON » A Full Moon has risen!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Full Moon ").formatted(Formatting.BOLD).withColor(0xFFFFFF),
                Text.literal("has risen!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("120 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a)
                )
        ),
        BLUE_MOON("blue_moon", "BLUE MOON » A Blue Moon has risen.", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Blue Moon ").formatted(Formatting.BOLD).withColor(0x60C2E9),
                Text.literal("has risen!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("120 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a),
                        Text.literal(", ").formatted(Formatting.GRAY),
                        Text.literal("5x ").formatted(Formatting.WHITE),
                        Text.literal("XP").withColor(0x73b3e6)
                )
        ),
        SUPER_MOON("super_moon", "SUPER MOON » A Super Moon has risen.", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Super Moon ").formatted(Formatting.BOLD).withColor(0xB0F9D5),
                Text.literal("has risen!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("120 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a),
                        Text.literal(", ").formatted(Formatting.GRAY),
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("150 ").formatted(Formatting.WHITE),
                        Text.literal("Reel Speed").withColor(0x72ebfc)
                )
        ),
        BLOOD_MOON("blood_moon", "BLOOD MOON » A Blood Moon has risen!", TextHelper.concat(
                Text.literal("A ").formatted(Formatting.WHITE),
                Text.literal("Super Moon ").formatted(Formatting.BOLD).withColor(0xE44848),
                Text.literal("has risen!").formatted(Formatting.WHITE)),
                TextHelper.concat(
                        Text.literal("+").formatted(Formatting.GRAY),
                        Text.literal("120 ").formatted(Formatting.WHITE),
                        Text.literal("Bite Speed").withColor(0x40f25a)
                )
        );

        public final String ID;
        public final String TEXT;
        public final Text TAG;
        public final Text DESC;

        WeatherEvent(String id, String text, Text tag, Text desc) {
            this.ID = id;
            this.TEXT = text;
            this.TAG = tag;
            this.DESC = desc;
        }
    }
}
