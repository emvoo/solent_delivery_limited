package controllers.staff;

import Dao.DeliveryJobDao;
import Dao.DeliveryServiceDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import com.j256.ormlite.dao.ForeignCollection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Company;
import models.DbModels.DeliveryJob;
import models.DbModels.DeliveryService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for retriving and displaying all deliveries within the system.
 */
public class GetDeliveriesController implements Initializable {
    private boolean isInitialized = false;

    private ObservableList<DeliveryService> deliveryServicesObservableList = FXCollections.observableArrayList();
    @FXML private TableView<DeliveryService> deliveriesTable;
    @FXML private TableColumn<DeliveryService, String> receiverName,deliveryType,datePurchased,datePredicted,dateDelivered,cancelledCol,assigned;
    @FXML private TableColumn<DeliveryService, Double> price;
    @FXML private ToggleGroup deliveriesGroup;
    @FXML private RadioButton todays;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        todays.setSelected(true);
        if(!isInitialized){
            initiateTable();
            populateTable();
        }
        initializeEventListener();
        populateTable();
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

    /**
     * Based on user selection display table content.
     */
    public void populateTable(){
        deliveriesTable.getItems().clear();
        String selected = getSelectedRadioId();
        if(selected != null){
            switch(selected){
                case "todays":
                    deliveriesTable.getItems().addAll(getTodaysDeliveries());
                    hideDeliveredDateColumn();
                    showPredictedDeliveryDateColumn();
                    break;
                case "pending":
                    deliveriesTable.getItems().addAll(getPendingDeliveries());
                    hideDeliveredDateColumn();
                    showPredictedDeliveryDateColumn();
                    break;
                case "delivered":
                    deliveriesTable.getItems().addAll(getDeliveredDeliveries());
                    showPredictedDeliveryDateColumn();
                    showDeliveredDateColumn();
                    break;
                case "cancelled":
                    deliveriesTable.getItems().addAll(getCancelledDeliveries());
            }
        }
    }

    /**
     * Displays window to assign delivery service to driver.
     */
    @FXML private void assignToDriverAction() {
        if( !deliveriesTable.getSelectionModel().isEmpty()){
            DeliveryService delivery = deliveriesTable.getSelectionModel().getSelectedItem();
            if(delivery.isAssigned()){
                // display warning message
                Dialogs.message("Delivery service already assigned.");
            }else if(delivery.isCancelled()){
                // display another warning message
                Dialogs.warning("Delivery service is cancelled therefore cannot be delivered.");
            }
            else{
                RadioButton selectedRadio = (RadioButton) deliveriesGroup.getSelectedToggle();
                if(selectedRadio.getId().equals("pending") || selectedRadio.getId().equals("todays")){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff/assignToDriver.fxml"));
                    Stage stage = new Stage();
                    Scene scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException|NullPointerException e) {
                        Dialogs.exception("Resource not found.", e);
                    }
                    stage.setScene(scene);
                    AssignToDriverController controller = loader.getController();
                    controller.initData(delivery);
                    stage.showAndWait();
                    Company.sendEmail(delivery.getDeliveryAddress().getEmail(), "Your parcel has just been loaded on the van and will be with you shortly.");
                    populateTable();
                }
            }
        }else{
            Dialogs.error("Select delivery service to perform the action.");
        }
    }

    /**
     * Cancel delivery service.
     */
    @FXML private void cancelDeliveryAction(){
        if(!deliveriesTable.getSelectionModel().isEmpty()){
            DeliveryService delivery = deliveriesTable.getSelectionModel().getSelectedItem();
            if(!delivery.isAssigned()){
                delivery.setCancelled(true);
                DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
                deliveryServiceDao.createOrUpdate(delivery);
                populateTable();
                Dialogs.message("Cancelled successfully.");
                Company.sendEmail(delivery.getDeliveryAddress().getEmail(), "Delivery has been cancelled. Contact seller to investigate further.");
            }else
                Dialogs.error("Assigned delivery cannot be cancelled.");

        }else
            Dialogs.error("Select delivery service to perform the action.");
    }

    /**
     * Update delivery service to delivered and delivery job to completed.
     */
    @FXML private void setDeliveredAction(){
        if(!deliveriesTable.getSelectionModel().isEmpty()){
            DeliveryService delivery = deliveriesTable.getSelectionModel().getSelectedItem();
            if(delivery.isAssigned()){
                delivery.setDelivered();
                setDeliveryJobCompleted(delivery);
                DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
                deliveryServiceDao.createOrUpdate(delivery);
                populateTable();
                Company.sendEmail(delivery.getDeliveryAddress().getEmail(), "Your parcel has just been delivered. Thank you.");
                Dialogs.message("Set to delivered succesfully. Email sent to customer.");
            }else{
                Dialogs.error("Cant set delivered before assigning to be delivered.");
            }
        }else{
            Dialogs.error("Select delivery service to perform the action.");
        }
    }

    /**
     * Sets delivery job to completed
     * @param delivery DeliveryService - to get delivery job based
     */

    private void setDeliveryJobCompleted(DeliveryService delivery) {
        ArrayList<DeliveryJob> deliveryJobs = new ArrayList<DeliveryJob>(delivery.getDeliveryJobs());
        DeliveryJob deliveryJob = deliveryJobs.get(0);
        deliveryJob.setCompleted(true);
        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        deliveryJobDao.createOrUpdate(deliveryJob);
    }

    /**
     * Associate table columns with tables object properties.
     */
    public void initiateTable(){
        receiverName.setCellValueFactory(new PropertyValueFactory<>("receiverNameSurname"));
        deliveryType.setCellValueFactory(new PropertyValueFactory<>("deliveryTypes"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        datePurchased.setCellValueFactory(new PropertyValueFactory<>("datePurchased"));
        datePredicted.setCellValueFactory(new PropertyValueFactory<>("predictedDeliveryDate"));
        dateDelivered.setCellValueFactory(new PropertyValueFactory<>("dateDelivered"));
        cancelledCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        assigned.setCellValueFactory(new PropertyValueFactory<>("hasBeenAssigned"));
        isInitialized = true;
    }

    /**
     *
     * @return id - id of selected Radio button
     */
    public String getSelectedRadioId(){
        RadioButton selected = (RadioButton) deliveriesGroup.getSelectedToggle();
        return selected.getId();
    }

    //<editor-fold desc="Manipulate Table columns">
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

    /**
     * retrive all delivery services from database.
     * @return List<DeliveryService>
     */
    private List<DeliveryService> getDeliveryServices(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> deliveryServices = deliveryServiceDao.queryForAll(DeliveryService.class);
        return deliveryServices;
    }

    /**
     *  Return all delivered deliveries.
     * @return ObservableList<DeliveryService>
     */
    public ObservableList<DeliveryService> getDeliveredDeliveries() {
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> deliveries = deliveryServiceDao.whereDeliveredStatus(true);
        deliveryServicesObservableList.clear();
        deliveryServicesObservableList.addAll(deliveries);
        return deliveryServicesObservableList;
    }

    /**
     *  Return purchased today deliveries.
     * @return ObservableList<DeliveryService>
     */
    public ObservableList<DeliveryService> getTodaysDeliveries(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> todaysDeliveries = deliveryServiceDao.whereToday();
        deliveryServicesObservableList.clear();
        deliveryServicesObservableList.addAll(todaysDeliveries);
        return deliveryServicesObservableList;
    }

    /**
     *  Return all pending deliveries.
     * @return ObservableList<DeliveryService>
     */
    public ObservableList<DeliveryService> getPendingDeliveries(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> pendingDeliveries = deliveryServiceDao.whereDeliveredStatus(false);
        deliveryServicesObservableList.clear();
        deliveryServicesObservableList.addAll(pendingDeliveries);
        return deliveryServicesObservableList;
    }

    /**
     *  Return all cancelled deliveries.
     * @return ObservableList<DeliveryService>
     */
    public ObservableList<DeliveryService> getCancelledDeliveries(){
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        List<DeliveryService> cancelledDeliveries = deliveryServiceDao.whereCancelled();
        deliveryServicesObservableList.clear();
        deliveryServicesObservableList.addAll(cancelledDeliveries);
        return deliveryServicesObservableList;
    }
}
