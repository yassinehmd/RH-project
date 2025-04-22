package models;

import java.time.LocalDate;
import java.util.List;

public class ParametresConge {
    private int id;
    private int annee;
    private List<LocalDate> joursFeries;
    private int dureeMaternite;
    private int dureeMaladieCourte;
    
    public ParametresConge() {
        this.dureeMaternite = 98; // Valeurs par défaut
        this.dureeMaladieCourte = 15;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    
    public List<LocalDate> getJoursFeries() { return joursFeries; }
    public void setJoursFeries(List<LocalDate> joursFeries) { 
        this.joursFeries = joursFeries; 
    }
    
    public int getDureeMaternite() { return dureeMaternite; }
    public void setDureeMaternite(int dureeMaternite) { 
        this.dureeMaternite = dureeMaternite; 
    }
    
    public int getDureeMaladieCourte() { return dureeMaladieCourte; }
    public void setDureeMaladieCourte(int dureeMaladieCourte) { 
        this.dureeMaladieCourte = dureeMaladieCourte; 
    }
}