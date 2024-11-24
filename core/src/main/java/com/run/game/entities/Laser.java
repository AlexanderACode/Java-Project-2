package com.run.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Laser {
    private Rectangle bounds;

    public Laser(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }


    public void update(float delta) {
        bounds.x += 500 * delta; // Move the laser to the right
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}



