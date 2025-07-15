package io.github.markassk.fishonmcextras.handler;

import io.github.markassk.fishonmcextras.config.FishOnMCExtrasConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.text.Text;

public class TimerHandler {
    private static TimerHandler INSTANCE = new TimerHandler();
    private final FishOnMCExtrasConfig config = FishOnMCExtrasConfig.getConfig();

    public long baitShopOffset = 60 * 60 * 1000;
    public final long baitShopTotalTime = 60 * 60 * 1000 * 4; // 4h
    public long baitShopTimer = 0L;
    public long baitShopAlertTime = 0L;

    public final long contestTotalTime =  60 * 60 * 1000; // 1h
    public long contestTimer = 0L;

    public final long contestEndTotalTime =  60 * 60 * 1000; // 1h
    public long contestEndTimer = 0L;

    public static TimerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new TimerHandler();
        }
        return INSTANCE;
    }

    public void tick() {
        long currentTime = System.currentTimeMillis();

        long baitShopTime = baitShopTotalTime;
        this.baitShopTimer = baitShopTime - ((currentTime + baitShopOffset + config.timerTracker.hiddenOffsetBaitShop) % baitShopTime);

        long contestOffset = 0L;
        long contestTime = contestTotalTime;
        this.contestTimer = contestTime - ((currentTime + contestOffset) % contestTime);

        long contestEndOffset = 60 * 30 * 1000L; // 30m
        long contestEndTime = contestEndTotalTime;
        this.contestEndTimer = contestEndTime - ((currentTime + contestEndOffset) % contestEndTime);
    }

    public void onReceiveMessage(Text text) {
        if(text.getString().startsWith("TACKLE SHOP »") && text.getString().contains("Tackle items restocked!")) {
            baitShopAlertTime = System.currentTimeMillis();

            if(baitShopTimer >= 1000L && baitShopTimer <= 600000L) {
                config.timerTracker.hiddenOffsetBaitShop = baitShopTimer - 1000L;
                AutoConfig.getConfigHolder(FishOnMCExtrasConfig.class).save();
            }
        }
    }
}
