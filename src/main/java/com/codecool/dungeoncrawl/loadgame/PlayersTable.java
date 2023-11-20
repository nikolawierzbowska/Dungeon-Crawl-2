package com.codecool.dungeoncrawl.loadgame;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.dao.GameStateDaoJdbc;
import com.codecool.dungeoncrawl.dao.PlayerDaoJdbc;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class PlayersTable {
    GameDatabaseManager gameDatabaseManager = new GameDatabaseManager();
    PlayerDaoJdbc playerDaoJdbc = new PlayerDaoJdbc(gameDatabaseManager.connect());
    GameStateDaoJdbc gameStateDaoJdbc = new GameStateDaoJdbc(gameDatabaseManager.connect());
    TableView<Map> tableView = new TableView<>();
    Player player;

    public PlayersTable() throws SQLException {
    }

    public Player getPlayer() {
        return player;
    }

    public void createTable() throws SQLException {
        tableView.getColumns().clear();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("List of players");
        TableColumn<Map, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new MapValueFactory<>("Name"));

        tableView.getColumns().add(column1);
        tableView.setItems(generateDataInMap());

        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<Map> generateDataInMap(){
        List<String> playersName = playerDaoJdbc.getPlayerNames();
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (String playerName : playersName) {
            Map<String, String> dataRow = new HashMap<>();
            dataRow.put("Name", playerName);
            allData.add(dataRow);
        }
        return allData;
    }

    public CompletableFuture<String> loadGameDB() throws SQLException {
        CompletableFuture<String> currentMapFuture = new CompletableFuture<>();
        gameDatabaseManager.setup();
        tableView.setOnMouseClicked(event -> {
            if (!tableView.getSelectionModel().isEmpty()){
                Map<String, String> selectedRow = (Map<String, String>) tableView.getSelectionModel().getSelectedItem();
                String name = selectedRow.get("Name");
                int playerId = playerDaoJdbc.getPlayerId(name);
                int gameId = gameStateDaoJdbc.getGameIdByPlayerId(playerId);
                player = gameDatabaseManager.loadPlayer(gameId);
                String currentMap = gameDatabaseManager.loadGame(gameId).getCurrentMap();
                currentMapFuture.complete(currentMap);
            }
        });
        return currentMapFuture;
    }
}



