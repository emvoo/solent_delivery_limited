package controllers.client;

import Utils.Dialogs.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller responsible for displaying payment gateway.
 */
public class PaymentController {
    @FXML private TextField firstFour, secondFour, thirdFour, fourthFour, expiryMonth, expiryYear, cvvCode, nameOnCard;

    /**
     * Cancelles taking payment
     * @param event
     */
    @FXML private void cancelAction(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setUserData("cancelled");
        stage.hide();
    }

    /**
     *  Imitates to take payment.
     * @param event
     */
    @FXML private void payAction(ActionEvent event){
        if(validation()){
            Dialogs.message("Payment successful.");
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setUserData("paid");
            stage.hide();
        }
    }

    /**
     * validates entered data.
     * @return
     */
    private boolean validation() {
        boolean validate = true;
        if(firstFour.getText().equals("") || secondFour.getText().equals("") || thirdFour.getText().equals("") || fourthFour.getText().equals("") || expiryMonth.getText().equals("") || expiryYear.getText().equals("") || cvvCode.getText().equals("") || nameOnCard.getText().equals("")){
            Dialogs.error("All fields must not be empty.");
            validate = false;
        }
        if(firstFour.getText().length() != 4 || secondFour.getText().length() != 4 || thirdFour.getText().length() != 4 || fourthFour.getText().length() != 4){
            Dialogs.error("Card number must be sections of 4 digits");
            validate = false;
        }
        if(cvvCode.getText().length() != 3){
            Dialogs.error("CVV number should be 3 digits long.");
            validate = false;
        }
        if(expiryMonth.getText().length() != 2 || expiryYear.getText().length() != 2){
            Dialogs.error("Expiry date should be combination of last 2 digits in case of year or month represented by digits.");
            validate = false;
        }
        return validate;
    }
}
