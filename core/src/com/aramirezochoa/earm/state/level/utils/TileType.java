package com.aramirezochoa.earm.state.level.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by boheme on 7/03/14.
 */
public enum TileType {
    NO_BLOCK("NB"),
    DIG("DG"),
    TOP("TOP") {
        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            return checkCollisionTOP(x0, y0, x1, y1, tile, ZERO_UNO, UNO_UNO);
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case RIGHT:
                    return TOP_RIGHT;
                case LEFT:
                    return TOP_LEFT;
            }
            return this;
        }
    },
    TOP_LEFT("TOPL") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            return LEFT.checkCollisionX(x0, y0, x1, y1, tile);
        }

        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            return checkCollisionTOP(x0, y0, x1, y1, tile, ZERO_UNO, UNO_UNO);
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case RIGHT:
                    return TOP_BOTH;
            }
            return this;
        }
    },
    TOP_RIGHT("TOPR") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            return RIGHT.checkCollisionX(x0, y0, x1, y1, tile);
        }

        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            return checkCollisionTOP(x0, y0, x1, y1, tile, ZERO_UNO, UNO_UNO);
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case LEFT:
                    return TOP_BOTH;
            }
            return this;
        }
    },
    TOP_BOTH("TOPRL") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            if (x0 < x1) {
                return LEFT.checkCollisionX(x0, y0, x1, y1, tile);
            } else {
                return RIGHT.checkCollisionX(x0, y0, x1, y1, tile);
            }
        }

        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            return checkCollisionTOP(x0, y0, x1, y1, tile, ZERO_UNO, UNO_UNO);
        }
    },
    LEFT("LEFT") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            if (x0 < x1 && tile.contains(x1, y1)) {
                return true;
            }
            return false;
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case RIGHT:
                    return RIGHT_LEFT;
            }
            return this;
        }
    },
    RIGHT("RIGHT") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            if (x0 > x1 && tile.contains(x1, y1)) {
                return true;
            }
            return false;
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case LEFT:
                    return RIGHT_LEFT;
            }
            return this;
        }
    },
    RIGHT_LEFT("RL") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            if (x0 < x1) {
                return LEFT.checkCollisionX(x0, y0, x1, y1, tile);
            } else {
                return RIGHT.checkCollisionX(x0, y0, x1, y1, tile);
            }
        }
    },
    DGR1("DGR1") {
        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            Vector2 result = checkCollisionDG(x0, y0, x1, y1, tile, ZERO_ZERO, UNO_UNO);
            if (result != null && (
                    y0 == y1 && x0 < x1
                            || x0 < x1 && y0 > y1 && result.y >= y1
                            || x0 > x1 && y0 > y1 && result.y >= y1 && result.y <= y0)) {
                return result;
            }
            return null;
        }

        @Override
        public boolean isSlope() {
            return true;
        }

        @Override
        public float getGroundY(float x, float size) {
            return x;
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case RIGHT:
                    return BDGR1;
            }
            return this;
        }
    },
    BDGR1("BDGR1") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            return RIGHT.checkCollisionX(x0, y0, x1, y1, tile);
        }

        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            return DGR1.checkCollisionY(x0, y0, x1, y1, tile);
        }

        @Override
        public boolean isSlope() {
            return DGR1.isSlope();
        }

        @Override
        public float getGroundY(float x, float size) {
            return DGR1.getGroundY(x, size);
        }
    },
    DGL1("DGL1") {
        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            Vector2 result = checkCollisionDG(x0, y0, x1, y1, tile, ZERO_UNO, UNO_ZERO);
            if (result != null && (
                    y0 == y1 && x0 > x1
                            || x0 > x1 && y0 > y1 && result.y >= y1
                            || x0 < x1 && y0 > y1 && result.y >= y1 && result.y <= y0)) {
                return result;
            }
            return null;
        }

        @Override
        public boolean isSlope() {
            return true;
        }

        @Override
        public float getGroundY(float x, float size) {
            return size - x;
        }

        @Override
        public TileType addBlockAndGetTile(TileType block) {
            switch (block) {
                case LEFT:
                    return BDGL1;
            }
            return this;
        }
    },
    // Same as diagonal 1 but with collision X axis
    BDGL1("BDGL1") {
        @Override
        public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
            return LEFT.checkCollisionX(x0, y0, x1, y1, tile);
        }

        @Override
        public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
            return DGL1.checkCollisionY(x0, y0, x1, y1, tile);
        }

        @Override
        public boolean isSlope() {
            return DGL1.isSlope();
        }

        @Override
        public float getGroundY(float x, float size) {
            return DGL1.getGroundY(x, size);
        }
    };

    private static Vector2 ZERO_ZERO = new Vector2(0, 0);
    private static Vector2 ZERO_UNO = new Vector2(0, 1);
    private static Vector2 UNO_ZERO = new Vector2(1, 0);
    private static Vector2 UNO_UNO = new Vector2(1, 1);

    private final String code;

    TileType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public boolean checkCollisionX(float x0, float y0, float x1, float y1, Rectangle tile) {
        return false;
    }

    public Vector2 checkCollisionY(float x0, float y0, float x1, float y1, Rectangle tile) {
        return null;
    }

    protected Vector2 checkCollisionTOP(float x0, float y0, float x1, float y1, Rectangle tile, Vector2 origin, Vector2 end) {
        Vector2 result = new Vector2();
        if (Intersector.intersectLines(x0, y0, x1, y1,
                tile.x + origin.x * tile.getWidth(), tile.y + origin.y * tile.getHeight(),
                tile.x + end.x * tile.getWidth(), tile.y + end.y * tile.getHeight(),
                result) && y0 >= result.y && result.y >= y1) {
            return result;
        }
        return null;
    }

    protected Vector2 checkCollisionDG(float x0, float y0, float x1, float y1, Rectangle tile, Vector2 origin, Vector2 end) {
        Vector2 result = new Vector2();
        float size = tile.getWidth();
        Intersector.intersectLines(x1, tile.y, x1, tile.y + tile.getHeight(), tile.x + origin.x * size, tile.y + origin.y * size, tile.x + end.x * size, tile.y + end.y * size, result);
        return result;
    }

    public static TileType getTileType(TiledMapTileLayer.Cell cell) {
        if (cell == null || cell.getTile() == null || cell.getTile().getProperties().get("type") == null) {
            return NO_BLOCK;
        }
        return parseHillType((String) cell.getTile().getProperties().get("type"));
    }

    private static TileType parseHillType(String hill) {
        for (TileType tileType : TileType.values()) {
            if (tileType.code.equals(hill)) {
                return tileType;
            }
        }
        return NO_BLOCK;
    }

    public boolean isSlope() {
        return false;
    }

    public float getGroundY(float x, float size) {
        return 0;
    }

    /**
     * @param block: LEFT, RIGHT
     * @return TileType adding the block typed with 'block'
     */
    public TileType addBlockAndGetTile(TileType block) {
        return this;
    }
}
