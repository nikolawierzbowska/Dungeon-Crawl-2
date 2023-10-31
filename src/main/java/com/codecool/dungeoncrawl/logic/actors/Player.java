package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.handler.SoundHandler;
import com.codecool.dungeoncrawl.logic.cell.Cell;
import com.codecool.dungeoncrawl.logic.Inventory;
import lombok.Getter;

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
           SoundHandler.playSound(SoundHandler.FIGHT_SOUND);
            monster.damageReceived(damage);
            int monsterDamage = monster.getAttackStrength();
            this.damageReceived(monsterDamage);
            monster.removeIfDead(this.getCell());
        } else {
            Player player = this;
            String playerName = player.getName();
            if (DeveloperName.isDeveloperName(playerName)) {
                // cheat mode is on and play sound
                SoundHandler.playSound(SoundHandler.CHEAT_SOUND);
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