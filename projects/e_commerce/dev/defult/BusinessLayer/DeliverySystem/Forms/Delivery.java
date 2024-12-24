package defult.BusinessLayer.DeliverySystem.Forms;

import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Transport.DTO.DeliveryDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class Delivery implements FormType {

    public enum State {
        Scheduled(0),
        In_Progress(1),
        Done(2) ;

        private int state;

        State(int i) {
            state = i;
        }

        public int getState() {
            return state;
        }
    }


    // -------- Variables ---------------------------------------------------------------------------------------------------------

    static public int scheduled_delivery_counter = 0; // ---
    static public int shipped_delivery_counter = 0; // ---
    static final int Center_site_shippingArea = 0; // ---
    private int deliveryID;
    private String _site_source;
    private LinkedList<Site> _sites;
    private HashMap<Site, RequestedOrder> orderToBranch; // ---
    private LocalDate _departure_date;
    private State state;
    private LocalTime _departure_time;
    private LocalTime _arrival_time;
    private LocalTime _back_to_logistic_center; // ---
    private int _truck_ID;
    private double _truck_weight;
    private String _driver_name;
    private int _driver_id;
    private LinkedList<Order> _orders; // !!
    private LinkedList<Integer> _overloads; // !!


    // -------- Constructors -------------------------------------------------------------------------------------------------------


    public Delivery ( LocalDate out_date, LocalTime out_time, String source, LinkedList<Site> sites, int truck,
                      double weight, String name, LinkedList<Order> orders, LinkedList<Integer> overloads){

        scheduled_delivery_counter++;
        deliveryID = scheduled_delivery_counter;
        _site_source = source;
        _departure_date = out_date;
        _departure_time = out_time;
        _sites = sites;
        _truck_ID = truck;
        _truck_weight = weight;
        _driver_name = name;
        _overloads = overloads;
        _driver_id = -1;
        _orders = orders;
        state = State.Scheduled;

        initOrdersToBranch();
        computeArrivals();

    }


    public Delivery ( LocalDate out_date, LocalTime out_time, String source, LinkedList<Site> sites, int truck,
                      double weight, String name, LinkedList<Order> orders, LinkedList<Integer> overloads,
                      int deliveryID, int state){

        scheduled_delivery_counter++;
        this.deliveryID = deliveryID;
        _site_source = source;
        _departure_date = out_date;
        _departure_time = out_time;
        _sites = sites;
        _truck_ID = truck;
        _truck_weight = weight;
        _driver_name = name;
        _overloads = overloads;
        _driver_id = -1;
        _orders = orders;

        switch(state){
            case 0:
                this.state = State.Scheduled; break;
            case 1:
                this.state = State.In_Progress; break;
            case 2:
                this.state = State.Done; break;
        }


        initOrdersToBranch();
        computeArrivals();

    }


    // -------- Functions -------------------------------------------------------------------------------------------------------------

    public String overLoads_toString()
    {
        String s = "";

        for (int i = 0 ; i<_overloads.size() ; i++) {
            s+=_overloads.get(i);
            if(i != _overloads.size()-1)
                s+=", ";
        }
        return  s;
    }
    public DeliveryDTO toDTO(){
        return new DeliveryDTO(deliveryID,_site_source,_driver_id,_driver_name,_sites.get(0).getSiteID(),_sites.get(1).getSiteID(),
                _truck_ID,_truck_weight, _departure_date.toString(), _departure_time.toString(), _arrival_time.toString(),state.getState(),_orders.get(0).getDo_id(),overLoads_toString());
    }

    public String s_overloads(){
        String s = "";
        for(int i = 0; i<_overloads.size(); i++) {
            s += _overloads.get(i);
            if (i != _overloads.size()-1)
                s+= ", ";
        }
        return s;
    }

    public String s_orders(){
        String s = "\n----------------------------\n";
        for(int i = 0; i<_orders.size(); i++) {
            s += _orders.get(i);
            if (i != _orders.size()-1)
                s+= ", ";
        }
        s += "----------------------------\n";
        return s;
    }

    public void initOrdersToBranch(){
        orderToBranch = new HashMap<>();
        for (Order order : _orders) {

            Site branch = order.get_branch();

            if (orderToBranch.containsKey(branch))
                orderToBranch.get(order.get_branch()).addItemsFromAnotherOrder(order.get_order());
            else
                orderToBranch.put(order.get_branch(), order.get_order());
        }
    }

    public void computeArrivals(){
        int shippingAreaSupplier = _sites.get(0).getShipping_area();
        int shippingAreaBranch = _sites.get(1).getShipping_area();

        int hours = Math.abs(shippingAreaSupplier - Delivery.Center_site_shippingArea)
                + Math.abs(shippingAreaSupplier - shippingAreaBranch);
        _arrival_time = _departure_time.plusHours(hours);
        int hoursToLogistic = Math.abs( Delivery.Center_site_shippingArea - shippingAreaBranch);
        _back_to_logistic_center = _arrival_time.plusHours(hoursToLogistic);
    }

    public String printProducts() {
        String output = "";
        for (HashMap.Entry<Site, RequestedOrder> set : getOrderToBranch().entrySet()){
            String branchName = set.getKey().getSiteName();
            HashMap<String, Integer> BranchOrder = set.getValue().get_itemsOrdered();
            output += branchName + " ordered:\n";
            for (HashMap.Entry<String, Integer> innerSet : BranchOrder.entrySet())
                output += "\t"+innerSet.getKey() + " : " + innerSet.getValue()+"\n";
            output+="\n";
        }
        return output;
    }

    @Override
    public String toString() {
        String output = "";
        output = "Delivery ID: "+ deliveryID
                +"\n"+printProducts();
        return output;
    }

    @Override
    public String light_toString() {
        String s = "";
        s+= "Delivery: " + deliveryID + "\t ";
        s+= "Date: " + _departure_date + "\t ";
        s+= "Time: " + _departure_time + " - " + _arrival_time + "\t ";
        s+= "Driver: " + _driver_name + "\t ";
        s+= "Overloads: " + s_overloads() + "\n";
        s+= "Orders:" + s_orders();

        return  s;
    }

    public String toStringForDrivers() {
        String output = "";
        output = "Delivery ID: "+ deliveryID+" Date: "+_departure_date
                +"\nDeparture time: "+_departure_time+"\nArrival plan: "+_arrival_time;
        return output;
    }

    // -------- getters/setters ----------------------------------------------------------------------------------------------------------


    public LocalDate getDeparture_date () {
        return _departure_date;
    }
    public int get_delivery_id () {
        return deliveryID;
    }
    public HashMap<Site, RequestedOrder> getOrderToBranch () {
        return orderToBranch;
    }
    public String get_driver_name () {
        return _driver_name;
    }
    public static int getDelivery_counter () {
        return scheduled_delivery_counter;
    }
    public LocalTime get_arrival_time () {
        return _arrival_time;
    }
    public LocalTime get_back_to_logistic_center () {
        return _back_to_logistic_center;
    }
    public LinkedList<Order> get_orders() {
        return _orders;
    }
    public LinkedList<Integer> get_overloads() {
        return _overloads;
    }
    public void setDriver(int id, String name) {
        this._driver_id = id;
        this._driver_name = name;
    }
    public int get_driverId() {
        return _driver_id;
    }
    public void set_departure_time ( LocalTime _departure_time ) {
        this._departure_time = _departure_time;
    }
    public int getState() {
        return state.getState();
    }
    public void inProgress() {
        state = State.In_Progress;
    }
    public void Done() {
        state = State.Done;
    }
    public int get_truck_ID () {
        return _truck_ID;
    }
}
