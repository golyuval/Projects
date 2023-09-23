package defult.BusinessLayer.HRsystem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Shift {
    private LocalDate date;
    /**>shiftType: false for morning shift - true for night shift*/
    private boolean shiftType;

    private Employee manager;
    private Map<Employee, String> finalEmployees;
    private int branch;
    private List<String> shiftEvents;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private boolean isScheduled;
    private boolean inShift;
    private boolean endShift;
    private boolean isNewDelivery;


    /**

     The Shift class represents a shift for a branch of a company.
     A shift can be scheduled, managed by an employee, and have a start and end time.
     It also keeps track of the final list of employees assigned to the shift, the branch it belongs to,
     and any shift events that occur during the shift.

     */

    public Shift(LocalDate date, boolean shiftType, Employee manager,
                 Map<Employee, String> finalEmployees,
                 int branch, LocalTime shiftStart, LocalTime shiftEnd) {
        this.date = date;
        this.shiftType = shiftType;
        this.manager = manager;
        this.finalEmployees = finalEmployees;
        this.branch = branch;
        this.shiftEvents = new ArrayList<>();
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.isScheduled =false;
        this.endShift =false;
        this.inShift = false;
        this.isNewDelivery =false;
    }

    public Shift(boolean shiftType, LocalDate date, Employee manager, int branchId, String shiftEvents, LocalTime startShift,
                 LocalTime endShift, boolean InShift, boolean EndShift, boolean IsNewDelivery, Map<Employee, String> employees) {
        this.shiftType = shiftType;
        this.date = date;
        this.manager = manager;
        this.finalEmployees = employees;
        this.branch = branchId;
        this.shiftStart = startShift;
        this.shiftEnd = endShift;
        this.endShift = EndShift;
        this.isNewDelivery = IsNewDelivery;
        List<String> shiftEventsList = new LinkedList<>(Arrays.asList(shiftEvents.split("//")));
        this.shiftEvents = shiftEventsList;
        this.inShift = InShift;


    }

    public String shiftEventString(){
        String joinedString;
        return joinedString = String.join("//", shiftEvents);
    }

    public boolean isEndShift() {
        return endShift;
    }

    public void setEndShift(boolean endShift) {
        this.endShift = endShift;
    }

    public boolean isInShift() {
        return inShift;
    }

    public void setInShift(boolean inShift) {
        this.inShift = inShift;
    }

    public void scheduleShift(){
        this.isScheduled =true;
    }

    public boolean isValidShift(){
        return (manager!=null && finalEmployees.size() > 1);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isShiftType() {
        return shiftType;
    }

    public void setShiftType(boolean shiftType) {
        this.shiftType = shiftType;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Map<Employee, String> getFinalEmployees() {
        return finalEmployees;
    }

    public void setFinalEmployees(Map<Employee, String> finalEmployees) {
        this.finalEmployees = finalEmployees;
    }

    public void removeFromFinalEmployees(Employee employee){
        System.out.println("removing: "+finalEmployees.size());
        finalEmployees.keySet().removeIf(e -> e.equals(employee));
//        this.finalEmployees.remove(employee);
        System.out.println("removed? "+finalEmployees.size());

    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public List<String> getShiftEvents() {
        return shiftEvents;
    }

    public void addToShiftEvents(String event){
        shiftEvents.add(event);
    }

    public void setShiftEvents(String event) {
        this.shiftEvents.add(event);
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(LocalTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public LocalTime getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(LocalTime shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public void schedule(){
        isScheduled = true;
    }
    public boolean isDone(){ //notDone
        return this.date.isAfter(LocalDate.now());
    }

    public void addEmployeeToShift(Employee employee, String role) {
        this.finalEmployees.put(employee, role);
    }

    public String employeesToString() {
        StringBuilder employees = new StringBuilder();
        for (Map.Entry<Employee, String> emp:
             finalEmployees.entrySet()) {
            employees.append("Role: ").append(emp.getValue()).append(", ").append(emp.getKey().toString()).append("\n");
        }
        return employees.toString();
    }

    public boolean isNewDelivery() {
        return isNewDelivery;
    }
}
