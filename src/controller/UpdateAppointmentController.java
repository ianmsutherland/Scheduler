/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scheduler.Appointment;
import static scheduler.User.loggedInUser;
import static utils.DBQuery.*;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class UpdateAppointmentController
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
    private Button addNewAppointmentButton;
    @FXML
    private Label Name;
    @FXML
    private Button Submit;
    @FXML
    private Color x21;
    @FXML
    private Font x11;
    @FXML
    private DatePicker appointmentDateField;
    @FXML
    private ComboBox<String> appointmentTypeField;
    @FXML
    private TextField customerNameField;
    @FXML
    private ComboBox<String> appointmentStartHour;

    @FXML
    private ComboBox<String> appointmentStartMin;

    @FXML
    private ComboBox<String> appointmentEndHour;

    @FXML
    private ComboBox<String> appointmentEndMin;
    
    // create lists for the javafx fields (hours/minutes/appointment types)
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
    private static Appointment selectedAppointment;
    private int customerId;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hours.addAll("05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17");
        minutes.addAll("00", "15", "30", "45");
        appointmentTypes.addAll("Presentation", "Scrum");
        
        appointmentStartHour.setItems(hours);
        appointmentEndHour.setItems(hours);
        appointmentStartMin.setItems(minutes);
        appointmentEndMin.setItems(minutes);
        appointmentTypeField.setItems(appointmentTypes);
        
    }
    
    public void populateAppointment(Appointment appointment) {
        
        // reformat date/time for easier reading in editing
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("HH");
        DateTimeFormatter minFormat = DateTimeFormatter.ofPattern("mm");
        customerNameField.setText(appointment.getCustomerName());
        appointmentTypeField.setValue(appointment.getType());
        
        appointmentDateField.setValue(appointment.getStart().toLocalDate());
        appointmentStartHour.setValue(appointment.getStart().toLocalTime().format(hourFormat));
        appointmentStartMin.setValue(appointment.getStart().toLocalTime().format(minFormat));
        appointmentEndHour.setValue(appointment.getEnd().toLocalTime().format(hourFormat));
        appointmentEndMin.setValue(appointment.getEnd().toLocalTime().format(minFormat));   
        selectedAppointment = appointment;
    }

    @FXML
    private void onActionDisplayRecords(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/Records.fxml"));
        stage.setScene(new Scene((Parent) scene));
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
    private void onActionSubmit(ActionEvent event) throws SQLException, IOException {
        
        // set hours of operation
        int businessOpen = 5;
        int businessClose = 17;
        
        // ensure all data fields are filled out
        if (appointmentTypeField.getValue()== null || appointmentDateField.getValue() == null || appointmentStartHour.getValue() == null || appointmentStartMin.getValue() == null || appointmentEndHour.getValue() == null || appointmentEndMin.getValue() == null) {
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment data is not filled in!");
            alert.setContentText("Please fill in all the appointment data to continue.");
            alert.setGraphic(null);
            alert.showAndWait();
        }     
        
        else {
            String type = appointmentTypeField.getValue();
            LocalDate date = appointmentDateField.getValue();
            int startHour = Integer.parseInt(appointmentStartHour.getValue());
            int startMin = Integer.parseInt(appointmentStartMin.getValue());
            int endHour = Integer.parseInt(appointmentEndHour.getValue());
            int endMin = Integer.parseInt(appointmentEndMin.getValue());

            // verify the appointment is not before or after busienss hours
            if (startHour < businessOpen || startHour > businessClose || endHour < businessOpen || endHour > businessClose) {
                System.out.println("startHour: "+ startHour +", endHour: "+ endHour);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You cannot schedule outside business hours!");
                alert.setContentText("You cannot schedule an appointment between the hours of "+ businessClose +" and "+ businessOpen +". Please schedule another time.");
                alert.setGraphic(null);
                alert.showAndWait();
            }
            else {
                
                // reformat dates/time to prepare for database
                ZonedDateTime appointmentDateStart = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), startHour, startMin).atZone(ZoneId.of("UTC"));
                ZonedDateTime appointmentDateEnd = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), endHour, endMin).atZone(ZoneId.of("UTC"));

                // verify appointment is does not overlap another appointment
                Appointment.appointmentList.forEach(a -> {

                    // if the start of this appointment is before than the end of another but the end is after
                    //  or the end of this appointment is b
                    if (a.getStart().isBefore(appointmentDateEnd) && a.getEnd().isAfter(appointmentDateStart) && (a.getAppointmentId() != selectedAppointment.getAppointmentId())) {
                        if (a.getUserId() == loggedInUser.get(0).getUserId()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("This appointment overlaps with another appointment!");
                            alert.setContentText("This appointment for "+ customerNameField + " overlaps with another appointment for "+ loggedInUser.get(0)+ ". Pleae choose another time.");
                            alert.setGraphic(null);
                            alert.showAndWait();
                        }

                    }
                });

                // add appointment to database
                updateAppointment(selectedAppointment.getAppointmentId(), appointmentTypeField.getValue(), appointmentDateStart, appointmentDateEnd);
                
                // regather all appointments from database, to update appointmentList with appointmentId
                getAllAppointmentsFromDb();

                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Appointments.fxml"));

                Parent root = loader.load();

                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();

                AppointmentsController controller = loader.getController();
                controller.populateAppointments();
            }
        }
        
     
    }
    public void receiveSelectedAppointment(Appointment appointment) {
        
    }
    
}

 