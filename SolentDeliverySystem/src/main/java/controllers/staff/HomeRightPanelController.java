package controllers.staff;


import com.j256.ormlite.dao.ForeignCollection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.Company;
import models.DbModels.Client;
import models.DbModels.DeliveryService;
import models.DbModels.Staff;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying home staff view.
 */
public class HomeRightPanelController implements Initializable {
    @FXML private Label welcome, dateTime, positionMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcome.setText("Welcome " + getNameSurname());
        displayPositionMessage();
        dateTime.setText("Today's date: " + Company.getTodaysDate());
    }

    /**
     * Returns name and surname of logged in
     * @return
     */
    public String getNameSurname(){
        Staff staff = Company.getLoggedinStaff();
        return staff.getAccount().getName() + " " + staff.getAccount().getSurname();
    }

    /**
     * Displays welcome message depending on position held.
     */
    private void displayPositionMessage() {
        Staff staff = Company.getLoggedinStaff();
        if(staff.isAdmin()){
            positionMessage.setText("Hi, Admin");
        }else{
            positionMessage.setText("Hi, Driver");
        }
    }
}
