package defult.BusinessLayer.Controllers.HR;

import defult.BusinessLayer.HRsystem.Driver;
import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Shift;
import defult.DataAccessLayer.HR.Dal.EmployeeHRDAO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmployeeController {
    private Map<Integer, Employee> employees;

    EmployeeHRDAO employeeDAO;
    public EmployeeController(EmployeeHRDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
        this.employees = new LinkedHashMap<>();
    }
    public boolean validateId(int id) {// if id exist return false
//        System.out.println(id+" :"+employees.containsKey(id)+" :"+employees.keySet().stream().anyMatch((x) -> x==id)+" : "+ Arrays.toString(employees.keySet().toArray()));
        return !(employees.containsKey(id));
    }
    public void employeeSubmit(Employee employee, LocalDate date, boolean type) throws Exception {
//        System.out.println(employee.getAvailability().stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", ")));
//
//        System.out.println(employee.getBlockedShifts().stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", ")));
        validateSubmitPossible(employee, date, type);
        employee.addToAvailability(date, type);
        employee.setBlockedShifts(date.toString() + "," + type);
        employeeDAO.setBlockedShifts(employee.getId(), employee.getBlockedShiftsString());
        employeeDAO.setAvailability(employee.getId(), employee.getAvailabilityString());

    }

    private void validateSubmitPossible(Employee employee, LocalDate date, boolean type) throws Exception {
        if (employee.getBlockedShifts().contains(date + "," + type)) {
            throw new Exception("this shift cant be submitted");
        }
    }



    public Map<Integer, Employee> getEmployees() {
        return employees;
    }

    //shift scheduling
    public Map<Employee, List<String>> whoIsAvailable(LocalDate date, boolean type){
        LinkedHashMap<Employee, List<String>> available = new LinkedHashMap<>();
        for (Employee employee :
                employees.values()) {
            if(employee.isShiftAvailable(date, type)!=-1){
                if(employee.canAddToWeekShift(date, type))
                    available.put(employee, employee.getRoles());
            }
        }

        return available;
    }
    public void newEmployee(String name, int id, int bankInfo, int salary,
                            LocalDate startDate, List<String> terms, List<String> roles,
                            boolean isManager, String specialInfo, String password){
        Employee employee = new Employee(name, id, bankInfo, salary, startDate, terms, roles, isManager, specialInfo, password);
        employees.put(id, employee);
        employeeDAO.Insert(employee);

    }


    public void editEmployeeSalary(int id, int salary) throws Exception {
        if(!validateSalaryEdit(id, salary))
            throw new Exception("Worsening conditions is unacceptable!");
        Employee employee = employees.get(id);
        employee.setSalary(salary);
        employeeDAO.setSalary(id, salary);

    }
    public void addEmployeeTerms(int id, String terms){
        Employee employee = employees.get(id);
        employee.addTerms(terms);
        employeeDAO.setHiringConditions(id, employee.getTermsString());
    }

    public void deleteEmployeeTerms(int id, int term){
        Employee employee = employees.get(id);
        employee.removeTerm(term);
        employeeDAO.setHiringConditions(id, employee.getTermsString());
    }


    public void editEmployeeBankInfo(int id, int bankInfo){
        Employee employee = employees.get(id);
        employee.setBankInfo(bankInfo);
        employeeDAO.setBankAccount(id, employee.getBankInfo());
    }

    public void addEmployeeRole(int id, String role){
        Employee employee = employees.get(id);
        employee.addRole(role);
        employeeDAO.setJobType(id, employee.getRolesString());
    }

    public void deleteEmployeeRoleS(int id, String role){
        Employee employee = employees.get(id);
        employee.deleteRoleS(role);
        employeeDAO.setJobType(id, employee.getRolesString());
    }

    public void deleteEmployeeRole(int id, int role){
        Employee employee = employees.get(id);
        employee.deleteRole(role);
        employeeDAO.setJobType(id, employee.getRolesString());
    }

    public void editEmployeeSpecialInfo(int id, String info){
        Employee employee = employees.get(id);
        employee.setSpecialInfo(info);
        employeeDAO.setSpecialInfo(id, employee.getSpecialInfo());

    }

    public void blockEmployee(int id, String shift){
        Employee employee = employees.get(id);
        //employee.setBlockedShifts(shift);
        if(employee.deleteShift(shift)) employeeDAO.setBlockedShifts(id, employee.getBlockedShiftsString());
    }

    public void removeEmployee(int id) {
        employeeDAO.Delete(getEmployeeById(id));
        employees.remove(id);
    }

    public Employee getEmployeeById(int id) {
        return employees.get(id);
    }
    public boolean validateSalaryEdit(int id, int newSalary) {
        return getEmployeeById(id).getSalary()<=newSalary;
    }

    public boolean validateStartDate(int id) {// if id exist return false
        return !getEmployeeById(id).getStartDate().isBefore(LocalDate.now());
    }

    public void validateCanBlock(int id, LocalDate date, boolean type) throws Exception {
        if(getEmployeeById(id).getWeekShifts().containsKey(date)) {
            if (getEmployeeById(id).getWeekShifts().get(date) == type) {
                throw new Exception("this shift is already scheduled for that employee, it cant be blocked");
            }
        }
    }
    public void newDriver(String name, int id, int bankInfo, int salary, LocalDate startDate, List<String> terms, List<String> roles, boolean b, String specialInfo, String password, List<String> driverLicenses) {
        Driver driver = new Driver(name, id, bankInfo, salary, startDate, terms, roles, false, specialInfo, password,driverLicenses );
        employees.put(id, driver);
        employeeDAO.Insert(driver);
    }

    public Map<Driver, List<String>> driverLicenseMap(){
        Map<Driver, List<String>> driverLicense = new LinkedHashMap<>();
        for(Employee employee: employees.values()){
            employee.isDriver(driverLicense);
        }
        return driverLicense;
    }

    public void init_Data() {
        this.employees = employeeDAO.SelectEmployeeMap();

    }

    public void setEmployeeNotAvailable(Employee employeeToRemove, LocalDate date, boolean type) {
        employeeToRemove.setNotAvailable(date, type);
        employeeDAO.setBlockedShifts(employeeToRemove.getId(), employeeToRemove.getBlockedShiftsString());
        employeeDAO.setAvailability(employeeToRemove.getId(), employeeToRemove.getAvailabilityString());
    }

    public void availableEmp(Employee employee, Shift shift) {
        employee.addToAvailability(shift.getDate(), shift.isShiftType());
        employee.removeFromWeekShift(shift);
        employeeDAO.setAvailability(employee.getId(), employee.getAvailabilityString());

    }
}
