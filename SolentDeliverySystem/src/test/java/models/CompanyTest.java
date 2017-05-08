package models;

import Dao.AccountDao;
import Dao.ClientDao;
import Dao.StaffDao;
import Utils.DbManager;
import models.DbModels.Account;
import models.DbModels.Client;
import models.DbModels.Staff;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by marcin on 01/05/17.
 */
public class CompanyTest {

    private static AccountDao accountDao;
    private static ClientDao clientDao;
    private static StaffDao staffDao;

    @BeforeClass
    public static void beforeClass(){
        accountDao = new AccountDao(DbManager.getConnection());
        clientDao = new ClientDao(DbManager.getConnection());
        staffDao = new StaffDao(DbManager.getConnection());
    }

    /**
     * Checks if getting logged in user works as expected.
     * @throws Exception
     */
    @Test
    public void getLoggedIn() throws Exception {
        List<Account> accounts = accountDao.queryForAll(Account.class);
        Account account = accounts.get(0);
        account.login();
        accountDao.createOrUpdate(account);
        int expected = account.getId();
        int result = Company.getLoggedIn().getId();

        assertEquals(expected, result);
        account.logout();
        accountDao.createOrUpdate(account);
    }

    /**
     * Checks if retrieved logged in client is the one logged in.
     * @throws Exception
     */
    @Test
    public void getLoggedinClient() throws Exception {
        List<Client> clients = clientDao.queryForAll(Client.class);
        Client client = clients.get(0);
        Account clientsAccount = client.getAccount();
        clientsAccount.login();
        accountDao.createOrUpdate(clientsAccount);
        int expected = client.getId();
        int result = Company.getLoggedinClient().getId();
        assertEquals(expected, result);
        clientsAccount.logout();
        accountDao.createOrUpdate(clientsAccount);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getLoggedinStaff() throws Exception {
        List<Staff> staffs = staffDao.queryForAll(Staff.class);
        Staff staff = staffs.get(0);
        Account staffAccount = staff.getAccount();
        staffAccount.login();
        accountDao.createOrUpdate(staffAccount);
        int expected = staff.getId();
        int result = Company.getLoggedinStaff().getId();
        assertEquals(expected, result);
        staffAccount.logout();
        accountDao.createOrUpdate(staffAccount);
    }
}