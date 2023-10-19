package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;
    public static int INVENTORY_TILE_SIZE = 15;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();

    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("stairs", new Tile(3, 6));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("elemental", new Tile(24, 8));
        tileMap.put("ghost", new Tile(27, 6));
        tileMap.put("ghost_wall", new Tile(11, 18));
        tileMap.put("smilingBob", new Tile(21, 26));
        tileMap.put("key", new Tile(17, 23));
        tileMap.put("sword", new Tile(0, 29));
        tileMap.put("armour", new Tile(4, 23));
        tileMap.put("elixir", new Tile(26, 23));
        tileMap.put("tree1", new Tile(0, 1));
        tileMap.put("tree2", new Tile(3, 2));
        tileMap.put("tree3", new Tile(4, 2));
        tileMap.put("door", new Tile(0,9 ));
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
