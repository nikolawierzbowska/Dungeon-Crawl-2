package com.codecool.dungeoncrawl.inventory;

import com.codecool.dungeoncrawl.handler.SoundHandler;
import com.codecool.dungeoncrawl.logic.cell.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Armour;
import com.codecool.dungeoncrawl.logic.items.Elixir;
import com.codecool.dungeoncrawl.logic.items.KeyClass;
import com.codecool.dungeoncrawl.logic.items.Sword;
import javafx.scene.control.Label;

public class CollectItems {

    SoundHandler soundHandler = new SoundHandler();
    Label healthLabel = new Label();
    public void collectItems(GameMap map, Player player, Cell cell) {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                cell = map.getCell(x, y);

                if (cell.getActor() != null && cell.getItem() != null) {
                    if (cell.getItem() instanceof Armour) {
                        SoundHandler.playSound(soundHandler.SWORD_SOUND);
                        addHealth(cell, player,healthLabel);
                    } else if (cell.getItem() instanceof Elixir) {
                        SoundHandler.playSound(soundHandler.ELIXIR_SOUND);
                        addHealth(cell,player,healthLabel);
                    } else if (cell.getItem() instanceof KeyClass) {
                        SoundHandler.playSound(soundHandler.KEYS_SOUND);
                        addHealth(cell,player,healthLabel);
                    }
                    player.getInventory().addItem(cell.getItem());

                    if (cell.getItem() instanceof Sword) {
                        SoundHandler.playSound(soundHandler.SWORD_SOUND);
                        int attackStrength = player.getAttackStrength();
                        attackStrength += cell.getItem().getVALUE();
                        player.setAttackStrength(attackStrength);
                    }
                    cell.setItem(null);

                }
            }
        }
    }

    private void addHealth(Cell cell, Player player, Label healthLabel) {
        int health = player.getHealth();
        health += cell.getItem().getVALUE();
        player.setHealth(health);
        healthLabel.setText(String.valueOf(health));
    }
}
