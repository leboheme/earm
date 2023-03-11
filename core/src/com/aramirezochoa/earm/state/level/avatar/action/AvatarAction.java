package com.aramirezochoa.earm.state.level.avatar.action;

import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 15/08/2014.
 */
public enum AvatarAction {

    // Earm actions
    IDLE(new IdleAvatarActionImpl()),
    WALK(new IdleAvatarActionImpl()),
    JUMP(new JumpAvatarActionImpl()),
    FALL(new FallAvatarActionImpl()),
    FLY(new FlyAvatarActionImpl()),
    DEAD(new DeadAvatarActionImpl()),
    DEAD_ALTITUDE(new DeadAvatarActionImpl()),
    GOAL(new GoalAvatarActionImpl()),
    STOP(new StopAvatarActionImpl()),
    DIG(new DigAvatarActionImpl()),
    PARACHUTE(new ParachuteAvatarActionImpl()),
    CLIMB(new ClimbAvatarActionImpl()),
    // Enemy actions
    // Solfyer
    WAGGLE(new WaggleAvatarActionImpl()),
    // Soldier
    PATROL(new PatrolAvatarActionImpl()),
    // Flairy
    STAND(new AvatarActionDef() {
        @Override
        public void set(Avatar avatar, Level level) {
        }

        @Override
        public void update(float deltaTime, Avatar avatar, Level level) {
        }
    }),
    // Landmine
    GREEN(new LandmineAvatarActionImpl()),
    RED(new LandmineAvatarActionImpl());

    private final AvatarActionDef avatarAction;

    AvatarAction(AvatarActionDef avatarAction) {
        this.avatarAction = avatarAction;
    }

    public AvatarAction set(Avatar avatar, Level level) {
        avatarAction.set(avatar, level);
        return this;
    }

    public void update(float deltaTime, Avatar avatar, Level level) {
        avatarAction.update(deltaTime, avatar, level);
    }

}
