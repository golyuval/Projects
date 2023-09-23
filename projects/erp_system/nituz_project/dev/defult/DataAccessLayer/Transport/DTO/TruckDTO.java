package defult.DataAccessLayer.Transport.DTO;

public class TruckDTO extends DTO {



    // ----------- Fields ----------------------------------------------------------------------------------------------

    private int truckID;
    private double weight;
    private double maxWeight;
    private double coolingCapacity;
    private int currentSite;
    private boolean inDelivery;
    private String licences;


    // ----------- Constructor ----------------------------------------------------------------------------------------------

    public TruckDTO(int truckID, double weight, double maxWeight, double coolingCapacity,
                    int currentSite, boolean inDelivery, String licences) {
        this.truckID = truckID;
        //this.inDeliveryTime = inDeliveryTime;
        this.weight = weight;
        this.maxWeight = maxWeight;
        this.coolingCapacity = coolingCapacity;
        this.currentSite = currentSite;
        this.inDelivery = inDelivery;
        this.licences = licences;
    }


    // ----------- Getter / Setter ----------------------------------------------------------------------------------------------

    public String getLicences() {
        return licences;
    }

    public void setLicences(String licences) {
        this.licences = licences;
    }

    public int getTruckID() {
        return truckID;
    }

    public void setTruckID(int truckID) {
        this.truckID = truckID;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public double getCoolingCapacity() {
        return coolingCapacity;
    }

    public void setCoolingCapacity(double coolingCapacity) {
        this.coolingCapacity = coolingCapacity;
    }

    public int getCurrentSite() {
        return currentSite;
    }

    public void setCurrentSite(int currentSite) {
        this.currentSite = currentSite;
    }

    public boolean isInDelivery() {
        return inDelivery;
    }

    public void setInDelivery(boolean inDelivery) {
        this.inDelivery = inDelivery;
    }
}