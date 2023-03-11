package com.aramirezochoa.earm.state.level.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leboheme on 02/06/2014.
 */
public enum WorldType {
    WOODS("woods", true, 1, 25),
    SNOW("snow", true, 26, 50),
    JUNGLE("jungle", true, 51, 75)/*,
    DESERT("desert", false, 76, 100),
    WATER("water", false, 101, 125),
    CLOUDS("clouds", false, 126, 150)*/,
    MAIN_MENU("main menu", false, 0, 0);

    private final String description;
    private final boolean opened;
    private final int minLevel;
    private final int maxLevel;

    WorldType(String description, boolean opened, int minLevel, int maxLevel) {
        this.description = description;
        this.opened = opened;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOpened() {
        return opened;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getTotalLevels() {
        return maxLevel - minLevel + 1;
    }

    public static List<WorldType> getWorlds() {
        List<WorldType> worlds = new ArrayList<WorldType>();
        for (WorldType type : values()) {
            if (!type.equals(MAIN_MENU)) {
                worlds.add(type);
            }
        }
        return worlds;
    }
}
