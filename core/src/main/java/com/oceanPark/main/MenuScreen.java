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
import com.badlogic.gdx.utils.JsonWriter;
import com.oceanPark.main.model.Player;

import java.io.IOException;
import java.io.StringWriter;

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
                        .set("name",nombre.getText());

                    String resultado = writer.toString();
                    msg(resultado);

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                updatePlayers();
                game.setScreen(new GameScreen(game));
            }
        });



        //lista de players

        //iniciamos lista de players
        scrollPane = new ScrollPane(lPlayers,skin);
        scrollPane.setFadeScrollBars(true);

        labelInfo = new Label("Players",labelStyle);
        //s

        Table table = new Table(skin);
        table.setFillParent(true);

        table.add(labelInfo).size(400,50).padTop(20);
        table.row();
        table.add(scrollPane).size(400,200);


//        table.add(labelNombre).size(300,50).padTop(20);
//        table.row();
//        table.add(nombre).size(300,50);
//        table.add(lPlayers).size(600,200);
//        table.row();
//        table.add(labelPlayerInfo).size(400,50);
//        table.add(labelInfo).size(300,50);
//        table.row();
//        table.add(button).size(100,50);
        labelNombre.setScale(3*escala);
        labelNombre.setPosition(50,300);
        nombre.setPosition(50,250);
        nombre.setScale(3*escala);
        button.setScale(4*escala);
        button.setPosition(50,100);

        table.setScale(2*escala);
        table.setPosition(150,50);



        stage.addActor(nombre);
        stage.addActor(labelNombre);
        stage.addActor(button);
        stage.addActor(table);



    }

    public void msg(String msg){

        game.socket.send(msg);
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
