package defult.ServiceLayer.HR;

import defult.BusinessLayer.HRsystem.Shift;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.ServiceLayer.Singletones.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static defult.ServiceLayer.HR.HRService.HR_Main;
import static defult.ServiceLayer.HR.HRService.validateIntInput;


public class ShiftManagerService {
    static Shift shift =null;

    //shift manager
    public static void shiftManagerWindow(ShiftBoard sb, Scanner s) {
        //get shift by date and type?
        System.out.println("1. start shift\n" +
                "2. back to shift\n" +
                "3. main menu\n");
        int isInShift = validateIntInput(s);
        switch (isInShift){
            case 1:
                System.out.println("1. morning\n" +
                        "2. night");
                int typeP =validateIntInput(s);
                boolean type = typeP != 1;
                System.out.println("Enter branch ID");
                int branchID =validateIntInput(s);
                while (branchID<=1|| branchID>=11){
                    System.out.println("branch is not exist, please enter branch ID again (id is 2 to 10)");
                    branchID = validateIntInput(s);
                }
                //TODO: delete after testing
                Validator v = Validator.getInstance();
                System.out.println("insert date: test only");//TODO: delete
                LocalDate testDateParsed  = v.valid_Date();
                shift = sb.SC.getShiftByDateType(testDateParsed, type, branchID);

                //------------
                //TODO: return when done testing
                //shift = sb.SC.getShiftByDateType(LocalDate.now(), type, branchID);

                //------------
                if(shift == null) {
                    try {
                        sb.isShiftIsClose(LocalDate.now(), type, branchID);
                        System.out.println("there is no shift scheduled");

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    HR_Main(sb, s);

                }else{
                    try{
                        sb.SC.validateShiftStart(shift);

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        shiftManagerWindow(sb, s);
                    }

                }

                System.out.println("enter Shift Manager password:\n");
                String password = s.next();
                try {
                    sb.SC.validateShiftManager(shift, password);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    shiftManagerWindow(sb, s);
                }

                if(shift.isInShift()){
                    backToShift(sb, s, shift);
                }else{
                    sb.SC.logStartShift(shift);
                }
                System.out.println("hello, have a nice shift!\n" +
                        "1. cancel transaction\n" +
                        "2. end shift\n" +
                        "3. back to menu");
                int choice = validateIntInput(s);
                if(choice!= 3)
                    inShift(sb, s, shift, choice);
                else
                    shiftManagerWindow(sb, s);
                break;
            case 2:
                if(shift !=null) {
                    try {
                        sb.SC.validateIsInShift(sb, s, shift);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        shiftManagerWindow(sb, s);
                    }
                    System.out.println("enter Shift Manager password:\n");
                    String password2 = s.next();
                    try {
                        sb.SC.validateShiftManager(shift, password2);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        shiftManagerWindow(sb, s);
                    }
                    backToShift(sb, s, shift);
                }
                shiftManagerWindow(sb, s);
                break;
            case 3:
                HR_Main(sb, s);
                break;
            default:
                System.out.println("invalid input please enter again: ");
                shiftManagerWindow(sb,s);
                break;
        }

    }

    public static void backToShift(ShiftBoard sb, Scanner s, Shift shift){
        System.out.println("enter your selection:\n" +
                "1. cancel transaction\n" +
                "2. end shift\n" +
                "3. back to menu");
        int choice = validateIntInput(s);
        if(choice!= 3)
            inShift(sb, s, shift, choice);
        else
            shiftManagerWindow(sb, s);
    }

    public static void inShift(ShiftBoard sb, Scanner s,Shift shift, int choice){
        switch (choice) {
            case 1:
                System.out.println("Enter cashier name:\n");
                String cashier = s.next();
                try {
                    sb.SC.validateCashierInShift(cashier, shift);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    backToShift(sb, s, shift);
                }
                System.out.println(sb.SC.logCancelTransaction(shift, cashier));
                break;
            case 2:
                try {
                    sb.SC.validateShiftEnd(shift);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    backToShift(sb, s, shift);
                }
                if(LocalTime.now().isBefore(shift.getShiftEnd().minusHours(1))){
                    sb.endShiftPolicy(shift);
                    System.out.println("it is too early to close "+shift.getShiftEnd().toString());
                    backToShift(sb, s, shift);

                }
                System.out.println(sb.SC.logEndShift(shift)+"\n");
                System.out.println("thank you, goodbye!");
                shiftManagerWindow(sb, s);
                break;
            default:
                System.out.println("invalid input please enter again: ");
                backToShift(sb,s, shift);
                break;
        }
        backToShift(sb, s, shift);
    }
}
