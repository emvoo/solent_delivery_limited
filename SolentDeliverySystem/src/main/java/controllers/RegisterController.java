package controllers;

import Dao.ClientDao;
import Dao.StaffDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.SolentDeliverySystem;
import models.*;
import models.DbModels.Account;
import models.DbModels.Address;
import models.DbModels.Client;
import models.DbModels.Staff;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class responsible for registering new accounts to the system.
 */
public class RegisterController implements Initializable{

    @FXML private TextField usernameField, nameField, surnameField, houseNoField, streetNameField, postCodeField, cityField;
    @FXML private PasswordField passwordField, repasswordField;
    @FXML private ComboBox<String> accountTypeField;
    @FXML private Label usernameErrorField, accountTypeErrorField, passwordErrorField, repasswordErrorField, nameErrorField, surnameErrorField, houseNoErrorField, streetNameErrorField, postCodeErrorField, cityErrorField, loginErrorField;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountTypeField.getItems().addAll("Client", "Staff");
        accountTypeField.setStyle("-fx-font: 24px \"System\";");
    }

    /**
     * Adds client/staff based on user input.
     * @param event
     */
    @FXML public void registerBtnAction(ActionEvent event){
        if(validation()){
            String accountType = accountTypeField.getSelectionModel().getSelectedItem();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String houseNo = houseNoField.getText();
            String streetName = streetNameField.getText();
            String postCode = postCodeField.getText();
            String city = cityField.getText();
            switch(accountType){
                case "Client":
                    // add client
                    this.addClient(houseNo, streetName, postCode, username, name, surname, password, city);
                    Dialogs.message("Account created. You can login now.");
                    break;
                case "Staff":
                    // add staff
                    this.addStaff(houseNo, streetName, postCode, username, name, surname, password, city);
                    Dialogs.message("Account created. You have to wait for admin to approve your account before you can login.");
                    break;
                default:
                    break;
            }
            ((Node)event.getSource()).getScene().getWindow().hide();
            SolentDeliverySystem.primaryStage.show();
        }
    }

    /**
     * Closes register user window.
     * @param event
     */
    @FXML public void cancelBtnAction(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
        SolentDeliverySystem.primaryStage.show();
    }

    /**
     * Validates user input.
     * @return
     */
    private boolean validation(){
        Validate v = new Validate();
        boolean validate = true;
        if(v.isFieldEmpty(usernameField, usernameErrorField)){
            validate = false;
        }else if(v.isUsernameTaken(usernameField, usernameErrorField)){
            validate = false;
        }
        if(v.isOptionSelected(accountTypeField, accountTypeErrorField)){ validate = false; }
        if(v.isFieldEmpty(passwordField, passwordErrorField)){ validate = false; }
        if(v.isFieldEmpty(repasswordField, repasswordErrorField)){ validate = false; }
        if(v.comparePasswords(passwordField, repasswordField, passwordErrorField, repasswordErrorField)){ validate = false; }
        if(v.isFieldEmpty(nameField, nameErrorField)){ validate = false; }
        if(v.isFieldEmpty(surnameField, surnameErrorField)){ validate = false; }
        if(v.isFieldEmpty(houseNoField, houseNoErrorField) || v.isNumber(houseNoField, houseNoErrorField)){ validate = false; }
        if(v.isFieldEmpty(streetNameField, streetNameErrorField)){ validate = false; }
        if(v.isFieldEmpty(postCodeField, postCodeErrorField)){ validate = false; }
        if(v.isFieldEmpty(cityField, cityErrorField)){ validate = false; }
        return validate;
    }

    /**
     * Creates user address based on input.
     * @param houseNo
     * @param street_name
     * @param post_code
     * @param city
     * @return
     */
    private Address createAddress(String houseNo, String street_name, String post_code, String city){
        Address address = new Address();
        int number = Integer.parseInt(houseNo);

        address.setHouseNo(number);
        address.setStreetName(street_name);
        address.setPostCode(post_code);
        address.setTown(city);

        return address;
    }

    /**
     * Creates users account based on user input.
     * @param username
     * @param name
     * @param surname
     * @param password
     * @param address
     * @return
     */
    private Account createAccount(String username, String name, String surname, String password, Address address){
        Account account = new Account();
        account.setUsername(username);
        account.setName(name);
        account.setSurname(surname);
        account.setPassword(password);
        account.setAddress(address);
        return account;
    }

    /**
     * Creates client if user selected client account type.
     * @param account
     * @return
     */
    private Client createClient(Account account){
        Client client = new Client();
        client.setAccount(account);
        return client;
    }

    /**
     * Creates staff if user  selected staff account type.
     * @param account
     * @return
     */
    private Staff createStaff(Account account){
        Staff staff = new Staff();
        staff.setAccount(account);
        return staff;
    }

    /**
     * Saves client in database.
     * @param houseNo
     * @param street_name
     * @param post_code
     * @param username
     * @param name
     * @param surname
     * @param password
     * @param city
     */
    private void addClient(String houseNo, String street_name, String post_code, String username, String name, String surname, String password, String city){
        Address address = createAddress(houseNo, street_name, post_code, city);
        Account account = createAccount(username, name, surname, password, address);
        Client client = createClient(account);
        ClientDao clientDao = new ClientDao(DbManager.getConnection());
        clientDao.createOrUpdate(client);
    }

    /**
     * Saves staff in database.
     * @param houseNo
     * @param street_name
     * @param post_code
     * @param username
     * @param name
     * @param surname
     * @param password
     * @param city
     */
    private void addStaff(String houseNo, String street_name, String post_code, String username, String name, String surname, String password, String city){
        Address address = createAddress(houseNo, street_name, post_code, city);
        Account account = createAccount(username, name, surname, password, address);
        Staff staff = createStaff(account);
        StaffDao staffDao = new StaffDao(DbManager.getConnection());
        staffDao.createOrUpdate(staff);
    }
}
