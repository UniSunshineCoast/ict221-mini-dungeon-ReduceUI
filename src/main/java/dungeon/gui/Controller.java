package dungeon.gui;

import dungeon.engine.gameEngine;
import dungeon.engine.map;
import dungeon.engine.cell;
import dungeon.engine.Enemy;
import dungeon.engine.Item;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

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
        difficultyLabel.textProperty().bind(difficultySlider.valueProperty().asString("%.0f"));

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

        boolean playerMoved = engine.processPlayerMove(dx, dy);
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
        engine = new gameEngine(false, (int) difficultySlider.getValue());
        updateGui();
        textArea.setText("Welcome to Mini Dungeon!\n");
        textArea.appendText(engine.getMyString());
    }

    private void saveGame(){
    engine.saveGame();
    textArea.appendText("Game saved!\n");
    }

    private void loadGame(){
        engine = new gameEngine(true, 1);
        if (engine.isGameLoaded()){
            textArea.setText("Game loaded!\n");
        } else {
            textArea.setText("Load failed, starting new game\n");
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

//                Pane pane = new Pane();
//                pane.setPrefSize(CELL_SIZE, CELL_SIZE);
//                pane.setStyle("-fx-border-colour: #333333; -fx-border-width: 0.5");

                //walls
                if (!cell.isWalkable()) {
                    cell.setStyle("-fx-background-color: #555555");
                } else {
                    cell.setStyle("-fx-background-color: #AAAAAA");
                }

                ImageView imageView = null;
                String imagePath = null;
                Enemy enemy = null;
                Item item = null;

                //dynamic objects
                if (cell.hasPlayer()) {
                    imagePath = "/p.png";
                } else if (cell.hasLadder()) {
                    imagePath = "/l.png";
                } else if (cell.hasEnemy()) {
                    enemy = cell.getEnemy();
                    imagePath = "/" + enemy.getSymbol().toLowerCase().trim() + ".png";
                } else if (cell.hasItem()) {
                    item = cell.getItem();
                    imagePath = "/" + item.getSymbol().toLowerCase().trim() + ".png";
                }

                if (imagePath != null) {
                    try {
                        InputStream inputStream = getClass().getResourceAsStream(imagePath);
                        assert inputStream != null;
                        Image image = new Image(inputStream);
                        imageView = new ImageView(image);
                        imageView.setFitWidth(CELL_SIZE);
                        imageView.setFitHeight(CELL_SIZE);

                        imageView.setPreserveRatio(true);
                        cell.getChildren().add(imageView);
                    } catch (Exception e){
                        if (cell.hasPlayer()) cell.getChildren().add(new Text ("P"));
                        if (cell.hasLadder()) cell.getChildren().add(new Text ("L"));
                        if (cell.hasEnemy()) {
                            assert enemy != null;
                            cell.getChildren().add(new Text (enemy.getSymbol()));
                        }
                        if (cell.hasItem()) {
                            assert item != null;
                            cell.getChildren().add(new Text (item.getSymbol()));
                        }
                    }
                }
                gridPane.add(cell, x, y);
            }
        }

    }

}