package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.ActivityManager;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by leboheme on 13/08/2014.
 */
public class LevelLoseImpl extends LevelStateDef {

    private Table table;

    @Override
    public void init(Level level, Stage gameStage, Stage guiStage) {
        super.init(level, gameStage, guiStage);

        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        guiStage.addActor(table);

        final TextureAtlas.AtlasRegion subMenu = MediaManager.INSTANCE.getSubMenu();
        table.add(new Actor() {
            float x = ((Constant.SCREEN_WIDTH - subMenu.getRegionWidth()) / 2);
            float y = (Constant.SCREEN_HEIGHT - subMenu.getRegionHeight()) / 2;

            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(subMenu, x, y);
            }
        });

        final TextureAtlas.AtlasRegion sadEarm = MediaManager.INSTANCE.getSadEarm();
        table.add(new Actor() {
            float x = (Constant.SCREEN_WIDTH - sadEarm.getRegionWidth()) / 2;
            float y = ((Constant.SCREEN_HEIGHT - sadEarm.getRegionHeight()) / 2) + 30;

            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(sadEarm, x, y, sadEarm.getRegionWidth(), sadEarm.getRegionHeight());
            }
        });

        table.bottom().left();
        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.LEVEL, MenuAction.RETRY)).padBottom(20f).padLeft(290f);
        table.row();
        table.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.BACK)).padBottom(10f).padLeft(10f);

        this.guiStage.addActor(table);
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
                return LevelScreenState.LOSE;
            case YES:
                disableRetry();
                ScreenManager.INSTANCE.setScreen(level.getNumber(), level.isTutorial());
                return LevelScreenState.INGAME;
            case NO:
                enable();
                disableRetry();
                return LevelScreenState.LOSE;
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
                    } else if(isEnterJustPressed()) {
                        disableRetry();
                        ScreenManager.INSTANCE.setScreen(level.getNumber(), level.isTutorial());
                        return LevelScreenState.INGAME;
                    }
                } else {
                    if (isBackJustPressed()) {
                        // case BACK
                        ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LEVEL_SELECTOR);
                        SoundManager.INSTANCE.stopTheme(level.getWorld());
                        return LevelScreenState.EMPTY;
                    } else if(isEnterJustPressed()) {
                        disable();
                        enableRetry();
                        return LevelScreenState.LOSE;
                    }
                }
        }
        return LevelScreenState.LOSE;
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
