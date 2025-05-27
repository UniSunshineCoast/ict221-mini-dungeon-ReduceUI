package dungeon.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Melee extends Enemy{

    public Melee(int x, int y){
        super (x, y, 2, " M ", 2);
    }

    @Override
    public List<String> move(map map, player player) {
        List<String> messages = new ArrayList<>();
        int oldX = this.x;
        int oldY = this.y;
        int targetX = player.getX();
        int targetY = player.getY();
        int dx = 0;
        int dy = 0;

        if (targetX > oldX) dx = 1;
        else if (targetX < oldX) dx = -1;

        if (targetY > oldY) dy = 1;
        else if (targetY < oldY) dy = -1;

        boolean moved = false;

        if (dx != 0 || dy != 0) {
            if (dx != 0) {
                int newX = oldX + dx;
                int newY = oldY;
                moved = tryMove(map, newX, newY, oldX, oldY);
            }
            if (!moved && dy != 0){
                int newX = oldX;
                int newY = oldY + dy;
                moved = tryMove(map, newX, newY, oldX, oldY);
            }
        }
        return messages;
    }
    private boolean tryMove(map map, int newX, int newY, int oldX, int oldY) {
        if (newX >= 0 && newX < map.getSize() && newY >= 0 && newY < map.getSize() &&
                map.getCell(newX, newY).isWalkable() && !map.getCell(oldX, oldY).isOccupied()) {

            map.getCell(oldX, oldY).setEnemy(null);
            this.setX(newX);
            this.setY(newY);
            map.getCell(newX, newY).setEnemy(this);

            return true;
        }
        return false;
    }










//        Random rand = new Random();
//        int dx = 0, dy = 0;
//        int moveAttempts = 0;
//
//        while (moveAttempts < 4) {
//            int direction = rand.nextInt(4);
//            switch (direction) {
//                case 0:
//                    dx = 0;
//                    dy = -1;
//                    break;
//                case 1:
//                    dx = 0;
//                    dy = 1;
//                    break;
//                case 2:
//                    dx = -1;
//                    dy = -0;
//                    break;
//                case 3:
//                    dx = 1;
//                    dy = 0;
//                    break;
//            }
//            int newX = x + dx;
//            int newY = y + dy;
//
//            if (map.getCell(newX, newY) != null && map.getCell(newX, newY).isWalkable() && !map.getCell(newX, newY).isOccupied()) {
//                map.getCell(x, y).setEnemy(null);
//                map.getCell(x, y).setOccupied(false);
//                x = newX;
//                y = newY;
//                map.getCell(x, y).setEnemy(this);
//                map.getCell(x, y).setOccupied(true);
//
//            }
//            moveAttempts++;
//        }
//        return messages;
//    }
}

