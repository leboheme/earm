package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 14/08/2014.
 */
public interface AvatarActionDef {

    void set(Avatar avatar, Level level);

    void update(float deltaTime, Avatar avatar, Level level);

}
