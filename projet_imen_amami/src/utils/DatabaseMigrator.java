package utils;

import dao.ConnexionDB;
import java.sql.*;

public class DatabaseMigrator {
    private static final String[] MIGRATIONS = {
        // Scripts de migration versionnés
        "CREATE TABLE IF NOT EXISTS Migration (version INT PRIMARY KEY)",
        "INSERT IGNORE INTO Migration VALUES (1)"
    };
    
    public static void migrer() {
        try (Connection conn = ConnexionDB.getConnexion()) {
            int versionActuelle = getVersion(conn);
            
            for (int i = versionActuelle; i < MIGRATIONS.length; i++) {
                executerMigration(conn, MIGRATIONS[i], i + 1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur migration DB: " + e.getMessage());
        }
    }
    
    private static int getVersion(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            if (!tableExists(conn, "Migration")) {
                return 0;
            }
            
            ResultSet rs = stmt.executeQuery("SELECT MAX(version) FROM Migration");
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, tableName, null);
        return rs.next();
    }
    
    private static void executerMigration(Connection conn, String sql, int version) throws SQLException {
        conn.setAutoCommit(false);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            // Mettre à jour la version
            if (version > 1) {
                stmt.execute("INSERT INTO Migration VALUES (" + version + ")");
            }
            
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}