package controllers.client;

import Dao.AddressDao;
import Dao.DeliveryAddressDao;
import Utils.DbManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.DbModels.Address;
import models.Company;
import models.DbModels.DeliveryAddress;
import models.Validate;

/**
 * Controller responsible for creating new delivery address
 * based on user input
 */
public class NewDeliveryAddressController {

    @FXML private TextField nameField,surnameField,houseNoField,streetNameField,postCodeField,cityField,emailField;
    @FXML private Label nameErrorField,surnameErrorField,houseNoErrorField,streetNameErrorField,postCodeErrorField,cityErrorField,emailErrorField;
    @FXML private AnchorPane anchPane;

    /**
     *  acreate and add new address to database
     */
    @FXML private void addNewAddress(){
        if(validate()){
            int houseNo = Integer.parseInt(houseNoField.getText());
            String name = nameField.getText();
            String surname = surnameField.getText();
            String streetName = streetNameField.getText();
            String postCode = postCodeField.getText();
            String city = cityField.getText();
            String email = emailField.getText();
            DeliveryAddress deliveryAddress = saveDeliveryAddress(houseNo, name, surname, streetName, postCode, city, email);
            Stage stage = (Stage)anchPane.getScene().getWindow();
            stage.setUserData(deliveryAddress);
            stage.close();
        }

    }

    /**
     * Close adding new address window.
     * @param event
     */
    @FXML private void cancelAddNewAddress(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Validate required fields
     * @return
     */
    private boolean validate(){
        Validate v = new Validate();
        boolean validate = true;
        if(v.isFieldEmpty(nameField, nameErrorField))
            validate = false;
        if(v.isFieldEmpty(surnameField, surnameErrorField))
            validate = false;
        if(v.isFieldEmpty(houseNoField, houseNoErrorField))
            validate = false;
        if(v.isFieldEmpty(streetNameField, streetNameErrorField))
            validate = false;
        if(v.isFieldEmpty(postCodeField, postCodeErrorField))
            validate = false;
        if(v.isFieldEmpty(cityField, cityErrorField))
            validate = false;
        if(v.isFieldEmpty(emailField, emailErrorField))
            validate = false;
        return validate;
    }

    /**
     * Create and return new address
     * @param house_no
     * @param street_name
     * @param post_code
     * @param city
     * @return
     */
    private Address createAddress(int house_no, String street_name, String post_code, String city){
        Address address = new Address();
        address.setHouseNo(house_no);
        address.setStreetName(street_name);
        address.setPostCode(post_code);
        address.setTown(city);
        return address;
    }

    /**
     * Save address to database
     * @param house_no
     * @param street_name
     * @param post_code
     * @param city
     * @return
     */
    private Address saveAddress(int house_no, String street_name, String post_code, String city){
        AddressDao addressDao = new AddressDao(DbManager.getConnection());
        Address address = createAddress(house_no, street_name, post_code, city);
        addressDao.createOrUpdate(address);
        return address;
    }

    /**
     * Create and return delivery address
     * @param house_no
     * @param name
     * @param surname
     * @param street_name
     * @param post_code
     * @param city
     * @return
     */
    private DeliveryAddress createDeliveryAddress(int house_no, String name, String surname, String street_name, String post_code, String city, String email){
        Address address = saveAddress(house_no, street_name, post_code, city);
        // save delivery address
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setReceiverName(name);
        deliveryAddress.setReceiverSurname(surname);
        deliveryAddress.setClient(Company.getLoggedinClient());
        deliveryAddress.setAddress(address);
        deliveryAddress.setEmail(email);
        return deliveryAddress;
    }

    /**
     * Save delivery address in database
     * @param house_no
     * @param name
     * @param surname
     * @param street_name
     * @param post_code
     * @param city
     * @return
     */
    private DeliveryAddress saveDeliveryAddress(int house_no, String name, String surname, String street_name, String post_code, String city, String email){
        DeliveryAddressDao deliveryAddressDao = new DeliveryAddressDao(DbManager.getConnection());
        DeliveryAddress deliveryAddress = createDeliveryAddress(house_no, name, surname, street_name, post_code, city, email);
        deliveryAddressDao.createOrUpdate(deliveryAddress);
        return deliveryAddress;
    }

}
