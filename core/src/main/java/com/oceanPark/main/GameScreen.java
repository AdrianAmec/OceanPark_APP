package com.oceanPark.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oceanPark.main.data.PlayerData;
import com.oceanPark.main.model.Coin;
import com.oceanPark.main.model.Door;
import com.oceanPark.main.model.Key;
import com.oceanPark.main.model.Player;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {


    //{"type":"STATE",
    // "players":[{"id":"045cceea-67a8-4a94-88fa-14187b00b4a7","name":"ee","x":80,"y":278,"state":"IDLE","facingRight":true,"onGround":true,"isVisor":false,"hasKey":false}],
    // "world":{"key":{"x":170,"y":295,"width":24,"height":24,"taken":false,"holderId":null},
    // "door":{"x":460,"y":340,"width":40,"height":60,"open":false},
    // "coins":[{"id":"coin_0","x":70,"y":380,"value":10,"isBonus":false},{"id":"coin_1","x":110,"y":380,"value":10,"isBonus":false},{"id":"coin_2","x":150,"y":380,"value":10,"isBonus":false},{"id":"coin_3","x":190,"y":380,"value":10,"isBonus":false},{"id":"coin_4","x":230,"y":380,"value":10,"isBonus":false},{"id":"coin_5","x":270,"y":380,"value":10,"isBonus":false},{"id":"coin_6","x":165,"y":300,"value":10,"isBonus":false},{"id":"coin_7","x":185,"y":300,"value":10,"isBonus":false},{"id":"coin_8","x":370,"y":375,"value":10,"isBonus":false},{"id":"coin_9","x":410,"y":375,"value":10,"isBonus":false},{"id":"coin_10","x":450,"y":375,"value":10,"isBonus":false},{"id":"coin_13","x":480,"y":375,"value":10,"isBonus":false}],"totalCoins":14,"collectedCoins":2,"platforms":[{"x":42,"y":402,"width":246,"height":13,"name":"Suelo1"},{"x":335,"y":401,"width":168,"height":14,"name":"Suelo2"},{"x":150,"y":325,"width":62,"height":14,"name":"Plataforma"},{"x":58,"y":278,"width":47,"height":14,"name":"PlataformaAlta"},{"x":104,"y":278,"width":16,"height":14,"name":"Escalon1"},{"x":120,"y":294,"width":16,"height":14,"name":"Escalon2"},{"x":42,"y":264,"width":16,"height":14,"name":"Escalon3"},{"x":26,"y":249,"width":16,"height":14,"name":"Escalon4"},{"x":134,"y":309,"width":16,"height":14,"name":"Escalon5"}],"deathZones":[{"x":-231,"y":436,"width":1664,"height":153,"name":"Foso"}],"spawnPoints":[{"x":80,"y":370},{"x":130,"y":370},{"x":180,"y":370},{"x":230,"y":370},{"x":280,"y":370},{"x":370,"y":370},{"x":420,"y":370},{"x":470,"y":370}],"playerZone":{"x":25,"y":100,"width":400,"height":400},"levelCompleted":false,"width":2300,"height":1380,"viewportWidth":320,"viewportHeight":180,"backgroundColor":"#568BB1"}}
    JsonReader lector;
    float escala;
    Label labelTest;
    final Main game;

    Stage stage,worldStage;

    Skin skin;
    private Viewport uiViewport,worldViewport;

    public GameScreen(final Main game){
        uiViewport = new ScreenViewport();
        worldViewport = new FitViewport(320, 180);
//        worldViewport = new FitViewport(1000, 1000);

        this.game=game;
        this.stage=new Stage(uiViewport, game.batch);
        this.worldStage= new Stage(worldViewport, game.batch);
        this.skin=game.skin;



        lector = new JsonReader();

        escala= game.escala;

        //inicializando estilo de labels
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(); // Font per defecte
        labelStyle.font.setUseIntegerPositions(false);
        labelTest = new Label("Test",labelStyle);

        labelTest.setScale(1.2f*escala);
        labelTest.setPosition(100,100);

        //fondo de pantalla

        //botones en pantalla




        TextureRegion flecha = new TextureRegion(game.flechaTexture);
        TextureRegion flechaIze = new TextureRegion(game.flechaTexture);
        flechaIze.flip(true,false);

        TextureRegion flechaUp= new TextureRegion(game.flechaUp);
        ImageButton btnDer = new ImageButton(new TextureRegionDrawable(flecha));
        ImageButton btnIzq = new ImageButton(new TextureRegionDrawable(flechaIze));
        ImageButton btnUp = new ImageButton(new TextureRegionDrawable(flechaUp));

        btnIzq.getColor().a=0.3f;
        btnDer.getColor().a=0.3f;
        btnUp.getColor().a=0.3f;

        btnDer.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                move("RIGHT",true);
                game.jugadoresMap.get(game.playerId).setAnimation(game.mapaAnimation.get("Mushroom Right"));
                game.jugadoresMap.get(game.playerId).facingRight=true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                move("RIGHT",false);
                game.jugadoresMap.get(game.playerId).setAnimation(game.mapaAnimation.get("Mushroom Idle"));
                game.jugadoresMap.get(game.playerId).facingRight=false;

            }
        });
        btnIzq.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                move("LEFT",true);
                game.jugadoresMap.get(game.playerId).setAnimation(game.mapaAnimation.get("Mushroom  Left"));
                game.jugadoresMap.get(game.playerId).facingRight=false;


                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                move("LEFT",false);
                game.jugadoresMap.get(game.playerId).setAnimation(game.mapaAnimation.get("Mushroom Idle"));
                game.jugadoresMap.get(game.playerId).facingRight=false;


            }
        });

        btnUp.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                move("JUMP",true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                move("JUMP",false);
            }
        });



        //organizando los controles en pantalla
        Table controles = new Table();
        controles.setFillParent(true);
        controles.bottom().left();

        controles.add(btnIzq).size(400, 400).bottom().pad(8);
        controles.add(btnDer).size(400, 400).bottom().pad(8);
        controles.add().expandX();
        controles.add(btnUp).size(550,550).bottom().right().pad(8);
        controles.setPosition(4,4);



//        //pruebas
//        Player player = new Player("asd",game.mushPlayer);
//        player.posX=160;
//        player.posY=930;
//        game.jugadoresMap.put("asd",player);
//        worldStage.addActor(player);
//
//        Key key = new Key("1",game.key);
//        key.updatePoss(130,930);
//        worldStage.addActor(key);
//        //pruebas

        stage.addActor(labelTest);
        stage.addActor(controles);
        Gdx.input.setInputProcessor(stage);
    }

    public void move(String direcicon,boolean b){
        StringWriter writer = new StringWriter();
        JsonWriter json = new JsonWriter(writer);

        try {
            json.object() // Empieza con {
                .set("type", "MOVE")
                .set(direcicon,b)
                .pop();
            json.close();

            String resultado = writer.toString();
            Gdx.app.log("MSG_TEST_ENVIAR", resultado);

            game.socket.send(resultado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // organize code into three methods
        //input();
        draw(delta);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, false);
        uiViewport.update(width, height, true);    }

    @Override public void pause() {

    }
    @Override public void resume() {

    }

    @Override
    public void hide() {

    }

    public void msg(String msg){
        //< {"type":"STATE",
        //  "players":[
        //          {"id":"88042af4-034b-4cbe-ac15-8faf3edcf612","name":"ijsdficusbi","x":100,"y":100,"skin":"mew"},
        //          {"id":"564bb5e1-ef56-4af9-a448-31fa9dd288dc","name":"Player1","x":110,"y":100,"skin":"creeper"}]}
        JsonValue base = lector.parse(msg);

        // Obtener el array "jugadores"
        String mensaje = base.getString("type");
        if(mensaje.equals("STATE")){
            //actualizamos jugadores
            JsonValue players = base.get("players");
            for (JsonValue jugador : players) {
                String playerId = jugador.getString("id");
                Player p = game.jugadoresMap.get(playerId);
                //actualizamos existentes
                if(p!=null){
                    p.posY=jugador.getFloat("y");
                    p.posX=jugador.getFloat("x");
                    p.facingRight=jugador.getBoolean("facingRight");
                    //Gdx.app.log("TEST_right",p.facingRight+" ave");

                    //logica iddle
                //creamos nuevos
                } else {
                    //Gdx.app.log("player",jugador.toString());
                    Player player = new Player(jugador.getString("name"),game.mapaAnimation.get("Mushroom Idle"));
                    player.posX=jugador.getFloat("x");
                    player.posY=jugador.getFloat("y");
                    game.jugadoresMap.put(playerId,player);
                    worldStage.addActor(player);
                }
            }
            //actualizamos mundo
            JsonValue world = base.get("world");
            for (JsonValue entity : world) {
                //actalizamos llaves
                if(entity.name.equals("key")){
                    String id = "1";
                    Float x = entity.getFloat("x");
                    Float y = entity.getFloat("y");
                    Key k = game.keyMap.get(id);

                    if(k!=null){
                        if(k.taken){
                            Player p = game.jugadoresMap.get(entity.getString("holderID"));
                            k.updatePoss(p.getX(),p.posY+32);
                        }else {
                            k.updatePoss(x,y);
                        }

                    }else {
                        Key key = new Key("key",game.mapaAnimation.get("Leaf Idle"));
                        key.setPosition(x,y);
                        game.keyMap.put("1",key);
                        worldStage.addActor(key);
                    }
                //actualizamos puertas
                }else if(entity.name.equals("door")){
                    String id = "1";
                    Float x = entity.getFloat("x");
                    Float y = entity.getFloat("y");
                    Door d = game.doorMap.get(id);
                    if(d!=null){
                        d.open=entity.getBoolean("open");
                    }else {
                        //agregar sprites
                        Door door = new Door("1",game.mapaAnimation.get("Leaf Idle"));
                        door.updatePoss(entity.getFloat("x"),entity.getFloat("y"));
                        game.doorMap.put(id,door);
                        worldStage.addActor(door);
                    }
                }else if(entity.equals("coins")){
                    for (JsonValue coin : entity) {
                        String id = coin.getString("id");
                        Float x = coin.getFloat("x");
                        Float y = coin.getFloat("y");
                        Coin c = game.coinMap.get(id);

                        if(c==null){
                            Coin moneda = new Coin(id,game.mapaAnimation.get("Leaf Idle"),x,y);
                            game.coinMap.put(id,moneda);
                            worldStage.addActor(moneda);
                        }
                    }
                }
            }
        }
    }


    private void draw(float delta) {
//        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClearColor(0.33f, 0.54f, 0.69f, 1); // El backgroundColorHex del JSON
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldViewport.apply(true);

        if (game.jugadoresMap.get(game.playerId) != null) {
            float targetX = game.jugadoresMap.get(game.playerId).posX;
            float targetY = game.jugadoresMap.get(game.playerId).posY;

            // Redondeamos para evitar el efecto de colores mezclados
            worldViewport.getCamera().position.set(Math.round(targetX), Math.round(targetY), 0);
        } else {
            // Si no hay player, al menos apunta a una zona con bloques según tus logs
            worldViewport.getCamera().position.set(160, 930, 0);
        }
//        worldViewport.getCamera().position.set(160, 930, 0);
        worldViewport.getCamera().update();

        //Mapa
        game.batch.setProjectionMatrix(worldViewport.getCamera().combined);
        game.batch.disableBlending();

        game.batch.begin();

        game.renderMapa(game.batch); // Dibujamos el fondo primero

        game.batch.end();
        game.batch.enableBlending(); // importante restaurar


        // Dibujamos a los Players (worldStage)
        // Este stage se moverá junto con la cámara
        worldStage.act(delta);
        //worldStage.getBatch().disableBlending();
        worldStage.draw();
        //worldStage.getBatch().enableBlending();


        uiViewport.apply(true);

        // El stage dibuja los Players (Mushroom) que vienen del servidor

        stage.act(delta);
        stage.draw();
    }



//    public void onUserJoined(String id, String nombre) {
//        Player nuevoJugador = new Player(nombre,flechaTexture);
//        game.jugadoresMap.put(id, nuevoJugador);
//        stage.addActor(nuevoJugador);
//    }
}






