package com.parking.dao;

import com.parking.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    
    public Reservation getReservationById(int id) {
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE r.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Reservation getReservationByCode(String code) {
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE r.reservationCode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Reservation> getReservationsByUser(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE r.userId = ? ORDER BY r.startTime DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    // NOUVELLE METHODE
    public List<Reservation> getPendingReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE r.status = 'EN_ATTENTE' ORDER BY r.startTime";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    // NOUVELLE METHODE
    public List<Reservation> getConfirmedReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE r.status = 'CONFIRMEE' AND r.endTime > NOW() ORDER BY r.startTime";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    // NOUVELLE METHODE - Remplacer l'ancienne getActiveReservations
    public List<Reservation> getActiveReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE r.status = 'CONFIRMEE' AND r.endTime > NOW() ORDER BY r.startTime";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    public List<Reservation> getTodayReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                     "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                     "LEFT JOIN users u ON r.userId = u.id " +
                     "WHERE DATE(r.startTime) = CURDATE() AND r.status = 'CONFIRMEE' ORDER BY r.startTime";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    public boolean createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (userId, spotId, startTime, endTime, status, totalPrice, creationDate, reservationCode, vehiclePlate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getSpotId());
            stmt.setTimestamp(3, new Timestamp(reservation.getStartTime().getTime()));
            stmt.setTimestamp(4, new Timestamp(reservation.getEndTime().getTime()));
            stmt.setString(5, reservation.getStatus());
            stmt.setDouble(6, reservation.getTotalPrice());
            stmt.setTimestamp(7, new Timestamp(reservation.getCreationDate().getTime()));
            stmt.setString(8, reservation.getReservationCode());
            stmt.setString(9, reservation.getVehiclePlate());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean confirmReservation(int id) {
        String sql = "UPDATE reservations SET status = 'CONFIRMEE' WHERE id = ? AND status = 'EN_ATTENTE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean rejectReservation(int id) {
        String sql = "UPDATE reservations SET status = 'ANNULEE' WHERE id = ? AND status = 'EN_ATTENTE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean cancelReservation(int id) {
        String sql = "UPDATE reservations SET status = 'ANNULEE' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean completeReservation(int id) {
        String sql = "UPDATE reservations SET status = 'TERMINEE' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isSpotAvailable(int spotId, java.util.Date startTime, java.util.Date endTime) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE spotId = ? AND status IN ('CONFIRMEE', 'EN_ATTENTE') AND " +
                     "((startTime <= ? AND endTime > ?) OR (startTime < ? AND endTime >= ?) OR " +
                     "(startTime >= ? AND endTime <= ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, spotId);
            stmt.setTimestamp(2, new Timestamp(endTime.getTime()));
            stmt.setTimestamp(3, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(4, new Timestamp(endTime.getTime()));
            stmt.setTimestamp(5, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(6, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(7, new Timestamp(endTime.getTime()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setUserId(rs.getInt("userId"));
        reservation.setSpotId(rs.getInt("spotId"));
        reservation.setStartTime(rs.getTimestamp("startTime"));
        reservation.setEndTime(rs.getTimestamp("endTime"));
        reservation.setStatus(rs.getString("status"));
        reservation.setTotalPrice(rs.getDouble("totalPrice"));
        reservation.setCreationDate(rs.getTimestamp("creationDate"));
        
        try {
            reservation.setReservationCode(rs.getString("reservationCode"));
        } catch (SQLException e) {
            reservation.setReservationCode(null);
        }
        try {
            reservation.setVehiclePlate(rs.getString("vehiclePlate"));
        } catch (SQLException e) {
            reservation.setVehiclePlate(null);
        }
        try {
            reservation.setMotifAnnulation(rs.getString("motifAnnulation"));
        } catch (SQLException e) {
            reservation.setMotifAnnulation(null);
        }
        
        reservation.setSpotNumber(rs.getString("spotNumber"));
        reservation.setUserName(rs.getString("userName"));
        return reservation;
    }
    
 // Ajouter ces méthodes dans ReservationDAO.java

 // Annuler une réservation (Admin/Agent)
 public boolean cancelReservationByAdmin(int reservationId, String motif) {
     String sql = "UPDATE reservations SET status = 'ANNULEE', motifAnnulation = ? WHERE id = ?";
     
     try (Connection conn = DatabaseConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {
         
         stmt.setString(1, motif);
         stmt.setInt(2, reservationId);
         
         return stmt.executeUpdate() > 0;
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return false;
 }

 // Supprimer définitivement une réservation (Admin seulement)
 public boolean deleteReservation(int reservationId) {
     String sql = "DELETE FROM reservations WHERE id = ?";
     
     try (Connection conn = DatabaseConnection.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {
         
         stmt.setInt(1, reservationId);
         
         return stmt.executeUpdate() > 0;
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return false;
 }

 // Obtenir toutes les réservations (pour admin)
 public List<Reservation> getAllReservations() {
     List<Reservation> reservations = new ArrayList<>();
     String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                  "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                  "LEFT JOIN users u ON r.userId = u.id " +
                  "ORDER BY r.creationDate DESC";
     
     try (Connection conn = DatabaseConnection.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {
         
         while (rs.next()) {
             reservations.add(mapResultSetToReservation(rs));
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return reservations;
 }

 // Obtenir les réservations terminées
 public List<Reservation> getCompletedReservations() {
     List<Reservation> reservations = new ArrayList<>();
     String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                  "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                  "LEFT JOIN users u ON r.userId = u.id " +
                  "WHERE r.status = 'TERMINEE' ORDER BY r.endTime DESC";
     
     try (Connection conn = DatabaseConnection.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {
         
         while (rs.next()) {
             reservations.add(mapResultSetToReservation(rs));
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return reservations;
 }

 // Obtenir les réservations annulées
 public List<Reservation> getCancelledReservations() {
     List<Reservation> reservations = new ArrayList<>();
     String sql = "SELECT r.*, s.spotNumber, u.fullName as userName FROM reservations r " +
                  "LEFT JOIN parking_spots s ON r.spotId = s.id " +
                  "LEFT JOIN users u ON r.userId = u.id " +
                  "WHERE r.status = 'ANNULEE' ORDER BY r.endTime DESC";
     
     try (Connection conn = DatabaseConnection.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(sql)) {
         
         while (rs.next()) {
             reservations.add(mapResultSetToReservation(rs));
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return reservations;
 }
}