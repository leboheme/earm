package com.aramirezochoa.earm.options;

import com.aramirezochoa.earm.AbstractScreen;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.language.LanguageManager;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by boheme on 25/08/14.
 */
public class OptionsScreen extends AbstractScreen {

    private Stage stage;

    public OptionsScreen() {

    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(new InputAdapter());
        im.addProcessor(InputProcessor.class.cast(stage));
        Gdx.input.setInputProcessor(im);

        Table table = new Table();
        table.setFillParent(true);

        // Add options
        table.add(ButtonFactory.createCheckBox(ButtonSize.MID, ButtonContext.MENU, OptionAction.SOUND)).padRight(25f).spaceBottom(25f).bottom().right().padTop(200f);
        table.add(ButtonFactory.createCheckBox(ButtonSize.MID, ButtonContext.MENU, OptionAction.MUSIC)).padRight(25f).spaceBottom(25f).bottom().left();
        table.row();

        // About
        table.add(new Label(LanguageManager.INSTANCE.getString("author"), MediaManager.INSTANCE.getLabelStyle())).expand().bottom().colspan(2);
        table.row();

        //Add back button
        table.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.MENU, MenuAction.BACK)).padRight(10f).padBottom(10f).right().colspan(2);
        table.row();

        table.setBackground(new TextureRegionDrawable(MediaManager.INSTANCE.getBackground(WorldType.WOODS)));

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(1, 1, 1, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        SoundManager.INSTANCE.playTheme(WorldType.MAIN_MENU);

        switch (HandleInput.INSTANCE.getMenuAction()) {
            case BACK:
                ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.MAIN_MENU);
                break;
            default:
                if (isBackJustPressed()) {
                    ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.MAIN_MENU);
                }
                break;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
