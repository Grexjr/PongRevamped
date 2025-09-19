package com.badlogic.pongrevamp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private static final int BOUNCE_TIMER_MAX = 10;
    private static final Vector2 CEILING_NORMAL = new Vector2(0,-1); // can condense by just multiplying by -1
    private static final Vector2 FLOOR_NORMAL = new Vector2(0,1);
    private static final Vector2 PLAYER_NORMAL = new Vector2(1,0);
    private static final Vector2 OPPONENT_NORMAL = new Vector2(-1,0);

    final PongRevamp game;
    final float worldWidth;
    final float worldHeight;
    final Vector2 worldCenter;

    Texture backgroundTexture;

    Ball gameBall;
    Paddle playerPaddle;
    Paddle opponentPaddle;

    int playerScore, opponentScore, bounceTimer, startTimer;
    boolean isGameOver;

    public GameScreen(PongRevamp pongRevamp){
        this.game = pongRevamp;
        this.worldWidth = game.viewport.getWorldWidth();
        this.worldHeight = game.viewport.getWorldHeight();

        this.worldCenter = new Vector2(worldWidth/2,worldHeight/2);

        this.gameBall = new Ball();

        this.backgroundTexture = TextureUtils.createPlaceholderTexture(Color.BLACK);

        this.playerPaddle = new Paddle(PLAYER_NORMAL);
        this.opponentPaddle = new Paddle(OPPONENT_NORMAL);
    }

    @Override
    public void show() {
        float delta = Gdx.graphics.getDeltaTime();
        // Prepare your screen here.
        playerPaddle.setPosition(new Vector2(0,(worldHeight/2) - 2));

        // Sets ball position and velocity to start the game
        gameBall.resetBall(worldCenter,delta);

        opponentPaddle.setPosition(new Vector2(worldWidth-1,(worldHeight/2) -2));

        startTimer = 0;
        playerScore = 0;
        opponentScore = 0;
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
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            game.setScreen(new TitleScreen(game));
        }
    }

    private void logic(){
        float delta = Gdx.graphics.getDeltaTime();

        startTimer++;
        setRectangles();

        gameBall.move(delta);
        playerPaddle.move(delta);
        opponentPaddle.calcOpponentPaddleMove(gameBall.getPosition(),delta);
        opponentPaddle.move(delta);
        playerPaddle.experienceDrag();
        opponentPaddle.experienceDrag();

        playerPaddle.checkPaddleVertical(worldHeight);
        opponentPaddle.checkPaddleVertical(worldHeight);
        gameBall.checkBallVertical(CEILING_NORMAL,FLOOR_NORMAL,worldHeight);
        score(delta);

        bounceTimer++;

        //System.out.println("Ball velocity: " + gameBall.getVelocity()); // DEBUG

        //Hit paddle functionality; can move to ball class too; onPaddleCollision()
        // When i move this, it breaks. i don't know why. Suddenly the ball will sometimes disappear when hitting a
        // paddle.
        // I think it's the order of the conditions. I need to run this when rect overlap, then if bouncetimer is >.
        if(gameBall.getPhysRectangle().overlaps(playerPaddle.getPhysRectangle()) && bounceTimer > BOUNCE_TIMER_MAX){
            gameBall.setCollided(false);
            gameBall.bounce(playerPaddle.getVelocity(),playerPaddle.getPaddleNormal());
            bounceTimer = 0;
            //System.out.println("Hit Player!"); // DEBUG
        }
        if(gameBall.getPhysRectangle().overlaps(opponentPaddle.getPhysRectangle()) && bounceTimer > BOUNCE_TIMER_MAX){
            gameBall.setCollided(false);
            gameBall.bounce(opponentPaddle.getVelocity(),opponentPaddle.getPaddleNormal());
            bounceTimer = 0;
            //System.out.println("Hit Opponent!"); // DEBUG
        }

        playerPaddle.enforceTopSpeed(delta);
        opponentPaddle.enforceTopSpeed(delta);
        gameBall.enforceTopSpeed(delta);
    }

    private void score(float delta){
        if(gameBall.getPosition().x > worldWidth -1){
            playerScore += 1;
        }
        if(gameBall.getPosition().x < 0){
            opponentScore += 1;
        }
        gameBall.checkBallHorizontal(worldCenter,worldWidth,delta);
    }

    private void setRectangles(){ // Can do this in the class itself of renderable
        playerPaddle.setRectangle();
        opponentPaddle.setRectangle();
        gameBall.setRectangle();
    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.batch.draw(backgroundTexture,0,0,worldWidth,worldHeight);

        game.font.draw(game.batch,Integer.toString(playerScore),1,worldHeight);
        game.font.draw(game.batch,Integer.toString(opponentScore),worldWidth-1-1,worldHeight);

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
        this.dispose();
        System.out.println("GAME SCREEN HIDDEN AND DISPOSED");
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
