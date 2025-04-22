package services;

import dao.DemandeCongeDAO;
import models.DemandeConge;
import models.RepriseTravail;
import models.Utilisateur;
import java.util.List;

public class GestionCongeService {
    private DemandeCongeDAO demandeDao;
    
    public GestionCongeService() {
        this.demandeDao = new DemandeCongeDAO();
    }
    
    public boolean creerDemande(DemandeConge demande) {
        return demandeDao.creerDemande(demande);
    }
    
    public List<DemandeConge> getDemandesParEnseignant(int enseignantId) {
        return demandeDao.getDemandesParEnseignant(enseignantId);
    }
    
    public boolean traiterDemande(int demandeId, String statut, String motif, int rhId) {
        return demandeDao.traiterDemande(demandeId, statut, motif, rhId);
    }

    public boolean mettreAJourDemande(DemandeConge demande) {
        if (demande.getId() <= 0) {
            throw new IllegalArgumentException("ID de demande invalide");
        }
        return demandeDao.update(demande);
    }

	public boolean enregistrerReprise(RepriseTravail reprise) {
		// TODO Auto-generated method stub
		return false;
	}}