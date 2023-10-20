package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Elemental;
import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.MonsterMovementEvent;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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


public class Main extends Application implements EventHandler<MonsterMovementEvent> {
    private Alert alert;
    private final List<Monster> monsters = new ArrayList<>();
    public static boolean keyFlag = false;
    private final String STEP_SOUND = "step.wav";
    private final String ELIXIR_SOUND = "elixir.wav";
    public static final String FIGHT_SOUND = "fight.wav";
    private final String SWORD_SOUND = "sword.wav";
    private final String KEYS_SOUND = "keys.wav";
    GameMap map;
    Player player;
    Canvas canvas;

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

//        TODO wywołać event listener do movementThread

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

//        for (Monster monster : monsters) {
//            if (!(monster instanceof Elemental))
//                monster.setMovementEventHandler(this);
//        }

        Iterator<Monster> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            if (!(monster instanceof Elemental)) {
                monster.setMovementEventHandler(this);
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

    public void init() {
        player = new Player();
        map = MapLoader.loadMap(keyFlag, "", player);
        canvas = new Canvas(
                map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();
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
        healthLabel.setText("" + player.getHealth());
        playerAttackLabel.setText("" + player.getAttackStrength());
        inventoryLabel.setText("Inventory: ");
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

    private void addHealth(Cell cell) {
        int health = player.getHealth();
        health += cell.getItem().getVALUE();
        player.setHealth(health);
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
//TODO debug playera
    public void changeMap() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null && cell.getType().equals(CellType.DOOR)) {
                    stopMonsterMovementThreads();
                    keyFlag = !keyFlag;
                    map = MapLoader.loadMap(keyFlag, "Forest", player);
                } else if (cell.getActor() != null && cell.getType().equals(CellType.STAIRS)) {
                    stopMonsterMovementThreads();
                    map = MapLoader.loadMap(keyFlag, "Basement", player);
                }
            }
        }
        refresh();
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
        map = MapLoader.loadMap(keyFlag, "", player);
        player.getInventory().clearInventory();
        alert.close();
        contextInventory.clearRect(0, 0, canvasInventory.getWidth(), canvasInventory.getHeight());
        refresh();
    }

    private void stopMonsterMovementThreads() {
        for (Monster monster : monsters) {
            if (!(monster instanceof Elemental))
                monster.stopMovementThread();
        }
    }

    @Override
    public void handle(MonsterMovementEvent event) {
        refresh();
    }
}
