package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;

public class ChatScreenHandler {
    private static ChatScreenHandler INSTANCE = new ChatScreenHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public boolean screenInit = false;

    public static ChatScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatScreenHandler();
        }
        return INSTANCE;
    }

    public Text appendTooltip(Text text) {
        String textString = text.getString();
        if(textString.startsWith("!")
                && textString.contains("\uF028 DannyPX »")
        ) {
            if(config.fun.isFoeTagPrefix) {
                String jsonText = TextHelper.textToJson(text);
                jsonText = jsonText.replace("\uF028", "\uE00B");
                jsonText = jsonText.replace("#B05BF9", "#00AF0E");
                return TextHelper.jsonToText(jsonText);
            } else {
                String jsonText = TextHelper.textToJson(text);
                jsonText = jsonText.replace("\uF028", "\uF028 \uE00B");
                return TextHelper.jsonToText(jsonText);
            }

        }
        return text;
    }
}
