/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.gui;

import com.mycompany.gestionparcautomobile.dao.ChauffeurDAO;
import com.mycompany.gestionparcautomobile.models.Chauffeur;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panneau de gestion des chauffeurs 
 * @author AFFABLE MUSHOBOZI
 */
public class ChauffeurPanel extends JPanel {
    
    private ChauffeurDAO chauffeurDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrenom, txtNumPermis, txtTelephone;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private int selectedId = -1;
    
    // THEME PREMIUM DARK 
    private static final Color BG_DARK = new Color(24, 24, 27);
    private static final Color PANEL_BG = new Color(34, 34, 38);
    private static final Color FIELD_BG = new Color(45, 45, 50);
    private static final Color TEXT_PRIMARY = new Color(244, 244, 245);
    private static final Color ACCENT_COLOR = new Color(99, 102, 241);
    private static final Color BORDER_COLOR = new Color(63, 63, 70);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color INFO_COLOR = new Color(59, 130, 246);
    
    // Police plus grande pour les champs de texte
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    // Couleurs Table
    private static final Color TABLE_BG = new Color(30, 30, 35);
    private static final Color TABLE_HEADER_BG = BG_DARK;
    
    public ChauffeurPanel() {
        chauffeurDAO = new ChauffeurDAO();
        initComponents();
        chargerDonnees();
        appliquerStyle();
    }
    
    private void appliquerStyle() {
        setBackground(BG_DARK);
        
        // Style pour les champs de texte (police plus grande)
        Component[] textFields = {txtNom, txtPrenom, txtNumPermis, txtTelephone};
        for (Component c : textFields) {
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                field.setBackground(FIELD_BG);
                field.setForeground(TEXT_PRIMARY);
                field.setCaretColor(TEXT_PRIMARY);
                field.setFont(TEXT_FIELD_FONT);  
               
              // Police plus grande
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));
                // Padding plus grand
            }
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //FORMULAIRE 
        JPanel formulairePanel = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true), 
                " FORMULAIRE CHAUFFEUR ");
        tb.setTitleColor(ACCENT_COLOR);
        tb.setTitleFont(TITLE_FONT);
        formulairePanel.setBorder(BorderFactory.createCompoundBorder(tb, 
                BorderFactory.createEmptyBorder(15, 25, 20, 25)));
        formulairePanel.setBackground(PANEL_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  
   // Espacement plus grand
   
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Initialisation des composants
        txtNom = new JTextField(15);
        txtPrenom = new JTextField(15);
        txtNumPermis = new JTextField(15);
        txtTelephone = new JTextField(15);
        
        // Ajout des lignes du formulaire
        addGridRow(formulairePanel, "Nom :", txtNom, 0, gbc);
        addGridRow(formulairePanel, "Prénom :", txtPrenom, 1, gbc);
        addGridRow(formulairePanel, "Numéro de permis :", txtNumPermis, 2, gbc);
        addGridRow(formulairePanel, "Téléphone :", txtTelephone, 3, gbc);
        
        //BOUTONS 
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        boutonsPanel.setOpaque(false);
        
        btnAjouter = createStyledButton("Ajouter", SUCCESS_COLOR);
        btnModifier = createStyledButton("Modifier", SUCCESS_COLOR);
        btnSupprimer = createStyledButton("🗑️ Supprimer", SUCCESS_COLOR);
        btnActualiser = createStyledButton(" Actualiser", SUCCESS_COLOR);
        
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        
        boutonsPanel.add(btnAjouter);
        boutonsPanel.add(btnModifier);
        boutonsPanel.add(btnSupprimer);
        boutonsPanel.add(btnActualiser);
        
        // TABLEAU 
        String[] colonnes = {"ID", "Nom", "Prénom", "N° Permis", "Téléphone"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override 
            public boolean isCellEditable(int r, int c) { 
                return false; 
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);  // Hauteur de ligne augmentée
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));  // Police du tableau plus grande
        
        // Alignement des données des cellules au centre
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Style et alignement de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(ACCENT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Alignement central du texte de l'en-tête
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Écouteur de sélection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirFormulaire();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder tableBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), 
                " LISTE DES CHAUFFEURS ", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                LABEL_FONT, TEXT_PRIMARY);
        scrollPane.setBorder(tableBorder);
        scrollPane.getViewport().setBackground(TABLE_BG);
        
        //ASSEMBLAGE
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formulairePanel, BorderLayout.CENTER);
        topPanel.add(boutonsPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Actions des boutons
        btnAjouter.addActionListener(e -> ajouterChauffeur());
        btnModifier.addActionListener(e -> modifierChauffeur());
        btnSupprimer.addActionListener(e -> supprimerChauffeur());
        btnActualiser.addActionListener(e -> {
            chargerDonnees();
            resetFormulaire();
        });
    }
    
    private void addGridRow(JPanel panel, String labelText, Component component, 
                            int row, GridBagConstraints gbc) {
        gbc.gridy = row;
        
        // Label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(LABEL_FONT);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(label, gbc);
        
        // Composant
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(component, gbc);
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));  // Police des boutons plus grande
        button.setPreferredSize(new Dimension(130, 40));  // Boutons plus grands
        
        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            List<Chauffeur> chauffeurs = chauffeurDAO.listerChauffeurs();
            for (Chauffeur c : chauffeurs) {
                Object[] ligne = {
                    c.getIdChauffeur(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getNumPermis(),
                    c.getTelephone()
                };
                tableModel.addRow(ligne);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des chauffeurs : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row != -1) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtNom.setText((String) tableModel.getValueAt(row, 1));
            txtPrenom.setText((String) tableModel.getValueAt(row, 2));
            txtNumPermis.setText((String) tableModel.getValueAt(row, 3));
            txtTelephone.setText((String) tableModel.getValueAt(row, 4));
            
            btnAjouter.setEnabled(false);
            btnModifier.setEnabled(true);
            btnSupprimer.setEnabled(true);
        }
    }
    
    private void ajouterChauffeur() {
        // Validation des champs
        if (txtNom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire !");
            txtNom.requestFocus();
            return;
        }
        if (txtNumPermis.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le numéro de permis est obligatoire !");
            txtNumPermis.requestFocus();
            return;
        }
        
        try {
            Chauffeur c = new Chauffeur(
                txtNom.getText().trim().toUpperCase(),
                txtPrenom.getText().trim(),
                txtNumPermis.getText().trim().toUpperCase(),
                txtTelephone.getText().trim()
            );
            
            chauffeurDAO.ajouterChauffeur(c);
            JOptionPane.showMessageDialog(this, 
                "Chauffeur ajouté avec succès !\n" +
                "Nom : " + c.getNom() + " " + c.getPrenom(),
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            resetFormulaire();
            chargerDonnees();
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, 
                    "Ce numéro de permis existe déjà !\nVeuillez en saisir un autre.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }
    
    private void modifierChauffeur() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un chauffeur à modifier !");
            return;
        }
        
        try {
            Chauffeur c = new Chauffeur(
                selectedId,
                txtNom.getText().trim().toUpperCase(),
                txtPrenom.getText().trim(),
                txtNumPermis.getText().trim().toUpperCase(),
                txtTelephone.getText().trim()
            );
            
            chauffeurDAO.modifierChauffeur(c);
            JOptionPane.showMessageDialog(this, 
                " Chauffeur modifié avec succès !",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            resetFormulaire();
            chargerDonnees();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
    
    private void supprimerChauffeur() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un chauffeur à supprimer !");
            return;
        }
        
        String nomComplet = txtNom.getText() + " " + txtPrenom.getText();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer le chauffeur : " + nomComplet + " ?\nCette action est irréversible.",
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                chauffeurDAO.supprimerChauffeur(selectedId);
                JOptionPane.showMessageDialog(this, 
                    "✓ Chauffeur supprimé avec succès !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                resetFormulaire();
                chargerDonnees();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }
    
    private void resetFormulaire() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtNumPermis.setText("");
        txtTelephone.setText("");
        selectedId = -1;
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        table.clearSelection();
        txtNom.requestFocus();
    }
}