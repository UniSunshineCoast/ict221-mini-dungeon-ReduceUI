package dungeon.engine;

public class Trap extends Item {

    private final int damage;

    public Trap(int x, int y) {
        super(x, y, " T ");
        this.damage = 2;
    }

    public int getDamage(){
        return damage;
    }
}
