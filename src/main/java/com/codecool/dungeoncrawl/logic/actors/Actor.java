package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import static com.codecool.dungeoncrawl.Main.FIGHT_SOUND;

public abstract class Actor implements Drawable {
    protected Cell cell;
    private int health = 10;
    private int attackStrength = 5;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public Actor(){

    }

    public void move(int dx, int dy) {
    }

    public void damageReceived(int damage) {
        int remainingHealth = this.getHealth();
        remainingHealth -= damage;
        this.setHealth(remainingHealth);

//        if (remainingHealth <= 0) {
//            this.getCell().setActor(null);
//        }
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

    public void setCell(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
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
