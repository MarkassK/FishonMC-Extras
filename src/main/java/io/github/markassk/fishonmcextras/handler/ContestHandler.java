package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;

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
    public String previousPlayerPosition = "";
    public String pendingTimeRemaining = "";

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

        // Replace the final "You →" message with our contextual message instead of the header
        if (messageText.startsWith("You → ")) {
            if (FishOnMCExtrasConfig.getConfig().contestTracker.suppressServerMessages) {
                // Check for specific refresh reasons first, before checking hasEnded
                if (this.refreshReason.equals("personal_best")) {
                    Text pbMessage = getPersonalBestMessage();
                    this.refreshReason = "";
                    this.pendingTimeRemaining = "";
                    return pbMessage;
                }

                // Check for other player PB before checking hasEnded
                if (this.refreshReason.startsWith("other_player_pb:")) {
                    String contextualMessage = getContextualMessage();
                    this.refreshReason = "";
                    this.pendingTimeRemaining = "";
                    return Text.literal(contextualMessage);
                }

                // Check for contest ended
                if (this.hasEnded || this.refreshReason.equals("contest_ended")) {
                    this.refreshReason = "";
                    this.pendingTimeRemaining = "";
                    return Text.literal("Displaying Contest Results:")
                            .formatted(net.minecraft.util.Formatting.GREEN);
                }

                if (this.pendingTimeRemaining != null && !this.pendingTimeRemaining.isEmpty() && this.refreshReason.isEmpty()) {
                    Text fancy = createFancyRefreshMessage(this.pendingTimeRemaining);
                    this.pendingTimeRemaining = ""; // clear after use
                    return fancy;
                }

                String contextualMessage = getContextualMessage();
                this.refreshReason = "";
                this.pendingTimeRemaining = "";
                return Text.literal(contextualMessage);
            }
        }

        // Replace "FISHING CONTEST RANKINGS" with custom message
        if (messageText.contains("FISHING CONTEST RANKINGS")) {          
            // Header is handled (and suppressed) in onReceiveMessage now; no modification here
            // when suppression is enabled. Leave original message untouched for non-suppressed flows.
            this.lastUpdated = System.currentTimeMillis();
            this.isContest = true;

            
        }

        return message; // Return original message if no modification needed
    }

    private String extractTimeRemaining(String messageText) {
        // Look for pattern like "FISHING CONTEST RANKINGS (10m)" or "FISHING CONTEST RANKINGS (30s)" or similar
        if (messageText.contains("FISHING CONTEST RANKINGS (")) {
            try {
                int startIndex = messageText.indexOf("FISHING CONTEST RANKINGS (") + "FISHING CONTEST RANKINGS (".length();
                int endIndex = messageText.indexOf(")", startIndex);
                
                
                if (endIndex > startIndex && startIndex >= 0 && endIndex < messageText.length()) {
                    String timeStr = messageText.substring(startIndex, endIndex).trim();
                    
                    // Check if it's a time format (contains 'm' for minutes or 's' for seconds and is not "ENDED")
                    if ((timeStr.contains("m") || timeStr.contains("s")) && !timeStr.equals("ENDED")) {                      
                        return timeStr;
                    } else {
                        FishOnMCExtras.LOGGER.info("[FoE] Time string does not contain valid time format: '{}'", timeStr);
                    }
                } else {
                    FishOnMCExtras.LOGGER.warn("[FoE] Invalid indices - start: {}, end: {}, length: {}", startIndex, endIndex, messageText.length());
                }
            } catch (StringIndexOutOfBoundsException e) {
                FishOnMCExtras.LOGGER.error("[FoE] StringIndexOutOfBoundsException parsing time from contest message: '{}'", messageText, e);
            } catch (Exception e) {
                FishOnMCExtras.LOGGER.error("[FoE] Unexpected error parsing time from contest message: '{}'", messageText, e);
            }
        } else {
            FishOnMCExtras.LOGGER.debug("[FoE] Message does not contain 'FISHING CONTEST RANKINGS (': '{}'", messageText);
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
                    return baseMessage + " (still not top 3)";
                }
            } else {
                String playerName = this.refreshReason.substring("other_player_pb:".length());
                this.otherPlayerName = playerName;
                return playerName + " got a contest PB! (still not top 3)";
            }
        }

        switch (this.refreshReason) {
            case "personal_best":
                return getPersonalBestMessage().getString();
            case "manual_refresh":
                return "Manually refreshed Contest Stats";
            case "contest_ended":
                return "Displaying Contest Results:";
            default:
                return "Contest stats refreshed: " + this.refreshReason;
        }
    }

    private MutableText getPersonalBestMessage() {
        String baseMessage = "New Contest Personal Best!";
        
        // Get current player ranking information
        String rankingInfo = getCurrentPlayerRanking();
        
        // Create base message with default color
        MutableText message = Text.literal(baseMessage).formatted(net.minecraft.util.Formatting.YELLOW);
        
        if (rankingInfo != null) {
            // Add ranking info with color based on position
            MutableText rankingText = getRankingText(rankingInfo);
            return message.append(Text.literal(" (")).append(rankingText).append(Text.literal(")"));
        } else {
            return message.append(Text.literal(" (still not top 3)").formatted(net.minecraft.util.Formatting.GRAY));
        }
    }

    private MutableText getRankingText(String rankingInfo) {
        if (rankingInfo.equals("1st")) {
            return Text.literal("1st").formatted(net.minecraft.util.Formatting.GOLD);
        } else if (rankingInfo.equals("2nd")) {
            return Text.literal("2nd").formatted(net.minecraft.util.Formatting.GRAY);
        } else if (rankingInfo.equals("3rd")) {
            return Text.literal("3rd").formatted(net.minecraft.util.Formatting.RED);
        } else if (rankingInfo.equals("still not top 3")) {
            return Text.literal("still not top 3").formatted(net.minecraft.util.Formatting.GRAY);
        } else if (rankingInfo.contains(" - unchanged")) {
            // Handle "1st - unchanged", "2nd - unchanged", etc.
            String rank = rankingInfo.substring(0, rankingInfo.indexOf(" - unchanged"));
            MutableText rankText;
            if (rank.equals("1st")) {
                rankText = Text.literal("1st").formatted(net.minecraft.util.Formatting.GOLD);
            } else if (rank.equals("2nd")) {
                rankText = Text.literal("2nd").formatted(net.minecraft.util.Formatting.GRAY);
            } else if (rank.equals("3rd")) {
                rankText = Text.literal("3rd").formatted(net.minecraft.util.Formatting.RED);
            } else {
                rankText = Text.literal(rank).formatted(net.minecraft.util.Formatting.GRAY);
            }
            return rankText.append(Text.literal(" - unchanged").formatted(net.minecraft.util.Formatting.WHITE));
        } else if (rankingInfo.equals("unchanged")) {
            return Text.literal("unchanged").formatted(net.minecraft.util.Formatting.WHITE);
        } else {
            return Text.literal(rankingInfo).formatted(net.minecraft.util.Formatting.GRAY);
        }
    }

    public boolean onReceiveMessage(Text message) {
        String messageText = message.getString();

        // Suppress the contest header and prime state for replacement at the final line
        if (messageText.contains("FISHING CONTEST RANKINGS")) {
            this.lastUpdated = System.currentTimeMillis();
            this.isContest = true;
            if (FishOnMCExtrasConfig.getConfig().contestTracker.suppressServerMessages) {
                this.isFilteringMessages = true;

                if (messageText.contains("FISHING CONTEST RANKINGS (ENDED)")) {
                    FishOnMCExtras.LOGGER.info("[FoE] Contest ended");
                    this.refreshReason = "contest_ended";
                    this.hasEnded = true;
                } else {
                    String timeRemaining = extractTimeRemaining(messageText);
                    if (timeRemaining != null) {
                        this.pendingTimeRemaining = timeRemaining;
                    }
                }
                return true; // suppress header
            }
        }

        if (!this.isContest)
            return false;

        boolean suppressMessage = false;

        // Stop filtering when we see "You →" message
        if (messageText.startsWith("You → ")) {
            this.isFilteringMessages = false;
            String newRank = messageText.substring(messageText.indexOf(" → ") + 3, messageText.indexOf("(")).trim();
            String newRankStat = newRank.contains("Unranked") ? ""
                    : messageText.substring(messageText.indexOf("(") + 1, messageText.indexOf(")"));
            
            // Store previous position before updating to new position
            if (!this.rank.equals("Unranked") && !this.rank.equals(newRank)) {
                this.previousPlayerPosition = this.rank;
            }
            
            this.rank = newRank;
            this.rankStat = newRankStat;

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
            // Allow this message to pass so we can replace it in modifyMessage()
            suppressMessage = false;
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

        // Suppress all messages while filtering is active, but not when contest has ended
        if (this.isFilteringMessages && FishOnMCExtrasConfig.getConfig().contestTracker.shouldShowFullContest() && FishOnMCExtrasConfig.getConfig().contestTracker.suppressServerMessages && !this.hasEnded) {
            suppressMessage = true;
        }

        return suppressMessage;
    }

    private String getPlayerRanking() {
        if (this.otherPlayerName.isEmpty()) return null;
        
        // Check if player is in the top 3 leaderboard positions we already parsed
        if (!this.firstName.isEmpty() && this.firstName.equals(this.otherPlayerName)) {
            return "1st";
        } else if (!this.secondName.isEmpty() && this.secondName.equals(this.otherPlayerName)) {
            return "2nd";
        } else if (!this.thirdName.isEmpty() && this.thirdName.equals(this.otherPlayerName)) {
            return "3rd";
        }
        
        // Player is not in top 3, so they're unranked
        return "still not top 3";
    }
    

    private String getCurrentPlayerRanking() {
        // Use the rank we already parsed from the "You →" message
        if (this.rank == null || this.rank.isEmpty() || this.rank.equals("Unranked")) {
            return "still not top 3";
        }

        // Extract numeric part from ranks like "#1", "#2", "#3"
        String digits = this.rank.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return "still not top 3";
        }

        String ordinal;
        switch (digits) {
            case "1": ordinal = "1st"; break;
            case "2": ordinal = "2nd"; break;
            case "3": ordinal = "3rd"; break;
            default: ordinal = digits + "th"; break;
        }

        // Compare with previous position to detect if it changed
        if (this.previousPlayerPosition != null && !this.previousPlayerPosition.isEmpty()) {
            String previousDigits = this.previousPlayerPosition.replaceAll("[^0-9]", "");
            if (!previousDigits.isEmpty() && previousDigits.equals(digits)) {
                return ordinal + " - unchanged";
            }
        }
        
        // Update the previous position for next time
        this.previousPlayerPosition = this.rank;
        return ordinal;
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
        this.previousPlayerPosition = "";
    }
}
