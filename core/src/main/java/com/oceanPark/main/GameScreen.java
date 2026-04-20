package com.oceanPark.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {

    final Main game;
    Texture backgroundTexture;

    Stage stage;

    Skin skin;

    public GameScreen(final Main game){
        this.game=game;
        this.stage=new Stage(game.viewport);
        this.skin=game.skin;

        backgroundTexture = new Texture("background_oceanPark.png");
        Image fondo = new Image(backgroundTexture);
        fondo.setFillParent(true); // Hace que el fondo ocupe todo el viewport
        stage.addActor(fondo);

        Gdx.input.setInputProcessor(stage);



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // organize code into three methods
        input();
        logic();
        draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {

        game.viewport.update(width, height, true);
    }

    @Override public void pause() {

    }
    @Override public void resume() {

    }

    @Override
    public void hide() {

    }

    public void msg(String msg){

    }


    private void draw() {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualitzar i dibuixar l'Stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


//      store the worldWidth and worldHeight as local variables for brevity
//        float worldWidth = game.viewport.getWorldWidth();
//        float worldHeight = game.viewport.getWorldHeight();



//        bucketSprite.draw(spriteBatch); // Sprites have their own draw method

//       spriteBatch.draw(bucketTexture, 0, 0, 1, 1); // draw the bucket with width/height of 1 meter
        // draw each sprite
//        for (Sprite dropSprite : dropSprites) {
//            dropSprite.draw(spriteBatch);
//        }

    }
    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //bucketSprite.translateX(speed * delta); // move the bucket right
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //bucketSprite.translateX(-speed * delta); // move the bucket left
        }
        if (Gdx.input.isTouched()) {
            //touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            //viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            //bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }
    }


    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

    }


    void crearSprites(){
//        Animation<TextureRegion> walk;
//
//        spriteSheet = new Texture("perro_sprite.png");
//        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 33, 33);
//
//        TextureRegion[] walkFrames = new TextureRegion[10];
//                for (int i = 0; i < 10; i++) {
//        walkFrames[i] = tmp[0][i];
//            }
//        walkAnimation = new Animation<>(0.1f, walkFrames); // 0.1f es la velocidad
//
//        TextureRegion[] iddleFrames = new TextureRegion[2];
//
//                for (int i = 0; i < 2; i++) {
//        iddleFrames[i] = tmp[0][i+22];
//            }
//
//        idleAnimation = new Animation<>(0.1f,iddleFrames);
//
//        TextureRegion[] jumpFrames = new TextureRegion[2];
//
//                for (int i = 0; i < 7; i++) {
//        jumpFrames[i] = tmp[0][i+25];
//            }
//        jumpAnimation = new Animation<>(0.1f,jumpFrames);
    }



}






