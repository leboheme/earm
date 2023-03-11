package com.aramirezochoa.earm.state.level.avatar;

import com.aramirezochoa.earm.media.MediaManager;
import com.aramirezochoa.earm.media.SoundManager;
import com.aramirezochoa.earm.state.level.Level;
import com.aramirezochoa.earm.state.level.avatar.action.AvatarAction;
import com.aramirezochoa.earm.state.level.avatar.types.AvatarType;
import com.aramirezochoa.earm.state.level.avatar.types.DirectionType;
import com.aramirezochoa.earm.state.level.utils.Constant;
import com.aramirezochoa.earm.state.level.utils.Point2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

import java.util.Map;

/**
 * Created by leboheme on 14/08/2014.
 */
public abstract class Avatar extends Actor implements Pool.Poolable {

    protected AvatarType type;
    public AvatarAction currentAction;
    protected Point2 dimension;

    private Point2 position;
    public float originalY;
    protected Level level;
    private Vector2 velocity;
    private Vector2 gravity;
    private DirectionType moveDirection;

    // Media
    protected Map<AvatarAction, Animation> animations;
    private TextureRegion currentFrame;
    public float frameTime;
    protected DirectionType lookingAt;
    public float actionTimer;
    public boolean grounded = false;

    public void init(AvatarType type, Point2 position, Level level, DirectionType lookingAt) {
        this.type = type;
        this.dimension = type.getDimension();
        setVisible(true);
        setAnimations();
        this.currentAction = type.getDefaultAction().set(this, level);
        this.moveDirection = DirectionType.RIGHT;
        this.lookingAt = lookingAt;
        this.grounded = false;
        this.velocity = new Vector2(0f, 0f);
        this.gravity = new Vector2(0f, 0f);
        this.position = position;
        this.originalY = position.y;
        this.level = level;
        this.frameTime = 0;
        updateCurrentFrame(0);
    }

    protected void setAnimations() {
        this.animations = MediaManager.INSTANCE.getAnimations(type);
    }

    @Override
    public void reset() {
        // ??? I think is needed nothing
    }

    @Override
    public void act(float deltaTime) {
        currentAction.update(deltaTime, this, level);
        updateCurrentFrame(deltaTime);
    }

    public void walk(DirectionType directionType) {
        switch (currentAction) {
            case IDLE:
            case WALK:
            case JUMP:
            case FALL:
                this.moveDirection = directionType;
        }
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return dimension.x;
    }

    public float getHeight() {
        return dimension.y;
    }

    public void notifyWall() {
        // Do nothing
    }

    public void notifyAbyss() {
        // Do nothing
    }

    public void setStatus(AvatarAction action) {
        // For Earm
    }

    public void updateCurrentFrame(float deltaTime) {
        if (DirectionType.RIGHT.equals(lookingAt) && velocity.x < 0
                || DirectionType.LEFT.equals(lookingAt) && velocity.x > 0) {
            lookingAt = lookingAt.flip();
        }
        Animation<TextureRegion> animation = animations.get(currentAction);
        switch (currentAction) {
            case JUMP:
            case FALL:
            case DEAD:
            case DEAD_ALTITUDE:
                currentFrame = animation.getKeyFrame(frameTime, false);
                break;
            default:
                currentFrame = animation.getKeyFrame(frameTime, true);
                break;
        }
        frameTime += deltaTime;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        boolean flip = DirectionType.LEFT.equals(lookingAt);
        currentFrame.flip(flip, false);
        batch.draw(currentFrame, position.x - (currentFrame.getRegionWidth() - dimension.x) / 2, position.y);
        currentFrame.flip(flip, false);
    }

    protected void stopSound(AvatarAction avatarAction) {
        SoundManager.INSTANCE.stopSound(type, avatarAction);
    }

    protected void playSound(AvatarAction avatarAction) {
        SoundManager.INSTANCE.playSound(type, avatarAction);
    }

    public Point2 getPosition() {
        return position;
    }

    public Point2 getDimension() {
        return dimension;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getAcceleration() {
        return gravity;
    }

    public void dumpVelocity() {
        velocity.x *= Constant.DAMPING;
    }

    public DirectionType getMoveDirection() {
        return moveDirection;
    }

    public DirectionType getLookingAt() {
        return lookingAt;
    }

    public float getWalkVelocity() {
        return 0;
    }

    public float getJumpVelocity() {
        return 0;
    }

    public float getFlyVelocity() {
        return 0;
    }

    public float getDigVelocity() {
        return 0;
    }

    public AvatarAction getCurrentAction() {
        return currentAction;
    }

    public void jump() {
    }

    public void fly() {
    }

    public void stop() {
    }

    public void turn() {
    }

    public void dig() {
    }

    public void parachute() {
    }

    public void climb() {
    }

    public abstract void collisionWith(Avatar avatar);
}
