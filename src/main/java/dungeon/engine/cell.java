package dungeon.engine;

import javafx.scene.layout.StackPane;

public class cell extends StackPane {

    private player player;
    private boolean hasLadder;
    private boolean isWalkable;

    public cell(){
        this.player = null;
        this.hasLadder = false;
        this.isWalkable = true;

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


}
