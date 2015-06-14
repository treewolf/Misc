import java.util.ArrayList;
import java.util.Collections;

public class TexasHoldem {
    String name="";
    
    //cards
    ArrayList<String>deck;
    ArrayList<String>gameDeck;
    ArrayList<String>player;
    ArrayList<String>comp1;
    ArrayList<String>comp2;
    ArrayList<String>comp3;
    ArrayList<String>table;
    ArrayList<String>alivePlayers;
    //chip stack;
    public int pstack;
    public int c1stack;
    public int c2stack;
    public int c3stack;
    public int pot;
    
    public TexasHoldem(){
        deck=new ArrayList<String>();
        // playing hands have arraylist so non-boundary on list can be used to advantage when scoring
        player=new ArrayList<String>();
        comp1=new ArrayList<String>();
        comp2=new ArrayList<String>();
        comp3=new ArrayList<String>();
        table=new ArrayList<String>();
        alivePlayers=new ArrayList<String>();

        deck.add("2s"); deck.add("2d"); deck.add("2c"); deck.add("2h");
        deck.add("3s"); deck.add("3d"); deck.add("3c"); deck.add("3h");
        deck.add("4s"); deck.add("4d"); deck.add("4c"); deck.add("4h");
        deck.add("5s"); deck.add("5d"); deck.add("5c"); deck.add("5h");
        deck.add("6s"); deck.add("6d"); deck.add("6c"); deck.add("6h");
        deck.add("7s"); deck.add("7d"); deck.add("7c"); deck.add("7h");
        deck.add("8s"); deck.add("8d"); deck.add("8c"); deck.add("8h");
        deck.add("9s"); deck.add("9d"); deck.add("9c"); deck.add("9h");
        deck.add("10s"); deck.add("10d"); deck.add("10c"); deck.add("10h");
        deck.add("Js"); deck.add("Jd"); deck.add("Jc"); deck.add("Jh");
        deck.add("Qs"); deck.add("Qd"); deck.add("Qc"); deck.add("Qh");
        deck.add("Ks"); deck.add("Kd"); deck.add("Kc"); deck.add("Kh");
        deck.add("As"); deck.add("Ad"); deck.add("Ac"); deck.add("Ah"); 
        
        gameDeck=new ArrayList<String>(deck);
        
        pstack=10000; c1stack=10000; c2stack=10000; c3stack=10000; pot=0;
        alivePlayers.add("player"); alivePlayers.add("comp1"); alivePlayers.add("comp2"); alivePlayers.add("comp3");
    }
    public void setName(String name){
        this.name=name;
    }
    public void dispenseCards(){//one card to each player all around, then deals next
        player.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        comp1.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        comp2.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        comp3.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        player.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn(); 
        comp1.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        comp2.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        comp3.add(gameDeck.get(0));
        gameDeck.remove(gameDeck.get(0));
        burn();
        //burn card after
        burn();
    }
    
    public void shuffleDeck(){
        int i=0;
        ArrayList<String>temp=new ArrayList<String>(gameDeck);
        while(i<30){
            Collections.shuffle(temp);
            i++;
        }
        gameDeck=temp;
    }
    
    public void reset(){
        player.clear();
        comp1.clear();
        comp2.clear();
        comp3.clear();
        table.clear();
        alivePlayers.clear();
        alivePlayers.add("player"); alivePlayers.add("comp1"); alivePlayers.add("comp2"); alivePlayers.add("comp3");
        gameDeck=new ArrayList<String>(deck);
        pot=0;
    }
    public String winner(int name){
        String n="";
        if(name==0){
            n=getName();
            pstack+=pot;
        }
        if(name==1){
            n="computer1";
            c1stack+=pot;
        }
        if(name==2){
            n="computer2";
            c2stack+=pot;
        }
        if(name==3){
            n="computer3";
            c3stack+=pot;
        }
        return n;
    }
    public void flop(){
        table.add(gameDeck.get(0));
        gameDeck.remove(0);
        table.add(gameDeck.get(0));
        gameDeck.remove(0);
        table.add(gameDeck.get(0));
        gameDeck.remove(0);
        burn();
    }
    public void turn(){
        table.add(gameDeck.get(0));
        gameDeck.remove(0);
        burn();
    }
    public void river(){
        table.add(gameDeck.get(0));
        gameDeck.remove(0);
    }
    private void burn(){
        gameDeck.remove(0);
    }
    public void fold(String user){
        if(user.equals("player")&&user.contains(user))
            alivePlayers.remove("player");
        else if(user.equals("c1")&&user.contains(user))
            alivePlayers.remove("comp1");
        else if(user.equals("c2")&&user.contains(user))
            alivePlayers.remove("comp2");
        else if(user.equals("c3")&&user.contains(user))
            alivePlayers.remove("comp3");   
        else{}
    }
    //process hand, see how powerful your current hand is, returns an int corresponding to powerlevel// only at the end of round
    public int processHand(String name){

        ArrayList<String>current=new ArrayList<String>();
        //current.clear();
        current=new ArrayList<String>(table);
        int len, x=0;
        if(name.equals("player")){
            current.add(player.get(0)); current.add(player.get(1));
            x= powerLevelis(current,"player");
        }
        else if(name.equals("c1")){
            current.add(comp1.get(0)); current.add(comp1.get(1));
            x= powerLevelis(current,"comp1");
        }
        else if(name.equals("c2")){
            current.add(comp2.get(0)); current.add(comp2.get(1));
            x= powerLevelis(current,"comp2");
        }
        else if(name.equals("c3")){
            current.add(comp3.get(0)); current.add(comp3.get(1));
            x= powerLevelis(current,"comp3");
        }
        else{}
        return x;
    }

    private boolean onePair(ArrayList<String> opt){
        boolean one=false;
        int x=2;
        if(x<100){
            int[]count=new int[15];
            ArrayList<Integer> pair=new ArrayList<Integer>(7);
            for(String c:opt)
                pair.add(numOnly(c));
            for(int i=0; i<7; i++){
                count[pair.get(i)]++;
            }
            for(int c:count){
                if(c>1 && one==false)
                    one=true;
            }
            x+=100;
        }
        return one;
    }
    private boolean twoPair(ArrayList<String> opt){
        boolean one=false;
        boolean two=false;
        int x=2;
        if(x<100){
            int[]count=new int[15];
            ArrayList<Integer> pair=new ArrayList<Integer>(7);
            for(String c:opt)
                pair.add(numOnly(c));
            for(int i=0; i<7; i++){
                count[pair.get(i)]++;
            }
            int real=0;
            for(int c:count){
                if(c>1){
                    one=true;
                    real++;                    
                }
            }
            if(real>1)
                two=true;
            x+=100;
        }
        return two;
    }
    private boolean threeKind(ArrayList<String> opt){
        ArrayList<Integer>ofKind=new ArrayList<Integer>();
        int[] count=new int[15];
        boolean tres=false;
        for(String c:opt)
            ofKind.add(numOnly(c));
        for(int i=0; i<7; i++)
            count[ofKind.get(i)]++;
        for(int c:count){
            if(c>2 && tres==false)
                tres=true;
        }
        return tres;
    }
    private boolean fourKind(ArrayList<String> opt){
        ArrayList<Integer>ofKind2=new ArrayList<Integer>();
        int[] count=new int[15];
        boolean cuat=false;
        for(String c:opt)
            ofKind2.add(numOnly(c));
        for(int i=0; i<7; i++)
            count[ofKind2.get(i)]++;
        for(int c:count){
            if(c>3 && cuat==false)
                cuat=true;
        }
        return cuat;
    }
    private boolean straight(ArrayList<String> opt){
        ArrayList<Integer>strate=new ArrayList<Integer>();
        int[] count=new int[15];
        boolean isStra=false;
        for(String c:opt)
            strate.add(numOnly(c));
        //puts in ascending order
        for(int i=0; i<strate.size();i++){
            for(int p=1; p<strate.size(); p++){
                if(strate.get(p)<strate.get(i)){
                    int temp=strate.get(i);
                    strate.set(i, strate.get(p));
                    strate.set(p, temp);
                }
            }
        }
        int[] rank=new int[15];
        for(int x:strate)
            rank[x]++;
        
        //looks for straight pattern
        int straight=0;
        boolean hasAce=false;
        for(int x:strate){
            if(x==14)
                hasAce=true;
        }
        for(int j=0; j<strate.size()-4; j++){
            if( rank[j]>0 && rank[j+1]>0 && rank[j+2]>0 && rank[j+3]>0 && rank[j+4]>0 )
                straight++;
            else if(rank[2]>0 && rank[3]>0 && rank[4]>0 && rank[5]>0 && hasAce)
                straight++;
            else{}
        }
        if(straight>0)
            isStra=true;
        return isStra;
    }
    private boolean flush(ArrayList<String> opt){
        ArrayList<String>sign=new ArrayList<String>();
        int[] count=new int[15];
        boolean fl=false;
        for(String c:opt)
                sign.add(suitOnly(c));
        int s=0, h=0, d=0, c=0;
        for(String fan:sign){
            if(fan.equals("h"))
                h++;
            else if(fan.equals("s"))
                s++;
            else if(fan.equals("d"))
                d++;
            else if(fan.equals("c"))
                c++;
            else{}
        }
        if(s>=5 || h>=5 || d>=5 || c>=5)
            fl=true;
        return fl;
    }
    private boolean fullHouse(ArrayList<String> opt){ //---------------------edit------------------------------
        int[]strate=new int[20], strate2=new int[20];
        ArrayList<Integer> str=new ArrayList<Integer>(7);
        boolean full=false, hasThree=false, hasPair=false;
        for(String x:opt)
            str.add(numOnly(x));
        
        for(int i=0; i<7; i++)
            strate[str.get(i)]++;
       
        int num=0;
        for(int x:strate){
            if(x==3){
                num=x;
                hasThree=true;
            }
        }
        if(hasThree){
            for(int x:str){
                if(x==num)
                    x=0;
            }
            for(int x:str){
                if(x!=0)
                    strate2[x]++;
                else
                    {}
            }
            for(int y:strate2){
                if(y==2)
                    hasPair=true;
                else if(hasPair==true && y!=2)
                    {}
            }
        }
        if(hasThree && hasPair)
            full=true;
        return full;
    }
    private boolean straightFlush(ArrayList<String> opt){
        boolean strfl=false;
        if(straight(opt)&&flush(opt)) //-----------------------------------edit ---------------------------
            strfl=true;
        return strfl;
    }
    // returns int after booleans are processed
    private int powerLevelis(ArrayList<String> opt, String name){
        if(!alivePlayers.contains(name))
            return 0;
        else 
            return highest(opt);
    }
       
    public int highest(ArrayList<String> x){
        if(straightFlush(x))
            return 9;
        else if(fourKind(x))
            return 8;
        else if(fullHouse(x))
            return 7;
        else if(flush(x))
            return 6;
        else if(straight(x))
            return 5;
        else if(threeKind(x))
            return 4;
        else if(twoPair(x))
            return 3;
        else if(onePair(x))
            return 2;
        else
            return 1;
    }
   
    public String powerName(int lvl){
        String x="test";
        if(lvl==1)
            x= "Wins with high card";
        
        else if(lvl==2)
            x= "Wins with one pair";
        
        else if(lvl==3)
            x= "Wins with two pairs";
        
        else if(lvl==4)
            x= "Wins with three of a kind";
        
        else if(lvl==5)
            x= "Wins with a straight";
        
        else if(lvl==6)
            x= "Wins with a flush";
        
        else if(lvl==7)
            x= "Wins with a full house";
        
        else if(lvl==8)
            x= "Wins with four of a kind";
        
        else
            x= "Wins with straight flush";
        
        return x;
    }

    private int numOnly(String val){
        if(val.substring(0,1).equals("J"))
            return 11;
        else if(val.substring(0,1).equals("Q"))
            return 12;
        else if(val.substring(0,1).equals("K"))
            return 13;
        else if(val.substring(0,1).equals("A"))
            return 14;
        else return Integer.parseInt(val.substring(0,val.length()-1));
    }
    private String suitOnly(String val){
        return val.substring(val.length()-1);
    }
    public void call(int amt, int cost){
        if(amt==0)
            fold("player");
        else if(verifyUser()==false)
            {}
        else
            bet(cost);
        if(cost>c1stack-50)
            fold("comp1");
        else if(cost>=c1stack)
            bet("c1",c1stack);
        else
            bet("c1",cost);
        if(cost>c2stack-100)
            fold("comp2");
        else if(cost>=c2stack)
            bet("c2",c2stack);
        else
            bet("c2",cost);
        if(cost>c3stack-70)
            fold("comp3");
        else if(cost>=c3stack)
            bet("c3",c3stack);
        else
            bet("c3",cost);
    }
    public void bet(int amount){
        if(amount>pstack)
            amount=pstack;
        else if(amount==0)
            fold("player");
        else{}
        pstack-=amount;
        pot+=amount;
    }

    public String bet(String user){
        int range=(100 - 5)+1, betting=0;
        String name="";
        if(user.equals("c1")&&c1stack>0){
            betting=(int)(Math.random()*range)+5;
            if(betting>c1stack)
                betting=c1stack;
            c1stack-=betting;
            name="computer1";
        }
        else if(user.equals("c2")&&c2stack>0){
            betting=(int)(Math.random()*range)+5;
            if(betting>c2stack)
                betting=c2stack;
            c2stack-=betting;
            name="computer2";
        }
        else if(user.equals("c3")&&c3stack>0){
            betting=(int)(Math.random()*range)+5;
            if(betting>c3stack)
                betting=c3stack;
            c3stack-=betting;
            name="computer3";
        }
        else
            betting=0;
        pot+=betting;
        return name+ " tossed in " + betting;
    }
    
    private void bet(String user, int amount){
        if(user.equals("c1")&&c1stack>0)
            c1stack-=amount;
        if(user.equals("c2")&&c2stack>0)
            c2stack-=amount;
        if(user.equals("c3")&&c3stack>0)
            c3stack-=amount;
        pot+=amount;
    }
    //mandatory ante, will substitute for bb and sb
    public void ante(){
        bet(5);
        bet("c1", 5);
        bet("c2", 5);
        bet("c3", 5);
    }
    
    public String getStats(int x){
        String h="";
        if(x==0)
            h+=player.get(0)+" " + player.get(1)+"     " + pstack;
        if(x==1)
            h+=comp1.get(0)+" " + comp1.get(1)+"     " + c1stack;
        if(x==2)
            h+=comp2.get(0)+ " " + comp2.get(1)+"     " + c2stack;
        if(x==3)
            h+=comp3.get(0)+" " + comp3.get(1)+"     " + c3stack;
        return h;
    }
    public String getPot(){
        return "Pot: " + pot;
    }
    public String getName(){
        return name;
    }
    public ArrayList<String> getTable(){
        return table;
    }
    
    public boolean verifyUser(){
        if(!alivePlayers.contains("player"))
            return false;
        else
            return true;
    }
    
    
    /*
     * for testing purposes
     */
    public ArrayList<String> getDeck(){
        return deck;
    }
    public ArrayList<String> getGD(){
        return gameDeck;
    }
    public ArrayList<String> getAlive(){
        return alivePlayers;
    }

    /*
     * Order from least to highest suits:
     * 
     * clubs, diamonds, hearts, spades
     * ______________________________________
     *
     * hand ranks, highest to least:
     * 
     *9  straight flush 
     *8  four kind
     *7  full house
     *6  flush
     *5  straight
     *4  three kind
     *3  two pair
     *2  one pair
     *1  high card
     *
     * 
     */
    
}
