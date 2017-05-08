package Utils.Seeders;

import models.DbModels.DeliveryType;

import java.util.ArrayList;
import java.util.List;

public class DeliveryTypesSeeder {
    public static List<DeliveryType> types(){
        List<DeliveryType> types = new ArrayList<>();

        DeliveryType type = new DeliveryType();
        type.setType("Standard");
        type.setDuration(5);
        type.setDescription("(3-5 working days)");
        type.setBasePrice(3.90);
        types.add(type);

        DeliveryType type2 = new DeliveryType();
        type2.setType("24+");
        type2.setDuration(3);
        type2.setDescription("(1-3 working days)");
        type2.setBasePrice(4.90);
        types.add(type2);

        DeliveryType type3 = new DeliveryType();
        type3.setType("Express");
        type3.setDuration(1);
        type3.setDescription("(next working day guaranteed)");
        type3.setBasePrice(5.90);
        types.add(type3);

        return types;


    }
}
