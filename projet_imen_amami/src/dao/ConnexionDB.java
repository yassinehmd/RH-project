package dao;

import utils.ConfigDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnexionDB {

    /**
     * Crée et retourne une nouvelle connexion à la base de données.
     * @return Une nouvelle connexion JDBC
     */
    public static Connection getConnexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                ConfigDB.getDbUrl(), 
                ConfigDB.getDbUser(), 
                ConfigDB.getDbPassword()
            );
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }

    /**
     * Prépare une déclaration SQL sécurisée avec gestion des ressources.
     * L'appelant est responsable de fermer le PreparedStatement et la Connexion.
     * @param query Requête SQL avec paramètres
     * @return PreparedStatement configuré
     * @throws SQLException Si erreur de préparation
     */
    public static PreparedStatement prepareStatement(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("La requête SQL ne peut être vide");
        }

        Connection conn = getConnexion();
        if (conn == null) {
            throw new SQLException("Impossible d'obtenir une connexion à la base de données.");
        }

        PreparedStatement stmt = conn.prepareStatement(
            query,
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY,
            ResultSet.HOLD_CURSORS_OVER_COMMIT
        );

        stmt.setQueryTimeout(30); // Timeout en secondes

        return stmt;
    }
}
