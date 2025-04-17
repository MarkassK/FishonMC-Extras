package io.github.markassk.fishonmcextras;

import io.github.markassk.fishonmcextras.commands.CommandRegistry;
import io.github.markassk.fishonmcextras.handler.FishCatchHandler;
import io.github.markassk.fishonmcextras.handler.PetEquipHandler;
import io.github.markassk.fishonmcextras.screens.hud.MainHudRenderer;
import io.github.markassk.fishonmcextras.v1.tooltip.TooltipPetRating;
import io.github.markassk.fishonmcextras.v1.handler.LookTickHandler;
import io.github.markassk.fishonmcextras.v1.screen.PetMergeCalculatorScreen;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.v1.hud.HudRenderer;
import io.github.markassk.fishonmcextras.v1.trackers.FishTracker;
import io.github.markassk.fishonmcextras.v1.trackers.EquippedPetTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class FishOnMCExtrasClient implements ClientModInitializer {
    public static FishOnMCExtrasConfig CONFIG;
    public static KeyBinding openConfigKeybind;

    //region V1
    public static final HudRenderer HUD_RENDERER = new HudRenderer();
    public static final FishTracker fishTracker = new FishTracker(HUD_RENDERER);
    private static boolean menuOpened = false;
    private static long lastMenuCloseTime = 0L;
    //endregion

    public static final MainHudRenderer MAIN_HUD_RENDERER = new MainHudRenderer();

    @Override
    public void onInitializeClient() {
        // Setup config screen, reads correct data to load IMPORTANT MUST BE FIRST
        AutoConfig.register(FishOnMCExtrasConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).getConfig();
        // Setup keybind to open config
        openConfigKeybind = new KeyBinding("key.fishonmcextras.openconfig", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.fishonmcextras.general");
        KeyBindingHelper.registerKeyBinding(openConfigKeybind);

        //TODO Change screen to new FoE Menu
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.player != null && minecraftClient.currentScreen == null)
                while (openConfigKeybind.wasPressed()){
                    minecraftClient.setScreen(AutoConfig.getConfigScreen(FishOnMCExtrasConfig.class, minecraftClient.currentScreen).get());
                }
        });

        EquippedPetTracker.initialize();
        HudRenderCallback.EVENT.register(HUD_RENDERER);
        HUD_RENDERER.loadStats();

        ClientPlayConnectionEvents.JOIN.register(this::onServerJoin);
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        ScreenEvents.BEFORE_INIT.register(this::beforeScreenOpen);
        ScreenEvents.AFTER_INIT.register(this::afterScreenOpen);

        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> lines = TooltipPetRating.appendTooltipRating(lines, stack));

        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> TooltipPetRating.appendTooltipRating(message));

        //TODO V2
        CommandRegistry.initialize();

        ClientPlayConnectionEvents.JOIN.register(this::onJoin);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onLeave);
        ClientTickEvents.END_CLIENT_TICK.register(this::onEndClientTick);

        HudRenderCallback.EVENT.register(MAIN_HUD_RENDERER);
    }


    private void onEndClientTick(MinecraftClient minecraftClient) {
        if(minecraftClient.getCurrentServerEntry() != null ) {
            if(Objects.equals(minecraftClient.getCurrentServerEntry().address, "play.fishonmc.net")) {
                assert minecraftClient.player != null;
                FishCatchHandler.instance().tick(minecraftClient.player);
                PetEquipHandler.instance().tick();
            }
        }
    }

    private void onJoin(ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender packetSender, MinecraftClient minecraftClient) {
        FishCatchHandler.instance().isDoneScanning = false;

        if(minecraftClient.getCurrentServerEntry() != null ) {
            if(Objects.equals(minecraftClient.getCurrentServerEntry().address, "play.fishonmc.net")) {
                System.out.println("[FoE] Scan Start");
                minecraftClient.execute(() -> {
                    assert minecraftClient.player != null;
                    FishCatchHandler.instance().onJoinServer(minecraftClient.player);
                });
            }
        }
    }


    private void onLeave(ClientPlayNetworkHandler clientPlayNetworkHandler, MinecraftClient minecraftClient) {
        FishCatchHandler.instance().onLeaveServer();
    }

    //region V1
    private void onClientTick(MinecraftClient client) {
        fishTracker.tick(client, menuOpened, lastMenuCloseTime);
        LookTickHandler.instance().tickClient();
    }

    // Corrected method signature
    private void onServerJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        // Delay scan by 1 tick to ensure player is initialized
        client.execute(() -> fishTracker.updateWorldContext(client));
    }

    private void afterScreenOpen(
            MinecraftClient client,
            Screen screen,
            int scaledWidth,
            int scaledHeight
    ) {
        // Pet Menu핑
        if(Objects.equals(screen.getTitle().getString(), "Pet Menu\uEEE6\uEEE5\uEEE3핑")) {
            Screens.getButtons(screen).add(ButtonWidget.builder(Text.literal("Pet Merge Calculator"), button -> {
                        assert client.player != null;
                        client.setScreen(new PetMergeCalculatorScreen(client.player, client.currentScreen));
                    })
                    .dimensions(scaledWidth / 2 - (130 / 2), scaledHeight / 2 + 120, 130, 20)
                    .tooltip(Tooltip.of(Text.literal("Open up the screen to calculate pet merging.")))
                    .build());

//            Screens.getButtons(screen).add(ButtonWidget.builder(Text.literal("Create data"), button -> {
//                        assert client.player != null;
//                        makeData(client);
//                    })
//                    .dimensions(scaledWidth / 2 - (130 / 2), scaledHeight / 2 + 143, 130, 20)
//                    .tooltip(Tooltip.of(Text.literal("Create data from items in inventory")))
//                    .build());
        }
    }

    // TODO EVENT
//    private void makeData(MinecraftClient client) {
//        Path dir = Paths.get(FabricLoader.getInstance().getConfigDir().resolve("fomc").toUri());
//        assert client.player != null;
//        client.player.getInventory().main.forEach(stack -> {
//            Types.FOMCItem fomcItem = Types.getFOMCItem(stack);
//            if(fomcItem != null) {
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                Path filePath;
//                String json = gson.toJson(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).getOrThrow());
//                Text prefix = Text.literal("EVENT | ").formatted(Formatting.BOLD, Formatting.GREEN);
//
//                switch (fomcItem.type) {
//                    case Constants.Identifier.ItemTypes.PET -> {
//                        Types.Pet pet = (Types.Pet) fomcItem;
//                        filePath = dir.resolve(pet.petId + "_" + pet.locationStat.id + ".json");
//                        if(!Files.exists(filePath)) {
//                            Text text = Text.literal(pet.petId + " " + pet.locationStat.id + ": " + pet.discovererName).formatted(Formatting.ITALIC, Formatting.GRAY);
//                            client.player.sendMessage(TextHelper.concat(prefix, text));
//                        }
//                    }
//                    case Constants.Identifier.ItemTypes.FISH -> {
//                        Types.Fish fish = (Types.Fish) fomcItem;
//                        filePath = dir.resolve(fish.fishId + "_" + fish.locationId + ".json");
//                        if(!Files.exists(filePath)) {
//                            Text text = Text.literal(fish.fishId + " " + fish.locationId + ": " + fish.catcherName).formatted(Formatting.ITALIC, Formatting.GRAY);
//                            client.player.sendMessage(TextHelper.concat(prefix, text));
//                        }
//                    }
//                    default -> {
//                        filePath = dir.resolve("error.json");
//                        if(Files.exists(filePath)) return;
//                    }
//                }
//
//                try {
//                    if(!Files.exists(filePath)) {
//                        Files.writeString(filePath, json);
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }

    private void beforeScreenOpen(
            MinecraftClient client,
            Screen screen,
            int scaledWidth,
            int scaledHeight
    ) {
        menuOpened = true;

        ScreenEvents.remove(screen).register(removedScreen -> {
            menuOpened = false;
            lastMenuCloseTime = System.currentTimeMillis();
        });
    }
    //endregion
}