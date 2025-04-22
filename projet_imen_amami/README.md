# Gestion des Congés - ISAE Gafsa

## Configuration requise

- Java JDK 11+
- MySQL 8.0+
- Bibliothèques:
  - mysql-connector-java
  - (optionnel) itextpdf pour les exports PDF

## Installation

1. Importer la base de données: `database/script.sql`
2. Configurer les accès DB dans `config.properties`
3. Compiler le projet: `mvn clean install`

## Fonctionnalités

- Gestion des demandes de congé
- Upload de certificats médicaux
- Workflow de validation RH
- Tableau de bord admin
