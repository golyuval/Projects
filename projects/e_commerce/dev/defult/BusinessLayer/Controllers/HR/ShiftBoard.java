package defult.BusinessLayer.Controllers.HR;

import defult.BusinessLayer.Controllers.Delivery.Delivery_Controller;
import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.BusinessLayer.HRsystem.Driver;
import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Shift;
import defult.DataAccessLayer.HR.Dal.EmployeeHRDAO;
import defult.DataAccessLayer.HR.Dal.ShiftHRDAO;

import java.time.*;
import java.util.*;

import static defult.BusinessLayer.HRsystem.Role.*;

public class ShiftBoard {

    // static variable to hold single instance of ShiftBoard
    private static ShiftBoard shiftBoardInstance = null;
    public ShiftController SC;
    public EmployeeController EC;
    public Delivery_Controller DC;
    // public static method to get the single instance of ShiftBoard
    public static ShiftBoard getInstance() {
        if (shiftBoardInstance == null) {
            shiftBoardInstance = new ShiftBoard();
        }
        return shiftBoardInstance;
    }



    public List<String> getCloseBranch() {
        return closeBranch;
    }

    private List<String> closeBranch;


    private EmployeeHRDAO employeeDAO;
    private ShiftHRDAO shiftDAO;
    public EmployeeHRDAO getEmployeeDAO() {
        return employeeDAO;
    }

    public ShiftBoard(){

        this.closeBranch = new ArrayList<>();
        this.employeeDAO = new EmployeeHRDAO();

        this.shiftDAO = new ShiftHRDAO(employeeDAO);

        this.SC = new ShiftController(shiftDAO);

        this.EC = new EmployeeController(employeeDAO);
        this.DC = Delivery_Controller.getInstance();



    }
    public void init_Data()
    {
        SC.init_Data();
        EC.init_Data();
    }
    public void removeSubmit(Employee employee, LocalDate date, boolean type){
        if(employee.isShiftAvailable(date, type)!=-1) {
            employee.setNotAvailable(date, type);
            employee.setNotBlocked(date, type);
            EC.employeeDAO.setBlockedShifts(employee.getId(), employee.getAvailabilityString());
            EC.employeeDAO.setAvailability(employee.getId(), employee.getAvailabilityString());
        }
    }


    public List<DeliveryTuple> isDeliveryOnShift (LocalDate date, LocalTime start, LocalTime end, int branchId, boolean arrival){
        List<DeliveryTuple> licensesForShift = new ArrayList<>();
        LocalDateTime dateStart = LocalDateTime.of(date, start);
        LocalDateTime dateEnd = LocalDateTime.of(date, end);
//        System.out.println("start date: "+dateStart.toString());
//        System.out.println("end date: "+dateEnd.toString());
        if (arrival) {//storage
            for (DeliveryTuple deliveryTuple :
                    DC.getDeliveryTupleMap().values()) {
                if ((deliveryTuple.getArrivalTime().isBefore(dateEnd)|| deliveryTuple.getArrivalTime().isEqual(dateEnd))
                        && (deliveryTuple.getArrivalTime().isAfter(dateStart) || deliveryTuple.getArrivalTime().isEqual(dateStart)) && deliveryTuple.getBranchId() == branchId ) {
                    licensesForShift.add(deliveryTuple);
                }
            }
        }else{
            for (DeliveryTuple deliveryTuple :
                    DC.getDeliveryTupleMap().values()) {

                if (deliveryTuple.getArrivalTime().isBefore(dateEnd) && deliveryTuple.getArrivalTime().isAfter(dateStart)) {
                    if(DC.getDeliveryByID(deliveryTuple.getDeliveryId()).get_driverId() == -1)
                        licensesForShift.add(deliveryTuple);
                }
            }
        }
        return licensesForShift;

    }


    public void closeAllBranches(LocalDate date, boolean type) {
        for(int i=1; i<10; i++){
            this.addToCloseShift(date,type, i);

        }
        for (Employee employee:
                EC.getEmployees().values()) {
            employee.setBlockedShifts(date.toString()+","+type);
        }
    }
    public void addToCloseShift (LocalDate date, boolean type, int branchID){
        closeBranch.add(date +","+ type+ ","+ branchID);
        if(SC.getShiftByDateType(date, type, branchID)!=null)
            SC.cancelShift(SC.getShiftByDateType(date, type, branchID));
    }


/*
    public void alertNewDelivery(LocalDateTime departureTime, LocalDateTime arrivalTime, int branchId) throws Exception {
        throw new Exception("\nalert: there is a new delivery departing at "+departureTime.toString()+ ", arriving at: "+ arrivalTime.toString()+", branch: "+branchId+", please check stuff is complete.\n");

    }*/

    //-----------------

    public Map<Employee, List<String>> AvailableManagers
            (Map<Employee, List<String>> options) throws Exception {
        String role = SHIFT_MANAGER.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }
    public Map<Employee, List<String>> AvailableCashier
            (Map<Employee, List<String>> options) throws Exception {
        String role = CASHIER.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }

    public Map<Employee, List<String>> AvailableStorage
            (Map<Employee, List<String>> options) throws Exception {
        String role = STORAGE.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }

    public List<String> reportFullShiftHistory(int branchId, LocalDate startDate, LocalDate endDate) {
        List<String> shifts = SC.reportShiftHistory(branchId, startDate, endDate);
        List<String> driversInDates = DC.getAllDriversInRange(startDate, endDate);
        List<String> combined = new LinkedList<>(shifts);
        combined.addAll(driversInDates);

        return combined;
    }

    public List<Shift> reportShifts(int branchId, LocalDate startDate, LocalDate endDate) {
        List<Shift> shifts = SC.reportShifts(branchId, startDate, endDate);

        return shifts;
    }

    public Map<Driver, List<String>> AvailableDrivers
            (Map<Employee, List<String>> options, String license) throws Exception {
        String role = DRIVER.getRoleName();
        LinkedHashMap<Driver, List<String>> available = new LinkedHashMap<>();

        for (Employee employee:
                 options.keySet()) {

            employee.isQualifiedDriver(available, license);
        }

        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }

    public Map<Employee, List<String>> AvailableGeneral
            (Map<Employee, List<String>> options) throws Exception {
        String role = GENERAL.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }

    public Map<Employee, List<String>> AvailableOrganizer
            (Map<Employee, List<String>> options) throws Exception {
        String role = ORGANIZER.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }

    public Map<Employee, List<String>> AvailableCleaner
            (Map<Employee, List<String>> options) throws Exception {
        String role = CLEANER.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }

    public Map<Employee, List<String>> AvailableSecurity
            (Map<Employee, List<String>> options) throws Exception {
        String role = SECURITY.getRoleName();
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                options.keySet()) {
            if(employee.getRoles().contains(role)){
                available.put(employee, employee.getRoles());
            }
        }
        if(available.size() == 0)
            throw new Exception("No "+role+" employees are available for this shift");
        return available;
    }


    //-----------------




    //system - employee




    //-----------------



    //system - shift

    public void removeEmployeeFromShift(Employee employee, Shift shift){
        shift.removeFromFinalEmployees(employee);
        EC.availableEmp(employee, shift);
//
        SC.shiftDAO.DeleteEmployeeFromShift(employee.getId(), shift.getDate(), shift.isShiftType());
    }


    public void validateSchedulePossible(LocalDate date, boolean type, int branchId) throws Exception {
        try{
            this.isShiftIsClose(date,type, branchId);
        }catch (Exception e) {
            throw new Exception("this branch is closed in this date due to occasion");
        }
        if(SC.isShiftExist(date,type, branchId))
            throw new Exception("this shift is already Scheduled");

    }

    public void isShiftIsClose(LocalDate date, boolean type, int branchId) throws Exception {
        String typeS = type? "night":"morning";
        if(closeBranch.contains(date+","+type+","+branchId))
            throw new Exception("branch is closed on "+date.toString() +", "+typeS);
    }



    public void addEmployeesToShift(Map<Employee, String> updatedStuff, Shift shift) {
        for (Employee employee:
                updatedStuff.keySet()) {
            shift.addEmployeeToShift(employee, updatedStuff.get(employee));
        }
    }



    //-----------------




    //hours and salary

    public int calcSalary(Employee employee, YearMonth yearMonth){
        int bonus =0;
        if(isBonus(employee, yearMonth)){
            bonus = employee.getBonuses().get(yearMonth);
        }
        return bonus + (employee.getSalary()*SC.hoursReport(employee, yearMonth));
    }

    public void bonusEmployee(Employee employee, int bonus ,YearMonth yearMonth) throws Exception {
        if(bonus<0)
            throw new Exception("no negative bonuses!!!");
        employee.addBonus(bonus, yearMonth);

    }

    public boolean isBonus(Employee employee, YearMonth yearMonth) {
        return employee.getBonuses().containsKey(yearMonth);
    }

    public void connectDriverToDelivery(Driver driverSend, int deliveryId) {
//        DC.getDeliveryByID(deliveryId).setDriver(driverSend.getId(), driverSend.getName());
        DC.setDriver(deliveryId, driverSend);
    }

    public void endShiftPolicy(Shift shift) {
        boolean notifyOnCall = true;
        if(!shift.isShiftType()){
            for (DeliveryTuple delivery:
                    DC.getDeliveryTupleMap().values()) {
                if(delivery.getArrivalTime().toLocalDate().isEqual(LocalDate.now()) && !delivery.isArrived()){
                    notifyOnCall =false;
                    break;
                }
            }
            if(notifyOnCall)
                SC.notifyStuffOnCall(shift);
        }
    }

    //-----------------

    //shift events - shift manager






    //----------------
}
