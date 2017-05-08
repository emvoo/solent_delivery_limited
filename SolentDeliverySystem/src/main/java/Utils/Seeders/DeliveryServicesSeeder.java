package Utils.Seeders;

import models.DbModels.Client;
import models.DbModels.DeliveryAddress;
import models.DbModels.DeliveryService;
import models.DbModels.DeliveryType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryServicesSeeder {

    public static List<DeliveryService> deliveries(List<Client> clients, List<DeliveryType> types, List<DeliveryAddress> deliveryAddresses) {
        List<DeliveryService> deliveries = new ArrayList<>();

        DeliveryService delivery = new DeliveryService();
        delivery.setLength(30);
        delivery.setWidth(40);
        delivery.setDepth(20);
        delivery.setWeight(4);
        delivery.setContents("electronics");
        delivery.setPrice(3.90);
        delivery.setDatePurchased();
        delivery.setSeller(clients.get(0));
        delivery.setDeliveryType(types.get(0));
        delivery.setDeliveryAddress(deliveryAddresses.get(0));
        delivery.setPredictedDeliveryDate();
        delivery.setPaid(true);
        deliveries.add(delivery);


        DeliveryService delivery2 = new DeliveryService();
        delivery2.setLength(60);
        delivery2.setWidth(35);
        delivery2.setDepth(28);
        delivery2.setWeight(7);
        delivery2.setContents("laptop");
        delivery2.setPrice(5.90);
        delivery2.setDatePurchased();
        delivery2.setDelivered();
        delivery2.setSeller(clients.get(0));
        delivery2.setDeliveryType(types.get(2));
        delivery2.setDeliveryAddress(deliveryAddresses.get(1));
        delivery2.setPredictedDeliveryDate();
        delivery2.setAssigned(true);
        delivery2.setPaid(true);
        deliveries.add(delivery2);

        return deliveries;
    }
}
