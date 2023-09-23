package defult.DataAccessLayer.Transport.DTO;

import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Transport.DAO.SiteTransportDAO;

public class OrderDTO extends DTO  {



    // ----------- Fields ----------------------------------------------------------------------------------------------

    private int OrderID;
    private String requestedOrder;
    private int branch;
    private int supplier;



    private boolean inDelivery;


    // ----------- Constructor ----------------------------------------------------------------------------------------------

    public OrderDTO(int OrderID, String requestedOrder, int branch, int supplier, boolean isInDelivery) {
        this.OrderID = OrderID;
        this.requestedOrder = requestedOrder;
        this.branch = branch;
        this.supplier = supplier;
        this.inDelivery = isInDelivery;
    }

//    public Order findObject(){
//        SiteDAO sitesDao = new SiteDAO();
//        SiteDTO branchDto = sitesDao.getSite(branch);
//        SiteDTO supplierDto = sitesDao.getSite(supplier);
//
//        Site branchSite = branchDto.toObject();
//        Site supplierSite = supplierDto.toObject();
//
//
//        RequestedOrder ro = new RequestedOrder();
//        ro.parseItems(requestedOrder);
//
//        new Order(getOrderID(),branchSite,supplierSite,ro);
//
//        return
//    }

    public Order toObject(){
        SiteTransportDAO sitesDao = new SiteTransportDAO();
        SiteDTO branchDto = sitesDao.getSite(branch);
        SiteDTO supplierDto = sitesDao.getSite(supplier);

        Site branchSite = branchDto.toObject();
        Site supplierSite = supplierDto.toObject();


        RequestedOrder ro = new RequestedOrder();
        ro.parseItems(requestedOrder);

        return new Order(getOrderID(),branchSite,supplierSite,ro,false);
    }

    // ----------- Getter / Setter ----------------------------------------------------------------------------------------------

    public String getRequestedOrder() {
        return requestedOrder;
    }

    public boolean isInDelivery () {
        return inDelivery;
    }

    public void setRequestedOrder(String requestedOrder) {
        this.requestedOrder = requestedOrder;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public int getSupplier() {
        return supplier;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }
}