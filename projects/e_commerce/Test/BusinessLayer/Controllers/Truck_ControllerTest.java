package BusinessLayer.Controllers;
import defult.BusinessLayer.Controllers.Delivery.Truck_Controller;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static defult.Main.repository;
import static defult.Main.resetDataBase;
import static org.junit.jupiter.api.Assertions.*;

class Truck_ControllerTest {

    @Test
    void truckSuitsDelivery () {
        resetDataBase();
        repository.createTables();
        String driverLicence = "A3";
        LinkedList<String> licences = new LinkedList<>();
        licences.add("A1");
        licences.add("B2");
        double maxWeight = 1500.5;
        double currentWeight = 1000.3;
        double requiredTemp = 6;
        Truck_Controller tc = new Truck_Controller();
        for(int i = 1; i <= 10; i++)
            tc.createTruck(currentWeight, maxWeight, requiredTemp, licences);
        assertEquals(tc.truckSuitsDelivery(tc.getTruckByTruckID(1),10,driverLicence),false);
    }

    @Test
    void canReplaceTruck () {
        resetDataBase();
        repository.createTables();
        LinkedList<String> licences = new LinkedList<>();
        licences.add("A1");
        licences.add("B2");
        double maxWeight = 1500.5;
        double currentWeight = 1000.3;
        double requiredTemp = 6;
        Truck_Controller tc = new Truck_Controller();
        for(int i = 1; i <= 10; i++)
            tc.createTruck(currentWeight, maxWeight, requiredTemp, licences);

        assertEquals(tc.canReplaceTruck(currentWeight,requiredTemp,licences.get(0)),true);

    }
}