package defult.DataAccessLayer.Transport.DTO;

import java.time.LocalDateTime;

public class DeliveryTupleDTO extends DTO  {

    String license;
    int branchId;
    int deliveryId;
    String depratureTime;
    String arrivalTime;
    String isArrived;


    public DeliveryTupleDTO(String license, int branchId, String depratureTime,
                            String arrivalTime, int deliveryId, String isArrived) {
        this.license = license;
        this.branchId = branchId;
        this.depratureTime = depratureTime;
        this.arrivalTime = arrivalTime;
        this.deliveryId = deliveryId;
        this.isArrived = isArrived;
    }



    public String getLicense() {
        return license;
    }

    public int getBranchId() {
        return branchId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public String getDepratureTime() {
        return depratureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String isArrived() {
        return isArrived;
    }

}
