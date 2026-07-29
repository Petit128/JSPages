package com.parking.metier;

import com.parking.model.User;
import com.parking.model.ParkingSpot;
import com.parking.dao.ParkingSpotDAO;
import java.util.List;
import java.util.stream.Collectors;

public class SpotAllocationService {
    
    private ParkingSpotDAO spotDAO;
    
    public SpotAllocationService() {
        this.spotDAO = new ParkingSpotDAO();
    }
    
    public ParkingSpot findBestAvailableSpot(User user, String preferredType) {
        List<ParkingSpot> availableSpots = spotDAO.getAvailableSpots();
        
        if (availableSpots.isEmpty()) {
            return null;
        }
        
        // Priorité 1: Places réservées VIP pour les utilisateurs VIP
        if (user != null && "VIP".equals(user.getUserType())) {
            List<ParkingSpot> vipSpots = availableSpots.stream()
                .filter(spot -> "VIP".equals(spot.getSpotType()))
                .collect(Collectors.toList());
            
            if (!vipSpots.isEmpty()) {
                return vipSpots.get(0);
            }
        }
        
        // Priorité 2: Type préféré
        if (preferredType != null && !preferredType.isEmpty()) {
            List<ParkingSpot> preferredSpots = availableSpots.stream()
                .filter(spot -> preferredType.equals(spot.getSpotType()))
                .collect(Collectors.toList());
            
            if (!preferredSpots.isEmpty()) {
                return preferredSpots.get(0);
            }
        }
        
        // Priorité 3: N'importe quelle place disponible
        return availableSpots.get(0);
    }
    
    public ParkingSpot findSpotForVIP() {
        List<ParkingSpot> vipSpots = spotDAO.getAvailableSpotsByType("VIP");
        if (!vipSpots.isEmpty()) {
            return vipSpots.get(0);
        }
        return null;
    }
    
    public int getAvailableSpotsCount() {
        return spotDAO.getAvailableSpotsCount();
    }
    
    public List<ParkingSpot> getAvailableSpotsByType(String spotType) {
        return spotDAO.getAvailableSpotsByType(spotType);
    }
    
    public boolean hasAvailableSpots() {
        return getAvailableSpotsCount() > 0;
    }
}