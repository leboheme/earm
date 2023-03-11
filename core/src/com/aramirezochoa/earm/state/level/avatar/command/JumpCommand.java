package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 15/08/2014.
 */
public class JumpCommand implements Command {

    @Override
    public void execute(Avatar avatar) {
        avatar.jump();
    }

}
