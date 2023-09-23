package defult.BusinessLayer.Controllers.Delivery;

import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.BusinessLayer.DeliverySystem.Structures.*;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.BusinessLayer.HRsystem.Driver;
import defult.DataAccessLayer.Transport.DAO.DeliveryTransportDAO;
import defult.DataAccessLayer.Transport.DAO.OrderTransportDAO;
import defult.DataAccessLayer.Repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Delivery_Controller {

    // -------- Variables ----------------------------------------------------------------------------------------------------------

    private final Repo repository = Repo.getInstance();
    private static Delivery_Controller Delivery_Controller_Instance;

    public Truck_Controller _tc;
    public Forms_Controller _fc;

    private Map<Integer, DeliveryTuple> deliveryTupleMap;

    private HashMap<Site, LinkedList<Order>> _ordersBySite; // Re-organized _requestedOrders by Site.
    private LinkedList<Order> _requestedOrders; // All pending orders received.

    private OrderTransportDAO OrderDAO;
    private DeliveryTransportDAO deliveryDAO;


    // -------- Constructor ----------------------------------------------------------------------------------------------------------

    public static Delivery_Controller getInstance() {
        return Delivery_Controller_Instance;
    }

    public Delivery_Controller(Truck_Controller tc, Forms_Controller fc) {
        _requestedOrders = new LinkedList<>();
        _tc = tc;
        _fc = fc;
        _ordersBySite = new HashMap<>();
        Delivery_Controller_Instance = this;
        this.deliveryTupleMap = new HashMap<>();

        OrderDAO = new OrderTransportDAO();
        deliveryDAO = new DeliveryTransportDAO();
    }


    // -------- Handlers ----------------------------------------------------------------------------------------------------------


    public void handleOption1(double weight, double requiredTemperature, String driverLicence,
                              LinkedList<Order> orders, LinkedList<Integer> overloads,
                              LocalDate currD, LocalTime currT, LocalTime arrivalT) // Replace truck
    {
        int newTruckID = _tc.replaceTruck(weight, requiredTemperature, driverLicence);
        if (newTruckID == -1)
            throw new NoSuchElementException("there is no truck available that can carry the order's weight");

        if (!orders.isEmpty()) {
            LinkedList<Site> dests = new LinkedList<>();
            for (Order Order : orders) {
                dests.add(Order.get_supplier());
                dests.add(Order.get_branch());
            }
            String source = "Main Logistic Center";
            Delivery delivery = _fc.addDeliveryToAppending(source, dests, newTruckID, weight, orders, overloads
                    , currD, currT);
        }
    }

    public void handleOption2(String ProductName, LinkedList<Order> orders) // Remove items
    {
        LinkedList<Order> removedOrders = new LinkedList<>();
        if (orders.isEmpty())
            throw new NoSuchElementException("no items left in the current order");
        for (Order currentOrder : orders) {
            HashMap<String, Integer> itemsInOrder = currentOrder.get_order().get_itemsOrdered();
            LinkedList<String> itemsInOrder2 = new LinkedList<>();
            Site supplier = currentOrder.get_supplier();
            Site branch = currentOrder.get_branch();

            for (HashMap.Entry<String, Integer> set : itemsInOrder.entrySet()) {

                if (set.getKey().toLowerCase().strip().equals(ProductName.strip())) {
                    Integer productQuantity = set.getValue();
                    RequestedOrder removedProducts = new RequestedOrder(ProductName, productQuantity);
                    Order returnProductsToOrder = new Order(supplier, branch, removedProducts);
                    removedOrders.add(returnProductsToOrder);
                    itemsInOrder2.add(set.getKey());
                }
            }

            for (String item : itemsInOrder2)
                currentOrder.removeFromCart(item);
        }

        addToPendingOrders(removedOrders);

    }

    public void handleOption3(String BranchName, LinkedList<Order> orders) {
        LinkedList<Order> returnedProducts = new LinkedList<>();
        for (Order currentOrder : orders) {
            if (currentOrder.get_branch().getSiteName().equals(BranchName))
                returnedProducts.add(currentOrder);
        }

        if (returnedProducts.isEmpty())
            throw new NoSuchElementException("returned products is empty");

        for (Order currentOrder : returnedProducts)
            if (orders.contains(currentOrder))
                orders.remove(currentOrder);

        addToPendingOrders(returnedProducts);
    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------

    public void init_Data() {
        OrderDAO.SelectOrder();
        _ordersBySite = OrderDAO.get_ordersBySite();
        _requestedOrders = OrderDAO.get_requestedOrders();

        deliveryDAO.SelectTuple();
        deliveryTupleMap = deliveryDAO.get_deliveryTupleMap();
    }

    public void saveDelivery(LocalDate date, LocalTime departureTime, LocalTime arrivalTime, int branchID, String license, int deliveryId) {
        DeliveryTuple tuple = new DeliveryTuple(license, branchID, LocalDateTime.of(date, arrivalTime), LocalDateTime.of(date, departureTime), deliveryId);
        deliveryTupleMap.put(deliveryId, tuple);
        deliveryDAO.insertTuple(tuple.toDTO());

    }

    public void orderDelivery(Site supplier, Site branch, RequestedOrder order) {

        if (!isValidOrderInput(supplier, branch, order))
            throw new IllegalArgumentException("Some of the order's details are missing.");


        // Creating a new DeliveryRequest and adding it to _requestedOrders.
        Order newOrder = new Order(supplier, branch, order);
        _requestedOrders.addLast(newOrder);


        OrderDAO.insert(newOrder.toDTO());

        // Re-arranging the data to be saved in _ordersBySite.
        if (!_ordersBySite.containsKey(branch))
            _ordersBySite.put(branch, new LinkedList<>());
        else
            _ordersBySite.get(branch).add(newOrder);
    }

    public LinkedList<Order> gatherOrdersToDelivery() // the function gathers 5 Orders currently

    {

        LinkedList<Order> deliveryRoute = new LinkedList<>();

        Order toAddToDelivery = new Order();

        for (int i = 0; i < 1; i++) {

            if (_requestedOrders.isEmpty())

                return deliveryRoute;

            else {

                for (Order o : _requestedOrders) {

                    if (!o.isInDelivery()) {

                        toAddToDelivery = o;

                        deliveryRoute.add(toAddToDelivery);

                        toAddToDelivery.setToInDelivery();

                        OrderDAO.update(toAddToDelivery.toDTO());

                        break;

                    }

                }

            }

            if (_requestedOrders.contains(toAddToDelivery))

                _requestedOrders.remove(toAddToDelivery);

        }

        return deliveryRoute;

    }

    public int recordOverLoad(int solution) {
        return _fc.createOverLoad(solution);
    }

    public void addToPendingOrders(LinkedList<Order> order) {
        for (Order delO : order) {
            _requestedOrders.addLast(delO);
            OrderDAO.insert(delO.toDTO());
        }
    }


    // -------- Check Functions ----------------------------------------------------------------------------------------------------------


    public boolean isValidOrderInput(Site supplier, Site branch, RequestedOrder order) {
        return supplier != null && branch != null && order.get_itemsOrdered().size() != 0;
    }


    public int tryScheduleDeliveryForUI(LinkedList<Order> orders, int truckID,
                                   double truckWeight, LinkedList<Integer> overloads,
                                   LocalDate currD, LocalTime currT, LocalTime arrivalT) {

        // case 1: no trucks Exception ----------
        if (truckID == -1)
            return -1;

        // case 2: no orders Exception ----------
        if (orders == null || orders.isEmpty())
            return -1;


        // --------- No Exception Detected ----------------------------------------------------------------------------

        LinkedList<Site> dest = new LinkedList<>();
        for (Order Order : orders) {
            dest.add(Order.get_supplier());
            dest.add(Order.get_branch());
        }

        // For now all trucks' source is the Main logistic center, will change next by Input on arrival.
        String source = "Main Logistic Center";
        double truckMaxWeight = _tc.getMaxWeightByID(truckID);

        // case 3: handle weight ----------
        if (truckWeight > truckMaxWeight)
            return -1;


        // case 4: Delivery Created ---------
        Delivery delivery = _fc.addDeliveryToAppending(source, dest, truckID, truckWeight, orders, overloads, currD, currT);
        _tc.getTruckByTruckID(truckID).run(); //truck goes on delivery ~10 minutes.
        return delivery.get_delivery_id();

    }

    public int tryScheduleDelivery(LinkedList<Order> orders, int truckID,
                                   double truckWeight, LinkedList<Integer> overloads,
                                   LocalDate currD, LocalTime currT, LocalTime arrivalT) {

        // case 1: no trucks Exception ----------
        if (truckID == -1)
            throw new NoSuchElementException("there are no suitable trucks for this delivery request," +
                    "please wait until a suitable truck returns");

        // case 2: no orders Exception ----------
        if (orders == null || orders.isEmpty())
            throw new NoSuchElementException("there are currently no Orders to ship");


        // --------- No Exception Detected ----------------------------------------------------------------------------

        LinkedList<Site> dest = new LinkedList<>();
        for (Order Order : orders) {
            dest.add(Order.get_supplier());
            dest.add(Order.get_branch());
        }

        // For now all trucks' source is the Main logistic center, will change next by Input on arrival.
        String source = "Main Logistic Center";
        double truckMaxWeight = _tc.getMaxWeightByID(truckID);

        // case 3: handle weight ----------
        if (truckWeight > truckMaxWeight)
            return -1;


        // case 4: Delivery Created ---------
        Delivery delivery = _fc.addDeliveryToAppending(source, dest, truckID, truckWeight, orders, overloads, currD, currT);
        _tc.getTruckByTruckID(truckID).run(); //truck goes on delivery ~10 minutes.
        return delivery.get_delivery_id();

    }

    public List<String> getAllDriversInRange(LocalDate start, LocalDate end) {
        List<String> driverDetails = new LinkedList<>();
        String current;
        for (Delivery delivery : _fc.getShippedDeliveries()) {
            if (delivery.getDeparture_date().isBefore(end) && delivery.getDeparture_date().isAfter(start)) {
                current = "Driver's name: " + delivery.get_driver_name() + "\tDriver's ID: " + delivery.get_delivery_id() +
                        "\tDelivery date: " + delivery.getDeparture_date() + "\n";
                driverDetails.add(current);
            }
        }
        return driverDetails;
    }


    // -------- Getters / Setters --------------------------------------------------------------------------------------


    public Delivery getDeliveryByID(int deliveryID) {
        for (Delivery delivery : _fc.getScheduledDeliveries()) {
            if (delivery.get_delivery_id() == deliveryID)
                return delivery;
        }
        for (Delivery delivery : _fc.getInProgressDeliveries()) {
            if (delivery.get_delivery_id() == deliveryID)
                return delivery;
        }
        return null;
    }

    public LinkedList<Order> get_requestedOrders() {
        return _requestedOrders;
    }

    public void delivery_done(int id) {
        //deliveryTupleMap.get(id).setIsArrival();
        Delivery doneDelivery = _fc.getDeliveryByID(id);
        _fc.Done(doneDelivery);
       // _fc.getShippedDeliveries().add(doneDelivery);
        doneDelivery.Done();
        _fc.getDeliveryDAO().update(doneDelivery.toDTO());


    }

    public Map<Integer, DeliveryTuple> getDeliveryTupleMap() {
        return deliveryTupleMap;
    }

    public void setDriver(int deliveryId, Driver driver) {
        getDeliveryByID(deliveryId).setDriver(driver.getId(), driver.getName());
        deliveryDAO.update(getDeliveryByID(deliveryId).toDTO());
    }
}