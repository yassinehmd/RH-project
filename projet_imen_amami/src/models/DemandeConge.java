package models;

import java.time.LocalDate;

public class DemandeConge {
    private int id;
    private String typeConge; // MALADIE, MATERNITE, ANNEE, SPECIAL
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut; // EN_ATTENTE, VALIDEE, REFUSEE
    private String motifRefus;
    private String certificatMedical;
    private Utilisateur enseignant;
    private Utilisateur responsableRH;
    
    // Constructeurs
    public DemandeConge() {
        this.statut = "EN_ATTENTE";
    }

    public DemandeConge(String typeConge, LocalDate dateDebut, LocalDate dateFin, Utilisateur enseignant) {
        this();
        this.typeConge = typeConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.enseignant = enseignant;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public String getCertificatMedical() {
        return certificatMedical;
    }

    public void setCertificatMedical(String certificatMedical) {
        this.certificatMedical = certificatMedical;
    }

    public Utilisateur getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Utilisateur enseignant) {
        this.enseignant = enseignant;
    }

    public Utilisateur getResponsableRH() {
        return responsableRH;
    }

    public void setResponsableRH(Utilisateur responsableRH) {
        this.responsableRH = responsableRH;
    }

    // Méthodes métier
    public boolean estValidee() {
        return "VALIDEE".equals(statut);
    }

    public boolean estRefusee() {
        return "REFUSEE".equals(statut);
    }

    public boolean estEnAttente() {
        return "EN_ATTENTE".equals(statut);
    }

    public long getDureeEnJours() {
        return dateFin.toEpochDay() - dateDebut.toEpochDay() + 1;
    }

    public boolean necessiteCertificat() {
        return "MALADIE".equals(typeConge) || "MATERNITE".equals(typeConge);
    }

    @Override
    public String toString() {
        return "DemandeConge{" +
                "id=" + id +
                ", type='" + typeConge + '\'' +
                ", période=" + dateDebut + " -> " + dateFin +
                ", statut='" + statut + '\'' +
                ", enseignant=" + (enseignant != null ? enseignant.getNomComplet() : "null") +
                '}';
    }
}