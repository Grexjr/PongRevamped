package com.badlogic.pongrevamp.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.pongrevamp.physics.SiPhysOb;
import com.badlogic.pongrevamp.textureutils.TextureUtils;

public class Ball extends SiPhysOb implements Renderable {
    // CONSTANTS
    private static final float BALL_HEIGHT = 1f;
    private static final float BALL_WIDTH = 1f;
    private static final float BALL_SPEED = 25f;

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

    // Phys obj methods
    @Override
    public void enforceTopSpeed(float delta){
        float yVelocity = this.getVelocity().y;
        if(yVelocity > (BALL_SPEED * delta)){
            this.setVelocity(new Vector2(0,(BALL_SPEED * delta)));
        }
        if(yVelocity < -(BALL_SPEED * delta)){
            this.setVelocity(new Vector2(0,-(BALL_SPEED * delta)));
        }
    }

    public float getBallHeight(){return BALL_HEIGHT;}

    public float getBallWidth(){return BALL_WIDTH;}

    public float getBallSpeed(){return BALL_SPEED;}

    public boolean getCollided(){return hasCollided;}
    public void setCollided(boolean collision){hasCollided = collision;}

    // Bounce function
    public void bounce(){
        if(!hasCollided) {
            this.setVelocity(new Vector2(-this.getVelocity().x, -this.getVelocity().y));
        }
        hasCollided = true;
    }







}
