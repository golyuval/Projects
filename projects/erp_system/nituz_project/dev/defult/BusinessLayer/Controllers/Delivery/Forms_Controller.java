package defult.BusinessLayer.Controllers.Delivery;

import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.BusinessLayer.DeliverySystem.Forms.DestForm;
import defult.BusinessLayer.DeliverySystem.Forms.OverLoad;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Transport.DAO.DeliveryTransportDAO;
import defult.DataAccessLayer.Transport.DAO.OrderTransportDAO;
import defult.DataAccessLayer.Transport.DAO.OverLoadTransportDAO;
import defult.DataAccessLayer.Transport.DAO.DestFormTransportDAO;
import defult.DataAccessLayer.Transport.DTO.DeliveryDTO;
import defult.DataAccessLayer.Repo;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Forms_Controller {
    public static Forms_Controller Form_Controller_Instance;
    public static Forms_Controller getInstance() {
        return Form_Controller_Instance;
    }
    // -------- Variables ---------------------------------------------------------------------------------------


    private HashMap<Integer, LinkedList<DestForm>> destForms_bySite;
    private LinkedList<DestForm> destForms;
    private LinkedList<Delivery> scheduledDeliveries;
    private LinkedList<Delivery> inProgressDeliveries;
    private LinkedList<Delivery> doneDeliveries;
    private LinkedList<OverLoad> overloadForms;

    private final Repo repository = Repo.getInstance();
    private DeliveryTransportDAO DeliveryDAO;
    private OverLoadTransportDAO overLoadDAO;
    private DestFormTransportDAO destFormDAO;
    private OrderTransportDAO orderDAO;


    // -------- Constructors ---------------------------------------------------------------------------------------

    public Forms_Controller (){
        destForms = new LinkedList<>();
        destForms_bySite = new HashMap<>();
        scheduledDeliveries = new LinkedList<>();
        inProgressDeliveries = new LinkedList<>();
        doneDeliveries = new LinkedList<>();
        overloadForms = new LinkedList<>();
        DeliveryDAO = new DeliveryTransportDAO();
        overLoadDAO = new OverLoadTransportDAO();
        destFormDAO = new DestFormTransportDAO();
        orderDAO = new OrderTransportDAO();
        Form_Controller_Instance = this;

    }


    // -------- Create --------------------------------------------------------------------------------------

    public void createDestForm( Site currentSite, RequestedOrder items, int deliveryID) {

        DestForm newDestForm = new DestForm(currentSite, items.get_itemsOrdered(), deliveryID);
        destForms.add(newDestForm);
        if (!destForms_bySite.containsKey(currentSite.getSiteID()))
            destForms_bySite.put(currentSite.getSiteID(), new LinkedList<>());
        destForms_bySite.get(currentSite.getSiteID()).add(newDestForm);

        destFormDAO.insert(newDestForm.toDTO());
    }

    public void sendDelivery(int deliverID){
        DeliveryDTO updatedDelivery = new DeliveryDTO();
        for(Delivery delivery : scheduledDeliveries)
            if(delivery.get_delivery_id() == deliverID) {
                updatedDelivery = delivery.toDTO();
                inProgressDeliveries.add(delivery);
                delivery.inProgress();
            }
        Delivery chosenDelivery = getDeliveryByID(deliverID);
        chosenDelivery.set_departure_time(LocalTime.now()); //change Delivery time to now
        chosenDelivery.computeArrivals();
        scheduledDeliveries.remove();

        if(updatedDelivery.getDeliveryID() == -1)
            return;
        updatedDelivery.setState(1);
        DeliveryDAO.update(updatedDelivery);
    }

    public String presentDeliveries(LocalDate date){
        String deliveries = "";
        if(scheduledDeliveries.isEmpty())
            throw new NoSuchElementException("no scheduled deliveries.");
        LinkedList<Delivery> specifiedDeliveries = new LinkedList<>();
        for(Delivery delivery : scheduledDeliveries)
            if(delivery.getDeparture_date().equals(date))
                specifiedDeliveries.add(delivery);
        if(specifiedDeliveries.isEmpty())
            throw new NoSuchElementException("no deliveries scheduled on: " + date + "\npress any key to return the menu");
        for(Delivery delivery : specifiedDeliveries)
            deliveries += delivery.toString()+"\n";
        return deliveries;
    }

    public String inProgressDeliveries(){
        String deliveries = "";
        if(inProgressDeliveries.isEmpty())
            throw new NoSuchElementException("no scheduled deliveries.");
        for(Delivery delivery : inProgressDeliveries)
            deliveries += delivery.toString()+"\n";
        return deliveries;
    }

    public int createOverLoad(int solution){
        OverLoad overLoad = new OverLoad(solution);
        overloadForms.add(overLoad);
        overLoadDAO.insert(overLoad.toDTO());
        return overLoad.get_overload_ID();
    }

    public Delivery addDeliveryToAppending(String source, LinkedList<Site> dests, int truckID, double truckWeight,
                                           LinkedList<Order> orders, LinkedList<Integer> overloads,
                                           LocalDate currD, LocalTime currT) {

        String driverName = "";

        checkDelivery(dests);

        Delivery newDelivery = new Delivery(currD, currT, source, dests, truckID, truckWeight, driverName, orders, overloads);
        for (HashMap.Entry<Site, RequestedOrder> set : newDelivery.getOrderToBranch().entrySet()) {
            RequestedOrder totalSiteProducts = set.getValue();
            createDestForm(set.getKey(), totalSiteProducts,newDelivery.get_delivery_id());
        }
        addDelivery(newDelivery);
        DeliveryDAO.insert(newDelivery.toDTO());
        for(Order o : orders) {
            o.setToInDelivery();

        }
//        for(Order o : orders)
//            orderDAO.delete(orderDAO.getOrder(o.getDo_id()));
        return newDelivery;
    }


    // -------- Add / Remove / Check --------------------------------------------------------------------------------------


    public void init_Data()
    {
        overLoadDAO.SelectOverLoads();
        overloadForms = overLoadDAO.getOverLoads();

        DeliveryDAO.SelectDeliveries();
        scheduledDeliveries = DeliveryDAO.getScheduled();
        inProgressDeliveries = DeliveryDAO.getProgress();
        doneDeliveries = DeliveryDAO.getDone();

        destFormDAO.SelectDestFrom();
        destForms = destFormDAO.getDestForms();
        destForms_bySite = destFormDAO.getDestForms_bySite();
    }
    public void addDelivery(Delivery delivery){
        scheduledDeliveries.add(delivery);
    }

    private void checkDelivery ( LinkedList<Site> dests){
        if(!((!dests.get(0).isBranch()) && dests.get(1).isBranch()))
            System.out.print("");
    }


    // -------- Print --------------------------------------------------------------------------------------

    public String printDestFormByDeliveryID(int deliveryID){
        String allDeliveryDestForms = "";
        if(deliveryID >= Delivery.getDelivery_counter())
            throw new NoSuchElementException("No existing form for this ID.");
        else if( DestForm.getForm_counter() == 0 )
            throw new NoSuchElementException("No recorded destination Forms yet.");


        for(DestForm destForm : getDestFormByDeliveryID(deliveryID)) {
            allDeliveryDestForms += destForm.light_toString() + "\n";
        }

        if( allDeliveryDestForms.length() == 0 )
            throw new NoSuchElementException("No recorded destination Forms for this delivery.");
        return allDeliveryDestForms;
    }

    public String printDestFormsByID(int destID){
        if( DestForm.getForm_counter() == 0 )
            throw new NoSuchElementException("No recorded destination Forms yet.");
        else if (destID >= DestForm.getForm_counter())
            throw new NoSuchElementException("No existing form for this ID.");
        return destForms.get(destID).printDestForm();

    }

    public String printOverLoadsByDeliveryID(int deliveryID){
        String allDeliveryOverLoad = "";
        for(OverLoad overLoad : getOverLoadsByDeliveryID(deliveryID))
            allDeliveryOverLoad += overLoad.printOverLoad() + "\n";
        if( allDeliveryOverLoad.length() == 0 )
            return "# no recorded destination Forms for this delivery #";
        return allDeliveryOverLoad;
    }

    public String printDeliveryByID(int deliveryID) {
        Delivery delivery = getDeliveryByID(deliveryID);
        if(delivery.printProducts().length() == 0)
            return "# no delivery was found #";
        return delivery.printProducts();
    }


    // -------- Getters / Setters --------------------------------------------------------------------------------------

    public LinkedList<Delivery> getScheduledDeliveries() {
        return scheduledDeliveries;
    }

    public LinkedList<OverLoad> getOverLoadsByDeliveryID(int deliveryID) {

        LinkedList<OverLoad> returned_OL = new LinkedList<>();
        for(Delivery del : scheduledDeliveries)
            if(del.get_delivery_id() == deliveryID)
                for (int overload : del.get_overloads())
                    returned_OL.add(overloadForms.get(overload));

        if(returned_OL.size() == 0)
            throw new NoSuchElementException("No recorded overloads for this delivery.");

        return returned_OL;
    }

    public Delivery getDeliveryByID(int deliveryID) {
        for(Delivery delivery : scheduledDeliveries)
            if(delivery.get_delivery_id() == deliveryID)
                return delivery;
        for(Delivery delivery : inProgressDeliveries)
            if(delivery.get_delivery_id() == deliveryID)
                return delivery;
        for(Delivery delivery : doneDeliveries)
            if(delivery.get_delivery_id() == deliveryID)
                return delivery;
        return null;
    }

    public LinkedList<DestForm> getDestFormByDeliveryID(int deliveryID){
        LinkedList<DestForm> allDeliveryDestForms = new LinkedList<>();
        System.out.println(destForms.size());
        for(DestForm destForm : destForms)
            if(destForm.getDelivery_ID() == deliveryID)
                allDeliveryDestForms.add(destForm);

    //    if(allDeliveryDestForms.size() == 0)
      //      throw new NoSuchElementException("No recorded destination Forms for this delivery.");

        return allDeliveryDestForms;
    }
    public LinkedList<Delivery> getShippedDeliveries () {
        return doneDeliveries;
    }


    // -------- Future implementation --------------------------------------------------------------------------------------

    private LinkedList<Delivery> recurringDeliveries; //deliveries that occur periodically.

    public void createRepeatingDelivery(LocalDate deliveryDate, LocalTime deliveryTime
            ,Order recuuringOrder){
        String source = "Main Logistic Center";
        LinkedList<Site> dests = new LinkedList<>();
        dests.add(recuuringOrder.get_supplier());
        dests.add(recuuringOrder.get_branch());


    }

    public DeliveryTransportDAO getDeliveryDAO () {
        return DeliveryDAO;
    }

    public String printAllOverloads() {

        String s = "";
        for ( OverLoad o : overloadForms)
            s+= "\n" + o.light_toString() + "\n";

        return  s;
    }
    public String printAll_ScheduledDeliveries() {

        String s = "";
        for ( Delivery o : scheduledDeliveries)
            s+= "\n" + o.light_toString() + "\n";

        return  s;

    }
    public String printAll_inProgressDeliveries() {

        String s = "";
        for ( Delivery o : inProgressDeliveries)
            s+= "\n" + o.light_toString() + "\n";

        return  s;

    }
    public String printAll_DoneDeliveries() {

        String s = "";
        for ( Delivery o : doneDeliveries)
            s+= "\n" + o.light_toString() + "\n";

        return  s;

    }
    public String printAllDestforms() {

        String s = "";
        for ( DestForm o : destForms)
            s+= "\n" + o.light_toString() + "\n";

        return  s;

    }
    public boolean deliveryExist_schedule(int id) {
        for ( Delivery d : scheduledDeliveries )
            if(d.get_delivery_id() == id)
                return true;

        return false;
    }

    public boolean deliveryExist_inProgress(int id) {
        for ( Delivery d : scheduledDeliveries )
            if(d.get_delivery_id() == id)
                return true;

        return false;
    }

    public LinkedList<Delivery> getInProgressDeliveries () {
        return inProgressDeliveries;
    }

    public LinkedList<DestForm> getDestForms() {
        return destForms;
    }

    public LinkedList<OverLoad> getOverloadForms() {
        return overloadForms;
    }

    public void Done(Delivery d) {
        inProgressDeliveries.remove(d);
        doneDeliveries.add(d);
    }
}