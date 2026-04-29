package com.oceanPark.main.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;

public class Map extends Actor {
    private JsonValue tileMap;
    private TextureRegion[][] tilesetRegions;
    private int tileW, tileH;
    private float viewportYConfig;
    private float mapaAlturaTotal;

    public Map(JsonValue level, JsonValue tileMapData, Texture tilesetTexture) {
        // Extraer datos del JSON
        this.tileMap = tileMapData.get("tileMap");
        JsonValue layer = level.get("layers").get(0);

        this.tileW = layer.getInt("tilesWidth"); // 23
        this.tileH = layer.getInt("tilesHeight"); // 23
        this.viewportYConfig = level.getFloat("viewportY"); // 473

        this.tilesetRegions = TextureRegion.split(tilesetTexture, tileW, tileH);
//        this.mapaAlturaTotal = tileMap.size * tileH;
        this.mapaAlturaTotal = 1380;

        // Posición inicial del Actor basada en el offset del layer
//        setX(layer.getFloat("x")); // -75
//        setY(layer.getFloat("y")); // 0
        setPosition(0,0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Aplicamos el color del actor por si quieres efectos de transparencia
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        int rowIndex = 0;
        for (JsonValue row : tileMap) {
            int colIndex = 0;
            for (JsonValue tile : row) {
                int tileId = tile.asInt();
                if (tileId != -1) {
                    // Calculamos la posición relativa al Actor
                    float drawX = getX() + (colIndex * tileW);

                    // Fórmula corregida para LibGDX usando el viewportY del JSON
                    float drawY = getY() + ((tileMap.size-1-rowIndex) * tileH);
                    int tilesPerRow = tilesetRegions[0].length;
                    int tileCol = tileId % tilesPerRow;
                    int tileRow = tileId / tilesPerRow;

                    batch.draw(tilesetRegions[tileRow][tileCol], drawX, drawY, tileW, tileH);
                }
                colIndex++;
            }
            rowIndex++;
        }
        // Resetear color para no afectar a otros actores
        batch.setColor(Color.WHITE);
    }
}
