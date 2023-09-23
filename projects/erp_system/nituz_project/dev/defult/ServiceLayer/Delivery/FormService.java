package defult.ServiceLayer.Delivery;

import defult.BusinessLayer.Controllers.Delivery.Delivery_Controller;
import defult.BusinessLayer.Controllers.Delivery.Forms_Controller;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static defult.Main.validator;

public class FormService {

    // -------- Variables ----------------------------------------------------------------------------------------------------------

    public Forms_Controller fc;
    Scanner userInput;


    // -------- Constructor ----------------------------------------------------------------------------------------------------------
    public static FormService Form_Service_Instance;
    public static FormService getInstance() {
        return Form_Service_Instance;
    }
    public FormService() {

        fc = new Forms_Controller();
        userInput = new Scanner(System.in);
        Form_Service_Instance = this;
    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------


    public void Forms_Menu(){
        boolean menuF = true;
        while (menuF) {

            menuF = false;

            System.out.println("what type of form would you like to view?");
            System.out.println("\n0. Exit\n");
            System.out.println("1. Destination Form");
            System.out.println("2. Delivery Form");
            System.out.println("3. OverLoad Form");
            int formType = validator.valid_Int();


            switch (formType) {
                case 1:
                    System.out.println();
                    System.out.println("1. Destination form by delivery ID ");
                    System.out.println("2. Print all destination forms");
                    int destFormView = validator.valid_Int();

                    if (destFormView == 1) {
                        System.out.println();
                        System.out.println("Insert delivery ID");
                        int deliveryID = validator.valid_Int();
                        System.out.println(print_DestForms(deliveryID));
                    }
                    else if (destFormView == 2) {
                        System.out.println("All destination forms: ");
                        System.out.println(fc.printAllDestforms());
                    }
                    else
                        System.out.println(destFormView + " is not a valid input. returning to main menu.");
                    break;

                case 2:
                    System.out.println();
                    System.out.println("1. Delivery form by delivery ID ");
                    System.out.println("2. Print all scheduled deliveries forms");
                    System.out.println("3. Print all in-progress deliveries forms");
                    System.out.println("4. Print all done deliveries forms");
                    int deliveryFormView = validator.valid_Int();

                    if(deliveryFormView == 1)
                    {
                        System.out.println("Insert delivery ID");
                        int deliveryID = validator.valid_Int();
                        System.out.println(print_Delivery(deliveryID));
                    }
                    else if (deliveryFormView == 2) {
                        System.out.println(fc.printAll_ScheduledDeliveries());
                    }
                    else if (deliveryFormView == 3) {
                        System.out.println(fc.printAll_inProgressDeliveries());
                    }
                    else if (deliveryFormView == 4) {
                        System.out.println(fc.printAll_DoneDeliveries());
                    }
                    else
                        System.out.println(deliveryFormView + " is not a valid input. returning to main menu.");
                    break;

                case 3:
                    System.out.println();
                    System.out.println("1. Overload form by delivery ID ");
                    System.out.println("2. Print all overload forms");
                    int overloadFormView = validator.valid_Int();

                    if(overloadFormView == 1)
                    {
                        System.out.println("Insert delivery ID");
                        int overLoadView = validator.valid_Int();
                        System.out.println(print_OverLoads(overLoadView));
                    }
                    else if(overloadFormView == 2)
                    {
                        System.out.println(fc.printAllOverloads());
                    }
                    else
                        System.out.println(overloadFormView + " is not a valid input. returning to main menu.");
                    break;


                default:
                    System.out.println("Please enter a number between the following options : ( 0 / 1 / 2 / 3 ) ");
                    menuF = true;
                    break;
            }
        }
    }



    public String print_OverLoads(int deliveryID){
        try{return fc.printOverLoadsByDeliveryID(deliveryID); }
        catch (NoSuchElementException ex){return ex.getMessage();}
    }

    public String print_DestForms(int deliveryID){

        try{return fc.printDestFormByDeliveryID(deliveryID); }
        catch (NoSuchElementException ex){return ex.getMessage();}
    }

    public String print_Delivery(int deliveryID){

        try{ return fc.printDeliveryByID(deliveryID); }
        catch (NoSuchElementException ex){return ex.getMessage();}
    }


    public Forms_Controller getFormsController() {
        return fc;
    }


}
