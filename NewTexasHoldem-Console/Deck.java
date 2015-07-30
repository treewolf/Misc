/**
 * Deck class is what deck is represented by.
 */
public class Deck {
    private java.util.Stack<String> deck;
    private final java.util.Stack<String> NEW_DECK;
    //stacks a new deck.
    public Deck(){
        NEW_DECK = new java.util.Stack<>();
        for(int value = 1; value < 10; value++){
            NEW_DECK.push((value + 1) + "D");
            NEW_DECK.push((value + 1) + "C");
            NEW_DECK.push((value + 1) + "H");
            NEW_DECK.push((value + 1) + "S");
        }
        NEW_DECK.push(("JD")); NEW_DECK.push(("JC")); NEW_DECK.push(("JH")); NEW_DECK.push(("JS"));
        NEW_DECK.push(("QD")); NEW_DECK.push(("QC")); NEW_DECK.push(("QH")); NEW_DECK.push(("QS"));
        NEW_DECK.push(("KD")); NEW_DECK.push(("KC")); NEW_DECK.push(("KH")); NEW_DECK.push(("KS"));
        NEW_DECK.push(("AD")); NEW_DECK.push(("AC")); NEW_DECK.push(("AH")); NEW_DECK.push(("AS"));
        
        deck = NEW_DECK;
        shuffle(deck);
    }
    
    //public methods.
    
    public void burnCard(){
        deck.pop();
    }
    
    //returns Stack deck.
    public java.util.Stack<String> getDeck(){
        return deck;
    }
    
    //returns String of deck.
    @Override
    public String toString(){
        String temp = "";
        for(String x : deck){
            temp += x + " ";
        }
        //minus one from string to erase the space.
        return temp.substring(0, temp.length()-1);
    }
    
    //private methods   .
    
    //this will shuffle a new deck.
    private void shuffle(java.util.Stack<String> deck){
        java.util.Stack<String> tempDeck = deck;
        //convert to arrayList for Collections.shuffle().
        java.util.ArrayList<String> listone = new java.util.ArrayList<>();
        java.util.ArrayList<String> listtwo = new java.util.ArrayList<>();
        for(int count = 0; count < 10; count++){
            java.util.Collections.shuffle(tempDeck);
        }
        for(int i = 0; i < 26; i++){
            listone.add(tempDeck.pop());
        }
        for(int i = 0; i < 26; i++){
            listtwo.add(tempDeck.pop());
        }
        int shufC = 10;
        while(shufC != 0){
            java.util.Collections.shuffle(listone);
            java.util.Collections.shuffle(listtwo);        
            shufC--;
        }
        //in case of a mathematical error, clears tempDeck.
        tempDeck.clear();
        //adds to stack for deck.
        for(int card = 0; card < 26; card++){
            tempDeck.push(listtwo.get(card));
            tempDeck.push(listone.get(card));
        }
        //error checking
        if(tempDeck.size() == 52){
            deck = tempDeck;
        }
        else{
            System.out.println("Error in Deck.shuffle(Stack<String> st)");
        }        
    }
       
}
