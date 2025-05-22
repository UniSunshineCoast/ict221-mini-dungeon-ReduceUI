package dungeon.engine;

public class Heal extends Item {

    private final int healValue;

    public Heal(int x, int y) {
        super(x, y, " H ");
        this.healValue = 4;
    }

    public int getHealValue() {
        return healValue;
    }
}