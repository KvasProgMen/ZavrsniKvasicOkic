package com.KvasicOkicIgra.PticaSkakalica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainMenuScreen implements Screen {
    private final FlappyBird game;
    private SpriteBatch batch;
    private Texture background;
    private Texture tapToStart;
    private ShapeRenderer shapeRenderer;
    private float alpha;
    private boolean transitioning;

    private static final float TAP_TO_START_WIDTH = 1500;
    private static final float TAP_TO_START_HEIGHT = 1000;

    public MainMenuScreen(FlappyBird game) {
        this.game = game;
        create();
    }

    private void create() {
        batch = new SpriteBatch();
        background = new Texture("mainmenu.jpeg");
        tapToStart = new Texture("tap_to_start.png");
        shapeRenderer = new ShapeRenderer();
        alpha = 1.0f;
        transitioning = false;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (transitioning) {
            alpha -= delta;
            if (alpha <= 0) {
                game.setScreen(new GameScreen(game));
                dispose();
                return;
            }
        }

        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        float tapToStartX = (Gdx.graphics.getWidth() - TAP_TO_START_WIDTH) / 2;
        float tapToStartY = (Gdx.graphics.getHeight() - TAP_TO_START_HEIGHT) / 2;
        batch.draw(tapToStart, tapToStartX, tapToStartY, TAP_TO_START_WIDTH, TAP_TO_START_HEIGHT);

        batch.end();

        if (Gdx.input.justTouched() && !transitioning) {
            transitioning = true;
        }

        if (transitioning) {
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 1 - alpha); // Apply inverse alpha to cover screen with black
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (tapToStart != null) tapToStart.dispose();
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
