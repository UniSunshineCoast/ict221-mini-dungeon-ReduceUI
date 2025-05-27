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
    private List<Enemy> meleeList;
    private List<Enemy> rangedList;
    private int difficulty = 4;
    private final ScoreManager scoreManager;
    private final SaverLoader saverLoader;
    private boolean gameLoaded;
    private boolean isClimbingLadder;
    private List<String> currentTurnMessages;
    private List<String> meleeMessages;

    public gameEngine(boolean loadChoice, int difficulty) {
        this.scoreManager = new ScoreManager();
        this.saverLoader = new SaverLoader();
        this.difficulty = difficulty;
        this.gameLoaded = false;
        this.gameOver = false;
        this.isClimbingLadder = false;
        this.currentTurnMessages = new ArrayList<>();
        this.meleeList = new ArrayList<>();

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
        currentTurnMessages.clear();
        startLevel();
    }

    private void startLevel() {
        this.map = new map(10, this);
        addGameMessage("Starting level " + level + " - Difficulty " + difficulty);
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
        moveEnemies();
        if (moves == 0) setGameOver(true);
        if (level > maxLevel) setGameOver(true);
        if (player.getHealth() <= 0) setGameOver(true);
        if (gameOver) {
            handleGameOver();
            processScores();
        }
    }

    public void processPlayerMove(int dx, int dy, String direction) {
        currentTurnMessages.clear();
        List<String> playerMessages = player.move(dx, dy, map, direction);
        currentTurnMessages.addAll(playerMessages);
        moves--;
    }

    public void handleGameOver() {
        if (moves == 0) {
            addGameMessage("Game Over - No more moves left");
            score = -1;
        } else if (player.getHealth() <= 0) {
            addGameMessage("Game Over - You ran out of health");
            score = -1;
        } else if (level > maxLevel) {
            addGameMessage("Congratulations - You have completed the Dungeon");
        } else {
            addGameMessage("Deciding to leave already?");
        }
        addGameMessage("Final Score: " + score);
    }

    public void processScores() {
        List<String> scoreMessages = scoreManager.addScore(score);
        currentTurnMessages.addAll(scoreMessages);
        List<String> highScoreMessages = scoreManager.displayHighScores();
        currentTurnMessages.addAll(highScoreMessages);
        scoreManager.saveHighScores();
    }

    private void moveEnemies() {
        if (isClimbingLadder) return; {
            for (Enemy enemy : meleeList) {
                meleeMessages = enemy.move(map, player);
                currentTurnMessages.addAll(meleeMessages);
            }
            for (Enemy enemy : rangedList) {
                List<String> rangedMessages = enemy.move(map, player);
                currentTurnMessages.addAll(rangedMessages);
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
            if (level <= maxLevel) {
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
                    addGameMessage("You picked up a gold.");
                    playerCell.setItem(null);
                }
                case Trap trap -> {
                    player.takeDamage(trap.getDamage());
                    addGameMessage("You fell into a trap.");
                }
                case Heal heal -> {
                    player.takeDamage(heal.getHealValue() * -1);
                    addGameMessage("You drank a health potion.");
                    playerCell.setItem(null);
                }
                case null, default -> addGameMessage("Invalid item");
            }
        }

        //check for enemy (melee / ranged)
        if (playerCell.hasEnemy()) {
            Enemy enemy = playerCell.getEnemy();
            score += enemy.getValue();

            if (enemy instanceof Melee) {
                player.takeDamage(enemy.getAttackDamage());
                meleeList.remove(enemy);
                addGameMessage("You defeated a melee mutant, but you lost " + enemy.getAttackDamage() + " HP.");
            } else if (enemy instanceof Ranged) {
                rangedList.remove(enemy);
                addGameMessage("You defeated a ranged mutant.");
            }
            playerCell.setEnemy(null);
        }
    }

    public void addGameMessage(String msg){
        currentTurnMessages.add(msg);
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
            addGameMessage("\nContinuing level " + level + " - Difficulty " + difficulty);

            return true;
        }
        return false;
    }

    public int getDifficulty() {
        return difficulty;
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

    public map getMap() {
        return map;
    }

    public void setGameOver(boolean bool) {
        this.gameOver = bool;
    }

    public boolean isGameOver() {
        return gameOver;
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

    public List<String> Messages() {
        return currentTurnMessages;
    }
}