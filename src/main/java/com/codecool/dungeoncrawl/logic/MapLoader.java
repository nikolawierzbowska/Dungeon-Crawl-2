package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Elemental;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(boolean key, String mapName) {
        InputStream is = null;
        if (!key) {
            is = MapLoader.class.getResourceAsStream("/map" + mapName + ".txt");

        }else if(key){
            is = MapLoader.class.getResourceAsStream("/map" + mapName + ".txt");

        }
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        scanner.nextLine();

        return drawGameMap(scanner, width, height);
    }

    private static GameMap drawGameMap(Scanner scanner, int width, int height) {
        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '1':
                            cell.setType(CellType.TREE1);
                            break;
                        case '2':
                            cell.setType(CellType.TREE2);
                            break;
                        case '3':
                            cell.setType(CellType.TREE3);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case '%':
                            cell.setType(CellType.STAIRS);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            new Ghost(cell);
                            break;
                        case 'z':
                            cell.setType(CellType.FLOOR);
                            new Elemental(cell);
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
        return map;
    }

}
