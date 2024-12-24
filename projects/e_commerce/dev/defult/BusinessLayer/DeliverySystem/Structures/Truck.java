package defult.BusinessLayer.DeliverySystem.Structures;;


import defult.DataAccessLayer.Transport.DTO.TruckDTO;

import java.util.LinkedList;

public class Truck implements Runnable
{
    // -------- Variables ----------------------------------------------------------------------------------------------------------

    static private int truck_counter = 0; // ---
    final private int truckID;
    private double weight;
    private double max_weight;
    private double cooling_capability;
    private LinkedList<String> needed_licence;
    private final int delivery_Duration = 0;

    private volatile int inDeliveryTime;
    private volatile boolean inDelivery;


    // init current Site as Main Logistic center.
    private Site currentSite;
    private Site Main_Logistic_Center;


    // -------- Constructors -------------------------------------------------------------------------------------------------------


    public Truck(Site mainS, double _weight, double max, double cool, LinkedList<String> licence)
    {
        truck_counter++;
        truckID = truck_counter;
        weight = _weight;
        max_weight = max;
        cooling_capability = cool;
        needed_licence = licence;
        inDelivery = false;
        inDeliveryTime = 0;
        currentSite = mainS;
        Main_Logistic_Center = mainS;


        if(truckID == 1)
            Main_Logistic_Center.insertBase();

    }

    public Truck(int id, double _weight, double max, double cool, LinkedList<String> licence, boolean inDelivery, Site site)
    {
        truck_counter++;
        truckID = id;
        weight = _weight;
        max_weight = max;
        cooling_capability = cool;
        needed_licence = licence;
        inDelivery = inDelivery;
        inDeliveryTime = 0;
        currentSite = site;

    }
    // -------- Thread Functions ----------------------------------------------------------------------------------------------------------

    @Override
    public void run () {
        //delivery_thread.start();
        inDelivery = true;

        // delivery in progress duration is ~ 10 minutes
        while(inDeliveryTime < delivery_Duration) {
            inDeliveryTime++;
            try {
                Thread.currentThread().sleep(1_000);
            } catch (InterruptedException e) {
                System.out.println("------------------------------------- ERROR -----------------------------------------");
                System.out.println(e.getMessage());
                System.out.println("-------------------------------------------------------------------------------------");
            }
        }

        // delivery finished
        inDelivery = false;
        inDeliveryTime = 0;
    }
    public void interrupt(){

        //interrupt();
        inDelivery = false;
        inDeliveryTime = 0;

    }


    // -------- Functions ----------------------------------------------------------------------------------------------------------


    public String licenses_toString()
    {
        String s = "";

        for (int i = 0 ; i<needed_licence.size() ; i++) {
            s+=needed_licence.get(i);
            if(i != needed_licence.size()-1)
                s+=", ";
        }

        return  s;
    }

    public TruckDTO toDTO() {
        return new TruckDTO(getId(),getWeight(),getMax_weight(),cooling_capability,currentSite.getSiteID(),isInDelivery(), licenses_toString());
    }

    private double getWeight() {
        return weight;
    }

    public void goToDelivery () {
        inDelivery = true;
    }

    public void returnsFromDelivery () {
        inDelivery = false;
    }
    public boolean isInDelivery () {
        return inDelivery;
    }

    public boolean cool_Enough(double need_to_be_cooled) // true if the truck could cool the item enough
    {
        return   cooling_capability <= need_to_be_cooled;
    }
    public void resetInDeliveryTime () {
        this.inDeliveryTime = 0;
    }

    public void resetInDelivery () {
        this.inDelivery = false;
    }

    public void changeInDelivery(){
        inDelivery = !inDelivery;
    }

    @Override
    public String toString() {
        String s = "";
        s += "\tID : " + truckID + "\n";
        s += "\tMax weight : " + max_weight + "\n";
        s += "\tCooling capability : " + cooling_capability + "\n";
        s += "\tNeeded Licence : " ;
        for (int i = 0 ; i < needed_licence.size() ; i++)
        {
            if(i != needed_licence.size()-1)
                s+= needed_licence.get(i)+", ";
            else
                s+= needed_licence.get(i);
        }
        return s;

    }

    public String light_toString() {
        String s = "";
        s += " Truck: " + truckID + "\t  ";
        s += "weight: " + max_weight + "\t  ";
        s += "cool: " + cooling_capability + "\t  ";
        s += "licence: " ;
        for (int i = 0 ; i < needed_licence.size() ; i++)
        {
            if(i != needed_licence.size()-1)
                s+= needed_licence.get(i)+", ";
            else
                s+= needed_licence.get(i);
        }
        return s;

    }


    // -------- Getters / Setters ----------------------------------------------------------------------------------------------------------

    public double getMax_weight(){ return max_weight; }

    public int getId() {
        return truckID;
    }

    public int getInDeliveryTime () {
        return inDeliveryTime;
    }

    public void setInDeliveryTime ( int inDeliveryTime ) {
        this.inDeliveryTime = inDeliveryTime;
    }

    public LinkedList<String> getNeeded_licence () {
        return needed_licence;
    }

    public Object getCooling_capability() {
        return cooling_capability;
    }
}
