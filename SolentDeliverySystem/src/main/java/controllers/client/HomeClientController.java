package controllers.client;

import Utils.Dialogs.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import models.Company;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying client's home panel.
 * Handles left menu user actions.
 */
public class HomeClientController implements Initializable{
    @FXML private AnchorPane rightPanelAnchorPane;
    @FXML private ToggleGroup menuBtns;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeAction();
    }

    @FXML public void homeAction(){
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/client/rightPanel.fxml"));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)fxmlLoader.load();
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        rightPanelAnchorPane.getChildren().add(anchPane);
    }

    @FXML public void buyDeliveryServiceAction(){
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/client/buyDeliveryService.fxml"));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)fxmlLoader.load();
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        rightPanelAnchorPane.getChildren().add(anchPane);
    }

    @FXML public void viewDeliveriesAction(){
        clearLeftMenuButtons();
        rightPanelAnchorPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/getDeliveries.fxml"));
        AnchorPane anchPane = null;
        try {
            anchPane = (AnchorPane)loader.load();
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
