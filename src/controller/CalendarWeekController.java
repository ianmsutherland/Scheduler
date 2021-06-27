/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
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
import scheduler.Appointment;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class CalendarWeekController
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
    private Button viewMonthButton;
    @FXML
    private Color x21;
    @FXML
    private Label noAppointmentsAlert;
    @FXML
    private Font x11;
    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> customerNameColumn;

    @FXML
    private TableColumn<Appointment, LocalDate> appointmentStartColumn;

    @FXML
    private TableColumn<Appointment, LocalDate> appointmentEndColumn;

    @FXML
    private Button previousWeekButton;
    @FXML
    private Button displayReports;
    @FXML
    private Button nextWeekButton;
    
    private static int currentWeek;
    
    // create a list that will hold all the appointments for this week
    ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // get the current month
        LocalDate date = LocalDate.now();
        int todayWeek = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        currentWeek = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        // filter all appointments that match the month
        filteredAppointmentList = Appointment.appointmentList.filtered(t -> t.getStart().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == todayWeek);
        
        if (filteredAppointmentList.isEmpty()) {
            noAppointmentsAlert.setText("There are no appointments for the given week.");
        }
        else {
            noAppointmentsAlert.setText("");
         
            customerNameColumn.setCellValueFactory(cellData -> { 
                return new SimpleStringProperty(cellData.getValue().getCustomerName()); 
            });
            appointmentStartColumn.setCellValueFactory(cellData -> { 
                return new SimpleObjectProperty(cellData.getValue().getStart().format(format)); 
            });
            appointmentEndColumn.setCellValueFactory(cellData -> {
                return new SimpleObjectProperty(cellData.getValue().getEnd().format(format)); 
            });
        
        }
        appointmentTable.getItems().addAll(filteredAppointmentList);
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
    private void onActionPreviousWeek(ActionEvent event) {
        
        if (currentWeek == 1) {
                currentWeek = 52;
            }
            else {
                currentWeek--;
            }
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        filteredAppointmentList = Appointment.appointmentList.filtered(t -> t.getStart().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == currentWeek);
        if (filteredAppointmentList.isEmpty()) {
            noAppointmentsAlert.setText("There are no appointments for the given week.");
        }
        else {
            noAppointmentsAlert.setText("");
 
            customerNameColumn.setCellValueFactory(cellData -> { 
                return new SimpleStringProperty(cellData.getValue().getCustomerName()); 
            });
            appointmentStartColumn.setCellValueFactory(cellData -> { 
                return new SimpleObjectProperty(cellData.getValue().getStart().format(format)); 
            });
            appointmentEndColumn.setCellValueFactory(cellData -> {
                return new SimpleObjectProperty(cellData.getValue().getEnd().format(format)); 
            });
        }
        appointmentTable.getItems().clear();
        appointmentTable.getItems().addAll(filteredAppointmentList);
    }

    @FXML
    private void onActionNextWeek(ActionEvent event) {
        
            if (currentWeek == 52) {
                currentWeek = 1;
            }
            else {
                currentWeek++;
            }
        
        filteredAppointmentList = Appointment.appointmentList.filtered(t -> t.getStart().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == currentWeek);
        if (filteredAppointmentList.isEmpty()) {
            noAppointmentsAlert.setText("There are no appointments for the given week.");
        }
        else {
            noAppointmentsAlert.setText("");        
            customerNameColumn.setCellValueFactory(cellData -> { 
                return new SimpleStringProperty(cellData.getValue().getCustomerName()); 
            });
            appointmentStartColumn.setCellValueFactory(cellData -> { 
                return new SimpleObjectProperty(cellData.getValue().getStart()); 
            });
            appointmentEndColumn.setCellValueFactory(cellData -> {
                return new SimpleObjectProperty(cellData.getValue().getEnd()); 
            });         
        }
        appointmentTable.getItems().clear();
        appointmentTable.getItems().addAll(filteredAppointmentList);
    }
    
    @FXML
    private void onActionDisplayReports(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setScene(new Scene(scene));
    }
    
}
