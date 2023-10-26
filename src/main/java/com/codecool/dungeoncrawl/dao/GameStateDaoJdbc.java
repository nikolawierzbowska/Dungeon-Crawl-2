package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {

    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {

    }

    @Override
    public void update(GameState state) {

    }

    @Override
    public GameState get(int id) {
        try(Connection connection = dataSource.getConnection()) {
            String GET_MAP_STATE = "SELECT * FROM game_state WHERE id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAP_STATE);
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    String currentMap  = resultSet.getString("current_map");
                    Date savedAt = resultSet.getDate("saved_at");
                    PlayerDaoJdbc playerDaoJdbc = new PlayerDaoJdbc(dataSource);
                    PlayerModel playerId = playerDaoJdbc.get(resultSet.getInt("player_id"));
                    resultSet.getInt("player_id");
                    return new GameState(currentMap, savedAt , playerId);
                }
            }
        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
        return null;
    }

    @Override
    public List<GameState> getAll() {
        List<GameState> gameStatesList = new ArrayList<>();
        return null;
    }





}
