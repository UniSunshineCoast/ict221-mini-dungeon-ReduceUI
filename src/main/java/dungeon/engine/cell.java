package dungeon.engine;

import javafx.scene.layout.StackPane;

public class cell extends StackPane {

    private player player;
    private boolean hasLadder;
    private boolean isWalkable;
    private Item item;

    public cell(){
        this.player = null;
        this.hasLadder = false;
        this.isWalkable = true;
        this.item = null;

    }

    public boolean hasPlayer(){
        return player != null;
    }

    public player getPlayer(){
        return player;
    }

    public void setPlayer(player player){
        this.player = player;
    }

    public boolean hasLadder(){
        return hasLadder;
    }

    public void setHasLadder(boolean hasLadder){
        this.hasLadder = hasLadder;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setWalkable(boolean walkable) {
        this.isWalkable = walkable;
    }

    public boolean hasItem(){
        return item != null;
    }

    public Item getItem(){
        return item;
    }

    public void setItem(Item item){
        this.item = item;
    }


}
