package com.codecool.dungeoncrawl;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddElementsOnScene {
    ButtonsAndLabels buttonsAndLabels = new ButtonsAndLabels();

    public void addElementsOnScene(GridPane ui, Label healthLabel, Label playerAttackLabel, Label inventoryLabel, TextField name,
                                   Button buttonSubmit, Button buttonPickUp, Button buttonPlayAgain) {
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10, 15, 10, 15));
        name.setPromptText("Enter developer's name.");
        ui.add(buttonsAndLabels.labelName, 0, 0);
        ui.add(name, 0, 1);
        GridPane.setMargin(buttonSubmit, new Insets(10, 0, 30, 0));
        ui.add(buttonSubmit, 0, 2);
        ui.add(new Label("Health:"), 0, 3);
        ui.add(healthLabel, 1, 3);
        ui.add(buttonsAndLabels.attackLabel, 0, 4);
        ui.add(playerAttackLabel, 1, 4);
        ui.add(inventoryLabel, 0, 6);
        GridPane.setMargin(buttonPickUp, new Insets(50, 0, 10, 0));
        ui.add(buttonPickUp, 0, 5);
        ui.add(buttonsAndLabels.createExitButton(), 0, 20);
        ui.add(buttonPlayAgain, 0, 21);
    }
}
