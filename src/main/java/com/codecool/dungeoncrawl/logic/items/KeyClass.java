package com.codecool.dungeoncrawl.logic.items;


import com.codecool.dungeoncrawl.logic.Cell;

public class KeyClass extends Item {
    public KeyClass(Cell cell) {
        super(cell);
    }


    @Override
    public String getTileName() {
        return "key";
    }
}


