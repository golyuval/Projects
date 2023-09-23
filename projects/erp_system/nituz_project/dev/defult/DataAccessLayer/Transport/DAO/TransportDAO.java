package defult.DataAccessLayer.Transport.DAO;
import defult.DataAccessLayer.Repo;

import java.sql.*;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public abstract class TransportDAO<DTO> {

    // ------------ Variables ------------------------------------------------------------------------------------------

    public static final String idCol = "ID";
    protected String tableName;


    // ------------ Constructor ------------------------------------------------------------------------------------------


    public TransportDAO(String tableName) {
        //connection = Repo.getInstance().connect();
        this.tableName = tableName;
    }


    // ------------ Abstract Functions ------------------------------------------------------------------------------------------

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateANDtimeFormat  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter  = DateTimeFormatter.ofPattern("HH:mm");

    protected LocalDate parseLocalDate(String data) {
        LocalDate d=null;
        try {
            DateTimeFormatter formatter_1  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            d= LocalDate.parse(data, formatter_1);
        } catch (Exception e) { }
        return d;
    }

    public abstract int insert ( DTO var1 );

    public abstract int update ( DTO var1 );

    public abstract int delete ( DTO var1 );

    public abstract DTO makeDTO(ResultSet var1);


    // ------------ Implemented Functions ------------------------------------------------------------------------------------------
    public List Select() {
        List list = new ArrayList<>();
        String sql = MessageFormat.format("SELECT * From {0}"
                , tableName);
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


    protected List Select(String columnName) {
        List list = new ArrayList<>();
        String sql = MessageFormat.format("SELECT {0} From {1}"
                , columnName, tableName);
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


    public List<String> makeList(String... strings) {
        List<String> list = new ArrayList<String>();
        for (String s : strings)
            list.add(s);
        return list;
    }

    protected List Select(List<String> Columnkeys, List<String> keys) {
        List list = new ArrayList<>();
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT * From {0} WHERE " + keysQuery(Columnkeys)
                , tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
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
    protected String keysQuery(List<String> Columnkeys) {


        String keysQuery = "";
        for (String key : Columnkeys) {
            keysQuery += " " + key + " = ? AND";
        }
        keysQuery = keysQuery.substring(0, keysQuery.length() - 4);


        return keysQuery;

    }
    protected List SelectString(String ColumnName, List<String> Columnkeys, List<String> keys) {
        List list = new ArrayList<>();
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE" + keysQuery(Columnkeys),
                ColumnName, tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                list.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return list;
    }
    public abstract Object convertReaderToObject(ResultSet res) throws SQLException, ParseException;

    protected String InsertStatement(String Values) {
        return String.format("INSERT INTO %s \nVALUES %s;", this.tableName, Values);
    }

//    protected ResultSet get ( String colName, String value, Connection con ) {
//        String SELECT_SQL = String.format("SELECT * FROM %s WHERE %s=\"%s\"", this.tableName, colName, value);
//        ResultSet rs = null;
//
//        try {
//            Statement stmt = con.createStatement();
//            rs = stmt.executeQuery(SELECT_SQL);
//        } catch (SQLException ex) {
//            System.out.println("------------------------------------- ERROR -----------------------------------------");
//            System.out.println(ex.getMessage());
//            System.out.println("-------------------------------------------------------------------------------------");
//        }
//
//        return rs;
//    }

    public ResultSet get(String nameOfTable, String colName, String value, Connection con) {
        String SELECT_SQL = String.format("SELECT * FROM %s WHERE \"%s\"=\"%s\"", nameOfTable, colName, value);
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_SQL);
        } catch (SQLException var8) {
        }

        return rs;
    }

    public ResultSet get(String nameOfTable, String colName, int value, Connection con) throws SQLException {
        String SELECT_SQL = String.format("SELECT * FROM %s WHERE %s = %s", nameOfTable, colName, value);
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_SQL);
        } catch (SQLException var8) {
        }

        return rs;

    }

    public int executeQuery ( String QUERY ) {
        int rowsAffected = -1;
        Connection con = Repo.getInstance().connect();

        try {
            Statement stmt = con.createStatement();
            rowsAffected = stmt.executeUpdate(QUERY);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Repo.getInstance().closeConnection(con);
        }

        return rowsAffected;
    }

    public int delete ( String colName, String value ) {
        String DELETE_SQL = String.format("Delete From %s WHERE %s=%s", this.tableName, colName, value);
        int rowsAffected = -1;
        Connection con = Repo.getInstance().connect();

        try {
            Statement stmt = con.createStatement();
            rowsAffected = stmt.executeUpdate(DELETE_SQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Repo.getInstance().closeConnection(con);
        }

        return rowsAffected;
    }

    protected int getInsertedID ( Connection con ) {
        int output = -1;

        try {
            String query = String.format("SELECT seq FROM sqlite_sequence Where name=\"%s\";", this.tableName);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            output = rs.getInt("seq");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return output;
    }

    public ResultSet getAll(Connection con) {
        String SELECT_SQL = String.format("SELECT * FROM %s", this.tableName);
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_SQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    protected ResultSet selectQuery ( Connection con, String query ) {
        ResultSet rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rs;
    }

    public boolean deleteAll()
    {
        try
        {
            Connection connection = Repo.getInstance().connect();

            String sql = "Delete From "+tableName;
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

    public boolean deleteTable()
    {
        try
        {
            Connection connection = Repo.getInstance().connect();

            String sql = "DROP TABLE IF EXISTS " + tableName;
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

    public boolean deleteTupleTable()
    {
        try
        {
            Connection connection = Repo.getInstance().connect();

            String sql = "DROP TABLE IF EXISTS " + "tuples";
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


