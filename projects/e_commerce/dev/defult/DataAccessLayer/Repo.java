package defult.DataAccessLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repo {


    // -------- Functions ----------------------------------------------------------------------------------------------------------


    private static Repo instance = null;
    private Connection connection;


    // -------- Functions ----------------------------------------------------------------------------------------------------------

    private Repo() {

    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------


    public static Repo getInstance() {
        if (instance == null)
            instance = new Repo();
        return instance;
    }


    public Connection connect () {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:Database/Delivery_Data.db";
            conn = DriverManager.getConnection(url);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection ( Connection conn ) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createTables() {

        String SitesTable = "CREATE TABLE IF NOT EXISTS \"Sites\" (\n" +
                "\t\"siteID\"\tINTEGER,\n" +
                "\t\"siteName\"\tTEXT,\n" +
                "\t\"address\"\tTEXT,\n" +
                "\t\"ContactName\"\tTEXT,\n" +
                "\t\"ContactNumber\"\tTEXT,\n" +
                "\t\"shippingArea\"\tINTEGER,\n" +
                "\t\"isBranch\"\tBOOLEAN,\n" +
                "\tPRIMARY KEY(\"siteID\")\n" +
                ");";
        //-inDeliveryTime:volatile int (missing - dont need>?)

        String TrucksTable = "CREATE TABLE IF NOT EXISTS \"Trucks\" (\n" +
                "\t\"truckID\"\tINTEGER,\n" +
                "\t\"weight\"\tFLOAT,\n" +
                "\t\"MaxWeight\"\tFLOAT,\n" +
                "\t\"coolingCapacity\"\tFLOAT,\n" +
                "\t\"currentSite\"\tINTEGER,\n" +
                "\t\"inDelivery\"\tBOOLEAN,\n" +
                "\t\"licenses\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"truckID\"),\n" +
                "\tFOREIGN KEY(\"currentSite\") REFERENCES \"Sites\"(\"siteID\")\n" +
                ");";

        String DeliveriesTable = "CREATE TABLE IF NOT EXISTS \"Deliveries\" (\n" +
                "\t\"deliveryID\"\tINTEGER,\n" +
                "\t\"siteSource\"\tString,\n" +
                "\t\"driverID\"\tINTEGER,\n" +
                "\t\"driverName\"\tTEXT,\n" +
                "\t\"supplierSite\"\tINTEGER,\n" +
                "\t\"branchSite\"\tINTEGER,\n" +
                "\t\"truckID\"\tINTEGER,\n" +
                "\t\"truckweight\"\tFLOAT,\n" +

                "\t\"departureDate\"\tTEXT,\n" + //todo: make sure date & time are strings
                "\t\"departureTime\"\tTEXT,\n" + //todo: make sure date & time are strings
                "\t\"arrivalTime\"\tTEXT,\n" + //todo: make sure date & time are strings
                "\t\"state\"\tINTEGER,\n" +
                "\t\"orderID\"\tINTEGER,\n" +
                "\t\"overLoads\"\tTEXT,\n" +


                "\tPRIMARY KEY(\"deliveryID\"),\n" +

                "\tFOREIGN KEY(\"driverID\") REFERENCES \"Employees\"(\"EmployeeID\"),\n" + //todo: fix by table name and column
                "\tFOREIGN KEY(\"siteSource\") REFERENCES \"Sites\"(\"siteID\"),\n" +
                "\tFOREIGN KEY(\"truckID\") REFERENCES \"Trucks\"(\"truckID\"),\n" +
                "\tFOREIGN KEY(\"orderID\") REFERENCES \"Orders\"(\"orderID\"),\n" +

                //---------------- not a list of sites dleivery equals supplier -> branch
                "\tFOREIGN KEY(\"BranchSite\") REFERENCES \"Sites\"(\"siteID\"),\n" +
                "\tFOREIGN KEY(\"SupplierSite\") REFERENCES \"Sites\"(\"siteID\")\n" +
                //----------------
                ");";



        String OverLoadsTable = "CREATE TABLE IF NOT EXISTS \"OverLoads\" (\n" +
                "\t\"overLoadID\"\tINTEGER,\n" +
                "\t\"selectedSolution\"\tINTEGER,\n" +
                "\t\"currentDate\"\tTEXT,\n" +
                "\t\"currentTime\"\tTEXT,\n" +

                "\tPRIMARY KEY(\"overLoadID\"))\n";


        String DestFormsTable = "CREATE TABLE IF NOT EXISTS \"DestForms\" (\n" +
                "\t\"destFormID\"\tINTEGER,\n" +
                "\t\"siteID\"\tINTEGER,\n" +
                "\t\"deliveryID\"\tINTEGER,\n" +
                "\t\"itemQuantity\"\tTEXT,\n" +


                "\tPRIMARY KEY(\"destFormID\"),\n" +
                "\tFOREIGN KEY(\"deliveryID\") REFERENCES \"Deliveries\"(\"deliveryID\"),\n" +
                "\tFOREIGN KEY(\"siteID\") REFERENCES \"Sites\"(\"siteID\")\n" +
                ");";


        String OrdersTable = "CREATE TABLE IF NOT EXISTS \"Orders\" (\n" +
                "\t\"OrderID\"\tINTEGER,\n" +
                "\t\"requestedOrder\"\tTEXT,\n" +
                "\t\"branch\"\tINTEGER,\n" +
                "\t\"supplier\"\tINTEGER,\n" +
                "\t\"isInDelivery\"\tBOOLEAN,\n" +

                "\tPRIMARY KEY(\"OrderID\"),\n" +
                "\tFOREIGN KEY(\"branch\") REFERENCES \"Sites\"(\"siteID\"),\n" +
                "\tFOREIGN KEY(\"supplier\") REFERENCES \"Sites\"(\"siteID\")\n" + //todo: change foreign key to suppliers
                ");";


        String employeesTable = "CREATE TABLE IF NOT EXISTS \"Employees\" (\n" +
                "\t\"Name\"\tVARCHAR(30) NOT NULL,\n" +
                "\t\"ID\"\tINT NOT NULL,\n" +
                "\t\"BankInfo\"\tINT NOT NULL,\n" +
                "\t\"Salary\"\tINT NOT NULL,\n" +
                "\t\"Terms\"\tVARCHAR NOT NULL,\n" +
                "\t\"StartDate\"\tVARCHAR(30) NOT NULL,\n" +
                "\t\"Roles\"\tVARCHAR NOT NULL,\n" +
                "\t\"ShiftManager\"\tBOOLEAN NOT NULL,\n" +
                "\t\"BlockedShifts\"\tVARCHAR NOT NULL,\n" +
                "\t\"SpecialInfo\"\tVARCHAR NOT NULL,\n" +
                "\t\"Availability\"\tVARCHAR NOT NULL,\n" +
                "\t\"password\"\tVARCHAR NOT NULL,\n" +
                "\t\"weekShifts\"\tVARCHAR NOT NULL,\n" +
                "\t\"bonuses\"\tVARCHAR NOT NULL,\n" +
                "\t\"alert\"\tVARCHAR NOT NULL,\n" +
                "\t\"License\"\tVARCHAR NOT NULL,\n" +
                "\tPRIMARY KEY(\"ID\")\n" +
                ");";

        String shiftTable = "CREATE TABLE IF NOT EXISTS \"Shift\" (\n" +
                "\t\"Type\"\tBOOLEAN NOT NULL,\n" +
                "\t\"Date\"\tVARCHAR(30) NOT NULL,\n" +
                "\t\"ShiftManagerID\"\tINT NOT NULL,\n" +
                "\t\"BranchId\"\tINT NOT NULL,\n" +
                "\t\"ShiftEvents\"\tVARCHAR NOT NULL,\n" +
                "\t\"ShiftStart\"\tVARCHAR NOT NULL,\n" +
                "\t\"ShiftEnd\"\tVARCHAR(30) NOT NULL,\n" +
                "\t\"InShift\"\tBOOLEAN NOT NULL,\n" +
                "\t\"EndShift\"\tBOOLEAN NOT NULL,\n" +
                "\t\"IsNewDelivery\"\tBOOLEAN NOT NULL,\n" +
                "\tPRIMARY KEY(\"Type\",\"Date\",\"BranchId\")\n" +
                ");";
        String employeeShiftTable = "CREATE TABLE IF NOT EXISTS \"EmployeeShift\" (\n" +
                "\t\"EmployeeID\"\tINT NOT NULL,\n" +
                "\t\"ShiftDate\"\tVARCHAR(30) NOT NULL,\n" +
                "\t\"ShiftBranch\"\tINT NOT NULL,\n" +
                "\t\"ShiftType\"\tBOOLEAN NOT NULL,\n" +
                "\t\"Role\"\tVARCHAR NOT NULL,\n" +
                "\tFOREIGN KEY(\"EmployeeID\") REFERENCES \"Employee\"(\"ID\"),\n" +
                "\tFOREIGN KEY(\"ShiftBranch\") REFERENCES \"Shift\"(\"BranchId\"),\n" +
                "\tFOREIGN KEY(\"ShiftDate\") REFERENCES \"Shift\"(\"Date\"),\n" +
                "\tFOREIGN KEY(\"ShiftType\") REFERENCES \"Shift\"(\"Type\"),\n" +
                "\tPRIMARY KEY(\"EmployeeID\",\"ShiftDate\",\"ShiftBranch\",\"ShiftType\")\n" +
                ");";

        String tupleTable = "CREATE TABLE IF NOT EXISTS \"tuples\" (\n" +
                "\t\"licence\"\tVARCHAR NOT NULL,\n" +
                "\t\"deliveryID\"\tINT NOT NULL,\n" +
                "\t\"branchID\"\tINT NOT NULL,\n" +
                "\t\"departureTime\"\tVARCHAR NOT NULL,\n" +
                "\t\"arrivalTime\"\tVARCHAR NOT NULL,\n" +
                "\t\"isArrived\"\tBOOLEAN NOT NULL,\n" +
                "\tFOREIGN KEY(\"deliveryID\") REFERENCES \"Deliveries\"(\"deliveryID\")\n" +
                ");";

        Connection conn = connect();
        if (conn == null) {
            return;
        }
        try {

            //-------------------- Transportation tables --------------------
            Statement stmt = conn.createStatement();
            stmt.execute(SitesTable);
            stmt.execute(TrucksTable);
            stmt.execute(DeliveriesTable);
            stmt.execute(OverLoadsTable);
            stmt.execute(DestFormsTable);
            stmt.execute(OrdersTable);
            stmt.execute(tupleTable);
            //---------------------------------------------------------------

            //------------------------- HR tables ---------------------------
            stmt.execute(employeesTable);
            stmt.execute(shiftTable);
            stmt.execute(employeeShiftTable);
            //---------------------------------------------------------------

        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        finally {closeConnection(conn);}
    }


    public List<Integer> getIds(String query) {
        ResultSet rs = null;
        Statement stmt = null;
        ArrayList<Integer> Ids = new ArrayList<>();
        Connection con = connect();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (rs == null) return null;
        try {
            while (rs.next())
                Ids.add(rs.getInt("ID"));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(con);
        }
        return Ids;
    }




}