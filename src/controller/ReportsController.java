/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scheduler.*;
import static scheduler.Appointment.appointmentList;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class ReportsController
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
    private Button reportsButton;
    @FXML
    private Label appointmentTypesLabel;
    @FXML
    private Label appointmentsNextMonthLabel;
    @FXML
    private Color x21;
    @FXML
    private Font x11;
    @FXML
    private TableColumn<Appointment, String> customerNameColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentUser;
    @FXML
    private TableView<Appointment> reportsTable;
    
    // create a list that will contain all the types of appointments 
    private ObservableList<String> appointmentType = FXCollections.observableArrayList();
    
    int types;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        reportsTable.getItems().clear();
      
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
        appointmentUser.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getUserName());
        });

        reportsTable.getSortOrder().add(appointmentUser);

        reportsTable.getItems().addAll(appointmentList);
        
        LocalDate date = LocalDate.now();
        int todayMonth = date.getMonthValue();
        int currentMonth = date.getMonthValue();
        
        
        // filter all appointments that match the month
        // a lambda expression is used to to allow for iterations of the list to make clearer in code the purpose of the iteration
        // it also allows for a simple 1 line statement rather than for/foreach loops
        // this is also easier than creating a class to handle it
        
        ObservableList<Appointment> filteredAppointmentList = Appointment.appointmentList.filtered(a -> a.getStart().getMonthValue() == currentMonth);
        
        filteredAppointmentList.forEach(a -> {
            if (appointmentType.contains(a.getType())) {
            }
            else {
                appointmentType.add(a.getType());
                types++;
            }          
        });
        appointmentTypesLabel.setText("There are "+ types +" types of appointments scheduled this month: ");
        appointmentsNextMonthLabel.setText("There are "+ filteredAppointmentList.size() +" appointments scheduled this month: ");

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
    
}
