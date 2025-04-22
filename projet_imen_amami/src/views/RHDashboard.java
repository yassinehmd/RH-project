package views;

import models.DemandeConge;
import models.Utilisateur;
import services.RHService;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RHDashboard extends JFrame {
    private Utilisateur responsableRH;
    private RHService rhService;
    
    private JTable tableDemandes;
    
    public RHDashboard(Utilisateur responsableRH) {
        this.responsableRH = responsableRH;
        this.rhService = new RHService();
        
        setTitle("Tableau de bord RH - " + responsableRH.getNom());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        chargerDemandesEnAttente();
    }
    
    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Onglets
        JTabbedPane onglets = new JTabbedPane();
        
        // Onglet demandes en attente
        JPanel panelAttente = new JPanel(new BorderLayout());
        tableDemandes = new JTable();
        panelAttente.add(new JScrollPane(tableDemandes), BorderLayout.CENTER);
        
        // Boutons de traitement
        JPanel panelBoutons = new JPanel();
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> traiterDemande(true));
        
        JButton btnRefuser = new JButton("Refuser");
        btnRefuser.addActionListener(e -> traiterDemande(false));
        
        panelBoutons.add(btnValider);
        panelBoutons.add(btnRefuser);
        panelAttente.add(panelBoutons, BorderLayout.SOUTH);
        
        onglets.addTab("Demandes en attente", panelAttente);
        
        panel.add(onglets, BorderLayout.CENTER);
        add(panel);
    }
    
    private void chargerDemandesEnAttente() {
        List<DemandeConge> demandes = rhService.getDemandesEnAttente();
        
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
    
    
    
    
    
    
    
    
    
    
    
    private void traiterDemande(boolean validation) {
        int selectedRow = tableDemandes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une demande");
            return;
        }
        
        int demandeId = (int) tableDemandes.getValueAt(selectedRow, 0);
        String motif = null;
        
        if (!validation) {
            motif = JOptionPane.showInputDialog(this, "Motif du refus:");
            if (motif == null || motif.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Un motif est requis pour le refus");
                return;
            }
        }
        
        boolean success = rhService.traiterDemandeConge(demandeId,validation ? "VALIDEE" : "REFUSEE",motif,responsableRH.getId());
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Demande traitée avec succès");
            chargerDemandesEnAttente();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors du traitement", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}