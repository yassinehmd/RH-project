package views;

import models.Utilisateur;
import services.AdminService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {
    private final Utilisateur admin;
    private final AdminService adminService;

    private JTabbedPane onglets;
    private JTable tableUtilisateurs;

    public AdminDashboard(Utilisateur admin) {
        this.admin = admin;
        this.adminService = new AdminService();

        setTitle("Tableau de bord Admin - " + admin.getNom());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        onglets = new JTabbedPane();

        // Onglet Utilisateurs
        JPanel panelUtilisateurs = new JPanel(new BorderLayout());
        initGestionUtilisateurs(panelUtilisateurs);
        onglets.addTab("Utilisateurs", panelUtilisateurs);

        // Onglet Paramètres
        JPanel panelParametres = new JPanel(new BorderLayout());
        initGestionParametres(panelParametres);
        onglets.addTab("Paramètres", panelParametres);

        add(onglets);
    }

    private void initGestionUtilisateurs(JPanel panel) {
        tableUtilisateurs = new JTable();
        adminService.chargerUtilisateurs(tableUtilisateurs);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(this::ajouterUtilisateur);
        btnModifier.addActionListener(e -> modifierUtilisateur(tableUtilisateurs));
        btnSupprimer.addActionListener(e -> supprimerUtilisateur(tableUtilisateurs));

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        panel.add(new JScrollPane(tableUtilisateurs), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initGestionParametres(JPanel panel) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Jours fériés
        formPanel.add(new JLabel("Jours fériés:"));
        JTextArea txtJoursFeries = new JTextArea(5, 20);
        txtJoursFeries.setText(adminService.getJoursFeries());
        formPanel.add(new JScrollPane(txtJoursFeries));

        // Durée maternité
        formPanel.add(new JLabel("Durée maternité (jours):"));
        JSpinner spinnerMaternite = new JSpinner(new SpinnerNumberModel(
                adminService.getDureeMaternite(), 1, 365, 1));
        formPanel.add(spinnerMaternite);

        // Bouton sauvegarde
        JButton btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.addActionListener(e -> {
            adminService.sauvegarderParametres(
                    txtJoursFeries.getText(),
                    (int) spinnerMaternite.getValue()
            );
            JOptionPane.showMessageDialog(this, "Paramètres sauvegardés");
        });

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(btnSauvegarder, BorderLayout.SOUTH);
    }

    private void ajouterUtilisateur(ActionEvent e) {
        new UtilisateurForm(this, null).setVisible(true);
        adminService.chargerUtilisateurs(tableUtilisateurs);
    }

    private void modifierUtilisateur(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        new UtilisateurForm(this, adminService.getUtilisateur(id)).setVisible(true);
        adminService.chargerUtilisateurs(table);
    }

    private void supprimerUtilisateur(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirmer la suppression ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (adminService.supprimerUtilisateur(id)) {
                JOptionPane.showMessageDialog(this, "Utilisateur supprimé");
                adminService.chargerUtilisateurs(table);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
