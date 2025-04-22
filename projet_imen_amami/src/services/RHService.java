package services;

import dao.DemandeCongeDAO;
import dao.UtilisateurDAO;
import models.DemandeConge;
import models.Utilisateur;
import java.util.List;

public class RHService {
    private DemandeCongeDAO demandeCongeDAO;
    private UtilisateurDAO utilisateurDAO;

    public RHService() {
        this.demandeCongeDAO = new DemandeCongeDAO();
        this.utilisateurDAO = new UtilisateurDAO();
    }

    /**
     * Valider ou refuser une demande de congé
     * @param demandeId ID de la demande
     * @param decision "accepter" ou "refuser"
     * @param motif Motif du refus (peut être null si acceptation)
     * @param rhId ID du responsable RH qui traite la demande
     * @return true si l'opération a réussi
     */
    public boolean traiterDemandeConge(int demandeId, String decision, String motif, int rhId) {
        return demandeCongeDAO.traiterDemande(demandeId, decision, motif, rhId);
    }

    /**
     * Récupérer toutes les demandes en attente de validation
     * @return Liste des demandes en attente
     */
    public List<DemandeConge> getDemandesEnAttente() {
        return demandeCongeDAO.getDemandesEnAttente()
                .stream()
                .map(this::convertToDemandeConge)
                .toList();
    }

    /**
     * Récupérer l'historique des demandes traitées par un RH
     * @param rhId ID du responsable RH
     * @return Liste des demandes traitées
     */
    public List<DemandeConge> getHistoriqueDemandesTraitees(int rhId) {
        return demandeCongeDAO.getDemandesTraiteesParRH(rhId);
    }

    /**
     * Calculer le solde de congés restant pour un enseignant
     * @param enseignantId ID de l'enseignant
     * @return Nombre de jours de congés restants
     */
    public int calculerSoldeConge(int enseignantId) {
        List<DemandeConge> demandes = demandeCongeDAO.getDemandesValideesParEnseignant(enseignantId);
        int joursPris = demandes.stream()
                .mapToInt(d -> (int) d.getDureeEnJours())
                .sum();
        
        // 30 jours par an (à adapter selon vos règles)
        return 30 - joursPris;
    }

    /**
     * Générer un rapport des congés pour un département
     * @param departement Nom du département
     * @return Rapport formaté sous forme de String
     */
    public String genererRapportDepartement(String departement) {
        List<Utilisateur> enseignants = utilisateurDAO.getEnseignantsParDepartement(departement);
        StringBuilder rapport = new StringBuilder();
        
        rapport.append("Rapport des congés - Département ").append(departement).append("\n\n");
        
        for (Utilisateur enseignant : enseignants) {
            List<DemandeConge> demandes = demandeCongeDAO.getDemandesParEnseignant(enseignant.getId());
            int solde = calculerSoldeConge(enseignant.getId());
            
            rapport.append(enseignant.getNomComplet())
                  .append(" - Solde: ").append(solde).append(" jours\n");
            
            for (DemandeConge demande : demandes) {
                rapport.append("  - ")
                      .append(demande.getTypeConge())
                      .append(": ")
                      .append(demande.getDateDebut())
                      .append(" -> ")
                      .append(demande.getDateFin())
                      .append(" (")
                      .append(demande.getStatut())
                      .append(")\n");
            }
            rapport.append("\n");
        }
        
        return rapport.toString();
    }

    // Méthode utilitaire pour convertir un tableau en objet DemandeConge
    private DemandeConge convertToDemandeConge(String[] data) {
        DemandeConge demande = new DemandeConge();
        demande.setId(Integer.parseInt(data[0]));
        
        Utilisateur enseignant = new Utilisateur();
        enseignant.setNom(data[1]);
        enseignant.setPrenom(data[2]);
        demande.setEnseignant(enseignant);
        
        demande.setTypeConge(data[3]);
        demande.setDateDebut(java.sql.Date.valueOf(data[4]).toLocalDate());
        demande.setDateFin(java.sql.Date.valueOf(data[5]).toLocalDate());
        demande.setStatut("EN_ATTENTE");
        
        return demande;
    }
}