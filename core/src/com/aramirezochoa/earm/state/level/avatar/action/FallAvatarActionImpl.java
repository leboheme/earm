package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by leboheme on 21/08/2014.
 */
public class FallAvatarActionImpl implements AvatarActionDef {

    @Override
    public void set(Avatar avatar, Level level) {
        avatar.frameTime = 0;
        avatar.actionTimer = 0;
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

        if (avatar.getVelocity().y == 0) {
            avatar.setStatus(AvatarAction.IDLE);
        } else if (avatar.getVelocity().y > 0) {
            avatar.setStatus(AvatarAction.WALK);
        }
    }

}
