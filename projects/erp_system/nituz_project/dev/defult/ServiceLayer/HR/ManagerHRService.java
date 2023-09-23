package defult.ServiceLayer.HR;

import defult.BusinessLayer.HRsystem.*;
import defult.BusinessLayer.Controllers.HR.ShiftBoard;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static defult.ServiceLayer.HR.HRService.HR_Main;
import static defult.ServiceLayer.HR.HRService.validateIntInput;
import static defult.BusinessLayer.HRsystem.Role.*;
import static defult.BusinessLayer.HRsystem.Role.CLEANER;

public class ManagerHRService {
    //HR manager

    public static void managerWindow(ShiftBoard sb, Scanner s) {
        try{
            sb.SC.alert();
        }catch (Exception ex){
            System.out.println(ex.getMessage()+"\n");
        }
        System.out.println("manager window(main):\n" +
                "1. add or edit or delete employee\n" +
                "2. schedule shift\n" +
                "3. schedule drivers to deliveries\n" +
                "4. block employee's shift\n" +
                "5. close branch on occasion\n" +
                "6. edit shift\n" +
                "7. get employee's hours and salary\n" +
                "8. get shift history\n" +
                "9. Main menu");
        int choice = validateIntInput(s);
        switch (choice) {
            case 1 -> editEmployees(sb, s);
            case 2 -> scheduleShift(sb, s);
            case 3 -> driversAndDeliveries(sb, s);
            case 4 -> blockEmployeeShift(sb, s);
            case 5 -> closeShiftOnDate(sb, s);
            case 6 -> editShift(sb, s);
            case 7 -> hoursAndSalary(sb, s);
            case 8 -> importShiftHistory(sb, s);
            case 9 -> HR_Main(sb, s);
            default -> {
                System.out.println("enter again");
                managerWindow(sb, s);
            }
        }
    }

    private static void importShiftHistory ( ShiftBoard sb, Scanner s ) {
        System.out.println("print shift history:\n" +
                "please enter branch id to print for: ");
        int branchId = validateIntInput(s);
        while(branchId>9 || branchId<1){
            System.out.println("Invalid branch, please enter again branch id (in rang 1-9):\n");
            branchId = validateIntInput(s);

        }
        System.out.println("please enter date to start the report from:\n");
        LocalDate startDate = validDate(s);
        System.out.println("please enter date to end the report on:\n");
        LocalDate endDate = validDate(s);
        List<String> history = sb.reportFullShiftHistory(branchId, startDate, endDate);
        for (String shift:
             history) {
            System.out.println(shift);
        }
    }
    static public LocalDate validDate(Scanner s){
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

    //case 1
    public static  void editEmployees(ShiftBoard sb, Scanner s) {
        System.out.println("Edit Employees window:\n" +
                "1.add new employee\n" +
                "2.edit existing employee\n" +
                "3.delete employee\n"+
                "4.back to menu\n");
        int choice = validateIntInput(s);
        switch (choice) {
            case 1 -> addNewEmployee(sb, s);
            case 2 -> editEmployee(sb, s);
            case 3 -> deleteEmployee(sb, s);
            case 4 -> managerWindow(sb, s);

            default -> {
                System.out.println("invalid input please enter again: ");
                editEmployees(sb, s);
            }
        }
    }

    public static void addNewEmployee(ShiftBoard sb, Scanner s) {
        System.out.println("1.add new employee\n" +
                "2.add new driver");
        int choice = validateIntInput(s);

        switch (choice) {
            case 1:addNewRegEmployee(sb,s);
            break;
            case 2: addNewRegDriver(sb,s);
            break;
            default:  System.out.println("invalid input please enter again: ");
                addNewEmployee(sb, s);
        }
    }
    public static void addNewRegEmployee(ShiftBoard sb, Scanner s) {
        System.out.println("add new employee:\n");
        System.out.println("Enter employee's name: \n");
        String name = s.next();

        System.out.println("\nEnter employee's ID:\n");
        int id =  validateIntInput(s);
        while (!sb.EC.validateId(id)) {
            System.out.println("Id is already registered, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) editEmployees(sb,s);
        }

        System.out.println("\nEnter password for employee\n");
        String password =  s.next();

        System.out.println("\nEnter employee's bank number(digits only):\n");
        int bankInfo =  validateIntInput(s);

        System.out.println("\nEnter employee's salary(digits only):\n");
        int salary =  validateIntInput(s);

        System.out.println("\nEnter employee's start date:\n");

        String startDateP = s.next();
        LocalDate startDate = null;
        while (startDate == null) {
            try {
                startDate = LocalDate.parse(startDateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                System.out.println("Enter employee's start date (dd/MM/yyyy):\n");
                startDateP = s.next();
            }
        }


        System.out.println("\nEnter employee terms(one at a time, when done enter 0):\n");
        List<String> terms  =  new ArrayList<>();

        String term = s.next();
        while(!term.equals("0")) {
            terms.add(term);
            term =s.next();
        }


        System.out.println("\nChoose employees qualifications:\n" +
                "1.Shift Manager\n" +
                "2.Cashier\n" +
                "3.Storage\n" +
                "4.Security\n" +
                "5.Cleaner\n" +
                "6.Organizer\n" +
                "7.General\n" +
                "Please enter roles numbers separated by a single comma (,).\n");

        List<String> roles  =  new ArrayList<>();
        String[] allR = {SHIFT_MANAGER.getRoleName(), CASHIER.getRoleName(), STORAGE.getRoleName(), SECURITY.getRoleName(), CLEANER.getRoleName(), ORGANIZER.getRoleName(), GENERAL.getRoleName()};
        String role = "";
        while(!role.matches("^[1-7](,[1-7])*$")) {
            role = s.next();
            if(role.matches("^[1-7](,[1-7])*$")) break;
            System.out.println("Invalid input. Please enter a comma-separated list of numbers between 1 and 7.\n" +
                    "please enter again:\n");
        }
        // Split the input by comma
        String[] sep = role.split(",");

        for (String r : sep) {
            // Trim any whitespace around the input
            r = r.trim();
            try {
                int index = Integer.parseInt(r) - 1;
                if (index >= 0 && index < allR.length) {
                    roles.add(allR[index]);
                } else {
                    System.out.println("Invalid role number: " + r);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + r);
                addNewEmployee(sb, s);
            }
        }
        System.out.println("roles added:\n" +
                roles);
        boolean isManager = (roles.contains(SHIFT_MANAGER.getRoleName()));

        System.out.println("\nEnter additional info about the employee:\n");
        String specialInfo = s.next();

        System.out.println(
                "1. save new Employee\n" +
                        "2. cancel");
        int save = validateIntInput(s);

        if(save == 1) {
            sb.EC.newEmployee(name, id, bankInfo, salary, startDate, terms, roles, isManager, specialInfo, password);
            System.out.println("Employee was added successfully");
            editEmployees(sb, s);
        }
        else if(save == 2){
            System.out.println("employee was not created");
            managerWindow(sb, s);

        }

    }

    public static void addNewRegDriver(ShiftBoard sb, Scanner s) {
        System.out.println("add new employee:\n");
        System.out.println("Enter employee's name: \n");
        String name = s.next();

        System.out.println("\nEnter employee's ID:\n");
        int id =  validateIntInput(s);
        while (!sb.EC.validateId(id)) {
            System.out.println("Id is already registered, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) editEmployees(sb,s);
        }

        System.out.println("\nEnter password for employee\n");
        String password =  s.next();

        System.out.println("\nEnter employee's bank number(digits only):\n");
        int bankInfo =  validateIntInput(s);

        System.out.println("\nEnter employee's salary(digits only):\n");
        int salary =  validateIntInput(s);

        System.out.println("\nEnter employee's start date:\n");

        String startDateP = s.next();
        LocalDate startDate = null;
        while (startDate == null) {
            try {
                startDate = LocalDate.parse(startDateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                System.out.println("Enter employee's start date (dd/MM/yyyy):\n");
                startDateP = s.next();
            }
        }


        System.out.println("\nEnter employee terms(one at a time, when done enter 0):\n");
        List<String> terms  =  new ArrayList<>();

        String term = s.next();
        while(!term.equals("0")) {
            terms.add(term);
            term =s.next();
        }
        List<String> licenses = new ArrayList<>();
        licenses.addAll(Arrays.stream(new String[]{"A1", "B1", "C1"}).toList());
        List<String> driverLicenses = new ArrayList<>();

        for(int i=0; i<licenses.size(); i++){
            System.out.println("1. "+name+" has a "+ licenses.get(i)+" license\n" +
                    "2. no");
            int hasILicence = validateIntInput(s);
            if (hasILicence==1)
                driverLicenses.add(licenses.get(i));
        }


        List<String> roles = new ArrayList<>();
        roles.add("Driver");

        System.out.println("\nEnter additional info about the employee:\n");
        String specialInfo = s.next();

        System.out.println(
                "1. save new Employee\n" +
                        "2. cancel");
        int save = validateIntInput(s);

        if(save == 1) {
            sb.EC.newDriver(name, id, bankInfo, salary, startDate, terms, roles, false, specialInfo, password,driverLicenses);
            System.out.println("Driver was added successfully");
            editEmployees(sb, s);
        }
        else if(save == 2){
            System.out.println("Driver was not created");
            managerWindow(sb, s);

        }

    }

    public static void editEmployee(ShiftBoard sb, Scanner s){
        System.out.println("edit existing employee:\n");
        System.out.println("\nEnter employee's ID:\n");
        int id =  validateIntInput(s);
        while (sb.EC.validateId(id)) {
            System.out.println("Id dose not exist, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) editEmployees(sb,s);
        }
        Employee employee = sb.EC.getEmployeeById(id);

        System.out.println("\nwhat do you wish to edit?\n" +
                "1. salary\n" +
                "2. bank info\n" +
                "3. special info\n" +
                "4. terms\n" +
                "5. roles\n");
        int choice = validateIntInput(s);
        switch (choice) {
            case 1 -> {
                System.out.println("Current salary is:\n" +
                        employee.getSalary() + "\n" +
                        "please enter new salary:\n");
                int newSalary = validateIntInput(s);
                try {
                    sb.EC.editEmployeeSalary(id, newSalary);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            case 2 -> {
                System.out.println("Current bank number is:\n" +
                        employee.getBankInfo() + "\n" +
                        "please enter new bank number:\n");
                int newBank = validateIntInput(s);
                sb.EC.editEmployeeBankInfo(id, newBank);
            }
            case 3 -> {
                System.out.println("Current additional info is:\n" +
                        employee.getSpecialInfo() + "\n" +
                        "please enter what you want to add:\n");
                String moreSpecialInfo = s.next();
                sb.EC.editEmployeeSpecialInfo(id, employee.getSpecialInfo() + "\n" + moreSpecialInfo);
            }
            case 4 ->
                    //terms
                    editTerms(sb, s, employee);
            case 5 -> {
                //roles
                if (employee.getRoles().contains("Driver")) {
                    System.out.println("This is a driver, you can not edit roles.");
                    editEmployees(sb, s);

                } else
                    editRoles(sb, s, employee);
            }
            default -> {
                System.out.println("invalid input please enter again: ");
                editEmployees(sb, s);
            }
        }
        System.out.println("1.keep editing\n" +
                "2.back to menu");
        int choiceLast = validateIntInput(s);
        if(choiceLast==1) editEmployees(sb, s);
        else managerWindow(sb, s);
    }

    public static void editTerms(ShiftBoard sb, Scanner s, Employee employee){
        List<String> terms = employee.getTerms();
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (String term : terms) {
            options.append(i).append(". ").append(term).append("\n");
            i++;
        }
        String optionsString = options.toString();
        System.out.println("current terms are: \n" +
                optionsString);
        System.out.println("do you want to?\n" +
                "-1 to go back\n" +
                "0. add\n" +
                "to delete enter term's number");
        int choice = validateIntInput(s);
        if(choice == 0) {
            System.out.println("enter new term: \n");
            String newTerm = s.next();
            sb.EC.addEmployeeTerms(employee.getId(), newTerm);
        }else if(choice == -1){
            editEmployees(sb, s);
        }else{
            sb.EC.deleteEmployeeTerms(employee.getId(), choice-1);
        }

    }

    public static void editRoles(ShiftBoard sb, Scanner s, Employee employee){
        List<String> roles = employee.getRoles();
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (String role : roles) {
            options.append(i).append(". ").append(role).append("\n");
            i++;
        }
        String optionsString = options.toString();
        System.out.println("current roles are: \n" +
                optionsString);
        System.out.println("do you want to?\n" +
                "-1 to go back\n" +
                "0. add\n" +
                "to delete enter role's number");
        int choice = validateIntInput(s);
        if(choice == 0) {
            System.out.println("choose new role: \n");
            String[] allR = {SHIFT_MANAGER.getRoleName(), CASHIER.getRoleName(), STORAGE.getRoleName(), SECURITY.getRoleName(), CLEANER.getRoleName(), ORGANIZER.getRoleName(), GENERAL.getRoleName()};

            List<String> leftOptions = new ArrayList<>();
            for (String role : allR) {
                if (!roles.contains(role)) {
                    leftOptions.add(role);
                }
            }
            StringBuilder leftOptionsS = new StringBuilder();
            int index =1;
            for (String role : leftOptions) {
                leftOptionsS.append(index).append(". ").append(role).append("\n");
                index++;
            }
            System.out.println(leftOptionsS);
            int newRoleP = validateIntInput(s);
            String newRole = leftOptions.get(newRoleP-1);
            sb.EC.addEmployeeRole(employee.getId(), newRole);
        }else if(choice == -1){
            editEmployees(sb, s);
        }else{
            sb.EC.deleteEmployeeRole(employee.getId(), choice-1);
        }

    }

    public static void deleteEmployee(ShiftBoard sb, Scanner s){
        System.out.println("delete existing employee:\n");
        System.out.println("\nEnter employee's ID:\n");
        int id =  validateIntInput(s);
        while (sb.EC.validateId(id)) {
            System.out.println("Id dose not exist, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) editEmployees(sb,s);
        }
        try{
            sb.SC.canDeleteEmployee(sb.EC.getEmployeeById(id));

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            editEmployees(sb, s);

        }
        System.out.println("are you sure you want to delete this employee?\n"+
                "1. yes\n" +
                "2. no");
        int save = validateIntInput(s);

        if(save == 1) {
            sb.EC.removeEmployee(id);
            System.out.println("Employee was removed successfully");
            managerWindow(sb, s);
        }
        else if(save == 2){
            System.out.println("employee was not removed");
            managerWindow(sb, s);

        }
    }


    //case 2
    public static void scheduleShift(ShiftBoard sb, Scanner s) {

        //date and type
        System.out.println("schedule shift:\n" +
                "enter date to schedule (0 to go back): \n");
        String dateP = s.next();
        if(dateP.equals("0")) managerWindow(sb,s);
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                System.out.println("enter date to schedule (dd/MM/yyyy):\n");
                dateP = s.next();
            }
        }

        if (!(date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(6)))){
            System.out.println("the date you entered is not in range.");
            scheduleShift(sb, s);
        }
        System.out.println("choose: \n" +
                "1. day shift\n" +
                "2. night shift\n");
        int typeP = validateIntInput(s);
        boolean type = typeP != 1; //day is false, night is true;
        //--------------------------------------------------

        //branch
        System.out.println("enter branch number:\n");
        int branchId = validateIntInput(s);
        while (branchId<=1|| branchId>=11){
            System.out.println("branch is not exist, please enter branch ID again (id is 2 to 10)");
            branchId = validateIntInput(s);
        }
        try {
            sb.validateSchedulePossible(date, type, branchId);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            System.out.println("do you want:\n"+
                    "1. edit a shift\n"+
                    "2. Schedule a shift");
            int choise = validateIntInput(s);
            switch (choise) {
                case (1) -> {
                    editShift(sb, s);
                    managerWindow(sb, s);
                }
                case (2) -> {
                    scheduleShift(sb, s);
                    managerWindow(sb, s);
                }
            }
        }
        System.out.println("\nenter shift start time: ");
        String startShiftP = "";
        boolean startGood = false;
        LocalTime startShift = LocalTime.parse("00:00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!startGood) {
            startShiftP = s.next();
            try {
                startShift = LocalTime.parse(startShiftP, formatter); // parse the string into a LocalTime object
                //System.out.println("Parsed time: " + startShift);
                startGood = true;
            } catch (DateTimeParseException e) {
                System.out.println("enter shift start time again in format: HH:mm ");
            }
        }


        System.out.println("\nenter shift end time: ");
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
                System.out.println("enter shift end time again in format: HH:mm ");
            }
        }

        //--------------------------------------------------



        Map<Employee, String> stuff = new LinkedHashMap<>();
        stuff = employeeChoosing(sb, s, date, type, stuff, startShift, endShift, branchId);
        //--------------------------------------------------

        //the employees you chose:
        System.out.println("The employees you chose: \n" +
                choiceString(stuff));

        try {
            boolean isDelArrive = !sb.isDeliveryOnShift(date, startShift, endShift, branchId, true).isEmpty();
            sb.SC.createShift(date, type, stuff, branchId, startShift, endShift, isDelArrive);
            System.out.println("saved successfully");
            managerWindow(sb, s);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
            scheduleShift(sb, s);

        }


    }


    public static Map<Employee, String> employeeChoosing(ShiftBoard sb, Scanner s, LocalDate date, boolean type, Map<Employee, String> stuff, LocalTime start, LocalTime end, int branchId){
        //employees for the shift
        String notMust = "0. if not needed for shift"; //for every non-mandatory

        System.out.println("choose employees:\n");
        Map<Employee, List<String>> allAvailable = sb.EC.whoIsAvailable(date, type);

        try {
            Map<Employee, List<String>> managers  = sb.AvailableManagers(allAvailable);

            if(!stuff.containsValue(SHIFT_MANAGER.getRoleName())){
                System.out.println("Shift Manager: \n"+makeString(managers));
                int managerP = validateIntInput(s);
                List<Employee> optionsM = makeList(managers);
                unAvailableEmployee(optionsM.get(managerP-1), allAvailable, stuff, SHIFT_MANAGER.getRoleName(), date, type, sb, branchId);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        List<DeliveryTuple> deliveriesOnShiftS = sb.isDeliveryOnShift(date, start, end, branchId, true);
        List<DeliveryTuple> deliveriesOnShiftD = sb.isDeliveryOnShift(date, start, end, branchId, false);


        int storage =-1;
        if(deliveriesOnShiftS.isEmpty()) {
            try {
                while (storage != 0) {
                    Map<Employee, List<String>> storages = sb.AvailableStorage(allAvailable);
                    String role = STORAGE.getRoleName();
                    System.out.println(role + ": \n" + makeString(storages) + notMust);
                    List<Employee> options = makeList(storages);
                    storage = validateIntInput(s);
                    if (storage != 0)
                        unAvailableEmployee(options.get(storage - 1), allAvailable, stuff, role, date, type, sb, branchId);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }else {
            try {
                while (storage != 0) {
                    Map<Employee, List<String>> storages = sb.AvailableStorage(allAvailable);
                    String role = STORAGE.getRoleName();
                    System.out.println(role + ": \n" + makeString(storages));
                    List<Employee> options = makeList(storages);
                    storage = validateIntInput(s);
                    unAvailableEmployee(options.get(storage - 1), allAvailable, stuff, role, date, type, sb, branchId);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        if(!deliveriesOnShiftD.isEmpty()){
            int driver = -1;
            for(DeliveryTuple dt:
                    deliveriesOnShiftD) {
                System.out.println("Delivery ID: "+dt.getDeliveryId());
                try {
                    //while (driver != 0) {
                        Map<Driver, List<String>> drivers = sb.AvailableDrivers(allAvailable, dt.getLicense());
                        String role = DRIVER.getRoleName();
                        System.out.println(role + ": \n" + makeStringD(drivers));
                        List<Driver> options = makeListD(drivers);
                        driver = validateIntInput(s);
                        Driver driverSend = options.get(driver - 1);
                        unAvailableEmployee(options.get(driver - 1), allAvailable, stuff, role, date, type, sb, branchId);
                        sb.connectDriverToDelivery(driverSend, dt.getDeliveryId());
                    //}
                }catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        }

        int cashier =-1;
        try {
            while (cashier != 0) {
                Map<Employee, List<String>> cashiers = sb.AvailableCashier(allAvailable);
                String role = CASHIER.getRoleName();
                System.out.println(role + ": \n" + makeString(cashiers) + notMust);
                List<Employee> options = makeList(cashiers);
                cashier = validateIntInput(s);
                if (cashier!=0)
                    unAvailableEmployee(options.get(cashier - 1), allAvailable, stuff, role, date, type, sb, branchId);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        int general =-1;
        try {
            while(general !=0) {

                Map<Employee, List<String>> generals  = sb.AvailableGeneral(allAvailable);
                String role = GENERAL.getRoleName();
                System.out.println(role+": \n" + makeString(generals) + notMust);
                List<Employee> options = makeList(generals);
                general = validateIntInput(s);
                if (general!=0)
                    unAvailableEmployee(options.get(general-1), allAvailable, stuff, role, date, type, sb, branchId);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        int security =-1;
        try {
            while(security !=0) {
                Map<Employee, List<String>> securities  = sb.AvailableSecurity(allAvailable);
                String role = SECURITY.getRoleName();
                System.out.println(role+": \n" + makeString(securities) + notMust);
                List<Employee> options = makeList(securities);
                security = validateIntInput(s);
                if(security!=0)
                    unAvailableEmployee(options.get(security-1), allAvailable, stuff, role, date, type, sb, branchId);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        int cleaner =-1;
        try {
            while(cleaner !=0) {
                Map<Employee, List<String>> cleaners  = sb.AvailableCleaner(allAvailable);
                String role = CLEANER.getRoleName();
                System.out.println(role+": \n" + makeString(cleaners) + notMust);
                List<Employee> options = makeList(cleaners);
                cleaner = validateIntInput(s);
                if (cleaner!=0)
                    unAvailableEmployee(options.get(cleaner-1), allAvailable, stuff, role, date, type, sb, branchId);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        int organizer =-1;
        try {
            while(organizer !=0) {
                Map<Employee, List<String>> organizers  = sb.AvailableOrganizer(allAvailable);
                String role = ORGANIZER.getRoleName();
                System.out.println(role+": \n" + makeString(organizers) + notMust);
                List<Employee> options = makeList(organizers);
                organizer = validateIntInput(s);
                if (organizer!=0)
                    unAvailableEmployee(options.get(organizer-1), allAvailable, stuff, role, date, type, sb, branchId);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        //--------------------------------------------------

        return stuff;

    }

    public static void unAvailableEmployee(Employee employeeToRemove, Map<Employee, List<String>> allAvailable, Map<Employee, String> stuff, String role, LocalDate date, boolean type, ShiftBoard sb, int branchId){
        allAvailable.remove(employeeToRemove);
        sb.EC.setEmployeeNotAvailable(employeeToRemove, date, type);
        if(!employeeToRemove.isDriver(new HashMap<>())) {
            if (sb.SC.isShiftExist(date, type, branchId)){
                sb.SC.updateStuffInDB(date, type, branchId, employeeToRemove.getId(), role);
            }
            stuff.put(employeeToRemove, role);
        }
    }

    public static String makeString(Map<Employee, List<String>> employeeListMap){
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (Map.Entry<Employee, List<String>> entry : employeeListMap.entrySet()) {
            Employee employee = entry.getKey();
            List<String> roles = entry.getValue();
            options.append(i).append(". ").append(employee.getName()).append(" - roles: ").append(String.join(", ", roles)).append("\n");
            i++;
        }
        String optionsString = options.toString();
        return optionsString;
    }

    public static String makeStringD(Map<Driver, List<String>> employeeListMap){
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (Map.Entry<Driver, List<String>> entry : employeeListMap.entrySet()) {
            Employee employee = entry.getKey();
            List<String> roles = entry.getValue();
            options.append(i).append(". ").append(employee.getName()).append(" - licenses: ").append(String.join(", ", roles)).append("\n");
            i++;
        }
        String optionsString = options.toString();
        return optionsString;
    }

    public static String choiceString(Map<Employee, String> employeeRoleMap){
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (Map.Entry<Employee, String> entry : employeeRoleMap.entrySet()) {
            Employee employee = entry.getKey();
            String role = entry.getValue();
            options.append(i).append(". ").append(employee.getName()).append(" - role: ").append(role).append("\n");
            i++;
        }
        String optionsString = options.toString();
        return optionsString;
    }

    public static List<Employee> makeList(Map<Employee, List<String>> employeeListMap){
        List<Employee> out= new ArrayList<>();
        for (Map.Entry<Employee, List<String>> entry : employeeListMap.entrySet()) {
            Employee employee = entry.getKey();
            out.add(employee);
        }
        return out;
    }

    public static List<Driver> makeListD(Map<Driver, List<String>> employeeListMap){
        List<Driver> out= new ArrayList<>();
        for (Map.Entry<Driver, List<String>> entry : employeeListMap.entrySet()) {
            Driver employee = entry.getKey();
            out.add(employee);
        }
        return out;
    }


    //case 3
    private static void driversAndDeliveries(ShiftBoard sb, Scanner s) {
//        for(int i=0; i<6; i++){
//            allAvailable.putAll(sb.EC.whoIsAvailable(now.plusDays(i+1), false));
//            allAvailable.putAll(sb.EC.whoIsAvailable(now.plusDays(i+1), true));
//            deliveriesOnShiftD.addAll(sb.isDeliveryOnShift(now.plusDays(i+1), dayStart, dayEnd, -1, false));
//        }
        System.out.println("choose drivers to deliveries:\n");
        LocalDate now = LocalDate.now();
        LocalTime dayStart = LocalTime.of(6,0);
        LocalTime dayEnd = LocalTime.of(16,0);
        LocalTime nightStart = LocalTime.of(16,0);
        LocalTime nightEnd = LocalTime.of(23,59);

        for(int i=0; i<6; i++) {
            Map<Employee, List<String>> allAvailableMorning = sb.EC.whoIsAvailable(now.plusDays(i), false);
            Map<Employee, List<String>> allAvailableNight = sb.EC.whoIsAvailable(now.plusDays(i), true);

            //allAvailable.putAll(sb.EC.whoIsAvailable(now, true));
            List<DeliveryTuple> deliveriesOnShiftMorning = sb.isDeliveryOnShift(now.plusDays(i), dayStart, dayEnd, -1, false);
            List<DeliveryTuple> deliveriesOnShiftNight = sb.isDeliveryOnShift(now.plusDays(i), nightStart, nightEnd, -1, false);


            int driver = -1;
            for (DeliveryTuple dt :
                    deliveriesOnShiftMorning) {
                try {
                    //while (driver != 0) {
                    System.out.println(sb.DC.getDeliveryByID(dt.getDeliveryId()).toStringForDrivers());
                    Map<Driver, List<String>> drivers = sb.AvailableDrivers(allAvailableMorning, dt.getLicense());
                    String role = DRIVER.getRoleName();
                    System.out.println(role + ": \n" + makeStringD(drivers));
                    List<Driver> options = makeListD(drivers);
                    driver = validateIntInput(s);
                    Driver driverSend = options.get(driver - 1);

                    unAvailableEmployee(options.get(driver - 1), allAvailableMorning, new HashMap<>(), role, dt.getArrivalTime().toLocalDate(), false, sb, -1);
                    sb.connectDriverToDelivery(driverSend, dt.getDeliveryId());
                    //}
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            driver = -1;
            for (DeliveryTuple dt :
                    deliveriesOnShiftNight) {
                try {
                    //while (driver != 0) {
                    System.out.println(sb.DC.getDeliveryByID(dt.getDeliveryId()).toStringForDrivers());
                    Map<Driver, List<String>> drivers = sb.AvailableDrivers(allAvailableNight, dt.getLicense());
                    String role = DRIVER.getRoleName();
                    System.out.println(role + ": \n" + makeStringD(drivers));
                    List<Driver> options = makeListD(drivers);
                    driver = validateIntInput(s);
                    Driver driverSend = options.get(driver - 1);

                    unAvailableEmployee(options.get(driver - 1), allAvailableNight, new HashMap<>(), role, dt.getArrivalTime().toLocalDate(), false, sb, -1);
                    sb.connectDriverToDelivery(driverSend, dt.getDeliveryId());
                    //}
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    //case 4
    public static void blockEmployeeShift(ShiftBoard sb, Scanner s){
        System.out.println("block Employee's shifts:\n" +
                "enter employee id:\n");
        int id =  validateIntInput(s);
        while (sb.EC.validateId(id)) {
            System.out.println("Id dose not exist, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) managerWindow(sb,s);
        }
        System.out.println("enter date to block");
        String dateP = s.next();
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                System.out.println("Enter date to block (dd/MM/yyyy):\n");
                dateP = s.next();
            }
        }
        System.out.println("choose: \n" +
                "1. day shift\n" +
                "2. night shift\n");
        int typeP = validateIntInput(s);
        boolean type = typeP != 1; //day is false, night is true;
        try {
            sb.EC.validateCanBlock(id, date, type);
        }catch (Exception e){
            System.out.println(e.getMessage());
            managerWindow(sb, s);
        }

        System.out.println("are you sure you want to block this employee on "+dateP+"?\n"+
                "1. yes\n" +
                "2. no");
        int save = validateIntInput(s);
        if(save == 1) {
            sb.EC.blockEmployee(id, dateP+","+type);
            System.out.println("Employee was blocked successfully");
            managerWindow(sb, s);
        }
        else if(save == 2){
            System.out.println("employee was not blocked");
            managerWindow(sb, s);
        }
    }


    //case 5
    public static void closeShiftOnDate(ShiftBoard sb, Scanner s){
        System.out.println("Close shift on a date:\n");
        System.out.println("Enter date to close on");
        String dateP = s.next();
        LocalDate date = null;
        while (date == null) {
            try {
                date = LocalDate.parse(dateP, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in dd/MM/yyyy format.");
                System.out.println("Enter date to close on (dd/MM/yyyy):\n");
                dateP = s.next();
            }
        }
        System.out.println("Choose: \n" +
                "1. Day shift\n" +
                "2. Night shift\n");
        int typeP = validateIntInput(s);
        String shiftClosed = dateP+","+typeP;

        boolean type = typeP != 1; //day is false, night is true;

        System.out.println("Enter branch id or enter 0 to close all branches:\n");
        int branchId = validateIntInput(s);
        while (branchId<=1|| branchId>=11){
            System.out.println("Branch is not exist, please enter branch ID again (id is 2 to 10)");
            branchId = validateIntInput(s);
        }

        System.out.println("Are you sure you want to close this shift on "+dateP+"?\n"+
                "1. Yes\n" +
                "2. No");
        int save = validateIntInput(s);
        if(save == 1) {
            if(branchId==0){
                sb.closeAllBranches(date, type);
            }else {
                sb.addToCloseShift(date, type, branchId);
                Shift shiftToDelete = sb.SC.getShiftByDateType(date, type, branchId);
                int indexToRemove = sb.SC.getShifts().indexOf(shiftToDelete);
                //sb.cancelShift(shiftToDelete);
            }
            System.out.println("Shift was closed successfully");
            managerWindow(sb, s);
        }
        else if(save == 2){
            System.out.println("Shift was not closed");
            managerWindow(sb, s);
        }

    }

    //case 6
    public static void editShift(ShiftBoard sb, Scanner s){
        System.out.println("edit shift:\n");
        List<Shift> shifts = new ArrayList<>();
        for (Shift shift:
                sb.SC.getShifts()) {
            if(shift.isDone()) shifts.add(shift);
        }
        String options = makeStringShift(shifts);
        System.out.println(options);
        System.out.println("0. to go back");
        int choice = validateIntInput(s);
        if(choice == 0) managerWindow(sb, s);
        Shift chosenShift = shifts.get(choice-1);
        System.out.println("current shift employees:\n");
        Map<Employee, String> employeeListMap = chosenShift.getFinalEmployees();
        String optionsE = makeStringE(employeeListMap);
        System.out.println(optionsE);
        System.out.println("\ndo you want to\n" +
                "1.add employee\n" +
                "2.remove employee\n" +
                "3.done");
        int choice2 = validateIntInput(s);
        switch (choice2) {
            case 1 -> {
                Map<Employee, String> updatedStuff = employeeChoosing(sb, s, chosenShift.getDate(), chosenShift.isShiftType(), chosenShift.getFinalEmployees(), chosenShift.getShiftStart(), chosenShift.getShiftEnd(), chosenShift.getBranch());

                editShift(sb, s);
            }
            case 2 -> {
                System.out.println("choose employee to remove:\n");
                System.out.println(makeStringE(chosenShift.getFinalEmployees()));
                int choice3 = validateIntInput(s);
                List<Employee> optionsToRemove = new ArrayList<>(chosenShift.getFinalEmployees().keySet());
                sb.removeEmployeeFromShift(optionsToRemove.get(choice3 - 1), chosenShift);
                if (!chosenShift.getFinalEmployees().containsValue(SHIFT_MANAGER.getRoleName())) {
                    System.out.println("you need to choose shift manager");
                    Map<Employee, List<String>> allAvailable = sb.EC.whoIsAvailable(chosenShift.getDate(), chosenShift.isShiftType());
                    try {
                        Map<Employee, List<String>> managers = sb.AvailableManagers(allAvailable);

                        if (!chosenShift.getFinalEmployees().containsValue(SHIFT_MANAGER.getRoleName())) {
                            System.out.println("Shift Manager: \n" + makeString(managers));
                            int managerP = validateIntInput(s);
                            List<Employee> optionsM = makeList(allAvailable);
                            unAvailableEmployee(optionsM.get(managerP - 1), allAvailable, chosenShift.getFinalEmployees(), SHIFT_MANAGER.getRoleName(), chosenShift.getDate(), chosenShift.isShiftType(), sb, chosenShift.getBranch());
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }
                System.out.println(optionsToRemove.get(choice3 - 1).getName() + " was removed from shift\n");
                editShift(sb, s);
            }
            case 3 -> managerWindow(sb, s);
            default -> {
                System.out.println("invalid input please enter again: ");
                editShift(sb, s);
            }
        }

    }

    public static String makeStringShift(List<Shift> shiftsToEdit){
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (Shift shift : shiftsToEdit) {
            options.append(i).append(". ").append(shift.getDate()).append(!shift.isShiftType()? " morning":" night").append(" branch ID:"+shift.getBranch()).append("\n");
            i++;
        }
        String optionsString = options.toString();
        return optionsString;
    }

    public static String makeStringE(Map<Employee, String> employeeListMap){
        StringBuilder options = new StringBuilder();
        int i = 1;
        for (Map.Entry<Employee, String> entry : employeeListMap.entrySet()) {
            Employee employee = entry.getKey();
            String roles = entry.getValue();
            options.append(i).append(". ").append(employee.getName()).append(" - roles: ").append(roles).append("\n");
            i++;
        }
        String optionsString = options.toString();
        return optionsString;
    }

    //case 7
    public static void hoursAndSalary(ShiftBoard sb, Scanner s){

        System.out.println("\nEnter employee's ID, or 0 to go back to menu\n");
        int id =  validateIntInput(s);
        if (id == 0) managerWindow(sb, s);
        while (sb.EC.validateId(id)) {
            System.out.println("Id is not registered, do you want to:\n" +
                    "1.back to menu\n" +
                    "enter id again:");
            id = validateIntInput(s);
            if(id == 1) managerWindow(sb,s);
        }

        Employee employee = sb.EC.getEmployeeById(id);


        YearMonth yearMonth = null;
        while (yearMonth == null) {
            System.out.println("Enter month:");
            String month = s.next();
            System.out.println("Enter Year:");
            String year = s.next();
            try {
                int bonus = 0;
                yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
                if (yearMonth.equals(YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth()))
                        && !sb.isBonus(employee, yearMonth)) {
                    System.out.println("do you want to add a bonus to " + employee.getName() + "?\n" +
                            "1. yes\n" +
                            "2. no\n");
                    int isBonus = validateIntInput(s);
                    if (isBonus == 1) {
                        System.out.println("enter bonus:");
                        bonus = validateIntInput(s);
                        try {
                            sb.bonusEmployee(employee, bonus, yearMonth);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            hoursAndSalary(sb, s);
                        }
                    }
                }

                System.out.println(employee.getName() + "'s Salary: \n" +
                        "hourly rate: " + employee.getSalary() + "\n" +
                        "bonus: " + employee.getBonuses().get(yearMonth) + "\n" +
                        "hours: " + sb.SC.hoursReport(employee, yearMonth) + "\n" +
                        "total: " + sb.calcSalary(employee, yearMonth));

                hoursAndSalary(sb, s);
                // }
            } catch (Exception ex) {
                System.out.println("Year or month is not valid");

            }

        }


    }
}
