package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class Monster extends Actor {
    private final List<MonsterEventListener> eventListeners = new ArrayList<>();
    static final int TIMER_STEP = 600;
    Direction randomDirection = getRandomDirection();
    ScheduledExecutorService movementExecutor;
    private Direction currentDirection = Direction.NONE;
    private int movesLeft = 0;
    private int maxMonsterMoves = 3;

    public Monster(Cell cell, int health, int attackStrength) {
        super(cell);
        this.setHealth(health);
        this.setAttackStrength(attackStrength);
    }

    public void move() {
        movementExecutor = Executors.newScheduledThreadPool(1);

        Runnable movementTask = () -> {
            if (getHealth() <= 0) {
                removeIfDead(cell);
                return;
            }

            do { randomDirection = getRandomDirection();
            } while (randomDirection == Direction.NONE);
            standardMonsterMovement();
        };
        movementExecutor.scheduleAtFixedRate(movementTask, 0, TIMER_STEP, TimeUnit.MILLISECONDS);
    }

    Direction getRandomDirection() {
        if (movesLeft <= 0) {
            Direction[] directions = Direction.getUpDownLeftRightDirections();
            if (directions.length > 0) {
                int randomIndex = (int) (Math.random() * directions.length);
                currentDirection = directions[randomIndex];
                movesLeft = maxMonsterMoves;
            } else {
                currentDirection = Direction.NONE;
                movesLeft = 0;
            }
        } else {
            movesLeft--;
        }
        return currentDirection;
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
        Direction newDirection = getRandomDirection();
        int dx = newDirection.x;
        int dy = newDirection.y;
        Cell nextCell = cell.getNeighbor(dx, dy);

        if (nextCell.getType() != CellType.FLOOR || nextCell.isOccupied() || nextCell.hasItem()) {

            while (nextCell.getType() != CellType.FLOOR || nextCell.isOccupied() || nextCell.hasItem()) {
                newDirection = getRandomDirection();
                dx = newDirection.x;
                dy = newDirection.y;
                nextCell = cell.getNeighbor(dx, dy);
            }
            randomDirection = newDirection;
        }
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

}
