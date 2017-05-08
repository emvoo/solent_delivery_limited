package controllers.staff;

import Dao.AccountDao;
import Dao.AddressDao;
import Dao.ClientDao;
import Utils.DbManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import models.DbModels.Account;
import models.DbModels.Address;
import models.DbModels.Client;

/**
 * Controller responsible for updating client's details.
 */
public class EditClientController {
    private Client client;
    @FXML private TextField name, surname, houseNo, street, postcode, city, password;

    /**
     *
     * populates fields with data based on passed client from different controller.
     *
     * @param client Client - passed from different controller
     */
    public void initData(Client client){
        this.client = client;
        populateFields();
    }

    /**
     * Fill text fields with data
     */
    private void populateFields() {
        name.setText(client.getAccount().getName());
        surname.setText(client.getAccount().getSurname());
        houseNo.setText(String.valueOf(client.getAccount().getAddress().getHouseNo()));
        street.setText(client.getAccount().getAddress().getStreetName());
        postcode.setText(client.getAccount().getAddress().getPostCode());
        city.setText(client.getAccount().getAddress().getTown());
    }

    /**
     * Closes this window
     * @param event
     */
    @FXML private void cancelAction(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    /**
     *  Updates clients details including 3 database columns associated with client himself.
     * @param event
     */
    @FXML private void saveClientAction(ActionEvent event){
        // get Dao's
        ClientDao clientDao = new ClientDao(DbManager.getConnection());
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        AddressDao addressDao = new AddressDao(DbManager.getConnection());

        // get address and account
        Account account = client.getAccount();
        Address address = client.getAccount().getAddress();

        // set new addresses
        address.setHouseNo(Integer.parseInt(houseNo.getText()));
        address.setStreetName(street.getText());
        address.setPostCode(postcode.getText());
        address.setTown(city.getText());
        if(!password.getText().equals(""))
            account.setPassword(password.getText());
        // update address
        addressDao.createOrUpdate(address);
        // update account
        accountDao.createOrUpdate(account);
        clientDao.createOrUpdate(client);
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}
