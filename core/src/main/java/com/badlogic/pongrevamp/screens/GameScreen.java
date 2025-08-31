package com.badlogic.pongrevamp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.pongrevamp.PongRevamp;
import com.badlogic.pongrevamp.gameobjects.Ball;
import com.badlogic.pongrevamp.gameobjects.Paddle;
import com.badlogic.pongrevamp.textureutils.TextureUtils;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    // CONSTANTS
    private static final int BOUNCE_TIMER_MAX = 50;

    final PongRevamp game;
    final float worldWidth;
    final float worldHeight;

    Texture backgroundTexture;

    Ball gameBall;
    Paddle playerPaddle;
    Paddle opponentPaddle;

    int playerScore, enemyScore, bounceTimer;
    boolean isGameOver;

    public GameScreen(PongRevamp pongRevamp){
        this.game = pongRevamp;
        this.worldWidth = game.viewport.getWorldWidth();
        this.worldHeight = game.viewport.getWorldHeight();

        this.gameBall = new Ball();

        this.backgroundTexture = TextureUtils.createPlaceholderTexture(Color.BLACK);

        this.playerPaddle = new Paddle();
        this.opponentPaddle = new Paddle();
    }



    @Override
    public void show() {
        float delta = Gdx.graphics.getDeltaTime();
        // Prepare your screen here.
        playerPaddle.setPosition(new Vector2(0,(worldHeight/2) - 2));

        // Sets ball position to center
        gameBall.setPosition(new Vector2(worldWidth/2,worldHeight/2));
        // Sets ball velocity to start the game
        gameBall.setVelocity(new Vector2(gameBall.getBallSpeed() * delta,0));

        opponentPaddle.setPosition(new Vector2(worldWidth-1,(worldHeight/2) -2));
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic();
        draw();
    }

    private void input(){
        float delta = Gdx.graphics.getDeltaTime();
        if(!isGameOver){
            this.playerPaddle.movePlayerPaddle(delta);
        }
    }

    private void logic(){
        setRectangles();
        float delta = Gdx.graphics.getDeltaTime();
        gameBall.move(delta);
        playerPaddle.move(delta);
        opponentPaddle.calcOpponentPaddleMove(gameBall.getPosition(),delta);
        opponentPaddle.move(delta);
        //System.out.println(playerPaddle.getVelocity()); //DEBUG

        playerPaddle.experienceDrag();
        opponentPaddle.experienceDrag();

        checkPaddleVertical(playerPaddle);
        checkPaddleVertical(opponentPaddle);
        checkBallVertical();
        checkBallHorizontal();
        bounceTimer++;

        if(gameBall.getPhysRectangle().overlaps(playerPaddle.getPhysRectangle()) && bounceTimer > BOUNCE_TIMER_MAX){
            gameBall.setCollided(false);
            gameBall.bounce();
            bounceTimer = 0;
        }
        if(gameBall.getPhysRectangle().overlaps(opponentPaddle.getPhysRectangle()) && bounceTimer > BOUNCE_TIMER_MAX){
            gameBall.setCollided(false);
            gameBall.bounce();
            bounceTimer = 0;
        }

        playerPaddle.enforceTopSpeed(delta);
        opponentPaddle.enforceTopSpeed(delta);
        gameBall.enforceTopSpeed(delta);
    }

    private void checkPaddleVertical(Paddle paddle){ // Can be in paddle class
        if(paddle.getPosition().y > worldHeight - paddle.getPaddleHeight()){ // minus 4 because of sprite height
            paddle.setPosition(new Vector2(paddle.getPosition().x,worldHeight - paddle.getPaddleHeight()));
            paddle.setVelocity(new Vector2(0,0));
        }
        if(paddle.getPosition().y <= 0){
            paddle.setPosition(new Vector2(paddle.getPosition().x,0));
            paddle.setVelocity(new Vector2(0,0));
        }
    }

    private void checkBallVertical(){ // Can be in ball class
        if(gameBall.getPosition().y >= worldHeight - gameBall.getBallHeight()){
            gameBall.setPosition(new Vector2(gameBall.getPosition().x,worldHeight - gameBall.getBallHeight()));
            gameBall.bounce();
        }
        if(gameBall.getPosition().y <= 0){
            gameBall.setPosition(new Vector2(gameBall.getPosition().x,0));
            gameBall.bounce();
        }
    }

    private void checkBallHorizontal(){
        if(gameBall.getPosition().x >= worldWidth - gameBall.getBallWidth() || gameBall.getPosition().x <= 0){
            gameBall.setPosition(new Vector2(worldWidth/2,worldHeight/2));
        }
    }

    private void setRectangles(){ // Can do this in the class itself
        playerPaddle.getPhysRectangle().set(
            playerPaddle.getSprite().getX(),
            playerPaddle.getSprite().getY(),
            playerPaddle.getSprite().getWidth(),
            playerPaddle.getSprite().getHeight());
        opponentPaddle.getPhysRectangle().set(
            opponentPaddle.getSprite().getX(),
            opponentPaddle.getSprite().getY(),
            opponentPaddle.getSprite().getWidth(),
            opponentPaddle.getSprite().getHeight());
        gameBall.getPhysRectangle().set(
            gameBall.getSprite().getX(),
            gameBall.getSprite().getY(),
            gameBall.getSprite().getWidth(),
            gameBall.getSprite().getHeight());
    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.batch.draw(backgroundTexture,0,0,worldWidth,worldHeight);

        playerPaddle.updateRenderablePosition(playerPaddle.getPosition());
        playerPaddle.getSprite().draw(game.batch);

        opponentPaddle.updateRenderablePosition(opponentPaddle.getPosition());
        opponentPaddle.getSprite().draw(game.batch);

        gameBall.updateRenderablePosition(gameBall.getPosition());
        gameBall.getSprite().draw(game.batch);

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
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        backgroundTexture.dispose();
        playerPaddle.getTexture().dispose();
        opponentPaddle.getTexture().dispose();
        gameBall.getTexture().dispose();
    }
}
