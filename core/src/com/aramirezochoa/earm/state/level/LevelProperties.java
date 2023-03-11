package com.aramirezochoa.earm.state.level;

import com.aramirezochoa.earm.state.level.utils.Point2;
import com.aramirezochoa.earm.state.level.utils.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by boheme on 18/02/14.
 */
public class LevelProperties {

    private Status status;

    private String groundPath;
    private int width;
    private int height;
    private String walkableMapPath;

    Map<String, Point2> avatars = new HashMap<String, Point2>();

    private float gravity;

    public String getGroundPath() {
        return groundPath;
    }

    public void setGroundPath(String groundPath) {
        this.groundPath = groundPath;
    }

    public String getWalkableMapPath() {
        return walkableMapPath;
    }

    public void setWalkableMapPath(String walkableMapPath) {
        this.walkableMapPath = walkableMapPath;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map<String, Point2> getAvatars() {
        return avatars;
    }

}
