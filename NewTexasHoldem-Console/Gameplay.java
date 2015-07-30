/**
 * This is the main program.
 */

class Gameplay{
    
    //Declares global variables.
    static Player ai_1;
    static Player ai_2;
    static Player ai_3;
    static Player human;
    
    public static void main(String[] args){
        java.util.Scanner kb = new java.util.Scanner(System.in);
        System.out.println("Hello, you will be playing against 3 other players.\n" +
                "What is your name? ");
        String name = kb.next();
        //Makes Player instance and sets names.
        human = new Player(name);
        chooseAiNames();
        //initates new deck, automatically shuffled - look at deck.construct().
        Deck deck = new Deck();
        System.out.println("\nThe players for today's poker tournmament are: " + human.getName() +
                ", " + ai_1.getName() + ", " + ai_2.getName() + ", and " + ai_3.getName());

    }
    
    //picks random names for computer players.
    private static void chooseAiNames(){
        //able to add more names without changing anything, method is dynamic.
        String[] names = {"Daniel", "Chris", "Liv", "Kate", "Ben", "Kevin", "Leah", "Ryan", "Barry", "Lisa",
            "Anna", "Dorothy", "Michael", "Michelle", "Zak", "Carol", "Jordan", "Oscar", "Phil", "Antonio",
            "Doyle", "Vanessa", "Jennifer", "John", "Howard"};
        //Because default string[] is null, cannot compare a string to a null value, must preset to an empty string.
        String[] picked = {"", "", ""};
        for(int i = 0; i < picked.length; i++){
            java.util.Random r = new java.util.Random();
            int index = r.nextInt((names.length-1) - 0 + 1) + 0; 
            if(!picked[0].equals(names[index]) && !picked[1].equals(names[index]) && !picked[2].equals(names[index]) && 
                    (!(human.getName().toLowerCase()).equals(names[index]))){
                picked[i] = names[index];
            }
            else{
                i--;
            }
        }
        ai_1 = new Player(picked[0]);
        ai_2 = new Player(picked[1]);
        ai_3 = new Player(picked[2]);
    }
}
