package controllers.staff;

import Dao.DeliveryServiceDao;
import Utils.DbManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.DbModels.Client;
import models.DbModels.DeliveryService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying and manipulating
 * selected clients deliveries.
 */
public class ClientDeliveriesController implements Initializable{
    private Client client;

    private ObservableList<DeliveryService> deliveriesObservableList = FXCollections.observableArrayList();
    @FXML private Label clientLabel;
    @FXML private RadioButton todays;
    @FXML private TableView<DeliveryService> clientsDeliveries;
    @FXML private TableColumn<DeliveryService, String> nameSurname, deliveryType, price, datePurchased, expectedDelivery, dateDelivered, cancelled;
    @FXML private ToggleGroup radioBtns;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEventListeners();
    }

    /**
     * Sets passed from differnet controller Client client to local client
     * so we can access it anytime within this controller.
     * @param client Client - passed from other controller
     */
    public void initData(Client client){
        this.client = client;
        clientLabel.setText(client.getAccount().getName() + " " + client.getAccount().getSurname() + " deliveries.");
        todays.setSelected(true);
        initiateTable();
        populateTable();
    }

    /**
     * Fill table with clients delivery services
     */
    private void populateTable() {
        if (radioBtns.getSelectedToggle() != null) {
            RadioButton selectedButton = (RadioButton)radioBtns.getSelectedToggle();
            clientsDeliveries.getItems().clear();
            switch(selectedButton.getId()){
                case "todays":
                    clientsDeliveries.getItems().addAll(getTodaysDeliveries());
                    hideDeliveredDateColumn();
                    showPredictedDeliveryDateColumn();
                    break;
                case "pending":
                    clientsDeliveries.getItems().addAll(getPendingDeliveries());
                    hideDeliveredDateColumn();
                    break;
                case "delivered":
                    clientsDeliveries.getItems().addAll(getDeliveredDeliveries());
                    showDeliveredDateColumn();
                    showPredictedDeliveryDateColumn();
                    break;
            }
        }
    }

    /**
     * Initialize table columns and associate them with objects properties.
     */
    private void initiateTable() {
        nameSurname.setCellValueFactory(new PropertyValueFactory<>("receiverNameSurname"));
        deliveryType.setCellValueFactory(new PropertyValueFactory<>("deliveryTypes"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        datePurchased.setCellValueFactory(new PropertyValueFactory<>("datePurchased"));
        expectedDelivery.setCellValueFactory(new PropertyValueFactory<>("predictedDeliveryDate"));
        dateDelivered.setCellValueFactory(new PropertyValueFactory<>("dateDelivered"));
        cancelled.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    /**
     *  Retrieves all delivered delivery services purchased by the client.
     * @return ObservableList of DeliveryService
     */
    public ObservableList<DeliveryService> getDeliveredDeliveries() {
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> deliveries = deliveryServiceDao.whereDeliveredStatus(true);
        ArrayList<DeliveryService> clientsDeliveredDeliveries = new ArrayList<>();
        deliveries.forEach(delivery->{
            if(delivery.getSeller().getId() == client.getId()){
                clientsDeliveredDeliveries.add(delivery);
            }
        });
        deliveriesObservableList.clear();
        deliveriesObservableList.addAll(clientsDeliveredDeliveries);
        return deliveriesObservableList;
    }

    /**
     *  Retrieves all delivery services purchased today by the client.
     * @return ObservableList of DeliveryService
     */
    public ObservableList<DeliveryService> getTodaysDeliveries(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> todaysDeliveries = deliveryServiceDao.whereToday();
        ArrayList<DeliveryService> clientsTodaysDeliveries = new ArrayList<>();
        todaysDeliveries.forEach(delivery->{
            if(delivery.getSeller().getId() == this.client.getId()){
                clientsTodaysDeliveries.add(delivery);
            }
        });
        deliveriesObservableList.clear();
        deliveriesObservableList.addAll(clientsTodaysDeliveries);
        return deliveriesObservableList;
    }

    /**
     *  Retrieves all pending delivery services purchased by the client.
     * @return ObservableList of DeliveryService
     */
    public ObservableList<DeliveryService> getPendingDeliveries(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> pendingDeliveries = deliveryServiceDao.whereDeliveredStatus(false);
        ArrayList<DeliveryService> clientsPendingDeliveries = new ArrayList<>();
        pendingDeliveries.forEach(delivery->{
            if(delivery.getSeller().getId() == client.getId()){
                clientsPendingDeliveries.add(delivery);
            }
        });
        deliveriesObservableList.clear();
        deliveriesObservableList.addAll(clientsPendingDeliveries);
        return deliveriesObservableList;
    }

    //<editor-fold desc="Manipulate table"
    private void hideDeliveredDateColumn(){
        if(dateDelivered.isVisible()){
            dateDelivered.setVisible(false);
        }
    }

    private void showPredictedDeliveryDateColumn(){
        if(!expectedDelivery.isVisible()){
            expectedDelivery.setVisible(true);
        }
    }

    private void hidePredictedDeliveryDateColumn(){
        if(expectedDelivery.isVisible()){
            expectedDelivery.setVisible(false);
        }
    }

    private void showDeliveredDateColumn(){
        if(!dateDelivered.isVisible()){
            dateDelivered.setVisible(true);
        }
    }
    //</editor-fold>

    /**
     * Set event listeners
     */
    private void initializeEventListeners(){
        radioBtns.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                populateTable();
            }
        });
    }


}
