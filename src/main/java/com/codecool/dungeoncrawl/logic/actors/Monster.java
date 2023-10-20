package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.*;
import javafx.event.EventHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class Monster extends Actor{
    static final int TIMER_STEP = 500;
    Direction randomDirection = getRandomDirection();
    ScheduledExecutorService movementExecutor;
    private EventHandler<MonsterMovementEvent> movementEventHandler;

    public Monster(Cell cell, int health, int attackStrength) {
        super(cell);
        this.setHealth(health);
        this.setAttackStrength(attackStrength);
    }

    public void setMovementEventHandler(EventHandler<MonsterMovementEvent> handler) {
        this.movementEventHandler = handler;
    }

    public void startMovementThread() {
        //TODO animation timer - checkout https://gist.github.com/Kogs/5c943af590b2085b64c97442e0b41cda
        // ubić wątki z pierwszej planszy przy przechodzeniu do drugiej
        movementExecutor = Executors.newScheduledThreadPool(1);
//TODO wywołać
        Runnable movementTask = () -> {
            if (getHealth() <= 0) {
                removeIfDead(cell);
                return;
            }

            do { randomDirection = getRandomDirection();
            } while (randomDirection == Direction.NONE);
            standardMonsterMovement();
            notifyMovementEventListeners();
        };
        movementExecutor.scheduleAtFixedRate(movementTask, 0, TIMER_STEP, TimeUnit.MILLISECONDS);
    }

    void notifyMovementEventListeners() {
        if (movementEventHandler != null) {
            movementEventHandler.handle(new MonsterMovementEvent());
        }
    }

    Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[(int) (Math.random() * directions.length)];
    }

    void removeIfDead(Cell cell) {
        cell.removeMonster(this);
    }
//sprawdź czy movementExecutor nie jest null
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
