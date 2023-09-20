package defult.DataAccessLayer.Transport.DAO;

import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Repo;
import defult.DataAccessLayer.Transport.DTO.SiteDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class SiteTransportDAO extends TransportDAO<SiteDTO> {


    // ----------------------- objects to restore ----------------------------------------------------------------------


    // ----------------------- rest ------------------------------------------------------------------------------------


    public SiteTransportDAO() {
        super("Sites");
    }

    @Override
    public int insert ( SiteDTO _siteDTO ) {
        Connection conn = Repo.getInstance().connect();
        if (_siteDTO == null) {
            return 0;
        } else {


            String Values = String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", _siteDTO.getSiteID(), _siteDTO.getSiteName(), _siteDTO.getAddress(),
                                            _siteDTO.getContactName() ,_siteDTO.getContactNumber(),_siteDTO.getShippingArea(),_siteDTO.isBranch());

            try {
                Statement s = conn.createStatement();
                s.executeUpdate(this.InsertStatement(Values));
                return 1; // success
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return  -1; // failure
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }

    @Override
    public int update(SiteDTO _siteDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_siteDTO == null) {
            return 0; //fail empty
        } else {
            String updateString = String.format("UPDATE %s SET \"siteID\"= \"%s\", \"siteName\"= \"%s\", \"address\"= \"%s\" ," +
                                                " \"contactName\"= \"%s\", \"contactNumber\"= \"%s\", \"shippingArea\"= \"%s\" " +
                                                "WHERE \"siteID\" == \"%s\";",
                    this.tableName, _siteDTO.getSiteID(), _siteDTO.getSiteName(), _siteDTO.getAddress(), _siteDTO.getContactName(),
                                    _siteDTO.getContactNumber(), _siteDTO.getShippingArea(), _siteDTO.getSiteID());

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
    public int delete(SiteDTO _siteDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_siteDTO == null) {
            return 0; //fail empty
        } else {
            String delString = String.format("DELETE FROM Sites WHERE \"siteID\" == \"%s\";", _siteDTO.getSiteID());

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
    public SiteDTO makeDTO(ResultSet RS) {
        SiteDTO output = null;

        try {
            output = new SiteDTO(RS.getInt(1), RS.getString(2), RS.getString(3),
                    RS.getString(4),  RS.getString(5), RS.getInt(6),RS.getBoolean(7));
        } catch (Exception ex) {
            ex.printStackTrace();
            output = null; //failure
        }

        return output;
    }

    @Override
    public Object convertReaderToObject(ResultSet res) throws SQLException, ParseException {
        Site site;
        String[] addressString = res.getString(3).split(",");




        Address address = new Address(addressString[0],addressString[1],Integer.parseInt(addressString[2]));
        site = new Site(address, res.getString(5), res.getString(4), res.getInt(6),
                res.getString(2), res.getBoolean(7),res.getInt(1));
        return site;
    }

    public SiteDTO getSite(int siteID) {
        SiteDTO output = null;
        Connection conn = Repo.getInstance().connect();
        ResultSet RS = null;
        try {
            RS = this.get(this.tableName, "siteID", siteID, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {

            output = new SiteDTO(RS.getInt(1), RS.getString(2), RS.getString(3),
                    RS.getString(4),  RS.getString(5), RS.getInt(6),Boolean.valueOf(RS.getString(7)));



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            output = null;
        }

        finally {
            Repo.getInstance().closeConnection(conn);
        }

        return output;
    }


    public Site getSiteToObject(int siteID) {
        SiteDTO output = null;
        Connection conn = Repo.getInstance().connect();
        ResultSet res = null;
        try {
            res = this.get(this.tableName, "siteID", siteID, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {

            Site site;
            String[] addressString = res.getString(3).split(",");




            Address address = new Address(addressString[0],addressString[1],Integer.parseInt(addressString[2]));
            site = new Site(address, res.getString(5), res.getString(4), res.getInt(6),
                    res.getString(2), res.getBoolean(7),res.getInt(1));
            return site;



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            output = null;
        }

        finally {
            Repo.getInstance().closeConnection(conn);

        }
        return null;
    }

}
