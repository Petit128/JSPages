package com.parking.dao;

import com.parking.model.ParkingSpot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotDAO {
    
    public ParkingSpot getSpotById(int id) {
        String sql = "SELECT * FROM parking_spots WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSpot(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ParkingSpot getSpotByNumber(String spotNumber) {
        String sql = "SELECT * FROM parking_spots WHERE spotNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, spotNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSpot(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ParkingSpot> getAllSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                spots.add(mapResultSetToSpot(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
    
    public List<ParkingSpot> getAvailableSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE isAvailable = true ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                spots.add(mapResultSetToSpot(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
    
    public List<ParkingSpot> getSpotsByType(String spotType) {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE spotType = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, spotType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                spots.add(mapResultSetToSpot(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
    
    public List<ParkingSpot> getAvailableSpotsByType(String spotType) {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE spotType = ? AND isAvailable = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, spotType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                spots.add(mapResultSetToSpot(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }
    
    public List<ParkingSpot> getVIPSpots() {
        return getSpotsByType("VIP");
    }
    
    public boolean addSpot(ParkingSpot spot) {
        String sql = "INSERT INTO parking_spots (spotNumber, spotType, location, isAvailable, hourlyRate, reservedForVIP) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, spot.getSpotNumber());
            stmt.setString(2, spot.getSpotType());
            stmt.setString(3, spot.getLocation());
            stmt.setBoolean(4, spot.isAvailable());
            stmt.setDouble(5, spot.getHourlyRate());
            stmt.setBoolean(6, spot.isReservedForVIP());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    spot.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateSpot(ParkingSpot spot) {
        String sql = "UPDATE parking_spots SET spotNumber=?, spotType=?, location=?, hourlyRate=?, reservedForVIP=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, spot.getSpotNumber());
            stmt.setString(2, spot.getSpotType());
            stmt.setString(3, spot.getLocation());
            stmt.setDouble(4, spot.getHourlyRate());
            stmt.setBoolean(5, spot.isReservedForVIP());
            stmt.setInt(6, spot.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateSpotAvailability(int spotId, boolean isAvailable) {
        String sql = "UPDATE parking_spots SET isAvailable = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, spotId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteSpot(int id) {
        String sql = "DELETE FROM parking_spots WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public int getAvailableSpotsCount() {
        String sql = "SELECT COUNT(*) FROM parking_spots WHERE isAvailable = true";
        
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
    
    public int getTotalSpotsCount() {
        String sql = "SELECT COUNT(*) FROM parking_spots";
        
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
    
    public int getOccupiedSpotsCount() {
        return getTotalSpotsCount() - getAvailableSpotsCount();
    }
    
    private ParkingSpot mapResultSetToSpot(ResultSet rs) throws SQLException {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(rs.getInt("id"));
        spot.setSpotNumber(rs.getString("spotNumber"));
        spot.setSpotType(rs.getString("spotType"));
        spot.setLocation(rs.getString("location"));
        spot.setAvailable(rs.getBoolean("isAvailable"));
        spot.setHourlyRate(rs.getDouble("hourlyRate"));
        spot.setReservedForVIP(rs.getBoolean("reservedForVIP"));
        return spot;
    }
}