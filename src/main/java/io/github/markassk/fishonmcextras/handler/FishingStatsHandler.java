package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FishingStatsHandler {
    private static FishingStatsHandler INSTANCE = new FishingStatsHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public static FishingStatsHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new FishingStatsHandler();
        }
        return INSTANCE;
    }

    public void appendTooltip(List<Text> textList, ItemStack itemStack) {
        if(config.fishStatsTooltip.showStats && KeybindHandler.instance().showExtraInfo && FOMCItem.getFOMCItem(itemStack) instanceof FOMCItem item) {
            Constant textRarityWindow = getTextRarity(item.rarity);
            for (int i = textList.size() - 1; i >= 0; i--) {
                if(textList.get(i).getString().contains("ʟᴜᴄᴋ")) addLine(textList, i, "ᴄʜᴀɴᴄᴇ ꜰᴏʀ ʜɪɢʜᴇʀ ʀᴀʀɪᴛʏ ꜰɪѕʜ", textRarityWindow);
                if(textList.get(i).getString().contains("sᴄᴀʟᴇ")) addLine(textList, i, "ᴄʜᴀɴᴄᴇ ꜰᴏʀ ʙɪɢɢᴇʀ ꜰɪѕʜ ɢʀᴏᴜᴘѕ", textRarityWindow);
                if(textList.get(i).getString().contains("ᴘʀᴏsᴘᴇᴄᴛ")) addLine(textList, i, "ᴄʜᴀɴᴄᴇ ꜰᴏʀ ᴍᴏʀᴇ ѕʜᴀʀᴅѕ", textRarityWindow);
                if(textList.get(i).getString().contains("ʀᴇᴇʟ sᴘᴇᴇᴅ")) addLine(textList, i, "ʟᴏᴡᴇʀѕ ᴛɪᴍᴇ ᴏꜰ ʀᴇᴇʟɪɴɢ ꜰɪѕʜ", textRarityWindow);
                if(textList.get(i).getString().contains("ʟɪɴᴇ sᴛʀᴇɴɢᴛʜ")) addLine(textList, i, "ᴍᴀᴋᴇѕ ꜰɪѕʜᴇѕ ᴍᴏᴠᴇ ʟᴇѕѕ", textRarityWindow);
                if(textList.get(i).getString().contains("ʙɪᴛᴇ sᴘᴇᴇᴅ")) addLine(textList, i, "ʟᴏᴡᴇʀѕ ᴛɪᴍᴇ ᴛɪʟʟ ꜰɪѕʜᴇѕ ʙɪᴛᴇ", textRarityWindow);
                if(textList.get(i).getString().contains("ᴀʟʙɪɴᴏ ᴄʜᴀɴᴄᴇ")) addLine(textList, i, "ɪɴᴄʀᴇᴀѕᴇѕ ᴀʟʙɪɴᴏ ᴄʜᴀɴᴄᴇ", textRarityWindow);
                if(textList.get(i).getString().contains("ᴍᴇʟᴀɴɪsᴛɪᴄ ᴄʜᴀɴᴄᴇ")) addLine(textList, i, "ɪɴᴄʀᴇᴀѕᴇѕ ᴍᴇʟᴀɴɪѕᴛɪᴄ ᴄʜᴀɴᴄᴇ", textRarityWindow);
                if(textList.get(i).getString().contains("ᴛʀᴏᴘʜʏ ᴄʜᴀɴᴄᴇ")) addLine(textList, i, "ɪɴᴄʀᴇᴀѕᴇѕ ᴛʀᴏᴘʜʏ ᴄʜᴀɴᴄᴇ", textRarityWindow);
            }
        }
    }

    private void addLine(List<Text> textList, int index, String text, Constant textRarityWindow) {
        textList.add(index + 1, TextHelper.concat(
                textRarityWindow.TAG,
                Text.literal("    └ ").formatted(Formatting.GRAY),
                Text.literal(text).formatted(Formatting.DARK_GRAY)
        ));
    }

    private Constant getTextRarity(Constant rarity) {
        return switch (rarity) {
            case COMMON -> Constant.TEXTCOMMON;
            case RARE -> Constant.TEXTRARE;
            case EPIC -> Constant.TEXTEPIC;
            case LEGENDARY -> Constant.TEXTLEGENDARY;
            case MYTHICAL -> Constant.TEXTMYTHICAL;
            case SPECIAL -> Constant.TEXTSPECIAL;
            default -> Constant.TEXTDEFAULT;
        };
    }
}
