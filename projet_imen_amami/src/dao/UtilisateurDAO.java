package dao;

import models.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UtilisateurDAO {
    
    // Création d'un nouvel utilisateur
    public boolean ajouterUtilisateur(Utilisateur user) {
        String query = "INSERT INTO Utilisateur (nom, prenom, email, telephone, cin, service, role, grade, date_naissance) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getTelephone());
            stmt.setString(5, user.getCin());
            stmt.setString(6, user.getService());
            stmt.setString(7, user.getRole());
            stmt.setString(8, user.getGrade());
            stmt.setDate(9, user.getDateNaissance() != null ? 
                          Date.valueOf(user.getDateNaissance()) : null);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    public boolean modifierUtilisateur(Utilisateur user) {
        String query = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ?, telephone = ?, " +
                       "cin = ?, service = ?, role = ?, grade = ?, date_naissance = ? WHERE id = ?";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getTelephone());
            stmt.setString(5, user.getCin());
            stmt.setString(6, user.getService());
            stmt.setString(7, user.getRole());
            stmt.setString(8, user.getGrade());
            stmt.setDate(9, user.getDateNaissance() != null ? 
                         Date.valueOf(user.getDateNaissance()) : null);
            stmt.setInt(10, user.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    // Suppression d'un utilisateur
    public boolean supprimerUtilisateur(int id) {
        String query = "DELETE FROM Utilisateur WHERE id = ?";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
        }
        return false;
    }

    // Récupération d'un utilisateur par son ID
    public Utilisateur getUtilisateurById(int id) {
        String query = "SELECT * FROM Utilisateur WHERE id = ?";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapUtilisateurFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        }
        return null;
    }

    // Liste de tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur ORDER BY nom, prenom";
        
        try {
        	Connection conn = ConnexionDB.getConnexion();
         	System.out.print("mrigla11");

             Statement stmt = conn.createStatement();
         	System.out.print("mrigla");

             ResultSet rs = stmt.executeQuery(query);
             
            
            while (rs.next()) {
                utilisateurs.add(mapUtilisateurFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
        }
        return utilisateurs;
    }

    // Authentification d'un utilisateur
    public Utilisateur authentifier(String login, String password) {
        String query = "SELECT u.* FROM Compte c JOIN Utilisateur u ON c.utilisateur_id = u.id " +
                      "WHERE c.login = ? AND c.password = SHA2(?, 256)";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapUtilisateurFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur d'authentification: " + e.getMessage());
        }
        return null;
    }

    // Méthode utilitaire pour mapper un ResultSet vers un objet Utilisateur
    private Utilisateur mapUtilisateurFromResultSet(ResultSet rs) throws SQLException {
        Utilisateur user = new Utilisateur();
        user.setId(rs.getInt("id"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setEmail(rs.getString("email"));
        user.setTelephone(rs.getString("telephone"));
        user.setCin(rs.getString("cin"));
        user.setService(rs.getString("service"));
        user.setRole(rs.getString("role"));
        user.setGrade(rs.getString("grade"));
        user.setDateNaissance(rs.getDate("date_naissance") != null ? 
                             rs.getDate("date_naissance").toLocalDate() : null);
        return user;
    }

    // Récupération des enseignants uniquement
    public List<Utilisateur> getEnseignants() {
        List<Utilisateur> enseignants = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur WHERE role = 'ENSEIGNANT' ORDER BY nom";
        
        try (Connection conn = ConnexionDB.getConnexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                enseignants.add(mapUtilisateurFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération enseignants: " + e.getMessage());
        }
        return enseignants;
    }
 // Ajoutez cette méthode à votre classe existante UtilisateurDAO

    /**
     * Récupère les enseignants d'un département spécifique
     * @param departement Nom du département
     * @return Liste des enseignants du département
     */
    public List<Utilisateur> getEnseignantsParDepartement(String departement) {
        List<Utilisateur> enseignants = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur WHERE role = 'ENSEIGNANT' AND service = ?";

        try (PreparedStatement stmt = ConnexionDB.prepareStatement(query)) {
            stmt.setString(1, departement);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Utilisateur enseignant = new Utilisateur();
                enseignant.setId(rs.getInt("id"));
                enseignant.setNom(rs.getString("nom"));
                enseignant.setPrenom(rs.getString("prenom"));
                enseignant.setEmail(rs.getString("email"));
                enseignant.setService(rs.getString("service"));
                enseignant.setRole(rs.getString("role"));
                enseignant.setGrade(rs.getString("grade"));
                
                enseignants.add(enseignant);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération enseignants par département: " + e.getMessage());
        }
        return enseignants;
    }
}