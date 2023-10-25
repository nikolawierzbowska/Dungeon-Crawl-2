package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Armour extends Item {
    private final int VALUE = 20;
    private final String TILE_NAME = "armour";

    public Armour(Cell cell) {
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
