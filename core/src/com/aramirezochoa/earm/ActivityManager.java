package com.aramirezochoa.earm;

import com.aramirezochoa.earm.language.LanguageManager;

/**
 * Created by boheme on 16/09/14.
 */
public enum ActivityManager {
    INSTANCE;

    private ActivityRequestHandler activityRequestHandler;

    public void init(ActivityRequestHandler ActivityRequestHandler) {
        this.activityRequestHandler = ActivityRequestHandler;
    }

    public void showAdTop(boolean show) {
        activityRequestHandler.showBannerAd(show);
    }

    public void showAdFull(boolean show) {
        if (show) {
            activityRequestHandler.showInterstitialAd();
        }
    }

    public void publishSomewhere(int level, int flairies) {
        String title = LanguageManager.INSTANCE.getString("reached", String.valueOf(level), String.valueOf(flairies));
        String description = LanguageManager.INSTANCE.getString("playNow");
    }
}
