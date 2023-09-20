package defult.DataAccessLayer.Transport.DAO;

import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.DataAccessLayer.Transport.DTO.*;
import defult.DataAccessLayer.Repo;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class DeliveryTransportDAO extends TransportDAO<DeliveryDTO> {


    // ----------------------- objects to restore ----------------------------------------------------------------------


    private LinkedList<Delivery> scheduled = new LinkedList<>();
    private LinkedList<Delivery> progress = new LinkedList<>();
    private LinkedList<Delivery> done = new LinkedList<>();
    private Map<Integer, DeliveryTuple> _deliveryTupleMap = new LinkedHashMap<>(); // All pending orders received.


    // ----------------------- rest ------------------------------------------------------------------------------------


    public DeliveryTransportDAO() {
        super("Deliveries");
        //super.connection = connection;
    }

    public void SelectTuple() {
        String sql = MessageFormat.format("SELECT * From {0}"
                , "tuples");
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            int i = 0;
            while (resultSet.next()) {

                // Fetch each row from the result set
                DeliveryTuple tuple = convertReaderToTuple(resultSet);
                _deliveryTupleMap.put(tuple.getDeliveryId(),tuple);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    public int insert(DeliveryDTO delivery) {

        try
        {
            String sql = "INSERT INTO Deliveries (deliveryID, siteSource, driverID, driverName," +
                    "  supplierSite, branchSite, truckID, truckWeight," +
                    " departureDate, departureTime, arrivalTime, state, orderID, overLoads) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection connection = Repo.getInstance().connect();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, delivery.getDeliveryID());
            statement.setString(2, delivery.getSiteSource());
            statement.setInt(3, delivery.getDriverID());
            statement.setString(4, delivery.getDriverName());
            statement.setInt(5, delivery.getSupplierSite());
            statement.setInt(6, delivery.getBranchSite());
            statement.setInt(7, delivery.getTruckID());
            statement.setDouble(8, delivery.getTruckweight());
            statement.setString(9, delivery.getDepartureDate().toString());
            statement.setString(10, delivery.getDepartureTime().toString());
            statement.setString(11, delivery.getArrivalTime().toString());
            statement.setInt(12, delivery.getState());
            statement.setInt(13, delivery.getOrderID());
            statement.setString(14, delivery.getOverLoads());


            int affectedRows = statement.executeUpdate();
            Repo.getInstance().closeConnection(connection);
            return affectedRows;

        }

        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    public int update(DeliveryDTO delivery) {

        try {

            String sql = "UPDATE Deliveries SET deliveryID = ?, siteSource = ?, driverID = ?," +
                    " driverName = ?, supplierSite = ?, branchSite = ?, truckID = ?, truckWeight = ?," +
                    " departureDate = ?, departureTime = ?, arrivalTime = ?, state = ?, orderID = ?, overLoads = ?" +
                    "WHERE deliveryID = ?";

            Connection connection = Repo.getInstance().connect();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, delivery.getDeliveryID());
            statement.setString(2, delivery.getSiteSource());
            statement.setInt(3, delivery.getDriverID());
            statement.setString(4, delivery.getDriverName());
            statement.setInt(5, delivery.getSupplierSite());
            statement.setInt(6, delivery.getBranchSite());
            statement.setInt(7, delivery.getTruckID());
            statement.setDouble(8, delivery.getTruckweight());
            statement.setString(9, delivery.getDepartureDate().toString());
            statement.setString(10, delivery.getDepartureTime().toString());
            statement.setString(11, delivery.getArrivalTime().toString());
            statement.setInt(12, delivery.getState());
            statement.setInt(13, delivery.getOrderID());
            statement.setString(14, delivery.getOverLoads());
            statement.setInt(15, delivery.getDeliveryID());

            int affectedRows = statement.executeUpdate();
            Repo.getInstance().closeConnection(connection);
            return affectedRows;
        }

        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int delete(DeliveryDTO delivery) {
        try
        {
            Connection connection = Repo.getInstance().connect();
            String sql = "DELETE FROM deliveries WHERE delivery_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, delivery.getDeliveryID());

            int affectedRows = statement.executeUpdate();
            Repo.getInstance().closeConnection(connection);
            return affectedRows;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public DeliveryDTO makeDTO(ResultSet RS) {

        DeliveryDTO output = null;

        try {
            output = new DeliveryDTO(RS.getInt(1), RS.getString(2), RS.getInt(3),
                    RS.getString(4), RS.getInt(6), RS.getInt(5),
                    RS.getInt(7), RS.getInt(8), RS.getString(9),
                    RS.getString(10), RS.getString(11),RS.getInt(12),
                    RS.getInt(13),RS.getString(14));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            output = null; // failure
        }

        return output;
    }

    public DeliveryTupleDTO makeTupleDTO(ResultSet RS) {

        DeliveryTupleDTO output = null;

        try {
            output = new DeliveryTupleDTO(RS.getString(1), RS.getInt(2), RS.getString(3),
                    RS.getString(4), RS.getInt(5), RS.getString(6));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            output = null; // failure
        }

        return output;
    }


    public int insertTuple ( DeliveryTupleDTO _tupleDTO ) {
        Connection conn = Repo.getInstance().connect();
        if (_tupleDTO == null) {
            return 0;
        } else {
            String Values = String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", _tupleDTO.getLicense(),
                    _tupleDTO.getDeliveryId(),_tupleDTO.getBranchId(),_tupleDTO.getDepratureTime(),
                    _tupleDTO.getArrivalTime(),_tupleDTO.isArrived());

            try {
                Statement s = conn.createStatement();
                s.executeUpdate(this.InsertTupleStatement(Values));
                return 1; // success
            } catch (Exception ex) {
                ex.printStackTrace();
                return  0; // failure
            } finally {
                Repo.getInstance().closeConnection(conn);
            }
        }
    }

    private String InsertTupleStatement(String Values)
    {
        return String.format("INSERT INTO %s \nVALUES %s;", "tuples", Values);
    }

    public int deleteTuple(DeliveryTupleDTO tuple) {
        try
        {
            Connection connection = Repo.getInstance().connect();
            String sql = "DELETE FROM tuples WHERE deliveryID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, tuple.getDeliveryId());

            int affectedRows = statement.executeUpdate();
            Repo.getInstance().closeConnection(connection);
            return affectedRows;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public void SelectDeliveries() {

        String sql = MessageFormat.format("SELECT * From {0}"
                , tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                Delivery delivery = convertReaderToObject(resultSet);
                decide_delivery(delivery);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    public void decide_delivery(Delivery delivery) {
        switch (delivery.getState()){
            case 0:
                scheduled.add(delivery);
                break;
            case 1:
                progress.add(delivery);
                break;
            case 2:
                done.add(delivery);
                break;
        }
    }

    @Override
    public Delivery convertReaderToObject(ResultSet res) throws SQLException, ParseException {

        Delivery delivery;

        SiteTransportDAO dao = new SiteTransportDAO();

        SiteDTO supplier_dto = dao.getSite(res.getInt(5));
        SiteDTO branch_dto = dao.getSite(res.getInt(6));

        Site branch = branch_dto.toObject();
        Site supplier = supplier_dto.toObject();
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(supplier);
        sites.add(branch);

        OrderTransportDAO order_dao = new OrderTransportDAO();
        OrderDTO order_dto = order_dao.getOrder(res.getInt(13));
        Order order = order_dto.toObject();
        LinkedList<Order> orders = new LinkedList<>();
        orders.add(order);

        LinkedList<Integer> overloads = new LinkedList<>();
        String[] partial = res.getString(14).split(",");
        for (String s: partial) {
            if(s.length() != 0)
                overloads.add(Integer.parseInt(s.strip()));
        }


        delivery = new Delivery(LocalDate.parse(res.getString(9),dateFormat),
                LocalTime.parse(res.getString(10).substring(0,5),timeFormatter)
                ,res.getString(2),sites,res.getInt(7),
                res.getDouble(8),res.getString(4),
                orders, overloads,res.getInt(1),res.getInt(12));

        return delivery;
    }


    public DeliveryTuple convertReaderToTuple(ResultSet res) throws SQLException, ParseException {

        DeliveryTuple tuple;
        tuple = new DeliveryTuple(Boolean.parseBoolean(res.getString(6)),res.getString(1),
                res.getInt(3),
                LocalDateTime.parse(res.getString(4).replace("T"," "),dateANDtimeFormat),
                LocalDateTime.parse(res.getString(5).replace("T"," "),dateANDtimeFormat),
                res.getInt(2)
                );

        return tuple;
    }

    public LinkedList<Delivery> getScheduled() {
        return scheduled;
    }

    public LinkedList<Delivery> getProgress() {
        return progress;
    }

    public LinkedList<Delivery> getDone() {
        return done;
    }

    public Map<Integer,DeliveryTuple> get_deliveryTupleMap() {
        return _deliveryTupleMap;
    }




}