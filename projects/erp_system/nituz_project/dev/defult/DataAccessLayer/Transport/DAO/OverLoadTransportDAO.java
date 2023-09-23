package defult.DataAccessLayer.Transport.DAO;
import defult.BusinessLayer.DeliverySystem.Forms.OverLoad;
import defult.DataAccessLayer.Transport.DTO.OverLoadDTO;
import defult.DataAccessLayer.Repo;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

public class OverLoadTransportDAO extends TransportDAO<OverLoadDTO> {

    // ----------------------- objects to restore ----------------------------------------------------------------------


    LinkedList<OverLoad> OverLoads  = new LinkedList<>();

    // ----------------------- rest ------------------------------------------------------------------------------------



    public OverLoadTransportDAO() {
        super("OverLoads");
    }


    public void SelectOverLoads() {

        String sql = MessageFormat.format("SELECT * From {0}"
                , tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                OverLoad overload = convertReaderToObject(resultSet);
                OverLoads.add(overload);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Trucks all selection:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    @Override
    public int insert ( OverLoadDTO _overLoadDTO ) {
        Connection conn = Repo.getInstance().connect();
        if (_overLoadDTO == null) {
            return 0;
        } else {
            String Values = String.format("(\"%s\",\"%s\",\"%s\",\"%s\")", _overLoadDTO.getOv_id(), _overLoadDTO.getselectedSolution(),
                    _overLoadDTO.getCurrentDate(), _overLoadDTO.getCurrentTime());

            try {
                Statement s = conn.createStatement();
                s.executeUpdate(this.InsertStatement(Values));
                return 1; // success
            } catch (Exception ex) {
                ex.printStackTrace();
                return  0; // failure
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }

    @Override
    public int update ( OverLoadDTO _overLoadDTO ) {
        Connection conn = Repo.getInstance().connect();
        if (_overLoadDTO == null) {
            return 0; //fail empty
        } else {
            String updateString = String.format("UPDATE %s SET \"overLoadID\"= \"%s\", \"selectedSolution\"= \"%s\", \"currentDate\"= \"%s\" , \"currentTime\"= \"%s\"" +
                            "WHERE \"overLoadID\" == \"%s\";",
                    this.tableName, _overLoadDTO.getOv_id(), _overLoadDTO.getselectedSolution(), _overLoadDTO.getCurrentDate(), _overLoadDTO.getCurrentTime(),
                    _overLoadDTO.getOv_id());

            try {
                Statement s = conn.createStatement();
                int success = s.executeUpdate(updateString);
                return success;
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1; //failure
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }

    @Override
    public int delete ( OverLoadDTO _overLoadDTO ) {
        Connection conn = Repo.getInstance().connect();
        if (_overLoadDTO == null) {
            return 0; //fail empty
        } else {
            String delString = String.format("DELETE FROM OverLoads WHERE \"overLoadID\" == \"%s\";", _overLoadDTO.getOv_id());

            try {
                Statement s = conn.createStatement();
                int success = s.executeUpdate(delString);
                return success;
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1; //failure
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }


    @Override
    public OverLoadDTO makeDTO( ResultSet RS) {
        OverLoadDTO output = null;

        try {
            output = new OverLoadDTO(RS.getInt(1), RS.getInt(2), RS.getString(3),
                    RS.getString(4));
        } catch (Exception ex) {
            ex.printStackTrace();
            output = null; //failure
        }
        return output;
    }

    @Override
    public OverLoad convertReaderToObject(ResultSet res) throws SQLException, ParseException {
        OverLoad overLoad;
        overLoad = new OverLoad(res.getInt(1),res.getInt(2),
                LocalDate.parse(res.getString(3),dateFormat), LocalTime.parse(res.getString(4).substring(0,5),timeFormatter));
    return overLoad;
    }

    public LinkedList<OverLoad> getOverLoads() {
        return OverLoads;
    }

}