/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBQuery;

/**
 *
 * @author ian
 */
public class Appointment {
    
    // create a list that will contain all the appointments 
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    static ResultSet appointmentSet;
    
        final private int appointmentId;
        private int customerId;
        private int userId;
        private String userName;
        private String customerName;
        private String type;
        private ZonedDateTime start;
        private ZonedDateTime end;
        
        // constructor
        public Appointment(int appointmentId, int customerId, String customerName, int userId, String userName, String type, ZonedDateTime start, ZonedDateTime end) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.appointmentId = appointmentId;
            this.customerName = customerName;
            this.customerId = customerId;
            this.userId = userId;
            this.userName = userName;
            this.type = type;
            this.start = start;
            this.end = end;            
            Appointment.appointmentList.add(this);
        }
        
        public int getAppointmentId() {
            return appointmentId;
        }
        public int getCustomerId() {
            return customerId;
        }
        public String getCustomerName() {
            return customerName;
        }
        public int getUserId() {
            return userId;
        }
        public String getUserName() {
            return userName;
        }
        public String getType() {
            return type;
        }
        public ZonedDateTime getStart() {
            return start;
        }
        public ZonedDateTime getEnd() {
            return end;
        }
        
        // create methods that update both the appointmentList and the database
        public void setAppointmentType(String type) throws SQLException {
            this.type = type;
        }
        public void setAppointmentStart(ZonedDateTime start) throws SQLException {
            this.start = start;
        }
        public void setAppointmentEnd(ZonedDateTime end) throws SQLException {
            this.end = end;
        }
        public void addAppointment(Appointment appointment) throws SQLException {
            Appointment.appointmentList.add(appointment);
        }
        public void deleteAppointment() throws SQLException {
            appointmentList.remove(this);
            DBQuery.deleteAppointment(this);
        }
}
        
   
