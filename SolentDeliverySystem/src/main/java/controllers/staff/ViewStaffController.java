package controllers.staff;

import Dao.StaffDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.DbModels.Staff;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewStaffController implements Initializable{
    private ObservableList<Staff> staffsObservableList = FXCollections.observableArrayList();
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Integer> id;
    @FXML private TableColumn<Staff, String> username,name,surname,street,postCode,city,active;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initiateTable();
        populateTable();
    }

    /**
     * Associate table columns with tables object properties.
     */
    private void initiateTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        street.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        postCode.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        active.setCellValueFactory(new PropertyValueFactory<>("active"));
    }

    /**
     * Get list of staff members.
     * @return
     */
    private ObservableList<Staff> getStaff() {
        StaffDao staffDao = new StaffDao(DbManager.getConnection());
        List<Staff> staffs = staffDao.queryForAll(Staff.class);
        staffsObservableList.clear();
        staffsObservableList.addAll(staffs);
        return staffsObservableList;
    }

    /**
     * View deliveries made by selected driver.
     */
    @FXML private void viewDeliveriesAction() {
        if(!staffTable.getSelectionModel().isEmpty()){
            Staff staff = staffTable.getSelectionModel().getSelectedItem();
            if(!staff.isAdmin()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff/viewStaffDeliveries.fxml"));
                Stage stage = new Stage();
                Scene scene = null;
                try {
                    scene = new Scene(loader.load());
                } catch (IOException|NullPointerException e) {
                    Dialogs.exception("Resource not found.", e);
                }
                stage.setScene(scene);
                ViewStaffDeliveriesController controller = loader.getController();
                controller.initData(staff);
                stage.showAndWait();
                populateTable();
            }
            else{
                Dialogs.message("Admin has no deliveries as he is not a driver.");
            }
        }else{
            Dialogs.error("Select member of staff in order to complete the action.");
        }
    }

    /**
     * Update selected staff member details.
     */
    @FXML private void editStaffAction(){
        if(!staffTable.getSelectionModel().isEmpty()){
            Staff staff = staffTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff/editStaff.fxml"));
            Stage stage = new Stage();
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException|NullPointerException e) {
                Dialogs.exception("Resource not found.", e);
            }
            stage.setScene(scene);
            EditStaffController controller = loader.getController();
            controller.initData(staff);
            stage.showAndWait();
            populateTable();
        }else{
            Dialogs.error("Select member of staff in order to complete the action.");
        }
    }

    /**
     * Block/Ban/Deactivate staff member.
     */
    @FXML private void deactivateAction(){
        if(!staffTable.getSelectionModel().isEmpty()) {
            Staff staff = staffTable.getSelectionModel().getSelectedItem();
            if(staff.isAdmin())
                Dialogs.error("Admin!!! Do not deactivate yourself!!");
            else{
                if(staff.isConfirmed())
                    staff.setConfirmed(false);
                else
                    staff.setConfirmed(true);
                StaffDao staffDao = new StaffDao(DbManager.getConnection());
                staffDao.createOrUpdate(staff);
                populateTable();
            }
        }else{
            Dialogs.error("Select member of staff in order to complete the action.");
        }
    }

    /**
     * Populate table with staff members.
     */
    private void populateTable(){
        staffTable.getItems().clear();
        staffTable.getItems().addAll(getStaff());
    }


}
