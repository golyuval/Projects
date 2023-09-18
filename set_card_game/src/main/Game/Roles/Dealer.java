package Roles;

import Admin.Env;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class manages the dealer's threads and data
 */
public class Dealer implements Runnable {


    // --------------------------- Variables --------------------------------------------------------------------------------------------------------------------


    private final Env env;
    private Table table;
    private final Player[] players;
    protected List<Integer> deck;
    private volatile boolean terminate;
    protected final long displayTimerFinal = 300;
    protected final long sleepDuration = 100;
    protected final int maxCardsOnTable = 12;


    protected long displayTimer;
    private Thread dealerThread;



    // --------------------------- Constructor --------------------------------------------------------------------------------------------------------------------

    public Dealer(Env env, Table table, Player[] players) {
        this.env = env;
        this.table = table;
        this.players = players;
        this.displayTimer = displayTimerFinal;
        this.dealerThread = Thread.currentThread();
        deck = IntStream.range(0, env.config.deckSize).boxed().collect(Collectors.toList());
    }


    // --------------------------- main loops Functions --------------------------------------------------------------------------------------------------------------------


    @Override
    public void run() // Game loop
    {

        env.logger.info("thread " + Thread.currentThread().getName() + " starting.");
        startPlayers();
        table.setCapacityIDS(players.length);

        while (!shouldFinish()) {

            shuffleDeck();
            placeCardsOnTable();
            resumeAllPlayers();
            timerLoop();
            updateTimerDisplay(false);
            interruptAllPlayers();
            removeAllCardsFromTable();
            table.initIDS();
        }
        announceWinners();
        env.logger.info("thread " + Thread.currentThread().getName() + " terminated.");
    }
    private void timerLoop() // Round loop
    {

        displayTimer = displayTimerFinal;

        while (!terminate && displayTimer > 0 && !noTrioInTable_check()) {
            noTrioInDeck_check();
            nextTrio_check();
            sleepUntilWokenOrTimeout();
            updateTimerDisplay(displayTimer<=50);
            displayTimer-=1;
        }
    }


    // --------------------------- Action Functions --------------------------------------------------------------------------------------------------------------------


    private void invalidTrio(Player p) // invalid trio was found action
    {
        p.freeze();
    }
    private void validTrio(Player p) // valid trio was found action
    {
        p.point();
        removeValidTrio(p);
        replaceValidTrio(p.getTokens());
        p.initTokens();
        resetTimer();

    }
    private void removeValidTrio(Player player) // remove valid trio from table (and unnecessary tokens)
    {
        for(Player p : players)
            if(p.id != player.id)
                for (Integer token : p.getTokens())
                    if(player.getTokens().contains(token)) {
                        table.removeToken(p.id, token);
                        p.getTokens().remove(token);
                    }

    }
    private void replaceValidTrio(ArrayBlockingQueue<Integer> trio) // replace valid trio with other 3 cards
    {
        for (Integer slot : trio) {
            table.removeCard(slot);
            if(!deck.isEmpty())
                table.placeCard(deck.remove(0),slot);
            else
                table.slotToCard[slot] = -1;
        }
    }



    // --------------------------- Check Functions --------------------------------------------------------------------------------------------------------------------


    private void noTrioInDeck_check() // returns true if there are no legal sets on deck
    {
        Integer cardsInTable = table.countCards();
        ArrayList<Integer> cards = new ArrayList<>(cardsInTable + deck.size());
        cards = table.addTableCards(cards);
        cards = addDeck(cards);

        if (env.util.findSets(cards, 1).size() == 0)
            terminate();
    }
    private boolean noTrioInTable_check() // returns true if there are no legal sets on table
    {
        ArrayList<Integer> cardsOnTable = new ArrayList<>(table.countCards());
        cardsOnTable = table.addTableCards(cardsOnTable);

        return (env.util.findSets(cardsOnTable,1).size() == 0);
    }
    private void nextTrio_check() // next trio check
    {
        if(table.trioIDs.size() > 0)
        {
            synchronized (table)
            {
                interruptAllPlayers();

                int id = table.trioIDs.remove();
                Player p = findPlayer(id);

                if(validation_check(p))
                    validTrio(p);
                else
                    invalidTrio(p);

                table.initIDS();

                resumeAllPlayers();
            }
        }
    }
    protected boolean validation_check(Player p) // trio validation check
    {
        if(p.getTokens().size() == p.maxTokens)
            return env.util.testSet(p.tokensToCards());
        else
            return false;
    }
    private boolean shouldFinish() // returns true if game should be terminated
    {
        System.out.println("SHOULD FINISH "+env.util.findSets(deck,1).size());
        return terminate || setNotFoundConTerminate();
    }
    private boolean setNotFoundConTerminate(){
        if(env.util.findSets(deck, 1).size() == 0) {
            synchronized (this){
            terminate();
            notifyAll();
            }
            return true;
        }
        return false;
    }

    // --------------------------- Player-Related Functions --------------------------------------------------------------------------------------------------------------------


    private void interruptAllPlayers() // interrupt all players
    {
        for(Player p : players)
            p.interrupt();
    }
    private void resumeAllPlayers() // resume all players
    {
        for(Player p : players)
            p.resume();
    }

    // private void errorPlayers(int humanPlayers, int aiPlayers) throws Exception
    // {
    //     if(humanPlayers > 2)
    //         throw new IllegalArgumentException("Can't support more than 2 human players");
    //     if(aiPlayers > 4)
    //         throw new IllegalArgumentException("Can't support more than 4 AI players");
    //     if(humanPlayers <= 0 && aiPlayers <= 0)
    //         throw new IllegalArgumentException("There must be at least one player");
    //     if(humanPlayers+aiPlayers > 4)
    //         throw new IllegalArgumentException("Can't support more than 4 players");

    // }
    
    private void startPlayers() // run player threads
    {
        // errorPlayers()

        Thread[] pThreads = new Thread[players.length];
        for( int i=0 ; i<players.length ; i++ ) {
            pThreads[i] = new Thread(players[i]);
            pThreads[i].start();
        }

    }
    private Player findPlayer(int id) // finds player by id
    {
        for(Player p : players)
            if (p.id == id)
                return p;

        throw new IllegalArgumentException("No Player Has The Specified ID !!!!!");

    }


    // --------------------------- Maintenance Functions --------------------------------------------------------------------------------------------------------------------


    public void terminate() // terminates program
    {
        terminate = true;
        for(Player p : players)
            synchronized (p) {
                p.terminate();
                if(Thread.currentThread() == dealerThread)
                    notifyAll();
            }
        interruptAllPlayers();
        dealerThread.interrupt();
    }

    protected void resetTimer() // resets timer
    {
        displayTimer = displayTimerFinal;
    }

    private void shuffleDeck() // shuffles deck
    {
        Collections.shuffle(deck);
    }
    private void sleepUntilWokenOrTimeout() // time gaps
    {
        try {Thread.sleep(sleepDuration);} // 10 seconds per shuffle
        catch (InterruptedException ignored) { System.out.println("excepcion in sleeoUntillWoken ignored"); }
    }
    private void updateTimerDisplay(boolean red) // update timer color ( red is true, black is false)
    {
        env.ui.setCountdown(displayTimer*sleepDuration,red);
    }
    private void placeCardsOnTable() // place deck on table
    {
        for(int i=0; i < Math.min(deck.size()+ table.countCards(),maxCardsOnTable) & !deck.isEmpty(); i++) {
            Integer card = deck.remove(0);
            table.placeCard(card,i);
        }
    }

    private void removeAllCardsFromTable() // remove all cards from table
    {
        env.ui.removeTokens();
        for (Player p : players)
            p.initTokens();

        for (int i=0; (i<maxCardsOnTable && table.slotToCard[i] != -1) ;i++) {
            deck.add(table.slotToCard[i]);
            table.removeCard(i);
        }
    }


    private void announceWinners() // announce winners
    {

        int index = 0;
        int amountOfWinners = 0;
        int maxScore = 0;


        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i].score() > maxScore) {
                maxScore = this.players[i].score();
            }
        }

        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i].score() == maxScore) {
                ++amountOfWinners;
            }
        }

        int[] ids = new int[amountOfWinners];

        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i].score() == maxScore) {
                ids[index] = this.players[i].id;
                ++index;
            }
        }

        this.env.ui.announceWinner(ids);
    }
    protected ArrayList<Integer> addDeck(ArrayList<Integer> cards) // add deck cards to given "cards" from index j....
    {
        for( int i = 0 ; i < deck.size() ; i++ )
            cards.add(deck.get(i));

        return cards;
    }
}