package io.github.markassk.fishonmcextras.screens.main;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.widget.ModMenuButtonWidget;
import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class FoETitleScreen extends Screen {
    private static final Identifier INCOMPATIBLE_TEXTURE = Identifier.of("server_list/incompatible");
    private static final Identifier UNREACHABLE_TEXTURE = Identifier.of("server_list/unreachable");
    private static final Identifier PING_1_TEXTURE = Identifier.of("server_list/ping_1");
    private static final Identifier PING_2_TEXTURE = Identifier.of("server_list/ping_2");
    private static final Identifier PING_3_TEXTURE = Identifier.of("server_list/ping_3");
    private static final Identifier PING_4_TEXTURE = Identifier.of("server_list/ping_4");
    private static final Identifier PING_5_TEXTURE = Identifier.of("server_list/ping_5");
    private static final Identifier ICON = Identifier.of(FishOnMCExtras.MOD_ID, "title/icon");
    private static final int IMAGES_COUNT = 3;
    private static final List<Identifier> BACKGROUND_TEXTURES = Stream.iterate(0, i -> i < IMAGES_COUNT, i -> i + 1).map(i -> Identifier.of(FishOnMCExtras.MOD_ID, String.format("textures/gui/title/title_%d.png", (i + 1)))).toList();
    private static Identifier BACKGROUND_TEXTURE;

    private static final Text JOIN_SERVER = Text.literal("Join FishOnMC");
    private static final Text PLAY_SINGLEPLAYER = Text.literal("Singleplayer");
    private static final Text PLAY_MULTIPLAYER = Text.literal("Multiplayer");
    private static final Text MODS = Text.literal("Mods");
    private static final Text OPTIONS = Text.literal("Options");
    private static final Text QUIT = Text.literal("Quit Game");
    private static final Text COPYRIGHT = Text.translatable("title.credits");

    private static final int BUTTON_FULL_WIDTH = 180;
    private static final int BUTTON_HALF_WIDTH = 89;
    private static final int BUTTON_SPLIT = (BUTTON_FULL_WIDTH / 2) - BUTTON_HALF_WIDTH;
    private static final int BUTTON_PADDING = 4;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_LINE_HEIGHT = BUTTON_PADDING + BUTTON_HEIGHT;

    private MultiplayerServerListPinger pinger = new MultiplayerServerListPinger();
    private ServerInfo serverInfo;

    public FoETitleScreen() {
        super(Text.of("Title Screen"));
    }

    @Override
    public void tick() {
        super.tick();
        pinger.tick();
    }

    @Override
    protected void init() {
        BACKGROUND_TEXTURE = BACKGROUND_TEXTURES.get(new Random().nextInt(BACKGROUND_TEXTURES.size()));

        serverInfo = new ServerInfo("FishOnMC", "play.fishonmc.net", ServerInfo.ServerType.OTHER);

        Thread pingThread = new Thread(() -> {
            try {
                pinger.add(serverInfo, () -> serverInfo.setStatus(ServerInfo.Status.SUCCESSFUL), () -> {});
            } catch (Exception e) {
                serverInfo.setStatus(ServerInfo.Status.UNREACHABLE);
                serverInfo.ping = -1L;
                serverInfo.playerListSummary = null;
            }
        });
        pingThread.start();

        this.initButtons();
    }

    private void initButtons() {
        if(client != null) {
            ServerInfo sInfo = new ServerInfo("FishOnMC", "play.fishonmc.net", ServerInfo.ServerType.OTHER);
            sInfo.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED);

            int xPos = width / 4;
            int yPos = height / 2;

            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            clickableWidgets.add(this.getButton(JOIN_SERVER, Math.max(xPos - BUTTON_FULL_WIDTH / 2, 40), yPos, BUTTON_FULL_WIDTH, BUTTON_HEIGHT, () -> {
                ConnectScreen.connect(this, this.client, ServerAddress.parse("play.fishonmc.net"), sInfo, false, null);
            }));

            clickableWidgets.add(this.getButton(PLAY_SINGLEPLAYER, Math.max(xPos - BUTTON_FULL_WIDTH / 2, 40), yPos + BUTTON_LINE_HEIGHT, BUTTON_HALF_WIDTH, BUTTON_HEIGHT, () -> {
                this.client.setScreen(new SelectWorldScreen(this));
            }));

            clickableWidgets.add(this.getButton(PLAY_MULTIPLAYER, Math.max(xPos + BUTTON_SPLIT, 40 + BUTTON_HALF_WIDTH + BUTTON_SPLIT * 2), yPos + BUTTON_LINE_HEIGHT, BUTTON_HALF_WIDTH, BUTTON_HEIGHT, () -> {
                this.client.setScreen(this.client.options.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this));
            }));

            clickableWidgets.add(new ModMenuButtonWidget(Math.max(xPos - BUTTON_FULL_WIDTH / 2, 40), yPos + BUTTON_LINE_HEIGHT * 2, BUTTON_FULL_WIDTH, BUTTON_HEIGHT, MODS, this));

            clickableWidgets.add(this.getButton(OPTIONS, Math.max(xPos - BUTTON_FULL_WIDTH / 2, 40), (int) (yPos + BUTTON_LINE_HEIGHT * 3.5), BUTTON_HALF_WIDTH, BUTTON_HEIGHT, () -> {
                this.client.setScreen(new OptionsScreen(this, this.client.options));
            }));

            clickableWidgets.add(this.getButton(QUIT, Math.max(xPos + BUTTON_SPLIT, 40 + BUTTON_HALF_WIDTH + BUTTON_SPLIT * 2), (int) (yPos + BUTTON_LINE_HEIGHT * 3.5), BUTTON_HALF_WIDTH, BUTTON_HEIGHT, () -> {
                this.client.scheduleStop();
            }));

            int textWidth = this.textRenderer.getWidth(COPYRIGHT);
            int xPos2 = this.width - textWidth - 2;
            clickableWidgets.add(new PressableTextWidget(xPos2, this.height - 10, textWidth, 10, COPYRIGHT, button -> this.client.setScreen(new CreditsAndAttributionScreen(this)), this.textRenderer));

            clickableWidgets.forEach(this::addDrawableChild);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        this.renderLogo(context);
        this.renderServerStatus(context);
    }

    private void renderLogo(DrawContext context) {
        int xPos = width / 4;
        int yPos = height / 2;

        int textureSize = (int) (512 / 4f);

        context.drawGuiTexture(RenderLayer::getGuiTextured, ICON, Math.max(xPos - textureSize / 2, 40 + (BUTTON_FULL_WIDTH - textureSize) / 2), yPos - textureSize, textureSize, textureSize);
    }

    private void renderServerStatus(DrawContext context) {
        if(client != null) {
            try {
                boolean playerCountEmpty = serverInfo.playerCountLabel == null || serverInfo.playerCountLabel.getString().isEmpty();

                if(!playerCountEmpty) {
                    // Draw summarizing text ("There are X players online" or "There are currently no players online.")
                    MutableText key = serverInfo.players != null
                            ? TextHelper.concat(Text.literal("There are "), Text.literal(String.valueOf(serverInfo.players.online())).formatted(Formatting.BOLD, Formatting.YELLOW), Text.literal(" players online."))
                            : Text.literal("There are currently no online players.");
                    context.drawCenteredTextWithShadow(client.textRenderer, key, Math.max((width / 4), 90 + 40), (height / 2) - 11, 0xFFFFFFFF);

                    // Update prelogin latency info
                    FishOnMCExtras.preLoginLatency = ((int) serverInfo.ping);
                }
                else if(serverInfo.label != null) {
                    context.drawCenteredTextWithShadow(client.textRenderer, serverInfo.label, Math.max((width / 4), 90 + 40), (height / 2) - 11, 0xFFFFFFFF);
                }
            }
            catch (Exception e) {
                context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("An error occurred whilst connecting to the server."), Math.max((width / 4), 90 + 40), (height / 2) - 11, 0xFFFFFFFF);
                throw e;
            }

            // I have no idea how Mojang does anything, their UI code sucks balls
            int l = (height / 2);
            int x = (width / 4) - 75 + 147;
            int y = l + 5;
            Identifier tex;

            // This some real YandereDev shit
            if(serverInfo.ping < 0L) tex = UNREACHABLE_TEXTURE;
            else if(serverInfo.ping < 50L) tex = PING_5_TEXTURE;
            else if(serverInfo.ping < 100L) tex = PING_4_TEXTURE;
            else if(serverInfo.ping < 175L) tex = PING_3_TEXTURE;
            else if(serverInfo.ping < 300L) tex = PING_2_TEXTURE;
            else tex = PING_1_TEXTURE;

            context.drawGuiTexture(RenderLayer::getGuiTextured, tex, Math.max(x, 162 + 40), y, 10, 8);
        }
    }

    @Override
    public void removed() {
        super.removed();
        pinger.cancel();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int sw = width;
        int sh = height;
        if(sh >= sw) {
            sw = (int)(sh * 1.77f);
        }
        if(sw >= sh) {
            sh = (int)(sw * 0.56f);
        }
        if(sh < height) {
            int missing = height - sh;
            sh += missing;
            sw += missing * 1.77f;
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, 0, 0, 0, 0, sw, sh, sw, sh);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(client != null) {
            if (super.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }

            // Refresh bind
            if (keyCode == GLFW.GLFW_KEY_F5) {
                client.setScreen(new FoETitleScreen());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private ClickableWidget getButton(Text buttonText, int x, int y, int width, int height, Callback callback) {
        return ButtonWidget.builder(buttonText, button -> callback.execute())
                .dimensions(x, y, width, height)
                .build();
    }

    private interface Callback {
        void execute();
    }
}
