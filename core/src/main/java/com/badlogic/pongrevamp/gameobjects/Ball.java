package com.badlogic.pongrevamp.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.pongrevamp.physics.SiPhysOb;
import com.badlogic.pongrevamp.textureutils.TextureUtils;

import java.util.Random;

public class Ball extends SiPhysOb implements Renderable {
    // CONSTANTS
    private static final float BALL_HEIGHT = 1f;
    private static final float BALL_WIDTH = 1f;
    private static final float BALL_SPEED_MAX = 25f;

    private final Texture ballTexture;
    private final Sprite ballSprite;

    private boolean hasCollided;

    public Ball(){
        super(
            new Vector2(),
            new Vector2(),
            new Vector2(),
            new Rectangle()
        );
        this.ballTexture = TextureUtils.createPlaceholderTexture(Color.WHITE);
        this.ballSprite = new Sprite(ballTexture);
        this.ballSprite.setSize(BALL_WIDTH,BALL_HEIGHT);
        this.hasCollided = true;
    }

    @Override
    public Sprite getSprite(){return this.ballSprite;}

    @Override
    public Texture getTexture(){return this.ballTexture;}

    @Override
    public void updateRenderablePosition(Vector2 position){
        ballSprite.setPosition(position.x,position.y);
    }

    @Override
    public void setRectangle(){
        this.getPhysRectangle().set(
            ballSprite.getX(),
            ballSprite.getY(),
            BALL_WIDTH,
            BALL_HEIGHT
        );
    }

    // Phys obj methods
    @Override
    public void enforceTopSpeed(float delta){
        //System.out.println("TOP SPEED ENFORCED!"); //DEBUG
        float yVelocity = this.getVelocity().y;
        float xVelocity = this.getVelocity().x;
        if(yVelocity > (BALL_SPEED_MAX)){
            this.changeAcceleration(this.getVelocity().scl(-1));
        }
        if(yVelocity < -(BALL_SPEED_MAX)){
            this.changeAcceleration(this.getVelocity().scl(-1));
        }
        if(xVelocity != (BALL_SPEED_MAX)){
            this.changeAcceleration(this.getVelocity().scl(-1));
        }
        if(xVelocity != -(BALL_SPEED_MAX)){
            this.changeAcceleration(this.getVelocity().scl(-1));
        }
    }

    public float getBallHeight(){return BALL_HEIGHT;}

    public float getBallWidth(){return BALL_WIDTH;}

    public float getBallSpeed(){return BALL_SPEED_MAX;}

    public boolean getCollided(){return hasCollided;}
    public void setCollided(boolean collision){hasCollided = collision;}

    // can add vectors here as constants, or make them global...?
    public void checkBallVertical(Vector2 CEILING_NORMAL,Vector2 FLOOR_NORMAL,float worldHeight){
        if(this.getPosition().y >= worldHeight - BALL_HEIGHT){
            System.out.println("Ball forced down!"); //DEBUG
            hasCollided = false;
            bounce(new Vector2(),CEILING_NORMAL);
        }
        if(this.getPosition().y <= 0){
            System.out.println("Ball forced up!"); //DEBUG
            hasCollided = false;
            this.bounce(new Vector2(),FLOOR_NORMAL);
        }
    }

    public void checkBallHorizontal(Vector2 worldCenter,float worldWidth,float delta){
        if(this.getPosition().x >= worldWidth - BALL_WIDTH || this.getPosition().x <= 0){
            resetBall(worldCenter,delta);
        }
    }

    // Bounce function
    public void bounce(Vector2 bouncerVelocity, Vector2 bouncerNormal){
        Vector2 n = bouncerNormal.nor();
        Vector2 v = this.getVelocity();
        if(!hasCollided) {
            // Formula for reflection from the internet
            // newVelocityVec = velocityVec - 2 * normalVec * dot(normalVec,velocityVec)
            float dotProduct = n.dot(v);
            Vector2 scaledNormal = n.scl(dotProduct);
            Vector2 twiceNormal = scaledNormal.scl(2);
            Vector2 subtractedBounce = v.sub(twiceNormal);

            this.setVelocity(subtractedBounce.add(bouncerVelocity));
        }
        hasCollided = true;
    }

    public void resetBall(Vector2 worldCenter,float delta){ // Could probably go into ball
        this.setPosition(worldCenter);
        this.setVelocity(new Vector2());
        boolean randDirection = new Random().nextBoolean();
        float randYVelocity = new Random().nextFloat(-this.getBallSpeed(),this.getBallSpeed());
        //System.out.println("Random y: " + randYVelocity); //DEBUG
        if(randDirection){
            this.setVelocity(new Vector2(this.getBallSpeed() * delta,randYVelocity * delta));
        } else {
            this.setVelocity(new Vector2(-(this.getBallSpeed() * delta),randYVelocity * delta));
        }
    }

    public void onPaddleCollision(Paddle paddle){
        //Hit paddle functionality; can move to ball class too; onPaddleCollision()
        if(this.getPhysRectangle().overlaps(paddle.getPhysRectangle())){
            hasCollided = false;
            bounce(paddle.getVelocity(),paddle.getPaddleNormal());
            //System.out.println("Hit Paddle!"); // DEBUG
        }
    }

}
