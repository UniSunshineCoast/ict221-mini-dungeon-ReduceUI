package dungeon.engine;

public class Gold extends Item{

    private int value;

    public Gold(int x, int y){
        super(x, y ,"G");
        this.value = 2;
    }

    public int getValue() {
        return value;
    }

}
