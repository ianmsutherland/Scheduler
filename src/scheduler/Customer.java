/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBQuery;
import static utils.DBQuery.*;
/**
 *
 * @author ian
 */
public class Customer {
    
    // create a list that will contain all the customers 
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    
        final private int customerId;
        private int addressId;
        private int cityId;
        private int countryId;
        private String customerName;
        private String address1;
        private String address2;
        private String city;
        private String postal;
        private String country;
        private String phone;
        
        // constructor
        public Customer(int customerId, int cityId, int countryId, String customerName, int addressId, String address1, String address2, String city, String postal, String country, String phone) {
            this.customerId = customerId;
            this.cityId = cityId;
            this.countryId = countryId;
            this.addressId = addressId;
            this.customerName = customerName;
            this.address1 = address1;
            this.address2 = address2;
            this.city = city;
            this.postal = postal;
            this.country = country;
            this.phone = phone;            
            Customer.customerList.add(this);
        }
        
        public int getCustomerId() {
            return customerId;
        }
        public int getAddressId() {
            return addressId;
        }
        public String getCustomerName() {
            return customerName;
        }
        public String getAddress1() {
            return address1;
        }
        public String getAddress2() {
            return address2;
        }
        public String getCity() {
            return city;
        }
        public String getPostal() {
            return postal;
        }
        public String getCountry() {
            return country;
        }
        public String getPhone() {
            return phone;
        }
        
        // create methods that update both the customerList and the database
        public void deleteCustomer() throws SQLException {
            Customer.customerList.remove(this);
            DBQuery.deleteCustomer(this);
        }
        public void setCustomerName(String customerName) throws SQLException {
            updateCustomerName(customerId, customerName);
            this.customerName = customerName;
        }
        public void setAddress1(String address1) throws SQLException {
            updateAddress1(addressId, address1);
            this.address1 = address1;
        }
        public void setAddress2(String address2) throws SQLException {
            updateAddress2(addressId, address2);
            this.address2 = address2;
        }
        public void setCity(String city) throws SQLException {
            updateCity(addressId, city, countryId);
            this.city = city;
        }
        public void setPostal(String postal) throws SQLException {
            if (!postal.isEmpty()) {
                updatePostal(addressId, postal);
                this.postal = postal;
            }
        }
        public void setCountry(String country) throws SQLException {
            updateCountry(cityId, country);
            this.country = country;
        }
        public void setPhone(String phone) throws SQLException {
            updatePhone(addressId, phone);
            this.phone = phone;
        }
}
   
