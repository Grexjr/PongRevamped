package com.badlogic.pongrevamp.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.pongrevamp.physics.SiPhysOb;
import com.badlogic.pongrevamp.textureutils.TextureUtils;

public class Paddle extends SiPhysOb implements Renderable {
    // CONSTANTS
    private static final float PADDLE_HEIGHT = 4f;
    private static final float PADDLE_WIDTH = 1f;
    private static final float PADDLE_SPEED = 25f;

    private final Texture paddleTexture;
    private final Sprite paddleSprite;
    private final Vector2 paddleNormal;

    public Paddle(Vector2 normal){
        super(
            new Vector2(),
            new Vector2(),
            new Vector2(),
            new Rectangle()
        );
        this.paddleTexture = TextureUtils.createPlaceholderTexture(Color.WHITE);
        this.paddleSprite = new Sprite(paddleTexture);
        paddleSprite.setSize(PADDLE_WIDTH,PADDLE_HEIGHT);
        this.paddleNormal = normal;
    }

    @Override
    public Sprite getSprite(){return this.paddleSprite;}

    @Override
    public Texture getTexture(){return this.paddleTexture;}

    @Override
    public void updateRenderablePosition(Vector2 position){
        paddleSprite.setPosition(position.x,position.y);
    }

    @Override
    public void setRectangle(){
        this.getPhysRectangle().set(
            paddleSprite.getX(),
            paddleSprite.getY(),
            PADDLE_WIDTH,
            PADDLE_HEIGHT
        );
    }

    // Phys obj methods
    @Override
    public void enforceTopSpeed(float delta){
        float yVelocity = this.getVelocity().y;
        if(yVelocity > (PADDLE_SPEED * delta)){
            this.setVelocity(new Vector2(0,(PADDLE_SPEED * delta)));
        }
        if(yVelocity < -(PADDLE_SPEED * delta)){
            this.setVelocity(new Vector2(0,-(PADDLE_SPEED * delta)));
        }
    }

    public float getPaddleHeight(){return PADDLE_HEIGHT;}

    public float getPaddleWidth(){return PADDLE_WIDTH;}

    public float getPaddleSpeed(){return PADDLE_SPEED;}

    public Vector2 getPaddleNormal(){return paddleNormal;}

    public void movePlayerPaddle(float delta){
        if(this.getVelocity().y < PADDLE_SPEED){
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                this.setAcceleration(new Vector2(0, delta));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                this.setAcceleration(new Vector2(0,-delta));
            }
        }
        //System.out.println(playerPaddle.getVelocity()); //DEBUG
    }

    public void checkPaddleVertical(float worldHeight){ // Can be in paddle class
        if(this.getPosition().y > worldHeight - this.getPaddleHeight()){ // minus 4 because of sprite height
            this.setPosition(new Vector2(this.getPosition().x,worldHeight - this.getPaddleHeight()));
            this.setVelocity(new Vector2(0,0));
        }
        if(this.getPosition().y <= 0){
            this.setPosition(new Vector2(this.getPosition().x,0));
            this.setVelocity(new Vector2(0,0));
        }
    }

    public void calcOpponentPaddleMove(Vector2 ballPos, float delta){
        float opponentSpeedAdjust = 10f;
        float paddleCenterAdjust = 0.5f;
        // Makes it so the center of the paddle tracks the ball
        float offset = (this.getPaddleHeight()/2) - paddleCenterAdjust;
        float newY = (ballPos.y - (this.getPosition().y + offset));
        this.changeAcceleration(new Vector2(0,opponentSpeedAdjust * newY * delta));
    }

}
