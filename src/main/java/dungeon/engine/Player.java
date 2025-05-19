package dungeon.engine;

public class Player {
    private int x;
    private int y;
    private String symbol;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.symbol = "P";
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
    public String getSymbol() {
        return symbol;
    }
}
