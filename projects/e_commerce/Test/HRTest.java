import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.HRsystem.*;
import defult.BusinessLayer.HRsystem.Employee;

import defult.ServiceLayer.Delivery.DeliveryService;
import defult.ServiceLayer.Singletones.Generator;
import defult.ServiceLayer.Singletones.Validator;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

import static defult.Main.initData;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HRTest {
    static Validator validator = Validator.getInstance();
    static DeliveryService deliveryService = new DeliveryService(validator);
    static ShiftBoard sb = ShiftBoard.getInstance();
    static Generator generator = Generator.getInstance();
    LocalDate now = LocalDate.now();
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    LocalTime dayShiftStart = LocalTime.of(9,0);
    LocalTime dayShiftEnd = LocalTime.of(16,0);
    static Employee nofar;
    static Employee noa;
    static Employee amit;
    static Employee gili;


    static String[] roles = {"Shift Manager", "Cashier", "Storage", "Security", "Cleaner", "Organizer", "General"};

    static List<String> shiftManagerStorage = new ArrayList<>();
    static List<String> terms1 = new ArrayList<>();
    static List<String> fictiveDriverLicenses = new ArrayList<>();


    @BeforeAll
    static void setup(){
        initData();

//        generator.generate_HR_Env(sb);
//        generator.generate_Delivery_Env(deliveryService);
        nofar = sb.EC.getEmployeeById(206962516);
        noa = sb.EC.getEmployeeById(213890858);
//        amit = sb.EC.getEmployeeById(123456);
        gili = sb.EC.getEmployeeById(1234567);
//        shiftManagerStorage.add(roles[0]);
//        shiftManagerStorage.add(roles[2]);
//        shiftManagerStorage.add(roles[6]);
//        terms1.add("money");
        fictiveDriverLicenses.add("C1");
        fictiveDriverLicenses.add("A1");

    }
    @Test
    void testAddNewEmployee(){
        if(sb.EC.getEmployees().containsKey(333333))
            sb.EC.removeEmployee(333333);
        sb.EC.newEmployee("testWorker", 333333, 0, 45, LocalDate.of(2022, 10, 2), terms1, shiftManagerStorage, true, "no special info", "123456789");
        Employee testWorker = sb.EC.getEmployeeById(333333);

        Assertions.assertTrue(sb.EC.getEmployees().containsValue(testWorker),
                "the added employee was not found in  the controller");
    }

    @Before
    public Employee newEmp(){
        if(sb.EC.getEmployees().containsKey(333333))
            sb.EC.removeEmployee(333333);
        sb.EC.newEmployee("testWorker", 333333, 0, 45, LocalDate.of(2022, 10, 2), terms1, shiftManagerStorage, true, "no special info", "123456789");
        return sb.EC.getEmployeeById(333333);
    }
    @Test
    void testEmployeeSubmitShift(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Employee newEmp= newEmp();

        try {
            sb.EC.employeeSubmit(newEmp, tomorrow, type);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Assertions.assertTrue(sb.EC.whoIsAvailable(tomorrow, type).keySet().stream().anyMatch(e2 -> e2.equals(newEmp)),
                "the employee was not found in the available for the shift they submitted");
    }

    @Test
    void testCreateNewShift(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {
            if(sb.SC.isShiftExist(tomorrow, type, 1))
                sb.SC.cancelShift(sb.SC.getShiftByDateType(tomorrow, type, 1));
            sb.SC.createShift(tomorrow, type, employees, 1, dayShiftStart, dayShiftEnd, false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(tomorrow, type, 1);

        Assertions.assertTrue(sb.SC.isShiftExist(tomorrow, type, 1),
                "the shift does not exist");

        for (Employee e : employees.keySet()) {
            for (Employee e3:
                 shift.getFinalEmployees().keySet()) {
            }
            Assertions.assertTrue(shift.getFinalEmployees().keySet().stream().anyMatch(e2 -> e2.equals(e)),
                    "the employee " + e.getName() + " is not in the shift");
        }


    }


    @Test
    void testRemoveEmployeeFromShift(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {
            if(sb.SC.isShiftExist(tomorrow, type, 1))
                sb.SC.cancelShift(sb.SC.getShiftByDateType(tomorrow, type, 1));
            sb.SC.createShift(tomorrow, type, employees, 1, dayShiftStart, dayShiftEnd,false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(tomorrow, type, 1);

        Assertions.assertTrue(shift.getFinalEmployees().keySet().stream().anyMatch(e -> e.equals(gili)),
                "the shift does not contain that employee");

        sb.removeEmployeeFromShift(gili, shift);

        Assertions.assertFalse(shift.getFinalEmployees().keySet().stream().anyMatch(e -> e.equals(gili)),
                "the shift still contains that employee");
    }

    @Test
    void testAddEmployeeToShift(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {
            if(sb.SC.isShiftExist(tomorrow, type, 1))
                sb.SC.cancelShift(sb.SC.getShiftByDateType(tomorrow, type, 1));
            sb.SC.createShift(tomorrow, type, employees, 1, dayShiftStart, dayShiftEnd, false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(tomorrow, type, 1);

        Assertions.assertFalse(shift.getFinalEmployees().keySet().stream().anyMatch(e -> e.equals(noa)),
                "the shift already contains that employee");

        Map<Employee, String> addE = new LinkedHashMap<>();
        addE.put(noa, noa.getRoles().get(2));
        sb.addEmployeesToShift(addE, shift);

        Assertions.assertTrue(shift.getFinalEmployees().keySet().stream().anyMatch(e -> e.equals(noa)),
                "the shift does not contain that employee");
    }

    @Test
    void testSalary(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));

        if(sb.SC.isShiftExist(tomorrow, type, 1))
            sb.SC.cancelShift(sb.SC.getShiftByDateType(tomorrow, type, 1));
        int salaryBefore = sb.calcSalary(nofar, YearMonth.now());

        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {

            sb.SC.createShift(tomorrow, type, employees, 1, dayShiftStart, dayShiftEnd, false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        Shift shift = sb.SC.getShiftByDateType(tomorrow, type, 1);

        Assertions.assertNotEquals(sb.calcSalary(nofar, YearMonth.now()), salaryBefore,
                "the salary did not change");
    }

    @Test
    void testBonus(){

        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {
            if(sb.SC.isShiftExist(tomorrow, type, 1))
                sb.SC.cancelShift(sb.SC.getShiftByDateType(tomorrow, type, 1));
            sb.SC.createShift(tomorrow, type, employees, 1, dayShiftStart, dayShiftEnd, false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(tomorrow, type, 1);

        int salaryBefore = sb.calcSalary(nofar, YearMonth.now());

        try {
            sb.bonusEmployee(nofar, 2000, YearMonth.now());
            Assertions.assertNotEquals(sb.calcSalary(nofar, YearMonth.now()), salaryBefore,
                    "the salary did not change");
        }catch (Exception ex){

        }

    }

    @Test
    void testCancelShift(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {
            if(sb.SC.isShiftExist(now, type, 1))
                sb.SC.cancelShift(sb.SC.getShiftByDateType(now, type, 1));
            sb.SC.createShift(now, type, employees, 1, dayShiftStart, dayShiftEnd, false);
            sb.EC.getEmployeeById(206962516).setNotAvailable(LocalDate.now(),type);
            sb.EC.getEmployeeById(1234567).setNotAvailable(LocalDate.now(),type);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(now, type, 1);

        Assertions.assertFalse(sb.EC.whoIsAvailable(now, type).keySet().stream().anyMatch(e -> e.equals(gili)),
                "employee shouldn't be available");

        sb.SC.cancelShift(shift);

        Assertions.assertTrue(sb.EC.whoIsAvailable(now, type).keySet().stream().anyMatch(e -> e.equals(gili)),
                "employee should be available");
    }

    @Test
    void testCancelTransaction() {
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(2));
        employees.put(nofar, nofar.getRoles().get(0));
        try {
            sb.SC.createShift(now, type, employees, 1, dayShiftStart, dayShiftEnd, false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(now, type, 1);

        Assertions.assertFalse(shift.getShiftEvents().contains("test cancel"), "the test is already logged");

        shift.addToShiftEvents("test cancel");

        Assertions.assertTrue(shift.getShiftEvents().contains("test cancel"), "the test is not logged");

    }

    @Test
    void testDeleteEmployee() {
        newEmp();
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));

        Employee newEmp = sb.EC.getEmployeeById(333333);

        Assertions.assertTrue(sb.EC.getEmployees().containsValue(newEmp),
                "the employee is not registered");

        try{
            sb.SC.canDeleteEmployee(newEmp);
            sb.EC.removeEmployee(newEmp.getId());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        Assertions.assertFalse(sb.EC.getEmployees().values().stream().anyMatch(e -> e.equals(newEmp)), "the employee is still registered");

        try {
            sb.EC.employeeSubmit(newEmp, now, type);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        try{
            sb.SC.canDeleteEmployee(newEmp);
            sb.EC.removeEmployee(newEmp.getId());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        Assertions.assertFalse(sb.EC.getEmployees().values().stream().anyMatch(e -> e.equals(newEmp)), "deleted when should not");

    }

    @Test
    void testCloseAllBranches(){
        boolean type = LocalTime.now().isAfter(LocalTime.of(15, 0));
        Map<Employee, String> employees = new LinkedHashMap<>();
        employees.put(gili, gili.getRoles().get(1));
        employees.put(nofar, nofar.getRoles().get(0));

        newEmp();
        Employee newEmp = sb.EC.getEmployeeById(333333);
        try {
            sb.SC.createShift(tomorrow.plusDays(1), type, employees, 1, dayShiftStart, dayShiftEnd, false);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }        Shift shift = sb.SC.getShiftByDateType(tomorrow.plusDays(1), type, 1);

        Assertions.assertTrue(sb.SC.isShiftExist(tomorrow.plusDays(1), type, 1),
                "the shift does not exist");

        sb.closeAllBranches(tomorrow.plusDays(1), type);

        Assertions.assertFalse(sb.SC.isShiftExist(tomorrow.plusDays(1), type, 1),
                "the shift still exist");



        try{
            sb.EC.employeeSubmit(newEmp, tomorrow.plusDays(1), type);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Assertions.assertFalse(sb.EC.whoIsAvailable(tomorrow.plusDays(1), type).keySet().stream().anyMatch(e -> e.equals(newEmp)),
                "the employee submitted shift when should not");
    }

    @Test
    void testAddNewDriver() {
        if(sb.EC.getEmployees().containsKey(222))
            sb.EC.removeEmployee(222);
        LinkedList<String> driverRole =new LinkedList<>();
        driverRole.add("Driver");
        sb.EC.newDriver("testDriver", 222, 0, 45, LocalDate.of(2022, 10, 2), terms1, driverRole, false, "no special info", "123456789", fictiveDriverLicenses);
        Employee testWorker = sb.EC.getEmployeeById(222);

        Assertions.assertTrue(sb.EC.getEmployees().containsValue(testWorker),
                "the added driver was not found in  the controller");

        Assertions.assertTrue(testWorker.isDriver(new HashMap<>()),
                "the added employee is not a driver");
    }

    @Test
    void testDriverSumbit() {
        if(sb.EC.getEmployees().containsKey(222))
            sb.EC.removeEmployee(222);
        LinkedList<String> driverRole =new LinkedList<>();
        driverRole.add("Driver");
        sb.EC.newDriver("testDriver", 222, 0, 45, LocalDate.of(2022, 10, 2), terms1, driverRole, false, "no special info", "123456789", fictiveDriverLicenses);
        Employee testWorker = sb.EC.getEmployeeById(222);

        Assertions.assertFalse(testWorker.getAvailability().contains(tomorrow+","+false),
                "the shift is submitted when should not");

        try {
            sb.EC.employeeSubmit(testWorker, tomorrow, false);
            Assertions.assertTrue(testWorker.getAvailability().contains(tomorrow+","+false),
                    "the shift not submitted when should");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    void testDriverCantSumbit() {
        if(sb.EC.getEmployees().containsKey(222))
            sb.EC.removeEmployee(222);
        LinkedList<String> driverRole =new LinkedList<>();
        driverRole.add("Driver");
        sb.EC.newDriver("testDriver", 222, 0, 45, LocalDate.of(2022, 10, 2), terms1, driverRole, false, "no special info", "123456789", fictiveDriverLicenses);
        Employee testWorker = sb.EC.getEmployeeById(222);

        Assertions.assertFalse(testWorker.getAvailability().contains(tomorrow+","+false),
                "the shift is submitted when should not");

        try {
            sb.EC.employeeSubmit(testWorker, tomorrow, false);
            Assertions.assertTrue(testWorker.getAvailability().contains(tomorrow+","+false),
                    "the shift not submitted when should");


        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(Exception.class, () -> sb.EC.employeeSubmit(testWorker, tomorrow, false));


    }

    @Test
    void testDriverRemoveSumbit() {
        if(sb.EC.getEmployees().containsKey(222))
            sb.EC.removeEmployee(222);
        LinkedList<String> driverRole =new LinkedList<>();
        driverRole.add("Driver");
        sb.EC.newDriver("testDriver", 222, 0, 45, LocalDate.of(2022, 10, 2), terms1, driverRole, false, "no special info", "123456789", fictiveDriverLicenses);
        Employee testWorker = sb.EC.getEmployeeById(222);

        try {
            sb.EC.employeeSubmit(testWorker, tomorrow, false);
            Assertions.assertTrue(testWorker.getAvailability().contains(tomorrow+","+false),
                    "the shift not submitted when should");

            sb.removeSubmit(testWorker, tomorrow, false);
            Assertions.assertFalse(testWorker.getAvailability().contains(tomorrow+","+false),
                    "the shift is submitted when should not");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void testDeleteDriver() {
        LinkedList<String> driverRole =new LinkedList<>();
        driverRole.add("Driver");
        if(!sb.EC.getEmployees().containsKey(222))
            sb.EC.newDriver("testDriver", 222, 0, 45, LocalDate.of(2022, 10, 2), terms1, driverRole, false, "no special info", "123456789", fictiveDriverLicenses);

        Employee testWorker = sb.EC.getEmployeeById(222);
        sb.EC.removeEmployee(testWorker.getId());
        Assertions.assertFalse(sb.EC.getEmployees().containsValue(testWorker),
                "the deleted driver was in  the controller");

    }


}
