package com.codecool.dungeoncrawl;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ButtonsAndLabels {
    Label attackLabel = new Label("Attack:");
    Label labelName = new Label("Name:");

    public Button playersList() {
        Button button = new Button("PLAYERS LIST");
        button.setFocusTraversable(false);
        button.setOnAction(actionEvent -> {
            Platform.exit();
        });
        return button;
    }

    public Button createExitButton() {
        Button button = new Button("EXIT");
        button.setFocusTraversable(false);
        button.setOnAction(actionEvent -> {
            Platform.exit();
        });
        return button;
    }
}

