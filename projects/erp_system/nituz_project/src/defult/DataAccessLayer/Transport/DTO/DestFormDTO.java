package defult.DataAccessLayer.Transport.DTO;
import java.util.Map;

public class DestFormDTO extends DTO  {



    // ----------- Fields ----------------------------------------------------------------------------------------------

    private int destFormID;
    private int siteID;
    private int deliveryID;
    private String itemQuantity;


    // ----------- Constructor ----------------------------------------------------------------------------------------------


    public DestFormDTO(int df_id, int site, int deliveryID, String itemQuantity) {
        this.destFormID = df_id;
        this.siteID = site;
        this.deliveryID = deliveryID;
        this.itemQuantity = itemQuantity;
    }


    // ----------- Getter / Setter ----------------------------------------------------------------------------------------------

    public int getDf_id() {
        return destFormID;
    }

    public int getSite() {
        return siteID;
    }

    public int getDeliveryID() {
        return deliveryID;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setDf_id(int df_id) {
        this.destFormID = df_id;
    }

    public void setSite(int site) {
        this.siteID = site;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}