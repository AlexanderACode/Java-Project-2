package com.run.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Boss {
    private Texture texture;
    private Rectangle bounds;
    private int maxHealth; // Maximum health for the boss
    private int currentHealth; // Current health for the boss
    private float speed = 100;  // Boss speed
    private float damage = 20;  // Boss damage

    public Boss(Texture texture, float x, float y, int health) {
        this.texture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        this.maxHealth = health;
        this.currentHealth = health;
    }

    public void update(float delta) {
        // Move the boss toward the player (slowly)
        bounds.x -= speed * delta;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void takeDamage(int damage) {
        this.currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0; // Ensure health doesn't drop below zero
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Dispose of the texture when done
        }
    }
}


