package dungeon.engine;
import java.util.Random;

public class map {

    private final cell[][] map;
    private final int size;

    public map(int size) {
        this.size = size;
        this.map = new cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new cell();
            }
        }
        generateWalls();
    }

    public int getSize() {
        return size;
    }

    public cell getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            //System.out.println("Out of bounds");
            return null;
        }
        return map[x][y];
    }

    private void generateWalls() {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ( i == 0 && j == 9){ //remove potential player/wall overlap on lvl 1
                    map[i][j].setWalkable(true);
                } else if (random.nextDouble() < 0.1) {  //10% chance of wall... 1% chance of blocked path
                    map[i][j].setWalkable(false);
                }
            }
        }
    }

    public void placePlayer(player player) {
        int x = player.getX();
        int y = player.getY();
        if (getCell(x, y) != null) {
            map[x][y].setPlayer(player);
            getCell(x, y).setOccupied(true);
        } else {
            System.out.println("Check map.placePlayer");
        }
    }

    public void placeLadder() {
        Random rand = new Random();
        int ladderX, ladderY;
        do {
            ladderX = rand.nextInt(size);
            ladderY = rand.nextInt(size);
        } while (map[ladderX][ladderY].isOccupied() || !map[ladderX][ladderY].isWalkable());
        getCell(ladderX, ladderY).setHasLadder(true);
    }

    public void displayMap(){
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cell cell = map[x][y];
                if (cell.hasPlayer()){
                    System.out.print(" P ");
                } else if (cell.hasLadder()) {
                    System.out.print(" L ");
                } else if (!cell.isWalkable()) {
                    System.out.print(" # ");
                } else if (cell.hasItem()) {
                    System.out.print(cell.getItem().getSymbol());
                } else if (cell.hasEnemy()) {
                    System.out.print(cell.getEnemy().getSymbol());
                } else {
                    System.out.print(" _ ");
                }
            }
            System.out.println();
        }
    }
}
