package com.codecool.dungeoncrawl.loadgame;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.text.TableView;

public class PlayersTable {



    public void createTable(Stage stage) {
        TableView tableView;
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Player's Name");
        stage.setWidth(300);
        stage.setHeight(500);

        Label label = new Label("Chose the player's name");
        TableColumn<String, String>  firstNameCol = new TableColumn<>("First Name");


//        tableView.getColumns().add(firstNameCol);
//
//        VBox vbox = new VBox();
//        vbox.setSpacing(5);
//        vbox.getChildren().addAll(label, tableView);
//
//        ((Group) scene.getRoot()).getChildren().addAll(vbox);
//
//        stage.setScene(scene);
//        stage.show();
    }


}
