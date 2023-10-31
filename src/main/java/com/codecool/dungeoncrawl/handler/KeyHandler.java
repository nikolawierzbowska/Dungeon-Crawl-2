package com.codecool.dungeoncrawl.handler;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.KeyClass;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Iterator;
import java.util.List;

public class KeyHandler {

    public boolean hasKey(Player player) {
        for (Item item : player.getInventory().getItems()) {
            if (item instanceof KeyClass) {
                return true;
            }
        }
        return false;
    }

    public void deleteKey(Player player,GraphicsContext contextInventory,Canvas canvasInventory ) {
        List<Item> items = player.getInventory().getItems();
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item instanceof KeyClass) {
                iterator.remove();
                contextInventory.clearRect(0, 0, canvasInventory.getWidth(), canvasInventory.getHeight());
            }
        }
    }
}
