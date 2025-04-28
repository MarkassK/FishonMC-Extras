package io.github.markassk.fishonmcextras.FOMC;


public class Quests {
    public final Constant LOCATION;
    public final int SLOT;
    public final String FISH;
    public int PROGRESS;
    public final int NEEDED;

    public Quests(Constant location, int slot, String fish, int progress, int needed){
        this.LOCATION = location;
        this.SLOT = slot;
        this.FISH = fish;
        this.PROGRESS = progress;
        this.NEEDED = needed;
    }

    public Constant getLocation() {
        return LOCATION;
    }

    public int getSlot() {
        return SLOT;
    }

    public String getFishType() {
        return FISH;
    }

    public int getProgress() {
        return PROGRESS;
    }

    public int getNeeded() {
        return NEEDED;
    }

    public void incrementProgress(){
        if (PROGRESS < NEEDED){
            PROGRESS++;
        }
    }
}
