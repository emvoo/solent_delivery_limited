package Utils.Seeders;

import models.DbModels.Account;
import models.DbModels.Address;

import java.util.ArrayList;
import java.util.List;

public class AccountsSeeder {

    public static List<Account> accounts(List<Address> addresses){
        List<Account> accounts = new ArrayList<>();

        Account account = new Account();
        account.setUsername("client");
        account.setName("Name1");
        account.setSurname("Surname1");
        account.setPassword("pass");
        accounts.add(account);
        
        Account account2 = new Account();
        account2.setUsername("client2");
        account2.setName("Name2");
        account2.setSurname("Surname2");
        account2.setPassword("pass");
        accounts.add(account2);
        
        Account account3 = new Account();
        account3.setUsername("client3");
        account3.setName("Name3");
        account3.setSurname("Surname3");
        account3.setPassword("pass");
        accounts.add(account3);
        
        Account account4 = new Account();
        account4.setUsername("client4");
        account4.setName("Name4");
        account4.setSurname("Surname4");
        account4.setPassword("pass");
        accounts.add(account4);
        
        Account account5 = new Account();
        account5.setUsername("client5");
        account5.setName("Name5");
        account5.setSurname("Surname5");
        account5.setPassword("pass");
        accounts.add(account5);
        
        Account account6 = new Account();
        account6.setUsername("admin");
        account6.setName("Name6");
        account6.setSurname("Surname6");
        account6.setPassword("pass");
        accounts.add(account6);
        
        Account account7 = new Account();
        account7.setUsername("driver1");
        account7.setName("Name7");
        account7.setSurname("Surname7");
        account7.setPassword("pass");
        accounts.add(account7);
        
        Account account8 = new Account();
        account8.setUsername("driver2");
        account8.setName("Name8");
        account8.setSurname("Surname8");
        account8.setPassword("pass");
        accounts.add(account8);



        for(int i=0; i < accounts.size(); i++){
            Account acc = accounts.get(i);
            Address add = addresses.get(i);
            acc.setAddress(add);
        }
        return accounts;
    }
}
