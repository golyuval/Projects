package defult.DataAccessLayer.Transport.DAO;

import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Transport.DTO.OrderDTO;
import defult.DataAccessLayer.Repo;
import defult.DataAccessLayer.Transport.DTO.SiteDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;

public class OrderTransportDAO extends TransportDAO<OrderDTO> {




    // ----------------------- objects to restore ----------------------------------------------------------------------




    private HashMap<Site, LinkedList<Order>> _ordersBySite = new HashMap<>(); // Re-organized _requestedOrders by Site.
    private LinkedList<Order> _requestedOrders = new LinkedList<>(); // All pending orders received.


    // ----------------------- rest ------------------------------------------------------------------------------------


    public void SelectOrder() {
        String sql = MessageFormat.format("SELECT * From {0}"
                , tableName);
        try (Connection conn = Repo.getInstance().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            int i = 0;
            while (resultSet.next()) {

                // Fetch each row from the result set
                Order order = convertReaderToObject(resultSet);
                decide_Order(order);
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    public void decide_Order(Order order)
    {

    _requestedOrders.add(order);

        LinkedList<Order> newO = new LinkedList<>();
        newO.add(order);

        if (_ordersBySite.containsKey(order.get_branch()))
            _ordersBySite.get(order.get_branch()).add(order);
        else
            _ordersBySite.put(order.get_branch(),newO);

    }

    public OrderTransportDAO() {
        super("Orders");
    }

    public OrderDTO getOrder(int orderID) {
        OrderDTO output = null;
        Connection conn = Repo.getInstance().connect();
        ResultSet RS = null;
        try {
            RS = this.get(this.tableName, "orderID", orderID, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            output = new OrderDTO(orderID,RS.getString(2),RS.getInt(3),RS.getInt(4)
                    ,Boolean.parseBoolean(RS.getString(5)));

        } catch (Exception ex) {
            ex.printStackTrace();
            output = null;
        }

        finally {
            Repo.getInstance().closeConnection(conn);
        }

        return output;
    }

    @Override
    public int insert(OrderDTO _OrderDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_OrderDTO == null) {
            return 0;
        } else {
            String Values = String.format("(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")",
                    _OrderDTO.getOrderID(), _OrderDTO.getRequestedOrder(),
                    _OrderDTO.getBranch(), _OrderDTO.getSupplier(),_OrderDTO.isInDelivery());

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
        }    }

    @Override
    public int update(OrderDTO _OrderDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_OrderDTO == null) {
            return 0; //fail empty
        } else {
            String updateString = String.format("UPDATE %s SET \"OrderID\"= \"%s\"" +
                            ", \"requestedOrder\"= \"%s\", \"branch\"= \"%s\" , \"supplier\"= \"%s\"" +
                            ", \"isInDelivery\"= \"%s\"" +
                            "WHERE \"OrderID\" == \"%s\";",
                    this.tableName, _OrderDTO.getOrderID(), _OrderDTO.getRequestedOrder(),
                    _OrderDTO.getBranch(), _OrderDTO.getSupplier(),_OrderDTO.isInDelivery(),
                    _OrderDTO.getOrderID());

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
    public int delete(OrderDTO _OrderDTO) {
        Connection conn = Repo.getInstance().connect();
        if (_OrderDTO == null) {
            return 0; //fail empty
        } else {
            String delString = String.format("DELETE FROM Orders WHERE \"OrderID\" == \"%s\";",
                    _OrderDTO.getOrderID());

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
    public OrderDTO makeDTO(ResultSet RS) {
        OrderDTO output = null;

        try {
            output = new OrderDTO(RS.getInt(1), RS.getString(2), RS.getInt(3),
                    RS.getInt(4),Boolean.parseBoolean(RS.getString(5)));
        } catch (Exception ex) {
            ex.printStackTrace();
            output = null; //failure
        }

        return output;
    }

    @Override
    public Order convertReaderToObject(ResultSet res) throws SQLException, ParseException {

        Order order ;
        SiteTransportDAO dao = new SiteTransportDAO();

        SiteDTO branch_dto = dao.getSite(res.getInt(3));
        SiteDTO supplier_dto = dao.getSite(res.getInt(4));

        Site branch = branch_dto.toObject();
        Site supplier = supplier_dto.toObject();

        RequestedOrder ro = new RequestedOrder();
        ro.parseItems(res.getString(2));

        order = new Order(res.getInt(1),supplier,branch,ro);

        return order;
    }
    public HashMap<Site, LinkedList<Order>> get_ordersBySite() {
        return _ordersBySite;
    }

    public LinkedList<Order> get_requestedOrders() {
        return _requestedOrders;
    }



}