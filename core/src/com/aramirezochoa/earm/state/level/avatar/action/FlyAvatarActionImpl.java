package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by leboheme on 17/08/2014.
 */
public class FlyAvatarActionImpl implements AvatarActionDef {

    private static float LIMIT_FLY_TIME = 1.25f;

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.actionTimer = LIMIT_FLY_TIME;
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        if (avatar.actionTimer < 0) {
            avatar.getAcceleration().y = 0;
            LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
            avatar.setStatus(AvatarAction.IDLE);
        } else {
            avatar.actionTimer -= deltaTime;
            avatar.getVelocity().y = 0;
            avatar.getAcceleration().y = -level.getGravity().y;
            switch (avatar.getLookingAt()) {
                case RIGHT:
                    avatar.getVelocity().x = avatar.getFlyVelocity();
                    break;
                case LEFT:
                    avatar.getVelocity().x = -avatar.getFlyVelocity();
                    break;
            }
            LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
        }
    }

}
