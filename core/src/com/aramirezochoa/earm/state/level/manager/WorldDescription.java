package com.aramirezochoa.earm.state.level.manager;

import java.util.List;

/**
 * Created by leboheme on 24/08/2014.
 */
public class WorldDescription {

    List<LevelDescription> levels;

    public void setLevels(List<LevelDescription> levels) {
        this.levels = levels;
    }

    public List<LevelDescription> getLevels() {
        return levels;
    }

}
