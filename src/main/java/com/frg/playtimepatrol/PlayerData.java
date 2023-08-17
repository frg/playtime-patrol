package com.frg.playtimepatrol;

public class PlayerData {
    private static final long MAX_ACTIVE_TIME = 4 * 60 * 60 * 20;  // 4 hours in ticks
    private long activeTime = 0;  // Active time in ticks

    public void incrementActiveTime() {
        activeTime += 1;
    }

    public boolean hasExceededLimit() {
        return activeTime > MAX_ACTIVE_TIME;
    }
}
