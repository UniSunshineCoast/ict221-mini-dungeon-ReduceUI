package dungeon.engine;

import java.util.List;

public abstract class Enemy {

    protected int x;
    protected int y;
    protected int attackDamage;
    protected String symbol;
    protected int value;

    public Enemy (int x, int y, int attackDamage, String symbol, int value) {
        this.x = x;
        this.y = y;
        this.attackDamage = attackDamage;
        this.symbol = symbol;
        this.value = value;
    }

    public int getAttackDamage(){
        return attackDamage;
    }

    public String getSymbol(){
        return symbol;
    }

    public int getValue(){
        return value;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public abstract List<String> move(map map, player player);
}
