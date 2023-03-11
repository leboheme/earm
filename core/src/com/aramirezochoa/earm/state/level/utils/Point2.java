package com.aramirezochoa.earm.state.level.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by boheme on 16/02/14.
 */
public class Point2 {
    public float x;
    public float y;

    public Point2() {
        this.x = 0;
        this.y = 0;
    }

    public Point2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2 add(Vector2 vector2) {
        this.x += vector2.x;
        this.y += vector2.y;
        return this;
    }

    public void extract(Point2 p2) {
        this.x -= p2.x;
        this.y -= p2.y;
    }

    public boolean overlaps(Rectangle tile) {
        return tile.contains(this.x, this.y);
    }
}
