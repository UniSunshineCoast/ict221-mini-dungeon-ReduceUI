package dungeon.engine;
import java.util.Random;

public class Map {

    private Cell[][] map;
    private int size;

    public Map(int size) {
        this.size = size;
        this.map = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new Cell();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            System.out.println("Out of bounds");
            return null;
        }
        return map[x][y];
    }

    public void placePlayer(Player player) {
        int x = player.getX();
        int y = player.getY();
        if (getCell(x, y) != null) {
            map[x][y].setPlayer(player);
        } else {
            System.out.println("Check placePlayer");
        }
    }

    public void placeLadder() {
        Random rand = new Random();
        int ladderX, ladderY;
        do {
            ladderX = rand.nextInt(size);
            ladderY = rand.nextInt(size);
        } while (map[ladderX][ladderY].hasPlayer());
        getCell(ladderX, ladderY).setHasLadder(true);
    }





    public void displayMap(){
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Cell cell = map[x][y];
                if (cell.hasPlayer()){
                    System.out.print(" P ");
                } else if (cell.hasLadder()){
                    System.out.print(" L ");
                } else {
                    System.out.print(" _ ");
                }
            }
            System.out.println();
        }
    }
}
