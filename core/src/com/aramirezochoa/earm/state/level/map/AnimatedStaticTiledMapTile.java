package com.aramirezochoa.earm.state.level.map;

import com.aramirezochoa.earm.state.level.utils.TileType;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

/**
 * Created by boheme on 25/10/14.
 */
public class AnimatedStaticTiledMapTile extends StaticTiledMapTile {

    private final TextureRegion originalTextureRegion;
    private final Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float frameTime = 0;
    private int regionX, regionY, regionWidth, regionHeight;

    public AnimatedStaticTiledMapTile(TextureRegion originalTextureRegion, Animation animation) {
        super(originalTextureRegion);
        this.originalTextureRegion = originalTextureRegion;
        this.regionX = originalTextureRegion.getRegionX();
        this.regionY = originalTextureRegion.getRegionY();
        this.regionWidth = originalTextureRegion.getRegionWidth();
        this.regionHeight = originalTextureRegion.getRegionHeight();
        this.animation = animation;
        this.getProperties().put("type", TileType.DIG.getCode());
    }

    @Override
    public TextureRegion getTextureRegion() {
        if (animation.isAnimationFinished(frameTime)) {
            currentFrame.setRegion(0, 0, 0, 0);
        } else {
            currentFrame = new TextureRegion(animation.getKeyFrame(frameTime, false), regionX, regionY, regionWidth, regionHeight);
            frameTime += 0.2f;
        }
        return currentFrame;
    }
}
