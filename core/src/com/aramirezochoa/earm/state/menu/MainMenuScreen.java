package com.aramirezochoa.earm.state.menu;

import com.aramirezochoa.earm.AbstractScreen;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by boheme on 11/02/14.
 */
public class MainMenuScreen extends AbstractScreen {

    private Stage stage;
    boolean wantsToExit = false;

    public MainMenuScreen() {

    }

    @Override
    public void show() {
        wantsToExit = false;

        stage = new Stage(new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.MENU, MenuAction.EXIT)).expand().bottom().left().padLeft(10f).padBottom(10f);
        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.MENU, MenuAction.OPTIONS)).bottom().right().padRight(10f).padBottom(10f);

        table.setBackground(new TextureRegionDrawable(MediaManager.INSTANCE.getBackground(WorldType.MAIN_MENU)));
        stage.addActor(table);

        Table table2 = new Table();
        table2.setFillParent(true);
        table2.add(ButtonFactory.createMenuButton(ButtonSize.BIG, ButtonContext.MENU, MenuAction.PLAY)).center().expand();
        stage.addActor(table2);

        final TextureAtlas.AtlasRegion earmAtlasRegion = MediaManager.INSTANCE.getMenuEarm();
        Actor earm = new Actor() {
            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(earmAtlasRegion, 150, 150);
            }
        };
        stage.addActor(earm);

        final TextureAtlas.AtlasRegion titleAtlasRegion = MediaManager.INSTANCE.getMenuTitle();
        final float posX = (Constant.SCREEN_WIDTH - titleAtlasRegion.getRegionWidth()) / 2;
        Actor title = new Actor() {
            @Override
            public void draw(Batch batch, float alpha) {
                batch.draw(titleAtlasRegion, posX, 20);
            }
        };
        stage.addActor(title);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(1, 1, 1, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        SoundManager.INSTANCE.playTheme(WorldType.MAIN_MENU);

        switch (HandleInput.INSTANCE.getMenuAction()) {
            case PLAY:
                ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LEVEL_SELECTOR);
                break;
            case OPTIONS:
                ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.OPTIONS_MENU);
                break;
            case EXIT:
                ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.EXIT);
                break;
            default:
                if (isBackJustPressed()) {
                    if (wantsToExit) {
                        ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.EXIT);
                    } else {
                        wantsToExit = true;
                    }
                }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
