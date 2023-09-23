package defult.ServiceLayer.Delivery;



import Presentation.DeliveryUI.DeliverySystemUI;
import defult.BusinessLayer.Controllers.Delivery.Delivery_Controller;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.ServiceLayer.Singletones.Validator;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DeliveryService {


    // -------- Variables ----------------------------------------------------------------------------------------------------------

    public TruckService _ts;
    public FormService _fs;
    public Delivery_Controller _dc;
    public Validator validator;


    // -------- Constructor ----------------------------------------------------------------------------------------------------------

    public static DeliveryService Delivery_Service_Instance;
    public static DeliveryService getInstance() {
        return Delivery_Service_Instance;
    }

    public DeliveryService (Validator v) {
        _ts = new TruckService();
        _fs = new FormService();
        _dc = new Delivery_Controller(_ts.getTruckController(),_fs.getFormsController());
        validator = v;
        Delivery_Service_Instance = this;
    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------


    public void Maneger_Menu(Validator validator){
        boolean menuM = true;
        System.out.println("please insert the password to enter manger menu"); // throws Illegal Argumet Exception

        if(validator.valid_Maneger() != 123456)
            menuM = false;

        while(menuM) {


            System.out.println("\n0. Exit\n");
            System.out.println("1. Send delivery");
            System.out.println("2. Update delivery state to : Delivery Done ");

            int manegerInput = validator.valid_Int();

            switch (manegerInput){

                case 0:
                    menuM = false;
                    break;

                case 1:
                    System.out.println("would you like to view today's deliveries? y for yes");
                    String answer = validator.valid_Str();
                    if (!answer.equals("y")) {
                        System.out.println("What date would you like to view");
                        LocalDate currD = validator.valid_Date();
                        try {
                            System.out.println(_fs.fc.presentDeliveries(currD));
                            System.out.println("please insert the delivery id you want to ship");
                            int deliveryID = validator.valid_DeliveryID_date(this,currD);

                            if(deliveryID == -1) {
                                System.out.println(" No such delivery was found ");
                                break;
                            }
                            _fs.fc.sendDelivery(deliveryID);
                            int truckID = _dc._fc.getDeliveryByID(deliveryID).get_truck_ID();
                            _ts.getTruckController().truckGoesOnDelivery(truckID);

                            String shippedDelivery = "Delivery #"+ deliveryID+" details:\n";
                            for (Order orderInDelivery : _dc._fc.getDeliveryByID(deliveryID).get_orders())
                                    if (!orderInDelivery.get_order().get_itemsOrdered().isEmpty())
                                        shippedDelivery += orderInDelivery + "\n\n";


                            System.out.println(shippedDelivery);

                        }
                        catch (NoSuchElementException ex){System.out.println(ex.getMessage());}
                    }
                    else
                        try
                        {
                            System.out.println(_fs.fc.presentDeliveries(LocalDate.now()));
                            System.out.println("please insert the delivery id you want to ship");
                            int deliveryID = validator.valid_DeliveryID_date(this,LocalDate.now());

                            if(deliveryID == -1) {
                                System.out.println(" No such delivery was found ");
                                break;
                            }
                            _fs.fc.sendDelivery(deliveryID);
                            String shippedDelivery = "Delivery #"+ deliveryID+" details:\n";
                                for (Order orderInDelivery : _dc._fc.getDeliveryByID(deliveryID).get_orders()) {
                                    if (!orderInDelivery.get_order().get_itemsOrdered().isEmpty())
                                        shippedDelivery += orderInDelivery + "\n\n";
                                }

                                System.out.println(shippedDelivery);

                        }
                        catch (NoSuchElementException ex){
                            System.out.println(ex.getMessage());
                        }
                    break;

                case 2:
                    System.out.println("Choose a delivery from the ones below:\n");
                    System.out.println(_fs.fc.inProgressDeliveries());
                    int deliveryDone = validator.valid_DeliveryDone();
                    int truckID = _dc._fc.getDeliveryByID(deliveryDone).get_truck_ID();
                    if(deliveryDone != -1) {
                        _dc.delivery_done(deliveryDone);
                        _ts.getTruckController().truckReturnsFromDelivery(truckID);
                        System.out.println("Delivery #"+ deliveryDone+" has reached it's destination!");
                        System.out.println("Truck #"+ truckID+" has returned tambien");

                    }

                    break;

                default:
                    System.out.println("Please enter a number between the following options : ( 0 / 1 / 2 / 3 ) ");
                    break;
            }
        }
    }


    public String scheduleDelivery (){

        System.out.println("Please insert the necessary temperature for this delivery.");
        double requiredTemperature = validator.valid_Double();

        System.out.println("Please insert the delivery driver's licence restrictions.");
        String driverLicence = validator.valid_Licence();

        System.out.println("Please insert the delivery destination.");
        int branchID = validator.valid_Dest();

        System.out.println("Please insert the delivery departure Date.");
        LocalDate currD = validator.valid_Date();

        System.out.println("Please insert the delivery departure Time");
        LocalTime currT = validator.valid_Time("departure");

        System.out.println("Please insert the delivery arrival Time");
        LocalTime arrivalT = validator.valid_Time("arrival");

        int truckID = _ts.getTruckController().getNewTruck(requiredTemperature, driverLicence);
        System.out.println("The truck has been loaded with the order!");

        System.out.println("\nChosen truck: ");
        System.out.println(_ts.getTruckController().getTruckByTruckID(truckID).toString());

        System.out.println("Now, please insert the truck's weight.");
        double truckWeight = validator.valid_Double();
        LinkedList<Integer> overloads = new LinkedList<>();
        LinkedList<Order> orders = _dc.gatherOrdersToDelivery();
        int current_ID = -1;

        try {
            while ( (current_ID = _dc.tryScheduleDelivery(orders,truckID, truckWeight,overloads,currD,currT,arrivalT)) == -1) {

                System.out.println("The truck weigh more than it's capacity\nplease choose your preferred solution:");

                if (_ts.getTruckController().canReplaceTruck(truckWeight,requiredTemperature,driverLicence))
                    System.out.println("\t1: change truck.");
                System.out.println("\t2: remove items.\n\t3: remove one of the destinations and all it's products.");

                int solution = validator.valid_Int();
                while((!_ts.getTruckController().canReplaceTruck(truckWeight,requiredTemperature,driverLicence) && (solution < 2 || solution > 3)) ||
                        _ts.getTruckController().canReplaceTruck(truckWeight,requiredTemperature,driverLicence) && (solution < 1 || solution > 3))
                {
                    System.out.println("Choose a valid solution.");
                    solution = validator.valid_Int();
                }
                overloads.add(_dc.recordOverLoad(solution));

                truckWeight = handle_OverWeight(solution, truckWeight, requiredTemperature, driverLicence, overloads, orders,currD,currT,arrivalT);

                if (truckWeight == 0) {
                    System.out.println("Truck changed successfully");
                    break;
                }
                else if (truckWeight == -1)
                    continue;
                else if (truckWeight == -2)
                    continue;

            }
            _dc.saveDelivery(currD,currT,arrivalT,branchID,driverLicence, current_ID);

        } catch (NoSuchElementException ex) {
            return ex.getMessage();
        }

        return "Delivery ordered successfully\n";
    }

    public String orderDelivery (Site supplier, Site branch, HashMap<String, Integer> order ) {
        String ui_Output = "";
        try {

            RequestedOrder branchOrder = new RequestedOrder(order);
            _dc.orderDelivery(supplier, branch, branchOrder);
            ui_Output = "Order has been created!";


            return ui_Output;
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }

    public void print_PendingOrders(){

        System.out.println("current pending orders:\n");
        for (Order curr : _dc.get_requestedOrders()) {
            System.out.print(curr.toString()+"\n\n");
        }
    }

    public double handle_OverWeight(int solution, double truckWeight, double requiredTemperature,
                                   String driverLicence,  LinkedList<Integer> overloads,
                                   LinkedList<Order> orders, LocalDate currD, LocalTime currT, LocalTime arrivalT){

        Scanner userInput = new Scanner(System.in);
        switch (solution) {
            case 1:
                try {
                    if(!_ts.getTruckController().canReplaceTruck(truckWeight,requiredTemperature,driverLicence)) {
                        System.out.println("Changing Truck is not available");
                        return -1;
                    }
                    _dc.handleOption1(truckWeight, requiredTemperature, driverLicence,
                                      orders, overloads,currD,currT,arrivalT);
                    return 0;
                }
                catch(NoSuchElementException ex){System.out.println(ex.getMessage()); return -10;}

            case 2:
                System.out.println("current products in the delivery Order are:");
                for(Order curr: orders)
                    System.out.println(curr+"\n");

                System.out.println("please write the name of the product you wish to remove from ALL branches' orders");
                String productToRemove = userInput.next().toLowerCase();
                try {
                    _dc.handleOption2(productToRemove, orders);
                }
                catch (NoSuchElementException ex){
                    System.out.println(ex.getMessage());
                    return -1;
                }
                System.out.println("\nplease insert the new truck's weight:");
                truckWeight = userInput.nextDouble();
                return truckWeight;

            case 3:
                System.out.println("current branches in the delivery Order & their requested goods are:");
                for(Order curr: orders)
                    System.out.println(curr+"\n");

                System.out.println("please select the name of the branch you wish to remove from the delivery");
                String branchToRemove = userInput.next().toLowerCase();
                _dc.handleOption3(branchToRemove,orders);

                System.out.println("\nplease insert the new truck's weight:");
                truckWeight = userInput.nextDouble();
                return truckWeight;

            default:
                System.out.println("choose a number between 1-3 amigo");
                return -2;

        }
    }

    public static void Delivery_Main(Validator validator, DeliveryService deliveryService) {

        boolean leave = false;
        String mainP = "Main Menu:";
        Thread.currentThread().setName("mainThread");
        //SwingUtilities.invokeLater(DeliverySystemUI::new);

        while(!leave) {

            System.out.println(mainP);
            System.out.println("\n0. Exit\n");
            System.out.println("1. Truck menu");
            System.out.println("2. Forms menu");
            System.out.println("3. Schedule new delivery");
            System.out.println("4. Show pending orders");
            System.out.println("5. Manager menu (password needed)");


            int menuOption = validator.valid_Int();
            switch (menuOption) {

                case 0:
                    leave = true;
                    break;

                case 1:
                    deliveryService._ts.Truck_Menu();
                    break;

                case 2:
                    deliveryService._fs.Forms_Menu();
                    break;

                case 3:
                    String delivery = deliveryService.scheduleDelivery();
                    System.out.println(delivery);
                    break;

                case 4:
                    //SwingUtilities.invokeLater(DeliverySystemUI::new);
                    deliveryService.print_PendingOrders();
                    break;

                case 5:
                    deliveryService.Maneger_Menu(validator);
                    break;

                default:
                    System.out.println("Please enter a number between the following options : ( 0 / 1 / 2 / 3 / 4 / 5 ) ");
                    break;

            }
        }
    }
}

