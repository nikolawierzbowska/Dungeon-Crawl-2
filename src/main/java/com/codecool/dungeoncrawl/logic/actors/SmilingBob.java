package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SmilingBob extends Monster {
    public SmilingBob(Cell cell, int health, int attackStrength) {
        super(cell, 18, 4);
    }

    @Override
    public void startMovementThread() {
        movementExecutor = Executors.newScheduledThreadPool(1);

        Runnable movementTask = () -> {
            if (getHealth() <= 0) {
                removeIfDead(cell);
                return;
            }

            if (this.cell.getActor() instanceof Player) {
                Player player = (Player) this.cell.getActor();
                Direction playerDirection = calculatePlayerDirection(player);
                if (playerDirection != Direction.NONE) {
                    randomDirection = playerDirection;
                    standardMonsterMovement();
                }
            }
        };

        movementExecutor.scheduleAtFixedRate(movementTask, 0, TIMER_STEP, TimeUnit.MILLISECONDS);
    }

    protected Direction calculatePlayerDirection(Player player) {
        int playerX = player.getCell().getX();
        int playerY = player.getCell().getY();
        int monsterX = cell.getX();
        int monsterY = cell.getY();
        int dx = playerX - monsterX;
        int dy = playerY - monsterY;

        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
            return Direction.fromDelta(dx, dy);
        }
        return Direction.NONE;
    }

    @Override
    public String getTileName() {
        return "smilingBob";
    }
}
