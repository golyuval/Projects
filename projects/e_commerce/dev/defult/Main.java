package defult;
import Presentation.DeliveryUI.DeliverySystemUI;
import Presentation.MainUI;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.DataAccessLayer.HR.Dal.EmployeeHRDAO;
import defult.DataAccessLayer.HR.Dal.ShiftHRDAO;
import defult.DataAccessLayer.Transport.DAO.*;
import defult.DataAccessLayer.Repo;
import defult.ServiceLayer.Delivery.DeliveryService;
import defult.ServiceLayer.HR.ManagerHRService;
import defult.ServiceLayer.Singletones.Generator;
import defult.ServiceLayer.Singletones.Validator;

import javax.swing.*;
import java.util.*;


import static defult.ServiceLayer.Delivery.DeliveryService.Delivery_Main;
import static defult.ServiceLayer.HR.HRService.HR_Main;
import static defult.ServiceLayer.HR.HRService.validateIntInput;


public class Main
{

    public static Repo repository = Repo.getInstance() ;

    public static Scanner s = new Scanner(System.in);
    public static Validator validator = Validator.getInstance();
    public static DeliveryService deliveryService = new DeliveryService(validator);
    public static ShiftBoard shiftBoard = ShiftBoard.getInstance();
    public static Generator generator = Generator.getInstance();




    public static void main(String[] args) {
       resetDataBase();
       repository.createTables();

       generator.generate_Delivery_Env(deliveryService);
       generator.generate_HR_Env(shiftBoard);


       initData();
       systemsMenu();
//        SwingUtilities.invokeLater(DeliverySystemUI::new);
    }

    public static void initData()
    {
        shiftBoard.init_Data();
        deliveryService._dc.init_Data();
        deliveryService._fs.fc.init_Data();
        deliveryService._ts.getTruckController().init_Data();
    }

    public static void resetTables()
    {
        DeliveryTransportDAO delivery = new DeliveryTransportDAO();
        OrderTransportDAO order = new OrderTransportDAO();
        DestFormTransportDAO dest = new DestFormTransportDAO();
        OverLoadTransportDAO overload = new OverLoadTransportDAO();
        SiteTransportDAO site = new SiteTransportDAO();
        TruckTransportDAO truck = new TruckTransportDAO();

        boolean b1 = delivery.deleteAll();
        boolean b2 = order.deleteAll();
        boolean b3 = dest.deleteAll();
        boolean b4 = overload.deleteAll();
        boolean b5 = site.deleteAll();
        boolean b6 = truck.deleteAll();

        if(!(b1 && b2 && b3 && b4 && b5 && b6))
            throw new RuntimeException("Programmer : delete method went wrong\n" +
                                        "delivery returned - " + b1 + "\n"+
                                        "order returned - " + b2 +"\n"+
                                        "destform returned - " + b3 +"\n"+
                                        "overload returned - " + b4 +"\n"+
                                        "site returned - " + b5 +"\n"+
                                        "truck returned - " + b6 +"\n");
    }

    public static void resetDataBase()
    {
        DeliveryTransportDAO delivery = new DeliveryTransportDAO();
        OrderTransportDAO order = new OrderTransportDAO();
        DestFormTransportDAO dest = new DestFormTransportDAO();
        OverLoadTransportDAO overload = new OverLoadTransportDAO();
        SiteTransportDAO site = new SiteTransportDAO();
        TruckTransportDAO truck = new TruckTransportDAO();

        EmployeeHRDAO emp = new EmployeeHRDAO();
        ShiftHRDAO shift = new ShiftHRDAO(emp);



        boolean b1 = delivery.deleteTable();
        boolean b2 = order.deleteTable();
        boolean b3 = dest.deleteTable();
        boolean b4 = overload.deleteTable();
        boolean b5 = site.deleteTable();
        boolean b6 = truck.deleteTable();
        boolean b7 = delivery.deleteTupleTable();

        boolean b8 = emp.deleteTable();
        boolean b9 = shift.deleteTable();


        if(!(b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8 && b9))
            throw new RuntimeException("Programmer : delete method went wrong\n" +
                    "delivery returned - " + b1 + "\n"+
                    "order returned - " + b2 +"\n"+
                    "destform returned - " + b3 +"\n"+
                    "overload returned - " + b4 +"\n"+
                    "site returned - " + b5 +"\n"+
                    "truck returned - " + b6 +"\n" +
                    "tuple returned - " + b7 +"\n"+
                    "employee returned - " + b8 +"\n"+
                    "shift returned - " + b9 +"\n");
    }



    public static void systemsMenu() {
        boolean menu = true;
        if(s.next().equals("CLI HRManager")) ManagerHRService.managerWindow(ShiftBoard.getInstance(), s);

        System.out.println("enter preferred view: CLI / GUI");
        String input = s.next();
        if(input.toLowerCase(Locale.ROOT).equals("gui")){
            SwingUtilities.invokeLater(MainUI::new);

        }else {
            while (menu) {


                System.out.println(

                        "\nEnter to :\n" +
                                "0. Exit\n" +
                                "1. HR System\n" +
                                "2. Delivery System\n"
                );

                int choice = validateIntInput(s);

                switch (choice) {

                    case 0:
                        menu = false;
                        break;

                    case 1:
                        HR_Main(shiftBoard, s);
                        break;

                    case 2:
                        Delivery_Main(validator, deliveryService);
                        break;

                    default:
                        System.out.println("Please enter a number between the following options : ( 0 / 1 / 2  ) ");
                        break;
                }
            }
        }


    }
}
