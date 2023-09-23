package defult.BusinessLayer.DeliverySystem.Structures;

public class Address
{
    public String city;
    public String street;
    public int number;

    public Address(String _city, String _street, int _number)
    {
        city = _city;
        street = _street;
        number = _number;
    }

    @Override
    public String toString() {
        String s = "";
        s+= city;
        s+= ", ";
        s+= street;
        s+= ", ";
        s+= number;
        return s;
    }
}
