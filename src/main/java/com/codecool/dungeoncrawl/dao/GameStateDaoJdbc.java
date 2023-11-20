package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
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
        try(Connection connection = dataSource.getConnection()) {
            String ADD_GAME_STATE = "INSERT INTO game_state (current_map, saved_at,player_id) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_GAME_STATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,state.getCurrentMap());
            preparedStatement.setDate(2,state.getSavedAt());
            preparedStatement.setInt(3, state.getPlayer().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        }catch(SQLException exception) {
            throw  new RuntimeException(exception);
        }
    }

    @Override
    public void update(GameState state) {
        try(Connection connection = dataSource.getConnection()) {
            String UPDATE_GAME_STATE = "UPDATE game_state SET current_map=?, saved_at=? WHERE player_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GAME_STATE);
            preparedStatement.setString(1,state.getCurrentMap());
            preparedStatement.setDate(2,state.getSavedAt());
            preparedStatement.setInt(3, state.getPlayer().getId());
            preparedStatement.executeUpdate();
        }catch(SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public GameState get(int id) {
        try(Connection connection = dataSource.getConnection()) {
            String GET_MAP_STATE = "SELECT current_map, saved_at, player_id FROM game_state WHERE id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MAP_STATE);
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    String currentMap  = resultSet.getString("current_map");
                    Date savedAt = resultSet.getDate("saved_at");
                    PlayerDaoJdbc playerDaoJdbc = new PlayerDaoJdbc(dataSource);
                    PlayerModel playerId = playerDaoJdbc.get(resultSet.getInt("player_id"));
                    return new GameState(currentMap, savedAt , playerId);
                }
            }
        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
        return null;
    }

    public int getGameIdByPlayerId(int playerId) {
        try(Connection connection = dataSource.getConnection()) {
            String GET_GAME_ID_BY_PLAYER_ID = "SELECT id FROM game_state WHERE player_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(GET_GAME_ID_BY_PLAYER_ID);
            preparedStatement.setInt(1, playerId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    int id = resultSet.getInt("id");
                    return id;
                }
            }
        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
        return -1;
    }
    @Override
    public List<GameState> getAll() {
        List<GameState> gameStatesList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            String GET_ALL_GAME_STATE = "SELECT * FROM game_state";
            try(ResultSet resultSet = connection.createStatement().executeQuery(GET_ALL_GAME_STATE)) {
                while (resultSet.next()) {
                    String currentMap  = resultSet.getString("current_map");
                    Date savedAt = resultSet.getDate("saved_at");
                    PlayerDaoJdbc playerDaoJdbc = new PlayerDaoJdbc(dataSource);
                    PlayerModel playerId = playerDaoJdbc.get(resultSet.getInt("player_id"));
                    GameState gameState = new GameState(currentMap, savedAt,playerId);
                    gameStatesList.add(gameState);
                }
            }
        }catch (SQLException exception){
            throw  new RuntimeException(exception);
        }
        return gameStatesList;
    }
}
