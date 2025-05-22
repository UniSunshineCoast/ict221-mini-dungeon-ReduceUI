package dungeon.engine;

public abstract class Item {
    protected int x;
    protected int y;
    protected String symbol;

    public Item(int x, int y, String symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }
}
