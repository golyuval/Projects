package defult.ServiceLayer.Singletones;

import defult.BusinessLayer.Controllers.HR.ShiftBoard;
import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import defult.BusinessLayer.HRsystem.DeliveryTuple;
import defult.BusinessLayer.HRsystem.Employee;
import defult.ServiceLayer.Delivery.DeliveryService;
import defult.ServiceLayer.Delivery.TruckService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static defult.BusinessLayer.HRsystem.Role.DRIVER;
import static defult.BusinessLayer.HRsystem.Role.SHIFT_MANAGER;

public class Generator
{

    private static Generator generator;

    // ----------------- Generator ------------------------------------------------------------------------------------------------

    public Generator() {}
    public static Generator getInstance()
    {
        if(generator == null)
            generator = new Generator();
        return generator;
    }

    // ----------------- Generate Functions ------------------------------------------------------------------------------------------------


    public void generate_Deliveries(DeliveryService deliveryService)
    {

        for (int i=0 ; i<3 ; i++){
            generate_Delivery(      deliveryService,
                                    deliveryService._dc.gatherOrdersToDelivery(),
                                    i+1,i*300,new LinkedList<>(),
                                    LocalDate.now().plusDays(1),
                                    LocalTime.of(12,0),
                                    LocalTime.of(18,0));
        }

        for (int i=3 ; i<5 ; i++){
            generate_Delivery(      deliveryService,
                    deliveryService._dc.gatherOrdersToDelivery(),
                    i+1,i*300,new LinkedList<>(),
                    LocalDate.now().plusDays(1),
                    LocalTime.of(12,0),
                    LocalTime.of(18,0));
        }



        //deliveryService._dc.saveDelivery(LocalDate.now(), LocalTime.of(12,0), LocalTime.of(18,0),deliveryService._dc.gatherOrdersToDelivery().get(0).get_branch().getSiteID(),"A1",30);
        //deliveryService._dc.saveDelivery(LocalDate.now(), LocalTime.of(16,0), LocalTime.of(18,0),deliveryService._dc.gatherOrdersToDelivery().get(0).get_branch().getSiteID(),"B1",35);
        //deliveryService._dc.saveDelivery(LocalDate.now().plusDays(1), LocalTime.of(16,0), LocalTime.of(18,0),deliveryService._dc.gatherOrdersToDelivery().get(0).get_branch().getSiteID(),"B1",40);
       // deliveryService._dc.saveDelivery(LocalDate.now().plusDays(1), LocalTime.of(16,0), LocalTime.of(18,0),deliveryService._dc.gatherOrdersToDelivery().get(0).get_branch().getSiteID(),"C1",50);

    }


    public int generate_Delivery(DeliveryService deliveryService,LinkedList<Order> orders, int truckID, double truckWeight, LinkedList<Integer> overLoad,
                                  LocalDate date, LocalTime departure, LocalTime arrival)
    {
        int x = -1;
        if((x = deliveryService._dc.tryScheduleDelivery(orders,truckID,truckWeight,overLoad,date,departure,arrival)) == -1)
            throw new NoSuchElementException("ERROR");
        deliveryService._dc.saveDelivery(date,departure,arrival,orders.get(0).get_branch().getSiteID(),"A1",x);
        return x;
    }

    public void generate_Delivery_Env(DeliveryService deliveryService)
    {
        generate_Trucks(deliveryService._ts);

        generate_Order(deliveryService);

        generate_Deliveries(deliveryService);

    }

    public LinkedList<String> generate_Order(DeliveryService deliveryService)
    {
        LinkedList<Site> branches = new LinkedList<>();
        LinkedList<Site> suppliers = new LinkedList<>();

        int i=0;
        for(i=0 ; i<9 ; i++)
            branches.add(generate_Site(generate_Address(),true));

        for(i=0 ; i<9 ; i++)
            suppliers.add(generate_Site(generate_Address(),false));

        i=0;
        LinkedList<String> orders = new LinkedList<>();
        for(RequestedOrder current : generate_requestedOrder())
            orders.add(deliveryService.orderDelivery(branches.get(i), suppliers.get(i++),
                    current.get_itemsOrdered()));

        return orders;
    }

    public void generate_Trucks(TruckService ts){
        for(int i = 0 ; i < 10 ; i++)
            generate_truck(10,2000, 500,ts);

        for(int i = 0 ; i < 10 ; i++)
            generate_truck(-20,2500, 1500,ts);

        for(int i = 0 ; i < 10 ; i++)
            generate_truck(0,2500, 500,ts);
    }

    public void generate_truck(double freezeTmp, double maxWeight, double weight, TruckService ts){

        LinkedList<String> licenceRestrictions = new LinkedList<>();
        licenceRestrictions.add("A1");
        licenceRestrictions.add("B1");
        licenceRestrictions.add("C1");

        ts.createTruck(weight,maxWeight,freezeTmp,licenceRestrictions);
    }

    public Address generate_Address(){
        String[] cities = {"Afula","Akko","Arad","Ariel","Ashdod","Ashkelon","Baqa al-Gharbiyye","Bat Yam","Beer Sheva","Beit Shean","Beit Shemesh","Betar Illit","Bnei Berak","Dimona","Eilat","Elad","Givatayim","Hadera","Haifa","Harish","Herzliya","Hod HaSharon","Holon","Jerusalem","Karmiel","Kfar Sava","Kiryat Ata","Kiryat Bialik","Kiryat Gat","Kiryat Malachi","Kiryat Motzkin","Kiryat Ono","Kiryat Shemone","Kiryat Yam","Lod","Maale Adumim","Maalot Tarshiha","Migdal HaEmek","Modiin","Nahariya","Nazareth","Nes Ziona","Nesher","Netanya","Netivot","Nof Hagalil","Ofakim","Or Akiva","Or Yehuda","Petah Tikva","Qalansawe","Raanana","Rahat","Ramat Hasharon","Ramat-Gan","Ramla","Rehovot","Rishon Lezion","Rosh Ha'ayin","Sakhnin","Sderot","Shefaram","Taibeh","Tamra","Tel Aviv","Tiberias","Tira","Tirat Carmel","Tsfat (Safed)","Umm al-Fahm","Yavne","Yehud-Monosson","Yokneam"};
        Random rnd = new Random();
        int i = rnd.nextInt(cities.length-1);
        String city = cities[i].toLowerCase();
        String[] streets = {"Abbey Road","Abbey Street","Abbot's Road","Abbotswell Street","Abingdon Street","Acacia Road","Acorn Street","Acorn Street","Acton Street","Adam Street","Adam Street","Adams Mews","Adelaide Place","Adelaide Place","Adelaide Road","Adelaide Road","Admiral Street","Agnes Street","Albany Street","Albany Street","Albemarle Street","Albert Cottages","Albert Cottages","Albert Cottages","Albert Cottages","Albert Mews","Albert Mews","Albert Mews","Albert Mews","Albert Mews","Albert Road","Albert Road","Albert Road","Albert Road","Albert Road (part)","Albert Road (part)","Albert Square","Albert Street","Albert Street","Albert Street","Albert Street","Albion Mews","Albion Mews (E. part)","Albion Mews (N. part)","Albion Mews (W. part)","Albion Place","Albion Place","Albion Road","Albion Road","Albion Road","Albion Road","Albion Street","Albion Street","Aldred Road","Alexander Street","Alexandra Road","Alexandra Road","Alexandra Road","Alexandra Villas","Alfred Cottages","Alfred Mews","Alfred Place","Alfred Place","Alfred Street","Alfred Street","Alfred Street","Alfred Street","Alfred's Place (Paul's Alley)","Allen Street","Allen Street","Allington Street","Alma Road","Alma Road"};
        int j = rnd.nextInt(streets.length-1);
        String street = cities[i].toLowerCase();
        int number = rnd.nextInt(999);
        return new Address(city,street,number);
    }

    public Site generate_Site(Address address, boolean branch){
        String phoneNumber = "05";
        Random rnd = new Random();
        for(int i=0; i<8; i++){
            int k = rnd.nextInt(10);
            phoneNumber += k;
        }
        String[] names = {"Mary","James","Patricia","Robert","Jennifer","John","Linda","Michael","Elizabeth","David","Barbara","William","Susan","Richard","Jessica","Joseph","Sarah","Thomas","Karen","Charles","Lisa","Christopher","Nancy","Daniel","Betty","Matthew","Margaret","Anthony","Sandra","Mark","Ashley","Donald","Kimberly","Steven","Emily","Paul","Donna","Andrew","Michelle","Joshua","Carol","Kenneth","Amanda","Kevin","Dorothy","Brian","Melissa","George","Deborah","Timothy","Stephanie","Ronald","Rebecca","Edward","Sharon","Jason","Laura","Jeffrey","Cynthia","Ryan","Kathleen","Jacob","Amy","Gary","Angela","Nicholas","Shirley","Eric","Anna","Jonathan","Brenda","Stephen","Pamela","Larry","Emma","Justin","Nicole","Scott","Helen","Brandon","Samantha","Benjamin","Katherine","Samuel","Christine","Gregory","Debra","Alexander","Rachel","Frank","Carolyn","Patrick","Janet","Raymond","Catherine","Jack","Maria","Dennis","Heather","Jerry","Diane","Tyler","Ruth","Aaron","Julie","Jose","Olivia","Adam","Joyce","Nathan","Virginia","Henry","Victoria","Douglas","Kelly","Zachary"};
        int i = rnd.nextInt(names.length-1);
        String name = names[i].toLowerCase();

        int shippingArea = rnd.nextInt(8)+1;

        String[] siteNames = {"Afula","Akko","Arad","Ariel","Ashdod","Ashkelon","Bat Yam","Beer Sheva","Dimona","Eilat","Elad","Givatayim","Hadera","Haifa","Harish","Herzliya","Holon","Jerusalem","Karmiel","Lod","Modiin","Nahariya","Nazareth","Nes Ziona","Nesher","Netanya","Netivot","Ofakim","Or Akiva","Or Yehuda","Petah Tikva","Qalansawe","Raanana","Rahat","Ramat Gan","Ramla","Rehovot","Rishon Lezion","Rosh Ha'ayin","Sakhnin","Sderot","Shefaram","Taibeh","Tamra","Tel Aviv","Tiberias","Tira","Tsfat","Yavne","Yokneam"};
        i = rnd.nextInt(siteNames.length-1);
        String siteName = siteNames[i].toLowerCase();

        Site newSite = new Site(generate_Address(), phoneNumber, name, shippingArea, siteName, branch);
        newSite.insertBase();

        return newSite;

    }

    public LinkedList<RequestedOrder> generate_requestedOrder(){

        LinkedList<RequestedOrder> ro = new LinkedList<>();
        for (int i=0 ; i<9 ; i++)
        {
            Random rnd = new Random();
            String[] foods = {"Salad","Sandwich","Bread","Steak","Tuna Steak","Fish","Shrimp","Rice","Spaghetti","Pizza","Hamburger","Eggs","Cheese","Sausages","Apple Juice","Grape Juice","Milk","Candy","Cookie","Pie","Cake","Cupcake"};
            int k = rnd.nextInt(foods.length-1);
            String food = foods[i/2].toLowerCase();
            String food2 = foods[i/2+1].toLowerCase();
            String food3 = foods[i/2+2].toLowerCase();

            int amount = rnd.nextInt(50)+10;
            int amount2 = rnd.nextInt(50)+10;
            int amount3 = rnd.nextInt(50)+10;
            ro.add(new RequestedOrder(food,amount));
            ro.get(i).addProductAndAmount(food2,amount2);
            ro.get(i).addProductAndAmount(food3,amount3);
        }

        return ro;
    }



    public void generate_HR_Env(ShiftBoard sb){
        LocalDate[] fictiveDate = {     LocalDate.of(2022, 4, 1),
                LocalDate.of(2022, 4, 28),
                LocalDate.of(2022, 5, 15),
                LocalDate.of(2022, 6, 12)    };


        LocalTime dayShiftStart = LocalTime.of(9,00);
        LocalTime dayShiftEnd= LocalTime.of(15,00);
        LocalTime nightShiftStart = LocalTime.of(16,00);
        LocalTime nightShiftEnd = LocalTime.of(23,00);

        List<String> terms1 = new ArrayList<>();
        List<String> terms2 = new ArrayList<>();
        terms1.add("money");
        terms2.add("health");

        String[] roles = {"Shift Manager", "Cashier", "Storage", "Security", "Cleaner", "Organizer", "General"};

        List<String> fictiveRoles1 = new ArrayList<>();
        fictiveRoles1.add(roles[0]);
        fictiveRoles1.add(roles[3]);
        fictiveRoles1.add(roles[6]);

        List<String> fictiveRoles2 = new ArrayList<>();
        fictiveRoles2.add(roles[2]);
        fictiveRoles2.add(roles[1]);
        fictiveRoles2.add(roles[4]);

        List<String> fictiveRoles3 = new ArrayList<>();
        fictiveRoles3.add(roles[0]);
        fictiveRoles3.add(roles[4]);
        fictiveRoles3.add(roles[5]);

        List<String> fictiveRoles4 = new ArrayList<>();
        fictiveRoles4.add(roles[0]);
        fictiveRoles4.add(roles[2]);
        fictiveRoles4.add(roles[5]);

        List<String> fictiveDriver = new ArrayList<>();
        fictiveDriver.add(DRIVER.getRoleName());

        List<String> fictiveDriverLicenses = new ArrayList<>();
        fictiveDriverLicenses.add("C1");
        fictiveDriverLicenses.add("A1");


        sb.EC.newEmployee("nofar", 206962516, 1234, 65, fictiveDate[0], terms1, fictiveRoles1, true, "no special info", "1234" );
        sb.EC.newEmployee("noa", 213890858, 1234, 45, fictiveDate[1], terms2, fictiveRoles2, false, "no special info", "1234" );
        sb.EC.newEmployee("amit", 123456, 1234, 45, fictiveDate[3], terms2, fictiveRoles3, true, "no special info", "1234" );
        sb.EC.newEmployee("gili", 1234567, 1234, 45, fictiveDate[3], terms2, fictiveRoles4, true, "no special info", "1234" );
        sb.EC.newEmployee("nadav", 12345678, 1234, 45, fictiveDate[3], terms2, fictiveRoles4, true, "no special info", "1234" );
        sb.EC.newEmployee("david", 123456789, 1234, 45, fictiveDate[3], terms2, fictiveRoles4, true, "no special info", "1234" );
        sb.EC.newDriver("drivi", 123123, 1234, 45, fictiveDate[3], terms1, fictiveDriver, false, "no", "1234", fictiveDriverLicenses);

        Map<Employee,String> morningStuff = new LinkedHashMap<Employee,String>();
        morningStuff.put(sb.EC.getEmployeeById(206962516),"Shift Manager");
        morningStuff.put(sb.EC.getEmployeeById(213890858),"Cashier");
        Map <Employee,String> nightStuff = new LinkedHashMap<Employee,String>();
        nightStuff.put(sb.EC.getEmployeeById(123456),SHIFT_MANAGER.getRoleName());
        nightStuff.put(sb.EC.getEmployeeById(1234567),"Storage");

        try {
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(123456), LocalDate.now().plusDays(1), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(123456), LocalDate.now().plusDays(1), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(1234567), LocalDate.now().plusDays(1), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(1234567), LocalDate.now().plusDays(1), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(213890858), LocalDate.now().plusDays(1), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(213890858), LocalDate.now().plusDays(1), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(206962516), LocalDate.now().plusDays(1), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(206962516), LocalDate.now().plusDays(1), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(12345678), LocalDate.now().plusDays(1), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(12345678), LocalDate.now().plusDays(1), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(123456789), LocalDate.now().plusDays(1), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(123456789), LocalDate.now().plusDays(1), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(12345678), LocalDate.now().plusDays(2), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(12345678), LocalDate.now().plusDays(2), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(123456789), LocalDate.now().plusDays(2), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(123456789), LocalDate.now().plusDays(2), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(206962516), LocalDate.now().plusDays(2), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(206962516), LocalDate.now().plusDays(2), true);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(213890858), LocalDate.now().plusDays(2), false);
            sb.EC.employeeSubmit(sb.EC.getEmployeeById(213890858), LocalDate.now().plusDays(2), true);
        }
        catch (Exception e){System.out.println(e.getMessage());}

        boolean deliveryT = true;
        boolean deliveryF = false;
        try
        {
            sb.SC.createShift(LocalDate.now(),false, morningStuff,2,LocalTime.of(15,0),nightShiftEnd, deliveryF);
            sb.SC.createShift(LocalDate.now(),true, nightStuff,2,nightShiftStart,nightShiftEnd, deliveryF);
            //sb.SC.createShift(LocalDate.now().plusDays(1),false, morningStuff,1,dayShiftStart,dayShiftEnd, deliveryT);
            sb.SC.createShift(LocalDate.now().plusDays(1),true, nightStuff,3,nightShiftStart,nightShiftEnd, deliveryF);
        }
        catch (Exception e){System.out.println(e.getMessage());}


        sb.EC.getEmployeeById(206962516).setNotAvailable(LocalDate.now(),false);
        sb.EC.getEmployeeById(213890858).setNotAvailable(LocalDate.now(),false);
        sb.EC.getEmployeeById(123456).setNotAvailable(LocalDate.now(),true);
        sb.EC.getEmployeeById(1234567).setNotAvailable(LocalDate.now(),true);
        sb.EC.getEmployeeById(206962516).setNotAvailable(LocalDate.now().plusDays(1),false);
        sb.EC.getEmployeeById(213890858).setNotAvailable(LocalDate.now().plusDays(1),false);
        sb.EC.getEmployeeById(123456).setNotAvailable(LocalDate.now().plusDays(1),true);
        sb.EC.getEmployeeById(1234567).setNotAvailable(LocalDate.now().plusDays(1),true);


    }


}
