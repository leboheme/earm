package com.aramirezochoa.earm.state.level.avatar;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.aramirezochoa.earm.state.level.avatar.types.AvatarType;
import com.aramirezochoa.earm.state.level.avatar.types.DirectionType;
import com.aramirezochoa.earm.state.level.utils.Point2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by leboheme on 14/08/2014.
 */
public class EarmAvatar extends Avatar {

    private static float WALK_VELOCITY = 150;
    private static float JUMP_VELOCITY = 475;
    private static float FLY_VELOCITY = WALK_VELOCITY * 1.5f;
    private static float DIG_VELOCITY = WALK_VELOCITY / 2f;
    private static final float CLIMB_LIMIT = 1f;
    private float climbCounter = 0;
    private boolean readyToClimb;
    private boolean setClimb;

    @Override
    public void init(AvatarType type, Point2 position, Level level, DirectionType lookingAt) {
        super.init(type, position, level, lookingAt);
        readyToClimb = false;
        setClimb = false;
        climbCounter = 0;
    }

    @Override
    public void notifyWall() {
        if (readyToClimb) {
            setClimb = true;
            readyToClimb = false;
            climbCounter = 0;
        } else {
            switch (currentAction) {
                case IDLE:
                case WALK:
                case FALL:
                case JUMP:
                    lookingAt = lookingAt.flip();
                    break;
            }
        }
    }

    @Override
    public float getWalkVelocity() {
        return WALK_VELOCITY;
    }

    @Override
    public float getJumpVelocity() {
        return JUMP_VELOCITY;
    }

    @Override
    public float getFlyVelocity() {
        return FLY_VELOCITY;
    }

    @Override
    public float getDigVelocity() {
        return DIG_VELOCITY;
    }


    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        if (readyToClimb) {
            climbCounter += deltaTime;
            if (climbCounter > CLIMB_LIMIT) {
                readyToClimb = false;
                climbCounter = 0;
            }
        } else if (setClimb) {
            setClimb = false;
            setAction(AvatarAction.CLIMB.set(this, level));
        }
    }

    @Override
    public void setStatus(AvatarAction action) {
        if (!AvatarAction.DEAD_ALTITUDE.equals(currentAction)
                && !AvatarAction.DEAD.equals(currentAction)
                && !AvatarAction.GOAL.equals(currentAction)) {
            switch (action) {
                case IDLE:
                case WALK:
                case FALL:
                case DEAD_ALTITUDE:
                case DEAD:
                case GOAL:
                    setAction(action.set(this, level));
                    break;
            }
        }
    }

    @Override
    public void jump() {
        switch (currentAction) {
            case IDLE:
            case WALK:
                if (getVelocity().y == 0)
                    tryAndNotify(AvatarCommand.JUMP, AvatarAction.JUMP);
                break;
        }
    }

    @Override
    public void fly() {
        switch (currentAction) {
            case IDLE:
            case WALK:
            case FALL:
            case JUMP:
            case PARACHUTE:
                tryAndNotify(AvatarCommand.FLY, AvatarAction.FLY);
                break;
        }
    }

    @Override
    public void stop() {
        switch (currentAction) {
            case WALK:
                tryAndNotify(AvatarCommand.STOP, AvatarAction.STOP);
                break;
        }
    }

    @Override
    public void turn() {
        switch (currentAction) {
            case FALL:
            case WALK:
            case PARACHUTE:
                if (isPossible(AvatarCommand.TURN)) {
                    lookingAt = lookingAt.flip();
                }
        }
    }

    @Override
    public void dig() {
        // Prepare dig for next 1 second, if there is a collision, then dig, otherwise walk (but the skill is wasted)
        tryAndNotify(AvatarCommand.DIG, AvatarAction.DIG);
    }

    @Override
    public void parachute() {
        switch (currentAction) {
            case FALL:
            case FLY:
                tryAndNotify(AvatarCommand.PARACHUTE, AvatarAction.PARACHUTE);
                break;
        }
    }

    @Override
    public void climb() {
        if (isPossible(AvatarCommand.CLIMB)) {
            readyToClimb = true;
        }
    }

    @Override
    public void collisionWith(Avatar avatar) {
        // Do nothing
    }

    private void tryAndNotify(AvatarCommand avatarCommand, AvatarAction avatarAction) {
        if (isPossible(avatarCommand)) {
            setAction(avatarAction);
        }
    }

    private boolean isPossible(AvatarCommand avatarCommand) {
        switch (currentAction) {
            case DEAD:
            case DEAD_ALTITUDE:
            case GOAL:
                return false;
            default:
                AtomicInteger limit = level.getActions().get(avatarCommand);
                if (limit.get() > 0) {
                    limit.decrementAndGet();
                    return true;
                } else if (limit.get() == -1) {
                    return true;
                }
                return false;
        }
    }

    private void setAction(AvatarAction avatarAction) {
        stopSound(avatarAction);
        currentAction = avatarAction.set(this, level);
        playSound(currentAction);
    }

}
