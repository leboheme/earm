package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by leboheme on 21/08/2014.
 */
public class PatrolAvatarActionImpl implements AvatarActionDef {

    @Override
    public void set(Avatar avatar, Level level) {

    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        if (avatar.getVelocity().y < 0) {
            avatar.getVelocity().x = 0;
        } else {
            switch (avatar.getLookingAt()) {
                case RIGHT:
                    avatar.getVelocity().x = avatar.getWalkVelocity();
                    break;
                case LEFT:
                    avatar.getVelocity().x = -avatar.getWalkVelocity();
                    break;
            }
        }
        LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
    }

}
