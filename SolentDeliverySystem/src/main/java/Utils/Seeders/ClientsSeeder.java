package Utils.Seeders;

import models.DbModels.Account;
import models.DbModels.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsSeeder {

    public static List<Client> clients(List<Account> accounts){
        List<Client> clients = new ArrayList<>();
        accounts.forEach(e->{
            if(e.getId() < 6){
                Client client = new Client();
                client.setAccount(e);
                clients.add(client);
            }

        });

        return clients;


    }
}
