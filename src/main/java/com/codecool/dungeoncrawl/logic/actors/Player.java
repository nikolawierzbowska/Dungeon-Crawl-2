package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Player extends Actor {
    private String name;

    public Player(Cell cell) {
        super(cell);
    }

    public Player(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTileName() {
        return "player";
    }
}
