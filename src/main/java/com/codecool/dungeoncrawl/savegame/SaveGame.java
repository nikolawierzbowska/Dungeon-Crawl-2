package com.codecool.dungeoncrawl.savegame;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Popup;

import java.util.List;
import java.util.Optional;


public class SaveGame {
    Label label = new Label("Name:");
    TextField textField = new TextField();
    Button buttonSave = new Button("Save");
    Button buttonCancel = new Button("Cancel");
    Player player;
    String currentMapName = "";
    GameDatabaseManager gameDatabaseManager;
    GameMap map;

    Popup popup = new Popup();

    public SaveGame(Player player, GameDatabaseManager gameDatabaseManager, GameMap map) {
        this.player = player;
        this.gameDatabaseManager = gameDatabaseManager;
        this.map = map;
    }

    public void saveGamePopup(Stage primaryStage1) {
        textField.setPromptText("Put the name");

        Region spacer = new Region();
        spacer.setPrefHeight(15);

        VBox content = new VBox(label, textField, spacer, buttonSave, buttonCancel);
        content.setSpacing(8);
        content.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-color: black;" +
                "-fx-border-width: 2px; -fx-border-style: solid;");

        popup.getContent().add(content);

        popup.setAutoHide(false);
        popup.show(primaryStage1);

        buttonCancel.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popup.hide();
            }
        });


        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                currentMapName = textField.getText();
                List playerNames = gameDatabaseManager.playerDao.getPlayerNames();
                if (playerNames.contains(currentMapName)) {
                    player.setName(currentMapName);
                    createDialogBox();
                } else {
                    player.setName(currentMapName);
                    PlayerModel saved = gameDatabaseManager.savePlayer(player);
                    gameDatabaseManager.saveGame(map, saved);
                    popup.hide();
                }
            }
        });
        primaryStage1.show();
    }

    public void createDialogBox() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Overwrite");
        dialog.setContentText("Would you like to overwrite the already existing state?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        dialog.getDialogPane().getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.isPresent() && clickedButton.get() == yesButton) {
            PlayerModel updated = gameDatabaseManager.updatePlayer(player);
            gameDatabaseManager.updateGame(map, updated);
            popup.hide();
        } else if (clickedButton.isPresent() && clickedButton.get() == noButton) {
            dialog.close();
            popup.setAutoHide(true);

        }
    }
}



