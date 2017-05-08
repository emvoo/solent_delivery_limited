package controllers.client;


import com.j256.ormlite.dao.ForeignCollection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.DbModels.Client;
import models.Company;
import models.DbModels.DeliveryService;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying first screen user sees when loggs in
 */
public class HomeRightPanelController implements Initializable {
    @FXML private Label welcome, dateTime, pendingMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pendingMessage.setText("Relax. You have no pending deliveries.");
        displayPendingMessage();
        welcome.setText("Welcome " + getNameSurname());
        dateTime.setText("Today's date: " + Company.getTodaysDate());
    }

    /**
     *
     * @return String username - logged in client's username.
     */
    public String getNameSurname(){
        Client client = Company.getLoggedinClient();
        return client.getAccount().getName() + " " + client.getAccount().getSurname();
    }

    /**
     * Display message about pengind deliveries
     */
    private void displayPendingMessage() {
        ForeignCollection<DeliveryService> deliveries = Company.getLoggedinClient().getDeliveryServices();
        deliveries.forEach(e->{
            if(!e.isDelivered()){
                pendingMessage.setText("You have pending deliveries. You can view details by selecting appropriate option on the left menu.");
            }
        });
    }


}
