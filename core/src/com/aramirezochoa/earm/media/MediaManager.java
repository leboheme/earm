package com.aramirezochoa.earm.media;

import com.aramirezochoa.earm.Manager;
import com.aramirezochoa.earm.common.ButtonContext;
import com.aramirezochoa.earm.common.ButtonSize;
import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.avatar.types.AvatarType;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by boheme on 13/02/14.
 */
public enum MediaManager implements Manager {
    INSTANCE();

    private static final String TILES_ATLAS_PATH = "data/images/tiles.atlas";
    private static final String LEVEL_ATLAS_PATH = "data/images/level.atlas";
    public static final String MENU_ATLAS_PATH = "data/images/menu.atlas";

    private static final String JUMP_SOUND_PATH = "data/sounds/jump.ogg";
    private static final String FLY_SOUND_PATH = "data/sounds/fly.ogg";
    private static final String DEAD_ALT_SOUND_PATH = "data/sounds/dead_altitude.ogg";
    private static final String STOP_SOUND_PATH = "data/sounds/stop.ogg";
    private static final String DEAD_SOUND_PATH = "data/sounds/dead.ogg";
    private static final String PARACHUTE_SOUND_PATH = "data/sounds/parachute.ogg";
    private static final String GOAL_SOUND_PATH = "data/sounds/goal.ogg";
    private static final String FLAIRY_SOUND_PATH = "data/sounds/flairy.ogg";

    private static final String WOODS_THEME = "data/sounds/woods_theme.ogg";
    private static final String SNOW_THEME = "data/sounds/snow_theme.ogg";
    private static final String JUNGLE_THEME = "data/sounds/jungle_theme.ogg";
    private static final String MENU_THEME = "data/sounds/menu_theme.ogg";
    private static final String BUTTON_SOUND = "data/sounds/button_sound.ogg";

    private Map<AvatarType, Map<AvatarAction, Animation>> levelAnimations;
    private TextureAtlas tilesTextureAtlas;
    private TextureAtlas levelTextureAtlas;
    private TextureAtlas menuTextureAtlas;
    private Map<AvatarType, Map<AvatarAction, Sound>> sounds;
    private Map<WorldType, Music> themes;

    private BitmapFont tutorialFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
    private BitmapFont mediumFont = new BitmapFont(Gdx.files.internal("data/PP_Handwriting.fnt"), Gdx.files.internal("data/PP_Handwriting.png"), false);
    private BitmapFont smallFont = new BitmapFont(Gdx.files.internal("data/PP_Handwriting.fnt"), Gdx.files.internal("data/PP_Handwriting.png"), false);
    private Color fontColor = new Color(0.145f, 0, 0.157f, 1.0f);

    private boolean loaded = false;
    private Sound buttonSound;
    private Label.LabelStyle tutorialLabelStyle;

    public void init() {
        if (loaded) {
            loaded = false;
            dispose();
        }
        create();
        // Load avatar textures
        tilesTextureAtlas = new TextureAtlas(Gdx.files.internal(TILES_ATLAS_PATH));
        levelTextureAtlas = new TextureAtlas(Gdx.files.internal(LEVEL_ATLAS_PATH));
        menuTextureAtlas = new TextureAtlas(Gdx.files.internal(MENU_ATLAS_PATH));
        loadEarm();
        loadOthers();
        loadOtherSounds();

        loaded = true;
    }

    private void create() {
        levelAnimations = new HashMap<>();
        sounds = new HashMap<>();
        themes = new HashMap<>();
        tutorialFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
        mediumFont = new BitmapFont(Gdx.files.internal("data/PP_Handwriting.fnt"), Gdx.files.internal("data/PP_Handwriting.png"), false);
        smallFont = new BitmapFont(Gdx.files.internal("data/PP_Handwriting.fnt"), Gdx.files.internal("data/PP_Handwriting.png"), false);
        smallFont.setColor(Color.BLACK);
        smallFont.getData().setScale(0.45f, 0.45f);
    }

    private void dispose() {
        tutorialFont.dispose();
        mediumFont.dispose();
        smallFont.dispose();
        tilesTextureAtlas.dispose();
        levelTextureAtlas.dispose();
        menuTextureAtlas.dispose();

        for (Music music : themes.values()) {
            music.dispose();
        }
        for (AvatarType avatarType : sounds.keySet()) {
            for (Sound sound : sounds.get(avatarType).values()) {
                sound.dispose();
            }
        }
        buttonSound.dispose();
    }

    private void loadOtherSounds() {
        themes.put(WorldType.WOODS, Gdx.audio.newMusic(Gdx.files.internal(WOODS_THEME)));
        themes.put(WorldType.SNOW, Gdx.audio.newMusic(Gdx.files.internal(SNOW_THEME)));
        themes.put(WorldType.JUNGLE, Gdx.audio.newMusic(Gdx.files.internal(JUNGLE_THEME)));
        themes.put(WorldType.MAIN_MENU, Gdx.audio.newMusic(Gdx.files.internal(MENU_THEME)));

        buttonSound = Gdx.audio.newSound(Gdx.files.internal(BUTTON_SOUND));
    }

    private void loadEarm() {
        Map<AvatarAction, Animation> animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.IDLE, loadAnimation(AvatarType.EARM, AvatarAction.IDLE, 0.1f));
        animations.put(AvatarAction.WALK, loadAnimation(AvatarType.EARM, AvatarAction.WALK, 0.1f));
        animations.put(AvatarAction.JUMP, loadAnimation(AvatarType.EARM, AvatarAction.JUMP, 0.1f));
        animations.put(AvatarAction.FALL, loadAnimation(AvatarType.EARM, AvatarAction.FALL, 0.05f));
        animations.put(AvatarAction.FLY, loadAnimation(AvatarType.EARM, AvatarAction.FLY, 0.1f));
        animations.put(AvatarAction.STOP, loadAnimation(AvatarType.EARM, AvatarAction.STOP, 0.1f));
        animations.put(AvatarAction.DEAD, loadAnimation(AvatarType.EARM, AvatarAction.DEAD, 0.1f));
        animations.put(AvatarAction.DEAD_ALTITUDE, loadAnimation(AvatarType.EARM, AvatarAction.DEAD_ALTITUDE, 0.1f));
        animations.put(AvatarAction.GOAL, loadAnimation(AvatarType.EARM, AvatarAction.GOAL, 0.1f));
        animations.put(AvatarAction.DIG, loadAnimation(AvatarType.EARM, AvatarAction.DIG, 0.1f));
        animations.put(AvatarAction.PARACHUTE, loadAnimation(AvatarType.EARM, AvatarAction.PARACHUTE, 0.1f));
        animations.put(AvatarAction.CLIMB, loadAnimation(AvatarType.EARM, AvatarAction.CLIMB, 0.1f));
        this.levelAnimations.put(AvatarType.EARM, animations);

        Map<AvatarAction, Sound> sounds = new HashMap<AvatarAction, Sound>();
        sounds.put(AvatarAction.JUMP, Gdx.audio.newSound(Gdx.files.internal(JUMP_SOUND_PATH)));
        sounds.put(AvatarAction.FLY, Gdx.audio.newSound(Gdx.files.internal(FLY_SOUND_PATH)));
        sounds.put(AvatarAction.DEAD_ALTITUDE, Gdx.audio.newSound(Gdx.files.internal(DEAD_ALT_SOUND_PATH)));
        sounds.put(AvatarAction.STOP, Gdx.audio.newSound(Gdx.files.internal(STOP_SOUND_PATH)));
        sounds.put(AvatarAction.DEAD, Gdx.audio.newSound(Gdx.files.internal(DEAD_SOUND_PATH)));
        sounds.put(AvatarAction.PARACHUTE, Gdx.audio.newSound(Gdx.files.internal(PARACHUTE_SOUND_PATH)));
        sounds.put(AvatarAction.GOAL, Gdx.audio.newSound(Gdx.files.internal(GOAL_SOUND_PATH)));
        this.sounds.put(AvatarType.EARM, sounds);
    }

    private void loadOthers() {
        Map<AvatarAction, Animation> animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.PATROL, loadAnimation(AvatarType.SOLDIER, AvatarAction.PATROL, 0.1f));
        this.levelAnimations.put(AvatarType.SOLDIER, animations);

        animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.WAGGLE, loadAnimation(AvatarType.SOLFLYER, AvatarAction.WAGGLE, 0.1f));
        this.levelAnimations.put(AvatarType.SOLFLYER, animations);

        animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.IDLE, loadAnimation(AvatarType.GOAL, AvatarAction.IDLE, 0.1f));
        animations.put(AvatarAction.FALL, loadAnimation(AvatarType.GOAL, AvatarAction.FALL, 0.1f));
        this.levelAnimations.put(AvatarType.GOAL, animations);

        animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.STAND, loadAnimation(AvatarType.FLAIRY, AvatarAction.STAND, 0.1f));
        this.levelAnimations.put(AvatarType.FLAIRY, animations);
        Map<AvatarAction, Sound> sounds = new HashMap<AvatarAction, Sound>();
        sounds.put(AvatarAction.STAND, Gdx.audio.newSound(Gdx.files.internal(FLAIRY_SOUND_PATH)));
        this.sounds.put(AvatarType.FLAIRY, sounds);

        animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.IDLE, loadAnimation(AvatarType.CANON_SNOW, AvatarAction.IDLE, 0.1f));
        animations.put(AvatarAction.FALL, loadAnimation(AvatarType.CANON_SNOW, AvatarAction.IDLE, 0.1f));
        this.levelAnimations.put(AvatarType.CANON_SNOW, animations);

        animations = new HashMap<AvatarAction, Animation>();
        animations.put(AvatarAction.RED, loadAnimation(AvatarType.LANDMINE, AvatarAction.RED, 0.1f));
        animations.put(AvatarAction.GREEN, loadAnimation(AvatarType.LANDMINE, AvatarAction.GREEN, 0.1f));
        this.levelAnimations.put(AvatarType.LANDMINE, animations);
    }

    private Animation loadAnimation(AvatarType avatarType, AvatarAction avatarAction, float frameDuration) {
        String region = avatarType.name() + "_" + avatarAction.name();
        return new Animation(frameDuration, levelTextureAtlas.findRegions(region));
    }

    public Map<AvatarAction, Animation> getAnimations(AvatarType type) {
        return levelAnimations.get(type);
    }

    public Map<AvatarAction, Animation> getAnimations(AvatarType type, int speed) {
        Map<AvatarAction, Animation> animations = new HashMap<AvatarAction, Animation>();
        Animation animation = new Animation(0.05f * speed, levelTextureAtlas.findRegions(type.name()));
        animations.put(AvatarAction.IDLE, animation);
        return animations;
    }

    public Map<AvatarType, Map<AvatarAction, Sound>> getSounds() {
        return sounds;
    }

    public Sound getButtonSound() {
        return buttonSound;
    }

    public Map<WorldType, Music> getThemes() {
        return themes;
    }

    public TextureRegion getBackground(WorldType world) {
        return menuTextureAtlas.findRegion(world.name() + "_" + "BACKGROUND");
    }

    public TextureAtlas.AtlasRegion getMenuEarm() {
        return menuTextureAtlas.findRegion("EARM");
    }

    public TextureAtlas.AtlasRegion getMenuTitle() {
        return menuTextureAtlas.findRegion("TITLE");
    }

    public TextureAtlas.AtlasRegion getSubMenu() {
        return levelTextureAtlas.findRegion("SUBMENU");
    }

    public TextureAtlas.AtlasRegion getSadEarm() {
        return levelTextureAtlas.findRegion("EARM_SAD");
    }

    public TextureAtlas.AtlasRegion getWinScore(int i) {
        return levelTextureAtlas.findRegion("WIN_SCORE", i);
    }

    public TextureAtlas.AtlasRegion getGuiFlairy() {
        return levelTextureAtlas.findRegion("LIT_FLAIRY");
    }

    public Button.ButtonStyle getButtonStyle(ButtonSize buttonSize, ButtonContext buttonContext, String name) {
        String key = buttonSize.name() + "_BUTTON_" + name;
        Button.ButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = getTextureRegionDrawable(buttonContext, key, 0);
        buttonStyle.down = getTextureRegionDrawable(buttonContext, key, 1);
        return buttonStyle;
    }

    public CheckBox.CheckBoxStyle getCheckBoxStyle(ButtonSize buttonSize, ButtonContext buttonContext, String name) {
        String key = buttonSize.name() + "_BUTTON_" + name;
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = mediumFont;
        checkBoxStyle.fontColor = fontColor;
        checkBoxStyle.checkboxOn = getTextureRegionDrawable(buttonContext, key, 0);
        checkBoxStyle.checkboxOff = getTextureRegionDrawable(buttonContext, key, 1);
        return checkBoxStyle;
    }

    public TextButton.TextButtonStyle getTextButtonStyle(ButtonSize buttonSize, ButtonContext buttonContext, String name, ButtonSize textSize) {
        String key = buttonSize.name() + "_BUTTON_" + name;
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = ButtonSize.MID.equals(textSize) ? mediumFont : smallFont;
        buttonStyle.fontColor = fontColor;
        buttonStyle.up = getTextureRegionDrawable(buttonContext, key, 0);
        buttonStyle.down = getTextureRegionDrawable(buttonContext, key, 1);
        return buttonStyle;
    }

    public TextureAtlas.AtlasRegion getLevelTexture(ButtonSize buttonSize, String name) {
        String key = buttonSize.name() + "_BUTTON_" + name;
        return levelTextureAtlas.findRegion(key);
    }


    private Drawable getTextureRegionDrawable(ButtonContext buttonContext, String key, int i) {
        try {
            switch (buttonContext) {
                case MENU:
                    return new TextureRegionDrawable(menuTextureAtlas.findRegion(key, i));
                case LEVEL:
                    return new TextureRegionDrawable(levelTextureAtlas.findRegion(key, i));
            }
        } catch (Exception e) {
            Gdx.app.error("ButtonFactory", "Error creating button " + key, e);
        }
        return null;
    }

    public Label.LabelStyle getLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = mediumFont;
        labelStyle.fontColor = fontColor;
        return labelStyle;
    }

    public Label.LabelStyle getTutorialLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = tutorialFont;
        labelStyle.fontColor = fontColor;
        labelStyle.background = new TextureRegionDrawable(levelTextureAtlas.findRegion("TUTO_BACKGROUND"));
        return labelStyle;
    }

    public BitmapFont getGuiFont() {
        return smallFont;
    }

    public Animation getTileAnimation(String effect, String world) {
        return new Animation(0.8f, tilesTextureAtlas.findRegions(effect + "_" + world + "_FX"));
    }
}
