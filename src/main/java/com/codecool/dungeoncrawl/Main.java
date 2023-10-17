package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Main extends Application {
    private Alert alert;
    private final List<Monster> monsters = new ArrayList<>();
    public static boolean key = false;
    private final String STEP_SOUND = "step.wav";
    private final String ELIXIR_SOUND = "elixir.wav";
    public static final String FIGHT_SOUND = "fight.wav";
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
    Label attackLabel = new Label("Attack:");
    Label playerAttackLabel = new Label();
    Button buttonPickUp = new Button("Pick Up");
    Label labelName = new Label("Name:");
    Button submit = new Button("Submit");


    public static void main(String[] args) {
        launch(args);
    }
  

    @Override
    public void start(Stage primaryStage) {
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
 
        ui.add(attackLabel, 0, 4);
        ui.add(playerAttackLabel, 1, 4);
        ui.add(inventoryLabel, 0, 6);
        ui.add(canvasInventory, 0, 7);
        GridPane.setMargin(buttonPickUp, new Insets(50, 0, 10, 0));
        ui.add(buttonPickUp, 0, 5);
        canvasInventory.setHeight(400);
      
        buttonPickUp.setFocusTraversable(false);
        buttonPickUp.setOnAction(actionEvent -> collectItems());

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

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() instanceof Monster) {
                    monsters.add((Monster) cell.getActor());
                }
            }
        }
      
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setOnCloseRequest(event -> stopMonsterMovementThreads());
      
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
        checkIsGameOver();
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
        healthLabel.setText("" +map.getPlayer().getHealth());
        playerAttackLabel.setText("" + map.getPlayer().getAttackStrength());
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

    private void addHealth(Cell cell) {
        int health = map.getPlayer().getHealth();
        health += cell.getItem().getVALUE();
        map.getPlayer().setHealth(health);
        healthLabel.setText("" + health);
    }

    public void collectItems() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);

                if (cell.getActor() != null && cell.getItem() != null) {
                    if (cell.getItem() instanceof Armour) {
                        playSound(SWORD_SOUND);
                        addHealth(cell);
                    }else if (cell.getItem() instanceof Elixir) {
                        playSound(ELIXIR_SOUND);
                        addHealth(cell);
                    } else if (cell.getItem() instanceof KeyClass) {
                        playSound(KEYS_SOUND);
                        addHealth(cell);
                    }
                    map.getPlayer().getInventory().addItem(cell.getItem());

                    if (cell.getItem() instanceof Sword) {
                        playSound(SWORD_SOUND);
                        int attackStrength = map.getPlayer().getAttackStrength();
                        attackStrength+= cell.getItem().getVALUE();
                        map.getPlayer().setAttackStrength(attackStrength);
                    }
                    cell.setItem(null);
                    refresh();
                }
            }
        }
    }

    public  static void playSound(String fileName) {
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

 
    public void checkIsGameOver() {
        int playerHealth = map.getPlayer().getHealth();
        if (playerHealth <= 0) {
            GameStateManager.setGameIsOver(true);
            stopMonsterMovementThreads();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Game Over!");
            alert.setContentText("What would you like to do?");

            ButtonType playAgainButton = new ButtonType("Play Again");
            ButtonType quitButton = new ButtonType("Quit");

            alert.getButtonTypes().setAll(playAgainButton, quitButton);

            alert.showAndWait().ifPresent(response -> {
                switch (response.getText()) {
                    case "Play Again":
                        resetGame();
                        break;
                    case "Quit":
                        Platform.exit();
                        break;
                }
            });
        }
    }

    private void resetGame() {
        GameStateManager.setGameIsOver(false);
        map = MapLoader.loadMap(key, "");
        Player player = map.getPlayer();
        player.getInventory().clearInventory();
        alert.close();
        contextInventory.clearRect(0, 0, canvasInventory.getWidth(), canvasInventory.getHeight());
        refresh();
    }

    private void stopMonsterMovementThreads() {
        for (Monster monster : monsters) {
            monster.stopMovementThread();
        }
    }
}
