package com.run.game.screens;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.run.game.RunningGame;
import com.run.game.entities.Enemy;
import com.run.game.entities.Laser;
import com.run.game.entities.Boss;
import com.run.game.entities.Obstacle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

public class GameScreen implements Screen {
    private final RunningGame game;

    private ShapeRenderer shapeRenderer;

    private Texture backgroundTexture;
    private float backgroundX1, backgroundX2;
    private float backgroundSpeed = 100;

    private Texture runnerTexture;
    private Rectangle runnerBounds;
    private float runnerSpeedY = 0;
    private float gravity = -500;
    private boolean isJumping = false;
    private float jumpVelocity = 0;
    private float jumpSpeed = 400;
    private float groundLevel = 150;

    private Array<Enemy> enemies;
    private Array<Laser> lasers;
    private Texture enemyTexture;

    private Array<Obstacle> obstacles;
    private Texture obstacleTexture;

    private float playerDistance = 0;
    private boolean isBossFight = false;
    private Boss boss;

    private BitmapFont font;
    private boolean gameOver = false;

    private boolean gameWon = false;

    public GameScreen(RunningGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        backgroundTexture = new Texture("background.png");
        runnerTexture = new Texture("runner.png");
        enemyTexture = new Texture("enemy.png");
        obstacleTexture = new Texture("obstacle.png");

        backgroundX1 = 0;
        backgroundX2 = backgroundTexture.getWidth();

        runnerBounds = new Rectangle(100, groundLevel, runnerTexture.getWidth(), runnerTexture.getHeight());

        enemies = new Array<>();
        lasers = new Array<>();
        obstacles = new Array<>();

        font = new BitmapFont();

        spawnEnemy();
        spawnObstacle();
    }

    @Override
    public void render(float delta) {
       try {
           Gdx.gl.glClearColor(0, 0, 0, 1);
           Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

           if (!gameOver) {
               updateBackground(delta);
               handleInput(delta);
               updateRunner(delta);
               updateEnemies(delta);
               updateLasers(delta);
               updateObstacles(delta);

               if (isBossFight) {
                   updateBoss(delta);
               } else {
                   playerDistance += delta * backgroundSpeed;
                   if (playerDistance >= 1000) {
                       spawnBoss();
                       isBossFight = true;
                   }
               }
           }

           // Draw background and all entities using SpriteBatch
           game.batch.begin();
           drawBackground();
           game.batch.draw(runnerTexture, runnerBounds.x, runnerBounds.y);

           for (Enemy enemy : enemies) {
               enemy.draw(game.batch);
           }

           for (Obstacle obstacle : obstacles) {
               obstacle.draw(game.batch);
           }

           if (isBossFight) {
               boss.draw(game.batch);
           }

           font.draw(game.batch, "Distance: " + (int) playerDistance, 10, 450);

           if (gameOver) {
               if (gameWon) {
                   font.draw(game.batch, "YOU'VE WON!", 350, 250);
               } else {
                   font.draw(game.batch, "GAME OVER", 350, 250);
               }
               font.draw(game.batch, "Press R to Restart", 320, 200);
               if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                   dispose();
                   game.setScreen(new GameScreen(game));
                   game.batch.end();
                   return;
               }
           }

           game.batch.end();
           if (isBossFight) {
                drawBossHealthBar();
           }

           // Draw lasers using ShapeRenderer
           shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
           for (Laser laser : lasers) {
               laser.draw(shapeRenderer); // Correct rendering of lasers as red lines
           }
           shapeRenderer.end();
       } catch (RuntimeException e) {
           System.out.println(e);
       }

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        runnerTexture.dispose();
        enemyTexture.dispose();
        obstacleTexture.dispose();
        if (boss != null) {
            boss.dispose();
        }
        font.dispose();
        shapeRenderer.dispose();
    }

    private void updateBackground(float delta) {
        backgroundX1 -= backgroundSpeed * delta;
        backgroundX2 -= backgroundSpeed * delta;

        if (backgroundX1 + backgroundTexture.getWidth() < 0) {
            backgroundX1 = backgroundX2 + backgroundTexture.getWidth();
        }
        if (backgroundX2 + backgroundTexture.getWidth() < 0) {
            backgroundX2 = backgroundX1 + backgroundTexture.getWidth();
        }
    }

    private void drawBackground() {
        game.batch.draw(backgroundTexture, backgroundX1, 0);
        game.batch.draw(backgroundTexture, backgroundX2, 0);
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            jumpVelocity = jumpSpeed;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            lasers.add(new Laser(
                runnerBounds.x + runnerBounds.width, // x-coordinate
                runnerBounds.y + runnerBounds.height / 2, // y-coordinate
                5, // Width of the laser
                2  // Height of the laser
            ));
        }
    }

    private void updateRunner(float delta) {
        if (isJumping) {
            jumpVelocity += gravity * delta;
            runnerBounds.y += jumpVelocity * delta;

            if (runnerBounds.y <= groundLevel) {
                runnerBounds.y = groundLevel;
                isJumping = false;
                jumpVelocity = 0;
            }
        }
    }

    private void updateEnemies(float delta) {
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta, runnerBounds.x);

            if (enemy.getBounds().overlaps(runnerBounds)) {
                enemy.setFlyingAway(true);
                gameOver = true;
                return;
            }

            if (enemy.getBounds().x + enemy.getBounds().width < 0 || enemy.getBounds().y > Gdx.graphics.getHeight()) {
                enemies.removeIndex(i);
            }
        }
    }

    private void updateLasers(float delta) {
        for (int i = lasers.size - 1; i >= 0; i--) {
            Laser laser = lasers.get(i);
            laser.update(delta);

            for (int j = enemies.size - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (laser.getBounds().overlaps(enemy.getBounds())) {
                    enemies.removeIndex(j);
                    lasers.removeIndex(i);
                    break;
                }
            }

            if (laser.getBounds().x > 800) {
                lasers.removeIndex(i);
            }
        }
    }

    private void updateBoss(float delta) {
        boss.update(delta);

        if (boss.getBounds().x > 400) {
            boss.getBounds().x -= backgroundSpeed * delta;
        } else {
            boss.getBounds().x = 400;
        }

        for (int i = lasers.size - 1; i >= 0; i--) {
            Laser laser = lasers.get(i);
            if (laser.getBounds().overlaps(boss.getBounds())) {
                boss.takeDamage(10);
                lasers.removeIndex(i);
            }
        }

        if (boss.isDead()) {
            gameWon = true;
            gameOver = true;
        }
    }

    private void spawnBoss() {
        boss = new Boss(new Texture("boss.png"), 800, 200, 100);
    }

    private void spawnEnemy() {
        float randomY = (float) Math.random() * 200 + 100;
        enemies.add(new Enemy(enemyTexture, 800, randomY));
    }

    private void spawnObstacle() {
        float randomY = groundLevel;
        obstacles.add(new Obstacle(obstacleTexture, 800, randomY));
    }

    private void updateObstacles(float delta) {
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);

            // Move the obstacle to the left
            obstacle.update(delta);

            // Check for collision with the runner
            if (obstacle.getBounds().overlaps(runnerBounds)) {
                gameOver = true; // End the game if there is a collision
                return;
            }

            // Remove obstacles that go off-screen
            if (obstacle.getBounds().x + obstacle.getBounds().width < 0) {
                obstacles.removeIndex(i);
            }
        }
    }

    private void drawBossHealthBar() {
        if (boss != null) {
            float barWidth = 100; // Total width of the health bar
            float barHeight = 10; // Height of the health bar
            float x = boss.getBounds().x + boss.getBounds().width / 2 - barWidth / 2; // Center above the boss
            float y = boss.getBounds().y + boss.getBounds().height + 10; // Just above the boss sprite

            float healthPercentage = (float) boss.getCurrentHealth() / boss.getMaxHealth();
            float currentBarWidth = barWidth * healthPercentage;


            try {
                shapeRenderer.setAutoShapeType(true);
                shapeRenderer.begin();
                shapeRenderer.setColor(Color.RED); // Background (empty) portion
                shapeRenderer.rect(x, y, barWidth, barHeight);


                shapeRenderer.setColor(Color.GREEN); // Filled (current health) portion
                shapeRenderer.rect(x, y, currentBarWidth, barHeight);
                shapeRenderer.end();
            }

            catch (RuntimeException e) {System.out.println(e.getMessage());}
        }
    }


}














