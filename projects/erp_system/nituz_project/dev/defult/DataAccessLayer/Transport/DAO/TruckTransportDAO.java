package defult.DataAccessLayer.Transport.DAO;

import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.DeliverySystem.Structures.Truck;
import defult.DataAccessLayer.Transport.DTO.SiteDTO;
import defult.DataAccessLayer.Transport.DTO.TruckDTO;
import defult.DataAccessLayer.Repo;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

public class TruckTransportDAO extends TransportDAO<TruckDTO> {

    // ----------------------- objects to restore ----------------------------------------------------------------------

    private LinkedList<Truck> available_trucks = new LinkedList<>();
    private LinkedList<Truck> inDelivery_trucks = new LinkedList<>();



    // ----------------------- rest ------------------------------------------------------------------------------------

    public void SelectTrucks() {

        String sql = MessageFormat.format("SELECT * From {0}"
                , tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                Truck truck = convertReaderToObject(resultSet);
                decide_truck(truck);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Trucks all selection:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    private void decide_truck(Truck truck)
    {
        if(truck.isInDelivery())
            inDelivery_trucks.add(truck);
        else
            available_trucks.add(truck);
    }

    public TruckTransportDAO() {
        super("Trucks");
    }

    @Override
    public int insert(TruckDTO _truckDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_truckDTO == null) {
            return 0; // failure - empty
        } else {
            String Values = String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", _truckDTO.getTruckID(),
                    _truckDTO.getWeight(), _truckDTO.getMaxWeight(), _truckDTO.getCoolingCapacity(),
                    _truckDTO.getCurrentSite(),_truckDTO.isInDelivery(), _truckDTO.getLicences());
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(this.InsertStatement(Values));
                return 1; //success

            } catch (Exception ex) {
                ex.printStackTrace();
                return -1; //failure
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }

    @Override
    public int update(TruckDTO _truckDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_truckDTO == null) {
            return 0; //fail - empty dto
        } else {
            String updateString = String.format("UPDATE %s SET " +
                            "\"truckID\"= \"%s\", \"weight\"= %s, \"MaxWeight\"= %s , \"coolingCapacity\"=\"%s\", \"currentSite\"=\"%s\", \"inDelivery\"=\"%s\", \"licenses\"=\"%s\"" +
                            " WHERE \"truckID\" == \"%s\";",
                    this.tableName, _truckDTO.getTruckID(), _truckDTO.getWeight(), _truckDTO.getMaxWeight(), _truckDTO.getCoolingCapacity(),
                    _truckDTO.getCurrentSite(), _truckDTO.isInDelivery(),_truckDTO.getLicences(),_truckDTO.getTruckID());

            try {
                Statement s = conn.createStatement();
                int success = s.executeUpdate(updateString);
                return success;
            }

            catch (Exception ex) {
                ex.printStackTrace();
                return -1;}

            finally {Repo.getInstance().closeConnection(conn);}

        }
    }

    @Override
    public int delete(TruckDTO _truckDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_truckDTO == null) {
            return 0; //failure empty dto
        } else {
            String delString = String.format("DELETE FROM Trucks WHERE \"TruckID\" == %s;", _truckDTO.getTruckID());

            try {
                Statement s = conn.createStatement();
                int success = s.executeUpdate(delString);
                return success;
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }

    public TruckDTO getTruck(String truckID) {
        TruckDTO output = null;
        Connection conn = Repo.getInstance().connect();
        ResultSet RS = this.get(this.tableName, "truckID", truckID, conn);

        try {
            output = new TruckDTO(RS.getInt(1), RS.getDouble(2),
                    RS.getDouble(3), RS.getDouble(4), RS.getInt(5),
                    RS.getBoolean(6), RS.getString(7));
        }
        catch (Exception var9) {
        } finally {
            Repo.getInstance().closeConnection(conn);
        }

        return output;
    }

    @Override
    public TruckDTO makeDTO(ResultSet RS) {
        TruckDTO output = null;

        try {
            output = new TruckDTO(RS.getInt(0), RS.getDouble(1),
                    RS.getDouble(2), RS.getDouble(3), RS.getInt(4),
                    RS.getBoolean(5), RS.getString(7));
        } catch (Exception ex) {
            ex.printStackTrace();

            return null; //failure
        }

        return output;
    }

    public LinkedList<String> transfer(List<String> l)
    {
        LinkedList<String> out = new LinkedList<>();
        for (String s : l)
            out.add(s);

        return out;
    }

    @Override
    public Truck convertReaderToObject(ResultSet res) throws SQLException, ParseException {
        Truck truck;
        LinkedList<String> licenceString = transfer(Arrays.stream(res.getString(7).split(",")).toList());
        SiteTransportDAO sitesDao = new SiteTransportDAO();
        SiteDTO siteDto = sitesDao.getSite(res.getInt(5));
        Site site = siteDto.toObject();
        truck = new Truck(res.getInt(1),res.getDouble(2),res.getDouble(3),
                res.getDouble(4), licenceString,res.getBoolean(6),site);
        return truck;
    }

    public int getBiggestID(){
        int biggestID = 0;
        String SELECT_SQL = String.format("SELECT CASE WHEN COUNT(*) > 0 THEN MAX(%s) ELSE -1 END AS highest_id\n" +
                "FROM %s","truckID",tableName);
        Connection con = Repo.getInstance().connect();
        try {
            Statement stmt = con.createStatement();
            biggestID = stmt.executeUpdate(SELECT_SQL);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            Repo.getInstance().closeConnection(con);
        }

        return biggestID;
    }


    public LinkedList<Truck> getAvailable_trucks() {
        return available_trucks;
    }

    public LinkedList<Truck> getInDelivery_trucks() {
        return inDelivery_trucks;
    }
}
