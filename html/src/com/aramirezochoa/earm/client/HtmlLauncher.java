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
        return new GwtApplicationConfiguration(GwtApplication.isMobileDevice());
        // Fixed size application:
//        return new GwtApplicationConfiguration(600, 400, GwtApplication.isMobileDevice());
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