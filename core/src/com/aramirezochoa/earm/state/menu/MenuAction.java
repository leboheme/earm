package com.aramirezochoa.earm.state.menu;

/**
 * Created by boheme on 8/04/14.
 */
public enum MenuAction {
    NOTHING(""),
    PLAY("Play"),
    PAUSE("Pause"),
    NEXT("Next level"),
    RETRY("Try again"),
    OPTIONS("Options"),
    CREDITS("Credits"),
    BACK("Back"),
    EXIT("Exit"),
    YES("Yes"),
    NO("No");

    private final String literal;

    MenuAction(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
