package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.ActivityManager;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.options.OptionAction;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by leboheme on 13/08/2014.
 */
public class LevelPauseImpl extends LevelStateDef {

    private Table table;

    @Override
    public void init(Level level, Stage gameStage, Stage guiStage) {
        super.init(level, gameStage, guiStage);

        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        guiStage.addActor(table);

        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.BACK)).padRight(10f);
        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.RETRY)).colspan(2).padRight(10f).padLeft(10f);
        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.PLAY)).padLeft(10f);
        table.row();
        table.add(ButtonFactory.createCheckBox(ButtonSize.MID, ButtonContext.LEVEL, OptionAction.SOUND)).colspan(2).padTop(10f).padRight(10f).right();
        table.add(ButtonFactory.createCheckBox(ButtonSize.MID, ButtonContext.LEVEL, OptionAction.MUSIC)).colspan(2).padTop(10f).padLeft(10f).left();

        this.guiStage.addActor(table);
    }


    @Override
    public LevelScreenState update(float deltaTime) {
        updateAds(deltaTime);
        guiStage.act(deltaTime);

        switch (HandleInput.INSTANCE.getMenuAction()) {
            case PLAY:
                return LevelScreenState.INGAME;
            case BACK:
                ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LEVEL_SELECTOR);
                SoundManager.INSTANCE.stopTheme(level.getWorld());
                return LevelScreenState.EMPTY;
            case RETRY:
                disable();
                enableRetry();
                return LevelScreenState.PAUSE;
            case YES:
                disableRetry();
                ScreenManager.INSTANCE.setScreen(level.getNumber(), level.isTutorial());
                return LevelScreenState.INGAME;
            case NO:
                enable();
                disableRetry();
                return LevelScreenState.PAUSE;
            default:
                if (retryShowed) {
                    if (isBackJustPressed()) {
                        // case NO
                        enable();
                        disableRetry();
                        return LevelScreenState.PAUSE;
                    }
                } else {
                    if (isBackJustPressed()) {
                        // case BACK
                        ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LEVEL_SELECTOR);
                        SoundManager.INSTANCE.stopTheme(level.getWorld());
                        return LevelScreenState.EMPTY;
                    }
                }
        }
        return LevelScreenState.PAUSE;
    }

    public void enable() {
        super.enable();
        // Show this state
        table.setVisible(true);
        ActivityManager.INSTANCE.showAdTop(true);
    }

    public void disable() {
        super.disable();
        table.setVisible(false);
        ActivityManager.INSTANCE.showAdTop(false);
    }

    @Override
    public void render(float deltaTime) {
        guiStage.draw();
    }
}
