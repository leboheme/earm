package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by boheme on 10/11/14.
 */
public class ParachuteAvatarActionImpl implements AvatarActionDef {

    private static float LIMIT_PARACHUTE_TIME = 2f;

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.actionTimer = LIMIT_PARACHUTE_TIME;
        avatar.getAcceleration().y = -level.getGravity().y * 4 / 5;
        avatar.getVelocity().y = 0;
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {

        LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);

        avatar.getVelocity().x = 0;
        if (avatar.getVelocity().y == 0) {
            avatar.getAcceleration().y = 0;
            avatar.setStatus(AvatarAction.IDLE);
        } else if (avatar.actionTimer < 0) {
            avatar.getAcceleration().y = 0;
            avatar.setStatus(AvatarAction.FALL);
        } else {
            avatar.actionTimer -= deltaTime;
        }
    }
}
