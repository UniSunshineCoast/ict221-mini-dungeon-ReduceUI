package dungeon.engine;

import java.util.Random;

public class Ranged extends Enemy {

    public Ranged(int x, int y) {
        super(x, y, 2, " R ", 2);
    }

    @Override
    public void move(map map, player player) {
        // range doesn't move, but it can attack
        Random rand = new Random();
        int hitChance;
        int dx = Math.abs(player.getX() - this.x);
        int dy = Math.abs(player.getY() - this.y);

        if ((dx == 0 && dy <= 2) || (dx == 2 && dy <= 0)) {
            hitChance = rand.nextInt(2);
            if (hitChance == 0) {
                player.takeDamage(getAttackDamage());
                System.out.println("A ranged mutant attacked, and you lost " + getAttackDamage() + " HP.");
            } else {
                System.out.println("A ranged mutant attacked, but missed.");
            }
        }

    }
}