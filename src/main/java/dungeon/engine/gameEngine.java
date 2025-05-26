package dungeon.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import dungeon.engine.SaverLoader.GameState;

public class gameEngine {

    private map map;
    private player player;
    private int level;
    private int maxLevel = 2;
    private boolean gameOver;
    private int score;
    private int health;
    private int previousExitX;
    private int previousExitY;
    private int moves;
    private boolean lastMove;
    private List<Enemy> meleeList;
    private List<Enemy> rangedList;
    private int difficulty = 4;
    private boolean difficultySet = false;
    private final ScoreManager scoreManager;
    private final SaverLoader saverLoader;
    private boolean gameLoaded;
    private boolean isClimbingLadder;
    private String myString;

    public gameEngine(boolean loadChoice, int difficulty) {

        this.scoreManager = new ScoreManager();
        this.saverLoader = new SaverLoader();
        this.difficulty = difficulty;
        this.gameLoaded = false;
        this.gameOver = false;
        this.isClimbingLadder = false;
        this.myString = "3";

        if (loadChoice) {
            if (!loadGame()) {
                System.out.println("Failed to load, starting new game.");

                initialiseNewGame();
            }
        } else {
            initialiseNewGame();
        }
    }

    private void initialiseNewGame() {
        this.level = 1;
        this.maxLevel = 2;
        this.score = 0;
        this.health = 10;
        this.gameOver = false;
        this.previousExitX = 0; //initialise as level 1 start position
        this.previousExitY = 9;
        this.moves = 100;
        this.lastMove = false;
        startLevel();
    }

    private void startLevel() {
        this.map = new map(10, this);
        myString = "\nStarting level " + level + " - Difficulty " + difficulty;
//        System.out.println(myString);
        player = new player(previousExitX, previousExitY, health);
        map.placePlayer(player);
        map.placeLadder();
        placeItems();
        meleeList = new ArrayList<>();
        rangedList = new ArrayList<>();
        placeEnemies();
    }

    private void placeItems() {
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

    private void placeEnemies() {
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

    public void processGameTurn() {
        if (gameOver) return;
        handleInteractions();
        if (moves == 0) setGameOver(true);
        if (level > maxLevel) setGameOver(true);
        if (player.getHealth() <= 0) setGameOver(true);
        if (gameOver) handleGameOver();
        if (!gameOver) moveEnemies();
    }

    public boolean processPlayerMove(int dx, int dy) {
        boolean success = player.move(dx, dy, map);
        if (success) {
            moves--;
            return true;
        } else {
            return false;
        }
    }

    public void handleGameOver() {
        if (moves == 0) {
            System.out.println("Game Over - No more moves left");
            score = -1;
        } else if (player.getHealth() <= 0) {
            System.out.println("Game Over - You ran out of health");
            score = -1;
        } else if (level > maxLevel) {
            System.out.println("Congratulations - You have completed the Dungeon");
        } else {
            System.out.println("Game Over - Deciding to leave already?");
            score = -1;
        }

        System.out.println("Final Score: " + score);
        scoreManager.addScore(score);
        scoreManager.displayHighScores();

        scoreManager.saveHighScores();
    }

    private void moveEnemies() {
        if (!isClimbingLadder) {
            for (Enemy enemy : meleeList) {
                enemy.move(map, player);
            }
            for (Enemy enemy : rangedList) {
                enemy.move(map, player);
            }
        }
        isClimbingLadder = false;
    }

    private void handleInteractions() {
        cell playerCell = map.getCell(player.getX(), player.getY());

        //Check for ladder/exit
        if (playerCell.hasLadder()) {
            difficulty += 2;
            level++;
            if (level >= maxLevel) {
                previousExitX = player.getX();
                previousExitY = player.getY();
                health = player.getHealth();
                isClimbingLadder = true;
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

    public void saveGame() {
        saverLoader.saveGame(map, player, level, score, moves, meleeList, rangedList, difficulty);
    }

    public boolean loadGame() {
        GameState loadedState = saverLoader.loadGame(this);
        if (loadedState != null) {
            this.map = loadedState.getMap();
            this.player = loadedState.getPlayer();
            this.level = loadedState.getLevel();
            this.score = loadedState.getScore();
            this.moves = loadedState.getMoves();
            this.meleeList = loadedState.getMeleeList();
            this.rangedList = loadedState.getRangedList();
            this.difficulty = loadedState.getDifficulty();
            this.gameLoaded = true;
            System.out.printf("Continuing level %d - Difficulty %d\n", level, difficulty);
            return true;
        }
        return false;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMoves() {
        return moves;
    }

    public int getHealth() {
        return player.getHealth();

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public map getMap() {
        return map;
    }

    public void setGameOver(boolean bool) {
        this.gameOver = bool;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public boolean isDifficultySet() {
        return difficultySet;
    }

    public void setDifficultySet(boolean difficultySet) {
        this.difficultySet = difficultySet;
    }

    public boolean isGameLoaded() {
        return gameLoaded;
    }

    public int getPreviousExitX() {
        return previousExitX;
    }
    public int getPreviousExitY() {
        return previousExitY;
    }

    public String getMyString() {
        return myString;
    }
}