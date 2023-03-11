package com.aramirezochoa.earm.state.level.avatar.types;

/**
 * Created by boheme on 18/02/14.
 */
public enum DirectionType {
    UP {
        @Override
        public DirectionType flip() {
            return DOWN;
        }
    },
    DOWN {
        @Override
        public DirectionType flip() {
            return UP;
        }
    },
    RIGHT {
        @Override
        public DirectionType flip() {
            return LEFT;
        }
    },
    LEFT {
        @Override
        public DirectionType flip() {
            return RIGHT;
        }
    },
    NONE {
        @Override
        public DirectionType flip() {
            return NONE;
        }
    };

    public abstract DirectionType flip();
}
