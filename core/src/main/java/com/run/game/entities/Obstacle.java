package com.run.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstacle {
    private Texture texture;        // Texture for the obstacle
    private Rectangle bounds;       // Rectangle for collision detection
    private float speed = 150;      // Speed at which the obstacle moves towards the player

    // Constructor to initialize the obstacle's position and texture
    public Obstacle(Texture texture, float x, float y) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    // Update method to move the obstacle towards the player
    public void update(float delta) {
        bounds.x -= speed * delta; // Move left towards the player

        // If the obstacle goes off the left side of the screen, it is removed
        if (bounds.x < 0) {
            // You can add logic here to remove obstacles in the game screen if necessary
        }
    }

    // Draw method to render the obstacle on the screen
    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    // Getter for the obstacle's bounds (for collision detection)
    public Rectangle getBounds() {
        return bounds;
    }
}

