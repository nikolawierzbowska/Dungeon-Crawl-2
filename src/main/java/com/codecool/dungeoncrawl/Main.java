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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Main extends Application {
    private Alert alert;
    private final List<Monster> monsters = new ArrayList<>();
    public static boolean keyFlag = false;
    private final String STEP_SOUND = "step.wav";
    private final String ELIXIR_SOUND = "elixir.wav";
    public static final String FIGHT_SOUND = "fight.wav";
    private final String SWORD_SOUND = "sword.wav";
    private final String KEYS_SOUND = "keys.wav";
    public static final String CHEAT_SOUND = "/cheatOn.wav";
    GameMap map;
    Canvas canvas;
    Player player;
    Canvas canvasInventory = new Canvas(
            4 * Tiles.TILE_WIDTH,
            5 * Tiles.TILE_WIDTH);

    GraphicsContext context;
    GraphicsContext contextInventory = canvasInventory.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();
    Label attackLabel = new Label("Attack:");
    Label playerAttackLabel = new Label();
    Button buttonPickUp = new Button("Pick Up");
    Label labelName = new Label("Name:");
    TextField name = new TextField();
    Button buttonSubmit = new Button("Submit");
    Button buttonExit = new Button("EXIT");
    Button buttonPlayAgain = new Button("Play again");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initialisation();
        GridPane ui = new GridPane();
        addElementsOnStage(ui);

        canvasInventory.setHeight(400);
        addEventsForButtonsAndLabels();

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

    public void initialisation () {
        player = new Player();
        map = MapLoader.loadMap(keyFlag, "", player);
        canvas = new Canvas(
                map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();
    }


    public void addElementsOnStage(GridPane ui){
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10, 15, 10, 15));
        name.setPromptText("Enter player's name.");
        ui.add(labelName, 0, 0);
        ui.add(name, 0, 1);
        GridPane.setMargin(buttonSubmit, new Insets(10, 0, 30, 0));
        ui.add(buttonSubmit, 0, 2);
        ui.add(new Label("Health:"), 0, 3);
        ui.add(healthLabel, 1, 3);
        ui.add(attackLabel, 0, 4);
        ui.add(playerAttackLabel, 1, 4);
        ui.add(inventoryLabel, 0, 6);
        ui.add(canvasInventory, 0, 7);
        GridPane.setMargin(buttonPickUp, new Insets(50, 0, 10, 0));
        ui.add(buttonPickUp, 0, 5);
        ui.add(buttonExit, 0, 20);
        ui.add(buttonPlayAgain, 0, 21);

    }

    public void addEventsForButtonsAndLabels(){
        buttonPickUp.setFocusTraversable(false);
        buttonExit.setFocusTraversable(false);
        buttonPlayAgain.setFocusTraversable(false);
        buttonPickUp.setOnAction(actionEvent -> collectItems());

        buttonExit.setOnAction(actionEvent -> Platform.exit());
        buttonPlayAgain.setOnAction(actionEvent -> resetGame());

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            map.getPlayer().setName(newValue);
        });

        if (name.getText().isEmpty()) {
            name.setFocusTraversable(false);
            buttonSubmit.setFocusTraversable(false);
        }
        name.setOnKeyPressed(actionEvent -> {
            buttonSubmit.setFocusTraversable(true);
            buttonSubmit.setDisable(false);
        });
        buttonSubmit.setOnAction(actionEvent -> {
            if (!name.getText().isEmpty())
                buttonSubmit.setDisable(true);
        });


    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                player.move(0, -1);
                refresh();
                break;
            case DOWN:
                player.move(0, 1);
                refresh();
                break;
            case LEFT:
                player.move(-1, 0);
                refresh();
                break;
            case RIGHT:
                player.move(1, 0);
                refresh();
                break;
        }
        playSound(STEP_SOUND);
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
        healthLabel.setText(String.valueOf(player.getHealth()));
        playerAttackLabel.setText(String.valueOf(player.getAttackStrength()));
        inventoryLabel.setText("Inventory: ");
        displayInventory();
        checkIsGameOver();
        changeMap();

    }


    public void displayInventory() {
        int x = 0;
        for (Item item : player.getInventory().getItems()) {
            int y = player.getInventory().getItems().indexOf(item);
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

    private boolean hasKey() {
        for (Item item : player.getInventory().getItems()) {
            if (item instanceof KeyClass) {
                return true;
            }
        }
        return false;
    }

    public void deleteKey() {
        List<Item> items = player.getInventory().getItems();
        Iterator<Item> iterator = items.iterator();

        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item instanceof KeyClass) {
                iterator.remove();
                contextInventory.clearRect(0, 0, canvasInventory.getWidth(), canvasInventory.getHeight());
                displayInventory();
            }
        }
    }


    private void addHealth(Cell cell) {
        int health = player.getHealth();
        health += cell.getItem().getVALUE();
        player.setHealth(health);
        healthLabel.setText(String.valueOf(health));
    }

    public void collectItems() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);

                if (cell.getActor() != null && cell.getItem() != null) {
                    if (cell.getItem() instanceof Armour) {
                        playSound(SWORD_SOUND);
                        addHealth(cell);
                    } else if (cell.getItem() instanceof Elixir) {
                        playSound(ELIXIR_SOUND);
                        addHealth(cell);
                    } else if (cell.getItem() instanceof KeyClass) {
                        playSound(KEYS_SOUND);
                        addHealth(cell);
                    }
                    player.getInventory().addItem(cell.getItem());

                    if (cell.getItem() instanceof Sword) {
                        playSound(SWORD_SOUND);
                        int attackStrength = player.getAttackStrength();
                        attackStrength += cell.getItem().getVALUE();
                        player.setAttackStrength(attackStrength);
                    }
                    cell.setItem(null);
                    refresh();
                }
            }
        }
    }

    public static Clip playSound(String fileName) {
        try {
            File wavFile = new File("src/main/resources/" + fileName);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(wavFile));
            clip.start();
            return clip; //return the clip to control it
        } catch (Exception e) {
            System.out.println(e);
            return null; //in case of an error return null
        }
    }

    public void changeMap() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null && cell.getType().equals(CellType.DOOR) && hasKey()) {
                    keyFlag = !keyFlag;
                    map = MapLoader.loadMap(keyFlag, "Forest", player);
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    refresh();
                    deleteKey();
                    return;
                } else if (cell.getActor() != null && cell.getType().equals(CellType.STAIRS)) {
                    map = MapLoader.loadMap(keyFlag, "Basement", player);
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    refresh();
                    return;
                }
            }
        }
    }


    public void checkIsGameOver() {
        int playerHealth = player.getHealth();
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
                    case "Play Again" -> resetGame();
                    case "Quit" -> Platform.exit();
                }
            });
        }
    }

    private void resetGame() {
        GameStateManager.setGameIsOver(false);
        map = MapLoader.loadMap(keyFlag, "", player);
        player.getInventory().clearInventory();
        player.setHealth(player.setValueOfHealth());
        player.setAttackStrength(player.setValueOfAttack());
        name.clear();
        if (alert == null) {
            refresh();
        } else {
            alert.close();
        }
        contextInventory.clearRect(0, 0, canvasInventory.getWidth(), canvasInventory.getHeight());
        refresh();
    }

    private void stopMonsterMovementThreads() {
        for (Monster monster : monsters) {
            monster.stopMovementThread();
        }
    }
}
