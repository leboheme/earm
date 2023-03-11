package com.aramirezochoa.earm.common;


import com.aramirezochoa.earm.media.SoundManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by boheme on 17/09/14.
 */
public class SoundButton extends Button {

    public SoundButton(ButtonStyle style) {
        super(style);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.INSTANCE.playButton();
            }
        });
    }

}
