package defult.BusinessLayer.HRsystem;

import defult.DataAccessLayer.Transport.DTO.DeliveryTupleDTO;
import defult.ServiceLayer.Delivery.DeliveryService;

import java.time.LocalDateTime;

public class DeliveryTuple {

    String license;
    int branchId;
    int deliveryId;
    LocalDateTime depratureTime;
    LocalDateTime arrivalTime;
    boolean isArrived;


    public DeliveryTuple(String license, int branchId, LocalDateTime depratureTime, LocalDateTime arrivalTime, int deliveryId) {
        this.license = license;
        this.branchId = branchId;
        this.depratureTime = depratureTime;
        this.arrivalTime = arrivalTime;
        this.deliveryId =deliveryId;
        isArrived = false;
    }

    public DeliveryTuple(boolean isArrived,String license, int branchId, LocalDateTime depratureTime, LocalDateTime arrivalTime, int deliveryId) {
        this.license = license;
        this.branchId = branchId;
        this.depratureTime = depratureTime;
        this.arrivalTime = arrivalTime;
        this.deliveryId = deliveryId;
        this.isArrived = isArrived;
    }

    public DeliveryTupleDTO toDTO() {

        if (isArrived)
            return new DeliveryTupleDTO(license, branchId, depratureTime.toString(), arrivalTime.toString(), deliveryId, "True");
        else
            return new DeliveryTupleDTO(license, branchId, depratureTime.toString(), arrivalTime.toString(), deliveryId, "False");
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public LocalDateTime getDepratureTime() {
        return depratureTime;
    }

    public void setDepratureTime(LocalDateTime depratureTime) {
        this.depratureTime = depratureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setIsArrival() {
        isArrived = true;
    }

    public boolean isArrived() {
        return isArrived;
    }
}
