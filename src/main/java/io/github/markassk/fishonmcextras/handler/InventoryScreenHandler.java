package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.mixin.RecipeBookScreenAccessor;
import io.github.markassk.fishonmcextras.screens.widget.IconButtonWidget;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryScreenHandler {
    private static InventoryScreenHandler INSTANCE = new InventoryScreenHandler();

    public boolean screenInit = false;
    public boolean isRecipeBookOpen = false;

    public static InventoryScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new InventoryScreenHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if (screenInit) {
            this.createButton(minecraftClient);
            this.screenInit = false;
        }

        if(minecraftClient.currentScreen != null) {
            if(minecraftClient.currentScreen instanceof RecipeBookScreen<?> recipeBookScreen) {
                if(isRecipeBookOpen != ((RecipeBookScreenAccessor) recipeBookScreen).getRecipeBook().isOpen()) {
                    isRecipeBookOpen = ((RecipeBookScreenAccessor) recipeBookScreen).getRecipeBook().isOpen();

                    // Update Buttons
                    Screens.getButtons(minecraftClient.currentScreen).removeIf(clickableWidget -> clickableWidget instanceof IconButtonWidget);
                    this.createButton(minecraftClient);
                }
            }
        }
    }

    private void createButton(MinecraftClient minecraftClient) {
        if (minecraftClient.currentScreen != null) {
            List<ClickableWidget> clickableWidgets = new ArrayList<>();


            clickableWidgets.add(getButton(0, "Artisan", "\uF018", "artisan", "Requires atleast " + Constant.ANGLER.TAG.getString(), true, minecraftClient));
            clickableWidgets.add(getButton(1, "Identifier", "\uF015", "identifier", "Requires atleast " + Constant.SAILOR.TAG.getString(), true, minecraftClient));
            clickableWidgets.add(getButton(2, "Forge", "\uF013", "forge", "Requires atleast " + Constant.SAILOR.TAG.getString(), true, minecraftClient));
            clickableWidgets.add(getButton(3, "Scrapper", "\uF020", "scrapper", "Requires atleast " + Constant.SAILOR.TAG.getString(), true, minecraftClient));
            clickableWidgets.add(getButton(4, "Fish Merchant", "\uF012", "sell", "Requires atleast " + Constant.MARINER.TAG.getString(), true, minecraftClient));
            clickableWidgets.add(getButton(5, "Quests", "\uF007", "quest", "", true, minecraftClient));

            clickableWidgets.add(getButton(1, "Crew Info", "\uF038", "crew", "", false, true, minecraftClient));
            clickableWidgets.add(getButton(2, "Crew Home", "\uF039", "crew home", "", false, true, minecraftClient));
            clickableWidgets.add(getButton(3, "Crew Chat", "ab", "crew chat", "", false, minecraftClient));

            if(LocationHandler.instance().currentLocation == Constant.CREW_ISLAND) {
                clickableWidgets.add(getButton(4, "Crew Fly", "↑", "crew fly", "", false, minecraftClient));
                clickableWidgets.add(getButton(0, "Spawn", "\uF016", "spawn", "", false, true, minecraftClient));
            } else {
                clickableWidgets.add(getButton(0, "Instances", "\uF016", "instances", "", false, true, minecraftClient));
            }



            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        }
    }

    private IconButtonWidget getButton(int index, String text, String icon, String command, String tooltip, boolean isRight, boolean isLoader, MinecraftClient minecraftClient) {
        int padding = 4;
        int offsetRecipe = isRecipeBookOpen ? (isRight ? 180 : 190) : 100;

        IconButtonWidget.Builder builder = IconButtonWidget.builder(Text.literal(text), button -> {
                    // Command
                    if (minecraftClient.player != null) {
                        minecraftClient.player.networkHandler.sendChatCommand(command);
                    }

                })
                .position(
                        isRight ? minecraftClient.getWindow().getScaledWidth() / 2 + offsetRecipe : minecraftClient.getWindow().getScaledWidth() / 2 - offsetRecipe - 100,
                        minecraftClient.getWindow().getScaledHeight() / 2 - 82 + index * (padding + 24))
                .width(100)
                .stringIcon(icon)
                .isLoader(isLoader);

        if (!Objects.equals(tooltip, "")) {
            builder = builder.tooltip(Tooltip.of(Text.literal(tooltip)));
        }

        return builder.build();
    }

    private IconButtonWidget getButton(int index, String text, String icon, String command, String tooltip, boolean isRight, MinecraftClient minecraftClient) {
        return getButton(index, text, icon, command, tooltip, isRight, false, minecraftClient);
    }
}
