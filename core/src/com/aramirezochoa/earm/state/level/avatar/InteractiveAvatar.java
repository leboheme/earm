package com.aramirezochoa.earm.state.level.avatar;

import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.avatar.types.AvatarType;
import com.aramirezochoa.earm.state.level.avatar.types.DirectionType;
import com.aramirezochoa.earm.state.level.utils.Point2;

/**
 * Created by leboheme on 23/10/2014.
 */
public class InteractiveAvatar extends Avatar {

    private float info = 1;
    private float landmineCounter = 0;

    @Override
    public void init(AvatarType type, Point2 position, Level level, DirectionType lookingAt) {
        init(type, position, level, lookingAt, 1);
    }

    public void init(AvatarType type, Point2 position, Level level, DirectionType lookingAt, float info) {
        super.init(type, position, level, lookingAt);
        this.info = info;
    }

    @Override
    public void act(float deltaTime) {
        switch (type) {
            case CANON_SNOW:
                super.act(deltaTime * info);
                break;
            case LANDMINE:
                super.act(deltaTime);
                updateLandmine(deltaTime);
                break;
            default:
                super.act(deltaTime);
        }
    }

    private void updateLandmine(float deltaTime) {
        if (AvatarAction.GREEN.equals(currentAction)) {
            if (landmineCounter > 1) {
                currentAction = AvatarAction.RED;
                landmineCounter = 0;
            } else {
                landmineCounter += deltaTime;
            }
        } else {
            if (landmineCounter > info) {
                currentAction = AvatarAction.GREEN;
                landmineCounter = 0;
            } else {
                landmineCounter += deltaTime;
            }
        }
    }

    @Override
    public void collisionWith(Avatar avatar) {
        // TODO we could abstract all this behaviour but is too simple to do that
        switch (type) {
            case GOAL:
                avatar.setStatus(AvatarAction.GOAL);
                break;
            case FLAIRY:
                SoundManager.INSTANCE.playSound(AvatarType.FLAIRY, AvatarAction.STAND);
                setVisible(false);
                level.incFlairyTaken();
                break;
            case CANON_SNOW:
                avatar.getVelocity().y += (25f * info);
                break;
            case LANDMINE:
                if (AvatarAction.RED.equals(currentAction)) {
                    avatar.setStatus(AvatarAction.DEAD);
                }
                break;
        }
    }

}
