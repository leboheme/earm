package com.aramirezochoa.earm.state.level.avatar.command;

import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.badlogic.gdx.Input;

/**
 * Created by leboheme on 15/08/2014.
 */
public enum AvatarCommand {

    NO_COMMAND(new NoCommand(), Input.Keys.UNKNOWN, ""),
    WALK(new WalkCommand(), Input.Keys.UNKNOWN, ""),
    AUTOMATIC_WALK(new AutomaticWalkCommand(), Input.Keys.UNKNOWN, ""),
    JUMP(new JumpCommand(), Input.Keys.UP, "Up"),
    FLY(new FlyCommand(), Input.Keys.A, "A"),
    STOP(new StopCommand(), Input.Keys.DOWN, "Down"),
    TURN(new TurnCommand(), Input.Keys.SPACE, "Space"),
    DIG(new DigCommand(), Input.Keys.D, "D"),
    PARACHUTE(new ParachuteCommand(), Input.Keys.S, "S"),
    CLIMB(new ClimbCommand(), Input.Keys.W, "W");

    private final Command command;
    private final int keyboardCode;
    private final String keyLabel;

    AvatarCommand(Command command, int keyboardCode, String keyLabel) {
        this.command = command;
        this.keyboardCode = keyboardCode;
        this.keyLabel = keyLabel;
    }

    public void execute(Avatar avatar) {
        command.execute(avatar);
    }

    public int getKeyboardCode() {
        return this.keyboardCode;
    }

    public static AvatarCommand parseAvatarCommand(String action) {
        for (AvatarCommand avatarAction : AvatarCommand.values()) {
            if (avatarAction.name().equals(action))
                return avatarAction;
        }
        return NO_COMMAND;
    }

    public String getKeyLabel() {
        return keyLabel;
    }
}
