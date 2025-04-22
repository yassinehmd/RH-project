package views;

import models.Utilisateur;
import services.AdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UtilisateurForm extends JDialog {
    private Utilisateur utilisateur;
    private AdminService adminService;
    
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtEmail;
    private JTextField txtCin;

    private JComboBox<String> comboRole;
    private JButton btnSauvegarder;
    
    public UtilisateurForm(JFrame parent, Utilisateur utilisateur) {
        super(parent, utilisateur == null ? "Nouvel utilisateur" : "Modifier utilisateur", true);
        this.utilisateur = utilisateur == null ? new Utilisateur() : utilisateur;
        this.adminService = new AdminService();
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        initUI();
    }
    
    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Nom
        panel.add(new JLabel("Nom:"));
        txtNom = new JTextField();
        panel.add(txtNom);
        
        // Prénom
        panel.add(new JLabel("Prénom:"));
        txtPrenom = new JTextField();
        panel.add(txtPrenom);
        
        // Email
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);
        //cin
        panel.add(new JLabel("CIN:"));
        txtCin = new JTextField();
        panel.add(txtCin);

        
        // Rôle
        panel.add(new JLabel("Rôle:"));
        String[] roles = {"ENSEIGNANT", "PERSONNEL", "RH", "ADMIN"};
        comboRole = new JComboBox<>(roles);
        panel.add(comboRole);
        
        // Si modification, charger les données
        if (utilisateur.getId() != 0) {
            txtNom.setText(utilisateur.getNom());
            txtPrenom.setText(utilisateur.getPrenom());
            txtEmail.setText(utilisateur.getEmail());
            txtCin.setText(utilisateur.getCin());
            comboRole.setSelectedItem(utilisateur.getRole());
        }
        
        // Bouton sauvegarder
        btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.addActionListener(this::sauvegarder);
        
        add(panel, BorderLayout.CENTER);
        add(btnSauvegarder, BorderLayout.SOUTH);
    }
    
    private void sauvegarder(ActionEvent e) {
        utilisateur.setNom(txtNom.getText());
        utilisateur.setPrenom(txtPrenom.getText());
        utilisateur.setEmail(txtEmail.getText());
        utilisateur.setCin(txtCin.getText());
        utilisateur.setRole((String) comboRole.getSelectedItem());
        
        if (adminService.sauvegarderUtilisateur(utilisateur)) {
            JOptionPane.showMessageDialog(this, "Utilisateur sauvegardé");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}