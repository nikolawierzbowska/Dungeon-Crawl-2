package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.GameMap;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Ghost extends Monster {
    private boolean isVisible = true;
    private int invisibleTimer = 0;
    private static final int INVISIBLE_DURATION = 3000;
    private Direction previousDirection = Direction.NONE;

    public Ghost(Cell cell, int health, int attackStrength, GameMap map) {
        super(cell, 12, 3);
    }

    @Override
    public void startMovementThread() {
        movementExecutor = Executors.newScheduledThreadPool(1);

        Runnable movementTask = () -> {
            if (getHealth() <= 0) {
                removeIfDead(cell);
                return;
            }

            if (invisibleTimer > 0) {
                invisibleTimer -= TIMER_STEP;
                if (invisibleTimer <= 0) {
                    isVisible = true;
                    Direction oppositeDirection = previousDirection.opposite();
                    standardMonsterMovement();
                }
            }

            if (isVisible) {
                Direction randomDirection;
                do {
                    randomDirection = getRandomDirection();
                } while (randomDirection == Direction.NONE);

                int dx = randomDirection.x;
                int dy = randomDirection.y;
                Cell nextCell = cell.getNeighbor(dx, dy);

                if (nextCell.getType() == CellType.WALL) {
                    cell.setActor(null);
                    nextCell.setActor(this);
                    cell = nextCell;
                    invisibleTimer = INVISIBLE_DURATION;
                    isVisible = false;
                }

                if (nextCell.getType() == CellType.FLOOR && !nextCell.hasActor() && !nextCell.hasItem()) {
                    cell.setActor(null);
                    nextCell.setActor(this);
                    cell = nextCell;
                }
            }
        };
        movementExecutor.scheduleAtFixedRate(movementTask, 0, TIMER_STEP, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getTileName() {
        if (isVisible) {
            return "ghost";
        } else {
            return "ghost_wall";
        }
    }

}
