package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by boheme on 25/10/14.
 */
public class DigCommand implements Command {
    @Override
    public void execute(Avatar avatar) {
        avatar.dig();
    }
}
