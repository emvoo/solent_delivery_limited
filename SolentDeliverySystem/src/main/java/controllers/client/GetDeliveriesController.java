package controllers.client;

import Dao.DeliveryServiceDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Company;
import models.DbModels.DeliveryService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for retrieving and displaying
 * all client's deliveries.
 */
public class GetDeliveriesController implements Initializable {
    private boolean isInitialized = false;

    ObservableList<DeliveryService> deliveryServicesObservableList = FXCollections.observableArrayList();
    @FXML private TableView<DeliveryService> deliveriesTable;
    @FXML private TableColumn<DeliveryService, String> receiverName, deliveryType, datePurchased, datePredicted, dateDelivered;
    @FXML private TableColumn<DeliveryService, Double> price;
    @FXML private ToggleGroup deliveriesGroup;
    @FXML private RadioButton pending;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pending.setSelected(true);
        if(!isInitialized){
            initiateTable();
            hideDeliveredDateColumn();
            showPredictedDeliveryDateColumn();
        }
        populateTable();
        initializeEventListener();

    }

    /**
     * set click event listener on radio buttons
     */
    private void initializeEventListener(){
        deliveriesGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                populateTable();
            }
        });
    }

    /**
     * Fill Table with client's deliveries
     */
    private void populateTable(){
        if (deliveriesGroup.getSelectedToggle() != null) {
            deliveriesTable.getItems().clear();
            RadioButton selectedButton = (RadioButton)deliveriesGroup.getSelectedToggle();
            switch(selectedButton.getId()){
                case "pending":
                    deliveriesTable.getItems().addAll(getPendingDeliveryServicesObservableList());
                    hideDeliveredDateColumn();
                    showPredictedDeliveryDateColumn();
                    break;
                case "delivered":
                    hidePredictedDeliveryDateColumn();
                    showDeliveredDateColumn();
                    deliveriesTable.getItems().addAll(getDeliveredDeliveryServicesObservableList());
                    break;
            }
        }
    }

    /**
     *  Retrieves list of clients pending delivery services
     * @return ObservableList of client's pending delivery services
     */
    public ObservableList<DeliveryService> getPendingDeliveryServicesObservableList(){
        List<DeliveryService> deliveryServices = getDeliveryServices();
        deliveryServicesObservableList.clear();
        for(DeliveryService ds:deliveryServices){
            if(!ds.isDelivered()){
                deliveryServicesObservableList.add(ds);
            }
        }
        return deliveryServicesObservableList;
    }

    /**
     *  Retrieves list of clients delivered delivery services
     * @return ObservableList of client's delivered delivery services
     */
    public ObservableList<DeliveryService> getDeliveredDeliveryServicesObservableList(){
        List<DeliveryService> deliveryServices = getDeliveryServices();
        deliveryServicesObservableList.clear();
        for(DeliveryService ds:deliveryServices){
            if(ds.isDelivered()){
                deliveryServicesObservableList.add(ds);
            }
        }
        return deliveryServicesObservableList;
    }

    /**
     *  Retrive list of all client's delivery services
     * @return List<DeliveryServices>
     */
    private List<DeliveryService> getDeliveryServices(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> deliveryServices = deliveryServiceDao.whereSeller(Company.getLoggedinClient().getId());
        return deliveryServices;
    }


    /**
     * Associate Table columns with object properties
     */
    public void initiateTable(){
        receiverName.setCellValueFactory(new PropertyValueFactory<>("receiverNameSurname"));
        deliveryType.setCellValueFactory(new PropertyValueFactory<>("deliveryTypes"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        datePurchased.setCellValueFactory(new PropertyValueFactory<>("datePurchased"));
        datePredicted.setCellValueFactory(new PropertyValueFactory<>("predictedDeliveryDate"));
        dateDelivered.setCellValueFactory(new PropertyValueFactory<>("dateDelivered"));
        isInitialized = true;
    }

    //<editor-fold desc="Manipulate table columns">

    private void hideDeliveredDateColumn(){
        if(dateDelivered.isVisible()){
            dateDelivered.setVisible(false);
        }
    }

    private void showPredictedDeliveryDateColumn(){
        if(!datePredicted.isVisible()){
            datePredicted.setVisible(true);
        }
    }

    private void hidePredictedDeliveryDateColumn(){
        if(datePredicted.isVisible()){
            datePredicted.setVisible(false);
        }
    }

    private void showDeliveredDateColumn(){
        if(!dateDelivered.isVisible()){
            dateDelivered.setVisible(true);
        }
    }
    //</editor-fold>
}
