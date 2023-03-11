package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by boheme on 25/10/14.
 */
public class DigAvatarActionImpl implements AvatarActionDef {

    private static float LIMIT_DIG_TIME = 2f;

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.actionTimer = LIMIT_DIG_TIME;
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
                    avatar.getVelocity().x = avatar.getDigVelocity();
                    break;
                case LEFT:
                    avatar.getVelocity().x = -avatar.getDigVelocity();
                    break;
            }
            LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
        }
    }

}
