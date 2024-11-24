package com.run.game;

import com.badlogic.gdx.Game;
import com.run.game.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RunningGame extends Game {
    public SpriteBatch batch;  // Declare the batch here

    @Override
    public void create() {
        batch = new SpriteBatch();  // Initialize it in the create method
        this.setScreen(new GameScreen(this));  // Pass the game instance to the screen
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();  // Clean up when done
    }
}




