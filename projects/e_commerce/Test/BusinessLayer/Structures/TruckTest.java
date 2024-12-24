package BusinessLayer.Structures;

import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.DeliverySystem.Structures.Truck;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {

    @Test
    void cool_Enough () {
        Site mainSite =  new Site(
                new Address("BeerSheva","Rager",59),
                "052-7903414", "Evyatar Kopans",0,"Main Logistic Center", false);

        LinkedList<String> licences = new LinkedList<>();
        licences.add("A1");
        licences.add("B2");
        double maxWeight = 1500.5;
        double currentWeight = 1000.3;
        double requiredTemp = 6;
        Truck truck = new Truck(mainSite,currentWeight,maxWeight,requiredTemp,licences);
        assertEquals(truck.cool_Enough(1),false);

    }

    @Test
    void resetInDeliveryTime () {
        Site mainSite =  new Site(
                new Address("BeerSheva","Rager",59),
                "052-7903414", "Evyatar Kopans",0,"Main Logistic Center", false);

        LinkedList<String> licences = new LinkedList<>();
        licences.add("A1");
        licences.add("B2");
        double maxWeight = 1500.5;
        double currentWeight = 1000.3;
        double requiredTemp = 6;
        Truck truck = new Truck(mainSite,currentWeight,maxWeight,requiredTemp,licences);
        truck.setInDeliveryTime(10);
        truck.resetInDeliveryTime();
        assertEquals(truck.getInDeliveryTime(),0);

    }

    @Test
    void resetInDelivery () {
        Site mainSite =  new Site(
                new Address("BeerSheva","Rager",59),
                "052-7903414", "Evyatar Kopans",0,"Main Logistic Center", false);

        LinkedList<String> licences = new LinkedList<>();
        licences.add("A1");
        licences.add("B2");
        double maxWeight = 1500.5;
        double currentWeight = 1000.3;
        double requiredTemp = 6;
        Truck truck = new Truck(mainSite,currentWeight,maxWeight,requiredTemp,licences);
        truck.changeInDelivery();
        truck.resetInDelivery();
        assertEquals(truck.isInDelivery(),false);
    }
}