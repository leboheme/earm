package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by boheme on 10/11/14.
 */
public class ParachuteCommand implements Command {

    @Override
    public void execute(Avatar avatar) {
        avatar.parachute();
    }

}