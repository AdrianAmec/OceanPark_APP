package com.oceanPark.main;

import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 posicion;
    public float stateTime;
    public String name;
    public states state;
    public boolean facingRight;
    Boolean ready;

    public Player(String name) {
        this.name = name;
        this.posicion = new Vector2(0, 0);
        this.stateTime = 0;
        state=states.IDDLE;
        facingRight=false;
        ready=false;
    }

    public void update(float delta) {
        stateTime += delta;
        // Lógica de movimiento aquí...
    }

    @Override
    public String toString() {
        return name;
    }

}


enum states {
        WALKING,JUMPING,IDDLE
    }
