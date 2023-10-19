package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Monster extends Actor {
    static final int TIMER_STEP = 200;
    Direction randomDirection = getRandomDirection();
    ScheduledExecutorService movementExecutor;

    public Monster(Cell cell, int health, int attackStrength) {
        super(cell);
        this.setHealth(health);
        this.setAttackStrength(attackStrength);
    }

    public void startMovementThread() {
        movementExecutor = Executors.newScheduledThreadPool(1);

        Runnable movementTask = () -> {
            if (getHealth() <= 0) {
                removeIfDead(cell);
                return;
            }

            do {
                randomDirection = getRandomDirection();
            } while (randomDirection == Direction.NONE);
            standardMonsterMovement();
        };
        movementExecutor.scheduleAtFixedRate(movementTask, 0, TIMER_STEP, TimeUnit.MILLISECONDS);
    }

    Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[(int) (Math.random() * directions.length)];
    }

    void removeIfDead(Cell cell) {
        cell.removeMonster(this);
    }

    public void stopMovementThread() {
        if (movementExecutor != null) {
            movementExecutor.shutdownNow();
        }
    }

    protected void standardMonsterMovement() {
        int dx = randomDirection.x;
        int dy = randomDirection.y;
        Cell nextCell = cell.getNeighbor(dx, dy);

        if (nextCell.getType() == CellType.FLOOR && !nextCell.isOccupied() && !nextCell.hasItem()) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }
}
