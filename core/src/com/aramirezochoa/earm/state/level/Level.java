package com.aramirezochoa.earm.state.level;

import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.avatar.EnemyAvatar;
import com.aramirezochoa.earm.state.level.avatar.InteractiveAvatar;
import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.level.map.AnimatedStaticTiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by boheme on 17/02/14.
 */
public class Level {

    private final int number;
    private Vector2 gravity = new Vector2(0f, 0f);
    private float width;
    private float height;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private int tileSize;
    private Map<AvatarCommand, AtomicInteger> actions = new TreeMap<AvatarCommand, AtomicInteger>();
    private boolean lose = false;
    private boolean win = false;
    private WorldType world;
    private int flairyTaken = 0;
    private boolean tutorial = false;
    private List<TutorialAction> tutorialActions = new ArrayList<TutorialAction>();

    private Avatar earmAvatar;
    private final Set<EnemyAvatar> enemyAvatars = new HashSet<EnemyAvatar>();
    private final Set<InteractiveAvatar> interactiveAvatars = new HashSet<InteractiveAvatar>();
    private TutorialAction nextAction;

    private List<AnimatedStaticTiledMapTile> animatedTiles = new ArrayList<AnimatedStaticTiledMapTile>();

    public Level(int levelNumber) {
        this.number = levelNumber;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public void setRenderer(OrthogonalTiledMapRenderer renderer) {
        this.renderer = renderer;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2 getGravity() {
        return gravity;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Map<AvatarCommand, AtomicInteger> getActions() {
        return actions;
    }

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setWorld(WorldType world) {
        this.world = world;
    }

    public WorldType getWorld() {
        return world;
    }

    public int getNumber() {
        return number;
    }

    public void dispose() {
        renderer.dispose();
        tiledMap.dispose();
    }

    public void incFlairyTaken() {
        flairyTaken++;
    }

    public int getFlairyTaken() {
        return flairyTaken;
    }

    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public void addAction(Rectangle rectangle, String name) {
        tutorialActions.add(new TutorialAction(rectangle, name));
    }

    public List<TutorialAction> getTutorialActions() {
        return tutorialActions;
    }

    public Avatar getEarmAvatar() {
        return earmAvatar;
    }

    public void setEarmAvatar(Avatar earmAvatar) {
        this.earmAvatar = earmAvatar;
    }

    public Set<EnemyAvatar> getEnemyAvatars() {
        return enemyAvatars;
    }

    public Set<InteractiveAvatar> getInteractiveAvatars() {
        return interactiveAvatars;
    }

    public void setNextAction(TutorialAction nextAction) {
        this.nextAction = nextAction;
        nextAction.setProcessed(true);
    }

    public TutorialAction getNextAction() {
        return nextAction;
    }

    public List<AnimatedStaticTiledMapTile> getAnimatedTiles() {
        return animatedTiles;
    }

    public class TutorialAction {
        Rectangle rectangle;
        String action;
        boolean done;

        public TutorialAction(Rectangle rectangle, String action) {
            this.rectangle = rectangle;
            this.action = action;
            this.done = false;
        }

        public void setProcessed(boolean done) {
            this.done = done;
        }

        public boolean isDone() {
            return done;
        }

        public Rectangle getRectangle() {
            return rectangle;
        }

        public String getAction() {
            return action;
        }
    }
}
