package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Elixir extends Item {

    public Elixir(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "elixir";
    }
}


