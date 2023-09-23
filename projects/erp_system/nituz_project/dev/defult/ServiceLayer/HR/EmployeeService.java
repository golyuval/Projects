package defult.ServiceLayer.HR;

import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import static defult.ServiceLayer.HR.HRService.*;


public class EmployeeService {

    //employee
    public static void employeeWindow(ShiftBoard sb, Scanner s){
        System.out.println("Hello, please enter id:\n");
        int id = validateIntInput(s);
        while (sb.EC.validateId(id)) {
            System.out.println("Id dose not exist, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) HR_Main(sb,s);
        }
        while (sb.EC.validateStartDate(id)){
            System.out.println("Your contract has not yet begun:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) HR_Main(sb,s);
        }
        System.out.println("please enter password:\n");
        String password = s.next();
        while (!sb.EC.getEmployeeById(id).getPassword().equals(password)) {
            System.out.println("password dose not match, \n" +
                    "enter password again:");
            password =s.next();
        }
        Employee employee= sb.EC.getEmployeeById(id);
        inEmployeeWindow(sb, s, employee);

    }

    public static void inEmployeeWindow(ShiftBoard sb, Scanner s, Employee employee){
        if(!employee.getAlert().isBlank()) System.out.println(employee.getAlert());
        System.out.println("1. submit a shift\n" +
                "2. remove existing submission\n" +
                "3. main menu");
        int choice = validateIntInput(s);
        switch (choice){
            case 1:
                submitAShift(sb, s, employee);
                break;
            case 2:
                removeSubmission(sb, s, employee);
                break;
            case 3:
                HR_Main(sb, s);
                break;
            default:
                System.out.println("invalid input please enter again: ");
                inEmployeeWindow(sb,s, employee);
                break;
        }
    }

    public static void submitAShift(ShiftBoard sb, Scanner s, Employee employee){
        String dateP ="";
        while(true) {
            System.out.println("submit a shift:\n" +
                    "enter date, to go back to menu enter 0:");
            dateP = s.next();
            if(dateP.equals("0")) inEmployeeWindow(sb, s, employee);
            LocalDate date = null;
            while (date == null) {
                try {
                    date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                    System.out.println("Enter date on (dd/MM/yyyy):\n");
                    dateP = s.next();
                }
            }
            if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(6))) {
                System.out.println("which shift:\n" +
                        "1.morning\n" +
                        "2.night");
                int typeP = validateIntInput(s);
                boolean type = typeP != 1;

                try {
                    sb.EC.employeeSubmit(employee, date, type);
                    System.out.println("shift was submitted successfully");
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    submitAShift(sb, s, employee);

                }

            }else{
                System.out.println("Enter a date in the next week.");
            }
        }
    }

    public static void removeSubmission(ShiftBoard sb, Scanner s, Employee employee){
        System.out.println("shifts you can cancel this week:\n");
        Map<Integer, String> submittedShifts = new LinkedHashMap<>();
        StringBuilder options = new StringBuilder();
        int i=1;
        if(!employee.getAvailability().isEmpty()) {
            for (String shift :
                    employee.getAvailability()) {
                options.append(i).append(". ").append(shift.split(",")[0])
                        .append(" ,").append(Boolean.parseBoolean(shift.split(",")[1]) ? "night" : "morning")
                        .append("\n");
                submittedShifts.put(i, shift);
                i++;
            }
            System.out.println(options);
        }
        else
            System.out.println("no submitted shifts to remove");
        System.out.println("0. back to menu\n");
        System.out.println("enter option number to remove submission for that shift:\n");
        int choice = validateIntInput(s);
        if(choice!=0){
            String type = Boolean.parseBoolean(submittedShifts.get(choice).split(",")[1]) ? "night":"morning";
            System.out.println("1. to remove submission for "+ submittedShifts.get(choice).split(",")[0]+" ,"+type+" ?\n"+
                    "2.cancel selection\n");
            int save = validateIntInput(s);
            if(save == 1) {
                String dateP = submittedShifts.get(choice).split(",")[0];
                System.out.println(dateP);
                LocalDate date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                boolean typeB = Boolean.parseBoolean(submittedShifts.get(choice).split(",")[1]);
                sb.removeSubmit(employee, date, typeB);
                removeSubmission(sb, s, employee);

            }else{
                removeSubmission(sb, s, employee);
            }
        }else
            inEmployeeWindow(sb, s, employee);
    }
}
