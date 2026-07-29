package com.parking.model;

import java.io.Serializable;
import java.util.Date;

public class Subscription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int userId;
    private int spotId;
    private String subscriptionType;
    private Date startDate;
    private Date endDate;
    private double price;
    private String status;
    
    // Champs supplémentaires
    private String spotNumber;
    private String userName;
    
    public Subscription() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getSpotId() { return spotId; }
    public void setSpotId(int spotId) { this.spotId = spotId; }
    
    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public boolean isActive() {
        return "ACTIVE".equals(status) && endDate != null && endDate.after(new Date());
    }
}