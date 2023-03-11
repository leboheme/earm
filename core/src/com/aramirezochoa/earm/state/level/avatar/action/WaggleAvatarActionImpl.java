package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by leboheme on 21/08/2014.
 */
public class WaggleAvatarActionImpl implements AvatarActionDef {

    private static final float VELOCITY_FACTOR = 2f;

    @Override
    public void set(Avatar avatar, Level level) {
        // Do nothing
    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        if (avatar.getPosition().y - avatar.getDimension().y < avatar.originalY) {
            avatar.getAcceleration().y = -level.getGravity().y * VELOCITY_FACTOR;
        } else {
            avatar.getAcceleration().y = 0;
        }
        LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
    }

}
