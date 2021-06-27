/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnect;
import utils.DBQuery;
 /*
 * @author ian
 */
public class Scheduler extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        // the first page is the login screen
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {

        // gather all information from the database prior to starting application
        DBQuery.getAllCustomersFromDb();
        DBQuery.getAllAppointmentsFromDb();
        DBQuery.getAllUsersFromDb();

        launch(args);
        
        // close the database connection
        DBConnect.closeConnection();
    }
    
}
