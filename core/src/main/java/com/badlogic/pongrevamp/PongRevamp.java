package com.badlogic.pongrevamp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.pongrevamp.screens.TitleScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class PongRevamp extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    @Override
    public void create() {

        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.viewport = new FitViewport(20,20);

        this.font.setUseIntegerPositions(false);
        this.font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());


        setScreen(new TitleScreen(this));
    }

    @Override
    public void render(){super.render();}

    @Override
    public void dispose(){
        batch.dispose();
        font.dispose();
        screen.dispose();
    }
}
