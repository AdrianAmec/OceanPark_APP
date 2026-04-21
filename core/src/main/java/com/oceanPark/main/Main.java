package com.oceanPark.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSockets;
import com.oceanPark.main.model.Player;
import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    String id = "1";

    HashMap<String, Player> jugadoresMap = new HashMap<>();
    HashMap<String,Player> KeyMap = new HashMap<>();
    HashMap<String,Player> DoorMap = new HashMap<>();
    WebSocket socket;

    FitViewport viewport;

    Skin skin;

    Float escala;


    SpriteBatch batch;


    @Override
    public void create() {


        skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Carregar un Skin per defecte



        socket = WebSockets.newSocket("ws://ieticloudpro.ieti.cat:3000");

        // 2. Configurar el listener
        socket.addListener(new WebSocketAdapter() {
            @Override
            public boolean onOpen(WebSocket webSocket) {
                Gdx.app.log("WS", "Conectado exitosamente");
                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, String packet) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // Obtenemos la pantalla actual
                        Screen pantallaActual = getScreen();

                        // Si la pantalla es la de Ranking, le pasamos el mensaje
                        if (pantallaActual instanceof GameScreen) {
                            ((GameScreen) pantallaActual).msg(packet);
                        }
                        // Si es la de Juego, se lo pasamos a la otra
                        else if (pantallaActual instanceof MenuScreen) {
                            ((MenuScreen) pantallaActual).msg(packet);
                        }
                    }
                });
                return FULLY_HANDLED;
            }
        });

        // 3. Conectar
        socket.connect();


        viewport = new FitViewport(800, 480);
        escala = viewport.getWorldHeight() / Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
    }


    @Override
    public void dispose() {
        batch.dispose();
    }
}
