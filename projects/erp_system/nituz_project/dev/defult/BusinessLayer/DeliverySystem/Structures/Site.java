package defult.BusinessLayer.DeliverySystem.Structures;


import defult.DataAccessLayer.Transport.DAO.SiteTransportDAO;
import defult.DataAccessLayer.Transport.DTO.SiteDTO;

public class Site
{

    // -------- Variables ----------------------------------------------------------------------------------------------------------

    private static int siteCount = 1; // ---
    private int siteID;
    private Address address;
    private String contact_number;
    private String contact_name;
    private int shipping_area;
    private String siteName;
    private boolean isBranch;
    private static SiteTransportDAO dao = new SiteTransportDAO();



    // -------- Constructors -------------------------------------------------------------------------------------------------------


    public Site(Address _address, String _contact_number, String _contact_name, int _shipping_area, String sitename, boolean _branch)
    {
        if(!sitename.equals("Main Logistic Center"))
            siteCount++;
        siteID = siteCount;
        address = _address;
        contact_name = _contact_name;
        contact_number = _contact_number;
        shipping_area = _shipping_area;
        siteName = sitename;
        isBranch = _branch;

    }
    public Site(Address _address, String _contact_number, String _contact_name, int _shipping_area, String sitename,
                boolean _branch,int id)
    {
;        if(!sitename.equals("Main Logistic Center"))
            siteCount++;
        siteID = id;
        address = _address;
        contact_name = _contact_name;
        contact_number = _contact_number;
        shipping_area = _shipping_area;
        siteName = sitename;
        isBranch = _branch;

    }
    public String toString(){
        String siteDescription = "";
        if(isBranch)
            siteDescription = "Branch Address:\t" + address + "\nBranch Name:\t"+ siteName;

        else
            siteDescription = "Supplier Address:\t" + address +"\nSupplier Name:\t"+ siteName;


        siteDescription += "\nContact Number:\t" +contact_number
                           +"\nContact Name:\t" + contact_name
                           +"\nShipping Area:\t"+ shipping_area;

        return siteDescription;
    }


    // -------- getters & setters ----------------------------------------------------------------------------------------------------------


    public int getShipping_area () {
        return shipping_area;
    }

    public String getSiteName () {return siteName.toLowerCase();}


    public boolean isBranch () {
        return isBranch;
    }

    public void insertBase(){
        SiteDTO dto = new SiteDTO(siteID,siteName,address.toString(),contact_name,contact_number,shipping_area,isBranch);
        int inserted = dao.insert(dto);
        if(inserted != 1)
            throw new IllegalArgumentException("INSERT ERROR ACCURED");
    }

    public int getSiteID() {
        return siteID;
    }
}
