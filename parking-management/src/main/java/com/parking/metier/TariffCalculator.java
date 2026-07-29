package com.parking.metier;

import com.parking.model.User;
import com.parking.model.ParkingSpot;
import com.parking.model.Subscription;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TariffCalculator {
    
    private static final double MAX_DAILY_RATE = 20.0;
    
    public double calculatePrice(ParkingSpot spot, Date entryTime, Date exitTime, User user, Subscription subscription) {
        // Si l'utilisateur a un abonnement actif, gratuit
        if (subscription != null && subscription.isActive()) {
            return 0;
        }
        
        long durationInMillis = exitTime.getTime() - entryTime.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(durationInMillis);
        
        // Arrondir à l'heure supérieure
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60;
        if (minutes > 0) {
            hours++;
        }
        
        if (hours == 0) {
            hours = 1; // Minimum 1 heure
        }
        
        double basePrice = hours * spot.getHourlyRate();
        
        // Application du tarif journalier maximum
        if (hours >= 24) {
            long days = hours / 24;
            double dailyPrice = days * MAX_DAILY_RATE;
            long remainingHours = hours % 24;
            double remainingPrice = remainingHours * spot.getHourlyRate();
            basePrice = dailyPrice + remainingPrice;
        }
        
        // Réduction pour les clients VIP
        if (user != null && "VIP".equals(user.getUserType())) {
            basePrice = basePrice * 0.85; // 15% de réduction
        }
        
        return Math.round(basePrice * 100.0) / 100.0;
    }
    
    public double calculateSubscriptionPrice(String subscriptionType, ParkingSpot spot) {
        double monthlyRate = spot.getHourlyRate() * 8 * 30; // 8 heures par jour en moyenne
        
        switch(subscriptionType) {
            case "WEEKLY":
                return monthlyRate / 4 * 0.8; // 20% de réduction
            case "MONTHLY":
                return monthlyRate * 0.7; // 30% de réduction
            case "YEARLY":
                return monthlyRate * 12 * 0.5; // 50% de réduction
            default:
                return monthlyRate;
        }
    }
    
    public double calculateReservationPrice(ParkingSpot spot, Date startTime, Date endTime, User user) {
        long durationInMillis = endTime.getTime() - startTime.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(durationInMillis);
        
        if (hours == 0) {
            hours = 1;
        }
        
        double price = hours * spot.getHourlyRate();
        
        if (user != null && "VIP".equals(user.getUserType())) {
            price = price * 0.85;
        }
        
        return Math.round(price * 100.0) / 100.0;
    }
}