package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import io.github.markassk.fishonmcextras.config.TrackerContestHUDConfig;
import net.minecraft.text.Text;

import java.util.Objects;

public class ContestHandler {
    private static ContestHandler INSTANCE = new ContestHandler();

    public long timeLeft = 0L;
    public boolean isContest = false;
    public String type = "";
    public String location = "";
    public int levelLow = 0;
    public int levelHigh = 0;
    public long lastUpdated = 0L;
    public String firstName = "";
    public String firstStat = "";
    public String secondName = "";
    public String secondStat = "";
    public String thirdName = "";
    public String thirdStat = "";
    public String rank = "Unranked";
    public String rankStat = "";
    public float biggestFish = 0.0f;
    public boolean isReset = true;
    public String refreshReason = "";
    public float otherPlayerFishSize = 0.0f;
    public String otherPlayerName = "";

    private boolean hasEnded = false;
    private boolean isFilteringMessages = false;

    public static ContestHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ContestHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        long time = System.currentTimeMillis();
        long hourMs = time % (60L * 60L * 1000L);

        this.timeLeft = 60L * 60L * 1000L / 2 - (hourMs % (60L * 60L * 1000L / 2));

        if (hourMs < (60L * 60L * 1000L / 2)) {
            // Contest
            this.isContest = true;
            this.isReset = false;
        } else if (hourMs > (60L * 60L * 1000L / 2)) {
            this.isContest = false;
            this.hasEnded = true;
            if (System.currentTimeMillis() - this.lastUpdated > 15000L && hasEnded && (!Objects.equals(this.type, "")
                    || !Objects.equals(this.location, "")
                    || !Objects.equals(this.firstName, "")
                    || !Objects.equals(this.firstStat, "")
                    || !Objects.equals(this.secondName, "")
                    || !Objects.equals(this.secondStat, "")
                    || !Objects.equals(this.thirdName, "")
                    || !Objects.equals(this.thirdStat, "")
                    || !Objects.equals(this.rank, "Unranked")
                    || this.lastUpdated != 0L)) {
                this.isReset = true;
                this.hasEnded = false;
                this.reset();
            }
        }
    }

    public void onLeaveServer() {
        this.hasEnded = true;
    }

    public void setRefreshReason(String reason) {
        this.refreshReason = reason;
    }

    public Text modifyMessage(Text message) {
        String messageText = message.getString();

        // Replace "FISHING CONTEST RANKINGS" with custom message
        if (messageText.contains("FISHING CONTEST RANKINGS")) {          

            this.lastUpdated = System.currentTimeMillis();
            this.isContest = true;

            if (FishOnMCExtrasConfig.getConfig().contestTracker.suppressServerMessages) {
                this.isFilteringMessages = true;

                // Return contextual message based on refresh reason
                this.refreshReason = "";

                if (messageText.contains("FISHING CONTEST RANKINGS (ENDED)")) {
                    FishOnMCExtras.LOGGER.info("[FoE] Contest ended");
                    this.isFilteringMessages = false;
                    this.refreshReason = "contest_ended";
                    this.hasEnded = true;
                    // Return green contest ended message
                    return Text.literal("Displaying Contest Results:")
                            .formatted(net.minecraft.util.Formatting.GREEN);
                } else {
                    // Check if message contains time remaining in parentheses
                    String timeRemaining = extractTimeRemaining(messageText);
                    if (timeRemaining != null && this.refreshReason.isEmpty()) {
                        // Create fancy message with green time
                        return createFancyRefreshMessage(timeRemaining);
                    }
                }
                String contextualMessage = getContextualMessage();

                return Text.literal(contextualMessage);
            }

            
        }

        return message; // Return original message if no modification needed
    }

    private String extractTimeRemaining(String messageText) {
        // Look for pattern like "FISHING CONTEST RANKINGS (10m)" or "FISHING CONTEST RANKINGS (30s)" or similar
        if (messageText.contains("FISHING CONTEST RANKINGS (")) {
            int startIndex = messageText.indexOf("FISHING CONTEST RANKINGS (") + "FISHING CONTEST RANKINGS (".length();
            int endIndex = messageText.indexOf(")", startIndex);
            if (endIndex > startIndex) {
                String timeStr = messageText.substring(startIndex, endIndex).trim();
                // Check if it's a time format (contains 'm' for minutes or 's' for seconds and is not "ENDED")
                if ((timeStr.contains("m") || timeStr.contains("s")) && !timeStr.equals("ENDED")) {
                    return timeStr;
                }
            }
        }
        return null;
    }

    private Text createFancyRefreshMessage(String timeRemaining) {
        // Create a fancy message with green time
        return Text.literal("Contest refreshed by server. ⏱ (")
                .formatted(net.minecraft.util.Formatting.GRAY)
                .append(Text.literal(timeRemaining)
                        .formatted(net.minecraft.util.Formatting.GREEN))
                .append(Text.literal(")")); 
    }

    private String getContextualMessage() {
        if (this.refreshReason.isEmpty()) {
            return "Resfreshed by server";
        }

        if (this.refreshReason.startsWith("other_player_pb:")) {
            String[] parts = this.refreshReason.split(":", 3);
            if (parts.length >= 3) {
                String playerName = parts[1];
                float fishSize = Float.parseFloat(parts[2]);
                this.otherPlayerFishSize = fishSize;
                this.otherPlayerName = playerName;
                
                String baseMessage = playerName + " got a contest PB of " + fishSize + " lbs!";
                
                // Check ranking against current leaderboard data
                String rankingInfo = getPlayerRanking();
                if (rankingInfo != null) {
                    return baseMessage + " (" + rankingInfo + ")";
                } else {
                    return baseMessage + " (still not ranked)";
                }
            } else {
                String playerName = this.refreshReason.substring("other_player_pb:".length());
                this.otherPlayerName = playerName;
                return playerName + " got a contest PB! (still not ranked)";
            }
        }

        switch (this.refreshReason) {
            case "personal_best":
                return "New Contest Personal Best! Leaderboard refreshed";
            case "manual_refresh":
                return "Manually refreshed Contest Stats";
            case "contest_ended":
                return "Displaying Contest Results:";
            default:
                return "Contest stats refreshed: " + this.refreshReason;
        }
    }

    public boolean onReceiveMessage(Text message) {
        if (!this.isContest)
            return false;

        String messageText = message.getString();

        boolean suppressMessage = false;

        // Stop filtering when we see "You →" message
        if (messageText.startsWith("You → ")) {
            this.isFilteringMessages = false;
            this.rank = messageText.substring(messageText.indexOf(" → ") + 3, messageText.indexOf("(")).trim();
            this.rankStat = this.rank.contains("Unranked") ? ""
                    : messageText.substring(messageText.indexOf("(") + 1, messageText.indexOf(")"));

            // Parse rankStat as float, removing "lb" suffix if present
            if (!this.rankStat.isEmpty() && !this.rank.contains("Unranked")) {
                try {
                    String weightStr = this.rankStat.replace("lb", "").trim();
                    // this is the player's biggest fish for this contest
                    this.biggestFish = Float.parseFloat(weightStr);
                    FishOnMCExtras.LOGGER.info("[FoE] Parsed rank stat: {} -> {} lbs", this.rankStat, this.biggestFish);
                } catch (NumberFormatException e) {
                    FishOnMCExtras.LOGGER.warn("[FoE] Failed to parse rank stat: {}", this.rankStat);
                }
            }
            suppressMessage = true;
        }

    
        if (messageText.startsWith("Type: "))
            this.type = messageText.substring(messageText.indexOf(": ") + 2);
        if (messageText.startsWith("Location: "))
            this.location = messageText.substring(messageText.indexOf(": ") + 2);
        if (messageText.startsWith("Level: ")) {
            String levelText = messageText.substring(messageText.indexOf(": ") + 2);
            try {
                if (levelText.contains("-")) {
                    String[] parts = levelText.split("-");
                    if (parts.length == 2) {
                        this.levelLow = Integer.parseInt(parts[0].trim());
                        this.levelHigh = Integer.parseInt(parts[1].trim());
                        FishOnMCExtras.LOGGER.info("[FoE] Parsed level range: {} -> {} to {}", levelText, this.levelLow, this.levelHigh);
                    }
                }
            } catch (NumberFormatException e) {
                FishOnMCExtras.LOGGER.warn("[FoE] Failed to parse level range: {}", levelText);
            }
        }
        if (messageText.startsWith("\uF060")) {
            this.firstName = messageText.substring(messageText.indexOf("\uF060 ") + 2, messageText.indexOf(" →"));
            this.firstStat = messageText.substring(messageText.indexOf("→ ") + 2);
            FishOnMCExtras.LOGGER.info("[FoE] Parsed first place: {} → {}", this.firstName, this.firstStat);
        }
        if (messageText.startsWith("\uF061")) {
            this.secondName = messageText.substring(messageText.indexOf("\uF061 ") + 2, messageText.indexOf(" →"));
            this.secondStat = messageText.substring(messageText.indexOf("→ ") + 2);
            FishOnMCExtras.LOGGER.info("[FoE] Parsed second place: {} → {}", this.secondName, this.secondStat);
        }
        if (messageText.startsWith("\uF062")) {
            this.thirdName = messageText.substring(messageText.indexOf("\uF062 ") + 2, messageText.indexOf(" →"));
            this.thirdStat = messageText.substring(messageText.indexOf("→ ") + 2);
            FishOnMCExtras.LOGGER.info("[FoE] Parsed third place: {} → {}", this.thirdName, this.thirdStat);
        }

        // Suppress all messages while filtering is active
        if (this.isFilteringMessages && FishOnMCExtrasConfig.getConfig().contestTracker.shouldShowFullContest() && FishOnMCExtrasConfig.getConfig().contestTracker.suppressServerMessages) {
            suppressMessage = true;
        }

        return suppressMessage;
    }

    private String getPlayerRanking() {
        if (this.otherPlayerName.isEmpty() || this.otherPlayerFishSize <= 0) return null;
        
        float pbWeight = this.otherPlayerFishSize;
        
        // Parse current leaderboard weights for comparison
        float firstWeight = parseWeightFromStat(this.firstStat);
        float secondWeight = parseWeightFromStat(this.secondStat);
        float thirdWeight = parseWeightFromStat(this.thirdStat);
        
        // Check if player is already in a position and their PB doesn't change it
        if (!this.firstName.isEmpty() && this.firstName.equals(this.otherPlayerName)) {
            // Player is already 1st, check if PB improves their position
            if (pbWeight > firstWeight) {
                return "now in 1st"; // Improved their 1st place
            } else {
                return "unchanged"; // Still 1st but didn't improve
            }
        } else if (!this.secondName.isEmpty() && this.secondName.equals(this.otherPlayerName)) {
            // Player is already 2nd, check if PB improves their position
            if (pbWeight > firstWeight) {
                return "now in 1st"; // Moved up to 1st
            } else if (pbWeight > secondWeight) {
                return "now in 2nd"; // Improved their 2nd place
            } else {
                return "unchanged"; // Still 2nd but didn't improve
            }
        } else if (!this.thirdName.isEmpty() && this.thirdName.equals(this.otherPlayerName)) {
            // Player is already 3rd, check if PB improves their position
            if (pbWeight > firstWeight) {
                return "now in 1st"; // Moved up to 1st
            } else if (pbWeight > secondWeight) {
                return "now in 2nd"; // Moved up to 2nd
            } else if (pbWeight > thirdWeight) {
                return "now in 3rd"; // Improved their 3rd place
            } else {
                return "unchanged"; // Still 3rd but didn't improve
            }
        }
        
        // Player is not currently in top 3, check if PB gets them into top 3
        if (firstWeight > 0 && pbWeight > firstWeight) {
            return "now in 1st";
        } else if (secondWeight > 0 && pbWeight > secondWeight && (firstWeight <= 0 || pbWeight <= firstWeight)) {
            return "now in 2nd";
        } else if (thirdWeight > 0 && pbWeight > thirdWeight && (secondWeight <= 0 || pbWeight <= secondWeight)) {
            return "now in 3rd";
        }
        
        return null; // Player is not in top 3 and PB doesn't change that
    }
    
    private float parseWeightFromStat(String stat) {
        if (stat == null || stat.isEmpty()) return 0.0f;
        
        try {
            // Remove "lb" suffix and parse as float
            String weightStr = stat.replace("lb", "").trim();
            return Float.parseFloat(weightStr);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    private void reset() {
        this.type = "";
        this.location = "";
        this.levelLow = 0;
        this.levelHigh = 0;
        this.lastUpdated = 0L;
        this.firstName = "";
        this.firstStat = "";
        this.secondName = "";
        this.secondStat = "";
        this.thirdName = "";
        this.thirdStat = "";
        this.rank = "Unranked";
        this.biggestFish = 0.0f;
        this.isFilteringMessages = false;
        this.refreshReason = "";
        this.otherPlayerFishSize = 0.0f;
        this.otherPlayerName = "";
    }
}
