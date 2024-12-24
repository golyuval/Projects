package BusinessLayer.Forms;

import defult.BusinessLayer.DeliverySystem.Forms.Delivery;
import defult.BusinessLayer.DeliverySystem.Structures.Address;
import defult.BusinessLayer.DeliverySystem.Structures.Order;
import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.BusinessLayer.DeliverySystem.Structures.Site;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Random;

import static defult.Main.repository;
import static defult.Main.resetDataBase;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {


    @Test
    void removeFromOrderToBranch() {
        resetDataBase();
        repository.createTables();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String source = "Main Logistic Center";
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(siteGenerator(AddressGenerator()));
        int truck = 1;
        double weight = 500;
        String name = "kopans";
        LinkedList<Order> orders = new LinkedList<>();
        for(int i = 0; i < 10; i ++)
            orders.add(new Order(siteGenerator(AddressGenerator()),siteGenerator(AddressGenerator()),requestedOrderGenerator()));
        Delivery delivery = new Delivery(date,time,source,sites,truck,weight,name,orders,new LinkedList<>());

    }

    public static Address AddressGenerator(){
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

    public static Site siteGenerator(Address address){
        String phoneNumber = "05";
        Random rnd = new Random();
        for(int i=0; i<8; i++){
            int k = rnd.nextInt(10);
            phoneNumber += k;
        }
        String[] names = {"Mary","James","Patricia","Robert","Jennifer","John","Linda","Michael","Elizabeth","David","Barbara","William","Susan","Richard","Jessica","Joseph","Sarah","Thomas","Karen","Charles","Lisa","Christopher","Nancy","Daniel","Betty","Matthew","Margaret","Anthony","Sandra","Mark","Ashley","Donald","Kimberly","Steven","Emily","Paul","Donna","Andrew","Michelle","Joshua","Carol","Kenneth","Amanda","Kevin","Dorothy","Brian","Melissa","George","Deborah","Timothy","Stephanie","Ronald","Rebecca","Edward","Sharon","Jason","Laura","Jeffrey","Cynthia","Ryan","Kathleen","Jacob","Amy","Gary","Angela","Nicholas","Shirley","Eric","Anna","Jonathan","Brenda","Stephen","Pamela","Larry","Emma","Justin","Nicole","Scott","Helen","Brandon","Samantha","Benjamin","Katherine","Samuel","Christine","Gregory","Debra","Alexander","Rachel","Frank","Carolyn","Patrick","Janet","Raymond","Catherine","Jack","Maria","Dennis","Heather","Jerry","Diane","Tyler","Ruth","Aaron","Julie","Jose","Olivia","Adam","Joyce","Nathan","Virginia","Henry","Victoria","Douglas","Kelly","Zachary"};
        int i = rnd.nextInt(names.length-1);
        String name = names[i].toLowerCase();
        int shippingArea = rnd.nextInt(9);
        String[] siteNames = {"AfulaSite","AkkoSite","AradSite","ArielSite","AshdodSite","AshkelonSite","Baqa al-GharbiyyeSite","Bat YamSite","Beer ShevaSite","Beit SheanSite","Beit ShemeshSite","Betar IllitSite","Bnei BerakSite","DimonaSite","EilatSite","EladSite","GivatayimSite","HaderaSite","HaifaSite","HarishSite","HerzliyaSite","Hod HaSharonSite","HolonSite","JerusalemSite","KarmielSite","Kfar SavaSite","Kiryat AtaSite","Kiryat BialikSite","Kiryat GatSite","Kiryat MalachiSite","Kiryat MotzkinSite","Kiryat OnoSite","Kiryat ShemoneSite","Kiryat YamSite","LodSite","Maale AdumimSite","Maalot TarshihaSite","Migdal HaEmekSite","ModiinSite","NahariyaSite","NazarethSite","Nes ZionaSite","NesherSite","NetanyaSite","NetivotSite","Nof HagalilSite","OfakimSite","Or AkivaSite","Or YehudaSite","Petah TikvaSite","QalansaweSite","RaananaSite","RahatSite","Ramat HasharonSite","Ramat-GanSite","RamlaSite","RehovotSite","Rishon LezionSite","Rosh Ha'ayinSite","SakhninSite","SderotSite","ShefaramSite","TaibehSite","TamraSite","Tel AvivSite","TiberiasSite","TiraSite","Tirat CarmelSite","Tsfat (Safed)Site","Umm al-FahmSite","YavneSite","Yehud-MonossonSite","YokneamSite"};
        i = rnd.nextInt(siteNames.length-1);
        String siteName = siteNames[i];

        Site newSite = new Site(AddressGenerator(),phoneNumber,name,shippingArea,siteName,true);
        //newSite.insertBase();

        return newSite;
    }


    public static RequestedOrder requestedOrderGenerator(){
        Random rnd = new Random();
        String[] foods = {"Salad","Sandwich","Bread","Steak","Tuna Steak","Fish","Shrimp","Rice","Spaghetti","Pizza","Hamburger","Eggs","Cheese","Sausages","Apple Juice","Grape Juice","Milk","Candy","Cookie","Pie","Cake","Cupcake"};
        int i = rnd.nextInt(foods.length-1);
        String food = foods[i];
        return new RequestedOrder(food,i);
    }

    @Test
    void checkLCArrival () {
        resetDataBase();
        repository.createTables();
        LinkedList<Site> sites= new LinkedList<>();

        Site supplier = new Site(AddressGenerator(),"00000","000000",1,"blabla",false);
       // supplier.insertBase();

        Site branch = new Site(AddressGenerator(),"00000","000000",5,"blabla",true);
       // branch.insertBase();

        sites.add(supplier);
        sites.add(branch);
        LinkedList<Order> orders = new LinkedList<>();
        orders.add(new Order(sites.get(0),sites.get(1),requestedOrderGenerator()));
        LinkedList<Integer> overloads = new LinkedList<>();
        LocalTime depT = LocalTime.of(12,0);
        LocalDate depD = LocalDate.of(2023,12,1);

        Delivery fakeDlivery = new Delivery (depD, depT, "Main Logistic Center",  sites, 1,
        100, "kopans", orders, overloads);

        LocalTime arrival = fakeDlivery.get_arrival_time();
        LocalTime backTOLogistic = fakeDlivery.get_back_to_logistic_center();

        LocalTime expectedArrival = LocalTime.of(17,0);
        LocalTime expectedBackTOLogistic = LocalTime.of(22,0);

        assertEquals(expectedArrival, arrival);
    }

    @Test
    void checkArrival () {
        resetDataBase();
        repository.createTables();
        LinkedList<Site> sites= new LinkedList<>();

        Site supplier = new Site(AddressGenerator(),"00000","000000",1,"blabla",false);
        // supplier.insertBase();

        Site branch = new Site(AddressGenerator(),"00000","000000",5,"blabla",true);
        // branch.insertBase();

        sites.add(supplier);
        sites.add(branch);
        LinkedList<Order> orders = new LinkedList<>();
        orders.add(new Order(sites.get(0),sites.get(1),requestedOrderGenerator()));
        LinkedList<Integer> overloads = new LinkedList<>();
        LocalTime depT = LocalTime.of(12,0);
        LocalDate depD = LocalDate.of(2023,12,1);

        Delivery fakeDlivery = new Delivery (depD, depT, "Main Logistic Center",  sites, 1,
                100, "kopans", orders, overloads);

        LocalTime arrival = fakeDlivery.get_arrival_time();
        LocalTime backTOLogistic = fakeDlivery.get_back_to_logistic_center();

        LocalTime expectedArrival = LocalTime.of(17,0);
        LocalTime expectedBackTOLogistic = LocalTime.of(22,0);


        assertEquals(expectedBackTOLogistic,backTOLogistic);
    }

}