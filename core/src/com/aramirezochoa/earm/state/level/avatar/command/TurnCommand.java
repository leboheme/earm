package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 24/10/2014.
 */
public class TurnCommand implements Command {
    @Override
    public void execute(Avatar avatar) {
        avatar.turn();
    }
}
