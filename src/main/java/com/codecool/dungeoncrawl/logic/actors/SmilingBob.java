package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.GameMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SmilingBob extends Monster {

    private GameMap map;
    private Map<Cell, Integer> distanceToPlayer;

    public SmilingBob(Cell cell, int health, int attackStrength, GameMap map) {
        super(cell, 18, 4);
        this.map = map;
        this.distanceToPlayer = new HashMap<>();
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
//            System.out.println("bob's health: " + getHealth());
                distanceToPlayer = DijkstraAlgorithm.dijkstraShortestPath(map, playerCell);
                Direction playerDirection = calculatePlayerDirection(playerCell);
                if (playerDirection != Direction.NONE) {
//                    System.out.println("kierunek player'a: " + playerDirection);
//                    System.out.println("to DX: " + playerDirection.x);
//                    System.out.println("to DY: " + playerDirection.y);
                    Cell nextCell = findNextCellWithLowestDistance(playerDirection);
                    System.out.println("następna kom: " + nextCell);
                    if (nextCell.getType() == CellType.FLOOR && !nextCell.isOccupied()) {
                        cell.setActor(null);
                        nextCell.setActor(this);
                        cell = nextCell;
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
//                    System.out.println("tu jestem: " + cell.getX());
//                    System.out.println("tu jestem: " + cell.getY());
                    return cell;
                }
//                if (cell.getActor() instanceof SmilingBob) {
////                    System.out.println("tu BOB: " + cell.getX());
////                    System.out.println("tu BOB: " + cell.getY());
//                    return cell;
//                }
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
//        System.out.println("różnicaX: " + dx);
//        System.out.println("różnicaY: " + dy);
//        System.out.println("pozycjaBobX: " + monsterX);
//        System.out.println("pozycjaBobY: " + monsterY);

        return Direction.fromDelta(dx, dy);

    }

    private Cell findNextCellWithLowestDistance(Direction playerDirection) {
        int dx = playerDirection.x;
        int dy = playerDirection.y;
        Cell currentCell = cell;
        Cell nextCell = cell.getNeighbor(dx, dy);
        int currentDistance = distanceToPlayer.get(currentCell);
        int nextCellDistance = distanceToPlayer.get(nextCell);

        return (nextCellDistance < currentDistance) ? nextCell : currentCell;
    }

    @Override
    public String getTileName() {
        return "smilingBob";
    }
}
