package defult.BusinessLayer.HRsystem;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Driver extends Employee{

    List<String> license;

    public Driver(String name, int id, int bankInfo, int salary, LocalDate startDate, List<String> terms, List<String> roles, boolean isManager, String specialInfo, String password, List<String> license) {
        super(name, id, bankInfo, salary, startDate, terms, roles, isManager, specialInfo, password);
        this.license =license;
    }
    public Driver(String name, int id, int bankInfo, int salary, String terms, LocalDate date, String roles,
                  boolean shiftManager, String blockedShifts, String specialInfo, String availability, String password,
                  String weekShifts, String bonuses, String alert, String license) {
        super(name,id,bankInfo,salary,terms,date,roles,shiftManager, blockedShifts, specialInfo, availability, password,
                weekShifts, bonuses, alert);
        this.license = new LinkedList<>(Arrays.asList(license.split("//")));

    }

    public boolean isDriver(Map<Driver, List<String>> driverLicense){
        driverLicense.put(this, license);
        return true;
    }

    public String getLicenseString() {
        String joinedString;
        return joinedString = String.join("//", license);
    }

    public boolean isQualifiedDriver(Map<Driver, List<String>> driverLicense, String licenseQ){
        if(this.license.contains(licenseQ))  driverLicense.put(this, license);
        return true;
    }

    public List<String> getLicenses() {
        return license;
    }
}
