package com.aramirezochoa.earm;

import com.aramirezochoa.earm.state.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

/**
 * Created by boheme on 10/09/14.
 */
public abstract class AbstractScreen implements Screen {

    public void init(int level, boolean doTutorial) {
        // Only to implement LevelScreen, other will do with show()
    }

    @Override
    public abstract void show();

    @Override
    public abstract void render(float delta);

    @Override
    public void resize(int width, int height) {
        // Do nothing
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public abstract void dispose();

    @Override
    public void pause() {
        // pause() is not disposing because later it is called resume(), and whith Game.setScreen(...) it is called hide(), which will dispose
    }

    @Override
    public void resume() {
        ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LOADING_SCREEN);
    }

    protected boolean isBackJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK);
    }
}
