package com.oceanPark.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSockets;
import com.oceanPark.main.model.Door;
import com.oceanPark.main.model.Key;
import com.oceanPark.main.model.Player;
import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    String playerId;
    String playerName;

    final JsonReader lector = new JsonReader();

    HashMap<String, Player> jugadoresMap = new HashMap<>();
    HashMap<String, Key> keyMap = new HashMap<>();
    HashMap<String, Door> doorMap = new HashMap<>();

    private final Array<String> queue = new Array<>();

    WebSocket socket;

    FitViewport viewport;

    Skin skin;

    Float escala;


    SpriteBatch batch;

    Texture flechaTexture,flechaUp, mushPlayer,key;

    Texture backgroundTexture;

    HashMap<String,TextureRegion[][]> mapaSprites;
    HashMap<String, Animation<TextureRegion>> mapaAnimation;
    private JsonValue levelData;
    private Texture tilesetTexture;
    private TextureRegion[][] tilesetRegions;
    private JsonValue tileMapData; // Aquí cargaremos el level_000_layer_000.json

    // Offset y configuración del Viewport del JSON
    private float offsetX, offsetY;
    private int tileW, tileH;



    @Override
    public void create() {

        mapaAnimation= new HashMap<>();
        mapaSprites = new HashMap<>();

        flechaTexture= new Texture("flecha.png");
        flechaUp= new Texture("flecha_up.png");
        //backgroundTexture = new Texture("background_oceanPark.png");
        mushPlayer= new Texture("mushroom_iddle.png");
        key = new Texture("key-rbg.png");

        skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Carregar un Skin per defecte
        cargarGameData();


        socket = WebSockets.newSocket("wss://pico3.ieti.site:443");

        // 2. Configurar el listener
        socket.addListener(new WebSocketAdapter() {
            @Override
            public boolean onOpen(WebSocket webSocket) {
                Gdx.app.log("WS", "Conectado exitosamente");
                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, String packet) {

                synchronized(queue) {
                    queue.add(packet);
                }

                return FULLY_HANDLED;
            }
        });

        // 3. Conectar
        Thread networkThread = new Thread(() -> {
            try {
                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        // En el create() de tu Main
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                if (socket != null && socket.isOpen()) {
//                    socket.send("{\"type\":\"ping\"}");
//                }
//            }
//        }, 5, 5); // Cada 5 segundos
        networkThread.setPriority(Thread.MIN_PRIORITY); // Dale prioridad baja para no asfixiar al GLThread
        networkThread.start();



       // viewport = new FitViewport(320, 180);
//        viewport = new FitViewport(800, 480);

        escala = viewport.getWorldHeight() / Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
    }


    @Override
    public void dispose() {
        if (socket != null) {
            socket.close();
        }
        batch.dispose();
        if (skin != null) skin.dispose();
    }

    @Override
    public void render() {


        synchronized(queue) {

            if (queue.size > 0) {
                Gdx.app.log("MSG_TEST",queue.get(0));
                Screen pantallaActual = getScreen();
                for (String msg : queue) {

                    if (pantallaActual instanceof GameScreen) {
                        ((GameScreen) pantallaActual).msg(msg);
                    } else if (pantallaActual instanceof MenuScreen) {
                        ((MenuScreen) pantallaActual).msg(msg);
                    }
                }
                queue.clear();
            }
        }

        super.render();
    }
    public void cargarGameData() {
        JsonReader reader = new JsonReader();
        // 1. Leer el manifiesto principal
        JsonValue root = reader.parse(Gdx.files.internal("game_data.json"));

        // 2. Obtener el primer nivel (Ocean World)
        levelData = root.get("levels").get(0);

        // 3. Configurar Viewport
        viewport= new FitViewport(levelData.getInt("viewportWidth"),levelData.getInt("viewportHeight"));
        // 4. Cargar la capa de Tiles
        JsonValue layer = levelData.get("layers").get(0);
        offsetX = layer.getFloat("x");
        offsetY = layer.getFloat("y");
        tileW = layer.getInt("tilesWidth"); // 23
        tileH = layer.getInt("tilesHeight"); // 23

        // 5. Cargar Textura del Tileset
        tilesetTexture = new Texture(Gdx.files.internal(layer.getString("tilesSheetFile")));
        tilesetTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        tilesetRegions = TextureRegion.split(tilesetTexture, tileW, tileH);

        // 6. Cargar el mapa de bits (la cuadrícula de IDs)
        // Este archivo contiene un array "data" con los números de cada tile
        tileMapData = reader.parse(Gdx.files.internal(layer.getString("tileMapFile")));

        cargarSprites(root);


        //cargar animaciones
        root = reader.parse(Gdx.files.internal("animations/animations.json"));
        cargarAnimaciones(root);


    }
    public void renderMapa(SpriteBatch batch) {
        if (tileMapData == null) return;
        JsonValue tileMap = tileMapData.get("tileMap");

        // Altura total del mapa: 42 filas * 23px = 966px
        float mapaAlturaTotal = tileMap.size * tileH;

        int rowIndex = 0;
        for (JsonValue row : tileMap) {
            int colIndex = 0;
            for (JsonValue tile : row) {
                int tileId = tile.asInt();
                if (tileId != -1) {
                    float drawX = (colIndex * tileW) + offsetX;

                    // Esta fórmula alinea el JSON con el mundo de LibGDX    -600 provicional
                    float drawY = (mapaAlturaTotal - ((rowIndex+1) * tileH)) + offsetY;

                    int tilesPerRow = tilesetTexture.getWidth() / tileW;

                    int tileCol = tileId % tilesPerRow;
                    int tileRow = tileId / tilesPerRow;

                    batch.draw(tilesetRegions[tileRow][tileCol], drawX, drawY, tileW, tileH);
                }
                colIndex++;
            }
            rowIndex++;
        }
    }

    public void cargarAnimaciones(JsonValue animRoot) {

        JsonValue animations = animRoot.get("animations");

        for (JsonValue animData : animations) {
            String animName = animData.getString("name");
            String mediaFile = animData.getString("mediaFile");
            int start = animData.getInt("startFrame");
            int end = animData.getInt("endFrame");
            float fps = animData.getFloat("fps");
            boolean loop = animData.getBoolean("loop");

            TextureRegion[][] regiones = mapaSprites.get(mediaFile);

            if(regiones==null){
                Gdx.app.log("TEST_NULL","name: "+animName+"  filename: "+mediaFile);

                continue;
            }else {
                Gdx.app.log("TEST_EXIST","name: "+animName+"  filename: "+mediaFile);
            }
            Array<TextureRegion> frames = new Array<>();
            for (int i = start; i <= end; i++) {
                frames.add(regiones[0][i]);
            }

            Animation<TextureRegion> anim = new Animation<>(1f / fps, frames);
            anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

            mapaAnimation.put(animName, anim);
        }
    }

    public void cargarSprites(JsonValue root){
        //carga sprites
        JsonValue mediaAssets = root.get("mediaAssets"); //
        for (JsonValue asset : mediaAssets) {
            String nombre = asset.getString("name"); //
            String archivo = asset.getString("fileName"); //
            int tileW = asset.getInt("tileWidth"); //
            int tileH = asset.getInt("tileHeight"); //
            Gdx.app.log("TEST_SPRITE","nombre: "+archivo+ " archivo: "+archivo);
            // Guardamos las regiones en un mapa para usarlas después por nombre
            Texture tex = new Texture(Gdx.files.internal(archivo));
            tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

            TextureRegion[][] regions = TextureRegion.split(tex, tileW, tileH);

            mapaSprites.put(archivo, regions);
        }
    }
}
