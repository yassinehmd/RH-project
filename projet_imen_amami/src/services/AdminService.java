package services;

import dao.ConnexionDB;
import dao.UtilisateurDAO;
import models.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class AdminService {
    private UtilisateurDAO utilisateurDao;
    
    public AdminService() {
        this.utilisateurDao = new UtilisateurDAO();
    }
    
    public void chargerUtilisateurs(JTable table) {
        DefaultTableModel model = new DefaultTableModel(
            new Object[] {"ID", "Nom", "Prénom", "Email", "Rôle"}, 0);
        
        for (Utilisateur user : utilisateurDao.getAllUtilisateurs()) {
            model.addRow(new Object[] {
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getRole()
            });
        }
        
        table.setModel(model);
    }
    
    public Utilisateur getUtilisateur(int id) {
        return utilisateurDao.getUtilisateurById(id);
    }
    
    public boolean sauvegarderUtilisateur(Utilisateur user) {
        if (user.getId() == 0) {
            return utilisateurDao.ajouterUtilisateur(user);
        } else {
            return utilisateurDao.modifierUtilisateur(user);
        }
    }
    
    public boolean supprimerUtilisateur(int id) {
        return utilisateurDao.supprimerUtilisateur(id);
    }
    
    public String getJoursFeries() {
        String query = "SELECT jours_feries FROM ParametresConge WHERE annee = YEAR(CURDATE())";
        
        try (Connection conn = ConnexionDB.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getString("jours_feries");
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération jours fériés: " + e.getMessage());
        }
        
        return "";
    }
    
    public int getDureeMaternite() {
        String query = "SELECT duree_maternite FROM ParametresConge LIMIT 1";
        
        try (Connection conn = ConnexionDB.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("duree_maternite");
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération durée maternité: " + e.getMessage());
        }
        
        return 98; // Valeur par défaut
    }
    
    public void sauvegarderParametres(String joursFeries, int dureeMaternite) {
        String query = "UPDATE ParametresConge SET jours_feries = ?, duree_maternite = ? "
                     + "WHERE annee = YEAR(CURDATE())";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, joursFeries);
            stmt.setInt(2, dureeMaternite);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde paramètres: " + e.getMessage());
        }
    }
}