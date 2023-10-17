package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;

public class Skeleton extends Monster {
    public Skeleton(Cell cell, int health, int attackStrength, GameMap map) {
        super(cell, 10, 2);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
