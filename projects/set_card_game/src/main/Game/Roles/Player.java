package Roles;

import Admin.Env;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This class manages the players' threads and data
 *
 * @inv id >= 0
 * @inv score >= 0
 */
public class Player implements Runnable {


    // --------------------------- Variables ---------------------------------------------------------------------------------------------------------------------


    private final Env env;
    protected final Table table;
    public final int id; // player's id
    protected final int no_Rush = 5; // player's id
    private Thread playerThread;
    private Thread aiThread;
    private final boolean human; // human player (true) or AI (false)
    private final long OneSec = 1000; // represents 1 second
    private final long OneTenthSec = 100; // represents 1/10 second

    private volatile boolean terminate; // terminate thread variable
    protected final int maxCardsOnTable = 12;
    private int score; // player's score
    private ArrayBlockingQueue<Integer> incomingActions; // incoming slots by order of key presses
    private boolean freeze; // = true when should be freezed or frozen
    private boolean melted; // = true after done with freeze
    protected final int maxTokens = 3; // max num of tokens
    private volatile ArrayBlockingQueue<Integer> tokens; // arraylist of size maxTokens, each value represents the slot of the token (-1 means token not placed)
    private volatile boolean interrupted; // player can not play till something happens
    private Random random;



    // --------------------------- Constructor -------------------------------------------------------------------------------------------------------------------


    public Player(Env env, Dealer dealer, Table table, int id, boolean human) {
        this.env = env;
        this.table = table;
        this.id = id;
        this.human = human;
        this.score = 0;
        this.interrupted = false;
        this.freeze = false;
        this.melted = false;
        this.tokens = new ArrayBlockingQueue<>(maxTokens);
        this.incomingActions = new ArrayBlockingQueue<>(maxTokens);
        this.random = new Random();
        initTokens();
    }



    // --------------------------- Interrupt related functions ---------------------------------------------------------------------------------------------------


    public synchronized boolean isInterrupted() { return interrupted; } // return the value of interrupted
    public synchronized void interrupt() { interrupted = true; } // interrupt this player
    public synchronized void resume() { interrupted = false; notify(); } // resume this player



    // --------------------------- Tokens related functions ------------------------------------------------------------------------------------------------------


    protected void initTokens() // initialize tokens array with "maxTokens" times -1 (fresh start with no tokens placed on table)
    {
        while (!tokens.isEmpty())
            table.removeToken(id, tokens.remove());
    }
    protected void removeAllTokens()
    {
        while (!tokens.isEmpty()) {
            AI_noRush(no_Rush);
            if(tokens.isEmpty())
                break;
            table.removeToken(id, tokens.remove());
        }
        AI_noRush(2*no_Rush);
    }

    protected Boolean tokenExists(int slot) // returns if a token of this player is placed on the table
    {
        return tokens.contains(slot);
    }
    protected Boolean addT(int slot) // add token slot to the tokens array if possible
    {

        if(tokens.size()<maxTokens && table.slotToCard[slot] != -1) {
            tokens.add(slot);
            return true;
        }

        return false;
    }
    protected void removeT(Integer slot) // removes token slot from the tokens array
    {
        tokens.remove(slot);
        if(melted)
            setMelted(false);
    }
    private void trioCheck() // set the id of this player as the trioID (in case he has 3 placed tokens)
    {

        if(tokens.size() == maxTokens && (!melted || !human) && !table.trioIDs.contains(id))
            table.trioIDs.add(id);

    }
    protected ArrayBlockingQueue<Integer> getTokens() // tokens getter
    { return tokens; }
    public int[] tokensToCards() // returns integer array which represents "tokens" cards.
    {
            int[] cards = new int[tokens.size()];
            int i = 0;
                for(Integer slot : tokens) {
                    cards[i] = table.slotToCard[slot];
                    i++;
                }
        return cards;
    }



    // --------------------------- Freeze-Related functions -------------------------------------------------------------------------------------------------------

    
    public void freezeCheck() // checks when should freeze
    {
        if(freeze)
            freezeNow();
    }
    public void freeze() // init variable freeze = true
    {
        freeze = true;
    }
    public void unFreeze() // init variable freeze = false
    {
        freeze = false;
        setMelted(true);
        if(!human)
            removeAllTokens();


    }
    private void freezeNow() // freeze action
    {
        interrupt();

        for (int i = maxTokens; i > 0; i--)
        {
            env.ui.setFreeze(id, i * OneSec);
            try {Thread.currentThread().sleep(OneSec);}
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        env.ui.setFreeze(id, -1);
        unFreeze();

        resume();
    }
    private void setMelted(boolean state) // set variable melted based on given state
    { melted = state; }



    // --------------------------- IncomingAction-Related functions --------------------------------------------------------------------------------------------------


    private void addIncomingAction(int slot) // add chosen slot
    {
        if(incomingActions.size() < maxTokens)
            incomingActions.add(slot);

    }
    private void action() // add/remove token from table based on IncomingAction
    {
        if(ready())
        {
            int newSlot = incomingActions.remove();

            if (tokenExists(newSlot))
            {
                table.removeToken(id, newSlot);
                removeT(newSlot);
            }
            else if (addT(newSlot))
                table.placeToken(id, newSlot);

        }

    }
    private boolean ready() // ready to action
    {
        return !incomingActions.isEmpty();
    }



    // --------------------------- Threads main loops Functions --------------------------------------------------------------------------------------------------------------------


    @Override
    public void run() // main loop of player
    {
        playerThread = Thread.currentThread();
        env.logger.info("thread " + Thread.currentThread().getName() + " starting.");

        if (!human) createArtificialIntelligence();

        while (!terminate)
        {
            trioCheck();
            freezeCheck();
            action();
        }

        if (!human) try { aiThread.join();} catch (InterruptedException ignored) {}
        env.logger.info("thread " + Thread.currentThread().getName() + " terminated.");
    }
    private void createArtificialIntelligence() // create thread for AI, replacing human choices.
    {
        aiThread = new Thread(() -> {
            env.logger.info("thread " + Thread.currentThread().getName() + " starting.");
            AI_noRush(30);
            while (!terminate)
            {
                AI_action(10); //! level dificulty 1 --> 10 !

                try { synchronized (this) { wait(); } }
                catch (InterruptedException ignored) {}
            }
            env.logger.info("thread " + Thread.currentThread().getName() + " terminated.");
        }, "computer-" + id);
        aiThread.start();
    }



    // --------------------------- AI Action Functions --------------------------------------------------------------------------------------------------------------------


    private void AI_action(int Difficulty_Level) // preform trio choice by AI
    {
        if(!isInterrupted())
        {
            if (Difficulty_Level < 1 || Difficulty_Level > 10)
                throw new IllegalArgumentException("enter level between 1-10");

            int[] trio;

            if (AI_IsCheater(random.nextInt((11-Difficulty_Level) * 10), Difficulty_Level))
                trio = AI_validTrio();
            else
                trio = AI_randomTrio();


            if (trio != null && !AI_trioContainsSlot(trio, -1) && trio.length == maxTokens)
                AI_keyPressedTiming(trio);
        }
    }
    private void AI_keyPressedTiming(int[] trio) // AI "press" trio keys
    {
        for (int i = 0; i<maxTokens ; i++)
        {
            int pause = 7*(random.nextInt(5) + 1);
            AI_noRush(pause);
            AI_keyPressed(trio[i]);
        }
    }

    private boolean AI_IsCheater(int randomNumber, int Difficulty_Level) // returns true when AI should cheat
    {
        int cheatFactor = 20-Difficulty_Level;
        if(cheatFactor > randomNumber)
            return true;
        return false;
    }


    // --------------------------- AI Maintenance Functions --------------------------------------------------------------------------------------------------------------------


    private void AI_noRush(long sec) // sleep for "sec" seconds
    {
        try {Thread.currentThread().sleep(sec*OneTenthSec);}
        catch (InterruptedException ignored){}
    }
    private int[] AI_randomTrio() // returns random trio
    {
        int[] trio = new int[maxTokens];
        for (int i = 0; i < trio.length; i++)
            trio[i] = -1;

        for (int i = 0; i < trio.length; i++) {

            boolean added = false;
            int x = random.nextInt(maxCardsOnTable);
            if( !AI_trioContainsSlot(trio,x) || table.slotEmpty(x))
            {
                trio[i] = x;
                added = true;
            }

            while ( !added && !terminate) {

                x = random.nextInt(maxCardsOnTable);
                if( !AI_trioContainsSlot(trio,x) || table.slotEmpty(x))
                {
                    trio[i] = x;
                    added = true;
                }
            }
        }

        return trio;
    }
    private boolean AI_trioContainsSlot(int[] trio, int slot) // return's true when trio already has "slot"
    {
        if (trio[0] == slot)
            return true;
        if (trio[1] == slot)
            return true;
        if (trio[2] == slot)
            return true;
        return false;
    }
    private int[] AI_validTrio() // finds a valid set in the table
    {
        ArrayList<Integer> tableCards = new ArrayList<>(table.countCards());
        tableCards = table.addTableCards(tableCards);

        List<int[]> validTrio = env.util.findSets(tableCards,1);

        if(validTrio.size() == 0)
            return null;

        int[] trio = validTrio.get(0);
        for (int i = 0; i < 3 ; i++ )
            trio[i] = table.cardToSlot[trio[i]];
        return trio;

    }



    // --------------------------- Maintenance Functions --------------------------------------------------------------------------------------------------------------------


    public void terminate() // called when game should be terminated
    {
        terminate = true;
        playerThread.interrupt();
        if(!human)
            aiThread.interrupt();
    }
    public void keyPressed(int slot) // when a key is pressed, new IncomingAction is added
    {
        if(!isInterrupted() && human)
            addIncomingAction(slot);
    }

    public void AI_keyPressed(int slot) // when a key is pressed, new IncomingAction is added
    {
        if(!isInterrupted())
            addIncomingAction(slot);
    }
    public void point() // award for the player when a valid set is chosen
    {

        env.ui.setElapsed(1000);

        env.ui.setScore(id, ++score);
    }
    public int score() // score getter
    {
        return score;
    }



}
