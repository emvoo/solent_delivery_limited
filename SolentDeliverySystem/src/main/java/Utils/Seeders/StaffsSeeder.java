package Utils.Seeders;

import models.DbModels.Account;
import models.DbModels.Staff;

import java.util.ArrayList;
import java.util.List;

public class StaffsSeeder {
    public static List<Staff> staffs(List<Account> accounts){
        List<Staff> staffs = new ArrayList<>();

        accounts.forEach(e->{
            if(e.getId() > 5){
                Staff staff = new Staff();
                if(e.getId() == 6){
                    staff.setAdmin(true);
                }
                staff.setConfirmed(true);
                staff.setAccount(e);
                staffs.add(staff);
            }
        });
        return staffs;
    }
}
