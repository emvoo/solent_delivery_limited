package Utils.Seeders;


import models.DbModels.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressesSeeder {

    public static List<Address> addresses(){
        List<Address> addresses = new ArrayList<>();

        Address address = new Address();
        address.setHouseNo(12);
        address.setStreetName("Some street name");
        address.setPostCode("SO12 5PE");
        address.setTown("Southampton");
        addresses.add(address);

        Address address2 = new Address();
        address2.setHouseNo(17);
        address2.setStreetName("Other street name");
        address2.setPostCode("SO12 7PP");
        address2.setTown("Southampton");
        addresses.add(address2);

        Address address3 = new Address();
        address3.setHouseNo(25);
        address3.setStreetName("Some road");
        address3.setPostCode("SO16 7PP");
        address3.setTown("Southampton");
        addresses.add(address3);

        Address address4 = new Address();
        address4.setHouseNo(34);
        address4.setStreetName("Some other road");
        address4.setPostCode("SO17 7PP");
        address4.setTown("Southampton");
        addresses.add(address4);

        Address address5 = new Address();
        address5.setHouseNo(2);
        address5.setStreetName("Other road");
        address5.setPostCode("SO24 7PP");
        address5.setTown("Southampton");
        addresses.add(address5);
        
        Address address6 = new Address();
        address6.setHouseNo(2);
        address6.setStreetName("Other road");
        address6.setPostCode("SO24 7PP");
        address6.setTown("Southampton");
        addresses.add(address6);
        
        Address address7 = new Address();
        address7.setHouseNo(2);
        address7.setStreetName("Other road");
        address7.setPostCode("SO24 7PP");
        address7.setTown("Southampton");
        addresses.add(address7);
        
        Address address8 = new Address();
        address8.setHouseNo(2);
        address8.setStreetName("Other road");
        address8.setPostCode("SO24 7PP");
        address8.setTown("Southampton");
        addresses.add(address8);
        
        return addresses;
    }
}
