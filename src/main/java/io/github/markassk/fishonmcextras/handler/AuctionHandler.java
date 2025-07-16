package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types.Bait;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Lure;
import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class AuctionHandler {
    private static AuctionHandler INSTANCE = new AuctionHandler();

    public boolean tackleShopMenuState = false;

    public static AuctionHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new AuctionHandler();
        }
        return INSTANCE;
    }

    public void appendTooltip(List<Text> textList, ItemStack itemStack) {
        if (itemStack.getCount() > 1) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if(nbtCompound != null && nbtCompound.contains("renderInfo")) {
                if(nbtCompound.get("renderInfo") instanceof NbtList nbtList) {
                    if(nbtList.size() == 1) {
                        NbtCompound renderInfo = nbtList.getCompound(0).orElse(new NbtCompound());
                        float money = renderInfo.getFloat("money").orElse(0f);
                        float moneyPerItem = money / itemStack.getCount();

                        appendMoney(textList, moneyPerItem);
                    }
                }
            }
        } else if (tackleShopMenuState && textList.size() > 5 && textList.get(textList.size() - 5).getString().contains("$")) {
            NumberFormat nf = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);

            if(FOMCItem.isBait(itemStack)) {
                Bait bait = Bait.getBait(itemStack);
                if(bait != null) {
                    String priceLine = textList.get(textList.size() - (MinecraftClient.getInstance().options.advancedItemTooltips ? 7 : 5)).getString();
                    try {
                        Number price = nf.parse(priceLine.substring(priceLine.indexOf("$") + 1));
                        float moneyPerItem = price.floatValue() / bait.counter;
                        appendMoney(textList, moneyPerItem);
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if (FOMCItem.isLure(itemStack)) {
                Lure lure = Lure.getLure(itemStack);
                if(lure != null) {
                    String priceLine = textList.get(textList.size() - (MinecraftClient.getInstance().options.advancedItemTooltips ? 7 : 5)).getString();
                    try {
                        Number price = nf.parse(priceLine.substring(priceLine.indexOf("$") + 1));
                        float moneyPerItem = price.floatValue() / lure.counter;
                        appendMoney(textList, moneyPerItem);
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } else if (FOMCItem.isBait(itemStack)) {
            Bait bait = Bait.getBait(itemStack);
            if(bait != null) {
                NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
                if(nbtCompound != null && nbtCompound.contains("renderInfo")) {
                    if(nbtCompound.get("renderInfo") instanceof NbtList nbtList) {
                        if(nbtList.size() == 1) {
                            NbtCompound renderInfo = nbtList.getCompound(0).orElse(new NbtCompound());
                            float money = renderInfo.getFloat("money").orElse(0f);
                            float moneyPerItem = money / bait.counter;

                            appendMoney(textList, moneyPerItem);
                        }
                    }
                }
            }
        } else if (FOMCItem.isLure(itemStack)) {
            Lure lure = Lure.getLure(itemStack);
            if(lure != null) {
                NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
                if(nbtCompound != null && nbtCompound.contains("renderInfo")) {
                    if(nbtCompound.get("renderInfo") instanceof NbtList nbtList) {
                        if(nbtList.size() == 1) {
                            NbtCompound renderInfo = nbtList.getCompound(0).orElse(new NbtCompound());
                            float money = renderInfo.getFloat("money").orElse(0f);
                            float moneyPerItem = money / lure.counter;

                            appendMoney(textList, moneyPerItem);
                        }
                    }
                }
            }
        }
    }

    private void appendMoney(List<Text> textList, float count) {
        textList.set(textList.size() - (MinecraftClient.getInstance().options.advancedItemTooltips ? 7 : 5), TextHelper.concat(textList.get(textList.size() - (MinecraftClient.getInstance().options.advancedItemTooltips ? 7 : 5)), Text.literal(" ($" + TextHelper.fmnt(count) + " per item)").formatted(Formatting.DARK_GREEN)));
    }
}
