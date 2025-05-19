import dungeon.engine.GameEngine;
import dungeon.engine.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayer {
    @Test
    void testPlayer(){
        Player p = new Player(1,1);

        assertEquals(1, p.getX());
        assertEquals(1, p.getY());
        assertEquals("P", p.getSymbol());

        p.setX(3);
        p.setY(4);
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());

    }
}
