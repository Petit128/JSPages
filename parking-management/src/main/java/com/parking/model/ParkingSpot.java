package com.parking.model;

import java.io.Serializable;

public class ParkingSpot implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String spotNumber;
    private String spotType;
    private String location;
    private boolean isAvailable;
    private double hourlyRate;
    private boolean reservedForVIP;
    
    public ParkingSpot() {}
    
    public ParkingSpot(String spotNumber, String spotType, String location, double hourlyRate) {
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.location = location;
        this.hourlyRate = hourlyRate;
        this.isAvailable = true;
        this.reservedForVIP = "VIP".equals(spotType);
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public String getSpotType() { return spotType; }
    public void setSpotType(String spotType) { this.spotType = spotType; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public boolean isReservedForVIP() { return reservedForVIP; }
    public void setReservedForVIP(boolean reservedForVIP) { this.reservedForVIP = reservedForVIP; }
    
    @Override
    public String toString() {
        return spotNumber + " - " + spotType + " ($" + hourlyRate + "/h)";
    }
}