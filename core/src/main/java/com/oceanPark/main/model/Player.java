package com.oceanPark.main.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oceanPark.main.data.States;

public class Player extends Actor {

    public float posX,posY;
    public float stateTime;
    public States state;
    public boolean facingRight;
    public Texture currentFrame;
    Boolean ready;

    public Player(String name,Texture texture) {
        this.setName(name);
        this.posX=50;
        this.posY=50;
        this.stateTime = 0;
        this.setSize(32,32);
        state=States.IDDLE;
        facingRight=false;
        ready=false;
        currentFrame=texture;

    }
    public Player(String name,float posX,float posY,States state,boolean facingRight,Texture texture){
        this.setName(name);
        this.posX=posX;
        this.posY=posY;
        this.state=state;
        this.facingRight=facingRight;
        this.currentFrame=texture;
        this.setSize(32,32);
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



