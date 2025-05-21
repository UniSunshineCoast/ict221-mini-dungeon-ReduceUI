package dungeon.engine;

import javafx.scene.layout.StackPane;

public class Cell extends StackPane {

    private Player player;
    private boolean hasLadder;

    public Cell(){
        this.player = null;
    }

    public boolean hasPlayer(){
        return player != null;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public boolean hasLadder(){
        return hasLadder;
    }

    public void setHasLadder(boolean hasLadder){
        this.hasLadder = hasLadder;
    }


}
