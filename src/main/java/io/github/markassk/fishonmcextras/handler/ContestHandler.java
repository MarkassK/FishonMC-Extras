package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.FishOnMCExtras;
import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import net.minecraft.text.Text;

import java.util.Objects;

public class ContestHandler {
    private static ContestHandler INSTANCE = new ContestHandler();

    public long timeLeft = 0L;
    public boolean isContest = false;
    public String type = "";
    public String location = "";
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

            if (FishOnMCExtrasConfig.getConfig().contestTracker.showFullContest) {
                this.isFilteringMessages = true;

                // Return contextual message based on refresh reason
                String contextualMessage = getContextualMessage();
                this.refreshReason = "";
                return Text.literal(contextualMessage);
            }
        }

        return message; // Return original message if no modification needed
    }

    private String getContextualMessage() {
        if (this.refreshReason.isEmpty()) {
            return "Contest stats refreshed by server";
        }

        if (this.refreshReason.startsWith("other_player_pb:")) {
            String playerName = this.refreshReason.substring("other_player_pb:".length());
            return playerName + " got a contest PB!";
        }

        switch (this.refreshReason) {
            case "personal_best":
                return "New Contest Personal Best! Leaderboard refreshed";
            case "manual_refresh":
                return "Manually refreshed Contest Stats";
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

        // Process other contest-related messages when not filtering
        if (messageText.contains("FISHING CONTEST RANKINGS (ENDED)"))
            this.hasEnded = true;
        if (messageText.startsWith("Type: "))
            this.type = messageText.substring(messageText.indexOf(": ") + 2);
        if (messageText.startsWith("Location: "))
            this.location = messageText.substring(messageText.indexOf(": ") + 2);
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
        if (this.isFilteringMessages && FishOnMCExtrasConfig.getConfig().contestTracker.showFullContest) {
            suppressMessage = true;
        }

        return suppressMessage;
    }

    private void reset() {
        this.type = "";
        this.location = "";
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

    }
}
