package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Constant;
import io.github.markassk.fishonmcextras.FOMC.LevelColors;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.mixin.RecipeBookScreenAccessor;
import io.github.markassk.fishonmcextras.screens.widget.TextWidget;
import io.github.markassk.fishonmcextras.screens.widget.container.ContainerSideWidget;
import io.github.markassk.fishonmcextras.screens.widget.container.ContainerButtonWidget;
import io.github.markassk.fishonmcextras.screens.widget.container.ContainerButtonsWidget;
import io.github.markassk.fishonmcextras.screens.widget.container.ContainerHeaderWidget;
import io.github.markassk.fishonmcextras.screens.widget.timer.BaitShopTimerWidget;
import io.github.markassk.fishonmcextras.screens.widget.timer.TimerWidget;
import io.github.markassk.fishonmcextras.util.TextHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InventoryScreenHandler {
    private static InventoryScreenHandler INSTANCE = new InventoryScreenHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public boolean screenInit = false;
    public boolean isRecipeBookOpen = false;

    private final int recipeTranslation = 77;
    private boolean isReset = true;
    private List<String> playerList = new ArrayList<>();

    public static InventoryScreenHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new InventoryScreenHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if (screenInit) {
            if(CrewHandler.instance().crewState == CrewHandler.CrewState.HASCREW) {
                playerList.clear();
                ProfileDataHandler.instance().profileData.crewMembers.forEach(uuid -> {
                    String displayName = TabHandler.instance().getPlayer(uuid);
                    if(displayName != null) {
                        playerList.add(displayName);
                    }
                });
            }

            this.createButtonMenu(minecraftClient);
            this.createCrewMenu(minecraftClient);
            this.createHeader(minecraftClient);
            this.createTimerMenu(minecraftClient);

            this.screenInit = false;
        }

        if(minecraftClient.currentScreen != null) {
            if(minecraftClient.currentScreen instanceof RecipeBookScreen<?> recipeBookScreen) {
                if(isRecipeBookOpen != ((RecipeBookScreenAccessor) recipeBookScreen).getRecipeBook().isOpen()) {
                    isRecipeBookOpen = ((RecipeBookScreenAccessor) recipeBookScreen).getRecipeBook().isOpen();

                    // Update Buttons
                    this.resetButtons(minecraftClient);
                }
            }
        }

        if(!isReset) {
            isReset = true;
            this.resetButtons(minecraftClient);
        }
    }

    private void createHeader(MinecraftClient minecraftClient) {
        if(minecraftClient.currentScreen != null) {
            int height = 83;
            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            Text header = TabHandler.instance().player;
            clickableWidgets.add(new TextWidget(minecraftClient.getWindow().getScaledWidth() / 2 - MinecraftClient.getInstance().textRenderer.getWidth(header) / 2, minecraftClient.getWindow().getScaledHeight() / 2 - height - 28 / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, header, 0xFFFFFF, true));

            clickableWidgets.add(new ContainerHeaderWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 174 / 2,  minecraftClient.getWindow().getScaledHeight() / 2 - height - 28, Text.empty()));

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        }
    }

    private void resetButtons(MinecraftClient minecraftClient) {
        if (minecraftClient.currentScreen != null) {
            Screens.getButtons(minecraftClient.currentScreen).removeIf(clickableWidget -> clickableWidget instanceof ContainerButtonWidget || clickableWidget instanceof ContainerButtonsWidget || clickableWidget instanceof ContainerSideWidget || clickableWidget instanceof TextWidget || clickableWidget instanceof ContainerHeaderWidget || clickableWidget instanceof TimerWidget);
        }
        this.createButtonMenu(minecraftClient);
        this.createCrewMenu(minecraftClient);
        this.createHeader(minecraftClient);
        this.createTimerMenu(minecraftClient);
    }

    private void createCrewMenu(MinecraftClient minecraftClient) {
        if (CrewHandler.instance().crewState == CrewHandler.CrewState.HASCREW) {
            if(minecraftClient.currentScreen != null && config.isCrewButtonMenuOpen) {
                int height = - 82;
                int buttonSize = 22 + 1;
                int offsetRecipe = isRecipeBookOpen ? recipeTranslation : 0;

                List<ClickableWidget> clickableWidgets = new ArrayList<>();

                Text crew = TextHelper.concat(Text.literal(ScoreboardHandler.instance().crewName).formatted(Formatting.DARK_GREEN), Text.literal(" [").formatted(Formatting.DARK_GRAY), Text.literal(ScoreboardHandler.instance().crewLevel).withColor(LevelColors.valueOfLvl(Integer.parseInt(ScoreboardHandler.instance().crewLevel)).color), Text.literal("]").formatted(Formatting.DARK_GRAY));
                clickableWidgets.add(new TextWidget(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 + 105 / 2 - MinecraftClient.getInstance().textRenderer.getWidth(crew) / 2 + offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height + 7 + buttonSize / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, crew, 0xFFFFFF, true));

                clickableWidgets.add(assembleCrewButton(-buttonSize * 1, buttonSize, Text.literal("\uF038"), "crew", Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Crew Info\n").formatted(Formatting.BOLD, Formatting.WHITE),
                                Text.literal("Open crew menu of " + ScoreboardHandler.instance().crewName + ".").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )), minecraftClient));
                clickableWidgets.add(assembleCrewButton(buttonSize * 0, buttonSize, Text.literal("\uF039"), "crew home", Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Crew Home\n").formatted(Formatting.BOLD, Formatting.WHITE),
                                Text.literal("Go to your crew island.").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )), minecraftClient));
                clickableWidgets.add(assembleCrewButton(buttonSize * 1, buttonSize, Text.literal("ab"), "crew chat", Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Crew Chat\n").formatted(Formatting.BOLD, Formatting.WHITE),
                                Text.literal("Toggles crew chat on and off.").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )), minecraftClient));

                if(BossBarHandler.instance().currentLocation == Constant.CREW_ISLAND) {
                    clickableWidgets.add(assembleCrewButton(buttonSize * 2, buttonSize, Text.literal("↑"), "crew fly", Tooltip.of(
                            TextHelper.concat(
                                    Text.literal("Crew Fly\n").formatted(Formatting.BOLD, Formatting.WHITE),
                                    Text.literal("Toggle crew fly on and off.").formatted(Formatting.GRAY, Formatting.ITALIC)
                            )), minecraftClient));
                }

                clickableWidgets.addAll(assembleCrewList(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 + 105 /2 + offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 - 82 + buttonSize * 3, minecraftClient));

                clickableWidgets.add(new ContainerSideWidget(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 + offsetRecipe,  minecraftClient.getWindow().getScaledHeight() / 2 + height, Text.empty()));

                clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 + offsetRecipe + 105, minecraftClient.getWindow().getScaledHeight() / 2 + height + 1, Text.literal("←"), Tooltip.of(
                        Text.literal("Close Crew Menu").formatted(Formatting.BOLD, Formatting.WHITE)
                ), button -> {
                    config.isCrewButtonMenuOpen = false;
                    AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                    this.resetButtons(minecraftClient);
                }));

                Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
            } else if(minecraftClient.currentScreen != null) {
                int offsetRecipe = isRecipeBookOpen ? recipeTranslation : 0;

                List<ClickableWidget> clickableWidgets = new ArrayList<>();

                clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 + offsetRecipe + 1, minecraftClient.getWindow().getScaledHeight() / 2 - 22 / 2, Text.literal("→"), Tooltip.of(
                        Text.literal("Open Crew Menu").formatted(Formatting.BOLD, Formatting.WHITE)
                ), button -> {
                    config.isCrewButtonMenuOpen = true;
                    AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                    this.resetButtons(minecraftClient);
                }));

                Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
            }
        }
    }

    private void createButtonMenu(MinecraftClient minecraftClient) {
        if(minecraftClient.currentScreen != null && config.isButtonMenuOpen) {
            int height = 83;
            int buttonSize = 22 + 1;

            List<ClickableWidget> clickableWidgets = new ArrayList<>();
            clickableWidgets.add(assembleButton( -buttonSize * 3, 0, Text.literal("\uF018"), "artisan", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Artisan\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("The Artisan lets you upgrade your rod parts\n").formatted(Formatting.GRAY, Formatting.ITALIC),
                            Text.literal("\n"),
                            Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                            Text.literal(Constant.ANGLER.TAG.getString()).formatted(Formatting.WHITE)
                            )), minecraftClient));
            clickableWidgets.add(assembleButton( -buttonSize * 2, 0, Text.literal("\uF015"), "identifier", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Identifier\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("The Identifier gives unidentified armor its quality.\n").formatted(Formatting.GRAY, Formatting.ITALIC),
                            Text.literal("\n"),
                            Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                            Text.literal(Constant.SAILOR.TAG.getString()).formatted(Formatting.WHITE)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( -buttonSize * 1, 0, Text.literal("\uF013"), "forge", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Forge\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("The Forge is where you upgrade both armour tiers and supercharges tiers.\n").formatted(Formatting.GRAY, Formatting.ITALIC),
                            Text.literal("\n"),
                            Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                            Text.literal(Constant.SAILOR.TAG.getString()).formatted(Formatting.WHITE)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( buttonSize * 0, 0, Text.literal("\uF020"), "scrapper", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Scrapper\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("The Scrapper lets you scrap unwanted common armor.\n").formatted(Formatting.GRAY, Formatting.ITALIC),
                            Text.literal("\n"),
                            Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                            Text.literal(Constant.SAILOR.TAG.getString()).formatted(Formatting.WHITE)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( buttonSize * 1, 0, Text.literal("\uF012"), "sell", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Fish Merchant\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("The Fish Merchant is where you sell your fish.\n").formatted(Formatting.GRAY, Formatting.ITALIC),
                            Text.literal("\n"),
                            Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                            Text.literal(Constant.MARINER.TAG.getString()).formatted(Formatting.WHITE)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( buttonSize * 2, 0, Text.literal("★").formatted(Formatting.YELLOW, Formatting.BOLD), "calibrator", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Calibrator\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("Calibrate your rod parts with extra stats.").formatted(Formatting.GRAY, Formatting.ITALIC),
                            Text.literal("\n"),
                            Text.literal("Requires atleast ").formatted(Formatting.WHITE, Formatting.ITALIC),
                            Text.literal(Constant.MARINER.TAG.getString()).formatted(Formatting.WHITE)
                    )), minecraftClient));

            if(BossBarHandler.instance().currentLocation == Constant.CREW_ISLAND) {
                clickableWidgets.add(assembleButton( -buttonSize * 3, buttonSize, Text.literal("\uF016"), "spawn", Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Spawn\n").formatted(Formatting.BOLD, Formatting.WHITE),
                                Text.literal("Return to spawn.").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )), minecraftClient));
            } else {
                clickableWidgets.add(assembleButton( -buttonSize * 3, buttonSize, Text.literal("\uF016"), "instances", Tooltip.of(
                        TextHelper.concat(
                                TextHelper.concat(Text.literal("Instances").formatted(Formatting.BOLD, Formatting.WHITE), Text.literal(" (").formatted(Formatting.DARK_GRAY), Text.literal("i" + TabHandler.instance().instance).formatted(Formatting.YELLOW), Text.literal(")\n").formatted(Formatting.DARK_GRAY)),
                                Text.literal("The Instance Selector lets you change between instances.").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )), minecraftClient));
            }
            clickableWidgets.add(assembleButton( -buttonSize * 2, buttonSize, Text.literal("\uF006"), "ah", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Auction House\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("The Auction House is where you can buy and sell items to other players.").formatted(Formatting.GRAY, Formatting.ITALIC)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( -buttonSize * 1, buttonSize, Text.literal("\uF007"), "quest", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Quests\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("Quests are missions you activate per location that give rewards for catching fish based on size or rarity.").formatted(Formatting.GRAY, Formatting.ITALIC)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( buttonSize * 0, buttonSize, Text.literal("\uF005"), "pv", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Personal Vault\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("Opens your personal vault.").formatted(Formatting.GRAY, Formatting.ITALIC)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( buttonSize * 1, buttonSize, Text.literal("❌").formatted(Formatting.RED, Formatting.BOLD), "trash", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Trash\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("Opens the trash bin.").formatted(Formatting.GRAY, Formatting.ITALIC)
                    )), minecraftClient));
            clickableWidgets.add(assembleButton( buttonSize * 2, buttonSize, Text.literal("⛏").formatted(Formatting.GOLD, Formatting.BOLD), "craft", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Crafting Table\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("Opens the crafting table.").formatted(Formatting.GRAY, Formatting.ITALIC)
                    )), minecraftClient));
            clickableWidgets.add(assembleUrlButton( buttonSize * 3, buttonSize, Text.literal("\uF014"), "https://wiki.fishonmc.net/", Tooltip.of(
                    TextHelper.concat(
                            Text.literal("Wiki\n").formatted(Formatting.BOLD, Formatting.WHITE),
                            Text.literal("Open the official Wiki of FishOnMC.").formatted(Formatting.GRAY, Formatting.ITALIC)
                    )), minecraftClient));

            clickableWidgets.add(new ContainerButtonsWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 174 / 2,  minecraftClient.getWindow().getScaledHeight() / 2 + height, Text.empty()));

            clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 - 1, minecraftClient.getWindow().getScaledHeight() / 2 + 90 + 22 / 2, Text.literal("↑"), Tooltip.of(
                    Text.literal("Close Button Menu").formatted(Formatting.BOLD, Formatting.WHITE)
            ), button -> {
                config.isButtonMenuOpen = false;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                this.resetButtons(minecraftClient);
            }));

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        } else if (minecraftClient.currentScreen != null){
            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 22 / 2, minecraftClient.getWindow().getScaledHeight() / 2 + 84, Text.literal("↓"), Tooltip.of(
                    Text.literal("Open Button Menu").formatted(Formatting.BOLD, Formatting.WHITE)
            ), button -> {
                config.isButtonMenuOpen = true;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                this.resetButtons(minecraftClient);
            }));

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        }
    }

    private void createTimerMenu(MinecraftClient minecraftClient) {
        if(minecraftClient.currentScreen != null && config.isTimerButtonMenuOpen) {
            int offsetRecipe = isRecipeBookOpen ? recipeTranslation : 0;
            int height = - 82;
            int buttonSize = 22 + 1;

            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            Text title = Text.literal("Timers").formatted(Formatting.DARK_GREEN);
            clickableWidgets.add(new TextWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - 105 / 2 - MinecraftClient.getInstance().textRenderer.getWidth(title) / 2 - offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height + 7 + buttonSize / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, title, 0xFFFFFF, true));

            Text lineText = Text.literal("─────────").formatted(Formatting.DARK_GRAY);
            // Line
            clickableWidgets.add(new TextWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - 105 / 2 - MinecraftClient.getInstance().textRenderer.getWidth(lineText) / 2 - offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height - 4 + buttonSize * 2 - MinecraftClient.getInstance().textRenderer.fontHeight * 2, lineText, 0xFFFFFF, true));

            Text baitShop = Text.literal("Tackle Shop").formatted(Formatting.WHITE);
            clickableWidgets.add(new TextWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - 105 / 2 - MinecraftClient.getInstance().textRenderer.getWidth(baitShop) / 2 - offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height - 4 + buttonSize * 2 - MinecraftClient.getInstance().textRenderer.fontHeight, baitShop, 0xFFFFFF, true));

            if(config.timerTracker.baitShopNotification) {
                clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - 82/2 - 22/2 - (23 * 2) - offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height - 4 + buttonSize * 2, Text.literal("✔").formatted(Formatting.GREEN), Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Currently ").formatted(Formatting.WHITE),
                                Text.literal("Enabled\n").formatted(Formatting.GREEN),
                                Text.literal("When enabled, it will show a notification when tackle shop is restocked").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )
                ), button -> {
                    config.timerTracker.baitShopNotification = false;
                    AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                    this.resetButtons(minecraftClient);
                }));
            } else {
                clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - 82/2 - 22/2 - (23 * 2) - offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height - 4 + buttonSize * 2, Text.literal("❌").formatted(Formatting.RED), Tooltip.of(
                        TextHelper.concat(
                                Text.literal("Currently ").formatted(Formatting.WHITE),
                                Text.literal("Disabled\n").formatted(Formatting.RED),
                                Text.literal("When enabled, it will show a notification when tackle shop is restocked").formatted(Formatting.GRAY, Formatting.ITALIC)
                        )
                ), button -> {
                    config.timerTracker.baitShopNotification = true;
                    AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                    this.resetButtons(minecraftClient);
                }));
            }

            clickableWidgets.add(new BaitShopTimerWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - 22 / 2 - offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 + height - 2 + buttonSize * 2));

            clickableWidgets.add(new ContainerSideWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - offsetRecipe - 105,  minecraftClient.getWindow().getScaledHeight() / 2 + height, Text.empty()));

            clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - offsetRecipe - 105 - 22, minecraftClient.getWindow().getScaledHeight() / 2 + height + 1, Text.literal("→"), Tooltip.of(
                    Text.literal("Close Timer Menu").formatted(Formatting.BOLD, Formatting.WHITE)
            ), button -> {
                config.isTimerButtonMenuOpen = false;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                this.resetButtons(minecraftClient);
            }));

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        } else if (minecraftClient.currentScreen != null) {
            int offsetRecipe = isRecipeBookOpen ? recipeTranslation : 0;
            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            clickableWidgets.add(new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 177 / 2 - offsetRecipe - 1 - 22, minecraftClient.getWindow().getScaledHeight() / 2 - 22 / 2, Text.literal("←"), Tooltip.of(
                    Text.literal("Open Timer Menu").formatted(Formatting.BOLD, Formatting.WHITE)
            ), button -> {
                config.isTimerButtonMenuOpen = true;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
                this.resetButtons(minecraftClient);
            }));

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        }
    }

    private ClickableWidget assembleCrewButton(int x, int y, Text icon, String command, @Nullable Tooltip tooltip, MinecraftClient minecraftClient) {
        int offsetRecipe = isRecipeBookOpen ? recipeTranslation : 0;

        return new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 + 177 / 2 + 82 / 2 - 22 / 2 + x + offsetRecipe, minecraftClient.getWindow().getScaledHeight() / 2 - 82 + 7 + y, icon, tooltip, button -> {
            if (minecraftClient.player != null) {
                minecraftClient.player.networkHandler.sendChatCommand(command);
            }
        });
    }

    private List<ClickableWidget> assembleCrewList(int x, int y, MinecraftClient minecraftClient) {
        int lineHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 1;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        List<ClickableWidget> clickableWidgets = new ArrayList<>();

        Text onlineText = Text.literal("ᴏɴʟɪɴᴇ").formatted(Formatting.GRAY, Formatting.ITALIC);
        clickableWidgets.add(new TextWidget(x - textRenderer.getWidth(onlineText) / 2, y - lineHeight * 2 + 5, onlineText, 0xFFFFFF, true));
        Text lineText = Text.literal("─────────").formatted(Formatting.DARK_GRAY);
        clickableWidgets.add(new TextWidget(x - textRenderer.getWidth(lineText) / 2, y - lineHeight + 2, lineText, 0xFFFFFF, true));

        for (int i = 0; i < playerList.size(); i++) {
            String player = StringUtils.abbreviate(playerList.get(i), 16);
            int finalI = i;
            TextWidget playerWidget = new TextWidget(x - textRenderer.getWidth(player) / 2, y + lineHeight * i, Text.literal(player).formatted(Formatting.GREEN), 0xFFFFFF, true, iconButtonWidget -> {
                if (minecraftClient.player != null) {
                    minecraftClient.player.networkHandler.sendChatCommand("find " + playerList.get(finalI));
                }
            });
            playerWidget.setTooltip(Tooltip.of(Text.literal("/find " + playerList.get(i))));
            clickableWidgets.add(playerWidget);
        }

        return clickableWidgets;
    }

    private ClickableWidget assembleButton(int x, int y, Text icon, String command, @Nullable Tooltip tooltip, MinecraftClient minecraftClient) {
        return new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 22 / 2 + x, minecraftClient.getWindow().getScaledHeight() / 2 + 90 + y, icon, tooltip, button -> {
            if (minecraftClient.player != null) {
                minecraftClient.player.networkHandler.sendChatCommand(command);
            }
        });
    }

    private ClickableWidget assembleUrlButton(int x, int y, Text icon, String url, @Nullable Tooltip tooltip, MinecraftClient minecraftClient) {
        return new ContainerButtonWidget(minecraftClient.getWindow().getScaledWidth() / 2 - 22 / 2 + x, minecraftClient.getWindow().getScaledHeight() / 2 + 90 + y, icon, tooltip, button -> minecraftClient.setScreen(new ConfirmLinkScreen((confirmed) -> {
            if (confirmed) {
                Util.getOperatingSystem().open(url);
            }

            minecraftClient.setScreen(null);
        }, url, true))
        );
    }
}
