package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.cell.Cell;
import com.codecool.dungeoncrawl.logic.cell.CellType;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.*;

public class MapLoader {
    static Map<CellType, Character> cells = new HashMap<>();
    static Map<Class, Character> items = new HashMap<>();
    static Map<Class, Character> actors = new HashMap<>();

    public static GameMap loadMap(boolean key, String mapName, Player player) {
        InputStream is = null;
        if (!key) {
            is = MapLoader.class.getResourceAsStream("/map" + mapName + ".txt");

        } else if (key) {
            is = MapLoader.class.getResourceAsStream("/map" + mapName + ".txt");
        }
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        scanner.nextLine();

        return drawGameMap(scanner, width, height, player);
    }

    private static GameMap drawGameMap(Scanner scanner, int width, int height, Player player) {
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
                            cells.put(CellType.EMPTY, ' ');
                            break;
                        case '1':
                            cell.setType(CellType.TREE1);
                            cells.put(CellType.TREE1, '1');
                            break;
                        case '2':
                            cell.setType(CellType.TREE2);
                            cells.put(CellType.TREE2, '2');
                            break;
                        case '3':
                            cell.setType(CellType.TREE3);
                            cells.put(CellType.TREE3, '3');
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            cells.put(CellType.WALL, '#');
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            cells.put(CellType.FLOOR, '.');
                            break;
                        case 'G':
                            cell.setType(CellType.GATE);
                            cells.put(CellType.GATE, 'G');
                            break;
                        case '%':
                            cell.setType(CellType.STAIRS);
                            cells.put(CellType.STAIRS, '%');
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            Skeleton skeleton = new Skeleton(cell, 10, 2, map);
                            skeletons.add(skeleton);
                            actors.put(Skeleton.class, 's');
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            Ghost ghost = new Ghost(cell, 12, 3, map);
                            ghosts.add(ghost);
                            actors.put(Ghost.class, 'g');
                            break;
                        case 'z':
                            cell.setType(CellType.FLOOR);
                            new Elemental(cell, 15, 5);
                            actors.put(Elemental.class, 'z');
                            break;
                        case 'b':
                            cell.setType(CellType.FLOOR);
                            SmilingBob bob = new SmilingBob(cell, 18, 4, map);
                            bob.move();
                            actors.put(SmilingBob.class, 'b');
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            player.setCell(cell);
                            map.setPlayer(player);
                            actors.put(Player.class, '@');
                            break;
                        case 'a':
                            cell.setType(CellType.FLOOR);
                            new Armour(cell);
                            items.put(Armour.class, 'a');
                            break;
                        case 'e':
                            cell.setType(CellType.FLOOR);
                            new Elixir(cell);
                            items.put(Elixir.class, 'e');
                            break;
                        case 'm':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            items.put(Sword.class, 'm');
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new KeyClass(cell);
                            items.put(KeyClass.class, 'k');
                            break;
                        case 'D':
                            cell.setType(CellType.DOOR);
                            new Door(cell);
                            items.put(Door.class, 'D');
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }

        }
        for (Skeleton skeleton : skeletons) {
            skeleton.move();
        }
        for (Ghost ghost : ghosts) {
            ghost.move();
        }
        return map;
    }


    public static String MapToString(GameMap map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Cell cell = map.getCell(x, y);
                stringBuilder.append(cell.getType().toString());
                if (map.getCell(x, y).getItem() == null) {
                    if (map.getCell(x, y).getActor() == null) {
                        stringBuilder.append(cells.get(map.getCell(x, y).getType()));
                    } else {
                        stringBuilder.append(actors.get(map.getCell(x, y).getActor().getClass()));
                    }
                } else {
                    stringBuilder.append(items.get(map.getCell(x, y).getItem().getClass()));
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}