/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scheduler.*;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class AppointmentsController
        implements Initializable {
    @FXML
    private Color x2;
    @FXML
    private Font x1;
    @FXML
    private Button recordsButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button calendarButton;
    @FXML
    private Button deleteAppointment;
    @FXML
    private Button editAppointmentsButton;
    @FXML
    private Font x11;
    @FXML
    private Color x21;
    @FXML
    private TableColumn<Appointment, String> customerNameColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndColumn;
    @FXML
    private TableView<Appointment> appointmentTable;

    private Customer customerName;
    
    
    private ResultSet customerSet;
    private ResultSet appointmentSet;
    private Customer selectedCustomer;
    PreparedStatement statement;


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    // method to populate the the table with appointments
    // avided using initialize since the scene/stage is created before I can pass objects to this
    public void populateAppointments() {        
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC);
        
        appointmentTable.getItems().clear();
      
            customerNameColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getCustomerName()); 
            });
            appointmentTypeColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getType()); 
            });
            appointmentStartColumn.setCellValueFactory(cellData -> { 
                return new SimpleObjectProperty(cellData.getValue().getStart().format(format)); 
            });
            appointmentEndColumn.setCellValueFactory(cellData -> {
                return new SimpleObjectProperty(cellData.getValue().getEnd().format(format)); 
            });
            
            appointmentTable.getSortOrder().add(appointmentStartColumn);
            
            // if the object selectedCustomer is null, populate all appointments (for the logged in user)
            if (selectedCustomer == null) { 
                appointmentTable.getItems().addAll(Appointment.appointmentList.filtered(a -> a.getUserId() == User.loggedInUser.get(0).getUserId()));
            }
            
            // otherwise, only show appointments for the customer selected
            else {
            
                ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList(Appointment.appointmentList.filtered(t-> t.getCustomerId() == selectedCustomer.getCustomerId()));
                
                appointmentTable.getItems().addAll(filteredAppointmentList);
            }
    }    

    @FXML
    private void onActionDisplayRecords(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/Records.fxml"));
        stage.setScene(new Scene(scene));
                
    }
    @FXML
    private void onActionDisplayAppointments(ActionEvent event) throws IOException {     
        selectedCustomer = null;
        populateAppointments();
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

    public void receiveSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }
    
    @FXML
    public void onActionDeleteAppointment(ActionEvent event) throws IOException, SQLException {
        
        // ensure a customer is highlighted before attempting to delete it
        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Not Selected!");
            alert.setContentText("Please select an appointment to delete.");
            alert.setGraphic(null);
            alert.showAndWait(); 
        }
        else {
            
            // delete the selected customer, then clear and refresh the table
            appointmentTable.getSelectionModel().getSelectedItem().deleteAppointment();
            populateAppointments();
        }

    }
    
    @FXML
    public void onActionEditAppointment(ActionEvent event) throws IOException, SQLException {
        
        // ensure an appointment is highlighted before attempting to edit it 
        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Not Selected!");
            alert.setContentText("Please select an appointment to edit.");
            alert.setGraphic(null);
            alert.showAndWait(); 
        }
        else {
            Stage stage=(Stage) appointmentTable.getScene().getWindow();  
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointment.fxml"));                        
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
            UpdateAppointmentController controller = loader.getController();
        
        // pass along to UpdateAppointment the selected appointment
        controller.populateAppointment(appointmentTable.getSelectionModel().getSelectedItem());   
        
        }
    }

}
