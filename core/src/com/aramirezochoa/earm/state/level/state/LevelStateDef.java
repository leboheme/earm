package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.ActivityManager;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.language.LanguageManager;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by leboheme on 13/08/2014.
 */
public abstract class LevelStateDef {

    protected Stage gameStage;
    protected Stage guiStage;
    protected Level level;
    protected Table retryTable;
    protected boolean active = false;

    protected boolean retryShowed = false;
    private int tryCounter = 0;
    private float timeCounter = 0;

    public void init(Level level, Stage gameStage, Stage guiStage) {
        this.gameStage = gameStage;
        this.guiStage = guiStage;
        this.level = level;
        active = false;

        retryTable = new Table();
        retryTable.setVisible(false);
        retryTable.setFillParent(true);
        guiStage.addActor(retryTable);

        retryTable.add(new Label(LanguageManager.INSTANCE.getString("retry"), MediaManager.INSTANCE.getLabelStyle())).colspan(2).padBottom(25f).row();
        retryTable.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.YES)).spaceRight(50f);
        retryTable.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.NO)).spaceLeft(50f);

        this.guiStage.addActor(retryTable);
    }

    protected void enableRetry() {
        timeCounter = 0;
        retryShowed = true;
        retryTable.setVisible(true);
        tryCounter++;
    }

    protected void disableRetry() {
        retryShowed = false;
        retryTable.setVisible(false);
        ActivityManager.INSTANCE.showAdFull(false);
    }

    public void enable() {
        active = true;
    }

    public void disable() {
        active = false;
    }

    public abstract LevelScreenState update(float deltaTime);

    protected void updateAds(float deltaTime) {
        timeCounter += deltaTime;
        if (retryShowed && tryCounter >= Constant.RETRY_LIMIT_TO_SHOW_AD && timeCounter > Constant.TIME_TO_SHOW) {
            tryCounter = 0;
            timeCounter = 0;
            ActivityManager.INSTANCE.showAdFull(true);
        }
    }

    public LevelScreenState update(float deltaTime, LevelScreenState parent) {
        throw new UnsupportedOperationException();
    }

    public abstract void render(float deltaTime);

    protected boolean isBackJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK);
    }

    protected boolean isEnterJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
    }
}
