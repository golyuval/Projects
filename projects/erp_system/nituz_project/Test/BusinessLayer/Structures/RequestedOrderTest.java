package BusinessLayer.Structures;

import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.DeliverySystem.Structures.Truck;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class RequestedOrderTest {

    @Test
    void printOrder () {
        HashMap<String, Integer> items = new HashMap<>();
        items.put("popcorn",10);
        items.put("peanutButter",100);
        items.put("toothpaste",1000);
        String output = "peanutButter : 100\n"+
                "toothpaste : 1000\n" +
                "popcorn : 10\n";
        RequestedOrder order = new RequestedOrder(items);
        System.out.println(order.toString1());

        assertEquals(output,order.toString1());
    }

    @Test
    void addItemsFromAnotherOrder () {
        HashMap<String, Integer> items = new HashMap<>();
        items.put("popcorn",10);
        items.put("peanutButter",100);
        items.put("toothpaste",1000);

        HashMap<String, Integer> sameItemsForCheck = new HashMap<>();
        sameItemsForCheck.put("popcorn",10);
        sameItemsForCheck.put("peanutButter",100);
        sameItemsForCheck.put("toothpaste",1000);
        RequestedOrder order = new RequestedOrder(items);
        RequestedOrder order2 = new RequestedOrder("popcorn",0);
        order2.addItemsFromAnotherOrder(order);

        assertEquals(order2.get_itemsOrdered(),sameItemsForCheck);
    }

}