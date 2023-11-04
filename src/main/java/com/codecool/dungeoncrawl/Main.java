package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.handler.ImageHandler;
import com.codecool.dungeoncrawl.handler.KeyHandler;
import com.codecool.dungeoncrawl.handler.SoundHandler;
import com.codecool.dungeoncrawl.inventory.CollectItems;
import com.codecool.dungeoncrawl.inventory.DisplayInventory;
import com.codecool.dungeoncrawl.loadgame.PlayersTable;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.cell.Cell;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.cell.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import com.codecool.dungeoncrawl.savegame.SaveGame;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class Main extends Application implements MonsterEventListener {
    KeyHandler keyHandler = new KeyHandler();
    SoundHandler soundHandler = new SoundHandler();
    DisplayInventory displayInventory = new DisplayInventory();
    ImageHandler imageHandler = new ImageHandler();
    ButtonsAndLabels buttonsAndLabels = new ButtonsAndLabels();
    CollectItems collectItems = new CollectItems();
    AddElementsOnScene addElementsOnScene = new AddElementsOnScene();
    GameDatabaseManager gameDatabaseManager = new GameDatabaseManager();
    private Alert alert;
    Stage primaryStage;
    public final List<Monster> monsters = new ArrayList<>();
    public static boolean keyFlag = false;
    public static boolean realGame = true;
    Cell cell;
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
    Label playerAttackLabel = new Label();
    TextField name = new TextField();
    Button buttonPickUp = new Button("Pick Up");
    Button buttonSubmit = new Button("Submit");
    Button buttonPlayAgain = new Button("Play again");
    Stage primaryStage1 =new Stage();
    PlayersTable playersTable = new PlayersTable();

    public Main() throws SQLException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, SQLException {
        this.primaryStage = primaryStage;
        createMenu(primaryStage);
        gameDatabaseManager.setup();
    }

    public void createMenu(Stage primaryStage) throws FileNotFoundException {
        GridPane ui1 = createUiForFirstBoarderPane();
        BorderPane borderPane1 = createFirstBoarderPane(ui1);
        Scene scene1 = new Scene(borderPane1);
        primaryStage.setScene(scene1);
        showPrimaryStage(primaryStage);
    }

    public void createGameStage(Stage primaryStage1) {
        this.primaryStage1 =primaryStage1;
        initializeGame();
        setupButtonAndLabelEvents();
        GridPane ui = createUI();
        setupCanvasInventory();

        BorderPane borderPane = createBorderPane(ui);
        extractMonstersFromMap();

        Scene scene2 = new Scene(borderPane);
        configureScene(primaryStage1, scene2);

        setupKeyPressEventHandler(scene2);
        setupCloseRequestHandler(primaryStage1);
        showPrimaryStage(primaryStage1);
    }
    public void createGameStageAfterLoadFromDb() throws SQLException {
        initialisation2();
    }


    private BorderPane createFirstBoarderPane(GridPane ui1) throws FileNotFoundException {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageHandler.setTheImage());
        borderPane.setBottom(ui1);
        borderPane.setStyle("-fx-background-color:black");
        return borderPane;
    }

    private GridPane createUiForFirstBoarderPane() {
        GridPane ui1 = new GridPane();
        addElementsOnFirstBoarder(ui1);
        return ui1;
    }

    public void addElementsOnFirstBoarder(GridPane ui1) {
        ui1.setPrefHeight(100);
        ui1.setVgap(20);
        ui1.setPadding(new Insets(10, 0, 20, 330));
        ui1.add(createPlayGameButton(primaryStage), 0, 0);
        ui1.add(playersList(), 0, 1);
        ui1.add(buttonsAndLabels.createExitButton(), 0, 2);
    }

    private void initializeGame() {
        initialisation();
    }

    private GridPane createUI() {
        GridPane ui = new GridPane();
        addElementsOnScene.addElementsOnScene(ui, healthLabel, playerAttackLabel, inventoryLabel, name, buttonSubmit, buttonPickUp, buttonPlayAgain);
        ui.add(canvasInventory, 0, 7);
        ui.add(createMenuButton(), 0, 16);
        return ui;
    }

    private void setupCanvasInventory() {
        canvasInventory.setHeight(400);
    }

    private void setupButtonAndLabelEvents() {
        addEventsForButtonsAndLabels(cell);
    }

    private BorderPane createBorderPane(GridPane ui) {
        BorderPane borderPane = new BorderPane();
        System.out.println("canvas " +canvas);
        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        borderPane.setStyle("-fx-border-color: black");
        return borderPane;
    }

    private void extractMonstersFromMap() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() instanceof Monster) {
                    monsters.add((Monster) cell.getActor());
                }
            }
        }
    }

    private void configureScene(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        refresh();
    }

    private void setupKeyPressEventHandler(Scene scene) {
        scene.setOnKeyPressed(this::onKeyPressed);
    }

    private void setupCloseRequestHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> stopMonsterMovementThreads());
    }

    private void showPrimaryStage(Stage primaryStage) {
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    public void initialisation() {
        player = new Player();
        map = MapLoader.loadMap(keyFlag, "", player);
        canvas = new Canvas(
                map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();
    }

    public void initialisation2() throws SQLException {
        realGame =false;

        CompletableFuture<String> currentMapFuture = playersTable.loadGameDB();
        currentMapFuture.thenAccept(currentMap -> {

            map = MapLoader.loadMapFromDB(currentMap, playersTable.getPlayer());
            canvas = new Canvas(map.getWidth() * Tiles.TILE_WIDTH, map.getHeight() * Tiles.TILE_WIDTH);
            context = canvas.getGraphicsContext2D();
            Platform.runLater(() -> {
                setupButtonAndLabelEvents();
                GridPane ui = createUI();
                setupCanvasInventory();
                BorderPane borderPane = createBorderPane(ui);

                Scene scene2 = new Scene(borderPane);
                primaryStage.close();
                configureScene(primaryStage1, scene2);
                setupKeyPressEventHandler(scene2);
                showPrimaryStage(primaryStage1);
            });
        });
    }


    private Button createPlayGameButton(Stage primaryStage) {
        Button button = new Button("PLAY GAME");
        button.setFocusTraversable(false);
        button.setOnAction(actionEvent -> {
            try {
                createGameStage(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }

    public Button playersList() {
        Button button = new Button("PLAYERS LIST");
        button.setFocusTraversable(false);
        button.setOnAction(actionEvent -> {
            try {
                playersTable.createTable();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                createGameStageAfterLoadFromDb();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }

    public Button createMenuButton() {
        Button button = new Button("MENU");
        button.setFocusTraversable(false);
        button.setOnAction(actionEvent -> {
            try {
                createMenu(primaryStage);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        });
        return button;
    }

    public void addEventsForButtonsAndLabels(Cell cell) {
        buttonPickUp.setFocusTraversable(false);
        buttonsAndLabels.createExitButton().setFocusTraversable(false);
        buttonPlayAgain.setFocusTraversable(false);
        name.setFocusTraversable(false);
        buttonSubmit.setFocusTraversable(false);
        buttonPickUp.setOnAction(actionEvent -> {
            collectItems.collectItems(map, player, cell);
            refresh();
        });
        buttonPlayAgain.setOnAction(actionEvent -> {
            resetGame();
            contextInventory.clearRect(0, 0, canvasInventory.getWidth(), canvasInventory.getHeight());
            refresh();
        });
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            map.getPlayer().setName(newValue);
        });
        manageNameAndSubmitField();
    }

    public void manageNameAndSubmitField() {
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
        if(!realGame){
            player = playersTable.getPlayer();
        }
        switch (keyEvent.getCode()) {
            case UP:
                player.move(0, -1);
                refresh();
                soundHandler.playSound(soundHandler.STEP_SOUND);
                break;
            case DOWN:
                player.move(0, 1);
                refresh();
                soundHandler.playSound(soundHandler.STEP_SOUND);
                break;
            case LEFT:
                player.move(-1, 0);
                soundHandler.playSound(soundHandler.STEP_SOUND);
                refresh();
                break;
            case RIGHT:
                player.move(1, 0);
                soundHandler.playSound(soundHandler.STEP_SOUND);
                refresh();
                break;
            case S:
                Player savePlayer = map.getPlayer();
                SaveGame saveGame = new SaveGame(savePlayer, gameDatabaseManager, map);
                saveGame.saveGamePopup(primaryStage1);
                break;
        }
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

        if(realGame) {
            healthLabel.setText(String.valueOf(player.getHealth()));
            playerAttackLabel.setText(String.valueOf(player.getAttackStrength()));
            inventoryLabel.setText("Inventory: ");
            displayInventory.displayInventory(player, contextInventory);
            checkIsGameOver();
            changeMap();
        }else{
            healthLabel.setText(String.valueOf(playersTable.getPlayer().getHealth()));
            playerAttackLabel.setText(String.valueOf(playersTable.getPlayer().getAttackStrength()));
            inventoryLabel.setText("Inventory: ");
            displayInventory.displayInventory(playersTable.getPlayer(), contextInventory);
        }
    }

    public void changeMap() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null && cell.getType().equals(CellType.DOOR) && keyHandler.hasKey(player)) {
                    keyFlag = !keyFlag;
                    map = MapLoader.loadMap(keyFlag, "Forest", player);
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    refresh();
                    keyHandler.deleteKey(player, contextInventory, canvasInventory);
                    displayInventory.displayInventory(player, contextInventory);
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Game Over!");
            alert.setContentText("What would you like to do?");

            ButtonType playAgainButton = new ButtonType("Play Again");
            ButtonType quitButton = new ButtonType("Quit");

            alert.getButtonTypes().setAll(playAgainButton, quitButton);

            alert.showAndWait().ifPresent(response -> {
                switch (response.getText()) {
                    case "Play Again":
                        stopMonsterMovementThreads();
                        resetGame();
                        break;
                    case "Quit":
                        stopMonsterMovementThreads();
                        Platform.exit();
                        break;
                }
            });
        }
    }

    public void resetGame() {
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
    }

    public void stopMonsterMovementThreads() {
        for (Monster monster : monsters) {
            if (!(monster instanceof Elemental))
                monster.stopMovementThread();
        }
    }

    @Override
    public void onMonsterMovement() {
    }
}

