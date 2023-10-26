package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try(Connection connection = dataSource.getConnection()) {
            int playerId = player.getId();
            String UPDATE_PLAYER = "UPDATE player SET(player_name,hp, x,y)=(?,?,?,?) WHERE id=?";
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PLAYER);
            updateStatement.setString(1,player.getPlayerName());
            updateStatement.setInt(2, player.getHp());
            updateStatement.setInt(3,player.getX());
            updateStatement.setInt(4,player.getY());
            updateStatement.setInt(5,playerId);
            updateStatement.executeUpdate();
        }catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try(Connection connection = dataSource.getConnection()) {
            String GET_PLAYER_BY_ID = "SELECT * FROM player WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PLAYER_BY_ID);
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    String playerName  = resultSet.getString("player_name");
                    int playerX = resultSet.getInt("x");
                    int playerY = resultSet.getInt("y");
                    return new PlayerModel(playerName, playerX , playerY);
                }
            }
        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
        return null;
    }

    @Override
    public List<PlayerModel> getAll() {
        List<PlayerModel> playersList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            String GET_ALL_PLAYERS = "SELECT * FROM player";
            try( ResultSet resultSet = connection.createStatement().executeQuery(GET_ALL_PLAYERS)) {
                while (resultSet.next()){
                    String playerName  = resultSet.getString("player_name");
                    int playerX = resultSet.getInt("x");
                    int playerY = resultSet.getInt("y");
                    PlayerModel playerModel =new PlayerModel(playerName, playerX , playerY);
                    playersList.add(playerModel);
                }
            }
        }catch(SQLException exception) {
            throw new RuntimeException(exception);
        }
        return playersList;
    }
}
