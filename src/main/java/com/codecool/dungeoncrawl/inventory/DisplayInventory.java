package com.codecool.dungeoncrawl.inventory;

import com.codecool.dungeoncrawl.Tiles;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.scene.canvas.GraphicsContext;

public class DisplayInventory {

    public void displayInventory(Player player, GraphicsContext contextInventory) {
        int x = 0;
        for (Item item : player.getInventory().getItems()) {
            int y = player.getInventory().getItems().indexOf(item);
            if (item instanceof Sword) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
            if (item instanceof KeyClass) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
            if (item instanceof Armour) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
            if (item instanceof Elixir) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
        }

    }
}
