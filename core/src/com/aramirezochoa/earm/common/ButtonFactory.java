package com.aramirezochoa.earm.common;

import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.options.OptionAction;
import com.aramirezochoa.earm.options.OptionsManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.aramirezochoa.earm.state.level.manager.LevelManager;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.menu.MenuAction;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by leboheme on 16/09/2014.
 */
public class ButtonFactory {

    public static Button createMenuButton(ButtonSize buttonSize, ButtonContext buttonContext, final MenuAction buttonAction) {
        SoundButton button = new SoundButton(MediaManager.INSTANCE.getButtonStyle(buttonSize, buttonContext, buttonAction.name()));
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                HandleInput.INSTANCE.notifyMenuAction(buttonAction);
                SoundManager.INSTANCE.playButton();
            }
        });
        return button;
    }

    public static Group createCommandButton(AtomicInteger text, Level level, final AvatarCommand avatarCommand) {
        TextButton.TextButtonStyle buttonStyle = MediaManager.INSTANCE.getTextButtonStyle(ButtonSize.MID, ButtonContext.LEVEL, avatarCommand.name(), ButtonSize.LIT);
        CommandButton button = new CommandButton(String.valueOf(text), buttonStyle, avatarCommand, level);

        Group grp = new Group();
        grp.setWidth(button.getWidth());
        Label label = new Label(avatarCommand.getKeyLabel(), new Label.LabelStyle(buttonStyle.font, buttonStyle.fontColor));
        label.setAlignment(Align.bottom);
        label.setAlignment(Align.center);
        label.setWidth(button.getWidth());
        grp.addActor(button);
        grp.addActor(label);

        return grp;
    }

    public static CheckBox createCheckBox(ButtonSize buttonSize, ButtonContext buttonContext, final OptionAction optionAction) {
        final CheckBox checkBox = new CheckBox("", MediaManager.INSTANCE.getCheckBoxStyle(buttonSize, buttonContext, optionAction.name()));
        checkBox.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                OptionsManager.INSTANCE.notify(optionAction, checkBox.isChecked());
            }
        });
        checkBox.align(Align.left);
        checkBox.setChecked(OptionsManager.INSTANCE.isEnabled(optionAction));
        return checkBox;
    }

    public static TextButton createLevelSelectButton(TextButton.TextButtonStyle style, final int levelNumber, boolean enabled) {
        TextButton button = new TextButton(enabled ? String.valueOf(levelNumber) : "", style);
        if (enabled) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    HandleInput.INSTANCE.notifyLevelSelected(levelNumber);
                    SoundManager.INSTANCE.playButton();
                }
            });
        }
        return button;
    }

    public static TextButton createWorldSelectButton(TextButton.TextButtonStyle style, final WorldType world) {
        TextButton button;
        if (world.isOpened()) {
            button = new TextButton("\n" + LevelManager.INSTANCE.getTakenFlairies(world) + "/" + (world.getTotalLevels() * 3), style);
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    HandleInput.INSTANCE.notifyWorldSelected(world);
                    SoundManager.INSTANCE.playButton();
                }
            });
        } else {
            button = new TextButton("", style);
        }
        return button;
    }

}
