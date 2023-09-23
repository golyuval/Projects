package defult.BusinessLayer.DeliverySystem.Structures;;

import java.util.HashMap;

public class RequestedOrder {



    // -------- Variables ----------------------------------------------------------------------------------------------------------

    public HashMap<String, Integer> _itemsOrdered;


    // -------- Constructors ----------------------------------------------------------------------------------------------------------

    public RequestedOrder () {
        _itemsOrdered = new HashMap<>();
    }

    public RequestedOrder ( HashMap<String, Integer> itemsOrdered ) {
        _itemsOrdered = itemsOrdered;
    }

    public RequestedOrder (String productName, Integer productAmount ) {
        _itemsOrdered = new HashMap<>();
        _itemsOrdered.put(productName,productAmount);
    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------


    public void parseItems(String items)
    {
        String[] partial1 = items.strip().split("[,-]");


        for (String pair : partial1 ) {
            if(pair.length() != 0) {
                String[] partial2 = pair.strip().split(":");
                _itemsOrdered.put(partial2[0], Integer.parseInt(partial2[1].strip()));
            }
        }
    }
    public void removeProduct(String productName){
        _itemsOrdered.remove(productName);
    }

    public void addProductAndAmount(String productName,int amount){
        _itemsOrdered.put(productName,amount);
    }

    public void addItemsFromAnotherOrder (RequestedOrder other) {
        for (HashMap.Entry<String, Integer> set : other.get_itemsOrdered().entrySet()) {
            if(!_itemsOrdered.containsKey(set.getKey()))
                _itemsOrdered.put(set.getKey(), set.getValue());
            else
                _itemsOrdered.put(set.getKey(), _itemsOrdered.get(set.getKey()) + set.getValue());
        }
    }


    // -------- Printers ----------------------------------------------------------------------------------------------------------

    public String toString () {
        String output = "";
        for (HashMap.Entry<String, Integer> set : _itemsOrdered.entrySet()) {
            // Printing all products & amount in the requested order
            output += "\t- " + set.getKey() + " : " + set.getValue()+"\n";
        }
        return output;
    }

    public String toString1 () {
        String output = "";
        int i = 0;
        for (HashMap.Entry<String, Integer> set : _itemsOrdered.entrySet()) {
            // Printing all products & amount in the requested order
            if(i<_itemsOrdered.size()-1)
                output += set.getKey() + " : " + set.getValue()+",";
            else
                output += set.getKey() + " : " + set.getValue();
            i++;
        }
        return output;
    }


    // -------- Getters / Setters ----------------------------------------------------------------------------------------------------------

    public HashMap<String, Integer> get_itemsOrdered () {
        return _itemsOrdered;
    }


}

