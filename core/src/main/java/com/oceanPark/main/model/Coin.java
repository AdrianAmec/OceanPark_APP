package com.oceanPark.main.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oceanPark.main.data.States;

public class Coin extends Actor {
    public float posX,posY;
    public float stateTime;
    public TextureRegion currentFrame;
    public Animation<TextureRegion> animation;
    public boolean taken;
    public String holder;



    public Coin(String name,Animation<TextureRegion> animation, float x, float y) {
        this.setName(name);
        this.posX=x;
        this.posY=y;
        this.stateTime = 0;
        this.setSize(32,32);
        this.animation=animation;
        taken=false;
        holder="";

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
        currentFrame = animation.getKeyFrame(stateTime);
        batch.draw(currentFrame, getX(), getY(),getWidth(),getHeight());
    }

    @Override
    public String toString() {
        return getName();
    }
}
