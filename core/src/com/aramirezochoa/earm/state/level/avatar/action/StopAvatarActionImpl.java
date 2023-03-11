package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 04/10/2014.
 */
public class StopAvatarActionImpl implements AvatarActionDef {

    public static float LIMIT_TIME = 0.5f;

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.frameTime = 0;
        avatar.actionTimer = LIMIT_TIME;
        avatar.getVelocity().x = 0;
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        if (avatar.actionTimer < 0) {
            avatar.setStatus(AvatarAction.IDLE);
        } else {
            avatar.actionTimer -= deltaTime;
        }
    }

}

