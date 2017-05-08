package controllers.staff;

import Dao.ClientDao;
import Utils.DbManager;
import Utils.Dialogs.Dialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.DbModels.Client;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller responsible for displaying and manipulating clients.
 */
public class ViewClientsController implements Initializable {
    private ObservableList<Client> clientsObservableList = FXCollections.observableArrayList();
    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, Integer> id;
    @FXML private TableColumn<Client, String> name,surname,street,postCode,city,active;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initiateTable();
        populateTable();
    }

    /**
     * Associate table columns with table objects properties.
     */
    private void initiateTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        street.setCellValueFactory(new PropertyValueFactory<>("street"));
        postCode.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        active.setCellValueFactory(new PropertyValueFactory<>("activeClient"));
    }

    /**
     *  Retrives list of all clients.
     * @return
     */
    private ObservableList<Client> getClients() {
        ClientDao clientDao = new ClientDao(DbManager.getConnection());
        List<Client> clients = clientDao.queryForAll(Client.class);
        clientsObservableList.clear();
        clientsObservableList.addAll(clients);
        return clientsObservableList;
    }

    /**
     * Updates clients details.
     */
    @FXML private void editClientAction() {
        if( !clientsTable.getSelectionModel().isEmpty()){
            Client client = clientsTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff/editClient.fxml"));
            Stage stage = new Stage();
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException|NullPointerException e) {
                Dialogs.exception("Resource not found.", e);
            }
            stage.setScene(scene);
            EditClientController controller = loader.getController();
            controller.initData(client);
            stage.showAndWait();
            populateTable();
        }
        else{
            Dialogs.message("Select a client from the table to perform selected action.");
        }
    }

    /**
     * Populates table with clients
     */
    private void populateTable(){
        clientsTable.getItems().clear();
        clientsTable.getItems().addAll(getClients());
    }

    /**
     * Ban/block/deactivate user
     */
    @FXML private void deactivateClientAction(){
        if( !clientsTable.getSelectionModel().isEmpty()){
            Client client = clientsTable.getSelectionModel().getSelectedItem();
            client.changeActive();
            ClientDao clientDao = new ClientDao(DbManager.getConnection());
            clientDao.createOrUpdate(client);
            populateTable();
        }else{
            Dialogs.message("Select a client from the table to perform selected action.");
        }
    }

    /**
     * Display clients deliveries
     */
    @FXML private void viewClientDeliveriesAction(){
        if( !clientsTable.getSelectionModel().isEmpty()){
            Client client = clientsTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff/clientDeliveries.fxml"));
            Stage stage = new Stage();
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException|NullPointerException e) {
                Dialogs.exception("Resource not found.", e);
            }
            stage.setScene(scene);
            ClientDeliveriesController controller = loader.getController();
            controller.initData(client);
            stage.showAndWait();
            populateTable();

        }
        else{
            Dialogs.message("Select a client from the table to perform selected action.");
        }
    }
}
