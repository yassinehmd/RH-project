package services;

import java.sql.*;

public class PrimeService {

    private Connection connection;

    public PrimeService(Connection connection) {
        this.connection = connection;
    }

    public void calculerEtMettreAJourPrime(int utilisateurId) throws SQLException {
        String query = "SELECT DATEDIFF(date_fin, date_debut) + 1 AS nb_jours " +
                       "FROM demandeconge WHERE enseignant_id = ? AND justifie = FALSE";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, utilisateurId);
        ResultSet rs = ps.executeQuery();

        int joursNonJustifies = 0;
        while (rs.next()) {
            joursNonJustifies += rs.getInt("nb_jours");
        }

        double primePourcentage;
        if (joursNonJustifies <= 2) {
            primePourcentage = 1.0;
        } else if (joursNonJustifies <= 5) {
            primePourcentage = 0.7;
        } else {
            primePourcentage = 0.0;
        }

        double montantBase = 1000.0; // À ajuster selon l'institut
        double primeCalculee = montantBase * primePourcentage;

        String update = "UPDATE utilisateur SET prime = ? WHERE id = ?";
        PreparedStatement psUpdate = connection.prepareStatement(update);
        psUpdate.setDouble(1, primeCalculee);
        psUpdate.setInt(2, utilisateurId);
        psUpdate.executeUpdate();

        System.out.println("Prime mise à jour pour utilisateur ID " + utilisateurId + " : " + primeCalculee + " DT");
    }
}
