package controllers.staff;

import Utils.Dialogs.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import models.Company;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for left menu user actions.
 */
public class HomeStaffController implements Initializable{
    @FXML private AnchorPane rightPanelAnchorPane;
    @FXML private ToggleGroup menuBtns;
    @FXML private ToggleButton viewStaffBtn,viewClientsBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeAction();

        if(Company.getLoggedinStaff().isAdmin()){
            viewClientsBtn.setVisible(true);
            viewStaffBtn.setVisible(true);
        }else{
            viewClientsBtn.setVisible(false);
            viewStaffBtn.setVisible(false);
        }
    }

    @FXML private void homeAction() {
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/staff/rightPanel.fxml"));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)fxmlLoader.load();
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        rightPanelAnchorPane.getChildren().add(anchPane);
    }

    @FXML private void viewDeliveriesAction(){
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        String view = "/views/staff/getAllDeliveries.fxml";
        if(!Company.getLoggedinStaff().isAdmin()){
            view = "/views/staff/getStaffDeliveries.fxml";
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)loader.load();
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        rightPanelAnchorPane.getChildren().add(anchPane);
    }

    @FXML private void viewClientsAction() {
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/staff/viewClients.fxml"));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)fxmlLoader.load();
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        rightPanelAnchorPane.getChildren().add(anchPane);
    }

    @FXML private void viewStaffAction() {
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/staff/viewStaff.fxml"));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)fxmlLoader.load();
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        rightPanelAnchorPane.getChildren().add(anchPane);
    }

    @FXML private void logout(ActionEvent event){
        Company.logout(event);
    }

    public void clearLeftMenuButtons(){
        menuBtns.getToggles().forEach(e->{
            if(e.isSelected()){
                e.setSelected(false);
            }
        });
    }
}
