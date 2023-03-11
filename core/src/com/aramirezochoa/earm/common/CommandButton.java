package com.aramirezochoa.earm.common;

import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by leboheme on 21/08/2014.
 */
public class CommandButton extends TextButton {

    private final AvatarCommand avatarCommand;
    private final Level level;
    private final TextureAtlas.AtlasRegion warn;
    private boolean showWarn = false;
    private float counter = 0;

    public CommandButton(String text, TextButtonStyle textButtonStyle, final AvatarCommand avatarCommand, Level level) {
        super(text, textButtonStyle);
        this.avatarCommand = avatarCommand;
        this.level = level;
        this.warn = MediaManager.INSTANCE.getLevelTexture(ButtonSize.MID, "WARN");
        getLabel().setAlignment(Align.top);
        if (!level.isTutorial()) {
            addListener(new ClickListener() {
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                    HandleInput.INSTANCE.notifyAvatarCommand(avatarCommand);
                    return false;
                }
            });
        }
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        // Update left number of actions
        int left = level.getActions().get(avatarCommand).get();
        if (left < 0) {
            setText("");
        } else {
            setText(String.valueOf(left));
        }
        counter += deltaTime;
        if (left == 0) {
            showWarn = true;
        } else if (left == 1 && counter > 0.5) {
            showWarn = !showWarn;
            counter = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (showWarn) {
            batch.draw(warn, getX(), getY());
        }
    }

}
