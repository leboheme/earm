package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;

/**
 * Created by leboheme on 15/08/2014.
 */
public enum AvatarCommand {

    NO_COMMAND(new NoCommand()),
    WALK(new WalkCommand()),
    AUTOMATIC_WALK(new AutomaticWalkCommand()),
    JUMP(new JumpCommand()),
    FLY(new FlyCommand()),
    STOP(new StopCommand()),
    TURN(new TurnCommand()),
    DIG(new DigCommand()),
    PARACHUTE(new ParachuteCommand()),
    CLIMB(new ClimbCommand());

    private final Command command;

    private AvatarCommand(Command command) {
        this.command = command;
    }

    public void execute(Avatar avatar) {
        command.execute(avatar);
    }

    public static AvatarCommand parseAvatarCommand(String action) {
        for (AvatarCommand avatarAction : AvatarCommand.values()) {
            if (avatarAction.name().equals(action))
                return avatarAction;
        }
        return NO_COMMAND;
    }

}
