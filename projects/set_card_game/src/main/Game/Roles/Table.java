package Roles;

import Admin.Env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * This class contains the data that is visible to the player.
 *
 * @inv slotToCard[x] == y iff cardToSlot[y] == x
 */
public class Table {

    // --------------------------- Variables --------------------------------------------------------------------------------------------------------------------

    private final Env env;
    protected final Integer[] slotToCard; // card per slot (if any)
    protected final Integer[] cardToSlot; // slot per card (if any)
    protected int trioID;
    protected final int maxPlayerSize = 4;

    protected ArrayBlockingQueue<Integer> trioIDs;


    // --------------------------- Constructors --------------------------------------------------------------------------------------------------------------------


    public Table(Env env, Integer[] slotToCard, Integer[] cardToSlot)
    {

        this.env = env;
        this.slotToCard = slotToCard;
        this.cardToSlot = cardToSlot;
        this.trioIDs = new ArrayBlockingQueue<Integer>(maxPlayerSize);
        this.trioID = -1;
    }
    public Table(Env env)
    {

        this(env, new Integer[env.config.tableSize], new Integer[env.config.deckSize]);
    }


    // --------------------------- Maintenance Functions --------------------------------------------------------------------------------------------------------------------


    public void initIDS()
    {
        for (Integer i : trioIDs)
            trioIDs.remove(i);
    }
    public void setCapacityIDS(int capacity) // set new capacity of IDS
    {
        trioIDs = new ArrayBlockingQueue<Integer>(capacity);
    }
    public void hints() // prints possible legal sets
    {
        List<Integer> deck = Arrays.stream(slotToCard).filter(Objects::nonNull).collect(Collectors.toList());
        env.util.findSets(deck, Integer.MAX_VALUE).forEach(set -> {
            StringBuilder sb = new StringBuilder().append("Hint: Set found: ");
            List<Integer> slots = Arrays.stream(set).mapToObj(card -> cardToSlot[card]).sorted().collect(Collectors.toList());
            int[][] features = env.util.cardsToFeatures(set);
            System.out.println(sb.append("slots: ").append(slots).append(" features: ").append(Arrays.deepToString(features)));
        });
    }
    public int countCards() // returns the amount of cards on table
    {
        int cards = 0;
        for (Integer card : slotToCard)
            if (card != null && card != -1)
                ++cards;
        return cards;
    }
    public void placeCard(int card, int slot) // place card on table (at slot)
    {
        try {
            Thread.sleep(env.config.tableDelayMillis);
        } catch (InterruptedException ignored) {}

        cardToSlot[card] = slot;
        slotToCard[slot] = card;
        env.ui.placeCard(card, slot);
    }
    public void removeCard(int slot) // remove card from table (from slot)
    {
        try {
            Thread.sleep(env.config.tableDelayMillis);
        } catch (InterruptedException ignored) {}
        env.ui.removeCard(slot);

    }
    public void placeToken(int player, int slot) // place token on table (at slot)
    {
        env.ui.placeToken(player,slot);
    }
    public boolean removeToken(int player, int slot) // remove token on table (at slot)
    {
        try
        {
            env.ui.removeToken(player,slot);
            return true;
        }
        catch (Exception ignored) { System.out.println("Exception in table-->removeToken"); return false; }
    }
    public boolean slotEmpty(int slot) // check if slot is empty
    {
        return slotToCard[slot] == null;
    }
    public ArrayList<Integer> addTableCards(ArrayList<Integer> tableCards) // add table cards to "cards"
    {

        for( int i = 0 ; i < slotToCard.length ; i++ )
            if(slotToCard[i] != null & slotToCard[i] != -1)
                tableCards.add( slotToCard[i] );

        return tableCards;
    }

}
