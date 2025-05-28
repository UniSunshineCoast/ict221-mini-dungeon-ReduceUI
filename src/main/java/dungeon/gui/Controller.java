package dungeon.gui;

import dungeon.engine.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML private Button upBn;
    @FXML private Button leftBn;
    @FXML private Button rightBn;
    @FXML private Button downBn;
    @FXML private TextArea textArea;
    @FXML private Button startBn;
    @FXML private Button helpBn;
    @FXML private Button saveBn;
    @FXML private Button loadBn;
    @FXML private Button quitBn;
    @FXML private Label difficultyLabel;
    @FXML private Slider difficultySlider;
    @FXML private GridPane gridPane;

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

        engine = new gameEngine(false, (int) Math.round(difficultySlider.getValue()));

        //Buttons
        upBn.setOnAction(event -> movePlayer(0, -1, "up"));
        leftBn.setOnAction(event -> movePlayer(-1, 0, "left"));
        rightBn.setOnAction(event -> movePlayer(1, 0, "right"));
        downBn.setOnAction(event -> movePlayer(0, 1, "down"));
        startBn.setOnAction(event -> startGame());
        helpBn.setOnAction(actionEvent -> showHelp());
        saveBn.setOnAction(event -> saveGame());
        loadBn.setOnAction(event -> loadGame());
        quitBn.setOnAction(event -> quitGame());

        textArea.setText("Welcome to Mini Dungeon!\n");
        textArea.setEditable(false);
        updateGui();
    }

    private void movePlayer(int dx, int dy, String direction) {
        if (engine.isGameOver()){
            textArea.setText("Start a new game.");
            return;
        }
        textArea.setText("");
        engine.processPlayerMove(dx, dy, direction);
        engine.processGameTurn();

        updateGui();
        displayGameMessages();

        if (engine.isGameOver()) {
            engine.handleGameOver(); //game over msg
        }
    }

    private void startGame(){
        engine = new gameEngine(false, (int) Math.round(difficultySlider.getValue()));
        updateGui();
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
        updateGui();
        displayGameMessages();
    }

    private void quitGame(){
        textArea.setText("");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit game?");
        alert.setHeaderText("Are you sure you want to quit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            javafx.application.Platform.exit();
            System.exit(0);
        }
        textArea.setText("Let's get some gold!\n");
    }

    private void showHelp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Welcome to Mini Dungeon!");
        String rules = "Enter an exciting dungeon-exploration game set in a \n" +
                "mysterious maze! Navigate through two challenging dungeon \n" +
                "levels, each represented as a 10 x 10 grid filled with \n" +
                "treasures, mutants, traps, and potions.\n\n" +
                "Your goal: achieve the highest possible score by collecting \n" +
                "gold, defeating mutants, and safely escaping the dungeon \n" +
                "through the ladder—all within a limited number of moves!";
        alert.setContentText(rules);
        alert.setResizable(true);
        alert.showAndWait();
    }

    private void displayGameMessages(){
        List<String> messages = engine.Messages();

        for (String message : messages){
            textArea.appendText(message + "\n");
        }
        if (!engine.isGameOver()) {
        textArea.appendText("\nMoves left: " + engine.getMoves() + "\n");
        textArea.appendText("Health remaining: " + engine.getHealth() + "\n");
        textArea.appendText("Score: " + engine.getScore() + "\n");
        }

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