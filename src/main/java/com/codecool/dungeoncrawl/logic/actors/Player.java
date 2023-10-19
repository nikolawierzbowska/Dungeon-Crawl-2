package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;

public class Player extends Actor {
    private Inventory inventory = new Inventory();
    private String name;


    public Player(Cell cell) {
        super(cell);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getTileName() {
        return "player";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
