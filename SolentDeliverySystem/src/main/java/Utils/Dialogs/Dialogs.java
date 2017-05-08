package Utils.Dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import models.Company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Dialogs {

    public static void warning(String content){
        Alert dialog = new Alert(Alert.AlertType.WARNING);
        dialog.setContentText(content);
        dialog.setHeaderText(null);
        dialog.setResizable(true);
        dialog.showAndWait();
    }

    public static void message(String content){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setContentText(content);
        dialog.setHeaderText(null);
        dialog.setResizable(true);
        dialog.showAndWait();
    }

    public static void error(String content){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setContentText(content);
        dialog.setHeaderText(null);
        dialog.setResizable(true);
        dialog.showAndWait();
    }

    public static void exception(String content, Exception ex){
        Company.logoutAll();

        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setHeaderText(null);
        dialog.setResizable(true);
        dialog.setContentText(content);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        dialog.getDialogPane().setExpandableContent(expContent);

        dialog.showAndWait();

    }
}
