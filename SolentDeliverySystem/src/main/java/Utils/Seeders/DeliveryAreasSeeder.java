package Utils.Seeders;

import models.DbModels.DeliveryArea;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAreasSeeder {

    public static List<DeliveryArea> deliveryAreas(){
        List<DeliveryArea> areas = new ArrayList<>();


        DeliveryArea area1 = new DeliveryArea();
        area1.setAreaPostCode("SO1");
        area1.setAreaCode(1);
        areas.add(area1);

        DeliveryArea area2 = new DeliveryArea();
        area2.setAreaPostCode("SO2");
        area2.setAreaCode(2);
        areas.add(area2);

        DeliveryArea area3 = new DeliveryArea();
        area3.setAreaPostCode("SO3");
        area3.setAreaCode(3);
        areas.add(area3);

        DeliveryArea area4 = new DeliveryArea();
        area4.setAreaPostCode("SO4");
        area4.setAreaCode(4);
        areas.add(area4);

        DeliveryArea area5 = new DeliveryArea();
        area5.setAreaPostCode("SO5");
        area5.setAreaCode(5);
        areas.add(area5);

        return areas;
    }
}
