package defult.ServiceLayer.HR;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import java.util.Scanner;

import static defult.Main.systemsMenu;
import static defult.ServiceLayer.HR.ManagerHRService.managerWindow;
import static defult.ServiceLayer.HR.ShiftManagerService.shiftManagerWindow;

public class HRService {
    public static int validateIntInput(Scanner s) {
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

    public static void HR_Main(ShiftBoard sb, Scanner s){

        boolean menuHR = true;

        while(menuHR) {

            System.out.println(
                    "enter as :\n" +
                            "1. Manager HR\n" +
                            "2. Employee\n" +
                            "3. Shift manager\n" +
                            "\n0. Exit\n"
            );

            int choice = validateIntInput(s);

            switch (choice) {

                case 0:
                    systemsMenu();
                    break;

                case 1:
                    managerWindow(sb, s);
                    break;

                case 2:
                    EmployeeService.employeeWindow(sb, s);
                    break;

                case 3:
                    shiftManagerWindow(sb, s);
                    break;

                default:
                    System.out.println("invalid input please enter again: ");
                    HR_Main(sb, s);
                    break;
            }
        }
    }
}
