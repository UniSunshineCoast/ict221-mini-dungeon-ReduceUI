import dungeon.engine.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMap {
    @Test
    void testGetSize() {
        Map map = new Map(2);
        assertEquals(2, map.getSize());
    }

    @Test
    void testGetCell() {
        Map map = new Map(2);
        assertNull(map.getCell(-10, 0));
        assertNotNull(map.getCell(0,1));
        assertNull(map.getCell(0,-1));
    }

//    @Test
//    void testPlaceLadder() {
//        Map map = new Map(10);
//        map.placeLadder();
//        assertEquals(10, map.getSize());
//    }
}
