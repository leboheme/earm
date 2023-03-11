package com.aramirezochoa.earm;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Created by boheme on 10/02/14.
 */
public class DesktopLauncher implements ActivityRequestHandler {

    private static DesktopLauncher application;

    public static void main(String[] args) {

        if (application == null) {
            application = new DesktopLauncher();
        }

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Earm!");
        config.setWindowedMode(800, 480);
        new Lwjgl3Application(new EarmGame(application), config);
    }

    @Override
    public void showBannerAd(boolean show) {
        System.out.println("Show banner ad:" + show);
    }

    @Override
    public void showInterstitialAd() {
        System.out.println("Show interstitial ad");
    }

}