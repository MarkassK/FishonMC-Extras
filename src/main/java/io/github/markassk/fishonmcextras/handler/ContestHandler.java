package io.github.markassk.fishonmcextras.handler;

import net.minecraft.text.Text;

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
    public boolean isReset = true;

    private boolean hasEnded = false;

    public static ContestHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new ContestHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        long time = System.currentTimeMillis();
        long hourMs = time % (60L*60L*1000L);

        this.timeLeft = 60L*60L*1000L / 2 - (hourMs % (60L*60L*1000L / 2));

        if(hourMs < (60L*60L*1000L / 2)) {
            // Contest
            this.isContest = true;
            this.isReset = false;
        } else if (hourMs > (60L*60L*1000L / 2)){
            this.isContest = false;
            if(System.currentTimeMillis() - this.lastUpdated > 15000L && !isReset && hasEnded) {
                this.isReset = true;
                this.hasEnded = false;
                this.reset();
            }
        }
    }

    public void onReceiveMessage(Text message) {
        if(message.getString().contains("FISHING CONTEST RANKINGS")) {
            this.lastUpdated = System.currentTimeMillis();
            this.isContest = true;
        } // You → #7 (2.55lb) (out of 7)
        if(message.getString().contains("FISHING CONTEST RANKINGS (ENDED)")) this.hasEnded = true;
        if(message.getString().startsWith("Type: ")) this.type = message.getString().substring(message.getString().indexOf(": ") + 2);
        if(message.getString().startsWith("Location: ")) this.location = message.getString().substring(message.getString().indexOf(": ") + 2);
        if(message.getString().startsWith("\uF060")) {
            this.firstName = message.getString().substring(message.getString().indexOf("\uF060 ") + 2, message.getString().indexOf(" →"));
            this.firstStat = message.getString().substring(message.getString().indexOf("→ ") + 2);
        }
        if(message.getString().startsWith("\uF061")) {
            this.secondName = message.getString().substring(message.getString().indexOf("\uF061 ") + 2, message.getString().indexOf(" →"));
            this.secondStat = message.getString().substring(message.getString().indexOf("→ ") + 2);
        }
        if(message.getString().startsWith("\uF062")) {
            this.thirdName = message.getString().substring(message.getString().indexOf("\uF062 ") + 2, message.getString().indexOf(" →"));
            this.thirdStat = message.getString().substring(message.getString().indexOf("→ ") + 2);
        }
        if(message.getString().startsWith("You → ")) {
            this.rank = message.getString().substring(message.getString().indexOf(" → ") + 3, message.getString().indexOf("(")).trim();
            this.rankStat = this.rank.contains("Unranked") ? "" : message.getString().substring(message.getString().indexOf("(") + 1, message.getString().indexOf(")"));
        }
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
    }
}
