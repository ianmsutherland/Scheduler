/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.DBConnect;
import utils.DBQuery;
import static utils.DBQuery.getAllCustomersFromDb;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class NewCustomerController
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
    private Label Name;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TextField customerAddress2Field;
    @FXML
    private TextField customerCityField;
    @FXML
    private TextField customerPostalCodeField;
    @FXML
    private TextField customerCountryField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private Button Submit;
    @FXML
    private TextField customerIdField;
    @FXML
    private Color x21;
    @FXML
    private Font x11;
    @FXML
    private Button onActionClearFields;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void onActionSubmit(ActionEvent event) throws IOException, SQLException {
        
        // ensure all the fields (except address2) are filled in
        if (customerNameField.getText().equals("") || customerAddressField.getText().equals("") ||  customerCityField.getText().equals("") || customerPostalCodeField.getText().equals("") || customerCountryField.getText().equals("") || customerPhoneField.getText().equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer data is not filled in!");
            alert.setContentText("Please fill in all the data to continue.");
            alert.setGraphic(null);
            alert.showAndWait();
        }
        else {
            
            int countryId;
            int cityId;
            int addressId;
            int customerId=0;
            String updateStatement;
            String selectStatement;                
            Connection conn;
            PreparedStatement statement;
            ResultSet rs;

            String customerName = customerNameField.getText();
            String customerAddress = customerAddressField.getText();
            String customerAddress2 = customerAddress2Field.getText();
            String customerCity = customerCityField.getText();
            String customerPostalCode = customerPostalCodeField.getText();
            String customerCountry = customerCountryField.getText();
            String customerPhone = customerPhoneField.getText();

             // first search for country in db, return id, or insert and return id
            selectStatement = "SELECT country, countryId FROM U059BO.country WHERE country = ?";
            conn = DBConnect.openConnection();
            DBQuery.setPreparedStatement(conn,selectStatement);
            statement = DBQuery.getPreparedStatement();
            statement.setString(1, customerCountry);
            statement.execute();
            rs = statement.getResultSet();
            if(rs.first()) {
                countryId = rs.getInt("countryId");
            }
            else {
                countryId = DBQuery.insertCountry(customerCountry);
            }

            // next search for city in db, return id or insert and return id
            selectStatement = "SELECT cityId, city, countryId FROM U059BO.city WHERE city = ? AND countryId = ?";
            conn = DBConnect.openConnection();
            DBQuery.setPreparedStatement(conn,selectStatement);
            statement = DBQuery.getPreparedStatement();
            statement.setString(1, customerCity);
            statement.setInt(2, countryId);
            statement.execute();
            rs = statement.getResultSet();
            if(rs.first()) {
                cityId = rs.getInt("cityId");
            }
            else {
                cityId = DBQuery.insertCity(customerCity, countryId);
            }

            // next search for address in db, return id or insert and return id
            selectStatement = "SELECT addressId, address, address2, cityId, postalCode, phone FROM U059BO.address WHERE address = ? AND address2 = ? AND cityId = ? AND postalCode = ? AND phone = ?";
            conn = DBConnect.openConnection();
            DBQuery.setPreparedStatement(conn,selectStatement);
            statement = DBQuery.getPreparedStatement();
            statement.setString(1, customerAddress);
            statement.setString(2, customerAddress2);
            statement.setInt(3, cityId);
            statement.setString(4, customerPostalCode);
            statement.setString(5, customerPhone);
            statement.execute();
            rs = statement.getResultSet();
            if(rs.first()) {
                addressId = rs.getInt("addressId");
            }
            else {
                addressId = DBQuery.insertAddress(customerAddress, customerAddress2, cityId, customerPostalCode, customerPhone);
            }

            try {


                // next search for the name in db, return id or insert and return id
                selectStatement = "SELECT customerId, customerName, addressId FROM U059BO.customer WHERE customerName = ? AND addressId = ?";
                conn = DBConnect.openConnection();
                DBQuery.setPreparedStatement(conn,selectStatement);
                statement = DBQuery.getPreparedStatement();
                statement.setString(1, customerName);
                statement.setInt(2, addressId);
                statement.execute();
                rs = statement.getResultSet();
                if(rs.first()) {
                    customerId = rs.getInt("customerId");
                }
                else {
                    customerId = DBQuery.insertCustomer(customerName, addressId);
                    getAllCustomersFromDb();
                }
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/Records.fxml"));
            stage.setScene(new Scene((Parent) scene));
        }
    }    
}
