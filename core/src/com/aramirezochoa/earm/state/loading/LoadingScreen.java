package com.aramirezochoa.earm.state.loading;

import com.aramirezochoa.earm.AbstractScreen;
import com.aramirezochoa.earm.Manager;
import com.aramirezochoa.earm.language.LanguageManager;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.options.OptionsManager;
import com.aramirezochoa.earm.state.ScreenManager;
import com.aramirezochoa.earm.state.level.manager.LevelManager;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by boheme on 19/04/14.
 */
public class LoadingScreen extends AbstractScreen {

    private ProgressStatusHelper progressStatusHelper;
    private Stage stage;
    private TextureAtlas textureAtlas;

    @Override
    public void show() {
        progressStatusHelper = new ProgressStatusHelper();
        textureAtlas = new TextureAtlas(Gdx.files.internal("data/images/loading.atlas"));
        stage = new Stage(new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));

        Table table = new Table();
        table.setFillParent(true);

        // Add title
        final Animation<TextureRegion> animation = new Animation(0.5f, textureAtlas.findRegions("LOADING"));
        table.add(new Actor() {
            private float timer = 0;

            @Override
            public void draw(Batch batch, float alpha) {
                timer += 0.15f;
                batch.draw(animation.getKeyFrame(timer, true), 690, 10);
            }
        });

        stage.addActor(table);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if (progressStatusHelper.isFinished()) {
            ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.MAIN_MENU);
            return;
        } else {
            progressStatusHelper.loadNext();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        ScreenManager.INSTANCE.setScreen(ScreenManager.ScreenType.LOADING_SCREEN);
    }

    @Override
    public void resume() {
        show();
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        stage.dispose();
    }

    private class ProgressStatusHelper {

        private Manager managers[] = new Manager[]{
                MediaManager.INSTANCE,
                SoundManager.INSTANCE,
                OptionsManager.INSTANCE,
                SoundManager.INSTANCE,
                LanguageManager.INSTANCE,
                LevelManager.INSTANCE
        };
        private volatile int counter = 0;

        public boolean isFinished() {
            return counter > managers.length;
        }

        public void loadNext() {
            if (counter < managers.length)
                managers[counter].init();
            counter++;
        }
    }

}
