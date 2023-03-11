package com.aramirezochoa.earm.media;

import com.aramirezochoa.earm.Manager;
import com.aramirezochoa.earm.options.OptionAction;
import com.aramirezochoa.earm.options.OptionsManager;
import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.avatar.types.AvatarType;
import com.aramirezochoa.earm.state.level.manager.WorldType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leboheme on 29/08/2014.
 */
public enum SoundManager implements Manager {
    INSTANCE;

    private Map<AvatarType, Map<AvatarAction, Sound>> sounds = new HashMap<AvatarType, Map<AvatarAction, Sound>>();
    private Map<WorldType, Music> themes = new HashMap<WorldType, Music>();
    private Map<WorldType, SoundStatus> themesStatus = new HashMap<WorldType, SoundStatus>(); // True: sounding

    private Sound buttonSound;

    private boolean loaded = false;

    public void playButton() {
        if (OptionsManager.INSTANCE.isEnabled(OptionAction.SOUND)) {
            buttonSound.play();
        }
    }

    private enum SoundStatus {
        STOP,
        PLAY,
        PAUSE;
    }

    public void init() {
        if (loaded) {
            stopThemes();
        }
        loaded = false;
        sounds = MediaManager.INSTANCE.getSounds();
        themes = MediaManager.INSTANCE.getThemes();
        buttonSound = MediaManager.INSTANCE.getButtonSound();
        loaded = true;
    }

    public void stopSound(AvatarType type, AvatarAction avatarAction) {
        Map<AvatarAction, Sound> avatarSounds = sounds.get(type);
        if (avatarSounds.containsKey(avatarAction)) {
            avatarSounds.get(avatarAction).stop();
        }
    }

    public void playSound(AvatarType type, AvatarAction avatarAction) {
        if (OptionsManager.INSTANCE.isEnabled(OptionAction.SOUND)) {
            Map<AvatarAction, Sound> avatarSounds = sounds.get(type);
            if (avatarSounds.containsKey(avatarAction)) {
                avatarSounds.get(avatarAction).play();
            }
        }
    }

    public void pauseTheme(WorldType world) {
        themes.get(world).pause();
        themesStatus.put(world, SoundStatus.PAUSE);
    }

    public void stopTheme(WorldType world) {
        themes.get(world).stop();
        themesStatus.put(world, SoundStatus.STOP);
    }

    public void stopThemes() {
        for (Music music : themes.values()) {
            music.stop();
        }
        for (WorldType world : themesStatus.keySet()) {
            themesStatus.put(world, SoundStatus.STOP);
        }
    }

    public void playTheme(WorldType world) {
        if (OptionsManager.INSTANCE.isEnabled(OptionAction.MUSIC)) {
            if (!themesStatus.containsKey(world)) {
                themesStatus.put(world, SoundStatus.STOP);
            }
            if (SoundStatus.STOP.equals(themesStatus.get(world))) {
                stopThemes();
                themes.get(world).setLooping(true);
                themes.get(world).play();
            } else if (SoundStatus.PAUSE.equals(themesStatus.get(world))) {
                themes.get(world).play();
            }
            themesStatus.put(world, SoundStatus.PLAY);
        }
    }

}
