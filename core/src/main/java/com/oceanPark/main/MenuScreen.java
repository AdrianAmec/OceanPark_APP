package com.oceanPark.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.oceanPark.main.model.Coin;
import com.oceanPark.main.model.Door;
import com.oceanPark.main.model.Key;
import com.oceanPark.main.model.Player;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

public class MenuScreen implements Screen {
    final Main game;
    Stage stage;
    Skin skin;


    TextField nombre;
    Label labelNombre,labelInfo;
    TextButton button;


    List<Player> lPlayers;

    ScrollPane scrollPane;
    Label labelPlayerInfo;

    float escala;


    public MenuScreen(final Main game){
        this.game=game;
        this.stage= new Stage(game.viewport);

        escala = game.escala;
        skin = game.skin;

        lPlayers= new List<>(skin);


        //inicializando texts
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        if (textFieldStyle.fontColor == null) {
            textFieldStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE; // O el color que prefieras
        }
        textFieldStyle.font = new BitmapFont();
        textFieldStyle.font.setUseIntegerPositions(false);
        nombre= new TextField("",textFieldStyle);
        nombre.setMessageText("Ingrese Nombre");

        //inicializando estilo de labels
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(); // Font per defecte
        labelStyle.font.setUseIntegerPositions(false);


        labelPlayerInfo = new Label("Lista de Jugadores",labelStyle);
        labelNombre = new Label("Nombre",labelStyle);
        labelInfo = new Label("",labelStyle);

        button= new TextButton("login",skin);
        button.setTransform(true);
        button.setScale(2*escala);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {


                StringWriter writer = new StringWriter();
                JsonWriter json = new JsonWriter(writer);

                try {
                    json.object() // Empieza con {
                        .set("type", "JOIN")
                        .set("name",nombre.getText())
                    .pop();

                    json.close();

                    String resultado = writer.toString();
                    Gdx.app.log("MSG_TEST_ENVIAR", resultado);

                    game.socket.send(resultado);

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                updatePlayers();
            }
        });



        //lista de players

        //iniciamos lista de players
        scrollPane = new ScrollPane(lPlayers,skin);
        scrollPane.setFadeScrollBars(true);

        labelInfo = new Label("Players",labelStyle);
        //s

        Table table = new Table(skin);
        table.right();
        table.setFillParent(true);

        table.add(labelInfo).size(160,20).padTop(8);
        table.row();
        table.add(scrollPane).size(160,80);


//        table.add(labelNombre).size(300,50).padTop(20);
//        table.row();
//        table.add(nombre).size(300,50);
//        table.add(lPlayers).size(600,200);
//        table.row();
//        table.add(labelPlayerInfo).size(400,50);
//        table.add(labelInfo).size(300,50);
//        table.row();
//        table.add(button).size(100,50);
//        labelNombre.setScale(0.8f*escala);
//        labelNombre.setPosition(20,112);
//        nombre.setPosition(20,93);
//        nombre.setSize(200f * escala, 40f * escala);
//        button.setSize(100*1.6f*escala,40*1.6f*escala);
//        button.setPosition(20,37);
//
//        table.setScale(0.8f*escala);
//        table.setPosition(60,18);

        Table menu = new Table();
        menu.left();
        menu.setFillParent(true); // Que ocupe toda la pantalla del uiStage

// Agregamos el Label
        menu.add(labelNombre).padBottom(10).size(200,20);

        menu.row(); // Nueva fila

// Agregamos el TextField (Aquí definimos el tamaño sin deformar)
// El método .size(ancho, alto) ajusta la hitbox y el dibujo perfectamente
        menu.add(nombre).size(200, 40).padBottom(20);
        menu.row();

// Agregamos el Botón
        menu.add(button).size(150, 50);

// Lo añadimos al stage de la interfaz
        stage.addActor(menu);



//        stage.addActor(nombre);
//        stage.addActor(labelNombre);
//        stage.addActor(button);
        stage.addActor(table);



    }

    public void msg(String msg){

        JsonValue base = game.lector.parse(msg);

        // Obtener el array "jugadores"
        String mensaje = base.getString("type");
        if(mensaje.equals("JOINED")) {
            game.playerId = base.getString("playerId");
            ;
            game.playerName = base.getString("name");
            game.setScreen(new GameScreen(game));
        } else if (mensaje.equals("ERROR")) {
            String info = base.getString("message");
            labelInfo.setText(info);
            //game.setScreen(new GameScreen(game));
        }

    }

    public void updatePlayers() {
        Array<Player> arrayParaLista = new Array<>();
        for (Player p : game.jugadoresMap.values()) {
            arrayParaLista.add(p);
        }
        lPlayers.setItems(arrayParaLista);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
