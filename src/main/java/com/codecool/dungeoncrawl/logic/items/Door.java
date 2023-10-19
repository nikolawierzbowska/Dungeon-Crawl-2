package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Door extends Item {
    public final String TILE_NAME = "door";

    public Door(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return TILE_NAME;
    }
}
