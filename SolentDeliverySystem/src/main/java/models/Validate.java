package models;

import Dao.AccountDao;
import Utils.DbManager;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class Validate {
    private static final String RED_FIELD = "-fx-background-color: rgba(255, 0, 0, .2); -fx-border-color: rgb(255,0,0);";
    private static final String FIELD_EMPTY = "This field is required.";
    private static final String NOT_EQUAL = "Passwords don\'t match.";
    private static final String NO_NUMBERS = "This field cannot contain digits.";
    private static final String NOT_NUMBERS = "This field can contain digits only.";
    private static final String NEGATIVE_VALUE = "Entered value should be grater than 0.";
    
    // function that checks if provided field has value entered
    public boolean isFieldEmpty(TextField field, Label errorLabel){
        if(field.getText().equals("")){
            setError(field, errorLabel, FIELD_EMPTY);
            return true;
        }
        reset(field, errorLabel);
        return false;
    }
    
    public boolean isOptionSelected(ComboBox box, Label errorLabel){
        if(box.getSelectionModel().getSelectedItem() == null){
            box.setStyle(RED_FIELD);
            errorLabel.setText(FIELD_EMPTY);
            return true;
        }
        box.setStyle(null);
        errorLabel.setText("");
        return false;
    }
    
    public boolean isPassFieldEmpty(PasswordField field, Label errorLabel){
        if(field.getText().equals("")){
            field.setStyle(RED_FIELD);
            errorLabel.setText(FIELD_EMPTY);
            return true;
        }
        reset(field, errorLabel);
        return false;
    }
    
    public boolean comparePasswords(PasswordField pass1, PasswordField pass2, Label errorLabel1, Label errorLabel2){
        if(!pass1.getText().equals("") && !pass2.getText().equals("")){
            if(!pass1.getText().equals(pass2.getText())){
                setError(pass1, errorLabel1, NOT_EQUAL);
                setError(pass2, errorLabel2, NOT_EQUAL);
                return true;
            }
            reset(pass1, errorLabel1);
            reset(pass2, errorLabel2);
            return false;
        }
        return true;
    }
    
//    // checks if characters entered are a type of String (do not contain digits))
//    public boolean isString(TextField field, Label errorLabel)
//    {
//        if(field.getText().matches(".*\\d.*")) {
//            setError(field, errorLabel, NO_NUMBERS);
//            return false;
//        }
//        else {
//            reset(field, errorLabel);
//            return true;
//        }
//    }
    
    // checks if value is a number
    public boolean isNumber(TextField field, Label label){
        try
        {
            reset(field, label);
            Integer.parseInt(field.getText());
            return false;
        }
        catch(NumberFormatException e)
        {
            setError(field, label, NOT_NUMBERS);
            return true;
        }
    }
    
    // cheks if number is positive
    public boolean isPositiveNumber(TextField field, Label label){
        reset(field, label);
        try{
            int value = Integer.parseInt(field.getText());
            if(value <= 0){
                setError(field, label, NEGATIVE_VALUE);
                return true;
            }
            return false;
        }
        catch(NumberFormatException e){
            setError(field, label, NOT_NUMBERS);
            return false;
        }
    }

    public boolean isRadioChecked(ToggleGroup toggleGroup, Label label){
        if(toggleGroup.getSelectedToggle() == null){
            label.setTextFill(Color.RED);
            return true;
        }
        label.setTextFill(Color.BLACK);
        return false;
    }
    
    public void reset(TextField field, Label errorLabel){
        field.setStyle(null);
        errorLabel.setText("");
    }
    
    private void setError(TextField field, Label label, String message)
    {
        field.setStyle(RED_FIELD);
        label.setText(message);
    }

    public boolean isUsernameTaken(TextField textField, Label label){
        reset(textField, label);
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        // if username exists in the system
        if(accountDao.whereUsername(textField.getText())){
            setError(textField, label, "Username taken.");
            System.out.println("username taken");
            return true;
        }
        System.out.println("passed checking");
        return false;
    }
    
    
}
