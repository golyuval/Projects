package defult.DataAccessLayer.HR.Dal;

import java.time.LocalTime;
import java.util.LinkedList;
import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import defult.BusinessLayer.HRsystem.Employee;
import defult.BusinessLayer.HRsystem.Shift;
import defult.DataAccessLayer.Repo;

public class ShiftHRDAO extends HRDAO {

        public static final String TypeColumnName = "Type";
        public static final String DateColumnName = "Date";
        public static final String ShiftManagerIDColumnName = "ShiftManagerID";
        public static final String BranchColumnName ="BranchId" ;
        public static final String ShiftEventsColumnName = "ShiftEvents";
        public static final String ShiftStartColumnName = "ShiftStart";
        public static final String ShiftEndColumnName = "ShiftEnd";
        public static final String InShiftColumnName = "InShift";
        public static final String EndShiftColumnName = "EndShift";
        public static final String IsNewDeliveryColumnName = "IsNewDelivery";
        public static String  TableName1 = "Shift";
        public static String  TableName2 = "EmployeeShift";
        public static final String EmployeeIDColumnName = "EmployeeID";
        public static final String ShiftDateColumnName = "ShiftDate";
        public static final String ShiftBranchColumnName = "ShiftBranch";
        public static final String ShiftTypeColumnName = "ShiftType";
        public static final String roleColumnName = "Role";



        //<DAta TYPE> Id
        private List <Shift> shiftsCache;
        private EmployeeHRDAO employeeDAO;

        //constructor
        public ShiftHRDAO(EmployeeHRDAO employeeDAO) {
            super(TableName1);
            this.employeeDAO = employeeDAO;
            shiftsCache = new LinkedList<>();
        }

    public boolean Insert(Object shiftObj) {
        Shift shift = (Shift) shiftObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4}, {5}, {6},{7}, {8}, {9}, {10}) VALUES(?,?,?,?,?,?,?,?,?,?) "
                , TableName1, TypeColumnName, DateColumnName, ShiftManagerIDColumnName,BranchColumnName,ShiftEventsColumnName,
                ShiftStartColumnName,ShiftEndColumnName,InShiftColumnName,EndShiftColumnName,IsNewDeliveryColumnName
        );

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, shift.isShiftType());
            pstmt.setString(2, shift.getDate().format(formatters));
            pstmt.setInt(3, shift.getManager().getId());
            pstmt.setInt(4, shift.getBranch());
            pstmt.setString(5, shift.shiftEventString());
            pstmt.setString(6, shift.getShiftStart().format(formatter_3));
            pstmt.setString(7, shift.getShiftEnd().format(formatter_3));
            pstmt.setBoolean(8, shift.isInShift());
            pstmt.setBoolean(9, shift.isEndShift());
            pstmt.setBoolean(10, shift.isNewDelivery());
            pstmt.executeUpdate();

            for (Map.Entry<Employee, String> entry : shift.getFinalEmployees().entrySet()) {
                InsertEmployeeToShift(shift.getDate(),shift.isShiftType(),entry.getKey().getId(), shift.getBranch(),entry.getValue());
            }
        } catch (SQLException e) {
            System.out.println("Got Exception in insert shift:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
//        sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4}, {5}) VALUES(?,?,?,?,?) "
//                , TableName2,EmployeeIDColumnName,ShiftDateColumnName, ShiftBranchColumnName,ShiftTypeColumnName,roleColumnName
//        );
//        try (Connection connection = DriverManager.getConnection(url);
//             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            for (Map.Entry<Employee, String> entry : shift.getFinalEmployees().entrySet()) {
//                Employee employee = entry.getKey();
//                String role = entry.getValue();
//                pstmt.setInt(1, employee.getId());
//                pstmt.setString(2, shift.getDate().format(formatters));
//                pstmt.setInt(3, shift.getBranch());
//                pstmt.setBoolean(4, shift.isShiftType());
//                pstmt.setString(5, role);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Got Exception:");
//            System.out.println(e.getMessage());
//            System.out.println(sql);
//            res = false;
//        }

        return res;
    }
    public boolean Delete(Object shiftObj) {
        Shift shift = (Shift) shiftObj;
        boolean res = true;

        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? "
                , TableName2, ShiftTypeColumnName, ShiftDateColumnName,ShiftBranchColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, shift.isShiftType());
            pstmt.setString(2, shift.getDate().format(formatters));
            pstmt.setInt(3,shift.getBranch());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception in delete employee shift:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? "
                , TableName1,TypeColumnName, DateColumnName, BranchColumnName);

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, shift.isShiftType());
            pstmt.setString(2, shift.getDate().format(formatters));
            pstmt.setInt(3,shift.getBranch());
            pstmt.executeUpdate();
            Repo.getInstance().closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Got Exception in delete shift:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean InsertEmployeeToShift(LocalDate date, boolean shiftType, int employeeID, int branch, String role) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4}, {5}) VALUES(?,?,?,?,?) "
                , TableName2, ShiftDateColumnName, ShiftTypeColumnName, EmployeeIDColumnName, ShiftBranchColumnName,roleColumnName
        );

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.format(formatters));
            pstmt.setBoolean(2, shiftType);
            pstmt.setInt(3,employeeID);
            pstmt.setInt(4, branch);
            pstmt.setString(5,role);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Got Exception in insert employee to shift:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean DeleteEmployeeFromShift(int id, LocalDate date, boolean shiftType) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? "
                , TableName2, EmployeeIDColumnName, ShiftDateColumnName, ShiftTypeColumnName);

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, date.format(formatters));
            pstmt.setBoolean(3, shiftType);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception in delete employee from shift:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


//        public Shift getShift(LocalDate date, boolean shiftType) {
//            List<Shift> l = Select(makeList(DateColumnName, TypeColumnName), makeList(date.format(formatters), shiftType.toString()));
//            if (l.size() == 0)
//                return null;
//            return l.get(0);
//        }


     public List<Shift> SelectAllShifts() {
//            List<Shift> list = (List<Shift>) (List<?>) Select();
         List list = new ArrayList<>();
         String sql = MessageFormat.format("SELECT * From {0}"
                 ,TableName1);
         try (Connection conn = Repo.getInstance().connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
             ResultSet resultSet = pstmt.executeQuery();
             while (resultSet.next()) {
                 // Fetch each row from the result set
                 list.add(convertReaderToObject(resultSet));
             }

         } catch (SQLException | ParseException e) {
             System.out.println("Got Exception:");
             System.out.println(e.getMessage());
             System.out.println(sql);
         }
         return list;

}




    public Map<Employee, String> getEmployeeRoles(String shiftDate, int shiftBranch, boolean shiftType) throws SQLException {
        Map<Employee, String> employeeRolesMap = new HashMap<>();
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT EmployeeID, Role FROM EmployeeShift " +
                             "WHERE ShiftDate = ? AND ShiftBranch = ? AND ShiftType = ?")) {
            stmt.setString(1, shiftDate);
            stmt.setInt(2, shiftBranch);
            stmt.setBoolean(3, shiftType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int employeeID = rs.getInt(EmployeeIDColumnName);
                    Employee employee= employeeDAO.getEmployeeById(employeeID);
                    String role = rs.getString(roleColumnName);
                    employeeRolesMap.put(employee, role);
                }
            }
        }
        return employeeRolesMap;
    }

        @Override
        public Shift convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
            String dateString = rs.getString(2);
//            System.out.println(LocalTime.parse(rs.getString(ShiftStartColumnName)));
            LocalTime startShift= LocalTime.parse(rs.getString(ShiftStartColumnName), formatter_3);
            LocalTime endShift= LocalTime.parse(rs.getString(ShiftEndColumnName), formatter_3);
            Map<Employee, String> employees = getEmployeeRoles(dateString,rs.getInt(4), rs.getBoolean(1));
            Shift shift = new Shift(rs.getBoolean(TypeColumnName), parseLocalDate(dateString), employeeDAO.getEmployeeById(rs.getInt(ShiftManagerIDColumnName)),
            rs.getInt(BranchColumnName), rs.getString(ShiftEventsColumnName),startShift,endShift, rs.getBoolean(InShiftColumnName),
                    rs.getBoolean(EndShiftColumnName), rs.getBoolean(IsNewDeliveryColumnName),employees);

            return shift;
        }

    public void setEndShift(LocalDate date, boolean type, int branch, boolean endShift){
        String UPDATE_INSHIFT_SQL =
                "UPDATE Shift SET " + EndShiftColumnName+ " = ? WHERE Type = ? AND BranchID = ? AND Date = ?";
        int out = 404;
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement statement = conn.prepareStatement(UPDATE_INSHIFT_SQL)) {
            statement.setBoolean(1, endShift);
            statement.setBoolean(2, type);
            statement.setString(4, date.format(formatters));
            statement.setInt(3, branch);
            out = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception in set end shift:");
            System.out.println(e.getMessage());
            System.out.println(UPDATE_INSHIFT_SQL);
        }
    }

    public void setIsNewDelivery(LocalDate date, boolean type, int branch, boolean isNewDelivery){
        String UPDATE_INSHIFT_SQL =
                "UPDATE Shift SET " + isNewDelivery+ " = ? WHERE Type = ? AND BranchID = ? AND Date = ?";
        int out = 404;
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement statement = conn.prepareStatement(UPDATE_INSHIFT_SQL)) {
            statement.setBoolean(1, isNewDelivery);
            statement.setBoolean(2, type);
            statement.setString(4, date.format(formatters));
            statement.setInt(3, branch);
            out = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception in set new delivery:");
            System.out.println(e.getMessage());
            System.out.println(UPDATE_INSHIFT_SQL);
        }
    }

    public void setInShift(LocalDate date, boolean type, int branch, boolean inShift){
        String UPDATE_INSHIFT_SQL =
                "UPDATE Shift SET InShift = ? WHERE Type = ? AND BranchID = ? AND Date = ?";
        int out = 404;
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement statement = conn.prepareStatement(UPDATE_INSHIFT_SQL)) {
            statement.setBoolean(1, inShift);
            statement.setBoolean(2, type);
            statement.setString(4, date.format(formatters));
            statement.setInt(3, branch);
            out = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception in set in shift:");
            System.out.println(e.getMessage());
            System.out.println(UPDATE_INSHIFT_SQL);
        }
    }

    public void setShiftEvents(LocalDate date, boolean shiftType, int branch, String shiftEventString) {
        String UPDATE_SHIFTEVENTS_SQL =
                "UPDATE Shift SET ShiftEvents = ? WHERE Type = ? AND BranchID = ? AND Date = ?";
        int rowsUpdated = 0;

        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement statement = conn.prepareStatement(UPDATE_SHIFTEVENTS_SQL)) {
            statement.setString(1, shiftEventString);
            statement.setBoolean(2, shiftType);
            statement.setInt(3, branch);
            statement.setString(4, date.format(formatters));
            rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception in set shift events:");
            System.out.println(e.getMessage());
            System.out.println(UPDATE_SHIFTEVENTS_SQL);
        }

        //System.out.println(rowsUpdated + " row(s) updated.");
    }


//        public int getShiftManagerIdByShift(String type, String date) {
//            List<Shift> l = Select(makeList(TypeColumnName, DateColumnName), makeList(type, date));
//            if (l.size()==0)
//                return 0;
//            return l.get(0).getManager();
//        }
//
//        public void SetIDmanager(int id, String type, String date) {
//            Update(ShiftManagerIDColumnName, id, makeList(TypeColumnName, DateColumnName), makeList(type, date));
//        }
//
//        public void resetCache() {
//            shiftsCache.clear();
//        }

    public boolean deleteTable()
    {
        deleteTableEmpShi();
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
    private boolean deleteTableEmpShi()
    {
        try
        {
            Connection connection = Repo.getInstance().connect();

            String sql = "DROP TABLE IF EXISTS " + TableName2;
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

