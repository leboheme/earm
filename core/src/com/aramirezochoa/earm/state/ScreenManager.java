package com.aramirezochoa.earm.state;

import com.aramirezochoa.earm.AbstractScreen;
import com.aramirezochoa.earm.EarmGame;
import com.aramirezochoa.earm.options.OptionsScreen;
import com.aramirezochoa.earm.state.level.LevelScreen;
import com.aramirezochoa.earm.state.level.manager.LevelManager;
import com.aramirezochoa.earm.state.loading.LoadingScreen;
import com.aramirezochoa.earm.state.menu.LevelSelectorScreen;
import com.aramirezochoa.earm.state.menu.MainMenuScreen;
import com.aramirezochoa.earm.state.splash.SplashScreen;
import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by boheme on 15/02/14.
 */
public enum ScreenManager {
    INSTANCE;

    EarmGame game;
    private Map<ScreenType, AbstractScreen> states;
    private ScreenType currentState;

    public enum ScreenType {
        SPLASH,
        LOADING_SCREEN,
        MAIN_MENU,
        OPTIONS_MENU,
        LEVEL_SELECTOR,
        LEVEL,
        EXIT;
    }

    public void init(EarmGame game) {
        this.game = game;
        states = new HashMap<ScreenType, AbstractScreen>();
        states.put(ScreenType.SPLASH, new SplashScreen());
        states.put(ScreenType.LOADING_SCREEN, new LoadingScreen());
        states.put(ScreenType.MAIN_MENU, new MainMenuScreen());
        states.put(ScreenType.OPTIONS_MENU, new OptionsScreen());
        states.put(ScreenType.LEVEL_SELECTOR, new LevelSelectorScreen());
        states.put(ScreenType.LEVEL, new LevelScreen());
        states.put(ScreenType.EXIT, new UndefinedState());

        // Init with main menu
        currentState = ScreenType.SPLASH;
        changeTo(currentState, 0, false);
    }

    public void setScreen(ScreenType screenType) {
        if (ScreenType.LEVEL.equals(screenType)) {
            changeTo(ScreenType.LEVEL_SELECTOR, 0, false);
        } else {
            changeTo(screenType, 0, false);
        }
    }

    public void setScreen(int level, boolean tryTutorial) {
        if (tryTutorial && LevelManager.INSTANCE.hasTutorial(level)) {
            changeTo(ScreenType.LEVEL, level, true);
        } else {
            changeTo(ScreenType.LEVEL, level, false);
        }
    }

    private void changeTo(ScreenType newScreenType, int level, boolean doTutorial) {
        if (ScreenType.EXIT.equals(newScreenType)) {
            Gdx.app.exit();
        }
        ScreenType lastScreen = currentState;
        try {
            setScreen(newScreenType, level, doTutorial);
        } catch (Exception e) {
            Gdx.app.error("StateManager", "Could not set next screen " + newScreenType.name() + " - " + level, e);
            setScreen(lastScreen);
        }
    }

    private void setScreen(ScreenType screen, int level, boolean doTutorial) {
        game.setScreen(states.get(screen));
        currentState = screen;
        states.get(currentState).init(level, doTutorial);
    }
}
