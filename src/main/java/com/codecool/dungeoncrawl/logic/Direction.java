package com.codecool.dungeoncrawl.logic;

public enum Direction {
    NONE(0, 0), UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    public final int x;
    public final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> NONE;
        };
    }

    public static Direction fromDelta(int dx, int dy) {
        if (dx == 0 && dy == -1) {
            return UP;
        } else if (dx == 0 && dy == 1) {
            return DOWN;
        } else if (dx == -1 && dy == 0) {
            return LEFT;
        } else if (dx == 1 && dy == 0) {
            return RIGHT;
        } else {
            return NONE;
        }
    }
}
