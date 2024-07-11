package com.KvasicOkicIgra.PticaSkakalica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class GameScreen implements Screen {
    private FlappyBird game;
    private SpriteBatch batch;
    private Texture background;
    private Texture gameover;

    private Texture[] birds;
    private int flapState = 0;
    private float birdY = 0;
    private float velocity = 0;
    private Circle birdCircle;
    private int score = 0;
    private int scoringTube = 0;
    private BitmapFont font;
    private BitmapFont scoreFont;

    private int gameState = 0;
    private float gravity = 2;

    private Texture topTube;
    private Texture bottomTube;
    private float gap = 400;
    private Random randomGenerator;
    private float tubeVelocity = 4;
    private int numberOfTubes = 4;

    private float[] tubeOffset = new float[numberOfTubes];
    private float distanceBetweenTubes;
    private float[] tubeX;
    private Rectangle[] topTubeRectangles;
    private Rectangle[] bottomTubeRectangles;

    public GameScreen(FlappyBird game) {
        this.game = game;
        create();
    }

    private void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        gameover = new Texture("gameover.png");
        birdCircle = new Circle();
        font = new BitmapFont();
        font.getData().setScale(10);
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(5);

        birds = new Texture[2];
        birds[0] = new Texture("flappybirdup.png");
        birds[1] = new Texture("flappybirddown.png");

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        tubeX = new float[numberOfTubes];
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        startGame();
    }

    private void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
        }
    }

    @Override
    public void show() {
        if (background == null || gameover == null) {
            create();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;
                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -30;
            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;

                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                    topTubeRectangles[i].setPosition(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                    bottomTubeRectangles[i].setPosition(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                } else {
                    tubeX[i] -= tubeVelocity;
                    topTubeRectangles[i].setX(tubeX[i]);
                    bottomTubeRectangles[i].setX(tubeX[i]);
                }

                batch.draw(topTube, tubeX[i], topTubeRectangles[i].getY());
                batch.draw(bottomTube, tubeX[i], bottomTubeRectangles[i].getY());
            }

            if (birdY > 0 && birdY < Gdx.graphics.getHeight() - birds[flapState].getHeight()) {
                velocity += gravity;
                birdY -= velocity;
            } else {
                gameState = 2;
            }
        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

            if (Gdx.input.justTouched()) {
                gameState = 0;
                game.setScreen(new MainMenuScreen(game));
                dispose();
                score = 0;
                scoringTube = 0;
                velocity = 0;
                startGame();
            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

        for (int i = 0; i < numberOfTubes; i++) {
            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                gameState = 2;
            }
        }

        batch.end();
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
        if (gameover != null) gameover.dispose();
        if (birds != null) {
            for (Texture bird : birds) {
                bird.dispose();
            }
        }
        if (topTube != null) topTube.dispose();
        if (bottomTube != null) bottomTube.dispose();
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (scoreFont != null) scoreFont.dispose();
    }
}
