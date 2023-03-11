package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.state.level.Level;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by leboheme on 06/08/2014.
 */
public enum LevelScreenState {

    INGAME(new LevelInGameImpl()),
    PAUSE(new LevelPauseImpl()),
    WIN(new LevelWinImpl()),
    LOSE(new LevelLoseImpl()),
    TUTORIAL(new LevelTutorialImpl()),
    EMPTY(null) {
        public void init(Level level, Stage gameStage, Stage levelStage) {
            // Nothing
        }

        public LevelScreenState update(float deltaTime) {
            return this;
        }

        public void render(float deltaTime) {
            // Nothing
        }

        public void enable() {
            // Nothing
        }

        public void disable() {
            // Nothing
        }
    };

    private final LevelStateDef levelStateDef;

    private LevelScreenState(LevelStateDef levelStateDef) {
        this.levelStateDef = levelStateDef;
    }

    public static void initStates(Level level, Stage gameStage, Stage guiStage) {
        for (LevelScreenState state : LevelScreenState.values()) {
            state.init(level, gameStage, guiStage);
        }
    }

    public void init(Level level, Stage gameStage, Stage guiStage) {
        levelStateDef.init(level, gameStage, guiStage);
    }

    public LevelScreenState update(float deltaTime) {
        return levelStateDef.update(deltaTime);
    }

    public void render(float deltaTime) {
        levelStateDef.render(deltaTime);
    }

    public void enable() {
        levelStateDef.enable();
    }

    public void disable() {
        levelStateDef.disable();
    }
}
