package com.run.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetManagerUtil {
    private static AssetManager assetManager;

    // Initialize the AssetManager
    public static void loadAssets() {
        assetManager = new AssetManager();

        // Queue textures for loading
        assetManager.load("background.png", Texture.class);
        assetManager.load("runner.png", Texture.class);

        // Block until assets are loaded (for simplicity)
        assetManager.finishLoading();
    }

    // Retrieve loaded assets
    public static <T> T getAsset(String assetName, Class<T> type) {
        return assetManager.get(assetName, type);
    }

    // Dispose of assets when the game is closed
    public static void dispose() {
        assetManager.dispose();
    }
}
