package com.aramirezochoa.earm.state.level.manager;

/**
 * Created by leboheme on 02/06/2014.
 */
public class LevelDescription {

    private int number;
    private boolean enabled = false;
    private int flairies = 0;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getFlairies() {
        return flairies;
    }

    public void setFlairies(int flairies) {
        this.flairies = flairies;
    }
}
