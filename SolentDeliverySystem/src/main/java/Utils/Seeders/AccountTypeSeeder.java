package Utils.Seeders;


import models.DbModels.AccountType;

import java.util.ArrayList;
import java.util.List;

public class AccountTypeSeeder {
    public static List<AccountType> accountTypes(){
        List<AccountType> accountTypes = new ArrayList<>();

        AccountType accountType = new AccountType();
        accountType.setAccountType("client");
        accountTypes.add(accountType);

        AccountType accountType2 = new AccountType();
        accountType2.setAccountType("staff");
        accountTypes.add(accountType2);

        return accountTypes;
    }
}
