package dungeon.engine;

import javafx.scene.text.Text;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private Player player;
    private Cell[][] map;
    private int entryX; // Keep track of the entry point
    private int entryY;
    private int ladderX; // Keep track of the exit point
    private int ladderY;
    private List<Point> occupiedPositions;

    /**
     * Creates a square game board.
     *
     * @param size the width and height.
     */
    public GameEngine(int size) {
        map = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = new Cell();
                Text text = new Text(i + "," + j); //javaFX
                cell.getChildren().add(text);
                map[i][j] = cell;
            }
        }
        //level 1 entry bottom left
        entryX = 0;
        entryY = size - 1;

        map[0][0].setStyle("-fx-background-color: #7baaa4");
        map[size-1][size-1].setStyle("-fx-background-color: #7baaa4");
    }

    /**
     * The size of the current game.
     *
     * @return this is both the width and the height.
     */
    public int getSize() {
        return map.length;
    }

    /**
     * The map of the current game.
     *
     * @return the map, which is a 2d array.
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Isolate specific cell to place game objects
     * @param row is the y coordinate top to bottom
     * @param col is the x coordinate left to right
     * @return the cell
     */
    public Cell getCell(int row, int col) {
        return map[row][col];
    }

    /**
     * Generate the player start point
     */
    public void placePlayer() {
        player = new Player(entryX, entryY);
        updateMap();
    }

    public void placeLadder() {
        Random rand = new Random();
        do {
            ladderX = rand.nextInt(getSize());
            ladderY = rand.nextInt(getSize());
        } while (ladderX == entryX && ladderY == entryY);
    }

    /**
     * Update the map
     */
    private void updateMap() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                map[i][j].setPlayer(null); //clear previous player
            }
        }
        Cell playerCell = map[player.getX()][player.getY()];
        playerCell.SetPlayer(player);
    }

    /**
     * Plays a text-based game
     */
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        System.out.printf("The size of map is %d * %d\n", engine.getSize(), engine.getSize());

        //print out a representation of the map to the console.
        for (int i = 0; i < engine.getSize(); i++) {
            for (int j = 0; j < engine.getSize(); j++) {
                if (engine.getCell(i, j).getPlayer() != null) {
                    System.out.print(player.getSymbol());
                } else {
                    System.out.print(" _ "); //empty cell
                }
            }
            System.out.println(); //start next row
        }
        System.out.print("Command:");
        System.out.println("Player is at: " + engine.player.getX() + ", " + engine.player.getY());
        System.out.println("Exit is at: " + engine.getLadderX() + ", " + engine.getLadderY());


    }
}
