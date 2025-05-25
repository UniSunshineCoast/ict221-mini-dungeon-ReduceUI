package dungeon.gui;

import dungeon.engine.gameEngine;
import dungeon.engine.map;
import dungeon.engine.cell;
import dungeon.engine.Enemy;
import dungeon.engine.Item;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Controller {
    @FXML private Button upBn;
    @FXML public Button leftBn;
    @FXML public Button rightBn;
    @FXML public Button downBn;
    @FXML public TextArea textArea;
    @FXML public Button startBn;
    @FXML public Button saveBn;
    @FXML public Button loadBn;
    @FXML public Button quitBn;
    @FXML public Label difficultyLabel;
    @FXML public Slider difficultySlider;
    @FXML private GridPane gridPane;
    private map map;                                        //bin?

    private gameEngine engine;
    private static final double CELL_SIZE = 40;

    @FXML
    public void initialize() {
        //Difficulty Slider first because default difficulty
        difficultySlider.setMin(0);
        difficultySlider.setMax(10);
        difficultySlider.setValue(3);
        difficultySlider.setShowTickLabels(true);
        difficultySlider.setShowTickMarks(true);
        difficultySlider.setSnapToTicks(true);
        difficultyLabel.setText(String.valueOf(difficultySlider.getValue()));

        engine = new gameEngine(false, (int) difficultySlider.getValue());

        //Buttons
        upBn.setOnAction(event -> movePlayer(0, -1, "up"));
        leftBn.setOnAction(event -> movePlayer(-1, 0, "left"));
        rightBn.setOnAction(event -> movePlayer(1, 0, "right"));
        downBn.setOnAction(event -> movePlayer(0, 1, "down"));
        startBn.setOnAction(event -> startGame());
        saveBn.setOnAction(event -> saveGame());
        loadBn.setOnAction(event -> loadGame());
        quitBn.setOnAction(event -> quitGame());

        updateGui();
    }

    private void movePlayer(int dx, int dy, String direction) {
        if (engine.getGameOver()){
            textArea.setText("Start a new game.");
            return;
        }

        boolean playerMoved = engine.processPlayerMove(dx, dy, direction);
        if (playerMoved) {
            textArea.setText("You moved " + direction + " one step.");
        } else {
            textArea.setText("You tried to move " + direction + " one step but it is a wall.");
        }
        engine.processGameTurn();
        updateGui();

        if (engine.getGameOver()){
            textArea.setText("Game over code 1.");
        }
    }

    private void startGame(){
        engine = new gameEngine(true, (int) difficultySlider.getValue());
        updateGui();
        textArea.setText("Welcome to Mini Dungeon!\n");
        textArea.appendText("Starting level " + engine.getLevel() + " - Difficulty " + engine.getDifficulty() + ".\n");
    }

    private void saveGame(){
    engine.saveGame();
    textArea.appendText("Game saved!\n");
    }

    private void loadGame(){
        engine = new gameEngine(true, 1);
        if (engine.isGameLoaded()){
            textArea.appendText("Game loaded!\n");
        } else {
            textArea.appendText("Load failed, starting new game\n");
        }


        difficultySlider.setValue(engine.getDifficulty());
    }

    private void quitGame(){
        textArea.setText("Deciding to leave already?");
    }

    private void updateGui() {
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);


        map gameMap = engine.getMap();
        int mapSize = gameMap.getSize();





        //Loop through map board and add each cell into grid pane
        for(int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                cell cell = gameMap.getCell(x, y);

                cell.getChildren().clear();

                Pane pane = new Pane();
                pane.setPrefSize(CELL_SIZE, CELL_SIZE);
                pane.setStyle("-fx-border-colour: #333333; -fx-border-width: 0.5");

                //walls
                if (!cell.isWalkable()) {
                    cell.setStyle("-fx-background-color: #555555");
                } else {
                    cell.setStyle("-fx-background-color: #AAAAAA");
                }

                //player
                if (cell.hasPlayer()) {
                    cell.getChildren().add(new Text ("P"));
                } else if (cell.hasLadder()) {
                    cell.getChildren().add(new Text ("L"));
                } else if (cell.hasEnemy()) {
                    Enemy enemy = cell.getEnemy();
                    cell.getChildren().add(new Text (enemy.getSymbol()));
                } else if (cell.hasItem()) {
                    Item item = cell.getItem();
                    cell.getChildren().add(new Text (item.getSymbol()));
                }

                gridPane.add(cell, x, y);
            }
        }

    }

}
