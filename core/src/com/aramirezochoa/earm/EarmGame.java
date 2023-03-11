package com.aramirezochoa.earm;

import com.aramirezochoa.earm.state.ScreenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Created by boheme on 10/02/14.
 */
public class EarmGame extends Game {

    private final ActivityRequestHandler application;

    public EarmGame(ActivityRequestHandler application) {
        this.application = application;
    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        // Loading main manager, others in LoadingState
        ScreenManager.INSTANCE.init(this);
        ActivityManager.INSTANCE.init(application);
    }

    @Override
    public void render() {
        super.render();
    }

}