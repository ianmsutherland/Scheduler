/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ian
 */
public class DBConnect {
 
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String server = "//wgudb.ucertify.com:3306/U059BO";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + server;
    
    // database login info
    private static final String dbUser = "U059BO";
    private static final String dbPassword = "53688439590";
    
    // Driver Interface Reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    
    // Connection and Interface Reference
    private static Connection conn;
    
    //  open a connection to the db
    public static Connection openConnection() {
        
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    //  close the connection to the db
    public static void closeConnection() {
        
        try {
            conn.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
