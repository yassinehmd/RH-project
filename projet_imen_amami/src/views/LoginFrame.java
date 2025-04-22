package views;

import models.Utilisateur;
import services.AuthentificationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Authentification - Gestion des congés ISAE");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Login Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Login:"), gbc);

        // Login Field
        gbc.gridx = 1;
        txtLogin = new JTextField(20);
        panel.add(txtLogin, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Mot de passe:"), gbc);

        // Password Field
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnLogin = new JButton("Se connecter");
        btnLogin.addActionListener(this::authentifier);
        panel.add(btnLogin, gbc);

        add(panel);
    }

    private void authentifier(ActionEvent e) {
        String login = txtLogin.getText();
        String password = new String(txtPassword.getPassword());

        AuthentificationService authService = new AuthentificationService();
        Utilisateur utilisateur = authService.authentifier(login, password);

        if (utilisateur != null) {
            JOptionPane.showMessageDialog(this, "Bienvenue " + utilisateur.getNom());
            ouvrirDashboard(utilisateur);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ouvrirDashboard(Utilisateur utilisateur) {
        switch (utilisateur.getRole()) {
            case "ENSEIGNANT":
                new EnseignantDashboard(utilisateur).setVisible(true);
                break;
            case "RH":
                new RHDashboard(utilisateur).setVisible(true);
                break;
            case "ADMIN":
                new AdminDashboard(utilisateur).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Rôle non reconnu", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
