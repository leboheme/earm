package com.aramirezochoa.earm.client;

import com.aramirezochoa.earm.ActivityRequestHandler;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.aramirezochoa.earm.EarmGame;

public class HtmlLauncher extends GwtApplication implements ActivityRequestHandler {

    @Override
    public GwtApplicationConfiguration getConfig() {
        // Resizable application, uses available space in browser
//        GwtApplicationConfiguration config = new GwtApplicationConfiguration(GwtApplication.isMobileDevice());
//         Fixed size application:
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(800, 480, GwtApplication.isMobileDevice());
        config.padHorizontal = 0;
        config.padVertical = 0;
        return config;

    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new EarmGame(this);
    }

    @Override
    public void showBannerAd(boolean show) {

    }

    @Override
    public void showInterstitialAd() {

    }
}