package dungeon.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class gameEngine {

    private map map;
    private final player player;
    private int level;
    private boolean gameOver;
    private int score;
    private final Scanner scanner;
    private int previousExitX;
    private int previousExitY;
    private int moves;
    private boolean lastMove;
    private List<Enemy> meleeList;
    private List<Enemy> rangedList;
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
        this.difficulty = 3;
        player = new player(previousExitX, previousExitY);
        setDifficulty();
        startLevel();
    }

    private void startLevel() {
        this.map = new map(10);
        System.out.printf("Starting level %d\n", level);


        map.placePlayer(player);
        map.placeLadder();
        placeItems();
        meleeList = new ArrayList<>();
        rangedList = new ArrayList<>();
        placeEnemies();
    }

    private void placeItems(){
        int numGold = 5;
        int numTrap = 5;
        int numHeal = 2;

        //place gold
        for (int i = 0; i < numGold; i++) {
            int[] coords = randomPlacement();
            map.getCell(coords[0], coords[1]).setItem(new Gold(coords[0], coords[1]));
        }

        //place traps
        for (int i = 0; i < numTrap; i++) {
            int[] coords = randomPlacement();
            map.getCell(coords[0], coords[1]).setItem(new Trap(coords[0], coords[1]));
        }

        //place healing potion
        for (int i = 0; i < numHeal; i++) {
            int[] coords = randomPlacement();
            map.getCell(coords[0], coords[1]).setItem(new Heal(coords[0], coords[1]));
        }
    }

    private void placeEnemies(){
        int numMelee = 3;
        int numRanged = difficulty;

        for (int i = 0; i < numMelee; i++) {
            int[] coords = randomPlacement();
            meleeList.add(new Melee(coords[0], coords[1]));
            map.getCell(coords[0], coords[1]).setEnemy(meleeList.get(i));
        }

        for (int i = 0; i < numRanged; i++) {
            int[] coords = randomPlacement();
            rangedList.add(new Ranged(coords[0], coords[1]));
            map.getCell(coords[0], coords[1]).setEnemy(rangedList.get(i));
        }
    }

    private int[] randomPlacement() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(map.getSize());
            y = rand.nextInt(map.getSize());
        } while (map.getCell(x, y).isOccupied() || !map.getCell(x, y).isWalkable());
        map.getCell(x, y).setOccupied(true);
        return new int[]{x, y};
    }

    private void play(){
        while (!gameOver && moves > 0 && level < 3 && player.getHealth() > 0) {
            System.out.println("MovesRemaining: " + moves + " HP: " + player.getHealth() + " Score: " + score);
            map.displayMap();
            getPlayerInput();
            handleInteractions();
            moveEnemies();
        }
        handleGameOver();
    }

    private void setDifficulty() {
        System.out.println("Select difficulty to play (0-10): ");
        String input = scanner.nextLine();
        try {
            int chosenDifficulty = Integer.parseInt(input);
            if (chosenDifficulty >= 0 && chosenDifficulty <= 10) {
                difficulty = chosenDifficulty;
            } else {
                System.out.printf("Invalid input. Difficulty set to default (%d).\n", difficulty);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number (e.g. 5).");
            setDifficulty();
        }
    }

    private void handleGameOver(){
        if (moves == 0){
            System.out.println("Game Over - No more moves left");
            score = -1;
        } else if (player.getHealth() <= 0) {
            System.out.println("Game Over - You ran out of health");
            score = -1;
        } else if (level == 3) {
            System.out.println("Congratulations - You have completed the Dungeon");
        } else {
            System.out.println("Game Over - Deciding to leave already?");
        }

        System.out.println("Final Score: " + score);
        scanner.close();

        //high Score!
    }

    private void getPlayerInput(){
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

    private void movePlayer(String input) {
        String moveReport = "";
        int dx = 0, dy = 0;
        moveReport = switch (input) {
            case "1" -> {
                dx = -1;
                yield "left";
            }
            case "2" -> {
                dy = -1;
                yield "up";
            }
            case "3" -> {
                dy = 1;
                yield "down";
            }
            case "4" -> {
                dx = 1;
                yield "right";
            }
            default -> moveReport;
        };

        lastMove = player.move(dx,dy,map);
        if (lastMove) {
            moves --;
            System.out.println("You moved " + moveReport + " one step.");
        } else {
            System.out.println("You tried to move " + moveReport + " one step but it is a wall.");
        }
    }

    private void moveEnemies(){
        for (Enemy enemy : meleeList) {
            enemy.move(map, player);
        }
        for (Enemy enemy : rangedList) {
            enemy.move(map, player);
        }
    }

    private void handleInteractions(){
        cell playerCell = map.getCell(player.getX(),player.getY());

        //Check for ladder/exit
        if (playerCell.hasLadder()) {
            difficulty += 2;
            level++;
            if (level < 3) {
                previousExitX = player.getX();
                previousExitY = player.getY();
                startLevel();
            }
        }

        //check for item (potion / trap / gold)
        if (playerCell.hasItem()) {
            Item item = playerCell.getItem();
            switch (item) {
                case Gold gold -> {
                    score += gold.getValue();
                    System.out.println("You picked up a gold.");
                    playerCell.setItem(null);
                }
                case Trap trap -> {
                    player.takeDamage(trap.getDamage());
                    System.out.println("You fell into a trap.");
                }
                case Heal heal -> {
                    player.takeDamage(heal.getHealValue() * -1);
                    System.out.println("You drank a health potion.");
                    playerCell.setItem(null);
                }
                case null, default -> System.out.println("Invalid item");
            }
        }

        //check for enemy (melee / ranged)
        if (playerCell.hasEnemy()) {
            Enemy enemy = playerCell.getEnemy();
            score += enemy.getValue();

            if (enemy instanceof Melee) {
                player.takeDamage(enemy.getAttackDamage());
                meleeList.remove(enemy);
                System.out.println("You attacked a melee mutant and wins."); // "wins" as per design document.
            } else if (enemy instanceof Ranged) {
                rangedList.remove(enemy);
                System.out.println("You attacked a ranged mutant and wins.");
            }
            playerCell.setEnemy(null);
        }
    }

    public static void main(String[] args) {
        gameEngine engine = new gameEngine();
        engine.play();
    }
}


// hang over javaFX
//        map[0][0].setStyle("-fx-background-color: #7ba aa4");
//        map[size-1][size-1].setStyle("-fx-background-color: #7ba aa4");
//    }