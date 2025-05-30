package dungeon.engine;

import javafx.scene.layout.StackPane;

public class cell extends StackPane {

    private player player;
    private boolean hasLadder;
    private boolean isWalkable;
    private boolean isOccupied;
    private Item item;
    private Enemy enemy;

    public cell(){
        this.player = null;
        this.hasLadder = false;
        this.isWalkable = true;
        this.isOccupied = false;
        this.item = null;
        this.enemy = null;

    }

    //Tracks player location
    public boolean hasPlayer(){
        return player != null;
    }
    public void setPlayer(player player){
        this.player = player;
    }

    //Tracks exit ladder
    public boolean hasLadder(){
        return hasLadder;
    }
    public void setHasLadder(boolean hasLadder){
        this.hasLadder = hasLadder;
    }

    //Tracks walls
    public boolean isWalkable() {
        return isWalkable;
    }
    public void setWalkable(boolean walkable) {
        this.isWalkable = walkable;
    }

    //Tracks items enemies and player
    public boolean isOccupied() {
        return hasPlayer() || hasLadder() || hasItem() || hasEnemy();
    }
    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    //Tracks items - gold traps and healing potions
    public boolean hasItem(){
        return item != null;
    }
    public Item getItem(){
        return item;
    }
    public void setItem(Item item){
        this.item = item;
    }

    //Tracks enemy - melee and range
    public boolean hasEnemy(){
        return enemy !=null;
    }
    public Enemy getEnemy() {
        return enemy;
    }
    public void setEnemy(Enemy enemy){
        this.enemy = enemy;
    }
}
