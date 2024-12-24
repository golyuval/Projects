package BusinessLayer.Structures;

//import BusinessLayer.Structures.Sites.Branch;
//import BusinessLayer.Structures.Sites.Supplier;
import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;

import static defult.Main.repository;
import static defult.Main.resetDataBase;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void addToCart () {

        Address address = new Address("Rishon", "hazaz", 6);

        Site site1 = new Site(address, "0527903414", "Evyatar", 1, "Home",false);
        //site1.insertBase();

        Site site2 = new Site(address, "9999999999", "Yuval", 1, "Home2",true);
        //site2.insertBase();

        RequestedOrder requestedOrder = new RequestedOrder("popcorn",10);
        RequestedOrder requestedOrder2 = new RequestedOrder("tiras",10);
        Order Order = new Order(site1, site2,requestedOrder);
        Order.addToCart(requestedOrder2);
        assertEquals(requestedOrder.get_itemsOrdered().containsKey("tiras"),true);
    }

    @Test
    void removeFromCart () {
        resetDataBase();
        repository.createTables();
        Address address = new Address("Rishon", "hazaz", 6);

        Site site1 = new Site(address, "0527903414", "Evyatar", 1, "Home",false);
        //site1.insertBase();

        Site site2 = new Site(address, "9999999999", "Yuval", 1, "Home2",true);
        //site2.insertBase();

        RequestedOrder requestedOrder = new RequestedOrder("popcorn",10);
        RequestedOrder requestedOrder2 = new RequestedOrder("tiras",10);
        Order Order = new Order(site1, site2,requestedOrder);
        Order.addToCart(requestedOrder2);
        Order.removeFromCart("tiras");
        assertEquals(requestedOrder.get_itemsOrdered().containsKey("tiras"),false);
    }

}