package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 21/08/2014.
 */
public class GoalAvatarActionImpl implements AvatarActionDef {

    public static float LIMIT_TIME = 0.75f;

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.frameTime = 0;
        avatar.actionTimer = LIMIT_TIME;
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        avatar.getVelocity().x = 0;
        avatar.getVelocity().y = 0;
        avatar.getAcceleration().y = -level.getGravity().y;
        if (avatar.actionTimer < 0) {
            Level.class.cast(level).setWin(true);
        } else {
            avatar.actionTimer -= deltaTime;
        }
    }

}
