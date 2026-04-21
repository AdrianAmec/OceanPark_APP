package com.oceanPark.main.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Key extends Actor {
    public float posX,posY;
    public float stateTime;

    public Key() {
        this.posX=0;
        this.posY=0;
        this.stateTime = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        this.setPosition(posX, posY);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Aquí dibujas tu textura o animación
        // batch.draw(textura, getX(), getY(), ...);
    }
}
