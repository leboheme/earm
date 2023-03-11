package com.aramirezochoa.earm.language;

import com.aramirezochoa.earm.Manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Created by leboheme on 28/09/2014.
 */
public enum LanguageManager implements Manager {
    INSTANCE;

    private I18NBundle languageBundle;

    public void init() {
        languageBundle = I18NBundle.createBundle(Gdx.files.internal("data/i18n/literals"));
    }

    public String getString(String literal) {
        return languageBundle.get(literal);
    }

    public String getString(String literal, String  ... args) {
        return languageBundle.format(literal, args);
    }
}
