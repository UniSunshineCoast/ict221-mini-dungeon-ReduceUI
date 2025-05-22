
import dungeon.engine.Gold;
import dungeon.engine.Heal;
import dungeon.engine.Trap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItem {

    @Test
    void TestGoldGetValue() {
        Gold gold = new Gold(5 ,5);

        int goldValue = gold.getValue();
        assertEquals(2, goldValue);
    }

    @Test
    void TestTrapGetDamage() {
        Trap trap = new Trap(5, 5);

        int trapValue = trap.getDamage();
        assertEquals(2, trapValue);
    }

    @Test
    void TestHealgetHealValue() {
        Heal heal = new Heal(5, 5);

        int healValue = heal.getHealValue();
        assertEquals(4, healValue);
    }

}
