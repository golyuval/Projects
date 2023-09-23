package defult.DataAccessLayer.Transport.DAO;

import defult.BusinessLayer.DeliverySystem.Forms.DestForm;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Transport.DTO.DestFormDTO;
import defult.DataAccessLayer.Repo;
import defult.DataAccessLayer.Transport.DTO.SiteDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;

public class DestFormTransportDAO extends TransportDAO<DestFormDTO> {


    // ----------------------- objects to restore ----------------------------------------------------------------------


    public HashMap<Integer, LinkedList<DestForm>> destForms_bySite = new HashMap<Integer,LinkedList<DestForm>>();
    public LinkedList<DestForm> destForms = new LinkedList<DestForm>();


    // ----------------------- rest ------------------------------------------------------------------------------------

    public void SelectDestFrom() {

        String sql = MessageFormat.format("SELECT * From {0}"
                , tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                DestForm destForm = convertReaderToObject(resultSet);
                decide_destForm(destForm);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    private void decide_destForm(DestForm destForm) {

        int siteID = destForm.getSite().getSiteID();

        destForms.add(destForm);

        LinkedList<DestForm> newD = new LinkedList<>();
        newD.add(destForm);

        if (destForms_bySite.containsKey(siteID))
            destForms_bySite.get(siteID).add(destForm);
        else
            destForms_bySite.put(siteID,newD);

    }

    public DestFormTransportDAO() {
        super("DestForms");
    }

    @Override
    public int insert ( DestFormDTO _destFormDTO ) {
        Connection conn = Repo.getInstance().connect();
        if (_destFormDTO == null) {
            return 0;
        } else {
            String Values = String.format("(\"%s\",\"%s\",\"%s\",\"%s\")", _destFormDTO.getDf_id(),
                    _destFormDTO.getSite(),_destFormDTO.getDeliveryID(), _destFormDTO.getItemQuantity());

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
    public int update ( DestFormDTO _destFormDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_destFormDTO == null) {
            return 0; //fail empty
        } else {
            String updateString = String.format("UPDATE %s SET \"destFormID\"= \"%s\"" +
                            ", \"siteID\"= \"%s\", \"deliveryID\"= \"%s\" , \"itemQuantity\"= \"%s\"" +
                            "WHERE \"overLoadID\" == \"%s\";",
                    this.tableName, _destFormDTO.getDf_id(), _destFormDTO.getSite(),
                    _destFormDTO.getDeliveryID(), _destFormDTO.getItemQuantity(),
                    _destFormDTO.getDf_id());

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
    public int delete ( DestFormDTO _destFormDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_destFormDTO == null) {
            return 0; //fail empty
        } else {
            String delString = String.format("DELETE FROM DestForms WHERE \"destFormID\" == \"%s\";", _destFormDTO.getDf_id());

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
    public DestFormDTO makeDTO ( ResultSet RS ) {
        DestFormDTO output = null;

        try {
            output = new DestFormDTO(RS.getInt(1), RS.getInt(2), RS.getInt(3),
                    RS.getString(4));
        } catch (Exception ex) {
            ex.printStackTrace();
            output = null; //failure
        }

        return output;
    }

    @Override
    public DestForm convertReaderToObject(ResultSet res) throws SQLException, ParseException {

        DestForm df;

        SiteTransportDAO sitesDao = new SiteTransportDAO();
        SiteDTO siteDto = sitesDao.getSite(res.getInt(2));
        Site site = siteDto.toObject();

        RequestedOrder ro = new RequestedOrder();
        ro.parseItems(res.getString(4));
        df = new DestForm(site,res.getInt(3),res.getInt(1),ro.get_itemsOrdered());

        return df;
    }

    public HashMap<Integer, LinkedList<DestForm>> getDestForms_bySite() {
        return destForms_bySite;
    }

    public LinkedList<DestForm> getDestForms() {
        return destForms;
    }

}