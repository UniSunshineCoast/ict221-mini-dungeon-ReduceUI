package dungeon.engine;

import java.util.ArrayList;
import java.util.List;

public class player {
    private int x;
    private int y;
    private int health;

    public player(int x, int y, int instancedHealth) {
        this.x = x;
        this.y = y;
        this.health = instancedHealth;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health > 10) {
            health = 10;
        }
    }

    public List<String> move(int dx, int dy, map map, String direction) {
        List<String> messages = new ArrayList<>();
        int newX = x + dx;
        int newY = y + dy;
        if (map.getCell(newX, newY) != null && map.getCell(newX, newY).isWalkable()) {
            map.getCell(x, y).setPlayer(null);
            x = newX;
            y = newY;
            map.getCell(x, y).setPlayer(this);
            messages.add("You moved " + direction + " one step.");
        } else {
            messages.add("You tried to move " + direction + " one step but it is a wall.");
        }
        return messages;
    }
}
