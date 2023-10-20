package com.codecool.dungeoncrawl.logic.items;


import com.codecool.dungeoncrawl.logic.Cell;

public class KeyClass extends Item {
    private final String TILE_NAME = "key";

    public KeyClass(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return TILE_NAME;
    }
}
