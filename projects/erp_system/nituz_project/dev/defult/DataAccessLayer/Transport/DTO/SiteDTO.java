package defult.DataAccessLayer.Transport.DTO;

import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Site;

public class SiteDTO extends DTO {


    // ----------- Fields ----------------------------------------------------------------------------------------------

    private int siteID;
    private String siteName;
    private String address;
    private String contactName;
    private String contactNumber;
    private int shippingArea;
    private boolean isBranch;


    // ----------- Constructor ----------------------------------------------------------------------------------------------

    public SiteDTO(int siteID, String siteName, String address, String contactName, String contactNumber,
                   int shippingArea, boolean branch) {
        this.siteID = siteID;
        this.siteName = siteName;
        this.address = address;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.shippingArea = shippingArea;
        this.isBranch = branch;
    }


    // ----------- Getter / Setter ----------------------------------------------------------------------------------------------

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }

    public Site toObject() {

        String[] addressString = address.split(",");
        Address compAddress = new Address(addressString[0].strip(), addressString[1].strip(), Integer.parseInt(addressString[2].strip()));
        Site newSite = new Site(compAddress, contactNumber, contactName, shippingArea, siteName, isBranch, siteID);


        return newSite;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getShippingArea() {
        return shippingArea;
    }

    public void setShippingArea(int shippingArea) {
        this.shippingArea = shippingArea;
    }

    public boolean isBranch() {
        return isBranch;
    }

}