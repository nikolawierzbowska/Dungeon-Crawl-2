package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import lombok.Getter;

@Getter
public class CellDistancePair {
    private Cell cell;
    private int distance;

    public CellDistancePair(Cell cell, int distance) {
        this.cell = cell;
        this.distance = distance;
    }

}
