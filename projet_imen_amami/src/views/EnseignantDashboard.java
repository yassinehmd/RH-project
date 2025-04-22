package views;

import models.DemandeConge;
import models.Utilisateur;
import services.GestionCongeService;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EnseignantDashboard extends JFrame {
    private Utilisateur enseignant;
    private GestionCongeService congeService;
    
    private JTable tableDemandes;
    private JButton btnNouvelleDemande;
    
    public EnseignantDashboard(Utilisateur enseignant) {
        this.enseignant = enseignant;
        this.congeService = new GestionCongeService();
        
        setTitle("Tableau de bord - " + enseignant.getNom());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        chargerDemandes();
    }
    
    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel supérieur avec informations
        JPanel infoPanel = new JPanel(new GridLayout(0, 2));
        infoPanel.add(new JLabel("Nom: " + enseignant.getNom()));
        infoPanel.add(new JLabel("Prénom: " + enseignant.getPrenom()));
        infoPanel.add(new JLabel("Grade: " + enseignant.getGrade()));
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Tableau des demandes
        String[] columns = {"ID", "Type", "Début", "Fin", "Statut"};
        Object[][] data = {};
        tableDemandes = new JTable(data, columns);
        panel.add(new JScrollPane(tableDemandes), BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel();
        btnNouvelleDemande = new JButton("Nouvelle demande");
        btnNouvelleDemande.addActionListener(e -> ouvrirFormulaireDemande());
        buttonPanel.add(btnNouvelleDemande);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void chargerDemandes() {
        List<DemandeConge> demandes = congeService.getDemandesParEnseignant(enseignant.getId());
        
        Object[][] data = new Object[demandes.size()][5];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (int i = 0; i < demandes.size(); i++) {
            DemandeConge d = demandes.get(i);
            data[i][0] = d.getId();
            data[i][1] = d.getTypeConge();
            data[i][2] = d.getDateDebut().format(formatter);
            data[i][3] = d.getDateFin().format(formatter);
            data[i][4] = d.getStatut();
        }
        
        tableDemandes.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String[] {"ID", "Type", "Début", "Fin", "Statut"}
        ));
    }
    
    private void ouvrirFormulaireDemande() {
        // Implémenter le formulaire de création de demande
        JOptionPane.showMessageDialog(this, "Fonctionnalité à implémenter");
    }
}