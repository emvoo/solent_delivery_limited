package controllers.staff;

import Dao.DeliveryAreaDao;
import Dao.DeliveryJobDao;
import Dao.DeliveryServiceDao;
import Dao.StaffDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import com.j256.ormlite.dao.ForeignCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Company;
import models.DbModels.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller responsible for creating new delivery job
 * and assigning this role to selected driver (staff).
 */
public class AssignToDriverController {
    private DeliveryService delivery;

    @FXML private Label deliveryId;
    @FXML private ComboBox<Staff> drivers;

    public void initData(DeliveryService delivery){
        this.delivery = delivery;
        deliveryId.setText("Delivery #" + delivery.getId());
        populateDrivers();
    }

    /**
     * Close creating new delivery job window.
     * @param event
     */
    @FXML private void cancelAction(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    /**
     *
     * @param event
     */
    @FXML private void assignAction(ActionEvent event){
        // get selected driver from combo box
        if(!drivers.getSelectionModel().isEmpty()){
            // get selected driver (Staff object)
            Staff selected = drivers.getSelectionModel().getSelectedItem();
            assignDeliveryJob(selected);
            Dialogs.message("Done :)");
            ((Node)event.getSource()).getScene().getWindow().hide();
        }else
            Dialogs.warning("Select driver from the list.");
    }

    /**
     * Create delivery job for selected delivery service for selected driver
     * @param selected
     */
    private void createDeliveryJob(Staff selected){
        // get delivery area based on delivery post code
        DeliveryAreaDao deliveryAreaDao = new DeliveryAreaDao(DbManager.getConnection());
        String postCode = delivery.getDeliveryAddress().getAddress().getPostCode().substring(0,3);
        DeliveryArea deliveryArea = deliveryAreaDao.wherePostCode(postCode);
        // create new delivery job
        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        DeliveryJob newDeliveryJob = new DeliveryJob();
        newDeliveryJob.setDeliveryArea(deliveryArea);
        newDeliveryJob.setStaff(selected);
        newDeliveryJob.setDeliveryService(delivery);
        newDeliveryJob.setAssignedOn(Company.getTodaysDate());
        // save delivery job
        deliveryJobDao.createOrUpdate(newDeliveryJob);
    }

    /**
     * Update delivery_service.assigned in database
     */
    private void updateDeliveryServiceToAssigned(){
        delivery.setAssigned(true);
        DeliveryServiceDao deliveryServiceDao = new DeliveryServiceDao(DbManager.getConnection());
        deliveryServiceDao.createOrUpdate(delivery);
    }

    /**
     * Save delivery job to database
     * @param selected
     */
    private void assignDeliveryJob(Staff selected){
        createDeliveryJob(selected);
        updateDeliveryServiceToAssigned();
    }

    /**
     * Method that takes care of displaying drivers for selected delivery service
     * If driver has delivery job set for today already and didnt  reach his daily limit
     * method compares his delivery area with selected delivery service.
     * If different driver is not being displayed
     * If the same driver is being displayed and is the only one to choose from
     * to assign delivery action to.
     */
    private void populateDrivers(){
        StaffDao staffDao = new StaffDao(DbManager.getConnection());
        List<Staff> allButAdmin = staffDao.allButAdmin();
        Set<Staff> driversList = new HashSet<>();
        // once we have list of all drivers
        // check if any of them have deliveries for today
        // for selected not assigned yet delivery post area
        // if thats the case display only drivers that deliver to that area already
        allButAdmin.forEach(driver->{
            // get drivers todays delivery jobs
            ArrayList<DeliveryJob> deliveryJobs = new ArrayList<>(getDriversTodaysDeliveryJobs(driver));
            if(deliveryJobs.size() <= driver.getDeliveryLimit()){
                // for each delivery job check assigned date equals todays date
                deliveryJobs.forEach(deliveryJob->{
                    if(deliveryJob.getAssignedOn().equals(Company.getTodaysDate())){
                        // dates equal so check post codes
                        String newDeliveryPostCode = delivery.getDeliveryAddress().getAddress().getPostCode().substring(0, 3);
                        if(newDeliveryPostCode.equals(deliveryJob.getDeliveryArea().getAreaPostCode())){
                            // postcodes equal than add driver
                            driversList.add(driver);
                        }
                    }
                });
            // if driver reached the limit should not be included in the availability list
            }else{
                allButAdmin.remove(driver);
            }
        });
        // clear combo box
        drivers.getItems().clear();
        // if there are any drivers delivering to this area display list of those drivers in the list
        if(driversList.size() > 0){
            drivers.getItems().addAll(driversList);
        }
        // otherwise display all drivers
        else{
            drivers.getItems().addAll(allButAdmin);
        }

    }

    private List<DeliveryJob> getDriversTodaysDeliveryJobs(Staff driver){
        DeliveryJobDao deliveryJobDao = new DeliveryJobDao(DbManager.getConnection());
        List<DeliveryJob> deliveryJobs = deliveryJobDao.whereDriverAndToday(driver.getId());

        return deliveryJobs;

    }
}
