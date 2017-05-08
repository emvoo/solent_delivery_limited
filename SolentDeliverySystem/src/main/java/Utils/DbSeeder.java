package Utils;


import Dao.*;
import Utils.Seeders.*;
import com.j256.ormlite.support.ConnectionSource;
import models.DbModels.*;

import java.text.ParseException;
import java.util.List;

/**
 * Class responsible for seeding data into existing database.
 */
public class DbSeeder {

    public static void seed() {
        DbManager.initDatabase();
        ConnectionSource connection = DbManager.getConnection();
        MigrateDao migrateDao = new MigrateDao(connection);

        if(!migrateDao.migrated()){
            DbManager.initDatabase();
            AddressDao addressDao = new AddressDao(connection);
            List<Address> addresses = AddressesSeeder.addresses();
            addresses.forEach(e->{
                addressDao.createOrUpdate(e);

            });

            AccountTypeDao accountTypeDao = new AccountTypeDao(connection);
            List<AccountType> accountTypes = AccountTypeSeeder.accountTypes();
            accountTypes.forEach(e->{
                accountTypeDao.createOrUpdate(e);
            });

            AccountDao accountDao = new AccountDao(connection);
            List<Account> accounts = AccountsSeeder.accounts(addresses);
            accounts.forEach(e->{
                accountDao.createOrUpdate(e);
                if(e.getId() < 6){
                    e.setAccountType(accountTypes.get(0));
                }
                else{
                    e.setAccountType(accountTypes.get(1));
                }
                accountDao.createOrUpdate(e);
            });

            ClientDao clientDao = new ClientDao(connection);
            List<Client> clients = ClientsSeeder.clients(accounts);
            clients.forEach(e->{
                clientDao.createOrUpdate(e);
            });

            DeliveryTypeDao typeDao = new DeliveryTypeDao(connection);
            List<DeliveryType> types = DeliveryTypesSeeder.types();
            types.forEach(e->{
                typeDao.createOrUpdate(e);
            });

            StaffDao staffDao = new StaffDao(connection);
            List<Staff> staffs = StaffsSeeder.staffs(accounts);
            staffs.forEach(e->{
                staffDao.createOrUpdate(e);
            });

            DeliveryAreaDao deliveryAreaDao = new DeliveryAreaDao(connection);
            List<DeliveryArea> areas = DeliveryAreasSeeder.deliveryAreas();
            areas.forEach(e->{
                deliveryAreaDao.createOrUpdate(e);
            });

            DeliveryAddressDao deliveryAddressDao = new DeliveryAddressDao(connection);
            List<DeliveryAddress> deliveryAddresses = DeliveryAddressSeeder.deliveryAddresses(addresses, clients);
            deliveryAddresses.forEach(e->{
                deliveryAddressDao.createOrUpdate(e);
            });

            DeliveryServiceDao deliveryDao = new DeliveryServiceDao(connection);
            List<DeliveryService> deliveries = DeliveryServicesSeeder.deliveries(clients, types, deliveryAddresses);
            List<Staff> staffList = staffDao.queryForAll(Staff.class);
            Staff staff = staffList.get(1);
            deliveries.forEach(e->{
                deliveryDao.createOrUpdate(e);
            });

            DeliveryJobDao deliveryJobDao = new DeliveryJobDao(connection);
            List<DeliveryJob> jobs = DeliveryJobSeeder.deliveryJobs(deliveries, staffs, areas);
            jobs.forEach(e->{
                deliveryJobDao.createOrUpdate(e);
            });

            Migrate migrate = new Migrate();
            migrate.setMigrated(true);
            migrateDao.createOrUpdate(migrate);
        }
        DbManager.closeConnection();
    }
}
