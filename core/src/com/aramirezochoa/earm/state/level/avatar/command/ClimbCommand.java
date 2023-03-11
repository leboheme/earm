package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by boheme on 11/11/14.
 */
public class ClimbCommand implements Command {

    @Override
    public void execute(Avatar avatar) {
        avatar.climb();
    }

}
