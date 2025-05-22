package dungeon.engine;

public class Ranged extends Enemy{

    public Ranged(int x, int y){
        super (x, y, 2, " R ", 2);
    }

    @Override
    public void move(map map) {
        // range doesnt move
    }


    // ranged attack


}

