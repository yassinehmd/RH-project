package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.DemandeConge;
import models.Utilisateur;

public class DemandeCongeDAO {
    private Connection connexion;

    public DemandeCongeDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gestion_conges_isae", 
                "root", 
                "");
        } catch (Exception e) {
            System.err.println("Erreur connexion DB: " + e.getMessage());
        }
    }

    // 1. Récupérer les demandes en attente (version String[])
    public List<String[]> getDemandesEnAttente() {
        List<String[]> demandes = new ArrayList<>();
        String query = "SELECT d.id, u.nom, u.prenom, d.type_conge, d.date_debut, d.date_fin " +
                      "FROM DemandeConge d JOIN Utilisateur u ON d.enseignant_id = u.id " +
                      "WHERE d.statut = 'EN_ATTENTE'";

        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String[] demande = new String[6];
                demande[0] = String.valueOf(rs.getInt("id"));
                demande[1] = rs.getString("nom");
                demande[2] = rs.getString("prenom");
                demande[3] = rs.getString("type_conge");
                demande[4] = rs.getDate("date_debut").toString();
                demande[5] = rs.getDate("date_fin").toString();
                demandes.add(demande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération demandes: " + e.getMessage());
        }
        return demandes;
    }

    // 2. Créer une nouvelle demande (version avec paramètres simples)
    public boolean creerDemande(int enseignantId, String type, String dateDebut, String dateFin) {
        String query = "INSERT INTO DemandeConge (type_conge, date_debut, date_fin, statut, enseignant_id) " +
                      "VALUES (?, ?, ?, 'EN_ATTENTE', ?)";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {
            stmt.setString(1, type);
            stmt.setString(2, dateDebut);
            stmt.setString(3, dateFin);
            stmt.setInt(4, enseignantId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création demande: " + e.getMessage());
            return false;
        }
    }

    // 3. Créer une demande (version avec objet DemandeConge)
    public boolean creerDemande(DemandeConge demande) {
        String query = "INSERT INTO DemandeConge (type_conge, date_debut, date_fin, statut, enseignant_id) " +
                      "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, demande.getTypeConge());
            stmt.setDate(2, Date.valueOf(demande.getDateDebut()));
            stmt.setDate(3, Date.valueOf(demande.getDateFin()));
            stmt.setString(4, demande.getStatut());
            stmt.setInt(5, demande.getEnseignant().getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        demande.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur création demande: " + e.getMessage());
            return false;
        }
    }

    // 4. Traiter une demande (validation/refus)
    public boolean traiterDemande(int demandeId, String decision, String motif, int rhId) {
        String query = "UPDATE DemandeConge SET statut = ?, motif_refus = ?, responsable_rh_id = ? " +
                      "WHERE id = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {
            stmt.setString(1, decision.equals("accepter") ? "VALIDEE" : "REFUSEE");
            stmt.setString(2, decision.equals("accepter") ? null : motif);
            stmt.setInt(3, rhId);
            stmt.setInt(4, demandeId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur traitement demande: " + e.getMessage());
            return false;
        }
    }

    // 5. Récupérer les demandes d'un enseignant
    public List<DemandeConge> getDemandesParEnseignant(int enseignantId) {
        List<DemandeConge> demandes = new ArrayList<>();
        String query = "SELECT d.*, u.nom, u.prenom FROM DemandeConge d " +
                      "JOIN Utilisateur u ON d.enseignant_id = u.id " +
                      "WHERE d.enseignant_id = ? ORDER BY d.date_debut DESC";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                DemandeConge demande = new DemandeConge();
                demande.setId(rs.getInt("id"));
                demande.setTypeConge(rs.getString("type_conge"));
                demande.setDateDebut(rs.getDate("date_debut").toLocalDate());
                demande.setDateFin(rs.getDate("date_fin").toLocalDate());
                demande.setStatut(rs.getString("statut"));
                
                Utilisateur enseignant = new Utilisateur();
                enseignant.setId(enseignantId);
                enseignant.setNom(rs.getString("nom"));
                enseignant.setPrenom(rs.getString("prenom"));
                demande.setEnseignant(enseignant);
                
                demandes.add(demande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération demandes: " + e.getMessage());
        }
        return demandes;
    }

 // Ajoutez ces méthodes à votre classe existante DemandeCongeDAO

    /**
     * Récupère les demandes traitées par un responsable RH
     * @param rhId ID du responsable RH
     * @return Liste des demandes traitées
     */
    public List<DemandeConge> getDemandesTraiteesParRH(int rhId) {
        List<DemandeConge> demandes = new ArrayList<>();
        String query = "SELECT d.*, u.nom, u.prenom FROM DemandeConge d " +
                      "JOIN Utilisateur u ON d.enseignant_id = u.id " +
                      "WHERE d.responsable_rh_id = ? ORDER BY d.date_debut DESC";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {
            stmt.setInt(1, rhId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                DemandeConge demande = new DemandeConge();
                demande.setId(rs.getInt("id"));
                demande.setTypeConge(rs.getString("type_conge"));
                demande.setDateDebut(rs.getDate("date_debut").toLocalDate());
                demande.setDateFin(rs.getDate("date_fin").toLocalDate());
                demande.setStatut(rs.getString("statut"));
                demande.setMotifRefus(rs.getString("motif_refus"));
                
                Utilisateur enseignant = new Utilisateur();
                enseignant.setId(rs.getInt("enseignant_id"));
                enseignant.setNom(rs.getString("nom"));
                enseignant.setPrenom(rs.getString("prenom"));
                demande.setEnseignant(enseignant);
                
                demandes.add(demande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération demandes traitées: " + e.getMessage());
        }
        return demandes;
    }

    /**
     * Récupère les demandes validées d'un enseignant
     * @param enseignantId ID de l'enseignant
     * @return Liste des demandes validées
     */
    public List<DemandeConge> getDemandesValideesParEnseignant(int enseignantId) {
        List<DemandeConge> demandes = new ArrayList<>();
        String query = "SELECT d.* FROM DemandeConge d " +
                      "WHERE d.enseignant_id = ? AND d.statut = 'VALIDEE' " +
                      "ORDER BY d.date_debut DESC";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                DemandeConge demande = new DemandeConge();
                demande.setId(rs.getInt("id"));
                demande.setTypeConge(rs.getString("type_conge"));
                demande.setDateDebut(rs.getDate("date_debut").toLocalDate());
                demande.setDateFin(rs.getDate("date_fin").toLocalDate());
                demande.setStatut(rs.getString("statut"));
                
                Utilisateur enseignant = new Utilisateur();
                enseignant.setId(enseignantId);
                demande.setEnseignant(enseignant);
                
                demandes.add(demande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération demandes validées: " + e.getMessage());
        }
        return demandes;
    }

    /**
     * Ajoute un certificat médical à une demande de congé
     * @param demandeId ID de la demande de congé
     * @param savedPath Chemin d'accès relatif du fichier sauvegardé
     * @return true si l'opération a réussi, false sinon
     */
    public boolean ajouterCertificat(int demandeId, String savedPath) {
        String query = "UPDATE DemandeConge SET certificat_medical = ? WHERE id = ?";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, savedPath);
            stmt.setInt(2, demandeId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException e) {
            
            return false;
        }
    }

	public boolean update(DemandeConge demande) {
		// TODO Auto-generated method stub
		return false;
	}
}