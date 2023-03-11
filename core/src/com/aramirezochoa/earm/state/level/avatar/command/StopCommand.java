package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 04/10/2014.
 */
public class StopCommand implements Command {

    @Override
    public void execute(Avatar avatar) {
        avatar.stop();
    }

}
