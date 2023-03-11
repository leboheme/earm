package com.aramirezochoa.earm.state.level.manager;

import com.aramirezochoa.earm.Manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boheme on 16/02/14.
 */
public enum LevelManager implements Manager {
    INSTANCE;

    private static final String LEVEL_ID = "levels";
    private static final String LEVELS_INFO = "info";

    private List<LevelDescription> levels;

    private List<Integer> tutorials = new ArrayList<Integer>();

    LevelManager() {
        tutorials.add(1);
        tutorials.add(11);
        tutorials.add(21);
    }

    public void init() {
        loadLevelDescriptions();
    }

    public void loadLevelDescriptions() {
        Preferences prefs = Gdx.app.getPreferences(LEVEL_ID);
        if (prefs.getString(LEVELS_INFO) == null || prefs.getString(LEVELS_INFO) == "") {
            createDefaultLevelDescriptions(prefs);
        }
        loadLevelsDescription(prefs);
    }

    private void createDefaultLevelDescriptions(Preferences prefs) {
        LevelDescription levelDescription = new LevelDescription();
        levelDescription.setFlairies(0);
        levelDescription.setNumber(1);
        levelDescription.setEnabled(true);
        List<LevelDescription> defaultInfo = new ArrayList<LevelDescription>();
        defaultInfo.add(levelDescription);
        for (int i = 2; i <= WorldType.WOODS.getMaxLevel(); i++) {
            levelDescription = new LevelDescription();
            levelDescription.setNumber(i);
            defaultInfo.add(levelDescription);
        }

        Json json = new Json(JsonWriter.OutputType.json);
        prefs.putString(LEVELS_INFO, json.toJson(defaultInfo));
        prefs.flush();
    }

    public List<LevelDescription> getLevelsDescription(WorldType worldType) {
        List<LevelDescription> toReturn = new ArrayList<LevelDescription>();
        if (levels.size() < worldType.getMaxLevel()) {
            addDefaultLevels(levels.size(), worldType.getMaxLevel());
        }
        toReturn.addAll(levels.subList(worldType.getMinLevel() - 1, worldType.getMaxLevel()));
        return toReturn;
    }

    public int getTakenFlairies(WorldType worldType) {
        int total = 0;
        for (LevelDescription levelDescription : getLevelsDescription(worldType)) {
            total += levelDescription.getFlairies();
        }
        return total;
    }

    private void addDefaultLevels(int size, int maxLevel) {
        LevelDescription levelDescription;
        for (int i = size + 1; i <= maxLevel; i++) {
            levelDescription = new LevelDescription();
            levelDescription.setNumber(i);
            levels.add(levelDescription);
        }
    }

    private void loadLevelsDescription(Preferences prefs) {
        Json json = new Json(JsonWriter.OutputType.json);
        levels = json.fromJson(ArrayList.class, prefs.getString(LEVELS_INFO));
    }

    public void notifyLevelCompleted(WorldType world, int number, int numberOfFlairies) {
        if (levels.get(number - 1).getFlairies() < numberOfFlairies) {
            levels.get(number - 1).setFlairies(numberOfFlairies);
        }
        if (levels.size() > number) {
            levels.get(number).setEnabled(true);
        } else {
            LevelDescription levelDescription = new LevelDescription();
            levelDescription.setNumber(number + 1);
            levelDescription.setEnabled(true);
            levels.add(levelDescription);
        }

        Preferences prefs = Gdx.app.getPreferences(LEVEL_ID);
        Json json = new Json(JsonWriter.OutputType.json);
        prefs.putString(LEVELS_INFO, json.toJson(levels));
        prefs.flush();
    }

    public static WorldType getWorldType(int levelNumber) {
        for (WorldType worldType : WorldType.values()) {
            if (!worldType.equals(WorldType.MAIN_MENU)) {
                if (levelNumber >= worldType.getMinLevel() && levelNumber <= worldType.getMaxLevel()) {
                    return worldType;
                }
            }
        }
        return null;
    }

    public boolean hasTutorial(int level) {
        return tutorials.contains(level);
    }

}
