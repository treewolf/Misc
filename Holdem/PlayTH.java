import java.util.Scanner;

public class PlayTH extends TexasHoldem{
    public static void main(String[] args){
        Scanner kb=new Scanner(System.in);
        boolean playerWantsToPlay=true;
        String c1;
        String c2;
        String c3;
        
        System.out.println("+========================+");
        System.out.println("||  Texas Hold'em Lite  ||");
        System.out.println("+========================+");

        //instructions
        String welcome="Welcome to Texas Hold'em Lite, the world's number one poker game that has a twist." + 
        "\nThis is a fun and possibly addicting game. Today, you can play for free.";
        String explainProcess="\nFirst, you will be dealt 2 cards, called your hand. Then as you bet, there will be five cards on the table." +
        "\nThese five cards can be matched to one or both of your cards." +
        "\nThe point is to get certain combinations of card hands."+
        "\nYou have three adversaries, of whom you won't know their cards, and vice versa. You will find out at the end of the round.";
        String explainRules="\nThe following lists the possible combinations from highest to lowest: \n"+
        "\nStraight Flush\n   When you have a hand with 5 consecutive values and all have the same suit."+
        "\nFour of a Kind\n   When you have a 4 cards with the same number value."+
        "\nFull House\n   When you have a pair and a three of a kind."+
        "\nFlush\n   When you have 5 cards in the same suit."+
        "\nStraight\n   When you have 5 consecutive values."+
        "\nThree of a Kind\n   When you have 3 cards with the same number value."+
        "\nTwo Pairs\n   When you have two pairs of value matching cards."+
        "\nOne Pair\n   When you have two cards with the same number value."+
        "\nHigh Card\n   When you don't meet any of the other hand combos, then you have the highest number value from your hand."+
        "\n\nThe card rating will go in order from the human player to computer3. If there is a tie of hands, the player closest"+
        "\nto the front(also known as the dealer) will win the round." + " Another twist of this game is that you can bet ANY amount"+
        "\ninto the pot and still win. The downside is that all players are required to call every time the table acquires new cards."+
        "\nThis will require either luck or patience to beat your opponents!";
        String ready="\nPress Enter to start game. . .";
        
        System.out.println(welcome);
        System.out.println(" --Press Enter to continue--");
        kb.nextLine();
        System.out.println(explainProcess);
        System.out.println(" --Press Enter to continue--");
        kb.nextLine();
        System.out.println(explainRules);
        System.out.println(" --Press Enter to continue--");
        kb.nextLine();
        System.out.println(ready);
        kb.nextLine();
        
        TexasHoldem game= new TexasHoldem();
        
        System.out.println("What is your name? ");
        game.setName(kb.nextLine());
        int range=10;
        int r= (int)(Math.random()*range)+1;
        do{

            game.shuffleDeck();
            game.dispenseCards();
            System.out.println("\nAll players ante 5 chips");
            game.ante();
            
            System.out.println("\n"+game.getStats(0));
            System.out.println("Place your bet: ");
            game.bet(kb.nextInt());
            System.out.println(game.bet("c1"));
            System.out.println(game.bet("c2"));
            System.out.println(game.bet("c3"));
            
            game.flop();
            System.out.println("\nYour hand: " +  game.getStats(0));
            System.out.println(game.getTable() + "    " + game.getPot());
            if(game.verifyUser()){
                System.out.println("Place your bet: ");
                game.bet(kb.nextInt());
            }
            
            System.out.println(game.bet("c1"));
            System.out.println(game.bet("c2"));
            System.out.println(game.bet("c3"));
            
            if(r%5==1){
                game.call(50, 50);
                System.out.println("All players called " + 50);
            }
            else if(r%2==1){
                game.call(10,10);
                System.out.println("All players called " + 10);
            }
            else{
                game.call(50, 50);
                System.out.println("All players called " + 50);
            }
            if(game.verifyUser()){
                System.out.println("\nRaise or enter 0 to fold: ");
                int ans=kb.nextInt();
                game.call(ans, ans);
                if(ans==0)
                    game.fold("player");
                else{
                    game.call(ans, ans);
                    System.out.println("All players called " + ans);
                }
            }
            else
            {}
            
            
            game.turn();
            System.out.println("\nYour hand: "+ game.getStats(0));
            System.out.println("Table is: " +game.getTable() + "    " + game.getPot());
            
            if(game.verifyUser()){
                System.out.println("Place your bet: ");
                game.bet(kb.nextInt());
            }
            else{}
            
            System.out.println(game.bet("c1"));
            System.out.println(game.bet("c2"));
            System.out.println(game.bet("c3"));
            
            if(r%2==0){
                game.call(50, 50);
                System.out.println("All players called " + 50);
            }
            else if(r%5==4){
                game.call(10,10);
                System.out.println("All players called " + 10);
            }
            else{
                game.call(50, 50);
                System.out.println("All players called " + 50);
            }
            if(game.verifyUser()){
                System.out.println("\nRaise or enter 0 to fold: ");
                int ans=kb.nextInt();
                if(ans==0)
                    game.fold("player");
                else{
                    game.call(ans, ans);
                    System.out.println("All players called " + ans);
                }
            }
            else
            {}
            
            game.river();
            System.out.println("\nYour hand: " + game.getStats(0));
            System.out.println("Table is: " +game.getTable() + "    " + game.getPot());
            
            if(game.verifyUser()){
                System.out.println("Place your bet: ");
                game.bet(kb.nextInt());
            }
            else{}
            
            System.out.println(game.bet("c1"));
            System.out.println(game.bet("c2"));
            System.out.println(game.bet("c3"));
            
            if(r%7==1){
                game.call(50, 50);
                System.out.println("All players called " + 50);
            }
            else if(r%3==0){
                game.call(10,10);
                System.out.println("All players called " + 10);
            }
            else if(r%2==0){
                game.call(50, 50);
                System.out.println("All players called " + 50);
            }
            else{
                game.call(20,20);
                System.out.println("All players called " + 20);
            }
            if(game.verifyUser()){
                System.out.println("\nRaise or enter 0 to fold: ");
                int ans=kb.nextInt();
                if(ans==0)
                    game.fold("player");
                else{
                    game.call(ans, ans);
                    System.out.println("All players called " + ans);
                }
            }
            else
            {}
            
            int[]score=new int[4];
            if(game.getTable().size()<5)
                System.out.println("Error, you hacked the game. starting over...");
            else{
                score[0]=game.processHand("player"); 
                score[1]=game.processHand("c1");
                score[2]=game.processHand("c2");
                score[3]=game.processHand("c3");
                //determine highest score out of players
                int pc=0, n=0;
                for(int i=0; i<score.length; i++){
                    if(score[i]>n){
                        n=score[i];
                        pc=i;
                    }
                }
                System.out.println("---------------------------------------------------------------------");
                System.out.println("\n"+game.winner(pc)+ " gains " + game.getPot()+". "+ game.powerName(n));
                System.out.println(game.getStats(0));
                System.out.println(game.getStats(1));
                System.out.println(game.getStats(2));
                System.out.println(game.getStats(3));
                System.out.println(game.getTable() + " : Table");
            }
            if(game.pstack==0){
                game.pstack=10000;
                game.c1stack=10000;
                game.c2stack=10000;
                game.c3stack=10000;
                System.out.println("\n\nSorry, you have gone broke!");
            }
            
            boolean ask=true;
            do{
                //ask if still want to play
                System.out.println("____________________________________________________________________");
                System.out.println("Do you still want to play?  y/n");
                String answer=kb.next();
                if(answer.equalsIgnoreCase("y")){
                    playerWantsToPlay=true;
                    game.reset();
                    ask=false;
                }
                else if(answer.equalsIgnoreCase("n")){
                    playerWantsToPlay=false;
                    ask=false;
                }
                else ask=true;
            }while(ask==true);
        }while(playerWantsToPlay);
        
        System.out.println("______________________________________________" + 
        "\nThank you for playing Texas Hold'em Lite, " + game.getName() +"!");
        
        
    }
}
