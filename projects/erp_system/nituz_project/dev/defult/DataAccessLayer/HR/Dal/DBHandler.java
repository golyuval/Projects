package defult.DataAccessLayer.HR.Dal;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBHandler {

    //HR

    private static DBHandler dbHandler = null;

    public static Connection c = null;

    private DBHandler() {

    }

    public static DBHandler getInstance() {
        if (dbHandler == null) {
            return createDBHandler();
        }
        return dbHandler;
    }

    private static DBHandler createDBHandler() {
        dbHandler = new DBHandler();
        try {
            dbHandler.open();
            dbHandler.init();
        } catch (Exception e) {
        }
        return dbHandler;
    }


    public Connection open() throws Exception {
        if (c != null && !c.isClosed())
            return c;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Database:Delivery_Data.db");
        return c;
    }

    public void close() throws Exception {
        if (c != null)
            c.close();
    }


    public void init() throws Exception {
        try {
            createEmployeeTable();
        } catch (Exception e) {
        }
        try {
            createShiftTable();
        } catch (Exception e) {
        }
        try {
            createEmployeeShiftTable();
        } catch (Exception e) {
        }

    }

    //HR

    private void createEmployeeTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Employees " +
                "(Name VARCHAR(30) NOT NULL," +
                " ID INT NOT NULL," +
                " BankInfo INT NOT NULL," +
                " Salary INT NOT NULL," +
                " Terms VARCHAR NOT NULL," +
                " StartDate VARCHAR(30) NOT NULL," +
                " Roles VARCHAR NOT NULL," +
                " ShiftManager BOOLEAN NOT NULL," +
                " BlockedShifts VARCHAR NOT NULL," +
                " SpecialInfo VARCHAR NOT NULL," +
                " Availability VARCHAR NOT NULL," +
                " password VARCHAR NOT NULL," +
                " weekShifts VARCHAR NOT NULL," +
                " bonuses VARCHAR NOT NULL," +
                " alert VARCHAR NOT NULL," +
                " License VARCHAR NOT NULL," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createEmployeeShiftTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS EmployeeShift " +
                "(ShiftType BOOLEAN NOT NULL," +
                " ShiftDate VARCHAR(30) NOT NULL," +
                " EmployeeID INT NOT NULL," +
                " FOREIGN KEY(EmployeeID) REFERENCES Employee(ID)," +
                " FOREIGN KEY(ShiftDate) REFERENCES Shift(Date)," +
                " FOREIGN KEY(ShiftType) REFERENCES Shift(Type)," +
                " PRIMARY KEY (ShiftType ,ShiftDate ,EmployeeID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createShiftTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Shift " +
                "(Type BOOLEAN NOT NULL," +
                " Date VARCHAR(30) NOT NULL," +
                " ShiftManagerID INT NOT NULL," +
                " BranchId INT NOT NULL," +
                " ShiftEvents VARCHAR NOT NULL," +
                " ShiftStart VARCHAR(30) NOT NULL," +
                " ShiftEnd VARCHAR(30) NOT NULL," +
                " InShift BOOLEAN NOT NULL," +
                " EndShift BOOLEAN NOT NULL," +
                " IsNewDelivery BOOLEAN NOT NULL," +
                " PRIMARY KEY (Type ,Date) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

}
