package ServiceLayer.Singletones;

import defult.BusinessLayer.DeliverySystem.Structures.RequestedOrder;
import defult.ServiceLayer.Singletones.Generator;
import defult.ServiceLayer.Singletones.Validator;
import org.junit.jupiter.api.Test;


import static defult.Main.repository;
import static defult.Main.resetDataBase;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {


    Validator v = new Validator();
    Generator g = new Generator();

    @Test
    void Good_Sites()
    {

        resetDataBase();
        repository.createTables();
        assertEquals(v.valid_Order(g.generate_Site(g.generate_Address(),false),
                                    g.generate_Site(g.generate_Address(),true),
                                    new RequestedOrder("Pizza",20)),true);
    }

    @Test
    void Wrong_Sites()
    {
        resetDataBase();
        repository.createTables();
        assertEquals(v.valid_Order(null,
                g.generate_Site(g.generate_Address(),false),
                new RequestedOrder("Pizza",20)),false);
    }

    @Test
    void Wrong_req()
    {
        resetDataBase();
        repository.createTables();
        assertEquals(v.valid_Order(null,
                g.generate_Site(g.generate_Address(),false),
               null),false);
    }



}