package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.screens.widget.container.ContainerButtonWidget;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonalVaultScreenHandler {
    private static PersonalVaultScreenHandler INSTANCE = new PersonalVaultScreenHandler();

    public boolean personalVaultMenuState = false;
    public int page = 0;

    public static PersonalVaultScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new PersonalVaultScreenHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(personalVaultMenuState) {
            personalVaultMenuState = false;
            this.createButtons(minecraftClient);
        }
    }

    private void createButtons(MinecraftClient minecraftClient) {
        if(minecraftClient.currentScreen != null) {
            int buttonSize = 22 + 1;

            List<ClickableWidget> clickableWidgets = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                clickableWidgets.add(assembleButton( -buttonSize * 4 + buttonSize * i, 0, page == i ? Text.literal("" + i).formatted(Formatting.BOLD, Formatting.GOLD) : Text.literal("" + i).formatted(Formatting.BOLD),  page == i ? null : "pv " + i, Tooltip.of(
                                i == 1 ? TextHelper.concat(
                                        Text.literal("Page " + i).formatted(Formatting.BOLD, Formatting.WHITE)
                                ) :
                                TextHelper.concat(
                                        Text.literal("Page " + i).formatted(Formatting.BOLD, Formatting.WHITE),
                                        Text.literal("\n\n"),
                                        Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                                        i == 2 ? Text.literal(Constant.ANGLER.TAG.getString()).formatted(Formatting.WHITE) :
                                                i == 3 ? Text.literal(Constant.SAILOR.TAG.getString()).formatted(Formatting.WHITE) :
                                                        i == 4 ? Text.literal(Constant.MARINER.TAG.getString()).formatted(Formatting.WHITE) :
                                                                i == 5 ? Text.literal(Constant.CAPTAIN.TAG.getString()).formatted(Formatting.WHITE) :
                                                                        Text.literal(Constant.ADMIRAL.TAG.getString()).formatted(Formatting.WHITE)
                                )
                ), minecraftClient));
            }

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        }
    }

    private ClickableWidget assembleButton(int x, int y, Text icon, @Nullable String command, @Nullable Tooltip tooltip, MinecraftClient minecraftClient) {
        if(command != null) {
            return new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 22 / 2 + x, minecraftClient.getWindow().getScaledHeight() / 2 + 111 + y, icon, tooltip, button -> {
                if (minecraftClient.player != null) {
                    minecraftClient.player.networkHandler.sendChatCommand(command);
                }
            });
        } else {
            return new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 22 / 2 + x, minecraftClient.getWindow().getScaledHeight() / 2 + 111 + y, icon, tooltip);
        }
    }
}
