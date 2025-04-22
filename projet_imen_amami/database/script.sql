-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_conges_isae;
USE gestion_conges_isae;

-- Table Utilisateur
CREATE TABLE Utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    cin VARCHAR(20) UNIQUE NOT NULL,
    service VARCHAR(50),
    role ENUM('ENSEIGNANT', 'PERSONNEL', 'RH', 'ADMIN') NOT NULL,
    grade VARCHAR(50),
    date_naissance DATE
);

-- Table Compte
CREATE TABLE Compte (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    utilisateur_id INT UNIQUE NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id)
);

-- Table DemandeConge
CREATE TABLE DemandeConge (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_conge ENUM('MALADIE', 'MATERNITE', 'ANNEE', 'SPECIAL') NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut ENUM('EN_ATTENTE', 'VALIDEE', 'REFUSEE') DEFAULT 'EN_ATTENTE',
    motif_refus TEXT,
    certificat_medical VARCHAR(255),
    enseignant_id INT NOT NULL,
    responsable_rh_id INT,
    FOREIGN KEY (enseignant_id) REFERENCES Utilisateur(id),
    FOREIGN KEY (responsable_rh_id) REFERENCES Utilisateur(id)
);

-- Table RepriseTravail
CREATE TABLE RepriseTravail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_reprise DATE NOT NULL,
    demande_conge_id INT UNIQUE NOT NULL,
    FOREIGN KEY (demande_conge_id) REFERENCES DemandeConge(id)
);

-- Table ParametresConge
CREATE TABLE ParametresConge (
    id INT AUTO_INCREMENT PRIMARY KEY,
    annee INT NOT NULL,
    jours_feries TEXT,
    duree_maternite INT DEFAULT 98,
    duree_maladie_courte INT DEFAULT 15
);

-- Insertion des utilisateurs initiaux
INSERT INTO Utilisateur (nom, prenom, email, cin, role, grade) VALUES 
('Admin', 'System', 'admin@isaeg.u-gafsa.tn', '00000000', 'ADMIN', null);

INSERT INTO Compte (login, password, utilisateur_id) VALUES 
('admin', SHA2('admin123', 256), 1);