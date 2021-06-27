/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scheduler.*;

/**
 * FXML Controller class
 *
 * @author ian
 */
public class LoginController
        implements Initializable {
    

    Stage stage;
    Parent scene;

    @FXML
    private Color x2;
    @FXML
    private Font x1;
    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userField;
            
    @FXML
    private Label userLabel;            
    @FXML
    private Label passwordLabel;
    @FXML
    private Label sectionLabel;
    @FXML
    private Label loginErrorControlLabel;    
    
    //  set a public var to define if text needs to be translated
    //  to check only once upon initialize, and not repeatedly
    public boolean localizeText;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        //  At the start of the program, check the user's location.
        //  If it is Spanish (es) or French (fr), utilize the coresponding
        //  properties file to translate
        try {
            
        rb = ResourceBundle.getBundle("locale/Nat", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("fr")) {

                localizeText = true;

                sectionLabel.setText(rb.getString("login"));
                userLabel.setText(rb.getString("user"));
                passwordLabel.setText(rb.getString("password"));
                loginButton.setText(rb.getString("login"));
            }
        } catch (Exception e) {
            System.out.println(e);
                }
    }

    // verify the username and password entered, matches with the list of users
    @FXML
    private void onActionLogin(ActionEvent event) throws IOException {
        
        String enteredPassword = passwordField.getText();
        String enteredUserName = userField.getText();

        
        // create a new list from userList that contains the same username
        FilteredList<User> userNameList = User.userList.filtered(u -> u.getUserName().equals(enteredUserName));
        if (!userNameList.isEmpty()) {

            User user = User.userList.get(userNameList.getSourceIndex(0));
            String storedPassword = user.getPassword();

            // compare the entered data, to data from database
            if (storedPassword.equals(enteredPassword)) {
                
                ZonedDateTime now = ZonedDateTime.now();
                
                    // create text file showing which user logged in and when, assuming user permissions are granted
                    try {
                        System.out.println(System.getProperty("user.dir"));
                        String loginString = "This user "+ user.getUserName() +" logged in at "+ now;
                        byte b[]=loginString.getBytes();
                        FileOutputStream logins = new FileOutputStream("C:\\logins.txt", TRUE);
                        logins.write(b);                    
                        logins.close();        
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }
                
                // for each appointment in the list, check if it is within 15 minutes of now
                Appointment.appointmentList.forEach(a -> {
                if (a.getStart().until(now.toLocalDateTime().adjustInto(a.getStart()), ChronoUnit.MINUTES) > -15 && a.getStart().until(now.toLocalDateTime().adjustInto(a.getStart()), ChronoUnit.MINUTES) < 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Appointment starting soon!");
                    alert.setContentText("You have an appointment with "+ a.getCustomerName() +" that starts in "+ a.getStart().until(now.toLocalDateTime().adjustInto(a.getStart()), ChronoUnit.MINUTES) * -1 +" minutes.");
                    alert.setGraphic(null);
                    alert.showAndWait(); 
                        }
                });
                
                // the user has verified credentials and is put into loggedInUser for tracking
                User.loggedInUser.add(user);
                
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/view/Records.fxml"));
                stage.setScene(new Scene(scene));
            }  
            
            else {
                if (localizeText) {
                    ResourceBundle rb = ResourceBundle.getBundle("locale/Nat", Locale.getDefault());   
                    loginErrorControlLabel.setText(rb.getString("loginfail"));
                }
                else {
                    loginErrorControlLabel.setText("User or password incorrect.");
                }
            }
        }       
        else {
                if (localizeText) {
                    ResourceBundle rb = ResourceBundle.getBundle("locale/Nat", Locale.getDefault());   
                    loginErrorControlLabel.setText(rb.getString("loginfail"));
                }
                else {
                    loginErrorControlLabel.setText("No username found.");
                }
            
        }
        
    
    }

}
