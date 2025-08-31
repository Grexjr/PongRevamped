package com.badlogic.pongrevamp.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface PhysicsObject {

    Vector2 getPosition();
    void setPosition(Vector2 newPos);
    void changePosition(Vector2 changeVector);

    Vector2 getVelocity();
    void setVelocity(Vector2 newVelocity);
    void changeVelocity(Vector2 changeVector);

    Vector2 getAcceleration();
    void setAcceleration(Vector2 newAcceleration);
    void changeAcceleration(Vector2 changeVector);

    Rectangle getPhysRectangle();

    void move(float delta);

    void enforceTopSpeed(float delta);





}
