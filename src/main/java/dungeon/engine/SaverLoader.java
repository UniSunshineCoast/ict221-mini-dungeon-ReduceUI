package dungeon.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading the game state.
 */
public class SaverLoader {

    private static final String Filename = "savedGame.dat";

    public void saveGame(map map, player player, int level, int score, int moves, List<Enemy> meleeList, List<Enemy> rangedList, int difficulty) {
        System.out.println("Saving game...");
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(Filename))) {
            out.writeInt(player.getX());
            out.writeInt(player.getY());
            out.writeInt(player.getHealth());

            out.writeInt(level);
            out.writeInt(score);
            out.writeInt(moves);
            out.writeInt(difficulty);

            out.writeInt(map.getSize());
            for (int y = 0; y < map.getSize(); y++) {
                for (int x = 0; x < map.getSize(); x++) {
                    cell cell = map.getCell(x, y);
                    out.writeBoolean(cell.isWalkable());
                    out.writeBoolean(cell.hasLadder());

                    out.writeBoolean(cell.hasEnemy());
                    if (cell.hasEnemy()) {
                        Enemy enemy = cell.getEnemy();
                        if (enemy instanceof Melee) {
                            out.writeUTF("M");
                        } else if (enemy instanceof Ranged) {
                            out.writeUTF("R");
                        } else {
                            out.writeUTF("?");
                        }
                    }

                    out.writeBoolean(cell.hasItem());
                    if (cell.hasItem()) {
                        Item item = cell.getItem();
                        if (item instanceof Gold) {
                            out.writeUTF("G");
                        } else if (item instanceof Trap) {
                            out.writeUTF("T");
                        } else if (item instanceof Heal) {
                            out.writeUTF("H");
                        } else {
                            out.writeUTF("?"); // error catching
                        }
                    }
                }
            }


        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public GameState loadGame(gameEngine gameEngine) {
        System.out.println("Loading game...");
        try (DataInputStream in = new DataInputStream(new FileInputStream(Filename))) {
            int playerX = in.readInt();
            int playerY = in.readInt();
            int health = in.readInt();
            player loadedPlayer = new player(playerX, playerY, health);
//            loadedPlayer.setHealth(health);

            int loadedLevel = in.readInt();
            int loadedScore = in.readInt();
            int loadedMoves = in.readInt();
            int loadedDifficulty = in.readInt();

            int mapSize = in.readInt();
            map loadedMap = new map(mapSize, gameEngine);

            List<Enemy> loadedMelee = new ArrayList<>();
            List<Enemy> loadedRanged = new ArrayList<>();

            for (int y = 0; y < mapSize; y++) {
                for (int x = 0; x < mapSize; x++) {
                    cell cell = loadedMap.getCell(x, y);

                    cell.setWalkable(in.readBoolean());
                    cell.setHasLadder(in.readBoolean());

                    boolean hasEnemy = in.readBoolean();
                    Enemy enemy = null;
                    if (hasEnemy) {
                        String enemyType = in.readUTF();
                        if (enemyType.equals("M")) {
                            enemy = new Melee(x, y);
                            loadedMelee.add(enemy);
                        } else if (enemyType.equals("R")) {
                            enemy = new Ranged(x, y);
                            loadedRanged.add(enemy);
                        } else {
                            System.out.println("Unknown Enemy");
                        }
                        if (enemy != null) {
                            cell.setEnemy(enemy);
                        }
                    }

                    boolean hasItem = in.readBoolean();
                    Item item = null;
                    if (hasItem) {
                        String itemType = in.readUTF();
                        if (itemType.equals("G")) {
                            item = new Gold(x, y);
                        } else if (itemType.equals("T")) {
                            item = new Trap(x, y);
                        } else if (itemType.equals("H")) {
                            item = new Heal(x, y);
                        } else {
                            System.out.println("Unknown Item");
                        }
                        if (item != null) {
                            cell.setItem(item);
                        }
                    }
                }
            }

            loadedMap.getCell(loadedPlayer.getX(), loadedPlayer.getY()).setPlayer(loadedPlayer);
            System.out.println("Load successful");
            return new GameState(loadedPlayer, loadedLevel, loadedScore, loadedMoves, loadedDifficulty, loadedMap, loadedMelee, loadedRanged);

        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    public static class GameState {
        private final player player;
        private final int level;
        private final int score;
        private final int moves;
        private final int difficulty;
        private final map map;
        private final List<Enemy> meleeList;
        private final List<Enemy> rangedList;


        public GameState(player player, int level, int score, int moves, int difficulty, map map, List<Enemy> meleeList, List<Enemy> rangedList) {
            this.player = player;
            this.level = level;
            this.score = score;
            this.moves = moves;
            this.difficulty = difficulty;
            this.map = map;
            this.meleeList = meleeList;
            this.rangedList = rangedList;

        }

        public player getPlayer() {return player;}
        public int getLevel() {return level;}
        public int getScore() {return score;}
        public int getMoves() {return moves;}
        public int getDifficulty() {return difficulty;}
        public map getMap() {return map;}
        public List<Enemy> getMeleeList() {return meleeList;}
        public List<Enemy> getRangedList() {return rangedList;}
    }















}