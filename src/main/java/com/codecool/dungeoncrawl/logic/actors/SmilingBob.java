package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.cell.Cell;
import com.codecool.dungeoncrawl.logic.cell.CellType;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.map.GameMap;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SmilingBob extends Monster {
    private GameMap map;
    public SmilingBob(Cell cell, int health, int attackStrength, GameMap map) {
        super(cell, 18, 4);
        this.map = map;
    }

    @Override
    public void move() {
        movementExecutor = Executors.newScheduledThreadPool(1);

        Runnable movementTask = () -> {
            if (getHealth() <= 0) {
                removeIfDead(cell);
                return;
            }
            Cell playerCell = findPlayerPosition();
            if (playerCell.getActor() instanceof Player){
                Direction playerDirection = calculatePlayerDirection(playerCell);
                if (playerDirection != Direction.NONE) {
                    Cell nextCell = cell.getNeighbor(playerDirection.x, playerDirection.y);
                    if (nextCell.getType() == CellType.FLOOR && !nextCell.isOccupied()) {
                        cell.setActor(null);
                        nextCell.setActor(this);
                        cell = nextCell;
                    }
                }
            }
        };

        movementExecutor.scheduleAtFixedRate(movementTask, 0, TIMER_STEP, TimeUnit.MILLISECONDS);
    }

    public Cell findPlayerPosition() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() instanceof Player) {
                    return cell;
                }              
            }
        }
        return null;
    }

    protected Direction calculatePlayerDirection(Cell playerCell) {
        int playerX = playerCell.getX();
        int playerY = playerCell.getY();
        int monsterX = cell.getX();
        int monsterY = cell.getY();
        int dx = playerX - monsterX;
        int dy = playerY - monsterY;
        return Direction.fromDelta(dx, dy);
    }

    @Override
    public String getTileName() {
        return "smilingBob";
    }
}
