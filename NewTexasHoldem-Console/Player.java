/**
 * Player class makes all players.
 */
public class Player{
    
    private final String name;
    private String[] hand = new String[5];
    private boolean stillPlaying = true;
    
    // public methods
    
    public Player(String name){
        this.name = name;
    }
    
    //returns type string value of player's name
    public String getName(){
        return name;
    }
    
    //returns String[] type. use java.util.Arrays.
    public String[] getHand(){
        return hand;
    }
    
    public boolean stillPlaying(){
        return stillPlaying;        
    }
    
    //private methods
}
