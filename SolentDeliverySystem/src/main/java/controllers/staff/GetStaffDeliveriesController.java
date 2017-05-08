package controllers.staff;

import Dao.DeliveryJobDao;
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
import models.DbModels.DeliveryJob;
import models.DbModels.DeliveryService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying todays driver deliveries.
 */
public class GetStaffDeliveriesController implements Initializable {

    private boolean isInitialized = false;

    private ObservableList<DeliveryJob> deliveryServicesObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<DeliveryJob> deliveriesTable;
    @FXML private TableColumn<DeliveryJob, String> receiverName,deliveryType,postCode,datePredicted,dateDelivered;
    @FXML private ToggleGroup deliveriesGroup;
    @FXML private RadioButton pending;
    @FXML private Button setDelivered;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pending.setSelected(true);
        if(!isInitialized){
            initiateTable();
            populateTable();
        }
        initializeEventListener();
        populateTable();
    }

    /**
     * Associate table columns with tables object properties.
     */
    public void initiateTable(){
        receiverName.setCellValueFactory(new PropertyValueFactory<>("receiverNameSurname"));
        deliveryType.setCellValueFactory(new PropertyValueFactory<>("deliveryType"));
        postCode.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        datePredicted.setCellValueFactory(new PropertyValueFactory<>("expectedDate"));
        dateDelivered.setCellValueFactory(new PropertyValueFactory<>("deliveredDate"));
        isInitialized = true;
    }

    /**
     * Based on user selection display table content.
     */
    public void populateTable(){
        deliveriesTable.getItems().clear();
        String selected = getSelectedRadioId();
        if(selected != null){
            switch(selected){
                case "pending":
                    deliveriesTable.getItems().addAll(getPendingDeliveries());
                    hideDeliveredDateColumn();
                    showPredictedDeliveryDateColumn();
                    showSetDeliveredButton();
                    break;
                case "delivered":
                    deliveriesTable.getItems().addAll(getDeliveredDeliveries());
                    hideSetDeliveredButton();
                    showPredictedDeliveryDateColumn();
                    showDeliveredDateColumn();
                    break;
            }
        }
    }

    /**
     *
     * @return id - id of selected Radio button
     */
    public String getSelectedRadioId(){
        RadioButton selected = (RadioButton) deliveriesGroup.getSelectedToggle();
        return selected.getId();
    }

    /**
     *  Return purchased today deliveries.
     * @return ObservableList<DeliveryJob>
     */
    public ObservableList<DeliveryJob> getPendingDeliveries(){

        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        List<DeliveryJob> todaysDeliveries = deliveryJobDao.whereDriverAndToday(Company.getLoggedinStaff().getId());
        deliveryServicesObservableList.clear();
        deliveryServicesObservableList.addAll(todaysDeliveries);
        return deliveryServicesObservableList;
    }

    /**
     *  Return all delivered deliveries.
     * @return ObservableList<DeliveryJob>
     */
    public ObservableList<DeliveryJob> getDeliveredDeliveries() {
        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        List<DeliveryJob> deliveries = deliveryJobDao.whereDriverAndDelivered(Company.getLoggedinStaff().getId());
        deliveryServicesObservableList.clear();
        deliveryServicesObservableList.addAll(deliveries);
        return deliveryServicesObservableList;
    }

    //<editor-fold desc="Manipulate Table columns">
    private void hideDeliveredDateColumn(){
        if(dateDelivered.isVisible())
            dateDelivered.setVisible(false);
    }

    private void showPredictedDeliveryDateColumn(){
        if(!datePredicted.isVisible())
            datePredicted.setVisible(true);
    }

    private void hidePredictedDeliveryDateColumn(){
        if(datePredicted.isVisible())
            datePredicted.setVisible(false);
    }

    private void showDeliveredDateColumn(){
        if(!dateDelivered.isVisible())
            dateDelivered.setVisible(true);
    }

    private void hideSetDeliveredButton(){
        if(setDelivered.isVisible())
            setDelivered.setVisible(false);
    }

    private void showSetDeliveredButton(){
        if(!setDelivered.isVisible())
            setDelivered.setVisible(true);
    }
    /**
     * Sets event listener on radio buttons
     */
    private void initializeEventListener() {
        deliveriesGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                populateTable();
            }
        });
    }

    @FXML private void setDeliveredAction(){
        DeliveryJob delivery = deliveriesTable.getSelectionModel().getSelectedItem();
        if(delivery != null){
            delivery.setCompleted(true);
            DeliveryService ds = delivery.getDeliveryService();
            ds.setDelivered();
            DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
            DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
            deliveryServiceDao.createOrUpdate(ds);
            deliveryJobDao.createOrUpdate(delivery);
            populateTable();
        }else{
            Dialogs.error("Select delivery job.");
        }
    }
}
