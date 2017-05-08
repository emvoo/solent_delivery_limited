package models;

import Dao.AccountDao;
import Utils.DbManager;
import Utils.DbSeeder;
import com.j256.ormlite.dao.ForeignCollection;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import main.SolentDeliverySystem;
import models.DbModels.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*

    Application uses sqlite database
    To view contents of database it is highly recommended
    to install Firefox add-on available at the url
    https://addons.mozilla.org/en-GB/firefox/addon/sqlite-manager/

 */

public class Company {


    /*
        Mathods in various places across application
        therefore they're static for easy access.
     */
    public static Account getLoggedIn(){
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        return accountDao.getLoggedin();
    }

    public static Client getLoggedinClient(){
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        ForeignCollection<Client> clients = (ForeignCollection<Client>) getLoggedIn().getClients();
        for (Client client : clients) {
            DbManager.closeConnection();
            return client;
        }
        return null;
    }

    public static Staff getLoggedinStaff(){
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        ForeignCollection<Staff> staffs = (ForeignCollection<Staff>) getLoggedIn().getStaffs();
        for (Staff staff: staffs) {
            DbManager.closeConnection();
            return staff;
        }
        return null;
    }

    public static List<DeliveryAddress> getDeliveryAddresses(){
        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();
        for(DeliveryAddress da:getLoggedinClient().getDeliveryAddresses()){
            deliveryAddresses.add(da);
        }
        return deliveryAddresses;
    }

    public static void logout(ActionEvent event){
        Account account = Company.getLoggedIn();
        account.logout();
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        accountDao.createOrUpdate(account);
        DbManager.closeConnection();
        ((Node)event.getSource()).getScene().getWindow().hide();
        SolentDeliverySystem.primaryStage.show();
    }

    public static String getTodaysDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return sdf.format(c.getTime());
    }

    public static void logoutAll(){
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        List<Account> accounts = accountDao.queryForAll(Account.class);
        accounts.forEach(e->{
            e.logout();
            accountDao.createOrUpdate(e);
        });
    }

    public static void sendEmail(String email, String message){

    }

}
