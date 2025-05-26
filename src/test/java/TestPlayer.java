import dungeon.engine.player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayer {
    @Test
    void testPlayer(){
        player p = new player(1,1, 8);

        assertEquals(1, p.getX());
        assertEquals(1, p.getY());

        p.setX(3);
        p.setY(4);
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());

    }
}
