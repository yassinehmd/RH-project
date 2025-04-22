package dao;

import models.ParametresConge;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParametresCongeDAO {
    
    public ParametresConge getParametresActuels() {
        String query = "SELECT * FROM ParametresConge WHERE annee = YEAR(CURDATE())";
        ParametresConge parametres = new ParametresConge();
        
        try (Connection conn = ConnexionDB.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                parametres.setId(rs.getInt("id"));
                parametres.setAnnee(rs.getInt("annee"));
                parametres.setDureeMaternite(rs.getInt("duree_maternite"));
                parametres.setDureeMaladieCourte(rs.getInt("duree_maladie_courte"));
                
                // Conversion des jours fériés (stockés en JSON ou texte)
                String joursFeriesStr = rs.getString("jours_feries");
                List<LocalDate> joursFeries = convertirJoursFeries(joursFeriesStr);
                parametres.setJoursFeries(joursFeries);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération paramètres: " + e.getMessage());
        }
        
        return parametres;
    }
    
    public boolean mettreAJourParametres(ParametresConge parametres) {
        String query = "UPDATE ParametresConge SET jours_feries = ?, duree_maternite = ?, "
                     + "duree_maladie_courte = ? WHERE id = ?";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, convertirJoursFeriesEnTexte(parametres.getJoursFeries()));
            stmt.setInt(2, parametres.getDureeMaternite());
            stmt.setInt(3, parametres.getDureeMaladieCourte());
            stmt.setInt(4, parametres.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour paramètres: " + e.getMessage());
            return false;
        }
    }
    
    private List<LocalDate> convertirJoursFeries(String texte) {
        // Implémentation simple - à adapter selon votre format de stockage
        List<LocalDate> jours = new ArrayList<>();
        if (texte != null && !texte.isEmpty()) {
            String[] dates = texte.split(";");
            for (String dateStr : dates) {
                jours.add(LocalDate.parse(dateStr));
            }
        }
        return jours;
    }
    
    private String convertirJoursFeriesEnTexte(List<LocalDate> jours) {
        StringBuilder sb = new StringBuilder();
        for (LocalDate date : jours) {
            if (sb.length() > 0) sb.append(";");
            sb.append(date.toString());
        }
        return sb.toString();
    }
}