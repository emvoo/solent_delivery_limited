package main;

import Utils.DbSeeder;
import Utils.Dialogs.Dialogs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SolentDeliverySystem extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
        } catch (IOException|NullPointerException e) {
            Dialogs.exception("Resource not found.", e);
        }
        Scene scene = new Scene(root);
        stage.setTitle("Solent Delivery Limited");
        stage.setScene(scene);
        stage.show();
        primaryStage = stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DbSeeder.seed();
        launch(args);
    }
}
