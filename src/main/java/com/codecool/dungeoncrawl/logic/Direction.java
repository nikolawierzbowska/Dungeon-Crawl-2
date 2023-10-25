package com.codecool.dungeoncrawl.logic;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {
    NONE(0, 0), UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0),
    UP_LEFT(-1, 1 ), UP_RIGHT(1, 1), DOWN_LEFT(-1, -1), DOWN_RIGHT(1,-1);

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

    public static Direction[] getUpDownLeftRightDirections() {
        return Stream.of(UP, DOWN, LEFT, RIGHT)
                .toArray(Direction[]::new);
    }

    public static Direction fromDelta(int dx, int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return DOWN_LEFT;
            } else if (dy > 0) {
                return UP_LEFT;
            } else {
                return LEFT;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return DOWN_RIGHT;
            } else if (dy > 0) {
                return UP_RIGHT;
            } else {
                return RIGHT;
            }
        } else {
            if (dy < 0) {
                return DOWN;
            } else if (dy > 0) {
                return UP;
            }
        }
        return UP;
    }

}
