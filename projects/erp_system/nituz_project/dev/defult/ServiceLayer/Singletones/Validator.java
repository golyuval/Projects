package defult.ServiceLayer.Singletones;

import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.ServiceLayer.Delivery.DeliveryService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static defult.Main.deliveryService;
import static defult.Main.validator;


public class Validator {


    // ----------------- Variables ------------------------------------------------------------------------------------------------

    private static Validator validator;
    private Scanner s = new Scanner(System.in);


    // ----------------- Validator ------------------------------------------------------------------------------------------------

    public Validator() {
    }

    public static Validator getInstance() {
        if (validator == null)
            validator = new Validator();
        return validator;
    }

    // ----------------- Validations ------------------------------------------------------------------------------------------------

    public int valid_Int() {
        while (true) {
            if (s.hasNextInt()) {
                int input = s.nextInt();
                s.nextLine(); // consume the remaining newline character
                return input;
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                s.nextLine(); // consume the invalid input
            }
        }
    }

    public String valid_Licence() {
        List<String> okLicences = new LinkedList<>();

        okLicences.add("A1");
        okLicences.add("B1");
        okLicences.add("C1");
        String input;

        while (true) {
            if (s.hasNext()) {
                input = s.next();
                if(!okLicences.contains(input) && !input.equals("-1")) {
                    System.out.println("Invalid input. Please enter string of characters.");
                    s.nextLine(); // consume the remaining newline character
                }
                else
                    break;
            } else {
                System.out.println("Invalid input. Please enter string of characters.");
                s.nextLine(); // consume the invalid input
            }
        }
        return input;
    }
    public String valid_Str() {
        while (true) {
            if (s.hasNext()) {
                String input = s.next();
                s.nextLine(); // consume the remaining newline character
                return input;
            } else {
                System.out.println("Invalid input. Please enter string of characters.");
                s.nextLine(); // consume the invalid input
            }
        }
    }

    public double valid_Double() {
        while (true) {
            if (s.hasNextDouble()) {
                double input = s.nextDouble();
                s.nextLine(); // consume the remaining newline character
                return input;
            } else {
                System.out.println("Invalid input. Please enter an double.");
                s.nextLine(); // consume the invalid input
            }
        }
    }

    public LocalDate valid_Date() {
        String dateP = s.next();
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                System.out.println("enter date (dd/MM/yyyy):\n");
                dateP = s.next();
            }
        }
        return date;
    }

    public int valid_DeliveryID_date(DeliveryService deliveryService, LocalDate date)
    {
        int id = -1;
        boolean flag = true;
        LinkedList<Integer> ids;

        while (flag){
            ids = new LinkedList<>();
            id = valid_DeliveryID_schedule(deliveryService);

            for(Delivery delivery: deliveryService._dc._fc.getScheduledDeliveries())
                if(delivery.getDeparture_date().equals(date))
                    ids.add(delivery.get_delivery_id());

            flag = !ids.contains(id);
            if(flag)
                System.out.println("Invalid input, choose an ID from the printed deliveries above.");

        }

        return id;
    }
    public int valid_DeliveryID_schedule(DeliveryService deliveryService)
    {
        int id = valid_Int();
        if(deliveryService._dc._fc.deliveryExist_schedule(id))
            return id;
        return -1;

    }

    public int valid_DeliveryID_inProgress(DeliveryService deliveryService)
    {
        int id = valid_Int();
        if(deliveryService._dc._fc.deliveryExist_inProgress(id))
            return id;
        return -1;

    }

    public int valid_Dest() {
        int input = -1;
        while (true) {
            if (s.hasNextInt() && (input = s.nextInt()) < 10 && input > 0) {
                //int input = s.nextInt();
                s.nextLine(); // consume the remaining newline character
                return input;
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                s.nextLine(); // consume the invalid input
            }
        }
    }

    public boolean valid_Order(Site supplier, Site branch, RequestedOrder order) {
        return supplier != null && branch != null && order.get_itemsOrdered().size() != 0;
    }

    public LocalTime valid_Time(String arrive_departure) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        boolean endGood = false;
        String endShiftP = "";
        LocalTime endShift = LocalTime.parse("00:00");


        while (!endGood) {
            endShiftP = s.next();
            try {
                endShift = LocalTime.parse(endShiftP, formatter); // parse the string into a LocalTime object
                //System.out.println("Parsed time: " + startShift);
                endGood = true;
            } catch (DateTimeParseException e) {
                System.out.println("enter delivery " + arrive_departure + " time again in format: HH:mm ");
            }
        }

        return endShift;
    }

    public int valid_Maneger() {

        int password = valid_Int();

        for (int i = 0; i < 3 && password != 123456; i++) {
            System.out.println("try\t" + (i + 1) + "\n" + "this is not the correct password, to leave this screen press -1");
            password = valid_Int();
            if (password == -1)
                break;
        }

        if (password != -1 && password != 123456) {
            System.out.println("--- You have typed 3 times wrong password ---");
        }

        return password;
    }

    public int valid_DeliveryDone() {
        System.out.println("insert Delivery ID to set to done");
        int id = valid_Int();

        if (deliveryService._fs.fc.getDeliveryByID(id).getState() == 0) {
            System.out.println("The id which was typed in was not shipped yet.. ");
            return -1;
        }
        if (deliveryService._fs.fc.getDeliveryByID(id).getState() == 2) {
            System.out.println("The id which was typed is referred to delivery that is already done.. ");
            return -1;
        }
        return id;
    }
}
