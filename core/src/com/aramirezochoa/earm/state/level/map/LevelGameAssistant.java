package com.aramirezochoa.earm.state.level.map;

import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.utils.Point2;
import com.aramirezochoa.earm.state.level.utils.TileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Set;

/**
 * Created by boheme on 18/02/14.
 */
public enum LevelGameAssistant {
    INSTANCE;

    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Pool<Point2> pointPool = new Pool<Point2>() {
        @Override
        protected Point2 newObject() {
            return new Point2();
        }
    };
    private Array<Rectangle> tiles = new Array<Rectangle>();

    public void updateAvatar(Avatar avatar, Level level, float deltaTime) {

        avatar.getVelocity().add(level.getGravity()).add(avatar.getAcceleration());

        if (Math.abs(avatar.getVelocity().x) < 1) {
            avatar.getVelocity().x = 0;
        }

        avatar.getVelocity().scl(deltaTime);

        // Check collisions
        checkCollisionX(avatar, level);
        checkCollisionY(avatar, level);

        avatar.getPosition().add(avatar.getVelocity());

        avatar.getVelocity().scl(1 / deltaTime);

        controlSides(avatar, level);
        avatar.dumpVelocity();
    }

    private void checkCollisionX(Avatar avatar, Level level) {

        Rectangle avatarRect = createRectangleFromAvatar(avatar);
        avatarRect.x += avatar.getVelocity().x;

        // Points of interest
        Point2 sidePosition = pointPool.obtain();
        sidePosition.x = avatar.getVelocity().x > 0 ? avatarRect.x + avatarRect.width : avatarRect.x;
        sidePosition.y = avatarRect.y + 5 + avatarRect.width / 2;

        int startX, startY, endX, endY;
        if (avatar.getVelocity().x > 0) {
            startX = endX = (int) (avatarRect.x + avatarRect.width);
        } else {
            startX = endX = (int) (avatarRect.x);
        }
        startY = (int) (avatar.getPosition().y);
        endY = (int) (avatar.getPosition().y + avatar.getDimension().y);
        getTiles(startX, startY, endX, endY, tiles, level, avatar.getVelocity().x > 0);

        for (Rectangle tile : tiles) {
            if (sidePosition.overlaps(tile)) {
                TiledMapTileLayer.Cell cell = getCell(tile.x, tile.y, level);
                if (AvatarAction.DIG.equals(avatar.getCurrentAction())) {
                    digTile(avatar, level, cell, tile);
                } else {
                    TileType tileType = TileType.getTileType(cell);
                    if (!TileType.NO_BLOCK.equals(tileType)) {
                        if (tileType.checkCollisionX(avatar.getPosition().x + avatar.getWidth() / 2, avatar.getPosition().y, sidePosition.x, sidePosition.y, tile)) {
                            avatar.getVelocity().x = 0;
                            avatar.notifyWall();
                        }
                    }
                }
                break;
            }
        }
        pointPool.free(sidePosition);
        rectPool.free(avatarRect);
    }

    private void digTile(Avatar avatar, Level level, TiledMapTileLayer.Cell cellBase, Rectangle tile) {
        Gdx.app.error("DIG", "!!!");

        // Main cell
        if (!TileType.DIG.equals(TileType.getTileType(cellBase))) {
            AnimatedStaticTiledMapTile animatedTile = new AnimatedStaticTiledMapTile(cellBase.getTile().getTextureRegion(), MediaManager.INSTANCE.getTileAnimation("DIG", level.getWorld().name()));
            level.getAnimatedTiles().add(animatedTile);
            cellBase.setTile(animatedTile);
        }
        // Top cell
        TiledMapTileLayer.Cell topCell = getCell(tile.x, tile.y + tile.getHeight(), level);
        if (topCell != null && !TileType.DIG.equals(TileType.getTileType(topCell))) {
            AnimatedStaticTiledMapTile animatedTile = new AnimatedStaticTiledMapTile(topCell.getTile().getTextureRegion(), MediaManager.INSTANCE.getTileAnimation("DIG", level.getWorld().name()));
            level.getAnimatedTiles().add(animatedTile);
            topCell.setTile(animatedTile);
        }
        // Bottom colliding cell
        TiledMapTileLayer.Cell bottomCell = getCell(tile.x, tile.y - tile.getHeight(), level);
        if (bottomCell != null && !TileType.DIG.equals(TileType.getTileType(bottomCell))) {
            bottomCell.setTile(new StaticTiledMapTile((StaticTiledMapTile) bottomCell.getTile()));
            bottomCell.getTile().getProperties().put("type", TileType.TOP.getCode());
        }
        // Side cell
        TiledMapTileLayer.Cell sideCell;
        TileType blockType;
        if (avatar.getVelocity().x >= 0) {
            sideCell = getCell(tile.x + tile.getWidth(), tile.y, level);
            blockType = TileType.LEFT;
        } else {
            sideCell = getCell(tile.x - tile.getWidth(), tile.y, level);
            blockType = TileType.RIGHT;
        }
        if (sideCell != null && !TileType.DIG.equals(TileType.getTileType(sideCell))) {
            sideCell.setTile(new StaticTiledMapTile((StaticTiledMapTile) sideCell.getTile()));
            sideCell.getTile().getProperties().put("type", blockType.getCode());
        }
    }

    private void checkCollisionY(Avatar avatar, Level level) {

        Rectangle avatarRect = rectPool.obtain();
        avatarRect.set(avatar.getPosition().x, avatar.getPosition().y, avatar.getDimension().x, avatar.getDimension().y);
        avatarRect.x += avatar.getVelocity().x;
        avatarRect.y += avatar.getVelocity().y;
        if (avatarRect.y < 0) avatarRect.y = 0;

        // Points of interest
        Rectangle collisionPoint = rectPool.obtain();
        collisionPoint.set(avatarRect.x + avatar.getDimension().x / 2, avatar.getVelocity().y > 0 ? avatarRect.y + avatarRect.height : avatarRect.y, 1, 1);

        int startX, startY, endX, endY;
        if (avatar.getVelocity().y <= 0) {
            startY = (int) (avatarRect.y);
            endY = (int) (avatarRect.y) + level.getTileSize();
        } else {
            startY = endY = (int) (avatarRect.y + avatarRect.height);
        }
        startX = endX = (int) (avatarRect.x + avatarRect.getWidth() / 2);
        getTiles(startX, startY, endX, endY, tiles, level, avatar.getVelocity().x >= 0);

        boolean wasGrounded = avatar.grounded;
        avatar.grounded = false;
        // It gets the tiles ordered depending on the direction side (left/right)
        for (Rectangle tile : tiles) {
            TileType tileType = TileType.getTileType(getCell(tile.x, tile.y, level));
            if (!TileType.NO_BLOCK.equals(tileType)) {
                if (avatar.getVelocity().y <= 0) {
                    Vector2 intersection = tileType.checkCollisionY(avatar.getPosition().x + avatar.getWidth() / 2, wasGrounded ? avatar.getPosition().y + (tile.getHeight() / 4) : avatar.getPosition().y, collisionPoint.x, collisionPoint.y, tile);
                    if (intersection != null) {
                        avatar.getPosition().y = intersection.y;
                        avatar.getVelocity().y = 0;
                        avatar.grounded = true;
                    } else if (wasGrounded && tileType.isSlope() && (AvatarAction.IDLE.equals(avatar.getCurrentAction()) || AvatarAction.WALK.equals(avatar.getCurrentAction()))) {
                        avatar.getPosition().y = tile.y + tileType.getGroundY((avatar.getPosition().x + avatar.getWidth() / 2) - tile.getX(), tile.getWidth());
                        avatar.getVelocity().y = 0;
                        avatar.grounded = true;
                    }
                }
            }
        }

        if (wasGrounded && !avatar.grounded) {
            avatar.notifyAbyss();
        }
        rectPool.free(collisionPoint);
        rectPool.free(avatarRect);
    }

    private void controlSides(Avatar avatar, Level level) {
        Point2 position = avatar.getPosition();
        Point2 dimension = avatar.getDimension();
        if (position.x < 0) {
            position.x = 0;
            avatar.getVelocity().x = 0;
            avatar.notifyWall();
        }
        if (position.x + dimension.x > level.getWidth()) {
            position.x = level.getWidth() - dimension.x;
            avatar.getVelocity().x = 0;
            avatar.notifyWall();
        }
        if (position.y < 0) {
            avatar.getVelocity().y = 0;
            position.y = 0;
        }
        if (position.y + dimension.y > level.getHeight()) {
            position.y = level.getHeight() - dimension.y;
        }
    }

    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles, Level level, boolean right) {
        TiledMapTileLayer layer = (TiledMapTileLayer) level.getTiledMap().getLayers().get("ground");
        int tileSize = level.getTileSize();
        rectPool.freeAll(tiles);
        tiles.clear();

        if (right) {
            for (int y = startY / tileSize; y <= endY / tileSize; y++) {
                for (int x = startX / tileSize; x <= endX / tileSize; x++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        Rectangle rect = rectPool.obtain();
                        rect.set(x * tileSize, y * tileSize, tileSize, tileSize);
                        tiles.add(rect);
                    }
                }
            }
        } else {
            for (int y = startY / tileSize; y <= endY / tileSize; y++) {
                for (int x = endX / tileSize; x >= startX / tileSize; x--) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        Rectangle rect = rectPool.obtain();
                        rect.set(x * tileSize, y * tileSize, tileSize, tileSize);
                        tiles.add(rect);
                    }
                }
            }
        }
    }

    private TiledMapTileLayer.Cell getCell(float x, float y, Level level) {
        TiledMapTileLayer layer = (TiledMapTileLayer) level.getTiledMap().getLayers().get("ground");
        int tileSize = level.getTileSize();
        return layer.getCell((int) x / tileSize, (int) y / tileSize);
    }

    public void checkAvatarsCollision(Avatar mainAvatar, Set<? extends Avatar> otherAvatars) {
        Rectangle mainAvatarRect = createRectangleFromAvatar(mainAvatar);
        for (Avatar otherAvatar : otherAvatars) {
            Rectangle otherAvatarRect = createRectangleFromAvatar(otherAvatar);
            if (otherAvatar.isVisible() && mainAvatarRect.overlaps(otherAvatarRect)) {
                otherAvatar.collisionWith(mainAvatar);
            }
            rectPool.free(otherAvatarRect);
        }
        rectPool.free(mainAvatarRect);
    }

    private Rectangle createRectangleFromAvatar(Avatar avatar) {
        Rectangle rect = rectPool.obtain();
        rect.set(avatar.getPosition().x, avatar.getPosition().y, avatar.getDimension().x, avatar.getDimension().y);
        return rect;
    }

    public boolean checkRectangleCollision(Avatar avatar, Rectangle rectangle) {
        Rectangle avatarRect = createRectangleFromAvatar(avatar);
        return avatarRect.overlaps(rectangle);
    }
}
