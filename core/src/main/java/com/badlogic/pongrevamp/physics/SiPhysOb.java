package com.badlogic.pongrevamp.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class SiPhysOb implements PhysicsObject{

    private final Rectangle physRectangle;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;

    public SiPhysOb(Vector2 position, Vector2 velocity, Vector2 acceleration, Rectangle rectangle){
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.physRectangle = rectangle;
    }

    // PhysicsObject methods
    @Override
    public Vector2 getPosition(){return this.position;}

    public void setPosition(Vector2 newPos){this.position = newPos;}

    @Override
    public void changePosition(Vector2 changeVector){ // Always adds... is that correct?
        Vector2 oldPos = this.position;
        this.position = new Vector2(oldPos.x + changeVector.x, oldPos.y + changeVector.y);
    }

    @Override
    public Vector2 getVelocity(){return this.velocity;}

    public void setVelocity(Vector2 newVelocity){this.velocity = newVelocity;}

    @Override
    public void changeVelocity(Vector2 changeVector){
        Vector2 oldVelocity = this.velocity;
        this.velocity = new Vector2(oldVelocity.x + changeVector.x, oldVelocity.y + changeVector.y);
    }

    @Override
    public Vector2 getAcceleration(){return this.acceleration;}

    public void setAcceleration(Vector2 newAcceleration){this.acceleration = newAcceleration;}

    @Override
    public void changeAcceleration(Vector2 changeVector){
        Vector2 oldAcceleration = this.acceleration;
        this.acceleration = new Vector2(oldAcceleration.x + changeVector.x, oldAcceleration.y + changeVector.y);
    }

    @Override
    public Rectangle getPhysRectangle(){return this.physRectangle;}

    @Override
    public void move(float delta){
        changeVelocity(this.getAcceleration());
        changePosition(this.getVelocity());
    }







}
