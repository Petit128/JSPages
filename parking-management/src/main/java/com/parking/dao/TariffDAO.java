package com.parking.dao;

import com.parking.model.Tariff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TariffDAO {
    
    public Tariff getTariffById(int id) {
        String sql = "SELECT * FROM tariffs WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTariff(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Tariff getTariffByType(String tariffType) {
        String sql = "SELECT * FROM tariffs WHERE tariffType = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tariffType);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTariff(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Tariff> getAllTariffs() {
        List<Tariff> tariffs = new ArrayList<>();
        String sql = "SELECT * FROM tariffs ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tariffs.add(mapResultSetToTariff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tariffs;
    }
    
    public boolean addTariff(Tariff tariff) {
        String sql = "INSERT INTO tariffs (tariffType, hourlyRate, dailyRate, weeklyRate, monthlyRate, vipDiscount, subscriberDiscount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, tariff.getTariffType());
            stmt.setDouble(2, tariff.getHourlyRate());
            stmt.setDouble(3, tariff.getDailyRate());
            stmt.setDouble(4, tariff.getWeeklyRate());
            stmt.setDouble(5, tariff.getMonthlyRate());
            stmt.setDouble(6, tariff.getVipDiscount());
            stmt.setDouble(7, tariff.getSubscriberDiscount());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    tariff.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateTariff(Tariff tariff) {
        String sql = "UPDATE tariffs SET hourlyRate=?, dailyRate=?, weeklyRate=?, monthlyRate=?, vipDiscount=?, subscriberDiscount=? WHERE tariffType=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, tariff.getHourlyRate());
            stmt.setDouble(2, tariff.getDailyRate());
            stmt.setDouble(3, tariff.getWeeklyRate());
            stmt.setDouble(4, tariff.getMonthlyRate());
            stmt.setDouble(5, tariff.getVipDiscount());
            stmt.setDouble(6, tariff.getSubscriberDiscount());
            stmt.setString(7, tariff.getTariffType());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteTariff(int id) {
        String sql = "DELETE FROM tariffs WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Tariff mapResultSetToTariff(ResultSet rs) throws SQLException {
        Tariff tariff = new Tariff();
        tariff.setId(rs.getInt("id"));
        tariff.setTariffType(rs.getString("tariffType"));
        tariff.setHourlyRate(rs.getDouble("hourlyRate"));
        tariff.setDailyRate(rs.getDouble("dailyRate"));
        tariff.setWeeklyRate(rs.getDouble("weeklyRate"));
        tariff.setMonthlyRate(rs.getDouble("monthlyRate"));
        tariff.setVipDiscount(rs.getDouble("vipDiscount"));
        tariff.setSubscriberDiscount(rs.getDouble("subscriberDiscount"));
        tariff.setLastUpdated(rs.getTimestamp("lastUpdated"));
        return tariff;
    }
}