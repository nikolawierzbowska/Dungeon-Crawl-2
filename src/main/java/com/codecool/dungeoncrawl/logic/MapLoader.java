package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        List<Skeleton> skeletons = new ArrayList<>();
        List<Ghost> ghosts = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            Skeleton skeleton = new Skeleton(cell, 10, 2, map);
                            skeletons.add(skeleton);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            Ghost ghost = new Ghost(cell, 12, 3, map);
                            ghosts.add(ghost);
                            break;
                        case 'z':
                            cell.setType(CellType.FLOOR);
                            new Elemental(cell, 15, 5);
                            break;
                        case 'b':
                            cell.setType(CellType.FLOOR);
                            SmilingBob bob = new SmilingBob(cell, 18, 4);
                            bob.startMovementThread();
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            Player player = new Player(cell);
                            map.setPlayer(player);
                            break;
                        case 'a':
                            cell.setType(CellType.FLOOR);
                            new Armour(cell);
                            break;
                        case 'e':
                            cell.setType(CellType.FLOOR);
                            new Elixir(cell);
                            break;
                        case 'm':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new KeyClass(cell);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }

        }
        for (Skeleton skeleton : skeletons) {
            skeleton.startMovementThread();
        }
        for (Ghost ghost : ghosts) {
            ghost.startMovementThread();
        }
        return map;
    }
}
