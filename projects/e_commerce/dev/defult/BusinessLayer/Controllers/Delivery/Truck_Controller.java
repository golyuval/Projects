package defult.BusinessLayer.Controllers.Delivery;

import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.DeliverySystem.Structures.Truck;
import defult.DataAccessLayer.Transport.DAO.TruckTransportDAO;
import defult.DataAccessLayer.Repo;

import java.util.LinkedList;

public class Truck_Controller {
    public static Truck_Controller Truck_Controller_Instance;
    public static Truck_Controller getInstance() {
        return Truck_Controller_Instance;
    }


    // -------- Variables ----------------------------------------------------------------------------------------------------------

    private final Repo repository = Repo.getInstance();

    private static final Site mainSite =  new Site(
            new Address("BeerSheva","Rager",59),
            "052-7903414", "Evyatar Kopans",0,"Main Logistic Center", false);

    private LinkedList<Truck> available_trucks;
    private LinkedList<Truck> inDelivery_trucks;
    private TruckTransportDAO trucksDAO;

    // -------- Constructors -------------------------------------------------------------------------------------------------------

    public Truck_Controller () {
        available_trucks = new LinkedList<>();
        inDelivery_trucks = new LinkedList<>();
        trucksDAO = new TruckTransportDAO();
        Truck_Controller_Instance = this;
    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------

    public void init_Data()
    {
        trucksDAO.SelectTrucks();
        available_trucks = trucksDAO.getAvailable_trucks();
        inDelivery_trucks = trucksDAO.getInDelivery_trucks();
    }
    public void createTruck (double _weight, double max, double cool, LinkedList<String> licence ) {
        Truck newTruck = new Truck(mainSite,_weight, max, cool, licence);
        available_trucks.add(newTruck);
        this.trucksDAO.insert(newTruck.toDTO());
    }

    public void retrieve_Truck(){

        //re-using trucks that finished their delivery before choosing a truck for a delivery
        LinkedList<Truck> trucksThatFinishedDelivery = new LinkedList<Truck>();
        for(Truck occupiedTruck :inDelivery_trucks)
            if(!occupiedTruck.isInDelivery())
                trucksThatFinishedDelivery.add(occupiedTruck);

        for(Truck newlyAvailableTruck : trucksThatFinishedDelivery){
            inDelivery_trucks.remove(newlyAvailableTruck);
            available_trucks.add(newlyAvailableTruck);
        }
    }

    public int getNewTruck(double freezeTMP, String licence){
        retrieve_Truck();
        int truckID = getSuitableTruckID(freezeTMP, licence);
        return truckID;
    }

    public boolean truckSuitsDelivery(Truck truck, double requiredTemperature, String driverLicence){
        if(truck == null)
            return false;
        return truck.cool_Enough(requiredTemperature) &&
                truck.getNeeded_licence().contains(driverLicence);
    }

    public int replaceTruck(double weight,double temp, String dLicence) {
        for (Truck truck : getAvailable_trucks())
            if (weight <= truck.getMax_weight() && truckSuitsDelivery(truck, temp, dLicence)){
               /* getTruckByTruckID(truck.getId()).run(); //truck goes on delivery ~10 minutes.
                Thread t1= new Thread(truck);
                t1.start(); */
                return truck.getId();
            }
        return -1;
    }

    public boolean canReplaceTruck(double weight, double temp,String licence){
        for(Truck truck : getAvailable_trucks())
            if(weight <= truck.getMax_weight() & truck.cool_Enough(temp) & truck.getNeeded_licence().contains(licence))
                return true;
        return false;
    }

    public String print_AvailableTrucks()
    {
        String s = "";

        for (Truck truck : available_trucks)
            s += truck.light_toString()+"\n";

        return s;
    }

    public String print_UnavailableTrucks()
    {
        String s = "";

        for (Truck truck : inDelivery_trucks)
            s += truck.light_toString()+"\n";

        return s;

    }


    // -------- Getters / Setters ----------------------------------------------------------------------------------------------------------

    public int getSuitableTruckID(double requiredTemperature, String driverLicence) {
        if (available_trucks.size() != 0)
            // choose a suitable truck for the delivery
            for(Truck currentTruck : available_trucks)
                if (truckSuitsDelivery(currentTruck, requiredTemperature, driverLicence)){
                    return currentTruck.getId();
                }
        return -1;
    }

    public Truck getTruckByTruckID(int truckID) {
        for(Truck truck : getAvailable_trucks())
            if(truck.getId() == truckID)
                return truck;
        for(Truck truck : getInDelivery_trucks())
            if(truck.getId() == truckID)
                return truck;
        return null;
    }

    public double getMaxWeightByID(int truckID){
        for(Truck truck : getAvailable_trucks())
            if(truckID == truck.getId())
                return truck.getMax_weight();
        return -1;
    }

    public LinkedList<Truck> getAvailable_trucks() {
        return available_trucks;
    }

    public void truckGoesOnDelivery(int truckID){
        Truck truck = getTruckByTruckID(truckID);
        truck.goToDelivery();
        available_trucks.remove(truck);
        inDelivery_trucks.add(truck);
        trucksDAO.update(truck.toDTO());
    }

    public void truckReturnsFromDelivery(int truckID){
        Truck truck = getTruckByTruckID(truckID);
        truck.returnsFromDelivery();
        inDelivery_trucks.remove(truck);
        available_trucks.add(truck);
        trucksDAO.update(truck.toDTO());
    }

    public TruckTransportDAO getTrucksDAO () {
        return trucksDAO;
    }

    public LinkedList<Truck> getInDelivery_trucks () {
        return inDelivery_trucks;
    }
}