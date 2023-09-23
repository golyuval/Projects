package defult.DataAccessLayer.HR.Dal;

import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Driver;
import defult.DataAccessLayer.Repo;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeHRDAO extends HRDAO {
    public static final String nameColumnName = "Name";
    public static final String idColumnName = "ID";
    public static final String bankInfoColumnName = "BankInfo";
    public static final String salaryColumnName = "Salary";
    public static final String termsColumnName = "Terms";
    public static final String startDateColumnName = "StartDate";
    public static final String rolesColumnName = "Roles";
    public static final String shiftManagerColumnName = "ShiftManager";
    public static final String blockedShiftsColumnName = "BlockedShifts";
    public static final String specialInfoColumnName = "SpecialInfo";
    public static final String availabilityColumnName = "Availability";
    public static final String passwordColumnName = "password";
    public static final String weekShiftsColumnName = "weekShifts";
    public static final String bonusesColumnName = "bonuses";
    public static final String alertColumnName = "alert";
    public static final String licenseColumnName = "License";
    public static String TableName1 = "Employees";
    private Map<Integer, Employee> EmployeeCache;

    public EmployeeHRDAO() {
        super(TableName1);
        EmployeeCache = new HashMap<>();
    }

    public boolean Insert(Object employeeObj) {
        Employee employee = (Employee) employeeObj;
        String License = "none";
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4}, {5}, {6},{7}, {8}, {9}, {10}, {11},{12}, {13}, {14}, {15}, {16}) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
                , TableName1, nameColumnName, idColumnName, bankInfoColumnName, salaryColumnName, termsColumnName,
                startDateColumnName, rolesColumnName, shiftManagerColumnName, blockedShiftsColumnName, specialInfoColumnName, availabilityColumnName,
                passwordColumnName, weekShiftsColumnName, bonusesColumnName, alertColumnName, licenseColumnName
        );

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getId());
            pstmt.setInt(3, employee.getBankInfo());
            pstmt.setInt(4, employee.getSalary());
            pstmt.setString(5, employee.getTermsString());
            pstmt.setString(6, employee.getStartDate().format(formatters));
            pstmt.setString(7, employee.getRolesString());
            pstmt.setBoolean(8, employee.isShiftManager());
            pstmt.setString(9, employee.getBlockedShiftsString());
            pstmt.setString(10, employee.getSpecialInfo());
            pstmt.setString(11, employee.getAvailabilityString());
            pstmt.setString(12, employee.getPassword());
            pstmt.setString(13, employee.getWeekShiftsString());
            pstmt.setString(14, employee.getBonusesString());
            pstmt.setString(15, employee.getAlert());
            pstmt.setString(16, License);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception in insert employee:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Insert(Driver driver) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4}, {5}, {6},{7}, {8}, {9}, {10}, {11},{12}, {13}, {14}, {15}, {16}) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
                , TableName1, nameColumnName, idColumnName, bankInfoColumnName, salaryColumnName, termsColumnName,
                startDateColumnName, rolesColumnName, shiftManagerColumnName, blockedShiftsColumnName, specialInfoColumnName, availabilityColumnName,
                passwordColumnName, weekShiftsColumnName, bonusesColumnName, alertColumnName,licenseColumnName
        );
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,driver.getName());
            pstmt.setInt(2,  driver.getId());
            pstmt.setInt(3,  driver.getBankInfo());
            pstmt.setInt(4,  driver.getSalary());
            pstmt.setString(5,  driver.getTermsString());
            pstmt.setString(6,  driver.getStartDate().format(formatters));
            pstmt.setString(7,  driver.getRolesString());
            pstmt.setBoolean(8,  driver.isShiftManager());
            pstmt.setString(9,  driver.getBlockedShiftsString());
            pstmt.setString(10,  driver.getSpecialInfo());
            pstmt.setString(11,  driver.getAvailabilityString());
            pstmt.setString(12,  driver.getPassword());
            pstmt.setString(13,  driver.getWeekShiftsString());
            pstmt.setString(14,  driver.getBonusesString());
            pstmt.setString(15,  driver.getAlert());
            pstmt.setString(16, driver.getLicenseString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception in insert driver:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public boolean Delete(Object employeeObj) {
        Employee employee = (Employee) employeeObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , TableName1, idColumnName);

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employee.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception in delete employee:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public Employee getEmployeeById(int id) {
        if (EmployeeCache.containsKey(id)) //Employee in cache
            return EmployeeCache.get(id);
        else {//Employee in db
            List<Employee> result = Select(makeList(idColumnName), makeList(String.valueOf(id)));
            if (result.size() == 0)
                throw new IllegalArgumentException("Employee " + id + " does not exist");
            Employee employee = result.get(0);
            EmployeeCache.put(id, employee);
            return employee;
        }
    }
    public void setBankAccount(int id, int bankAccount) {
        Update(bankInfoColumnName, bankAccount, makeList(idColumnName), makeList(String.valueOf(id)));
    }

    public void setSalary(int id, int salary) {
        Update(salaryColumnName, salary, makeList(idColumnName), makeList(String.valueOf(id)));
    }

    public void setHiringConditions(int id, String hiringConditions) {
        Update(termsColumnName, hiringConditions, makeList(idColumnName), makeList(String.valueOf(id)));
    }

    public void setJobType(int id, String jobType) {
        Update(rolesColumnName, jobType, makeList(idColumnName), makeList(String.valueOf(id)));
    }
    public void setSpecialInfo(int id, String SpecialInfo) {
        Update(specialInfoColumnName, SpecialInfo, makeList(idColumnName), makeList(String.valueOf(id)));
    }
    public void setAvailability(int id, String Availability) {
        Update(availabilityColumnName, Availability, makeList(idColumnName), makeList(String.valueOf(id)));
    }

    public void setBlockedShifts(int id, String BlockedShifts) {
        Update(blockedShiftsColumnName, BlockedShifts, makeList(idColumnName), makeList(String.valueOf(id)));
    }
    public Map<Integer,Employee> SelectEmployeeMap() {
        Map map = new HashMap();
        String sql = MessageFormat.format("SELECT * From {0}"
                , _tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                Employee employee = convertReaderToObject(resultSet);
                map.put(employee.getId(), employee);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return map;
    }


    public Employee convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        Employee employee;
        LocalDate date= parseLocalDate(rs.getString(startDateColumnName));
        if (rs.getString(licenseColumnName).equals("none")) {
            employee = new Employee(rs.getString(nameColumnName), rs.getInt(idColumnName), rs.getInt(bankInfoColumnName), rs.getInt(salaryColumnName), rs.getString(termsColumnName),
                    date, rs.getString(rolesColumnName), rs.getBoolean(shiftManagerColumnName), rs.getString(blockedShiftsColumnName),
                    rs.getString(specialInfoColumnName), rs.getString(availabilityColumnName), rs.getString(passwordColumnName), rs.getString(weekShiftsColumnName), rs.getString(alertColumnName), rs.getString(alertColumnName));
            EmployeeCache.put(employee.getId(), employee);
        }
        else {
            employee = new Driver(rs.getString(nameColumnName), rs.getInt(idColumnName), rs.getInt(bankInfoColumnName), rs.getInt(salaryColumnName), rs.getString(termsColumnName),
                    date, rs.getString(rolesColumnName), rs.getBoolean(shiftManagerColumnName), rs.getString(blockedShiftsColumnName),
                    rs.getString(specialInfoColumnName), rs.getString(availabilityColumnName), rs.getString(passwordColumnName), rs.getString(weekShiftsColumnName), rs.getString(alertColumnName), rs.getString(alertColumnName), rs.getString(licenseColumnName));
            EmployeeCache.put(employee.getId(), employee);
        }
        return  employee;
    }

    public boolean deleteTable()
    {
        try
        {
            Connection connection = Repo.getInstance().connect();

            String sql = "DROP TABLE IF EXISTS " + TableName1;
            Statement statement = connection.createStatement();
            statement.execute(sql);

            return true;
        }
        catch (SQLException e)
        {
            System.out.println("------------------------------------- ERROR -----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("-------------------------------------------------------------------------------------");
            return false;
        }

    }
}
