package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;
import lombok.Getter;

import static com.codecool.dungeoncrawl.Main.FIGHT_SOUND;
import static com.codecool.dungeoncrawl.Main.CHEAT_SOUND;


@Getter
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
            monster.removeIfDead(this.getCell());
            System.out.println("zdrowie: " + monster.getHealth());
        } else {
            Player player = this;
            String playerName = player.getName();
            if (DeveloperName.isDeveloperName(playerName)) {
                // cheat mode is on and play sound
                Main.playSound(CHEAT_SOUND);
                // Allow walking through walls
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
        }

    }

    public String getTileName() {
        return "player";
    }


    public void setName(String name) {
        this.name = name;
    }
}