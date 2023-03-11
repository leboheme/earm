package com.aramirezochoa.earm.input;

import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.menu.MenuAction;

/**
 * Created by leboheme on 05/08/2014.
 */
public enum HandleInput {
    INSTANCE;

    private volatile AvatarCommand avatarCommand = AvatarCommand.NO_COMMAND;
    private volatile MenuAction menuAction = MenuAction.NOTHING;
    private volatile int levelSelected = 0;
    private AvatarCommand moveCommand = AvatarCommand.AUTOMATIC_WALK;
    private volatile WorldType worldSelected = WorldType.MAIN_MENU;

    public synchronized void notifyAvatarCommand(AvatarCommand avatarCommand) {
        this.avatarCommand = avatarCommand;
    }

    public synchronized AvatarCommand getAvatarCommand() {
        AvatarCommand toReturn = avatarCommand;
        avatarCommand = AvatarCommand.NO_COMMAND;
        return toReturn;
    }

    public synchronized void notifyMenuAction(MenuAction menuAction) {
        this.menuAction = menuAction;
    }

    public synchronized MenuAction getMenuAction() {
        MenuAction toReturn = menuAction;
        menuAction = MenuAction.NOTHING;
        return toReturn;
    }

    public synchronized AvatarCommand getAvatarMovementCommand() {
        return moveCommand;
    }

    public synchronized void notifyLevelSelected(int levelNumber) {
        this.levelSelected = levelNumber;
    }

    public synchronized int getLevelSelected() {
        int toReturn = levelSelected;
        levelSelected = 0;
        return toReturn;
    }

    public synchronized void notifyWorldSelected(WorldType worldSelected) {
        this.worldSelected = worldSelected;
    }

    public synchronized WorldType getWorldSelected() {
        WorldType toReturn = worldSelected;
        worldSelected = WorldType.MAIN_MENU;
        return toReturn;
    }
}
