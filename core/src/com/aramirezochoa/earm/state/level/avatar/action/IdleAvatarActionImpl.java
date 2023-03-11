package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by leboheme on 15/08/2014.
 */
public class IdleAvatarActionImpl implements AvatarActionDef {

    @Override
    public void set(Avatar avatar, Level level) {
        // Do nothing
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        switch (avatar.getMoveDirection()) {
            case RIGHT:
                avatar.getVelocity().x = avatar.getWalkVelocity();
                break;
            case LEFT:
                avatar.getVelocity().x = -avatar.getWalkVelocity();
                break;
        }

        // We do level collisions at this level in case any action wants to avoid it
        LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
        if (avatar.getPosition().y == 0) {
            avatar.setStatus(AvatarAction.DEAD_ALTITUDE);
        }

        if (avatar.getVelocity().y > 0) {
            avatar.setStatus(AvatarAction.WALK);
        } else if (avatar.getVelocity().y < 0) {
            avatar.setStatus(AvatarAction.FALL);
        } else if (avatar.getVelocity().x != 0) {
            avatar.setStatus(AvatarAction.WALK);
        } else {
            avatar.setStatus(AvatarAction.IDLE);
        }
    }

}
