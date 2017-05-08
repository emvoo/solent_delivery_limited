package controllers;

import Dao.AccountDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.*;
import models.DbModels.Account;

import java.io.IOException;

public class LoginController {
    private static final String WRONG_CREDENTIALS = "Wrong username/password.";

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label usernameErrorField, passwordErrorField, loginErrorField;

    /**
     *  Exit application
     */
    @FXML private void cancelBtnAction(ActionEvent event){
        System.exit(0);
    }

    /**
     * Attepmt to login user
     * @param event
     */
    @FXML private void loginBtnAction(ActionEvent event){
        if(validation()){
            Account account = login();
            if(account != null){
                ((Node)event.getSource()).getScene().getWindow().hide();
                displayHomeScreen(account);
                clearFields();
            }else
                loginErrorField.setText(WRONG_CREDENTIALS);
        }
    }

    private void displayHomeScreen(Account account){
        String view = "";
        switch(account.getAccountType().getAccountType()){
            case "client":
                view = "/views/client/homeClient.fxml";
                break;
            case "staff":
                view = "/views/staff/homeStaff.fxml";
                break;
        }
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(view));
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        Scene scene = new Scene(root);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Goes through all subscribed users and compares entered credentials.
     * @return Account
     */
    public Account login(){
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        Account account = accountDao.login(usernameField.getText(), passwordField.getText());
        account.login();
        accountDao.createOrUpdate(account);
        return account;
    }

    /**
     * If user not subscribed display window to subscribe user to the service
     * @param event
     */
    @FXML private void registerAction(MouseEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/register.fxml"));
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        Scene scene = new Scene(root);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Validates user input.
     * @return
     */
    private boolean validation() {
        // instantiate validation class
        Validate v = new Validate();
        // falg to indicate state of validation
        boolean validate = true;
        // check username field
        if (v.isFieldEmpty(usernameField, usernameErrorField)) {
            validate = false;
        }
        // check password field
        if (v.isPassFieldEmpty(passwordField, passwordErrorField)) {
            validate = false;
        }
        // return state of the flag
        return validate;
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameErrorField.setText("");
        passwordErrorField.setText("");
        loginErrorField.setText("");
    }
}
