package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.dao.PlayerDaoJdbc;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        Main.main(args);
        GameDatabaseManager gameDatabaseManager = new GameDatabaseManager();
        PlayerDaoJdbc playerDaoJdbc = new PlayerDaoJdbc(gameDatabaseManager.connect());
    }
}
