package com.aramirezochoa.earm.state.level;

import com.aramirezochoa.earm.AbstractScreen;
import com.aramirezochoa.earm.ActivityManager;
import com.aramirezochoa.earm.input.HandleInput;
import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.level.avatar.Avatar;
import com.aramirezochoa.earm.state.level.avatar.EarmAvatar;
import com.aramirezochoa.earm.state.level.avatar.EnemyAvatar;
import com.aramirezochoa.earm.state.level.avatar.InteractiveAvatar;
import com.aramirezochoa.earm.state.level.avatar.command.AvatarCommand;
import com.aramirezochoa.earm.state.level.avatar.types.AvatarType;
import com.aramirezochoa.earm.state.level.avatar.types.DirectionType;
import com.aramirezochoa.earm.state.level.manager.LevelManager;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.aramirezochoa.earm.state.level.state.LevelScreenState;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.aramirezochoa.earm.state.level.utils.Point2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by boheme on 11/02/14.
 */
public class LevelScreen extends AbstractScreen {

    private static final String STAGES_PATH = "data/stages/";
    private static final String TUTORIAL_PATH = "data/stages/tutorials/";

    private static final Pool<Avatar> earmPool = new Pool<Avatar>() {
        @Override
        protected EarmAvatar newObject() {
            return new EarmAvatar();
        }
    };

    private static final Pool<EnemyAvatar> enemyPool = new Pool<EnemyAvatar>() {
        @Override
        protected EnemyAvatar newObject() {
            return new EnemyAvatar();
        }
    };
    private static final Pool<InteractiveAvatar> interactivePool = new Pool<InteractiveAvatar>() {
        @Override
        protected InteractiveAvatar newObject() {
            return new InteractiveAvatar();
        }
    };

    private Level level;
    private Stage gameStage, guiStage;
    private LevelScreenState levelState;
    private SpriteBatch spriteBatch;
    private TextureRegion background;
    private OrthographicCamera camera;

    private long pauseTimer;

    @Override
    public void init(int levelNumber, boolean doTutorial) {
        this.level = createLevel(LevelManager.getWorldType(levelNumber), levelNumber, doTutorial);

        // Initialize game stage & stuff
        gameStage = new Stage(new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
        spriteBatch = new SpriteBatch();
        // Set camera for background & tiledMap
        camera = new OrthographicCamera(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        camera.setToOrtho(false, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        camera.update();
        gameStage.getViewport().setCamera(camera);
        spriteBatch.setProjectionMatrix(camera.combined);

        background = MediaManager.INSTANCE.getBackground(level.getWorld());

        gameStage.addActor(level.getEarmAvatar());

        // Initialize GUI
        guiStage = new Stage(new StretchViewport(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
        LevelScreenState.initStates(level, gameStage, guiStage);
        levelState = LevelScreenState.INGAME;
        levelState.enable();

        // Initialize InputProcessor
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(new InputAdapter());
        im.addProcessor(guiStage);
        im.addProcessor(gameStage);
        Gdx.input.setInputProcessor(im);

        // Add characters to stage
        for (Avatar avatar : level.getInteractiveAvatars()) {
            gameStage.addActor(avatar);
        }
        for (Avatar avatar : level.getEnemyAvatars()) {
            gameStage.addActor(avatar);
        }
        gameStage.addActor(level.getEarmAvatar());
    }

    @Override
    public void pause() {
        if (LevelScreenState.INGAME.equals(levelState)) {
            levelState.disable();
            levelState = LevelScreenState.PAUSE;
            levelState.enable();
        }
        pauseTimer = TimeUtils.millis();
    }

    @Override
    public void resume() {
        long actual = TimeUtils.millis();
        // Wait 2min to dispose send to loading screen
        if (actual - pauseTimer > 120000) {
            super.resume();
        }
    }

    @Override
    public void show() {
        // All done in init()
    }

    @Override
    public void hide() {
        if (level != null) {
            SoundManager.INSTANCE.pauseTheme(level.getWorld());
        }
        super.hide();
        ActivityManager.INSTANCE.showAdTop(false);
    }

    @Override
    public void render(float deltaTime) {

        Gdx.graphics.getGL20().glClearColor(1, 1, 1, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        deltaTime = deltaTime > Constant.MAX_LAG ? Constant.MAX_LAG : deltaTime;

        // Draw
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        level.getRenderer().setView(camera);
        level.getRenderer().render();

        gameStage.draw();
        levelState.render(deltaTime);

        // Update: handle input, update avatars & GUI status
        HandleInput.INSTANCE.getAvatarCommand().execute(level.getEarmAvatar());
        HandleInput.INSTANCE.getAvatarMovementCommand().execute(level.getEarmAvatar());

        LevelScreenState lastState = levelState;
        levelState = levelState.update(deltaTime);
        if (!lastState.equals(levelState)) {
            lastState.disable();
            levelState.enable();
        }

        updateCamera();
        SoundManager.INSTANCE.playTheme(level.getWorld());
    }

    private void updateCamera() {
        // Cast to int because of the bleeding problem :)
        Point2 position = level.getEarmAvatar().getPosition();
        if (position.x < Constant.SCREEN_WIDTH / 2) {
            camera.position.x = Constant.SCREEN_WIDTH / 2;
        } else if (position.x > level.getWidth() - (Constant.SCREEN_WIDTH / 2)) {
            camera.position.x = level.getWidth() - (Constant.SCREEN_WIDTH / 2);
        } else {
            camera.position.x = (int) position.x;
        }
        if (position.y < Constant.SCREEN_HEIGHT / 2) {
            camera.position.y = Constant.SCREEN_HEIGHT / 2;
        } else if (position.y > level.getHeight() - (Constant.SCREEN_HEIGHT / 2)) {
            camera.position.y = level.getHeight() - (Constant.SCREEN_HEIGHT / 2);
        } else {
            camera.position.y = (int) position.y;
        }
        camera.update();
    }

    @Override
    public void dispose() {
        Gdx.app.log("Level", "Dispose");
        try {
            // Earm
            earmPool.free(level.getEarmAvatar());
            // Enemies
            for (EnemyAvatar avatar : level.getEnemyAvatars()) {
                enemyPool.free(avatar);
            }
            level.getEnemyAvatars().clear();
            // Interactives
            for (InteractiveAvatar avatar : level.getInteractiveAvatars()) {
                interactivePool.free(avatar);
            }
            level.getInteractiveAvatars().clear();

            level.dispose();
            guiStage.dispose();
            gameStage.dispose();
            spriteBatch.dispose();
        } catch (Exception e) {
            Gdx.app.error("Level", "Error while disposing. No init or called twice?");
        }
    }

    private Level createLevel(WorldType world, int levelNumber, boolean doTutorial) {
        TiledMap map;
        if (doTutorial) {
            Gdx.app.log("Level", "Loading tutorial for level " + levelNumber);
            map = new TmxMapLoader().load(TUTORIAL_PATH + levelNumber + ".tmx");
        } else {
            Gdx.app.log("Level", "Loading level " + levelNumber);
            map = new TmxMapLoader().load(STAGES_PATH + LevelManager.getWorldType(levelNumber).getDescription() + "/" + levelNumber + ".tmx");
        }
        OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, 1);

        Level level = new Level(levelNumber);
        if (doTutorial) {
            level.setTutorial(doTutorial);
            setTutorialAction(level, map.getLayers().get("actions"));
        }
        level.setWorld(world);
        level.setTiledMap(map);
        level.setRenderer(renderer);

        int width = (Integer) map.getProperties().get("width");
        int height = (Integer) map.getProperties().get("height");
        int tileSize = (Integer) map.getProperties().get("tilewidth");

        level.setTileSize(tileSize);
        level.setWidth(width * tileSize);
        level.setHeight(height * tileSize);
        level.getGravity().y = map.getProperties().get("gravity") == null ? Constant.DEFAULT_GRAVITY.y : Float.parseFloat((String) map.getProperties().get("gravity"));

        // Load avatars
        loadAvatars(map, level);
        // Load action limits
        loadActionLimits(map, level);

        return level;
    }

    private void setTutorialAction(Level level, MapLayer actions) {
        for (MapObject rectangleMapObject : actions.getObjects()) {
            level.addAction(((RectangleMapObject) rectangleMapObject).getRectangle(), rectangleMapObject.getName());
        }
    }

    private void loadActionLimits(TiledMap map, Level level) {
        Iterator iter = map.getProperties().getKeys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            AvatarCommand command = AvatarCommand.parseAvatarCommand(key);
            if (!AvatarCommand.NO_COMMAND.equals(command)) {
                level.getActions().put(command, new AtomicInteger(Integer.parseInt((String) map.getProperties().get(key))));
            }
        }
    }

    // TODO : better create a factory
    private void loadAvatars(TiledMap map, Level level) {
        for (MapObject mapObject : map.getLayers().get("avatars").getObjects()) {
            AvatarType avatarType = AvatarType.getType(mapObject.getName());
            DirectionType lookingAt = Boolean.parseBoolean((String) mapObject.getProperties().get("type")) ? DirectionType.LEFT : DirectionType.RIGHT;
            switch (avatarType) {
                case EARM:
                    level.setEarmAvatar(earmPool.obtain());
                    level.getEarmAvatar().init(avatarType, new Point2(((TiledMapTileMapObject) mapObject).getX(), ((TiledMapTileMapObject) mapObject).getY()), level, lookingAt);
                    break;
                case SOLDIER:
                case SOLFLYER:
                    EnemyAvatar enemyAvatar = enemyPool.obtain();
                    if (mapObject instanceof RectangleMapObject) {
                        enemyAvatar.init(avatarType, new Point2(((RectangleMapObject) mapObject).getRectangle().x, ((RectangleMapObject) mapObject).getRectangle().y), level, lookingAt);
                    } else {
                        enemyAvatar.init(avatarType, new Point2(((TiledMapTileMapObject) mapObject).getX(), ((TiledMapTileMapObject) mapObject).getY()), level, lookingAt);
                    }
                    level.getEnemyAvatars().add(enemyAvatar);
                    break;
                case GOAL:
                case FLAIRY:
                case CANON_SNOW:
                case LANDMINE:
                    //
                    float info = getSpeed((String) mapObject.getProperties().get("type"), 1);
                    InteractiveAvatar interactiveAvatar = interactivePool.obtain();
                    if (mapObject instanceof RectangleMapObject) {
                        interactiveAvatar.init(avatarType, new Point2(((RectangleMapObject) mapObject).getRectangle().x, ((RectangleMapObject) mapObject).getRectangle().y), level, lookingAt, info);
                    } else {
                        interactiveAvatar.init(avatarType, new Point2(((TiledMapTileMapObject) mapObject).getX(), ((TiledMapTileMapObject) mapObject).getY()), level, lookingAt, info);
                    }
                    level.getInteractiveAvatars().add(interactiveAvatar);
                    break;
                default:
                    // Do nothing
                    break;
            }
        }
    }

    private float getSpeed(String speed, float defaultValue) {
        if (speed == null) {
            return defaultValue;
        }
        try {
            return Float.valueOf(speed);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
