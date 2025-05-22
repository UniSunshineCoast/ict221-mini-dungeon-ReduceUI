package dungeon.engine;

import java.util.Random;

public class Melee extends Enemy{

    public Melee(int x, int y){
        super (x, y, 2, " M ", 2);
    }

    @Override
    public void move(map map, player player) {
        Random rand = new Random();
        int dx = 0, dy = 0;
        int moveAttempts = 0;

        while (moveAttempts < 4) {
            int direction = rand.nextInt(4);
            switch (direction) {
                case 0:
                    dx = 0;
                    dy = -1;
                    break;
                case 1:
                    dx = 0;
                    dy = 1;
                    break;
                case 2:
                    dx = -1;
                    dy = -0;
                    break;
                case 3:
                    dx = 1;
                    dy = 0;
                    break;
            }
            int newX = x + dx;
            int newY = y + dy;

            if (map.getCell(newX, newY) != null && map.getCell(newX, newY).isWalkable() && !map.getCell(newX, newY).isOccupied()) {
                map.getCell(x, y).setEnemy(null);
                map.getCell(x, y).setOccupied(false);
                x = newX;
                y = newY;
                map.getCell(x, y).setEnemy(this);
                map.getCell(x, y).setOccupied(true);
                return;
            }
            moveAttempts++;
        }
    }
}

