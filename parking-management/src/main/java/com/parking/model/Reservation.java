package com.parking.model;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int userId;
    private int spotId;
    private Date startTime;
    private Date endTime;
    private String status;  // EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE
    private double totalPrice;
    private Date creationDate;
    private String reservationCode;
    private String vehiclePlate;
    
    private String spotNumber;
    private String userName;
    
    public Reservation() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getSpotId() { return spotId; }
    public void setSpotId(int spotId) { this.spotId = spotId; }
    
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    
    public String getReservationCode() { return reservationCode; }
    public void setReservationCode(String reservationCode) { this.reservationCode = reservationCode; }
    
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public long getDurationHours() {
        return (endTime.getTime() - startTime.getTime()) / (60 * 60 * 1000);
    }
    
 // Ajouter ce champ dans la classe Reservation
    private String motifAnnulation;

    // Getter et Setter
    public String getMotifAnnulation() { return motifAnnulation; }
    public void setMotifAnnulation(String motifAnnulation) { this.motifAnnulation = motifAnnulation; }
    
    // Générer un code de réservation unique
    public static String generateCode() {
        return "RES" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}