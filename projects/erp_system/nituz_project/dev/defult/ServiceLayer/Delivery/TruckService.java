package defult.ServiceLayer.Delivery;

import defult.BusinessLayer.Controllers.Delivery.Truck_Controller;

import java.util.LinkedList;

import static defult.Main.validator;

public class TruckService {


    // -------- variables ----------------------------------------------------------------------------------------------------------

    private Truck_Controller truckController;
    public static TruckService Truck_Service_Instance;
    public static TruckService getInstance() {
        return Truck_Service_Instance;
    }

    // -------- Functions ----------------------------------------------------------------------------------------------------------

    public TruckService() {

        truckController = new Truck_Controller();
        Truck_Service_Instance = this;
    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------

    public void Truck_Menu() {
        boolean menuT = true;
        while (menuT) {

            System.out.println("\n0. Exit\n");
            System.out.println("1. Add Truck");
            System.out.println("2. Show Available Trucks");
            System.out.println("3. Show Unavailable Trucks");
            int choice = validator.valid_Int();

            switch (choice){
                case 0:
                    menuT = false;
                    break;

                case 1:
                    System.out.println("\nInsert the necessary licence restrictions for this truck (-1 to Stop)");
                    LinkedList<String> licences = new LinkedList<>();
                    String licenceR = validator.valid_Licence();

                    while (!licenceR.equals("-1")) {
                        licences.add(licenceR);
                        licenceR = validator.valid_Licence();
                    }
                    System.out.println("\nInsert the cooling capability for this truck");
                    double freezeTmp = validator.valid_Double();

                    System.out.println("\nInsert the max weight for this truck");
                    double maxWeight = validator.valid_Double();

                    try {
                        createTruck(500, maxWeight, freezeTmp, licences);
                        System.out.println("Truck Added successfully!");
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;

                case 2:
                    System.out.println(truckController.print_AvailableTrucks());
                    break;

                case 3:
                    System.out.println(truckController.print_UnavailableTrucks());
                    break;

                default:
                    System.out.println("Please enter a number between the following options : ( 0 / 1 / 2 / 3 ) ");
                    break;

            }



        }
    }

    public String createTruck(double weight, double max, double cool, LinkedList<String> licence) {
        try {
            truckController.createTruck(weight, max, cool, licence);
            return "Truck created successfully\n";
        }
        catch (Exception ex){return ex.getMessage();}
    }

    public Truck_Controller getTruckController () {
        return truckController;
    }
}