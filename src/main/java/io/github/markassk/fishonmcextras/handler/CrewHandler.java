package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.compat.LabyModCompat;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.mixin.ChatScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class CrewHandler {
    private static CrewHandler INSTANCE = new CrewHandler();
    private List<UUID> crewMembers = new ArrayList<>();

    public boolean crewMenuState = false;
    public boolean isCrewNearby = false;
    public boolean isCrewInRenderDistance = false;

    public static CrewHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new CrewHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(this.crewMenuState && minecraftClient.player != null) {
            List<UUID> uuids = new ArrayList<>();

            for (int i = 0; i < minecraftClient.player.currentScreenHandler.slots.size(); i++) {
                ItemStack itemStack = minecraftClient.player.currentScreenHandler.getSlot(i).getStack();
                if (
                        minecraftClient.player.currentScreenHandler.getSlot(i).inventory != minecraftClient.player.getInventory() && itemStack.getItem() == Items.PLAYER_HEAD
                                && Objects.requireNonNull(itemStack.get(DataComponentTypes.PROFILE)).id().isPresent()) {
                    if(uuids.stream().noneMatch(uuid -> uuid.equals(Objects.requireNonNull(itemStack.get(DataComponentTypes.PROFILE)).id().get()))) {
                        uuids.add(Objects.requireNonNull(itemStack.get(DataComponentTypes.PROFILE)).id().get());
                    }

                }
            }

            if(uuids.stream().anyMatch(uuid -> uuid.equals(minecraftClient.player.getUuid()))) {
                ProfileDataHandler.instance().profileData.crewState = CrewState.HASCREW;
                this.crewMembers = uuids;
            }
        }

        isCrewNearbyCheck(minecraftClient);
    }

    public void onScreenClose() {
        if(this.crewMembers.stream().anyMatch(uuid -> uuid.equals(MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getUuid() : null))) {
            ProfileDataHandler.instance().profileData.crewMembers = this.crewMembers;
        }
        ProfileDataHandler.instance().saveStats();
    }

    public void setNoCrew() {
        ProfileDataHandler.instance().profileData.crewState = CrewState.NOCREW;
        ProfileDataHandler.instance().saveStats();
    }

    public void reset() {
        ProfileDataHandler.instance().profileData.crewState = CrewState.NOTINITIALIZED;
        ProfileDataHandler.instance().saveStats();
    }

    public void onReceiveMessage(Text message) {
        if(message.getString().contains("CREWS » You left the crew")) {
            setNoCrew();
        } else if(message.getString().contains("CREWS » You have joined")) {
            reset();
        }

        if(message.getString().startsWith("CREWS » Crew Chat has been enabled")) {
            ProfileDataHandler.instance().profileData.isInCrewChat = true;
            ProfileDataHandler.instance().saveStats();
        } else if (message.getString().startsWith("CREWS » Crew Chat has been disabled")) {
            ProfileDataHandler.instance().profileData.isInCrewChat = false;
            ProfileDataHandler.instance().saveStats();
        }
    }

    private void isCrewNearbyCheck(MinecraftClient minecraftClient) {
        if(ProfileDataHandler.instance().profileData.crewState == CrewState.HASCREW && !Objects.equals(ScoreboardHandler.instance().crewName, "")) {
            AtomicBoolean foundCrew = new AtomicBoolean(false);
            AtomicBoolean isNearby = new AtomicBoolean(false);

            if (minecraftClient.world != null) {
                minecraftClient.world.getEntities().forEach(entity -> {
                    if (minecraftClient.player != null && entity instanceof PlayerEntity crewMember && ProfileDataHandler.instance().profileData.crewMembers.stream().anyMatch(uuid -> uuid.equals(crewMember.getUuid())) && !crewMember.getUuid().equals(minecraftClient.player.getUuid())) {
                        if (crewMember.getPos().distanceTo(minecraftClient.player.getPos()) < 10) {
                            isNearby.set(true);
                        }
                        foundCrew.set(true);
                    }
                });
            }
            this.isCrewNearby = isNearby.get();
            this.isCrewInRenderDistance = foundCrew.get();
        }
    }

    public void renderCrewChatMarker(DrawContext context, TextRenderer textRenderer, int xCoord, int yCoord) {
        FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();
        Text marker = Text.literal("ɪɴ ᴄʀᴇᴡ ᴄʜᴀᴛ").formatted(Formatting.GREEN, Formatting.ITALIC);
        if(ChatScreenHandler.instance().screenInit
                && !LabyModCompat.instance().isLabyMod
                && MinecraftClient.getInstance().currentScreen instanceof ChatScreen chatScreen
                && ProfileDataHandler.instance().profileData.isInCrewChat
                && !((ChatScreenAccessor) chatScreen).getChatField().getText().startsWith("/")
                && config.crewTracker.crewChatLocation == CrewHandler.CrewChatLocation.IN_CHAT
                && ((ChatScreenAccessor) chatScreen).getChatField().isVisible()) {
            context.drawText(textRenderer, marker, 16 + xCoord, yCoord - 1, ((int) 150f << 24) | 0xFFFFFF, true);
        }
    }

    public enum CrewState {
        NOTINITIALIZED,
        NOCREW,
        HASCREW
    }

    public enum CrewChatLocation {
        OFF,
        IN_CHAT,
        IN_NOTIFICATION
    }
}
