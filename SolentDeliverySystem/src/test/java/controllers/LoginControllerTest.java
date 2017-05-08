package controllers;

import Dao.ClientDao;
import Dao.CommonDao;
import Dao.StaffDao;
import Utils.DbManager;
import models.DbModels.Account;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class LoginControllerTest {
    private static ClientDao clientDao;
    private static StaffDao staffDao;

    @BeforeClass
    public static void beforeClass(){
        clientDao = new ClientDao(DbManager.getConnection());
        staffDao = new StaffDao(DbManager.getConnection());
    }

    /**
     *  Method to check if provided login details return expected results.
     */
    @Test
    public void loginCredentialsCheck(){
        Account expexted = clientDao.queryForAll(Account.class).get(0);
        Account result = clientDao.login("client", "pass");
        assertEquals(expexted.getId(), result.getId());
    }
}