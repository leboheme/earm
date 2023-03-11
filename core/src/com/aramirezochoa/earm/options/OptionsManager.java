package com.aramirezochoa.earm.options;

import com.aramirezochoa.earm.Manager;
import com.aramirezochoa.earm.media.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by boheme on 26/08/14.
 */
public enum OptionsManager implements Manager {
    INSTANCE;

    private static final String SOUND = "sounds";
    private static final String MUSIC = "music";
    public static final String OPTIONS = "Options";

    private Boolean sound = true;
    private Boolean music = true;

    public void init() {
        loadOptions();
    }

    private void loadOptions() {
        Preferences prefs = Gdx.app.getPreferences(OPTIONS);
        if (!prefs.contains(SOUND)) {
            createDefaultOptions(prefs);
        }
        this.sound = prefs.getBoolean(SOUND);
        this.music = prefs.getBoolean(MUSIC);
    }

    private void createDefaultOptions(Preferences prefs) {
        prefs.putBoolean(SOUND, true);
        prefs.putBoolean(MUSIC, true);
        prefs.flush();
    }

    public void notify(OptionAction optionAction, boolean checked) {
        switch (optionAction) {
            case SOUND:
                sound = checked;
                changeOption(SOUND, sound);
                break;
            case MUSIC:
                music = checked;
                changeOption(MUSIC, music);
                if (!checked) {
                    SoundManager.INSTANCE.stopThemes();
                }
                break;
        }
    }

    public boolean isEnabled(OptionAction optionAction) {
        switch (optionAction) {
            case SOUND:
                return sound;
            case MUSIC:
                return music;
        }
        return false;
    }

    private void changeOption(String attr, Boolean field) {
        Preferences prefs = Gdx.app.getPreferences(OPTIONS);
        prefs.remove(attr);
        prefs.putBoolean(attr, field);
        prefs.flush();
    }
}
