package com.parking.dao;

import com.parking.model.Subscription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {
    
    public Subscription getSubscriptionById(int id) {
        String sql = "SELECT s.*, sp.spotNumber, u.fullName as userName FROM subscriptions s " +
                     "LEFT JOIN parking_spots sp ON s.spotId = sp.id " +
                     "LEFT JOIN users u ON s.userId = u.id " +
                     "WHERE s.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSubscription(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Subscription> getSubscriptionsByUser(int userId) {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT s.*, sp.spotNumber, u.fullName as userName FROM subscriptions s " +
                     "LEFT JOIN parking_spots sp ON s.spotId = sp.id " +
                     "LEFT JOIN users u ON s.userId = u.id " +
                     "WHERE s.userId = ? ORDER BY s.startDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                subscriptions.add(mapResultSetToSubscription(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }
    
    public List<Subscription> getActiveSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT s.*, sp.spotNumber, u.fullName as userName FROM subscriptions s " +
                     "LEFT JOIN parking_spots sp ON s.spotId = sp.id " +
                     "LEFT JOIN users u ON s.userId = u.id " +
                     "WHERE s.status = 'ACTIVE' AND s.endDate > NOW() ORDER BY s.endDate";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                subscriptions.add(mapResultSetToSubscription(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }
    
    public Subscription getSubscriptionByUser(int userId) {
        String sql = "SELECT s.*, sp.spotNumber, u.fullName as userName FROM subscriptions s " +
                     "LEFT JOIN parking_spots sp ON s.spotId = sp.id " +
                     "LEFT JOIN users u ON s.userId = u.id " +
                     "WHERE s.userId = ? AND s.status = 'ACTIVE' AND s.endDate > NOW()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSubscription(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean createSubscription(Subscription subscription) {
        String sql = "INSERT INTO subscriptions (userId, spotId, subscriptionType, startDate, endDate, price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, subscription.getUserId());
            stmt.setInt(2, subscription.getSpotId());
            stmt.setString(3, subscription.getSubscriptionType());
            stmt.setTimestamp(4, new Timestamp(subscription.getStartDate().getTime()));
            stmt.setTimestamp(5, new Timestamp(subscription.getEndDate().getTime()));
            stmt.setDouble(6, subscription.getPrice());
            stmt.setString(7, subscription.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    subscription.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean cancelSubscription(int id) {
        String sql = "UPDATE subscriptions SET status = 'CANCELLED' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean renewSubscription(int id, Date newEndDate) {
        String sql = "UPDATE subscriptions SET endDate = ?, status = 'ACTIVE' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, new Timestamp(newEndDate.getTime()));
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getActiveSubscriptionsCount() {
        String sql = "SELECT COUNT(*) FROM subscriptions WHERE status = 'ACTIVE' AND endDate > NOW()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private Subscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(rs.getInt("id"));
        subscription.setUserId(rs.getInt("userId"));
        subscription.setSpotId(rs.getInt("spotId"));
        subscription.setSubscriptionType(rs.getString("subscriptionType"));
        subscription.setStartDate(rs.getTimestamp("startDate"));
        subscription.setEndDate(rs.getTimestamp("endDate"));
        subscription.setPrice(rs.getDouble("price"));
        subscription.setStatus(rs.getString("status"));
        subscription.setSpotNumber(rs.getString("spotNumber"));
        subscription.setUserName(rs.getString("userName"));
        return subscription;
    }
}