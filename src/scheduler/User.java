/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ian
 */
public class User {
    
    // create a list that will contain all the users
    public static ObservableList<User> userList = FXCollections.observableArrayList();
    
    // create a list that will contain the currently logged in user
    public static ObservableList<User> loggedInUser = FXCollections.observableArrayList();
    
        final private int userId;
        private String userName;
        private String password;
        
        // constructor
        public User(int userId, String userName, String password) {
            this.userId = userId;
            this.userName = userName;
            this.password = password;
            User.userList.add(this);
        }
         
        public int getUserId() {
            return userId;
        }
        public String getUserName() {
            return userName;
        }
        public String getPassword() {
            return password;
        }
        
}
