package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Elemental extends Monster {

    public Elemental(Cell cell, int health, int attackStrength) {
        super(cell, 15, 5);
    }

    @Override
    public String getTileName() {
        return "elemental";
    }
}
