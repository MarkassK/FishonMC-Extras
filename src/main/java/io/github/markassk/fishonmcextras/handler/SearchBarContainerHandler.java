package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FOMC.Types.Armor;
import io.github.markassk.fishonmcextras.FOMC.Types.FOMCItem;
import io.github.markassk.fishonmcextras.FOMC.Types.Pet;
import io.github.markassk.fishonmcextras.screens.widget.SearchBarKeyWordWidget;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchBarContainerHandler {
    private static SearchBarContainerHandler INSTANCE = new SearchBarContainerHandler();

    public boolean containerMenuState = false;
    public SearchBarKeyWordWidget searchBar;

    public String searchString = "";
    public SearchFilter searchFilter = null;
    public Operator operator = null;
    public Float searchValue = null;

    private final List<Text> hoverInfo = new ArrayList<>();

    public SearchBarContainerHandler() {
        hoverInfo.add(Text.literal("Can search any word, including in tooltips").formatted(Formatting.WHITE));
        hoverInfo.add(Text.literal("You can also use Search Filters for more granular filtering").formatted(Formatting.GRAY, Formatting.ITALIC));
        hoverInfo.add(Text.empty());
        hoverInfo.add(Text.literal("Filters").formatted(Formatting.BOLD, Formatting.GRAY));
        hoverInfo.addAll(Arrays.stream(SearchFilter.values()).map(value -> TextHelper.concat(Text.literal("- ").formatted(Formatting.GRAY), Text.literal(value.KEYWORD).formatted(Formatting.GREEN))).toList());
        hoverInfo.add(Text.empty());
        hoverInfo.add(Text.literal("Operators").formatted(Formatting.BOLD, Formatting.GRAY));
        hoverInfo.addAll(Arrays.stream(Operator.values()).map(value -> TextHelper.concat(Text.literal("- ").formatted(Formatting.GRAY), Text.literal(value.OPERATOR).formatted(Formatting.GREEN))).toList());
//        hoverInfo.add(Text.empty());
//        hoverInfo.add(Text.literal("Examples Search Queries").formatted(Formatting.BOLD, Formatting.GRAY));
//        hoverInfo.add(Text.literal("quality>60").formatted(Formatting.GREEN));
//        hoverInfo.add(Text.literal("Find armor pieces with higher than 60% quality").formatted(Formatting.GRAY, Formatting.ITALIC));
//        hoverInfo.add(Text.empty());
//        hoverInfo.add(Text.literal("rating<40").formatted(Formatting.GREEN));
//        hoverInfo.add(Text.literal("Find pets with lower than 40% pet rating").formatted(Formatting.GRAY, Formatting.ITALIC));
    }

    public static SearchBarContainerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchBarContainerHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(containerMenuState) {
            containerMenuState = false;
            this.createButtons(minecraftClient);
        }
    }

    private void createButtons(MinecraftClient minecraftClient) {
        if (minecraftClient.currentScreen != null) {
            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            searchBar = new SearchBarKeyWordWidget(minecraftClient.textRenderer, minecraftClient.getWindow().getScaledWidth() / 2 - 80, minecraftClient.getWindow().getScaledHeight() / 2 - 133, 160, 20, Text.literal("Search Item"), hoverInfo);
            searchBar.setText(searchString);
            searchBar.setPlaceholder(Text.literal("Search Item").formatted(Formatting.GRAY));

            if(!searchString.isBlank()) {
                searchFilter = Arrays.stream(SearchFilter.values()).filter(value -> searchString.startsWith(value.KEYWORD)).findFirst().orElse(null);
                operator = Arrays.stream(Operator.values()).filter(value -> searchString.contains(value.OPERATOR)).findFirst().orElse(null);
                if(searchFilter != null && operator != null) {
                    try {
                        searchValue = Float.parseFloat(searchString.substring(searchString.indexOf(operator.OPERATOR) + 1));
                    } catch (NumberFormatException e) {
                        searchValue = null;
                    }
                }
                searchBar.setSpecialFocus(searchFilter != null && operator != null);
            }

            searchBar.setChangedListener(listener -> {
                searchString = listener;
                searchFilter = Arrays.stream(SearchFilter.values()).filter(value -> searchString.startsWith(value.KEYWORD)).findFirst().orElse(null);
                operator = Arrays.stream(Operator.values()).filter(value -> searchString.contains(value.OPERATOR)).findFirst().orElse(null);
                if(searchFilter != null && operator != null) {
                    try {
                        searchValue = Float.parseFloat(searchString.substring(searchString.indexOf(operator.OPERATOR) + 1));
                    } catch (NumberFormatException e) {
                        searchValue = null;
                    }
                }
                searchBar.setSpecialFocus(searchFilter != null && operator != null);
            });
            searchBar.setMaxLength(256);
            clickableWidgets.add(searchBar);

            Screens.getButtons(minecraftClient.currentScreen).addAll(clickableWidgets);
        }
    }

    public static boolean checkItem(FOMCItem fomcItem, SearchFilter filter, Operator operator, float value) {
        return switch (filter) {
            case QUALITY -> fomcItem instanceof Armor armor && checkValue(operator, armor.quality, value);
            case LEVEL -> fomcItem instanceof Pet pet && checkValue(operator, pet.lvl, value);
            case RATING -> fomcItem instanceof Pet pet && checkValue(operator, pet.percentPetRating * 100, value);
            case LUCK -> fomcItem instanceof Armor armor ? checkValue(operator, armor.luck.amount, value) : fomcItem instanceof Pet pet && checkValue(operator, pet.climateStat.maxLuck + pet.locationStat.maxLuck, value);
            case SCALE -> fomcItem instanceof Armor armor ? checkValue(operator, armor.scale.amount, value) : fomcItem instanceof Pet pet && checkValue(operator, pet.climateStat.maxScale + pet.locationStat.maxScale, value);
        };
    }

    private static boolean checkValue(Operator operator, float value, float valueToMatch) {
        return switch (operator) {
            case GREATER -> value > valueToMatch;
            case LESSER -> value < valueToMatch;
            case EQUAL -> value == valueToMatch;
        };
    }

    public enum SearchFilter {
        QUALITY("quality"), // armor quality
        LEVEL("level"), // pet level
        RATING("rating"), // pet rating
        LUCK("luck"), // pet/armor luck
        SCALE("scale"), // pet/scale
        ;

        public final String KEYWORD;

        SearchFilter(String keyword) {
            this.KEYWORD = keyword;
        }
    }

    public enum Operator {
        GREATER(">"),
        LESSER("<"),
        EQUAL("==");

        public final String OPERATOR;

        Operator(String operator) {
            this.OPERATOR = operator;
        }
    }
}
