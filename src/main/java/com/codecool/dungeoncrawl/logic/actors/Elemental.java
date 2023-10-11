package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Elemental extends Actor {

    public Elemental(Cell cell) {
        super(cell);
    }
    @Override
    public String getTileName() {
        return "elemental";
    }
}
