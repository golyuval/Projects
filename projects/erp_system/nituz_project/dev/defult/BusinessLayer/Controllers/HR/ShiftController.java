package defult.BusinessLayer.Controllers.HR;

import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Shift;
import defult.DataAccessLayer.HR.Dal.ShiftHRDAO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static defult.BusinessLayer.HRsystem.Role.*;

public class ShiftController {
    private List<Shift> shifts;
    ShiftHRDAO shiftDAO;

    public ShiftController(ShiftHRDAO shiftDAO){
        this.shiftDAO =shiftDAO;

        this.shifts = new ArrayList<>();
    }

    public void alert() throws Exception {
        int tomorrow = 0;
        for (Shift shift:
                shifts) {
            if(shift.getDate().isEqual(LocalDate.now().plusDays(1)))
                tomorrow++;
        }
        //print to the screen message
        if(tomorrow<2)
            throw new Exception("\nalert: tomorrow's shift is not scheduled\n");

    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public Shift getShiftByDateType(LocalDate date, boolean type, int branchId){
        for (Shift s:
                shifts) {
            if(s.getDate().equals(date) && type==s.isShiftType()&& s.getBranch() == branchId)
                return s;
        }
        return null;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public void createShift(LocalDate date, boolean type,
                            Map<Employee, String> finalEmployees,
                            int branch, LocalTime shiftStart, LocalTime shiftEnd, boolean deliveryArrival) throws Exception {
        Employee manager = null;
        validateStuffManager(finalEmployees);
        if(deliveryArrival)
            validateDeliveryStuff(finalEmployees);
        for (Employee employee:
                finalEmployees.keySet()) {

            if(finalEmployees.get(employee).equals(SHIFT_MANAGER.getRoleName())) manager= employee;

        }
        Shift shift = new Shift(date, type, manager, finalEmployees, branch, shiftStart, shiftEnd);
        shifts.add(shift);
        shiftDAO.Insert(shift);
    }


    private String shiftToString(Shift shift){
        StringBuilder shiftOut = new StringBuilder(" Date: " + shift.getDate() + " Type: " + shiftType(shift.isShiftType()) + " -\n" +
                "Employees:\n");
        for (Employee e:
                shift.getFinalEmployees().keySet()) {
            shiftOut.append("Role: ").append(shift.getFinalEmployees().get(e)).append(" - Name: ").append(e.getName()).append(", ID: ").append(e.getId()).append(".\n");
        }
        return shiftOut.toString();
    }

    public void validateStuffManager(Map<Employee, String> stuff) throws Exception {
        if(!stuff.containsValue(SHIFT_MANAGER.getRoleName()))
            throw new Exception("no manager' this stuff is unacceptable");
    }

    private void validateDeliveryStuff(Map<Employee, String> stuff) throws Exception {
        if(!stuff.containsValue(STORAGE.getRoleName()))
            throw new Exception("no storage employee, this stuff is unacceptable");
    }
    public boolean isShiftExist(LocalDate date, boolean type, int branchId) {
        boolean toReturn = false;
        for (Shift shift:
                shifts) {
            if(shift.getDate().isEqual(date)&& shift.isShiftType() == type && shift.getBranch() == branchId){
                toReturn = true;
            }
        }
        return toReturn;
    }
    public void cancelShift(Shift shiftToDelete) {
        for (Employee employee : shiftToDelete.getFinalEmployees().keySet()) {
            FreeEmployeeClosedShift(employee, shiftToDelete);
            employee.alert(shiftToDelete);
        }
        shiftDAO.Delete(shiftToDelete);
        shifts.remove(shiftToDelete);
    }
    public void FreeEmployeeClosedShift(Employee employee, Shift shift){
        employee.addToAvailability(shift.getDate(), shift.isShiftType());
        employee.removeFromWeekShift(shift);
    }
    public int hoursReport(Employee employee, YearMonth yearMonth){
        int sum =0;
        for (Shift shift:
                shifts) {
            if (shift.getDate().getMonth().equals(yearMonth.getMonth()) && shift.getDate().getYear()==(yearMonth.getYear())) {
                if (shift.getFinalEmployees().keySet().stream().anyMatch((x) -> x.equals(employee)))
                    sum += Duration.between(shift.getShiftStart(), shift.getShiftEnd()).toHours();
            }
        }
        return sum;
    }



    public void validateShiftStart(Shift shift) throws Exception {
        if(LocalTime.now().isBefore(shift.getShiftStart().minusHours(1))) {
            throw new Exception ("shift is yet to start " + shift.getShiftStart().toString());
        }
        if(shift.getShiftEnd().isBefore(LocalTime.now())){
            throw new Exception ("shift was already over at- "+shift.getShiftEnd().toString());

        }

    }
    public void canDeleteEmployee(Employee employeeById) throws Exception{
        for ( Shift shift: shifts
        ) {
            if (shift.getDate().isAfter(LocalDate.now())) {
                if (shift.getFinalEmployees().containsKey(employeeById)) {
                    throw new Exception("this employee has shift this week, you can't delete him");
                }
            }
        }
    }
    private void shiftEventLog(String event, Shift shift) {
        shift.addToShiftEvents(event);
        shiftDAO.setShiftEvents(shift.getDate(), shift.isShiftType(), shift.getBranch(), shift.shiftEventString());
    }


    public void validateShiftManager(Shift shift, String password) throws Exception {
        if(shift.getManager() == null) {
            System.out.println(shift.getFinalEmployees().values().stream());
            throw new Exception("no shift is scheduled right now");
        }
        if(!password.equals(shift.getManager().getPassword()))
            throw new Exception("only this shift's manager can log into shift");
    }

    public void logStartShift(Shift shift) {
        if(LocalTime.now().isAfter(shift.getShiftStart().plusHours(1))) {
            shiftEventLog("shift started late- "+ LocalTime.now(), shift);
        }else{
            shiftEventLog("start shift "+ LocalTime.now() +" "+ shift.getManager().getName(), shift);
        }
        shift.setInShift(true);
        shiftDAO.setInShift(shift.getDate(), shift.isShiftType(), shift.getBranch(), true);

    }

    public void validateCashierInShift(String cashier, Shift shift) throws Exception {
        if(!isCashierInShift(shift, cashier)){
            throw new Exception("that is not a cashier in this shift");
        }
    }
    public boolean isCashierInShift(Shift shift, String cashier) {
        AtomicBoolean out = new AtomicBoolean(false);
        shift.getFinalEmployees().forEach((Employee employee, String role) ->
        {
            if (employee.getName().equals(cashier) && role.equals(CASHIER.getRoleName())){
                out.set(true);
            }
        });
        return out.get();
    }

    public String logCancelTransaction(Shift shift, String cashier) {
        String event = "Canceling "+ LocalTime.now() +" manager: "+ shift.getManager().getName()+", cashier: "+cashier;
        shiftEventLog(event, shift);
        return event;
    }

    public void validateShiftEnd(Shift shift) throws Exception {
        if(LocalTime.now().isBefore(shift.getShiftEnd().minusHours(1))) {
            throw new Exception ("it is too early to close, " + shift.getShiftEnd().toString());
        }
        if(shift.isEndShift()){
            throw new Exception ("shift is already over");

        }
        shift.setEndShift(true);
        shiftDAO.setEndShift(shift.getDate(),shift.isShiftType(), shift.getBranch(), shift.isEndShift() );
    }

    public String logEndShift(Shift shift) {
        String event = "end shift "+ LocalTime.now() +" "+ shift.getManager().getName();
        shiftEventLog(event, shift);
        return event;
    }

    public void validateIsInShift(ShiftBoard sb, Scanner s, Shift shift) throws Exception {
        if(!shift.isInShift())
            throw new Exception("shift is not logged yet");
        if(shift.isEndShift())
            throw new Exception("shift is over");
    }

    private String shiftType(boolean t){
        return (t)? "Night":"Morning";
    }


    public List<String> reportShiftHistory ( int branchId, LocalDate startDate, LocalDate endDate ) {
        List<String> shiftHistory = new LinkedList<>();
        for (Shift shift:
             shifts) {
            if((shift.getDate().isAfter(startDate)||(shift.getDate().isEqual(startDate)) && (shift.getDate().isBefore(endDate) ||(shift.getDate().isEqual(endDate))) && shift.getBranch() == branchId)){
                shiftHistory.add("Date: "+shift.getDate()+ " Type: "+ shiftType(shift.isShiftType())+"\n"+shift.employeesToString());
            }
        }
        return shiftHistory;
    }

    public void notifyStuffOnCall(Shift shift) {
        for (Employee employee:
                shift.getFinalEmployees().keySet()) {
            if(shift.getFinalEmployees().get(employee).equals(STORAGE.getRoleName()))
                employee.notifyOnCall();
        }
    }

    public void init_Data() {
        this.shifts = shiftDAO.SelectAllShifts();

    }

    public void updateStuffInDB(LocalDate date, boolean type, int branchId, int employeeId, String role) {
        shiftDAO.InsertEmployeeToShift(date, type, employeeId, branchId, role);
    }

    public List<Shift> reportShifts(int branchId, LocalDate startDate, LocalDate endDate) {
        List<Shift> shiftHistory = new LinkedList<>();
        for (Shift shift:
                shifts) {
            if((shift.getDate().isAfter(startDate)||(shift.getDate().isEqual(startDate)) && (shift.getDate().isBefore(endDate) ||(shift.getDate().isEqual(endDate))) && shift.getBranch() == branchId)){
                shiftHistory.add(shift);
            }
        }
        return shiftHistory;
    }
}
