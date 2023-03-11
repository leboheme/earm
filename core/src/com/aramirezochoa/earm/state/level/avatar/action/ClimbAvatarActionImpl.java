package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by boheme on 11/11/14.
 */
public class ClimbAvatarActionImpl implements AvatarActionDef {

    private static float LIMIT_CLIMB_TIME = 7f;

    private static final float VERTICAL_VELOCITY = 250f;
    private static final float LAST_PUSH = 250f;

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.actionTimer = LIMIT_CLIMB_TIME;
        avatar.getAcceleration().y = -level.getGravity().y;
        avatar.getVelocity().y = VERTICAL_VELOCITY;
        avatar.getVelocity().x = 0;
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        if (avatar.actionTimer < 0) {
            avatar.getAcceleration().y = 0;
            avatar.setStatus(AvatarAction.IDLE);
        } else {
            avatar.actionTimer -= deltaTime;
            switch (avatar.getMoveDirection()) {
                case RIGHT:
                    avatar.getVelocity().x = 500f;
                    break;
                case LEFT:
                    avatar.getVelocity().x = -500f;
                    break;
            }
            LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
            if (avatar.getVelocity().x != 0) {
                avatar.getAcceleration().y = 0;
                avatar.getVelocity().y = LAST_PUSH;
                avatar.setStatus(AvatarAction.IDLE);
            } else {
                avatar.getVelocity().y = VERTICAL_VELOCITY;
            }
        }
    }
}
