package defult.BusinessLayer.DeliverySystem.Forms;

//import BusinessLayer.Structures.Sites.Branch;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.DataAccessLayer.Transport.DTO.DestFormDTO;

import java.util.HashMap;

public class DestForm implements FormType
{




    // -------- Variables ----------------------------------------------------------------------------------------------------------

    private static int form_counter = 0;
    private int destFormID ;
    private int delivery_ID;
    private Site site ;
    private HashMap<String,Integer> item_quantity ;


    // -------- Constructors -------------------------------------------------------------------------------------------------------


    public DestForm(Site _site, int delivery) // in case no product was delivered
    {
        form_counter++;
        destFormID =  form_counter;
        site = _site;
        item_quantity = new HashMap<>();
        delivery_ID = delivery;
    }

    public DestForm(Site _site, HashMap<String,Integer> items, int delivery_ID)
    {
        this(_site,delivery_ID);
        item_quantity.putAll(items);
    }

    public DestForm(Site _site, int delivery, int df_id, HashMap<String,Integer> items) // in case no product was delivered
    {
        form_counter++;
        destFormID =  df_id;
        site = _site;
        item_quantity = items;
        delivery_ID = delivery;
    }



    // -------- Printers ----------------------------------------------------------------------------------------------------------

    @Override
    public String light_toString() {
        String s = "";
        s+= "Form: " + destFormID + "\t ";
        s+= "Delivery: " + delivery_ID + "\t ";
        s+= "\nDestination:\n " + site + "\n ";
        s+= "Items: " + printOrder() ;

        return  s;
    }
    public String printDestForm() {
        String output =  "Site: " + site.getSiteName() + " \norder:\t" + printOrder();

        return output;
    }
    public String printOrder () {
        String output = "";
        for (HashMap.Entry<String, Integer> set : item_quantity.entrySet()) {
            output += set.getKey() + " : " + set.getValue()+"\n";
        }
        return output;
    }

    public DestFormDTO toDTO(){
        RequestedOrder itemQuantityOrder = new RequestedOrder(item_quantity);
        return new DestFormDTO(destFormID,site.getSiteID(),delivery_ID,itemQuantityOrder.toString());
    }

    // -------- Getters / Setters ----------------------------------------------------------------------------------------------------------

    public static int getForm_counter () {
        return form_counter;
    }

    public Site getSite(){ return site; }

    public int getDelivery_ID () {
        return delivery_ID;
    }

    public int getDestFormID() {
        return destFormID;
    }

    public String getIQ_string()
    {
        String ret = "";
        for(String s : item_quantity.keySet())
            ret+=s+" (amount: "+item_quantity.get(s)+")\n";

        return ret;
    }
}