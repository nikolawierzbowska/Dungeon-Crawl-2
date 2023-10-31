package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GameDatabaseManager {
    public PlayerDao playerDao;
    private GameStateDao gameDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameDao = new GameStateDaoJdbc(dataSource);
    }

    public PlayerModel savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
        return model;
    }

    public PlayerModel updatePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        int playerId = playerDao.getPlayerId(player.getName());
        model.setId(playerId);
        playerDao.update(model);
        return model;
    }

    public void saveGame(GameMap currentMap, PlayerModel player) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date sqlDate = Date.valueOf(localDateTime.toLocalDate());
        GameState gameState = new GameState(MapLoader.MapToString(currentMap), sqlDate, player);
        gameDao.add(gameState);
    }

    public void updateGame(GameMap currentMap, PlayerModel player) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date sqlDate = Date.valueOf(localDateTime.toLocalDate());
        int playerId = playerDao.getPlayerId(player.getPlayerName());
        GameState gameState = new GameState(MapLoader.MapToString(currentMap), sqlDate, player);
        gameState.setId(playerId);
        gameDao.update(gameState);
    }
    public GameState loadGame(int id) {
        return gameDao.get(id);
    }

    public Player loadPlayer(int id) {
        PlayerModel currentPlayer = gameDao.get(id).getPlayer();
        Player loadedPlayer = new Player(null);
        return loadedPlayer;
    }


    public DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = "dungeon_DB";
        String user = "postgres";
        String password = "nikola";

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");
        return dataSource;
    }
}
