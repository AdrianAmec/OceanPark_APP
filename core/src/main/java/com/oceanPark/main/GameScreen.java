package com.oceanPark.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.oceanPark.main.data.PlayerData;
import com.oceanPark.main.model.Player;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {
    float escala;
    Label labelTest;
    final Main game;
    Texture backgroundTexture;
    Texture flechaTexture;

    Stage stage;

    Skin skin;

    public GameScreen(final Main game){
        this.game=game;
        this.stage=new Stage(game.viewport);
        this.skin=game.skin;

        escala= game.escala;

        //inicializando estilo de labels
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(); // Font per defecte
        labelStyle.font.setUseIntegerPositions(false);
        labelTest = new Label("Test",labelStyle);

        labelTest.setScale(4*escala);
        labelTest.setPosition(250,250);

        //fondo de pantalla
        backgroundTexture = new Texture("background_oceanPark.png");

        //botones en pantalla
        flechaTexture = new Texture("flecha.png");
        Image fondo = new Image(backgroundTexture);
        fondo.setFillParent(true); // Hace que el fondo ocupe todo el viewport

        TextureRegion flecha = new TextureRegion(flechaTexture);
        TextureRegion flechaIze = new TextureRegion(flechaTexture);
        flechaIze.flip(true,false);

        flechaTexture = new Texture("flecha_up.png");

        TextureRegion flechaUp= new TextureRegion(flechaTexture);
        ImageButton btnDer = new ImageButton(new TextureRegionDrawable(flecha));
        ImageButton btnIzq = new ImageButton(new TextureRegionDrawable(flechaIze));
        ImageButton btnUp = new ImageButton(new TextureRegionDrawable(flechaUp));

        btnIzq.getColor().a=0.3f;
        btnDer.getColor().a=0.3f;
        btnUp.getColor().a=0.3f;

        btnDer.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                labelTest.setText("Moviendo Derecha!!!!");
                Player player=game.jugadoresMap.get("1");
                player.updatePoss(player.posX+10f, player.posY);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                labelTest.setText("STOP!!!");
            }
        });
        btnIzq.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                labelTest.setText("Moviendo Izquierda!!!!");
                Player player=game.jugadoresMap.get("1");
                player.updatePoss(player.posX-10f, player.posY);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                labelTest.setText("STOPP!!!!");

            }
        });

        btnUp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                labelTest.setText("Saltando!!!!");

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                labelTest.setText("Boton Suelto!!!!");

            }
        });



        //organizando los controles en pantalla
        Table controles = new Table();
        controles.setFillParent(true);
        controles.bottom().left();

        controles.add(btnIzq).size(100, 100).bottom().pad(20);
        controles.add(btnDer).size(100, 100).bottom().pad(20);
        controles.add().expandX();
        controles.add(btnUp).size(300,300).bottom().right().pad(20);
        controles.setPosition(10,10);


        Texture texture = new Texture("flecha.png");
        Player player = new Player("test1");
        game.jugadoresMap.put("1",player);
        game.jugadoresMap.get("1").currentFrame=texture;


        stage.addActor(fondo);

        stage.addActor(labelTest);
        stage.addActor(player);
        stage.addActor(controles);
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
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

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



    }
    public void updateAllPlayersPositions(Array<PlayerData> dataFromServer) {
        for (PlayerData data : dataFromServer) {

            Player p = game.jugadoresMap.get(data.id);

            if (p != null) {
                p.setPosition(data.x, data.y);
                p.state = data.state;
                p.facingRight = data.facingRight;
            }else {
                Texture texture = new Texture("flecha.png");
                Player player = new Player(data.name,data.x, data.y, data.state,data.facingRight,texture);
                game.jugadoresMap.put(data.id,player);
                stage.addActor(player);

            }
        }
    }


    public void onUserJoined(String id, String nombre) {
        Player nuevoJugador = new Player(nombre);
        game.jugadoresMap.put(id, nuevoJugador);
        stage.addActor(nuevoJugador);
    }



}






