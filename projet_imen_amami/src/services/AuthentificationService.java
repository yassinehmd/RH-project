package services;

import dao.ConnexionDB;
import models.Utilisateur;
import java.sql.*;

public class AuthentificationService {

    public Utilisateur authentifier(String login, String password) {
        Utilisateur utilisateur = null;
        String query = "SELECT u.*, c.password FROM Compte c JOIN Utilisateur u ON c.utilisateur_id = u.id "
                     + "WHERE c.login = ? AND c.password = SHA2(?, 256)"; // Ne pas hacher le mot de passe ici
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, login);
            stmt.setString(2, password); // Utilisation du mot de passe non haché
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setRole(rs.getString("role"));
                // ... autres attributs
            } else {
                // 🔎 Si l'utilisateur existe mais mot de passe incorrect
                String checkLoginQuery = "SELECT c.password FROM Compte c WHERE c.login = ?";
                try (PreparedStatement stmt2 = conn.prepareStatement(checkLoginQuery)) {
                    stmt2.setString(1, login);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        String truePassword = rs2.getString("password"); // Mot de passe en clair
                        System.out.println("Mot de passe incorrect.");
                        System.out.println("Vrai mot de passe : " + truePassword);
                    } else {
                        System.out.println("Login inexistant.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur d'authentification : " + e.getMessage());
        }

        return utilisateur;
    }
}
