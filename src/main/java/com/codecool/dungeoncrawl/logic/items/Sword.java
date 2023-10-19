package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Item {
    private final int VALUE = 15;
    private final String TILE_NAME = "sword";

    public Sword(Cell cell) {
        super(cell);
    }

    @Override
    public int getVALUE() {
        return VALUE;
    }

    @Override
    public String getTileName() {
        return TILE_NAME;
    }
}
