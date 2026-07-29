package com.taxis.dao;

import com.taxis.model.Parametres;
import java.sql.*;

public class ParametresDAO {
    
    public Parametres getParametres() throws SQLException {
        String query = "SELECT * FROM parametres WHERE id = 1";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            if (rs.next()) {
                return mapResultSetToParametres(rs);
            }
        }
        
        // Si aucun paramètre, créer les paramètres par défaut
        Parametres defaults = getParametresParDefaut();
        updateParametres(defaults);
        return defaults;
    }
    
    public void updateParametres(Parametres parametres) throws SQLException {
        String query = "UPDATE parametres SET prix_prise_en_charge=?, prix_km_jour=?, prix_km_nuit=?, " +
                       "prix_attente_min=?, commission_entreprise=?, frais_bagages=?, frais_animaux=?, " +
                       "majoration_nuit=?, majoration_weekend=?, updated_at=NOW() WHERE id=1";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setDouble(1, parametres.getPrixPriseEnCharge());
            ps.setDouble(2, parametres.getPrixKmJour());
            ps.setDouble(3, parametres.getPrixKmNuit());
            ps.setDouble(4, parametres.getPrixAttenteMin());
            ps.setDouble(5, parametres.getCommissionEntreprise());
            ps.setDouble(6, parametres.getFraisBagages());
            ps.setDouble(7, parametres.getFraisAnimaux());
            ps.setDouble(8, parametres.getMajorationNuit());
            ps.setDouble(9, parametres.getMajorationWeekend());
            ps.executeUpdate();
        }
    }
    
    private Parametres getParametresParDefaut() {
        Parametres p = new Parametres();
        p.setPrixPriseEnCharge(3.50);
        p.setPrixKmJour(1.20);
        p.setPrixKmNuit(1.80);
        p.setPrixAttenteMin(0.50);
        p.setCommissionEntreprise(20.00);
        p.setFraisBagages(2.00);
        p.setFraisAnimaux(3.00);
        p.setMajorationNuit(50.00);
        p.setMajorationWeekend(20.00);
        return p;
    }
    
    private Parametres mapResultSetToParametres(ResultSet rs) throws SQLException {
        Parametres p = new Parametres();
        p.setId(rs.getInt("id"));
        p.setPrixPriseEnCharge(rs.getDouble("prix_prise_en_charge"));
        p.setPrixKmJour(rs.getDouble("prix_km_jour"));
        p.setPrixKmNuit(rs.getDouble("prix_km_nuit"));
        p.setPrixAttenteMin(rs.getDouble("prix_attente_min"));
        p.setCommissionEntreprise(rs.getDouble("commission_entreprise"));
        p.setFraisBagages(rs.getDouble("frais_bagages"));
        p.setFraisAnimaux(rs.getDouble("frais_animaux"));
        p.setMajorationNuit(rs.getDouble("majoration_nuit"));
        p.setMajorationWeekend(rs.getDouble("majoration_weekend"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}