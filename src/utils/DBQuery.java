/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import scheduler.Appointment;
import static scheduler.Appointment.appointmentList;
import scheduler.Customer;
import static scheduler.Customer.customerList;
import scheduler.User;
import static scheduler.User.loggedInUser;
import static scheduler.User.userList;



/**
 *
 * @author ian
 */
public class DBQuery {
    
    private static ResultSet rs;
    private static String updateStatement;
    private static String selectStatement;
    private static String insertStatement;
    private static String deleteStatement;
    private static PreparedStatement statement;
    private static int countryId;
    private static int cityId;
    private static Connection conn = DBConnect.openConnection();
    
    //  Create Statement Object
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        int keys = 0;
        statement = conn.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
    }
    
    //  Return Statement Object
    public static PreparedStatement getPreparedStatement() { 
        return statement;
    }
    
    public static int insertCountry(String country) throws SQLException {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        int lastId = 0;
        insertStatement = "INSERT INTO U059BO.country (country,createDate,createdBy,lastUpdateBy) SELECT ?,?,?,?";
        DBQuery.setPreparedStatement(conn, insertStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, country);
        statement.setTimestamp(2, now);
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setString(4, loggedInUser.get(0).getUserName());
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        lastId = rs.getInt(1);
        return lastId;
    }  
    
    public static int insertCity(String city, int countryId) throws SQLException {
        // set current time for updating database
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        int lastId = 0;
        insertStatement = "INSERT INTO U059BO.city (city,countryId,createDate,createdBy,lastUpdateBy) SELECT ?,?,?,?,?";
        DBQuery.setPreparedStatement(conn, insertStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, city);
        statement.setInt(2, countryId);
        statement.setTimestamp(3, now);
        statement.setString(4, loggedInUser.get(0).getUserName());
        statement.setString(5, loggedInUser.get(0).getUserName());
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        lastId = rs.getInt(1);
        return lastId;
    }
    
    
    public static int insertAddress(String address, String address2, int cityId, String postalCode, String phone) throws SQLException {
        // set current time for updating database
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        int lastId = 0; 
        insertStatement = "INSERT INTO U059BO.address (address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdateBy) SELECT ?,?,?,?,?,?,?,?";
        DBQuery.setPreparedStatement(conn, insertStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, address);
        statement.setString(2, address2);
        statement.setInt(3, cityId);
        statement.setString(4, postalCode);
        statement.setString(5, phone);
        statement.setTimestamp(6, now);
        statement.setString(7, loggedInUser.get(0).getUserName());
        statement.setString(8, loggedInUser.get(0).getUserName());
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        lastId = rs.getInt(1);
        return lastId;
    }
    
    
    public static int insertCustomer(String customerName, int addressId) throws SQLException {
        // set current time for updating database
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        int lastId = 0;
        
        Connection conn = DBConnect.openConnection();
        insertStatement = "INSERT INTO U059BO.customer (customerName,addressId,active,createDate,createdBy,lastUpdateBy) SELECT ?,?,?,?,?,?";
        DBQuery.setPreparedStatement(conn, insertStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, customerName);
        statement.setInt(2, addressId);
        statement.setInt(3, 1);
        statement.setTimestamp(4, now);
        statement.setString(5, loggedInUser.get(0).getUserName());
        statement.setString(6, loggedInUser.get(0).getUserName());
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        lastId = rs.getInt(1);
        return lastId;
    }
    
    public static void insertAppointment(int customerId, String type, ZonedDateTime start, ZonedDateTime end) throws SQLException {
        insertStatement = "INSERT INTO U059BO.appointment (customerId,type,start,end,createDate,createdBy,lastUpdateBy,userId,title,description,contact,url,location) SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?";
        DBQuery.setPreparedStatement(conn, insertStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setInt(1, customerId);
        statement.setString(2, type);
        statement.setTimestamp(3, Timestamp.from(start.toInstant()));
        statement.setTimestamp(4, Timestamp.from(end.toInstant()));
        statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(6, loggedInUser.get(0).getUserName());
        statement.setString(7, loggedInUser.get(0).getUserName());
        statement.setInt(8, loggedInUser.get(0).getUserId());
        statement.setString(9, "not needed");
        statement.setString(10, "not needed");
        statement.setString(11, "not needed");
        statement.setString(12, "not needed");
        statement.setString(13, "not needed");
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
    }
       
    public static void deleteCustomer(Customer customer) throws SQLException {
        deleteStatement = "UPDATE customer Set active = ? WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn,deleteStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setInt(1, 0);
        statement.setInt(2, customer.getCustomerId());
        statement.execute();
    }
    
    public static void updateCustomerName(int customerId, String customerName) throws SQLException {
        updateStatement = "UPDATE customer SET customerName = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn,updateStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, customerName);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setInt(4, customerId);
        statement.execute();
    }
    
    public static void updateAddress1(int addressId, String address) throws SQLException {
        updateStatement = "UPDATE address SET address = ?, lastUpdate = ?, lastUpdateBy =? WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn,updateStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, address);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setInt(4, addressId);
        statement.execute();
    }
    
    public static void updateAddress2(int addressId, String address) throws SQLException {
        updateStatement = "UPDATE address SET address2 = ?, lastUpdate = ?, lastUpdateBy = ?  WHERE addressId = ?";
        conn = DBConnect.openConnection();
        DBQuery.setPreparedStatement(conn,updateStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, address);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setInt(4, addressId);
        statement.execute();
    }
    
    public static void updateCity(int addressId, String city, int countryId) throws SQLException {

        // first search for the city in the database, return the id or insert and return the id
        selectStatement = "SELECT city, cityId FROM U059BO.city WHERE city = ?";
        DBQuery.setPreparedStatement(conn,selectStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, city);
        statement.execute();
        rs = statement.getResultSet();
        
        // if there is a city in the database, return the id
        if(rs.first()) {
            cityId = rs.getInt("cityId");            
        }
        else {
            cityId = DBQuery.insertCity(city, countryId);
            
            // since this is a new city, we need to add this customer address database
            String updateStatement = "UPDATE address SET cityId = ?, lastUpdate = ?, lastUpdateBy = ?  WHERE addressId = ?";
            DBQuery.setPreparedStatement(conn,updateStatement);
            statement = DBQuery.getPreparedStatement();
            statement.setInt(1, cityId);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, loggedInUser.get(0).getUserName());
            statement.setInt(4, addressId);
            statement.execute();
        }
    }
    
    public static void updatePostal(int addressId, String postal) throws SQLException {
        updateStatement = "UPDATE address SET postalCode = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn,updateStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, postal);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setInt(4, addressId);
        statement.execute();
    }
    
    public static void updateCountry(int cityId, String country) throws SQLException {
        
        // first search for the country in db, return id, or insert and return id
        selectStatement = "SELECT country, countryId FROM U059BO.country WHERE country = ?";
        DBQuery.setPreparedStatement(conn,selectStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, country);
        statement.execute();
        rs = statement.getResultSet();
        
        // if there is a country in the database, return the id
        if(rs.first()) {
            countryId = rs.getInt("countryId");            
        }
        else {
            countryId = insertCountry(country);
            
            // since this is a new country, we need to add this city/country to the city database
            updateStatement = "UPDATE city SET countryId = ?, lastUpdate = ?, lastUpdateBy = ? WHERE cityId = ?";
            DBQuery.setPreparedStatement(conn,updateStatement);
            statement = DBQuery.getPreparedStatement();
            statement.setInt(1, countryId);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, loggedInUser.get(0).getUserName());
            statement.setInt(4, cityId);
            statement.execute();
        }

        updateStatement = "UPDATE city SET countryId = ?, lastUpdate = ?, lastUpdateBy =? WHERE cityId = ?";
        DBQuery.setPreparedStatement(conn,updateStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setInt(1, countryId);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setInt(4, cityId);
        statement.execute();
    }
    
    public static void updatePhone(int addressId, String phone) throws SQLException {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        updateStatement = "UPDATE address SET phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn,updateStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, phone);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(3, loggedInUser.get(0).getUserName());
        statement.setInt(4, addressId);
        statement.execute();
    }
    
            
    public static void updateAppointment(int appointmentId, String type, ZonedDateTime start, ZonedDateTime end) throws SQLException {
        int lastId = 0;
        insertStatement = "UPDATE appointment SET type = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?";
        DBQuery.setPreparedStatement(conn, insertStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, type);
        statement.setTimestamp(2, Timestamp.from(start.toInstant()));
        statement.setTimestamp(3, Timestamp.from(end.toInstant()));
        statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(5, loggedInUser.get(0).getUserName());
        statement.setInt(6, appointmentId);
        statement.execute();
        
    }
    
    public static void deleteAppointment(Appointment appointment) throws SQLException {
        deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        statement = DBQuery.getPreparedStatement(); 
        statement.setInt(1, appointment.getAppointmentId());
        statement.execute();
    }
    
    public static void getAllAppointmentsFromDb() throws SQLException {
        appointmentList.clear();
        selectStatement = "SELECT appointment.appointmentId, appointment.type, appointment.start, appointment.end, appointment.customerId, customer.customerName, user.userName, appointment.userId"
            + " FROM U059BO.appointment"
            + " JOIN U059BO.customer ON U059BO.appointment.customerId = U059BO.customer.customerId"
            + " JOIN U059BO.user on U059BO.appointment.userId = U059BO.user.userId";
        DBQuery.setPreparedStatement(conn, selectStatement);
        statement = DBQuery.getPreparedStatement();
        statement.execute();
        ResultSet appointmentSet = statement.getResultSet();
        while(appointmentSet.next()) {
            
            Appointment appointment = new Appointment(appointmentSet.getInt("appointmentId"),
                                    appointmentSet.getInt("customerId"),
                                    appointmentSet.getString("customerName"),
                                    appointmentSet.getInt("userId"),
                                    appointmentSet.getString("userName"),
                                    appointmentSet.getString("type"),
                                    appointmentSet.getTimestamp("start").toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")),
                                    appointmentSet.getTimestamp("end").toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")));
        }  
    }
    
    public static void getAllUsersFromDb() throws SQLException {
        userList.clear();
        selectStatement = "SELECT user.userId, user.userName, user.password"
            + " FROM U059BO.user";
        DBQuery.setPreparedStatement(conn,selectStatement);
        statement = DBQuery.getPreparedStatement();
        statement.execute();
        ResultSet userSet = statement.getResultSet();

        while(userSet.next()) {
            User user = new User(userSet.getInt("userId"), userSet.getString("userName"), userSet.getString("password"));
            
        }
    }
    
    public static void getAllCustomersFromDb() throws SQLException {
        
        customerList.clear();
        selectStatement = "SELECT address.address, address.address2, address.postalCode, address.phone, customer.customerName, customer.customerId, customer.addressId, country.country, city.city, city.cityId, country.countryId"
                + " FROM U059BO.address"
                + " JOIN U059BO.customer ON U059BO.address.addressId = U059BO.customer.addressId"
                + " JOIN U059BO.city ON U059BO.address.cityId = U059BO.city.cityId"
                + " JOIN U059BO.country ON U059BO.city.countryId = U059BO.country.countryId"
                + " WHERE customer.active = ?";
        DBQuery.setPreparedStatement(conn,selectStatement);
        statement = DBQuery.getPreparedStatement();
        statement.setString(1, "1");
        statement.execute();
        ResultSet customerSet = statement.getResultSet();
        while(customerSet.next()) {
            
            Customer customer = new Customer(customerSet.getInt("customerId"), customerSet.getInt("cityId"), customerSet.getInt("countryId"), customerSet.getString("customerName"), 
                customerSet.getInt("addressId"), customerSet.getString("address"), customerSet.getString("address2"), customerSet.getString("city"), 
                customerSet.getString("postalCode"), customerSet.getString("country"), customerSet.getString("phone"));
        }
    }
}

