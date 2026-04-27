package com.oceanPark.main.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oceanPark.main.data.States;

public class Key extends Actor {
    public float posX,posY;
    public float stateTime;
    public Texture currentFrame;


    public Key(String name,Texture texture) {
        this.setName(name);
        this.posX=50;
        this.posY=50;
        this.stateTime = 0;
        this.setSize(32,32);
        currentFrame=texture;
    }


    public void updatePoss(float x,float y){
        posX=x;
        posY=y;
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
        batch.draw(currentFrame, getX(), getY(),getWidth(),getHeight());
    }

    @Override
    public String toString() {
        return getName();
    }
}
