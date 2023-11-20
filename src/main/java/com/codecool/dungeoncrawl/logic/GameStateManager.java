package com.codecool.dungeoncrawl.logic;

public class GameStateManager {
    private static volatile boolean gameIsOver = false;

    public static void setGameIsOver(boolean gameIsOver) {
        GameStateManager.gameIsOver = gameIsOver;
    }
}
