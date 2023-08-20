package com.frg.playtimepatrol;

import me.shedaniel.autoconfig.ConfigData;

@Config(name = "modid")
public class ModConfig implements ConfigData {

    enum CalendarPeriod {
        DAY,
        WEEK,
        MONTH
    }

    private final String PlaytimeAllowance = "4h0m0s";
    private final CalendarPeriod PlaytimeAllowancePeriod = CalendarPeriod.DAY;
}
