package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.util.ItemStackHelper;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AuctionHandler {
    private static AuctionHandler INSTANCE = new AuctionHandler();

    public static AuctionHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new AuctionHandler();
        }
        return INSTANCE;
    }

    public void appendTooltip(List<Text> textList, ItemStack itemStack) {
        if (itemStack.getCount() > 1) {
            NbtCompound nbtCompound = ItemStackHelper.getNbt(itemStack);
            if(nbtCompound != null && nbtCompound.contains("renderInfo", NbtElement.LIST_TYPE)) {
                if(nbtCompound.get("renderInfo") instanceof NbtList nbtList) {
                    if(nbtList.size() == 1) {
                        NbtCompound renderInfo = nbtList.getCompound(0);
                        float money = renderInfo.getFloat("money");
                        float moneyPerItem = money / itemStack.getCount();

                        textList.set(textList.size() - (MinecraftClient.getInstance().options.advancedItemTooltips ? 7 : 5), TextHelper.concat(textList.get(textList.size() - (MinecraftClient.getInstance().options.advancedItemTooltips ? 7 : 5)), Text.literal(" ($" + TextHelper.fmnt(moneyPerItem) + " per item)").formatted(Formatting.DARK_GREEN)));
                    }
                }
            }
        }
    }
}
