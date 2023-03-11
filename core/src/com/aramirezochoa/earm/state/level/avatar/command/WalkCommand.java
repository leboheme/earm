package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.avatar.types.DirectionType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by leboheme on 15/08/2014.
 */
public class WalkCommand implements Command {

    @Override
    public void execute(Avatar avatar) {
        DirectionType directionType;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            directionType = DirectionType.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            directionType = DirectionType.LEFT;
        } else {
            directionType = DirectionType.NONE;
        }
        avatar.walk(directionType);
    }

}
