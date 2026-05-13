package com.mycompany.gestionparcautomobile.gui;

import com.mycompany.gestionparcautomobile.dao.MaintenanceDAO;
import com.mycompany.gestionparcautomobile.dao.VehiculeDAO;
import com.mycompany.gestionparcautomobile.models.Maintenance;
import com.mycompany.gestionparcautomobile.models.Vehicule;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panneau de gestion des maintenance 
 * @author AFFABLE MUSHOBOZI
 */
public class MaintenancePanel extends JPanel {

    private MaintenanceDAO maintenanceDAO;
    private VehiculeDAO vehiculeDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<VehiculeItem> cmbVehicule;
    private JComboBox<String> cmbTypeIntervention;
    private JComboBox<String> cmbDevise;  
    private JTextField txtDateIntervention;
    private JTextField txtCoutTotal;
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
    
    // Couleurs pour les ComboBox (NOIR sur BLANC)
    private static final Color COMBOBOX_TEXT_COLOR = Color.BLACK;
    private static final Color COMBOBOX_BG_COLOR = Color.WHITE;
    
    // Couleurs Table
    private static final Color TABLE_BG = new Color(30, 30, 35);
    private static final Color TABLE_ROW_ALT = new Color(39, 39, 42);
    private static final Color TABLE_HEADER_BG = BG_DARK;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final double TAUX_EUR_TO_FC = 2500.0; 
    private static final double TAUX_EUR_TO_USD = 1.18;  

    private static class VehiculeItem {
        Vehicule vehicule;
        VehiculeItem(Vehicule v) { vehicule = v; }
        @Override
        public String toString() { return vehicule.getImmatriculation() + " - " + vehicule.getMarque() + " " + vehicule.getModele(); }
        public Vehicule getVehicule() { return vehicule; }
    }

    // combobox
    
    private class VehiculeListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            // Fond blanc pour la liste déroulante
            list.setBackground(COMBOBOX_BG_COLOR);
            list.setSelectionBackground(ACCENT_COLOR);
            
            if (value instanceof VehiculeItem) {
                VehiculeItem item = (VehiculeItem) value;
                Vehicule v = item.getVehicule();
                label.setText("  " + v.getImmatriculation() + " - " + v.getMarque() + " " + v.getModele());
                label.setForeground(COMBOBOX_TEXT_COLOR);
            } else if (value == null) {
                label.setText("Sélectionner un véhicule");
                label.setForeground(Color.GRAY);
            }
            
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            
            if (isSelected) {
                label.setBackground(ACCENT_COLOR);
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(COMBOBOX_BG_COLOR);
                label.setForeground(COMBOBOX_TEXT_COLOR);
            }
            
            return label;
        }
    }

    public MaintenancePanel() {
        maintenanceDAO = new MaintenanceDAO();
        vehiculeDAO = new VehiculeDAO();
        initComponents();
        chargerDonnees();
        chargerComboBox();
        appliquerStyle();
    }

    private void appliquerStyle() {
        setBackground(BG_DARK);
        
        // Style pour les champs de texte (fond sombre, texte blanc)
        Component[] textFields = {txtDateIntervention, txtCoutTotal};
        for (Component c : textFields) {
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                field.setBackground(FIELD_BG);
                field.setForeground(TEXT_PRIMARY);
                field.setCaretColor(TEXT_PRIMARY);
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)));
            }
        }
        
        // Style pour les ComboBox (fond BLANC, texte NOIR)
        
        cmbVehicule.setBackground(COMBOBOX_BG_COLOR);
        cmbVehicule.setForeground(COMBOBOX_TEXT_COLOR);
        cmbVehicule.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        
        cmbTypeIntervention.setBackground(COMBOBOX_BG_COLOR);
        cmbTypeIntervention.setForeground(COMBOBOX_TEXT_COLOR);
        cmbTypeIntervention.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        
        cmbDevise.setBackground(COMBOBOX_BG_COLOR);
        cmbDevise.setForeground(COMBOBOX_TEXT_COLOR);
        cmbDevise.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)));
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // FORMULAIRE 
        JPanel formulairePanel = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true), 
                " FORMULAIRE MAINTENANCE ");
        tb.setTitleColor(ACCENT_COLOR);
        tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        formulairePanel.setBorder(BorderFactory.createCompoundBorder(tb, 
                BorderFactory.createEmptyBorder(10, 20, 15, 20)));
        formulairePanel.setBackground(PANEL_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);

        // Initialisation des composants
        cmbVehicule = new JComboBox<>();
        String[] typesIntervention = {"Vidange", "Pneus", "Freins", "Courroie", "Batterie", 
                                       "Embrayage", "Amortisseurs", "Échappement", "Climatisation", "Révision générale"};
        cmbTypeIntervention = new JComboBox<>(typesIntervention);
        cmbTypeIntervention.setEditable(true);
        
        txtDateIntervention = new JTextField(LocalDate.now().format(DATE_FORMATTER));
        txtCoutTotal = new JTextField();
        
        String[] devises = {"€ (Euro)", "$ (Dollar US)", "FC (Franc Congolais)"};
        cmbDevise = new JComboBox<>(devises);

        // Appliquer le renderer pour la ComboBox véhicule
        cmbVehicule.setRenderer(new VehiculeListRenderer());

        // Création du panneau pour le coût + devise (côte à côte)
        JPanel coutPanel = new JPanel(new BorderLayout(8, 0));
        coutPanel.setOpaque(false);
        coutPanel.add(txtCoutTotal, BorderLayout.CENTER);
        coutPanel.add(cmbDevise, BorderLayout.EAST);

        // Ajout des lignes du formulaire
        addGridRow(formulairePanel, "Véhicule :", cmbVehicule, 0, gbc, labelFont);
        addGridRow(formulairePanel, "Type d'intervention :", cmbTypeIntervention, 1, gbc, labelFont);
        addGridRow(formulairePanel, "Date (AAAA-MM-JJ) :", txtDateIntervention, 2, gbc, labelFont);
        addGridRow(formulairePanel, "Coût total :", coutPanel, 3, gbc, labelFont);

        // les  BOUTONS 
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        boutonsPanel.setOpaque(false);

        btnAjouter = createStyledButton("Ajouter", SUCCESS_COLOR);
        btnModifier = createStyledButton(" Modifier", SUCCESS_COLOR);
        btnSupprimer = createStyledButton("Supprimer", SUCCESS_COLOR);
        btnActualiser = createStyledButton(" Actualiser", SUCCESS_COLOR);

        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);

        boutonsPanel.add(btnAjouter);
        boutonsPanel.add(btnModifier);
        boutonsPanel.add(btnSupprimer);
        boutonsPanel.add(btnActualiser);

        // TABLEAU 
        String[] colonnes = {"ID", "Véhicule", "Intervention", "Date", "Coût (€)", "Devise originale"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override 
            public boolean isCellEditable(int r, int c) { 
                return false; 
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Alignement des données des cellules au centre
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style et alignement de l'en-tête
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 35));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(ACCENT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Alignement central du texte de l'en-tête
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Écouteur de sélection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirFormulaire();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder tableBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), 
                " LISTE DES MAINTENANCES ", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                labelFont, TEXT_PRIMARY);
        scrollPane.setBorder(tableBorder);
        scrollPane.getViewport().setBackground(TABLE_BG);

        // PANEL STATISTIQUES 
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setBackground(PANEL_BG);
        statsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        JLabel lblStats = new JLabel();
        lblStats.setForeground(ACCENT_COLOR);
        lblStats.setFont(new Font("Segoe UI", Font.BOLD, 15));
        statsPanel.add(lblStats);
        chargerStatistiques(lblStats);

        // ASSEMBLAGE
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formulairePanel, BorderLayout.CENTER);
        topPanel.add(boutonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);

        // Actions des boutons
        btnAjouter.addActionListener(e -> ajouterMaintenance());
        btnModifier.addActionListener(e -> modifierMaintenance());
        btnSupprimer.addActionListener(e -> supprimerMaintenance());
        btnActualiser.addActionListener(e -> {
            chargerDonnees();
            chargerComboBox();
            chargerStatistiques(lblStats);
            resetFormulaire();
        });
    }

    private void addGridRow(JPanel panel, String labelText, Component component, 
                            int row, GridBagConstraints gbc, Font font) {
        gbc.gridy = row;
        
        // Label
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(font);
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        
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

    //  LOGIQUE METIER
    
    private double convertirVersEuro(double montant, String devise) {
        switch (devise) {
            case "$ (Dollar US)": return montant / TAUX_EUR_TO_USD;
            case "FC (Franc Congolais)": return montant / TAUX_EUR_TO_FC;
            default: return montant;
        }
    }

    private String getSymboleDevise(String devise) {
        switch (devise) {
            case "$ (Dollar US)": return "$";
            case "FC (Franc Congolais)": return "FC";
            default: return "€";
        }
    }

    private void chargerComboBox() {
        cmbVehicule.removeAllItems();
        try {
            cmbVehicule.addItem(null);
            List<Vehicule> vehicules = vehiculeDAO.listerVehicules();
            for (Vehicule v : vehicules) {
                cmbVehicule.addItem(new VehiculeItem(v));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement véhicules : " + e.getMessage());
        }
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            List<Maintenance> maintenances = maintenanceDAO.listerMaintenances();
            for (Maintenance m : maintenances) {
                Object[] ligne = {
                    m.getIdMaintenance(),
                    m.getImmatriculationVehicule(),
                    m.getTypeIntervention(),
                    m.getDateIntervention() != null ? m.getDateIntervention().format(DATE_FORMATTER) : "",
                    String.format("%.2f €", m.getCoutTotal()),
                    "Euro"
                };
                tableModel.addRow(ligne);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerStatistiques(JLabel lblStats) {
        try {
            double totalCout = maintenanceDAO.getTotalCoutMaintenance();
            lblStats.setText("Coût total des maintenances : " + String.format("%,.2f", totalCout) + " €");
        } catch (SQLException e) {
            lblStats.setText("Erreur chargement statistiques");
        }
    }

    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row != -1) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            String immat = (String) tableModel.getValueAt(row, 1);
            for (int i = 0; i < cmbVehicule.getItemCount(); i++) {
                VehiculeItem item = cmbVehicule.getItemAt(i);
                if (item != null && item.getVehicule().getImmatriculation().equals(immat)) {
                    cmbVehicule.setSelectedItem(item);
                    break;
                }
            }
            cmbTypeIntervention.setSelectedItem((String) tableModel.getValueAt(row, 2));
            txtDateIntervention.setText((String) tableModel.getValueAt(row, 3));
            String coutStr = ((String) tableModel.getValueAt(row, 4)).replace(" €", "").replace(",", ".");
            txtCoutTotal.setText(coutStr);
            cmbDevise.setSelectedIndex(0);
            
            btnAjouter.setEnabled(false);
            btnModifier.setEnabled(true);
            btnSupprimer.setEnabled(true);
        }
    }

    private void ajouterMaintenance() {
        VehiculeItem vItem = (VehiculeItem) cmbVehicule.getSelectedItem();
        
        if (vItem == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule !");
            return;
        }
        if (cmbTypeIntervention.getSelectedItem() == null || cmbTypeIntervention.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le type d'intervention est obligatoire !");
            return;
        }
        if (txtDateIntervention.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La date est obligatoire !");
            return;
        }
        if (txtCoutTotal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le coût total est obligatoire !");
            return;
        }
        
        try {
            LocalDate date = LocalDate.parse(txtDateIntervention.getText().trim(), DATE_FORMATTER);
            double montantSaisi = Double.parseDouble(txtCoutTotal.getText().trim().replace(",", "."));
            String deviseChoisie = (String) cmbDevise.getSelectedItem();
            double montantEuro = convertirVersEuro(montantSaisi, deviseChoisie);
            
            Maintenance m = new Maintenance(
                vItem.getVehicule().getIdVehicule(),
                cmbTypeIntervention.getSelectedItem().toString(),
                date,
                montantEuro
            );
            
            maintenanceDAO.ajouterMaintenance(m);
            
            String symbole = getSymboleDevise(deviseChoisie);
            JOptionPane.showMessageDialog(this, 
                "✓ Maintenance ajoutée avec succès !\n" +
                "Montant saisi : " + String.format("%.2f", montantSaisi) + " " + symbole + "\n" +
                "Converti en Euro : " + String.format("%.2f", montantEuro) + " €",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            
            resetFormulaire();
            chargerDonnees();
            chargerStatistiques((JLabel) ((JPanel) getComponent(2)).getComponent(0));
            
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide ! Utilisez AAAA-MM-JJ (ex: 2024-12-31)");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le coût doit être un nombre valide !");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void modifierMaintenance() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une maintenance à modifier !");
            return;
        }
        
        VehiculeItem vItem = (VehiculeItem) cmbVehicule.getSelectedItem();
        if (vItem == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule !");
            return;
        }
        
        try {
            LocalDate date = LocalDate.parse(txtDateIntervention.getText().trim(), DATE_FORMATTER);
            double montantEnEuro = Double.parseDouble(txtCoutTotal.getText().trim().replace(",", "."));
            
            Maintenance m = new Maintenance(
                selectedId,
                vItem.getVehicule().getIdVehicule(),
                cmbTypeIntervention.getSelectedItem().toString(),
                date,
                montantEnEuro
            );
            
            maintenanceDAO.modifierMaintenance(m);
            JOptionPane.showMessageDialog(this, "Maintenance modifiée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            resetFormulaire();
            chargerDonnees();
            chargerStatistiques((JLabel) ((JPanel) getComponent(2)).getComponent(0));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerMaintenance() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une maintenance à supprimer !");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer cette maintenance ?\nCette action est irréversible.",
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                maintenanceDAO.supprimerMaintenance(selectedId);
                JOptionPane.showMessageDialog(this, " Maintenance supprimée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                resetFormulaire();
                chargerDonnees();
                chargerStatistiques((JLabel) ((JPanel) getComponent(2)).getComponent(0));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }

    private void resetFormulaire() {
        cmbVehicule.setSelectedIndex(0);
        cmbTypeIntervention.setSelectedIndex(0);
        txtDateIntervention.setText(LocalDate.now().format(DATE_FORMATTER));
        txtCoutTotal.setText("");
        cmbDevise.setSelectedIndex(0);
        selectedId = -1;
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        table.clearSelection();
    }
}