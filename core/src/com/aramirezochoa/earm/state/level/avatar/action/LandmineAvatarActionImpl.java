package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.map.LevelGameAssistant;

/**
 * Created by boheme on 10/11/14.
 */
public class LandmineAvatarActionImpl implements AvatarActionDef {
    @Override
    public void set(Avatar avatar, Level level) {

    }

    @Override
    public void update(float deltaTime, Avatar avatar, Level level) {
        LevelGameAssistant.INSTANCE.updateAvatar(avatar, level, deltaTime);
    }
}
