package com.textile.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ParametreDAO {
    
    public String getValeur(String cle) throws SQLException {
        String sql = "SELECT valeur FROM parametres WHERE cle = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cle);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("valeur");
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public void updateValeur(String cle, String valeur) throws SQLException {
        String sql = "UPDATE parametres SET valeur = ? WHERE cle = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, valeur);
            ps.setString(2, cle);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public Map<String, String> getAllParametres() throws SQLException {
        Map<String, String> parametres = new HashMap<>();
        String sql = "SELECT cle, valeur FROM parametres";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                parametres.put(rs.getString("cle"), rs.getString("valeur"));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return parametres;
    }
}