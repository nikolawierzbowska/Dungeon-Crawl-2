package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;
    public static int INVENTORY_TILE_SIZE = 15;

    private static Image tileset = new Image("/newTiles.png", 960, 1760, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();

    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH);
            y = j * (TILE_WIDTH);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(29, 44));
        tileMap.put("wall", new Tile(7, 45));
        tileMap.put("closed_gate", new Tile(20, 47));
        tileMap.put("floor", new Tile(7, 49));
        tileMap.put("stairs", new Tile(1, 46));
        tileMap.put("player", new Tile(28, 20));
        tileMap.put("skeleton", new Tile(27, 0));
        tileMap.put("elemental", new Tile(28, 9));
        tileMap.put("ghost", new Tile(29, 9));
        tileMap.put("ghost_wall", new Tile(15, 45));
        tileMap.put("smilingBob", new Tile(4, 11));
        tileMap.put("key", new Tile(12, 40));
        tileMap.put("sword", new Tile(7, 24));
        tileMap.put("armour", new Tile(1, 27));
        tileMap.put("elixir", new Tile(9, 42));
        tileMap.put("tree1", new Tile(23, 47));
        tileMap.put("tree2", new Tile(11, 15));
        tileMap.put("tree3", new Tile(9, 15));
        tileMap.put("door", new Tile(17, 45));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }

    public static void drawItemIcon(GraphicsContext contextInventory, Item item, int x, int y) {
        Tile tile = tileMap.get(item.getTileName());
        contextInventory.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, INVENTORY_TILE_SIZE, INVENTORY_TILE_SIZE);
    }
}