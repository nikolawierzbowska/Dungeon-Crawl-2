package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.dao.PlayerDaoJdbc;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        Main.main(args);
        GameDatabaseManager gameDatabaseManager = new GameDatabaseManager();

        PlayerDaoJdbc playerDaoJdbc = new PlayerDaoJdbc(gameDatabaseManager.connect());
        System.out.println(playerDaoJdbc.get(1));
//        System.out.println(playerDaoJdbc.getAll());
//        PlayerModel playerModel = new PlayerModel("Jan",5,5);
//        playerModel.setHp(100);
//        playerModel.setId(1);
//        playerDaoJdbc.update(playerModel);
//        System.out.println(playerDaoJdbc.get(1));

    }


}
