package defult.DataAccessLayer.Transport.DTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DeliveryDTO extends DTO {


    // ----------- Fields ----------------------------------------------------------------------------------------------

    private int deliveryID;
    private String siteSource;
    private int driverID;
    private String driverName;
    private int branchSite;
    private int supplierSite;
    private String departureDate;
    private String departureTime;
    private int truckID;
    private String arrivalTime;
    private double truckweight;
    private int state;
    private int orderID;
    private String overLoads;


    // ----------- Constructor ----------------------------------------------------------------------------------------------
    public DeliveryDTO(){
        this.deliveryID = -1;
        this.siteSource = "";
        this.driverID = -1;
        this.driverName = "";
        this.branchSite = -1;
        this.supplierSite = -1;
        this.departureDate = "";
        this.departureTime = "";
        this.truckID = -1;
        this.arrivalTime = "";
        this.truckweight = -1;
        this.state = -1;
        this.orderID = -1;
        this.overLoads = "";
    }
    public DeliveryDTO(int deliveryID, String siteSource, int driverID, String driverName, int supplier, int branch,
                       int truckID, double truckweight, String departureDate, String departureTime,
                       String arrivalTime , int Dstate, int orderID, String overLoads) {
        this.deliveryID = deliveryID;
        this.siteSource = siteSource;
        this.driverID = driverID;
        this.driverName = driverName;
        this.branchSite = branch;
        this.supplierSite = supplier;
        this.truckID = truckID;
        this.truckweight = truckweight;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.state = Dstate;
        this.orderID = orderID;
        this.overLoads = overLoads;
    }


    // ----------- Getter / Setter ----------------------------------------------------------------------------------------------




    public int getDeliveryID() {
        return deliveryID;
    }

    public String getSiteSource() {
        return siteSource;
    }

    public int getDriverID() {
        return driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getBranchSite() {
        return branchSite;
    }

    public int getSupplierSite() {
        return supplierSite;
    }


    public String getDepartureDate() {
        return departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getTruckID() {
        return truckID;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public double getTruckweight() {
        return truckweight;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public void setSiteSource(String siteSource) {
        this.siteSource = siteSource;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setTruckID(int truckID) {
        this.truckID = truckID;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setTruckweight(double truckweight) {
        this.truckweight = truckweight;
    }

    public int getState () {return state;}

    public void setBranchSite(int branchSite) {
        this.branchSite = branchSite;
    }

    public void setSupplierSite(int supplierSite) {
        this.supplierSite = supplierSite;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOverLoads() {
        return overLoads;
    }

    public void setOverLoads(String overLoads) {
        this.overLoads = overLoads;
    }
    public void setState ( int state ) {this.state = state;}
}