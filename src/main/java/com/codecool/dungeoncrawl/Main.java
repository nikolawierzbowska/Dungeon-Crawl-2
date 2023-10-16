package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Objects;


public class Main extends Application {
    public static boolean key = false;
    private final String STEP_SOUND = "step.wav";
    private final String ELIXIR_SOUND = "elixir.wav";
    private final String FIGHT_SOUND = "fight.wav";
    private final String SWORD_SOUND = "sword.wav";
    private final String KEYS_SOUND = "keys.wav";
    GameMap map = MapLoader.loadMap(key, "");
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);

    Canvas canvasInventory = new Canvas(
            4 * Tiles.TILE_WIDTH,
            5 * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    GraphicsContext contextInventory = canvasInventory.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();
    Button buttonPickUp = new Button("Pick Up");

    Label labelName = new Label("Name:");
    Button submit = new Button("Submit");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10, 15, 10, 15));

        TextField name = new TextField();
        name.setPromptText("Enter player's name.");
        ui.add(labelName, 0, 0);
        ui.add(name, 0, 1);
        GridPane.setMargin(submit, new Insets(10, 0, 30, 0));
        ui.add(submit, 0, 2);

        ui.add(new Label("Health:"), 0, 3);
        ui.add(healthLabel, 1, 3);

        GridPane.setMargin(buttonPickUp, new Insets(50, 0, 10, 0));
        ui.add(buttonPickUp, 0, 4);
        ui.add(inventoryLabel, 0, 5);
        canvasInventory.setHeight(400);
        ui.add(canvasInventory, 0, 6);


        buttonPickUp.setFocusTraversable(false);
        buttonPickUp.setOnAction(actionEvent -> {
            collectItems();
        });

        if (name.getText().isEmpty()) {
            name.setFocusTraversable(false);
            submit.setFocusTraversable(false);
        }
        name.setOnKeyPressed(actionEvent -> {
            submit.setFocusTraversable(true);
            submit.setDisable(false);
        });
        submit.setOnAction(actionEvent -> {
            if (!name.getText().isEmpty())
                submit.setDisable(true);
        });


        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        borderPane.setStyle("-fx-border-color: black");

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1, 0);
                refresh();
                break;
        }
        playSound(STEP_SOUND);
        changeMap();
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        inventoryLabel.setText("Inventory: ");
        int x = 0;
        for (Item item : map.getPlayer().getInventory().getItems()) {
            int y = map.getPlayer().getInventory().getItems().indexOf(item);
            if (item instanceof Sword) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
            if (item instanceof KeyClass) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
            if (item instanceof Armour) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
            if (item instanceof Elixir) {
                Tiles.drawItemIcon(contextInventory, item, x, y);
            }
        }
    }


    public void collectItems() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);

                if (cell.getActor() != null && cell.getItem() != null) {
                    if (cell.getItem() instanceof Sword || cell.getItem() instanceof Armour) {
                        playSound(SWORD_SOUND);
                    } else if (cell.getItem() instanceof Elixir) {
                        playSound(ELIXIR_SOUND);
                    } else if (cell.getItem() instanceof KeyClass) {
                        playSound(KEYS_SOUND);
                    }
                    int health = map.getPlayer().getHealth();
                    map.getPlayer().getInventory().addItem(cell.getItem());

                    health += cell.getItem().getVALUE();
                    map.getPlayer().setHealth(health);
                    healthLabel.setText("" + health);
                    cell.setItem(null);
                    refresh();
                }
            }
        }
    }

    private void playSound(String fileName) {
        try {
            File wavFile = new File("src/main/resources/" + fileName);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void changeMap() {
        int healthPoint = map.getPlayer().getHealth();
        Inventory inventoryList = map.getPlayer().getInventory();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null && cell.getType().equals(CellType.DOOR)) {
                    key = !key;
                    map = MapLoader.loadMap(key, "Forest");
                    map.getPlayer().setHealth(healthPoint);
                    map.getPlayer().setInventory(inventoryList);
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    refresh();
                    return;
                } else if (cell.getActor() != null && cell.getType().equals(CellType.STAIRS)) {
                    map = MapLoader.loadMap(key, "Basement");
                    map.getPlayer().setHealth(healthPoint);
                    map.getPlayer().setInventory(inventoryList);
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    refresh();
                    return;
                }
            }
        }
    }
}
