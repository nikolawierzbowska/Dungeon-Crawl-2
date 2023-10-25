package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;
import com.codecool.dungeoncrawl.logic.GameMap;

import java.util.*;

public class DijkstraAlgorithm {
    public static Map<Cell, Integer> dijkstraShortestPath(GameMap map, Cell start) {

        Map<Cell, Integer> distance = new HashMap<>();
        PriorityQueue<CellDistancePair> pq = new PriorityQueue<>(Comparator.comparingInt(CellDistancePair::getDistance));

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                distance.put(cell, Integer.MAX_VALUE);
            }
        }
        distance.put(start, 0);

        pq.add(new CellDistancePair(start, 0));

        while (!pq.isEmpty()) {
            CellDistancePair current = pq.poll();
            Cell currentCell = current.getCell();


            for (Direction direction : Direction.getUpDownLeftRightDirections()) {
                int dx = direction.x;
                int dy = direction.y;
                Cell neighbor = map.getCell(currentCell.getX() + dx, currentCell.getY() + dy);

                int newDistance = distance.get(currentCell) + 1;

                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    pq.add(new CellDistancePair(neighbor, newDistance));
                }
            }
        }

        return distance;
    }
}
