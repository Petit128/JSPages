package com.parking.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String userType;
    private String vehiclePlate;
    private Date registrationDate;
    
    public User() {}
    
    public User(String username, String password, String fullName, String email, String role, String userType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.userType = userType;
        this.registrationDate = new Date();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    
    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", fullName=" + fullName + ", role=" + role + '}';
    }
}