package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.ActivityManager;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.language.LanguageManager;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * Created by leboheme on 13/08/2014.
 */
public class LevelWinImpl extends LevelStateDef {

    private Table table;

    private TextureAtlas.AtlasRegion scores[] = new TextureAtlas.AtlasRegion[4];

    @Override
    public void init(Level level, Stage gameStage, Stage guiStage) {
        super.init(level, gameStage, guiStage);

        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        guiStage.addActor(table);

        final TextureAtlas.AtlasRegion subMenu = MediaManager.INSTANCE.getSubMenu();
        table.add(new Actor() {
            float x = ((Constant.SCREEN_WIDTH - subMenu.getRegionWidth()) / 2) + 5f;
            float y = (Constant.SCREEN_HEIGHT - subMenu.getRegionHeight()) / 2;

            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(subMenu, x, y);
            }
        });

        // Labels
        Label levelLabel = new Label(LanguageManager.INSTANCE.getString(level.isTutorial() ? "tutorial" : "level") + level.getNumber(), MediaManager.INSTANCE.getLabelStyle());
        levelLabel.setX((Constant.SCREEN_WIDTH - levelLabel.getWidth()) / 2);
        levelLabel.setY(280);
        levelLabel.setAlignment(Align.center);
        table.addActor(levelLabel);
        Label clearedLabel = new Label(LanguageManager.INSTANCE.getString("cleared"), MediaManager.INSTANCE.getLabelStyle());
        clearedLabel.setX((Constant.SCREEN_WIDTH - clearedLabel.getWidth()) / 2);
        clearedLabel.setY(230);
        clearedLabel.setAlignment(Align.center);
        table.addActor(clearedLabel);

        table.bottom().left();
        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.NEXT)).colspan(2).padBottom(20f).padLeft(299f);
        table.row();
        table.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.BACK)).padBottom(10f).padLeft(10f);
        table.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.RETRY)).padBottom(10f).padLeft(10f).left();

        scores[0] = MediaManager.INSTANCE.getWinScore(0);
        scores[1] = MediaManager.INSTANCE.getWinScore(1);
        scores[2] = MediaManager.INSTANCE.getWinScore(2);
        scores[3] = MediaManager.INSTANCE.getWinScore(3);
        table.add(new Actor() {
            float x = ((Constant.SCREEN_WIDTH - scores[0].getRegionWidth()) / 2);
            float y = 190;

            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(getScore(), x, y);
            }
        });
        this.guiStage.addActor(table);
    }

    public TextureAtlas.AtlasRegion getScore() {
        return scores[level.getFlairyTaken()];
    }

    @Override
    public LevelScreenState update(float deltaTime) {
        updateAds(deltaTime);
        gameStage.act(deltaTime);
        guiStage.act(deltaTime);

        switch (HandleInput.INSTANCE.getMenuAction()) {
            case RETRY:
                disable();
                enableRetry();
                return LevelScreenState.WIN;
            case YES:
                disableRetry();
                ScreenManager.INSTANCE.setScreen(level.getNumber(), level.isTutorial());
                return LevelScreenState.INGAME;
            case NO:
                enable();
                disableRetry();
                return LevelScreenState.WIN;
            case NEXT:
                if (level.isTutorial()) {
                    ScreenManager.INSTANCE.setScreen(level.getNumber(), false);
                } else {
                    ScreenManager.INSTANCE.setScreen(level.getNumber() + 1, true);
                }
                return LevelScreenState.INGAME;
            case BACK:
                ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LEVEL_SELECTOR);
                SoundManager.INSTANCE.stopTheme(level.getWorld());
                return LevelScreenState.EMPTY;
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
        return LevelScreenState.WIN;
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
