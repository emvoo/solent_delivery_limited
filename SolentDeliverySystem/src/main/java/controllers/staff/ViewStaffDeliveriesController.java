package controllers.staff;

import Dao.DeliveryJobDao;
import Utils.DbManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.DbModels.DeliveryJob;
import models.DbModels.Staff;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for logging users of the system to the system.
 */
public class ViewStaffDeliveriesController implements Initializable{
    private Staff staff;

    @FXML private Label staffLabel;
    @FXML private ToggleGroup radioBtns;
    @FXML private RadioButton inProgress;
    @FXML private TableView<DeliveryJob> staffDeliveriesTable;
    @FXML private TableColumn<DeliveryJob, Integer> id, clientID;
    @FXML private TableColumn<DeliveryJob, String> deliveryAreaPostCode, streetFrom, postCodeFrom, streetTo, postCodeTo, expectedDate, deliveredDate;

    /**
     *  Assign passed staff object to local Staff object
     *  so we can access it within this controller later.
     * @param staff Staff - passed from different controller.
     */
    public void initData(Staff staff){
        this.staff = staff;
        staffLabel.setText(staff.getAccount().getName() + "'s deliveries.");
        populateTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initiateTable();
        inProgress.setSelected(true);
        initializeEventListener();
    }

    /**
     * Sets event listener on radio buttons.
     */
    private void initializeEventListener() {
        radioBtns.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                populateTable();
            }
        });
    }

    /**
     *  Populate table.
     */
    private void populateTable(){
        if(radioBtns.getSelectedToggle() != null){
            staffDeliveriesTable.getItems().clear();
            RadioButton selected = (RadioButton) radioBtns.getSelectedToggle();
            switch(selected.getId()){
                case "inProgress":
                    staffDeliveriesTable.getItems().addAll(inprogressDeliveryJobs());
                    hideDeliveredDate();
                    break;
                case "completed":
                    staffDeliveriesTable.getItems().addAll(completedDeliveryJobs());
                    showDeliveredDate();
                    break;
            }
        }
    }

    /**
     *  Retrieve list of in progress delivery jobs.
     * @return List<DeliveryJob> - list of delivery jobs.
     */
    private List<DeliveryJob> inprogressDeliveryJobs(){
        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        List<DeliveryJob> jobs = deliveryJobDao.whereDriver(staff.getId());
        List<DeliveryJob> inprogressJobs = new ArrayList<>();
        jobs.forEach(job->{
            if(!job.isCompleted()){
                inprogressJobs.add(job);
            }
        });
        return inprogressJobs;
    }

    /**
     *  Returns list of completed delivery jobs.
     *
     * @return List<DeliveryJob> - list of completed delivery jobs
     */
    private List<DeliveryJob> completedDeliveryJobs(){
        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        List<DeliveryJob> jobs = deliveryJobDao.whereDriver(staff.getId());
        List<DeliveryJob> completedJobs = new ArrayList<>();
        jobs.forEach(job->{
            if(job.isCompleted()){
                completedJobs.add(job);
            }
        });
        return completedJobs;
    }

    /**
     * Associate Table columns with tables object properties.
     */
    private void initiateTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        deliveryAreaPostCode.setCellValueFactory(new PropertyValueFactory<>("deliveryAreaPostCode"));
        clientID.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        streetFrom.setCellValueFactory(new PropertyValueFactory<>("streetFrom"));
        postCodeFrom.setCellValueFactory(new PropertyValueFactory<>("postCodeFrom"));
        streetTo.setCellValueFactory(new PropertyValueFactory<>("streetTo"));
        postCodeTo.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        expectedDate.setCellValueFactory(new PropertyValueFactory<>("expectedDate"));
        deliveredDate.setCellValueFactory(new PropertyValueFactory<>("deliveredDate"));
    }

    private void hideDeliveredDate(){
        if(deliveredDate.isVisible())
            deliveredDate.setVisible(false);
    }

    private void showDeliveredDate(){
        if(!deliveredDate.isVisible())
            deliveredDate.setVisible(true);
    }

}
