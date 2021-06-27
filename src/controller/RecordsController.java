/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scheduler.*;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class RecordsController
        implements Initializable {

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Button recordsButton;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button calendarButton;
    
    @FXML
    private Button addNewAppointmentButton;
    
    @FXML
    private Button reportsButton;

    @FXML
    private Label Name;

    @FXML
    private Font x11;

    @FXML
    private Color x21;

    @FXML
    private Button DeleteCustomer;

    @FXML
    private Button viewAppointmentsButton;
    
    @FXML
    private Button addCustomer;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> address1Column;

    @FXML
    private TableColumn<Customer, String> address2Column;

    @FXML
    private TableColumn<Customer, String> cityColumn;

    @FXML
    private TableColumn<Customer, String> postalColumn;

    @FXML
    private TableColumn<Customer, String> countryColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;
    
    @FXML
    private TableView<Customer> customerTable;
    
    private ResultSet countrySet;
    private ResultSet citySet;
    private ResultSet customerSet;
    private ResultSet customerUpdateSet;
    private Integer customerId;

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customerTable.getItems().clear();
        
        // set the table columns objects, and set as editable
        customerNameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCustomerName());
        });
        customerNameColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        customerNameColumn.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    try {
                                        if(t.getNewValue().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Name cannot be blank!");
                                            alert.setContentText("Please fill in name data to continue.");
                                            alert.setGraphic(null);
                                            alert.showAndWait();
                                            customerTable.getItems().clear();
                                            customerTable.getItems().addAll(Customer.customerList); 
                                        }
                                        else {        
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCustomerName(t.getNewValue());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        address1Column.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getAddress1());
        });
        address1Column.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        address1Column.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    try {
                                        if(t.getNewValue().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Address cannot be blank!");
                                            alert.setContentText("Please fill in address data to continue.");
                                            alert.setGraphic(null);
                                            alert.showAndWait();
                                            customerTable.getItems().clear();
                                            customerTable.getItems().addAll(Customer.customerList); 
                                        }
                                        else {        
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress1(t.getNewValue());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        address2Column.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getAddress2());
        });
        address2Column.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        address2Column.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    // no exception control for blank data since address2 is optional
                                    try {  
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress2(t.getNewValue());
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        cityColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getCity());
        });
        cityColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        cityColumn.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    try {
                                        if(t.getNewValue().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("City cannot be blank!");
                                            alert.setContentText("Please fill in city data to continue.");
                                            alert.setGraphic(null);
                                            alert.showAndWait();
                                            customerTable.getItems().clear();
                                            customerTable.getItems().addAll(Customer.customerList); 
                                        }
                                        else {        
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCity(t.getNewValue());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        postalColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getPostal());
        });
        postalColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        postalColumn.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    try {
                                        if(t.getNewValue().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Postal code cannot be blank!");
                                            alert.setContentText("Please fill in postal code data to continue.");
                                            alert.setGraphic(null);
                                            alert.showAndWait();
                                            customerTable.getItems().clear();
                                            customerTable.getItems().addAll(Customer.customerList); 
                                        }
                                        else {        
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPostal(t.getNewValue());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        countryColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getCountry());
        });
        countryColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        countryColumn.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    try {
                                        if(t.getNewValue().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Country cannot be blank!");
                                            alert.setContentText("Please fill in country data to continue.");
                                            alert.setGraphic(null);
                                            alert.showAndWait();
                                            customerTable.getItems().clear();
                                            customerTable.getItems().addAll(Customer.customerList); 
                                        }
                                        else {        
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCountry(t.getNewValue());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        phoneColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getPhone());
        });
        phoneColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        phoneColumn.setOnEditCommit(
                                (CellEditEvent<Customer, String> t) -> {
                                    try {
                                        if(t.getNewValue().isEmpty()) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.setTitle("Error");
                                            alert.setHeaderText("Phone cannot be blank!");
                                            alert.setContentText("Please fill in phone data to continue.");
                                            alert.setGraphic(null);
                                            alert.showAndWait();
                                            customerTable.getItems().clear();
                                            customerTable.getItems().addAll(Customer.customerList); 
                                        }
                                        else {        
                                            ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhone(t.getNewValue());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(RecordsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
        customerTable.getItems().addAll(Customer.customerList);  
        }
    
    @FXML
    private void onActionDisplayRecords(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/Records.fxml"));
        stage.setScene(new Scene(scene));       
    }
    
    @FXML
    private void onActionDisplayAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Appointments.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        AppointmentsController controller = loader.getController(); 
        controller.populateAppointments();
    }
    
    @FXML
    private void onActionDisplayCalendar(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/CalendarMonth.fxml"));
        stage.setScene(new Scene(scene));
    }
    
    @FXML
    private void onActionDisplayReports(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setScene(new Scene(scene));
        
    }
    
    @FXML
    private void onActionDisplayCustomerAppointment(ActionEvent event) throws IOException { 
        Stage stage=(Stage) customerTable.getScene().getWindow();  
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Appointments.fxml"));                        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        AppointmentsController controller = loader.getController();
        controller.receiveSelectedCustomer(customerTable.getSelectionModel().getSelectedItem());    
        controller.populateAppointments();
    }
    
    @FXML
    private void onActionDeleteCustomer(ActionEvent event) throws IOException, SQLException {
        
        // ensure a customer is selected before deleting
        if (customerTable.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer Not Selected!");
            alert.setContentText("Please select a customer to delete.");
            alert.setGraphic(null);
            alert.showAndWait(); 
        }
        else {      
            customerTable.getSelectionModel().getSelectedItem().deleteCustomer();
            customerTable.getItems().clear();
            customerTable.setItems(Customer.customerList);
        }
    }
    
    @FXML
    private void onActionAddCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/NewCustomer.fxml"));
        stage.setScene(new Scene(scene));
    }
    
    @FXML
    private void onActionNewAppointment(ActionEvent event) throws IOException {

        // ensure an appointment is selected before deleting
        if (customerTable.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer Not Selected!");
            alert.setContentText("Please select a customer to create an appointment.");
            alert.setGraphic(null);
            alert.showAndWait(); 
        }

        else {

            Stage stage=(Stage) customerTable.getScene().getWindow();  
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewAppointment.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            NewAppointmentController controller = loader.getController();
            controller.receiveSelectedCustomer(selectedCustomer);
        }
    }
    


}


