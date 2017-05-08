package Utils.Seeders;

import models.DbModels.Address;
import models.DbModels.Client;
import models.DbModels.DeliveryAddress;
import models.DbModels.DeliveryArea;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressSeeder {
    public static List<DeliveryAddress> deliveryAddresses(List<Address> addresses, List<Client> clients){
        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setReceiverName("Mickey");
        deliveryAddress.setReceiverSurname("Mouse");
        deliveryAddress.setAddress(addresses.get(5));
        deliveryAddress.setClient(clients.get(3));
        deliveryAddress.setEmail("test@test.pl");
        deliveryAddresses.add(deliveryAddress);

        DeliveryAddress deliveryAddress2 = new DeliveryAddress();
        deliveryAddress2.setAddress(addresses.get(4));
        deliveryAddress2.setReceiverName("Donald");
        deliveryAddress2.setReceiverSurname("Duck");
        deliveryAddress2.setClient(clients.get(4));
        deliveryAddress2.setEmail("test2@test.pl");
        deliveryAddresses.add(deliveryAddress2);
        
        return deliveryAddresses;
    }
}
