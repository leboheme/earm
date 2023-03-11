package com.aramirezochoa.earm.state.level.avatar;

import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by leboheme on 23/10/2014.
 */
public class EnemyAvatar extends Avatar {

    public EnemyAvatar() {
        addListener(new InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.error("Soldier touched!", "");
                return true;
            }
        });
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        this.setBounds(getPosition().x, getPosition().y, getDimension().x, getDimension().y);
    }

    @Override
    public void notifyWall() {
        lookingAt = lookingAt.flip();
    }

    @Override
    public void notifyAbyss() {
        getVelocity().x = 0;
        getVelocity().y = 0;
        lookingAt = lookingAt.flip();
    }

    @Override
    public float getWalkVelocity() {
        // We could do it more complex but it is too simple for that ;)
        switch (type) {
            case SOLDIER:
                return 150;
            default:
                return 0;
        }
    }

    @Override
    public void collisionWith(Avatar avatar) {
        avatar.setStatus(AvatarAction.DEAD);
    }

}
