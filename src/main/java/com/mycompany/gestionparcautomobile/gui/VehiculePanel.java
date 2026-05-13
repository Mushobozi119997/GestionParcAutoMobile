package com.mycompany.gestionparcautomobile.gui;

import com.mycompany.gestionparcautomobile.dao.VehiculeDAO;
import com.mycompany.gestionparcautomobile.models.Vehicule;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panneau de gestion des véhicules
 * @author AFFABLE MUSHOBOZI
 */
public class VehiculePanel extends JPanel {

    private VehiculeDAO vehiculeDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtImmatriculation, txtMarque, txtModele, txtKilometrage;
    private JComboBox<String> cmbCarburant, cmbStatut;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private int selectedId = -1;

    //THEME PREMIUM DARK 
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
    
    // Police plus grande
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font COMBOBOX_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Couleurs Table
    private static final Color TABLE_BG = new Color(29, 29, 34);

    public VehiculePanel() {
        vehiculeDAO = new VehiculeDAO();
        initComponents();
        chargerDonnees();
        appliquerStyle();
    }

    private void appliquerStyle() {
        setBackground(BG_DARK);
        
        // Style pour les champs de texte (police plus grande)
        Component[] textFields = {txtImmatriculation, txtMarque, txtModele, txtKilometrage};
        for (Component c : textFields) {
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                field.setBackground(FIELD_BG);
                field.setForeground(TEXT_PRIMARY);
                field.setCaretColor(TEXT_PRIMARY);
                field.setFont(TEXT_FIELD_FONT);
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(8, 9, 7, 9))); 
            }
        }
        
        // Style pour les ComboBox (police plus grande)
        Component[] combos = {cmbCarburant, cmbStatut};
        for (Component c : combos) {
            if (c instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) c;
                combo.setBackground(FIELD_BG);
                combo.setForeground(TEXT_PRIMARY);
                combo.setFont(COMBOBOX_FONT);
                combo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)));
            }
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(13, 13));
        setBorder(BorderFactory.createEmptyBorder(13, 13, 13, 13));

        //  FORMULAIRE 
        JPanel formulairePanel = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true), 
                " FORMULAIRE VÉHICULE ");
        tb.setTitleColor(ACCENT_COLOR);
        tb.setTitleFont(TITLE_FONT);
        formulairePanel.setBorder(BorderFactory.createCompoundBorder(tb, 
                BorderFactory.createEmptyBorder(13, 23, 18, 23)));
        formulairePanel.setBackground(PANEL_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
                 // Espacement plus grand
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialisation des composants
        txtImmatriculation = new JTextField(13);
        txtMarque = new JTextField(13);
        txtModele = new JTextField(13);
        txtKilometrage = new JTextField(13);
        cmbCarburant = new JComboBox<>(new String[]{"Essence", "Diesel", "Hybride", "Electrique"});
        cmbStatut = new JComboBox<>(new String[]{"Disponible", "En mission", "En maintenance"});

        // Ajout des lignes du formulaire
        addGridRow(formulairePanel, "Immatriculation :", txtImmatriculation, 0, gbc);
        addGridRow(formulairePanel, "Marque :", txtMarque, 1, gbc);
        addGridRow(formulairePanel, "Modèle :", txtModele, 2, gbc);
        addGridRow(formulairePanel, "Carburant :", cmbCarburant, 3, gbc);
        addGridRow(formulairePanel, "Kilométrage :", txtKilometrage, 4, gbc);
        addGridRow(formulairePanel, "Statut :", cmbStatut, 5, gbc);

        //BOUTONS 
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        boutonsPanel.setOpaque(false);

        btnAjouter = createStyledButton(" Ajouter", SUCCESS_COLOR);
        btnModifier = createStyledButton(" Modifier", SUCCESS_COLOR);
        btnSupprimer = createStyledButton("Supprimer", SUCCESS_COLOR);
        btnActualiser = createStyledButton("Actualiser", SUCCESS_COLOR);

        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);

        boutonsPanel.add(btnAjouter);
        boutonsPanel.add(btnModifier);
        boutonsPanel.add(btnSupprimer);
        boutonsPanel.add(btnActualiser);

        //  TABLEAU 
        String[] colonnes = {"ID", "Immatriculation", "Marque", "Modèle", "Carburant", "Km", "Statut"};
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
        header.setBackground(BG_DARK);
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
                " LISTE DES VÉHICULES ", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                LABEL_FONT, TEXT_PRIMARY);
        scrollPane.setBorder(tableBorder);
        scrollPane.getViewport().setBackground(TABLE_BG);

        // ASSEMBLAGE 
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formulairePanel, BorderLayout.CENTER);
        topPanel.add(boutonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Actions des boutons
        btnAjouter.addActionListener(e -> ajouterVehicule());
        btnModifier.addActionListener(e -> modifierVehicule());
        btnSupprimer.addActionListener(e -> supprimerVehicule());
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(130, 40));
        
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
            List<Vehicule> vehicules = vehiculeDAO.listerVehicules();
            for (Vehicule v : vehicules) {
                tableModel.addRow(new Object[]{
                    v.getIdVehicule(), 
                    v.getImmatriculation(), 
                    v.getMarque(),
                    v.getModele(), 
                    v.getCarburant(), 
                    String.format("%,d", v.getKilometrage()), 
                  
                      // Formatage du km
                    v.getStatut()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row != -1) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtImmatriculation.setText((String) tableModel.getValueAt(row, 1));
            txtMarque.setText((String) tableModel.getValueAt(row, 2));
            txtModele.setText((String) tableModel.getValueAt(row, 3));
            cmbCarburant.setSelectedItem((String) tableModel.getValueAt(row, 4));
            
            // Récupération du kilométrage (gère le format avec virgules)
            String kmStr = (String) tableModel.getValueAt(row, 5);
            kmStr = kmStr.replace(",", "").replace(" ", "");
            txtKilometrage.setText(kmStr);
            
            cmbStatut.setSelectedItem((String) tableModel.getValueAt(row, 6));

            btnAjouter.setEnabled(false);
            btnModifier.setEnabled(true);
            btnSupprimer.setEnabled(true);
        }
    }

    private void ajouterVehicule() {
        // Validations
        if (txtImmatriculation.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'immatriculation est obligatoire !");
            txtImmatriculation.requestFocus();
            return;
        }
        if (txtMarque.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La marque est obligatoire !");
            txtMarque.requestFocus();
            return;
        }
        if (txtModele.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le modèle est obligatoire !");
            txtModele.requestFocus();
            return;
        }
        
        try {
            int kilometrage = Integer.parseInt(txtKilometrage.getText().trim());
            if (kilometrage < 0) {
                JOptionPane.showMessageDialog(this, "Le kilométrage ne peut pas être négatif !");
                return;
            }
            
            Vehicule v = new Vehicule(
                txtImmatriculation.getText().trim().toUpperCase(),
                txtMarque.getText().trim(),
                txtModele.getText().trim(),
                (String) cmbCarburant.getSelectedItem(),
                kilometrage,
                (String) cmbStatut.getSelectedItem()
            );
            
            vehiculeDAO.ajouterVehicule(v);
            JOptionPane.showMessageDialog(this, 
                " Véhicule ajouté avec succès !\n" +
                "Immatriculation : " + v.getImmatriculation(),
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            
            resetFormulaire();
            chargerDonnees();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le kilométrage doit être un nombre valide !");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, 
                    " Cette immatriculation existe déjà !\nVeuillez en saisir une autre.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifierVehicule() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule à modifier !");
            return;
        }
        
        try {
            int kilometrage = Integer.parseInt(txtKilometrage.getText().trim());
            if (kilometrage < 0) {
                JOptionPane.showMessageDialog(this, "Le kilométrage ne peut pas être négatif !");
                return;
            }
            
            Vehicule v = new Vehicule(
                selectedId,
                txtImmatriculation.getText().trim().toUpperCase(),
                txtMarque.getText().trim(),
                txtModele.getText().trim(),
                (String) cmbCarburant.getSelectedItem(),
                kilometrage,
                (String) cmbStatut.getSelectedItem()
            );
            
            vehiculeDAO.modifierVehicule(v);
            JOptionPane.showMessageDialog(this, 
                "Véhicule modifié avec succès !",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            
            resetFormulaire();
            chargerDonnees();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le kilométrage doit être un nombre valide !");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerVehicule() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule à supprimer !");
            return;
        }
        
        String immatriculation = txtImmatriculation.getText();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer le véhicule : " + immatriculation + " ?\nCette action est irréversible.",
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vehiculeDAO.supprimerVehicule(selectedId);
                JOptionPane.showMessageDialog(this, 
                    "Véhicule supprimé avec succès !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                resetFormulaire();
                chargerDonnees();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }

    private void resetFormulaire() {
        txtImmatriculation.setText("");
        txtMarque.setText("");
        txtModele.setText("");
        txtKilometrage.setText("");
        cmbCarburant.setSelectedIndex(0);
        cmbStatut.setSelectedIndex(0);
        selectedId = -1;
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        table.clearSelection();
        txtImmatriculation.requestFocus();
    }
}