package com.codecool.dungeoncrawl.logic.cell;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    GATE("closed_gate"),
    DOOR("door"),
    STAIRS("stairs"),
    TREE1("tree1"),
    TREE2("tree2"),
    TREE3("tree3");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}