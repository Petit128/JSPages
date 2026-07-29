package com.parking.model;

import java.io.Serializable;
import java.util.Date;

public class ParkingEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int userId;
    private int spotId;
    private Date entryTime;
    private Date exitTime;
    private double amountPaid;
    private String paymentStatus;
    private String vehiclePlate;
    
    // Champs supplémentaires pour affichage
    private String spotNumber;
    private String userName;
    
    public ParkingEntry() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getSpotId() { return spotId; }
    public void setSpotId(int spotId) { this.spotId = spotId; }
    
    public Date getEntryTime() { return entryTime; }
    public void setEntryTime(Date entryTime) { this.entryTime = entryTime; }
    
    public Date getExitTime() { return exitTime; }
    public void setExitTime(Date exitTime) { this.exitTime = exitTime; }
    
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public long getDurationMinutes() {
        if (exitTime == null) return 0;
        return (exitTime.getTime() - entryTime.getTime()) / (60 * 1000);
    }
    
    public long getDurationHours() {
        return getDurationMinutes() / 60;
    }
}