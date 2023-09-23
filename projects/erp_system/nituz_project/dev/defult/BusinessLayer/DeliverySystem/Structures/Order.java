package defult.BusinessLayer.DeliverySystem.Structures;

import defult.DataAccessLayer.Transport.DTO.OrderDTO;

public class Order {


    // -------- Variables ----------------------------------------------------------------------------------------------------------

    private static int do_count = 0;
    private int orderID;
    private Site _supplierChosen;
    private Site _branchInNeed;
    private RequestedOrder _order;



    private boolean inDelivery;


    // -------- Constructor ----------------------------------------------------------------------------------------------------------


    public Order (Site supplierChosen, Site branchInNeed, RequestedOrder order){
        do_count++;
        orderID = do_count;
        _supplierChosen = supplierChosen;
        _branchInNeed = branchInNeed;
        _order = order;
        inDelivery = false;

//        System.out.print("counter : "+do_count + "  --- ");
//        System.out.println("id : "+orderID);
    }

    public Order (int id, Site supplierChosen, Site branchInNeed, RequestedOrder order){


        do_count++;
        orderID = id;
        _supplierChosen = supplierChosen;
        _branchInNeed = branchInNeed;
        _order = order;

    }

    public Order (int id, Site supplierChosen, defult.BusinessLayer.DeliverySystem.Structures.Site branchInNeed, RequestedOrder order, boolean for_show_only){


        orderID = id;
        _supplierChosen = supplierChosen;
        _branchInNeed = branchInNeed;
        _order = order;


    }

    public Order () {

    }

    public OrderDTO toDTO(){
        return new OrderDTO(orderID,_order.toString1(),_branchInNeed.getSiteID(),_supplierChosen.getSiteID(),isInDelivery());
    }

    // -------- Functions ----------------------------------------------------------------------------------------------------------


    public void addToCart(RequestedOrder o){
        _order.addItemsFromAnotherOrder(o);
    }

    public void removeFromCart(String productName){
        _order.removeProduct(productName);
    }


    // -------- Getters / Setters ----------------------------------------------------------------------------------------------------------

    public boolean isInDelivery () {
        return inDelivery;
    }

    public void setToInDelivery () {
        inDelivery = true;
    }

    public defult.BusinessLayer.DeliverySystem.Structures.Site get_supplier () {
        return _supplierChosen;
    }

    public Site get_branch () {
        return _branchInNeed;
    }

    public RequestedOrder get_order () {
        return _order;
    }

    public String toString(){
        String id = String.valueOf(getDo_id());
        String branch = _branchInNeed.getSiteName() + " ( branch )";
        String supplier = _supplierChosen.getSiteName() + " ( supplier ) ";

        String orderString = "order ID: " +id+"\n"+ supplier + "\t----------->\t" + branch + "\n";
        orderString += _order.toString();

        return orderString;
    }
    public int getDo_id() {
        return orderID;
    }

    public String getIQ_string()
    {
        String ret = "";
        for(String s : _order._itemsOrdered.keySet())
            ret+=s+" (amount: "+_order._itemsOrdered.get(s)+")\n";

        return ret;
    }

    public int getOrderID() {
        return orderID;
    }
}