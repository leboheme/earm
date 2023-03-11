package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.language.LanguageManager;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by boheme on 7/10/14.
 */
public class LevelTutorialImpl extends LevelStateDef {

    private Table table;
    private Label messageLabel;

    @Override
    public void init(Level level, Stage gameStage, Stage guiStage) {
        super.init(level, gameStage, guiStage);

        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        guiStage.addActor(table);

        messageLabel = new Label("", MediaManager.INSTANCE.getTutorialLabelStyle());
        table.add(messageLabel).center();
        table.row();
        table.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.NEXT)).padBottom(10f).padLeft(10f);

        this.guiStage.addActor(table);
    }

    @Override
    public LevelScreenState update(float deltaTime) {
        guiStage.act(deltaTime);
        switch (HandleInput.INSTANCE.getMenuAction()) {
            case NEXT:
                return LevelScreenState.INGAME;
        }
        return LevelScreenState.TUTORIAL;
    }

    public void enable() {
        super.enable();
        // Show this state
        table.setVisible(true);
        messageLabel.setText(LanguageManager.INSTANCE.getString(level.getNextAction().getAction()));
    }

    public void disable() {
        super.disable();
        table.setVisible(false);
    }

    @Override
    public void render(float deltaTime) {
        guiStage.draw();
    }

}
