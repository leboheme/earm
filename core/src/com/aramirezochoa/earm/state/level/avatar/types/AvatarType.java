package com.aramirezochoa.earm.state.level.avatar.types;

import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.utils.Point2;

/**
 * Created by boheme on 13/03/14.
 */
public enum AvatarType {
    EARM("earm", new Point2(35, 85), AvatarAction.IDLE),
    SOLDIER("soldier", new Point2(20, 90), AvatarAction.PATROL),
    SOLFLYER("solflyer", new Point2(30, 65), AvatarAction.WAGGLE),
    GOAL("goal", new Point2(70, 100), AvatarAction.IDLE),
    FLAIRY("flairy", new Point2(35, 40), AvatarAction.STAND),
    CANON_SNOW("canon_snow", new Point2(64, 128), AvatarAction.IDLE),
    LANDMINE("landmine", new Point2(32, 13), AvatarAction.GREEN),
    UNKNOWN("", null, null);

    private String name;
    private final Point2 dimension;
    private final AvatarAction defaultAction;

    AvatarType(String name, Point2 dimension, AvatarAction defaultAction) {
        this.name = name;
        this.dimension = dimension;
        this.defaultAction = defaultAction;
    }

    public Point2 getDimension() {
        return dimension;
    }

    public AvatarAction getDefaultAction() {
        return defaultAction;
    }

    public static AvatarType getType(String name) {
        for (AvatarType avatarType : AvatarType.values()) {
            if (avatarType.name.equals(name)) {
                return avatarType;
            }
        }
        return UNKNOWN;
    }
}
