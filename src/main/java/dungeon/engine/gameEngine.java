package dungeon.engine;

import java.util.Random;
import java.util.Scanner;

public class gameEngine {

    private map map;
    private player player;
    private int level;
    private boolean gameOver;
    private int score;
    private Scanner scanner;
    private int previousExitX;
    private int previousExitY;
    private int moves;
    private boolean lastMove;
    private int health;


    private int difficulty;

    private gameEngine() {
        this.scanner = new Scanner(System.in);
        this.level = 1;
        this.score = 0;
        this.gameOver = false;
        this.previousExitX = 0; //initialise as level 1 start position
        this.previousExitY = 9;
        this.moves = 100;
        this.lastMove = false;
        this.health = 10;


        this.difficulty = 3;
        startLevel();
    }

    private void startLevel() {
        this.map = new map(10);
        System.out.printf("Starting level %d\n", level);
        //System.out.printf("The size of map is %d * %d\n", map.getSize(), map.getSize());

        // place player
        player = new player(previousExitX, previousExitY);
        map.placePlayer(player);

        // place ladder/exit
        map.placeLadder();

        // place enemies ()?
        //

        // place items
        placeItems();

    }

    private void placeItems(){
        Random rand = new Random();
        int numGold = 5;
        int numHeal = 2;
        int numTrap = 5;

        //place gold
        for (int i = 0; i < numGold; i++) {
            int x, y;
            do {
                x = rand.nextInt(map.getSize());
                y = rand.nextInt(map.getSize());
            } while (map.getCell(x, y).hasPlayer() && !map.getCell(x, y).isWalkable());
            map.getCell(x, y).setItem(new Gold(x, y));
        }

        //place traps

        //place healing potion


    }

    private void placeEnemies(){
        Random rand = new Random();
        int numMelee = 3;
        int numRanged = difficulty;

        //do items first...
    }



    private void play(){
        while (!gameOver && moves > 0 && level < 3) {
            System.out.println("MovesRemaining: " + moves + " HP: " + health + " Score: " + score);
            map.displayMap();
            getPlayerInput();
            if (lastMove) {
                moves --;
            }
            handleInteractions();

        }
        handleGameOver();
    }

    private void handleGameOver(){
        if (moves == 0){
            System.out.println("Game Over - No more moves left");
        } else if (health <= 0) {
            System.out.println("Game Over - You ran out of health");
        } else if (level == 3) {
            System.out.println("Congratulations - You have completed the Dungeon");
        } else {
            System.out.println("Game Over - Deciding to leave already?");
        }

        System.out.println("Final Score: " + score);
        scanner.close();
    }



    private void getPlayerInput(){
        System.out.println("Enter your move (1=left, 2=up, 3=down, 4=right):");
        String input = scanner.nextLine().toLowerCase();
        if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")) {
            lastMove = movePlayer(input);
        } else if (input.equals("q")) {
            gameOver = true;

        // more inputs

        } else {
            System.out.println("Invalid move.");
            getPlayerInput();
        }
    }

    private boolean movePlayer(String input) {
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
        return player.move(dx,dy,map);
    }

    private void handleInteractions(){
        cell playerCell = map.getCell(player.getX(),player.getY());

        //Check for ladder/exit
        if (playerCell.hasLadder()) {
            level++;
            if (level < 3) {
                previousExitX = player.getX();
                previousExitY = player.getY();
                startLevel();
            }
        }

        //check for enemy (melee / ranged)

        //check for item (potion / trap / gold)
        if (playerCell.hasItem()) {
            Item item = playerCell.getItem();
            if (item instanceof Gold){
                score += ((Gold) item).getValue();
                System.out.println("Score: " + score);
            }
            // else if heal / trap


            playerCell.setItem(null);

        }
    }

    public static void main(String[] args) {
        gameEngine engine = new gameEngine();
        engine.play();
    }
}


// hang over javaFX
//        map[0][0].setStyle("-fx-background-color: #7baaa4");
//        map[size-1][size-1].setStyle("-fx-background-color: #7baaa4");
//    }