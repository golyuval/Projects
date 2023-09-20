package defult.BusinessLayer.HRsystem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class Employee {
    private String name;
    private int id;
    private int bankInfo;
    private int salary;
    private List<String> terms;
    private LocalDate startDate;
    private List<String> roles;
    private boolean shiftManager;
    private List<String> blockedShifts;
    private String specialInfo;
    private List<String> availability;
    private String password;
    private Map<LocalDate, Boolean> weekShifts;
    private Map<YearMonth, Integer> bonuses;
    private String alert;

//    private
public Employee(String name, int id, int bankInfo, int salary, String terms, LocalDate date, String roles,
                boolean shiftManager, String blockedShifts, String specialInfo, String availability, String password,
                String weekShifts, String bonuses, String alert) {
    this.id = id;
    this.name = name;
    this.bankInfo = bankInfo;
    this.salary = salary;

    if (blockedShifts == null || blockedShifts.equals("")) {
        this.blockedShifts = new LinkedList<>();
    } else {
        this.blockedShifts = new LinkedList<>(Arrays.asList(blockedShifts.split("//")));
    }

    if (roles == null || roles.equals("")) {
        this.roles = new LinkedList<>();
    } else {
        this.roles = new LinkedList<>(Arrays.asList(roles.split("//")));
    }

    this.shiftManager = shiftManager;
    this.specialInfo = specialInfo;
    this.startDate = date;
    if (terms == null || terms.equals("")) {
        this.terms = new LinkedList<>();
    } else {
        this.terms = new LinkedList<>(Arrays.asList(terms.split("//")));;
    }
    this.password = password;
    if (availability == null || availability.equals("")) {
        this.availability = new LinkedList<>();
    } else {
        this.availability = new LinkedList<>(Arrays.asList(availability.split("//")));
    }
    //this.availability = Arrays.asList(availability.split("//"));
    this.weekShifts = stringToMap2(weekShifts);
    this.bonuses =stringToMap(bonuses);
    this.alert = alert;

}

    public Employee(String name, int id, int bankInfo, int salary,
                    LocalDate startDate, List<String> terms, List<String> roles,
                    boolean isManager, String specialInfo, String password){
        this.id = id;
        this.name = name;
        this.bankInfo = bankInfo;
        this.salary = salary;
        this.blockedShifts = new ArrayList<>();
        this.roles = roles;
        this.shiftManager = isManager;
        this.specialInfo = specialInfo;
        this.startDate = startDate;
        this.terms = terms;
        this.password = password;
        this.availability = new ArrayList<>();
        this.weekShifts = new LinkedHashMap<>();
        this.bonuses = new LinkedHashMap<>();
        alert = "";

    }

    public Employee() {

    }

    public Map<LocalDate, Boolean> stringToMap2(String str) {
        Map<LocalDate, Boolean> map = new HashMap<>();
        String[] tuples = str.split("//");
        for (String tuple : tuples) {
            String[] parts = tuple.replace("(", "").replace(")", "").split(",");
            if (parts.length == 2) {
                LocalDate key = LocalDate.parse(parts[0]);
                Boolean value = Boolean.parseBoolean(parts[1]);
                map.put(key, value);
            }
        }
        return map;
    }
    public Map<YearMonth, Integer> stringToMap(String str) {
        Map<YearMonth, Integer> map = new HashMap<>();
        String[] tuples = str.split("//");
        for (String tuple : tuples) {
            String[] parts = tuple.replace("(", "").replace(")", "").split(",");
            if (parts.length == 2) {
                YearMonth key = YearMonth.parse(parts[0]);
                Integer value = Integer.parseInt(parts[1]);
                map.put(key, value);
            }
        }
        return map;
    }

    public Map<YearMonth, Integer> getBonuses() {
        return bonuses;
    }

    public void addBonus(int bonus, YearMonth yearMonth){
        bonuses.put(yearMonth, bonus);
    }

    public Map<LocalDate, Boolean> getWeekShifts() {
        return weekShifts;
    }

    public boolean canAddToWeekShift(LocalDate date, boolean type) {
        int count =0;

        for (LocalDate localDate:
             weekShifts.keySet()) {
            // Get the week numbers for each date
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int weekNumber1 = localDate.get(weekFields.weekOfWeekBasedYear());
            int weekNumber2 = LocalDate.now().get(weekFields.weekOfWeekBasedYear());

            if (weekNumber1 == weekNumber2) {
                count++;
            }
        }

        if(count>=6)
            return false;
        else return !weekShifts.containsKey(date) || type == weekShifts.get(date);
    }

    public void setWeekShifts(Map<LocalDate, Boolean> weekShifts) {
        this.weekShifts = weekShifts;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getAvailability() {
        return availability;
    }


    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public void addToAvailability(LocalDate date, boolean type){
//        System.out.println(availability.stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", ")));

        availability.add(date+","+type);

    }

    public int getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(int bankInfo) {
        this.bankInfo = bankInfo;
    }

    public int getSalary() {
        return salary;
    }


    public void setSalary(int salary) {
        if (salary>0)
            this.salary = salary;
        else System.out.println("salary can not be negative");
    }

    public List<String> getTerms() {
        return terms;
    }

    public void addTerms(String terms) {
        this.terms.add(terms);
    }

    public void removeTerm(int term) {
        this.terms.remove(term);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String roles) {
        this.roles.add(roles);
    }

    public boolean isShiftManager() {
        return shiftManager;
    }

    public void setShiftManager(boolean shiftManager) {
        this.shiftManager = shiftManager;
    }

    public List<String> getBlockedShifts() {
        return blockedShifts;
    }

    public void setBlockedShifts(String blockedShifts) {
        this.blockedShifts.add(blockedShifts);//shift is added as a string' separate type date by *

    }

    public String getSpecialInfo() {
        return specialInfo;
    }

    public void setSpecialInfo(String specialInfo) {
        this.specialInfo = specialInfo;
    }

    public void deleteRole(int role) {
        roles.remove(role);
    }

    public boolean deleteShift(String shift) {
        LocalDate date =null;
        try {
            date = LocalDate.parse(shift.split(",")[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }catch (Exception e){
            if(e.getMessage().contains("could not be parsed at index"))
                try {
                    date = LocalDate.parse(shift.split(",")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                }catch (Exception e2){
                    System.out.println(e2.getMessage());
                }
        }

        boolean type = Boolean.parseBoolean(shift.split(",")[1]);
        if(date==null) return false;

        if(availability.contains(date.toString()+","+type)){
            setNotAvailable(date, type);
            for (int i=0; i<blockedShifts.size(); i++){
                if(blockedShifts.get(i).equals(date.toString()+","+type)) {
                    blockedShifts.remove(i);
                    break;
                }
            }
        }

        setBlockedShifts(shift);
        return true;
    }

    public int isShiftAvailable(LocalDate date, boolean type){
        for (int i=0; i<availability.size(); i++) {
            if (availability.get(i).equals(date.toString() + "," + type)) {
                return i;
            }
        }
        return -1;
    }

    public void setNotAvailable(LocalDate date, boolean type) {
        for (int i=0; i<availability.size(); i++) {
            if (availability.get(i).equals(date.toString() + "," + type)) {
                availability.remove(i);
                break;
            }
        }
        this.weekShifts.put(date, type);

    }

    public void setNotBlocked(LocalDate date, boolean type) {
        for (int i=0; i<blockedShifts.size(); i++){
            if(blockedShifts.get(i).equals(date.toString()+","+type)) {
                blockedShifts.remove(i);
                break;
            }
        }
    }

    public void removeFromWeekShift(Shift shift) {
       //if(weekShifts.containsKey(shift.getDate()))
           weekShifts.remove(shift.getDate(),shift.isShiftType());
    }


    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Employee other)) {
            return false;
        }
        return (
                id == other.getId() &&
                name.equals(other.name) &&
                bankInfo == other.bankInfo &&
                startDate.isEqual(other.startDate) &&
                salary == other.salary
                );

    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public boolean isDriver(Map<Driver, List<String>> driverLicense){
        return false;
    }

    public boolean isQualifiedDriver(Map<Driver, List<String>> driverLicense, String license){
        return false;
    }

    public void alert(Shift shiftToDelete){
        alert =  ("your shift on "+ shiftToDelete.getDate() +" was canceled!");
    }

    public void notifyOnCall() {
        alert += "\nAll of today's deliveries arrived, you may not come today.";
    }

    public String getTermsString() {
        String joinedString;
        return joinedString = String.join("//", terms);
    }

    public String getRolesString() {
        String joinedString;
        return joinedString = String.join("//", roles);
    }

    public String getBlockedShiftsString() {
        String joinedString;
        return joinedString = String.join("//", blockedShifts);
    }

    public String getAvailabilityString() {
        String joinedString;
        return joinedString = String.join("//", availability);
    }

    public String getWeekShiftsString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, Boolean> entry : weekShifts.entrySet()) {
            sb.append("(").append(entry.getKey()).append(",").append(entry.getValue()).append(")").append("//");
        }
        String result = sb.toString();
        if (result.endsWith("//")) {
            result = result.substring(0, result.length() - 2); // remove the last "//"
        }
        return result;
    }

    public String getBonusesString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<YearMonth, Integer> entry : bonuses.entrySet()) {
            sb.append("(").append(entry.getKey().toString()).append(",").append(entry.getValue()).append(")").append("//");
        }
        String result = sb.toString();
        if (result.endsWith("//")) {
            result = result.substring(0, result.length() - 2); // remove the last "//"
        }
        return result;
    }

    @Override
    public String toString(){
        return "Name: " + name + ", ID: " + id + ".";
    }


    public void deleteRoleS(String role) {
        roles.remove(role);
    }
}
