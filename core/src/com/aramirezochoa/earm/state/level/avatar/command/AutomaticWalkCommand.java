package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by boheme on 22/08/14.
 */
public class AutomaticWalkCommand implements Command {

    @Override
    public void execute(Avatar avatar) {
        avatar.walk(avatar.getLookingAt());
    }

}
