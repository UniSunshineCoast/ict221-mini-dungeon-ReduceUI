package dungeon.engine;

import java.util.Random;
import java.util.List;
import java.util.Scanner;

public class GameEngine {

    private Map map;
    private Player player;
    private int level;
    private boolean gameOver;
    private int score;
    private Scanner scanner;
    private int previousExitX;
    private int previousExitY;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.level = 1;
        this.score = 0;
        this.gameOver = false;
        this.previousExitX = 0;
        this.previousExitY = 0;
        StartLevel();
    }

    public void StartLevel() {
        this.map = new Map(10);
        System.out.printf("Starting level %d\n", level);
        System.out.printf("The size of map is %d * %d\n", map.getSize(), map.getSize());

        // place player
        if (level == 1) {
            player = new Player(0, 9);
        } else {
            player = new Player(previousExitX, previousExitY); //change to match previous exit
        }
        map.placePlayer(player);

        // place ladder/exit
        map.placeLadder();

        // place enemies ()?
        // place gold
        // place health potions
        // place traps

    }

    public void Play(){
        while (!gameOver) {
            map.displayMap();
            getPlayerInput();
            handleInteractions();

        }
        System.out.println("Game Over");
        scanner.close();
    }

    public void getPlayerInput(){
        System.out.println("Enter your move (1=left, 2=up, 3=down, 4=right):");
        String input = scanner.nextLine().toLowerCase();
        if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")) {
            movePlayer(input);
        } else if (input.equals("q")) {
            gameOver = true;

        // more inputs

        } else {
            System.out.println("Invalid move.");
            getPlayerInput();
        }
    }

    public void movePlayer(String input) {
        int dx = 0, dy = 0;
        switch (input) {
            case "1":
                dx = -1;
                break;
            case "2":
                dy = -1;
                break;
            case "3":
                dy = 1;
                break;
            case "4":
                dx = 1;
                break;
        }
        player.move(dx,dy,map);
    }

    private void handleInteractions(){
        Cell playerCell = map.getCell(player.getX(),player.getY());

        //Check for ladder/exit
        if (playerCell.hasLadder()) {
            level++;
            previousExitX = player.getX();
            previousExitY = player.getY();

            StartLevel();
        }

        //check for enemy (melee / ranged)

        //check for item (potion / trap / gold)
    }

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.Play();
    }
}


// hang over javaFX
//        map[0][0].setStyle("-fx-background-color: #7baaa4");
//        map[size-1][size-1].setStyle("-fx-background-color: #7baaa4");
//    }