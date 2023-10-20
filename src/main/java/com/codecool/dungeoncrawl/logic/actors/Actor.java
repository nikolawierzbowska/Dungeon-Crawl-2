package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import lombok.Setter;

import javax.sound.sampled.Clip;

import static com.codecool.dungeoncrawl.Main.CHEAT_SOUND;
import static com.codecool.dungeoncrawl.Main.FIGHT_SOUND;


public abstract class Actor implements Drawable {
    @Setter
    protected Cell cell;
    private int health = 10;
    private int attackStrength = 5;
    private Clip cheatSound;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public Actor() {
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);

        // Check if the player is in cheat mode
        if (this instanceof Player) {
            Player player = (Player) this;
            String playerName = player.getName();
            if (DeveloperName.isDeveloperName(playerName)) {
                // If the next cell is a wall, play the cheat sound
                if (nextCell.getType() == CellType.WALL) {
                    if (cheatSound != null) cheatSound.stop();
                    cheatSound = Main.playSound(CHEAT_SOUND);
                } else {
                    if (cheatSound != null) {
                        cheatSound.stop();
                        cheatSound = null;
                    }
                }
                // Allow free movement when in cheat mode
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
                return;  // Exit early to skip the rest of the logic
            }
        }

        // General movement logic
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


    public void damageReceived(int damage) {
        int remainingHealth = this.getHealth();
        remainingHealth -= damage;
        this.setHealth(remainingHealth);

        if (remainingHealth <= 0) {
            this.getCell().setActor(null);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int setValueOfHealth() {
        return health = 10;
    }

    public int setValueOfAttack() {
        return attackStrength = 5;
    }


    public int getAttackStrength() {
        return attackStrength;
    }

    public void setAttackStrength(int attackStrength) {
        this.attackStrength = attackStrength;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }
}
