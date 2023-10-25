package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;

import static com.codecool.dungeoncrawl.Main.FIGHT_SOUND;

public class Player extends Actor {
    private Inventory inventory = new Inventory();
    private String name;


    public Player(Cell cell) {
        super(cell);
    }

    public Player() {
        super();
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (!nextCell.isOccupied()) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else if (nextCell.getActor() instanceof Monster) {
            Monster monster = (Monster) nextCell.getActor();
            int damage = this.getAttackStrength();
            Main.playSound(FIGHT_SOUND);
            monster.damageReceived(damage);
            int monsterDamage = monster.getAttackStrength();
            this.damageReceived(monsterDamage);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTileName() {
        return "player";
    }
    public String getName() {
        return name;
    }
}