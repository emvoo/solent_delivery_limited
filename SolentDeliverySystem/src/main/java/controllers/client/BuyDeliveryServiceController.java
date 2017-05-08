package controllers.client;

import Dao.DeliveryServiceDao;
import Dao.DeliveryTypeDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;
import models.DbModels.DeliveryAddress;
import models.DbModels.DeliveryService;
import models.DbModels.DeliveryType;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 *  Controller responsible for purchasing delivery service by client.
 */

public class BuyDeliveryServiceController implements Initializable{
    private String deliveryType;

    @FXML private VBox addressFields;
    @FXML private HBox priceHolder;
    @FXML private TextField lengthField,widthField,depthField,weightField,contentsField,nameField,surnameField,houseNoField,streetNameField,postCodeField,cityField;
    @FXML private Label lengthErrorField,widthErrorField,depthErrorField, weigthErrorField,contentsErrorField,addressComboErrorField,deliveryServiceType,priceField;
    @FXML private ComboBox<DeliveryAddress> existingRecipinets;
    @FXML private ToggleGroup deliveryTypeGroup;

    /**
     *
     *  Initialize method right after calling this controller (class)
     *
     *  In this case fills ComboBox with existing recipients, if any.
     *  Also sets event listener on changing recipient.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Fill CombBox with existing recipients, if any.
        populateExistingRecipinets();

        // initialize event listeners.
        initializeEventListeners();
    }

    /**
     * Makes sure all required data to buy delivery service by client
     * are in correct format and then attempts to purchase delivery service.
     */
    @FXML public void buyDeliveryService(){
        // validate
        if(validate()){
            // retrieve entered data
            int length = Integer.parseInt(lengthField.getText());
            int width = Integer.parseInt(widthField.getText());
            int depth = Integer.parseInt(depthField.getText());
            int weight = Integer.parseInt(weightField.getText());
            String contents = contentsField.getText();
            double price = Double.parseDouble(priceField.getText());
            // get delivery address
            DeliveryAddress deliveryAddress = existingRecipinets.getSelectionModel().getSelectedItem();

            if(displayPaymentWindow()){
                // create and save delivery service
                DeliveryService deliveryService = saveDeliveryService(length, width, depth, weight, contents, price, true, deliveryAddress);
                // send email to receiver
                Company.sendEmail(deliveryAddress.getEmail(), "Delivery service from " + Company.getLoggedIn().getName() + " " + Company.getLoggedIn().getSurname() + " has just been created. Expexted delivery date is: " + deliveryService.getPredictedDeliveryDate());
                // clear all fields after saving
                clearFields();
                // display confirmation dialog or redirect to list of deliveries
                Dialogs.message("Service purchased successfully. Your customer has been notified. Also your customer will be receiving regular updates about progress of this delivery.");
            }
        }
    }

    /**
     * Imitates payment gateway. Once pay button clicked by the user it sets payment status to paid.
     * If cancel button clicked returns paymentstatus as not complete.
     * @return boolean
     */
    private boolean displayPaymentWindow(){
        boolean paid = false;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/payment.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        stage.setScene(scene);
        PaymentController controller = loader.getController();
        stage.showAndWait();
        if(((String)stage.getUserData()).equals("paid"))
            paid = true;
        else if(((String)stage.getUserData()).equals("cancelled"))
            Dialogs.error("Service not purchased. You need pay for the service in order to get it delivered.");
        return paid;
    }



    //<editor-fold desc="Price">

    /**
     * update displayed price when conditions are met.
     */
    @FXML public void updatePrice(){
        if(deliveryTypeGroup.getSelectedToggle() != null)
            setPriceField();
    }

    /**
     * Set priceField value.
     */
    private void setPriceField(){
        DecimalFormat formatter = new DecimalFormat("#0.00");
        String value = formatter.format(calculatePrice(deliveryType));
        priceField.setText(value);
    }

    /**
     * Calculates and returns price based on weight and delivery type
     * chosen by the user.
     * Base price means price up to 10kg's weight will be the same.
     * Anything above is calculated like so basePrice/10*weight.
     *
     * @param deliveryType
     * @return price based on weight and delivery type
     */
    private double calculatePrice(String deliveryType){
        double price, basePrice = 0;
        int weight;
        // set base price based on delivery type
        switch(deliveryType){
            case "standard":
                basePrice = 3.90;
                break;
            case "nxtDay":
                basePrice = 5.50;
                break;
            case "xpress":
                basePrice = 7.90;
                break;
        }
        // set price based on weight
        if(!weightField.getText().equals("")){
            weight = Integer.parseInt(weightField.getText());
            if(weight <= 10)
                price = basePrice;
            else
                price = weight*basePrice/10;
            return price;
        }
        // weight not specified then return 0.
        return 0;
    }

    //</editor-fold>

    //<editor-fold desc="Addresses and Recipients">

    @FXML public void showAddAddressBox(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/client/newDeliveryAddressBox.fxml"));
        } catch (IOException|NullPointerException e) {
            // displayt dialog if file not found
            Dialogs.exception("Resource not found", e);
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add New Address");
        stage.setScene(scene);
        stage.showAndWait();
        populateExistingRecipinets();
        // wait for stage to be closed and get return data
        DeliveryAddress da = (DeliveryAddress) stage.getUserData();
        if(da != null){
            existingRecipinets.getSelectionModel().select(da);
        }
    }

    /**
     * fill ComboBox with existing recipients, if any.
     */
    private void populateExistingRecipinets(){
        existingRecipinets.getItems().clear();
        existingRecipinets.getItems().addAll(Company.getDeliveryAddresses());
    }

    /**
     *  fill address fields based on selected in ComboBox deliveryAddress
     *
     * @param address DeliveryAddress
     */
    private void fillAddressFields(DeliveryAddress address){
        nameField.setText(address.getReceiverName());
        surnameField.setText(address.getReceiverSurname());
        houseNoField.setText(String.valueOf(address.getAddress().getHouseNo()));
        streetNameField.setText(address.getAddress().getStreetName());
        postCodeField.setText(address.getAddress().getPostCode());
        cityField.setText(address.getAddress().getTown());
        addressFields.setVisible(true);
    }

    //</editor-fold>

    //<editor-fold desc="Delivery Service Creation and Saving to Database">

    /**
     * Creates Delivery Service absed on user input
     * @param length
     * @param width
     * @param depth
     * @param weight
     * @param contents
     * @param price
     * @param deliveryAddress
     * @return
     */
    private DeliveryService createDeliveryService(int length, int width, int depth, int weight, String contents, double price, Boolean paid, DeliveryAddress deliveryAddress) {
        // create instance of DeliveryService to save data
        DeliveryService deliveryService = new DeliveryService();
        deliveryService.setLength(length);
        deliveryService.setWidth(width);
        deliveryService.setDepth(depth);
        deliveryService.setWeight(weight);
        deliveryService.setContents(contents);
        deliveryService.setPrice(price);
        deliveryService.setDatePurchased();
        deliveryService.setDeliveryType(getDeliveryType());
        deliveryService.setPredictedDeliveryDate();
        deliveryService.setSeller(Company.getLoggedinClient());
        deliveryService.setDeliveryAddress(deliveryAddress);
        deliveryService.setPaid(paid);
        // return created delivery service
        return deliveryService;
    }

    /**
     * Save Created Delivery Service to Database
     * @param length
     * @param width
     * @param depth
     * @param weight
     * @param contents
     * @param price
     * @param deliveryAddress
     */
    private DeliveryService saveDeliveryService(int length, int width, int depth, int weight, String contents, double price, Boolean paid, DeliveryAddress deliveryAddress){
        // call dao and use it to save newly created delivery service in database
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        DeliveryService deliveryService = createDeliveryService(length, width, depth, weight, contents, price, paid, deliveryAddress);
        deliveryServiceDao.createOrUpdate(deliveryService);
        return deliveryService;
    }

    /**
     *  Depending on delivery type chosen by user return DeliveryType object
     * @return DeliveryType
     */
    private DeliveryType getDeliveryType(){
        DeliveryTypeDao deliveryTypeDao = new DeliveryTypeDao(DbManager.getConnection());
        List<DeliveryType> deliveryTypes = deliveryTypeDao.queryForAll(DeliveryType.class);
        switch(deliveryType){
            case "standard":
                return deliveryTypes.get(0);
            case "nxtDay":
                return  deliveryTypes.get(1);
            case "xpress":
                return  deliveryTypes.get(2);
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="Validation">

    /**
     *  Checks all fields are not empty, in the right format and so on.
     * @return boolean
     */
    private boolean validate(){
        Validate v = new Validate();
        boolean validate = true;
        if(v.isFieldEmpty(lengthField, lengthErrorField) || v.isPositiveNumber(lengthField, lengthErrorField)){
            validate = false;
        }
        if(v.isFieldEmpty(widthField, widthErrorField) || v.isPositiveNumber(widthField, widthErrorField)){
            validate = false;
        }
        if(v.isFieldEmpty(depthField, depthErrorField) || v.isPositiveNumber(depthField, depthErrorField)){
            validate = false;
        }
        if(v.isFieldEmpty(weightField, weigthErrorField) || v.isPositiveNumber(weightField, weigthErrorField)){
            validate = false;
        }
        if(v.isFieldEmpty(contentsField, contentsErrorField)){
            validate = false;
        }
        if(v.isOptionSelected(existingRecipinets, addressComboErrorField)){
            validate = false;
        }
        if(v.isRadioChecked(deliveryTypeGroup, deliveryServiceType)){
            validate = false;
        }
        return validate;
    }
    //</editor-fold>

    //<editor-fold desc="Clearing methods">
    private void clearRadioBtn(){
        ObservableList<Toggle> toggles = deliveryTypeGroup.getToggles();
        toggles.forEach(e->{
            if(e.isSelected()){
                e.setSelected(false);
            }
        });
    }

    private void clearFields(){
        lengthField.clear();
        widthField.clear();
        depthField.clear();
        weightField.clear();
        contentsField.clear();
        priceField.setText("0");
        priceHolder.setVisible(false);
        populateExistingRecipinets();
        clearRadioBtn();
        addressFields.setVisible(false);
    }
    //</editor-fold>


    //<editor-fold desc="Listeners Methods">
    private void initializeEventListeners(){
        initializeExistingRecipientEventListener();
        initializeDeliveryTypeGroupEventListener();
    }

    private void initializeExistingRecipientEventListener(){
        existingRecipinets.valueProperty().addListener(new ChangeListener<DeliveryAddress>() {
            @Override
            public void changed(ObservableValue<? extends DeliveryAddress> observable, DeliveryAddress oldValue, DeliveryAddress newValue) {
                fillAddressFields(newValue);
            }
        });
    }

    private void initializeDeliveryTypeGroupEventListener(){
        deliveryTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldValue, Toggle newValue)
            {
                if(newValue != null){
                    RadioButton radioBtn = (RadioButton) newValue;
                    deliveryType = radioBtn.getId();
                    if(!weightField.getText().equals(""))
                        setPriceField();
                    priceHolder.setVisible(true);
                }
            }
        });
    }
    //</editor-fold>


}
