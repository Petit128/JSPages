package com.parking.metier;

import com.parking.model.*;
import com.parking.dao.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ParkingService {
    
    private UserDAO userDAO;
    private ParkingSpotDAO spotDAO;
    private ReservationDAO reservationDAO;
    private SubscriptionDAO subscriptionDAO;
    private ParkingEntryDAO entryDAO;
    private TariffDAO tariffDAO;
    private TariffCalculator tariffCalculator;
    private SpotAllocationService spotAllocation;
    
    public ParkingService() {
        this.userDAO = new UserDAO();
        this.spotDAO = new ParkingSpotDAO();
        this.reservationDAO = new ReservationDAO();
        this.subscriptionDAO = new SubscriptionDAO();
        this.entryDAO = new ParkingEntryDAO();
        this.tariffDAO = new TariffDAO();
        this.tariffCalculator = new TariffCalculator();
        this.spotAllocation = new SpotAllocationService();
    }
    
    // ==================== USER MANAGEMENT ====================
    
    public User authenticateUser(String username, String password) {
        if (userDAO.validateUser(username, password)) {
            return userDAO.getUserByUsername(username);
        }
        return null;
    }
    
    public boolean registerUser(User user) {
        user.setRegistrationDate(new Date());
        return userDAO.addUser(user);
    }
    
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }
    
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
    
    // ==================== SPOT MANAGEMENT ====================
    
    public List<ParkingSpot> getAllSpots() {
        return spotDAO.getAllSpots();
    }
    
    public List<ParkingSpot> getAvailableSpotsList() {
        return spotDAO.getAvailableSpots();
    }
    
    public int getAvailableSpots() {
        return spotDAO.getAvailableSpotsCount();
    }
    
    public int getTotalSpots() {
        return spotDAO.getTotalSpotsCount();
    }
    
    public int getOccupiedSpots() {
        return spotDAO.getOccupiedSpotsCount();
    }
    
 // Dans ParkingService.java
    public double getOccupancyRate() {
        int total = getTotalSpots();
        if (total == 0) return 0;
        return (double) getOccupiedSpots() / total * 100;
    }
    
    public boolean addSpot(ParkingSpot spot) {
        return spotDAO.addSpot(spot);
    }
    
    public boolean updateSpot(ParkingSpot spot) {
        return spotDAO.updateSpot(spot);
    }
    
    public boolean deleteSpot(int id) {
        return spotDAO.deleteSpot(id);
    }
    
    // ==================== RESERVATION MANAGEMENT ====================
    
    public boolean createReservation(int userId, int spotId, Date startTime, Date endTime) {
        if (!reservationDAO.isSpotAvailable(spotId, startTime, endTime)) {
            return false;
        }
        
        User user = userDAO.getUserById(userId);
        ParkingSpot spot = spotDAO.getSpotById(spotId);
        
        if (user == null || spot == null) {
            return false;
        }
        
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setSpotId(spotId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setStatus("CONFIRMED");
        reservation.setTotalPrice(tariffCalculator.calculateReservationPrice(spot, startTime, endTime, user));
        reservation.setCreationDate(new Date());
        
        boolean created = reservationDAO.createReservation(reservation);
        if (created) {
            spotDAO.updateSpotAvailability(spotId, false);
        }
        
        return created;
    }
    
    public boolean cancelReservation(int reservationId) {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation != null) {
            boolean cancelled = reservationDAO.cancelReservation(reservationId);
            if (cancelled) {
                spotDAO.updateSpotAvailability(reservation.getSpotId(), true);
            }
            return cancelled;
        }
        return false;
    }
    
    public List<Reservation> getUserReservations(int userId) {
        return reservationDAO.getReservationsByUser(userId);
    }
    
    public List<Reservation> getTodayReservations() {
        return reservationDAO.getTodayReservations();
    }
    
    public List<Reservation> getActiveReservations() {
        return reservationDAO.getActiveReservations();
    }
    
    // ==================== ENTRY/EXIT MANAGEMENT ====================
    
    public ParkingEntry recordEntry(int userId, int spotId, String vehiclePlate) {
        ParkingSpot spot = spotDAO.getSpotById(spotId);
        if (spot == null || !spot.isAvailable()) {
            return null;
        }
        
        Subscription subscription = subscriptionDAO.getSubscriptionByUser(userId);
        
        ParkingEntry entry = new ParkingEntry();
        entry.setUserId(userId);
        entry.setSpotId(spotId);
        entry.setEntryTime(new Date());
        entry.setVehiclePlate(vehiclePlate);
        
        if (subscription != null) {
            entry.setPaymentStatus("PAID");
            entry.setAmountPaid(0);
        } else {
            entry.setPaymentStatus("PENDING");
        }
        
        boolean recorded = entryDAO.createEntry(entry);
        if (recorded) {
            spotDAO.updateSpotAvailability(spotId, false);
            return entry;
        }
        return null;
    }
    
    public ParkingEntry recordExit(int entryId) {
        ParkingEntry entry = entryDAO.getEntryById(entryId);
        if (entry != null && entry.getExitTime() == null) {
            User user = userDAO.getUserById(entry.getUserId());
            ParkingSpot spot = spotDAO.getSpotById(entry.getSpotId());
            Subscription subscription = subscriptionDAO.getSubscriptionByUser(entry.getUserId());
            
            Date exitTime = new Date();
            double amount = tariffCalculator.calculatePrice(spot, entry.getEntryTime(), exitTime, user, subscription);
            
            boolean updated = entryDAO.updateExit(entryId, exitTime, amount);
            if (updated) {
                spotDAO.updateSpotAvailability(entry.getSpotId(), true);
                entry.setExitTime(exitTime);
                entry.setAmountPaid(amount);
                if (amount > 0) {
                    entry.setPaymentStatus("PAID");
                }
            }
            return entry;
        }
        return null;
    }
    
    public List<ParkingEntry> getCurrentOccupancy() {
        return entryDAO.getActiveEntries();
    }
    
    public List<ParkingEntry> getUserEntries(int userId) {
        return entryDAO.getEntriesByUser(userId);
    }
    
    public List<ParkingEntry> getTodayEntries() {
        return entryDAO.getTodayEntries();
    }
    
    public int getTodayEntriesCount() {
        return entryDAO.getTodayEntriesCount();
    }
    
    // ==================== SUBSCRIPTION MANAGEMENT ====================
    
    public boolean createSubscription(int userId, int spotId, String subscriptionType) {
        User user = userDAO.getUserById(userId);
        ParkingSpot spot = spotDAO.getSpotById(spotId);
        
        if (user == null || spot == null) {
            return false;
        }
        
        if (subscriptionDAO.getSubscriptionByUser(userId) != null) {
            return false;
        }
        
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setSpotId(spotId);
        subscription.setSubscriptionType(subscriptionType);
        subscription.setStartDate(new Date());
        
        Calendar calendar = Calendar.getInstance();
        switch(subscriptionType) {
            case "WEEKLY":
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                break;
            case "MONTHLY":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "YEARLY":
                calendar.add(Calendar.YEAR, 1);
                break;
            default:
                calendar.add(Calendar.MONTH, 1);
        }
        subscription.setEndDate(calendar.getTime());
        
        subscription.setPrice(tariffCalculator.calculateSubscriptionPrice(subscriptionType, spot));
        subscription.setStatus("ACTIVE");
        
        boolean created = subscriptionDAO.createSubscription(subscription);
        if (created) {
            spotDAO.updateSpotAvailability(spotId, false);
        }
        
        return created;
    }
    
    public boolean cancelSubscription(int subscriptionId) {
        Subscription subscription = subscriptionDAO.getSubscriptionById(subscriptionId);
        if (subscription != null) {
            boolean cancelled = subscriptionDAO.cancelSubscription(subscriptionId);
            if (cancelled) {
                spotDAO.updateSpotAvailability(subscription.getSpotId(), true);
            }
            return cancelled;
        }
        return false;
    }
    
    public Subscription getUserSubscription(int userId) {
        return subscriptionDAO.getSubscriptionByUser(userId);
    }
    
    public List<Subscription> getAllActiveSubscriptions() {
        return subscriptionDAO.getActiveSubscriptions();
    }
    
    public int getActiveSubscriptionsCount() {
        return subscriptionDAO.getActiveSubscriptionsCount();
    }
    
    // ==================== TARIFF MANAGEMENT ====================
    
    public List<Tariff> getAllTariffs() {
        return tariffDAO.getAllTariffs();
    }
    
    public Tariff getTariffByType(String tariffType) {
        return tariffDAO.getTariffByType(tariffType);
    }
    
    public boolean updateTariff(Tariff tariff) {
        return tariffDAO.updateTariff(tariff);
    }
    
    // ==================== STATISTICS ====================
    
    public double calculateDailyRevenue() {
        return entryDAO.calculateDailyRevenue();
    }
    
    public double calculateWeeklyRevenue() {
        return entryDAO.calculateWeeklyRevenue();
    }
    
    public double calculateMonthlyRevenue() {
        return entryDAO.calculateMonthlyRevenue();
    }
    
    // ==================== GETTERS ====================
    
    public SpotAllocationService getSpotAllocation() {
        return spotAllocation;
    }
    
    public TariffCalculator getTariffCalculator() {
        return tariffCalculator;
    }
    
 // Ajouter ces méthodes dans ParkingService.java

    public boolean createPendingReservation(int userId, int spotId, Date startTime, Date endTime, String vehiclePlate) {
        if (!reservationDAO.isSpotAvailable(spotId, startTime, endTime)) {
            return false;
        }
        
        User user = userDAO.getUserById(userId);
        ParkingSpot spot = spotDAO.getSpotById(spotId);
        
        if (user == null || spot == null) {
            return false;
        }
        
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setSpotId(spotId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setStatus("EN_ATTENTE");  // En attente de validation
        reservation.setTotalPrice(tariffCalculator.calculateReservationPrice(spot, startTime, endTime, user));
        reservation.setCreationDate(new Date());
        reservation.setReservationCode(Reservation.generateCode());
        reservation.setVehiclePlate(vehiclePlate);
        
        return reservationDAO.createReservation(reservation);
    }

    public List<Reservation> getPendingReservations() {
        return reservationDAO.getPendingReservations();
    }

    public List<Reservation> getConfirmedReservations() {
        return reservationDAO.getConfirmedReservations();
    }

    public boolean confirmReservation(int reservationId) {
        boolean confirmed = reservationDAO.confirmReservation(reservationId);
        if (confirmed) {
            Reservation res = reservationDAO.getReservationById(reservationId);
            if (res != null) {
                spotDAO.updateSpotAvailability(res.getSpotId(), false);
            }
        }
        return confirmed;
    }

    public boolean rejectReservation(int reservationId) {
        return reservationDAO.rejectReservation(reservationId);
    }

    public boolean completeReservation(int reservationId) {
        return reservationDAO.completeReservation(reservationId);
    }

    public Reservation getReservationByCode(String code) {
        return reservationDAO.getReservationByCode(code);
    }
    
 // Ajouter ces méthodes dans ParkingService.java

    public boolean cancelReservationByAdmin(int reservationId, String motif) {
        boolean cancelled = reservationDAO.cancelReservationByAdmin(reservationId, motif);
        if (cancelled) {
            Reservation res = reservationDAO.getReservationById(reservationId);
            if (res != null && "ANNULEE".equals(res.getStatus())) {
                // Libérer la place si elle était réservée
                spotDAO.updateSpotAvailability(res.getSpotId(), true);
            }
        }
        return cancelled;
    }

    public boolean deleteReservation(int reservationId) {
        return reservationDAO.deleteReservation(reservationId);
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    public List<Reservation> getCompletedReservations() {
        return reservationDAO.getCompletedReservations();
    }

    public List<Reservation> getCancelledReservations() {
        return reservationDAO.getCancelledReservations();
    }
    
 // Ajouter ces méthodes dans ParkingService.java

    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
}