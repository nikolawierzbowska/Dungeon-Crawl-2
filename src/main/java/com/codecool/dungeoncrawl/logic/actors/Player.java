package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;

public class Player extends Actor {
    private Inventory inventory = new Inventory();

    public Player(Cell cell) {
        super(cell);
    }

    public Player() {
        super();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTileName() {
        return "player";
    }

}
