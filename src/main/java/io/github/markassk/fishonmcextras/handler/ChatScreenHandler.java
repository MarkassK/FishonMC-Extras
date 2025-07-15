package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types.Defaults;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.handler.packet.PacketHandler;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
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
                && textString.contains("»")
                && Defaults.foeDevs.values().stream().anyMatch(foEDevType -> textString.contains(foEDevType.text))
        ) {
            String jsonText = TextHelper.textToJson(text);
            if(config.fun.isFoeTagPrefix) {
                jsonText = TextHelper.replaceToFoE(jsonText);
                jsonText = jsonText.replace("B05BF9", "00AF0E");
            } else {
                jsonText = jsonText.replace("\uF028", "\uF028 \uE00B");
            }
            return TextHelper.jsonToText(jsonText);

        }
        return text;
    }

    public void onOpenScreen() {
        if(MinecraftClient.getInstance().player != null) {
            PacketHandler.TYPING_PACKET.sendStartTypingPacket(MinecraftClient.getInstance().player.getUuid());
        }
    }

    public void onRemoveScreen() {
        if(MinecraftClient.getInstance().player != null) {
            PacketHandler.TYPING_PACKET.sendStopTypingPacket(MinecraftClient.getInstance().player.getUuid());
        }
    }
}
