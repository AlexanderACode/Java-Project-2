package com.run.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    private Texture texture;
    private Rectangle bounds;
    private float speed;
    private boolean isFlyingAway = false;

    public Enemy(Texture texture, float x, float y) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        this.speed = 200; // Default horizontal speed
    }

    public void update(float delta, float runnerX) {
        if (isFlyingAway) {
            bounds.x -= speed * delta; // Continue moving left
            bounds.y += 200 * delta;  // Fly upward
        } else {
            bounds.x -= speed * delta; // Normal movement
        }
    }

    public void setFlyingAway(boolean flyingAway) {
        this.isFlyingAway = flyingAway;
    }

    public boolean isFlyingAway() {
        return isFlyingAway;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }
}


