package com.parking.dao;

import com.parking.model.ParkingEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingEntryDAO {
    
    public ParkingEntry getEntryById(int id) {
        String sql = "SELECT e.*, s.spotNumber, u.fullName as userName FROM parking_entries e " +
                     "LEFT JOIN parking_spots s ON e.spotId = s.id " +
                     "LEFT JOIN users u ON e.userId = u.id " +
                     "WHERE e.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ParkingEntry> getEntriesByUser(int userId) {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT e.*, s.spotNumber, u.fullName as userName FROM parking_entries e " +
                     "LEFT JOIN parking_spots s ON e.spotId = s.id " +
                     "LEFT JOIN users u ON e.userId = u.id " +
                     "WHERE e.userId = ? ORDER BY e.entryTime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                entries.add(mapResultSetToEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
    
    public List<ParkingEntry> getEntriesByDate(Date date) {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT e.*, s.spotNumber, u.fullName as userName FROM parking_entries e " +
                     "LEFT JOIN parking_spots s ON e.spotId = s.id " +
                     "LEFT JOIN users u ON e.userId = u.id " +
                     "WHERE DATE(e.entryTime) = DATE(?) ORDER BY e.entryTime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, new Timestamp(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                entries.add(mapResultSetToEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
    
    public List<ParkingEntry> getActiveEntries() {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT e.*, s.spotNumber, u.fullName as userName FROM parking_entries e " +
                     "LEFT JOIN parking_spots s ON e.spotId = s.id " +
                     "LEFT JOIN users u ON e.userId = u.id " +
                     "WHERE e.exitTime IS NULL ORDER BY e.entryTime";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                entries.add(mapResultSetToEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
    
    public List<ParkingEntry> getTodayEntries() {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT e.*, s.spotNumber, u.fullName as userName FROM parking_entries e " +
                     "LEFT JOIN parking_spots s ON e.spotId = s.id " +
                     "LEFT JOIN users u ON e.userId = u.id " +
                     "WHERE DATE(e.entryTime) = CURDATE() ORDER BY e.entryTime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                entries.add(mapResultSetToEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
    
    public boolean createEntry(ParkingEntry entry) {
        String sql = "INSERT INTO parking_entries (userId, spotId, entryTime, vehiclePlate, paymentStatus, amountPaid) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, entry.getUserId());
            stmt.setInt(2, entry.getSpotId());
            stmt.setTimestamp(3, new Timestamp(entry.getEntryTime().getTime()));
            stmt.setString(4, entry.getVehiclePlate());
            stmt.setString(5, entry.getPaymentStatus());
            stmt.setDouble(6, entry.getAmountPaid());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    entry.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateExit(int entryId, Date exitTime, double amountPaid) {
        String sql = "UPDATE parking_entries SET exitTime = ?, amountPaid = ?, paymentStatus = 'PAID' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, new Timestamp(exitTime.getTime()));
            stmt.setDouble(2, amountPaid);
            stmt.setInt(3, entryId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public double calculateDailyRevenue() {
        String sql = "SELECT COALESCE(SUM(amountPaid), 0) FROM parking_entries WHERE DATE(exitTime) = CURDATE()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double calculateWeeklyRevenue() {
        String sql = "SELECT COALESCE(SUM(amountPaid), 0) FROM parking_entries WHERE YEARWEEK(exitTime) = YEARWEEK(CURDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double calculateMonthlyRevenue() {
        String sql = "SELECT COALESCE(SUM(amountPaid), 0) FROM parking_entries WHERE MONTH(exitTime) = MONTH(CURDATE()) AND YEAR(exitTime) = YEAR(CURDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getTodayEntriesCount() {
        String sql = "SELECT COUNT(*) FROM parking_entries WHERE DATE(entryTime) = CURDATE()";
        
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
    
    private ParkingEntry mapResultSetToEntry(ResultSet rs) throws SQLException {
        ParkingEntry entry = new ParkingEntry();
        entry.setId(rs.getInt("id"));
        entry.setUserId(rs.getInt("userId"));
        entry.setSpotId(rs.getInt("spotId"));
        entry.setEntryTime(rs.getTimestamp("entryTime"));
        entry.setExitTime(rs.getTimestamp("exitTime"));
        entry.setAmountPaid(rs.getDouble("amountPaid"));
        entry.setPaymentStatus(rs.getString("paymentStatus"));
        entry.setVehiclePlate(rs.getString("vehiclePlate"));
        entry.setSpotNumber(rs.getString("spotNumber"));
        entry.setUserName(rs.getString("userName"));
        return entry;
    }
}