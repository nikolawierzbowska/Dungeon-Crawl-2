package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.items.Sword;

public class Player extends Actor {
    private Inventory inventory = new Inventory();

    public Player(Cell cell) {
        super(cell);
        initializeInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTileName() {
        return "player";
    }

    private void initializeInventory() {
        Cell playerCell = this.getCell();
        Sword sword = new Sword(playerCell); // how to add the sword to initial player's inv.
        this.getInventory().addItem(sword);
    }
}
