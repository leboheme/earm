package com.aramirezochoa.earm.state.menu;

import com.aramirezochoa.earm.AbstractScreen;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.manager.LevelDescription;
import com.aramirezochoa.earm.state.level.manager.LevelManager;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.List;

/**
 * Created by leboheme on 10/06/2014.
 */
public class LevelSelectorScreen extends AbstractScreen {

    private Stage stage;
    private Table worldTable;
    private Table levelTable;
    private WorldType worldSelected = WorldType.MAIN_MENU;

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(new InputAdapter());
        im.addProcessor(InputProcessor.class.cast(stage));
        Gdx.input.setInputProcessor(im);

        createWorldsButtons();
        if (!WorldType.MAIN_MENU.equals(worldSelected)) {
            worldTable.setVisible(false);
            createLevelsButtons();
            levelTable.setVisible(true);
        }
    }

    private void createWorldsButtons() {
        Table scrollTable = new Table();

        List<WorldType> worlds = WorldType.getWorlds();
        TextButton.TextButtonStyle closed = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, "CLOSED", ButtonSize.LIT);
        for (WorldType world : worlds) {
            TextButton.TextButtonStyle buttonStyle;
            if (world.isOpened()) {
                buttonStyle = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, world.name(), ButtonSize.LIT);
            } else {
                buttonStyle = closed;
            }
            TextButton button = ButtonFactory.createWorldSelectButton(buttonStyle, world);
            button.getLabel().setAlignment(Align.top);
            scrollTable.add(button).spaceLeft(30f).spaceBottom(25f).center();
        }

        scrollTable.row();
        ScrollPane scroll = new ScrollPane(scrollTable);

        worldTable = new Table();
        worldTable.setFillParent(true);

        worldTable.add(scroll).fill().expand().pad(25f).padLeft(30f);

        //Add back button
        worldTable.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.MENU, MenuAction.BACK)).padRight(10f).padBottom(10f).bottom().right();
        worldTable.row();

        worldTable.setBackground(new TextureRegionDrawable(MediaManager.INSTANCE.getBackground(worldSelected)));
        stage.addActor(worldTable);
    }

    private void createLevelsButtons() {
        Table scrollTable = new Table();

        List<LevelDescription> levels = LevelManager.INSTANCE.getLevelsDescription(worldSelected);
        int i = 1;
        // Done here to avoid creating a lot of styles
        TextButton.TextButtonStyle enabled[] = new TextButton.TextButtonStyle[4];
        enabled[0] = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, "SELECT_0", ButtonSize.MID);
        enabled[1] = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, "SELECT_1", ButtonSize.MID);
        enabled[2] = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, "SELECT_2", ButtonSize.MID);
        enabled[3] = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, "SELECT_3", ButtonSize.MID);
        TextButton.TextButtonStyle disabled = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.MENU, "LOCK", ButtonSize.MID);
        for (LevelDescription levelDescription : levels) {
            TextButton button = ButtonFactory.createLevelSelectButton(levelDescription.isEnabled() ? enabled[levelDescription.getFlairies()] : disabled, levelDescription.getNumber(), levelDescription.isEnabled());
            button.getLabel().setAlignment(Align.top);
            scrollTable.add(button).spaceLeft(30f).spaceBottom(25f).center();
            if (i % 5 == 0) {
                scrollTable.row();
            }
            i++;
        }
        scrollTable.row();
        ScrollPane scroll = new ScrollPane(scrollTable);

        levelTable = new Table();
        levelTable.setFillParent(true);

        levelTable.add(scroll).fill().expand().pad(25f);

        //Add back button
        levelTable.add(ButtonFactory.createMenuButton(ButtonSize.MID, ButtonContext.MENU, MenuAction.BACK)).padRight(10f).padBottom(10f).bottom().right();
        levelTable.row();

        levelTable.setBackground(new TextureRegionDrawable(MediaManager.INSTANCE.getBackground(worldSelected)));
        stage.addActor(levelTable);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(1, 1, 1, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // We are seeing world selection
        if (WorldType.MAIN_MENU.equals(worldSelected)) {
            updateWorldSelection();
        } else {
            updateLevelSelection();
        }
    }

    private void updateWorldSelection() {
        SoundManager.INSTANCE.playTheme(WorldType.MAIN_MENU);
        worldSelected = HandleInput.INSTANCE.getWorldSelected();
        // Lets go to see level details
        if (!WorldType.MAIN_MENU.equals(worldSelected)) {
            worldTable.setVisible(false);
            createLevelsButtons();
            levelTable.setVisible(true);
        } else {
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
    }

    private void updateLevelSelection() {
        int levelSelected = HandleInput.INSTANCE.getLevelSelected();
        if (levelSelected > 0) {
            ScreenManager.INSTANCE.setScreen(levelSelected, true);
        } else {
            SoundManager.INSTANCE.playTheme(WorldType.MAIN_MENU);
        }
        switch (HandleInput.INSTANCE.getMenuAction()) {
            case BACK:
                worldSelected = WorldType.MAIN_MENU;
                worldTable.setVisible(true);
                levelTable.setVisible(false);
                break;
            default:
                if (isBackJustPressed()) {
                    worldSelected = WorldType.MAIN_MENU;
                    worldTable.setVisible(true);
                    levelTable.setVisible(false);
                }
                break;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
