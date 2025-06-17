package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreboardHandler {
    private static ScoreboardHandler INSTANCE = new ScoreboardHandler();

    public String playerName = "";
    public int level = 0;
    public float percentLevel = 0;
    public String wallet = "";
    public String credits = "";
    public String catches = "";
    public String catchRate = "";
    public String crewName = "";
    public String crewLevel = "0";
    public String locationMin = "";
    public String locationMax = "";
    public boolean isCrewNearby = false;
    public boolean noScoreBoard = false;

    private List<Text> prevList = new ArrayList<>();

    public static ScoreboardHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ScoreboardHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient client) {
        try {
            List<Text> textList = new ArrayList<>();

            Scoreboard scoreboard = Objects.requireNonNull(client.player).getScoreboard();
            ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);

            if(objective != null) {
                objective.getScoreboard().getTeams().forEach(team -> {
                    if(!Objects.equals(team.getPrefix().getString(), "")) {
                        textList.add(team.getPrefix());
                    }
                });
            }

            noScoreBoard = textList.isEmpty();
            if(!noScoreBoard && !prevList.equals(textList)) {
                prevList = textList;
                extractData(textList, client.player);
            }
        } catch (NullPointerException e) {
            FishOnMCExtras.LOGGER.error(e.getMessage());
        }
    }

    private void extractData(List<Text> data, PlayerEntity player) {
        this.playerName = player.getName().getString();
        this.level = player.experienceLevel;
        this.percentLevel = player.experienceProgress;
        data.forEach(text -> {
            System.out.println(text.getString());
            if(text.getString().contains("ᴡᴀʟʟᴇᴛ")) wallet = text.getString().substring(text.getString().indexOf("$") + 1);
            if(text.getString().contains("ᴄʀᴇᴅɪᴛꜱ")) credits = text.getString().substring(text.getString().indexOf("\uF00C") + 1);
            if(text.getString().contains("ᴄᴀᴛᴄʜᴇꜱ")) catches = text.getString().substring(text.getString().indexOf(":") + 2);
            if(text.getString().contains("ᴄᴀᴛᴄʜ ʀᴀᴛᴇ")) catchRate = text.getString().substring(text.getString().indexOf(":") + 2);
            if(text.getString().contains("ᴄʀᴇᴡ:")) crewName = text.getString().substring(text.getString().indexOf("[") + 1, text.getString().lastIndexOf("]"));
            if(text.getString().contains("┠ ʟᴇᴠᴇʟ")) crewLevel = text.getString().substring(text.getString().indexOf("[") + 1, text.getString().lastIndexOf("]"));
            if(text.getString().contains("ᴄʀᴇᴡ ɴᴇᴀʀʙʏ")) isCrewNearby = text.getString().contains("✔");
            if(text.getString().contains("┠ ʟᴏᴄᴀᴛɪᴏɴ") && !text.getString().contains("---")) locationMin = text.getString().substring(text.getString().indexOf(":") + 2, text.getString().lastIndexOf("/"));
            if(text.getString().contains("┠ ʟᴏᴄᴀᴛɪᴏɴ") && !text.getString().contains("---")) locationMax = text.getString().substring(text.getString().indexOf("/") + 1).trim();
        });
    }
}
