package com.badlogic.pongrevamp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.pongrevamp.PongRevamp;
import com.badlogic.pongrevamp.textureutils.TextureUtils;

public class TitleScreen implements Screen {
    // CONSTANTS
    private static final String TITLE_STRING = "PONG REVAMPED";
    private static final String INSTRUCTIONS_STRING = "PRESS 'SPACE' TO BEGIN";

    final PongRevamp game;
    final float worldWidth;
    final float worldHeight;

    Texture backgroundTexture;

    public TitleScreen(PongRevamp pongRevamp){
        this.game = pongRevamp;
        this.worldWidth = game.viewport.getWorldWidth();
        this.worldHeight = game.viewport.getWorldHeight();

        this.backgroundTexture = TextureUtils.createPlaceholderTexture(Color.GRAY);
    }

    @Override
    public void show() {
        // Prepare your screen here.

    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        draw();
    }

    private void input() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            game.setScreen(new GameScreen(game));
        }
    }

    private void draw(){

        float titleVertical = worldHeight - worldHeight/3;
        float titleHorizontal = worldWidth/2.75f;

        // NEED TO DO THESE THINGS!!!
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.batch.draw(backgroundTexture,0,0,worldWidth,worldHeight);
        game.font.draw(game.batch,TITLE_STRING,titleHorizontal,titleVertical);
        game.font.draw(game.batch,INSTRUCTIONS_STRING,titleHorizontal,titleVertical-1);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width,height,true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
        this.dispose();
        System.out.println("TITLE SCREEN HIDDEN");
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        backgroundTexture.dispose();

    }





}
