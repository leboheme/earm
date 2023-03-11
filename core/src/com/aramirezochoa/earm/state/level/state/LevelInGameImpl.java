package com.aramirezochoa.earm.state.level.state;

import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonFactory;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.aramirezochoa.earm.state.level.manager.LevelManager;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by leboheme on 13/08/2014.
 */
public class LevelInGameImpl extends LevelStateDef {

    private TextureAtlas.AtlasRegion scoreFairy;

    private Table actionTable;
    private Table auxTable;

    @Override
    public void init(Level level, Stage gameStage, Stage guiStage) {
        super.init(level, gameStage, guiStage);

        actionTable = new Table();
        actionTable.setVisible(false);
        actionTable.setFillParent(true);
        actionTable.bottom().right();

        Map<AvatarCommand, AtomicInteger> actions = level.getActions();
        for (AvatarCommand avatarCommand : actions.keySet()) {
            actionTable.add(ButtonFactory.createCommandButton(actions.get(avatarCommand), level, avatarCommand)).padRight(15f).padBottom(15f);
        }
        this.guiStage.addActor(actionTable);

        auxTable = new Table();
        auxTable.setFillParent(true);
        if (level.isTutorial()) {
            auxTable.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.NEXT)).expand().top().left().padLeft(10f).padTop(10f);
        } else {
            auxTable.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.RETRY)).expand().top().left().padLeft(10f).padTop(10f);
            auxTable.row();
            auxTable.add(ButtonFactory.createMenuButton(ButtonSize.LIT, ButtonContext.LEVEL, MenuAction.PAUSE)).bottom().left().padLeft(10f).padBottom(10f);
        }

        scoreFairy = MediaManager.INSTANCE.getGuiFlairy();
        auxTable.add(new Actor() {
            @Override
            public void draw(Batch batch, float alpha) {
                drawScore(batch);
            }
        });

        this.guiStage.addActor(auxTable);
    }

    public void drawScore(Batch batch) {
        batch.draw(scoreFairy, 690, 425);
        MediaManager.INSTANCE.getGuiFont().draw(batch, "x " + level.getFlairyTaken(), 690 + scoreFairy.getRegionWidth(), 455);
    }

    @Override
    public LevelScreenState update(float deltaTime) {
        updateAds(deltaTime);
        if (!retryShowed) {
            gameStage.act(deltaTime);
        }
        guiStage.act(deltaTime);

        // Control different status
        if (level.isWin()) {
            if (!level.isTutorial()) {
                LevelManager.INSTANCE.notifyLevelCompleted(level.getWorld(), level.getNumber(), level.getFlairyTaken());
            }
            return LevelScreenState.WIN;
        } else if (level.isLose()) {
            return LevelScreenState.LOSE;
        }

        switch (HandleInput.INSTANCE.getMenuAction()) {
            case PAUSE:
                return LevelScreenState.PAUSE;
            case RETRY:
                disable();
                enableRetry();
                return LevelScreenState.INGAME;
            case YES:
                disableRetry();
                ScreenManager.INSTANCE.setScreen(level.getNumber(), level.isTutorial());
                return LevelScreenState.INGAME;
            case NO:
                enable();
                disableRetry();
                return LevelScreenState.INGAME;
            case NEXT:
                if (level.isTutorial()) {
                    ScreenManager.INSTANCE.setScreen(level.getNumber(), false);
                } else {
                    ScreenManager.INSTANCE.setScreen(level.getNumber() + 1, true);
                }
                return LevelScreenState.INGAME;
            default:
                if (retryShowed) {
                    if (isBackJustPressed()) {
                        enable();
                        disableRetry();
                        return LevelScreenState.INGAME;
                    }
                } else {
                    if (isBackJustPressed() && !level.isTutorial()) {
                        return LevelScreenState.PAUSE;
                    }
                }
        }

        return checkAvatarsCollisions();
    }

    private LevelScreenState checkAvatarsCollisions() {
        // We won't set level as dead or goal, because we want to do some animation before showing next options
        // First check collision with goals (interactive)
        LevelGameAssistant.INSTANCE.checkAvatarsCollision(level.getEarmAvatar(), level.getInteractiveAvatars());
        // Check collision between main avatar and enemies
        LevelGameAssistant.INSTANCE.checkAvatarsCollision(level.getEarmAvatar(), level.getEnemyAvatars());

        if (level.isTutorial()) {
            for (Level.TutorialAction tutorialAction : level.getTutorialActions()) {
                if (!tutorialAction.isDone() && LevelGameAssistant.INSTANCE.checkRectangleCollision(level.getEarmAvatar(), tutorialAction.getRectangle())) {
                    AvatarCommand command = AvatarCommand.parseAvatarCommand(tutorialAction.getAction());
                    if (!command.equals(AvatarCommand.NO_COMMAND)) {
                        command.execute(level.getEarmAvatar());
                        level.setNextAction(tutorialAction);
                    } else {
                        Gdx.app.log("Tutorial level", "Show message with key: " + tutorialAction.getAction());
                        level.setNextAction(tutorialAction);
                        return LevelScreenState.TUTORIAL;
                    }
                }
            }
        }
        return LevelScreenState.INGAME;
    }

    public void enable() {
        super.enable();
        // Show this state
        actionTable.setVisible(true);
        auxTable.setVisible(true);
    }

    public void disable() {
        super.disable();
        if (!level.isTutorial()) {
            actionTable.setVisible(false);
        }
        auxTable.setVisible(false);
    }

    @Override
    public void render(float deltaTime) {
        guiStage.draw();
    }

}
