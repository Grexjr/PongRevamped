package com.badlogic.pongrevamp.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Renderable {

    Sprite getSprite();

    Texture getTexture();

    void updateRenderablePosition(Vector2 position);

    void setRectangle();







}
