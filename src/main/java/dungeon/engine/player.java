package dungeon.engine;

public class player {
    private int x;
    private int y;

    public player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void move(int dx, int dy, map map) {
        int newX = x + dx;
        int newY = y + dy;
        if (map.getCell(newX, newY) != null && map.getCell(newX, newY).isWalkable()) {
            map.getCell(x, y).setPlayer(null);
            x = newX;
            y = newY;
            map.getCell(x, y).setPlayer(this);
        } else {
            System.out.println("You can't move this direction");
        }
    }
}
