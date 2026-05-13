package com.mycompany.gestionparcautomobile.gui;

import com.mycompany.gestionparcautomobile.dao.MissionDAO;
import com.mycompany.gestionparcautomobile.dao.VehiculeDAO;
import com.mycompany.gestionparcautomobile.dao.ChauffeurDAO;
import com.mycompany.gestionparcautomobile.models.Mission;
import com.mycompany.gestionparcautomobile.models.Vehicule;
import com.mycompany.gestionparcautomobile.models.Chauffeur;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panneau de gestion des missions 
 * @author AFFABLE MUSHOBOZI
 */
public class MissionPanel extends JPanel {
    
    private MissionDAO missionDAO;
    private VehiculeDAO vehiculeDAO;
    private ChauffeurDAO chauffeurDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<VehiculeItem> cmbVehicule;
    private JComboBox<ChauffeurItem> cmbChauffeur;
    private JTextField txtDestination;
    private JTextField txtDateDepart;
    private JTextField txtDateRetourPrevu;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private int selectedId = -1;
    
    //  THEME PREMIUM DARK 
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
    
    // Couleur pour le texte des ComboBox (NOIR)
    private static final Color COMBOBOX_TEXT_COLOR = Color.BLACK;
    private static final Color COMBOBOX_BG_COLOR = Color.WHITE;
    
    private static final Color TABLE_BG = new Color(30, 30, 35);
    private static final Color TABLE_HEADER_BG = BG_DARK;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // CLASSES INTERNES 
    
    private static class VehiculeItem {
        Vehicule vehicule;
        VehiculeItem(Vehicule v) { vehicule = v; }
        @Override
        public String toString() { return vehicule.getImmatriculation() + " - " + vehicule.getMarque() + " " + vehicule.getModele(); }
        public Vehicule getVehicule() { return vehicule; }
    }
    
    private static class ChauffeurItem {
        Chauffeur chauffeur;
        ChauffeurItem(Chauffeur c) { chauffeur = c; }
        @Override
        public String toString() { return chauffeur.getNom() + " " + chauffeur.getPrenom() + " - " + chauffeur.getNumPermis(); }
        public Chauffeur getChauffeur() { return chauffeur; }
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
                label.setText("" + v.getImmatriculation() + " - " + v.getMarque() + " " + v.getModele());
                label.setForeground(COMBOBOX_TEXT_COLOR);  // Texte NOIR
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
    
    private class ChauffeurListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            // Fond blanc pour la liste déroulante
            list.setBackground(COMBOBOX_BG_COLOR);
            list.setSelectionBackground(ACCENT_COLOR);
            
            if (value instanceof ChauffeurItem) {
                ChauffeurItem item = (ChauffeurItem) value;
                Chauffeur c = item.getChauffeur();
                label.setText("" + c.getNom() + " " + c.getPrenom() + " | Permis: " + c.getNumPermis());
                label.setForeground(COMBOBOX_TEXT_COLOR);  // Texte NOIR
            } else if (value == null) {
                label.setText("Sélectionner un chauffeur");
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
    
    //CONSTRUCTEUR 
    
    public MissionPanel() {
        missionDAO = new MissionDAO();
        vehiculeDAO = new VehiculeDAO();
        chauffeurDAO = new ChauffeurDAO();
        initComponents();
        chargerDonnees();
        chargerComboBox();
        appliquerStyle();
    }
    
    private void appliquerStyle() {
        setBackground(BG_DARK);
        
        // Style pour les JTextField (fond sombre, texte blanc)
        Component[] textFields = {txtDestination, txtDateDepart, txtDateRetourPrevu};
        for (Component c : textFields) {
            if (c instanceof JTextField) {
                JTextField field = (JTextField) c;
                field.setBackground(FIELD_BG);
                field.setForeground(TEXT_PRIMARY);
                field.setCaretColor(TEXT_PRIMARY);
                field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)));
            }
        }
        
        // Style des ComboBox (fond BLANC, texte NOIR)
        cmbVehicule.setBackground(COMBOBOX_BG_COLOR);
        cmbVehicule.setForeground(COMBOBOX_TEXT_COLOR);
        cmbVehicule.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbVehicule.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        
        cmbChauffeur.setBackground(COMBOBOX_BG_COLOR);
        cmbChauffeur.setForeground(COMBOBOX_TEXT_COLOR);
        cmbChauffeur.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbChauffeur.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)));
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //LE FORMULAIRE
        JPanel formulairePanel = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true), 
                " FORMULAIRE MISSION ");
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
        cmbChauffeur = new JComboBox<>();
        txtDestination = new JTextField(15);
        txtDateDepart = new JTextField(15);
        txtDateRetourPrevu = new JTextField(15);
        
        // Appliquer les renderers (avec texte noir)
        cmbVehicule.setRenderer(new VehiculeListRenderer());
        cmbChauffeur.setRenderer(new ChauffeurListRenderer());
        
        // Valeurs par défaut pour les dates
        txtDateDepart.setText(LocalDateTime.now().format(FORMATTER));
        txtDateRetourPrevu.setText(LocalDateTime.now().plusDays(1).format(FORMATTER));
        
        // Ajout des lignes du formulaire
        addGridRow(formulairePanel, "Véhicule :", cmbVehicule, 0, gbc, labelFont);
        addGridRow(formulairePanel, "Chauffeur :", cmbChauffeur, 1, gbc, labelFont);
        addGridRow(formulairePanel, "Destination :", txtDestination, 2, gbc, labelFont);
        addGridRow(formulairePanel, "Date départ :", txtDateDepart, 3, gbc, labelFont);
        addGridRow(formulairePanel, "Date retour prévu :", txtDateRetourPrevu, 4, gbc, labelFont);
        
        // BOUTONS 
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        boutonsPanel.setOpaque(false);
        
        btnAjouter = createStyledButton(" Ajouter", SUCCESS_COLOR);
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
        String[] colonnes = {"ID", "Véhicule", "Chauffeur", "Destination", "Date départ", "Date retour prévu"};
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
        
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirFormulaire();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder tableBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), 
                " LISTE DES MISSIONS ", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                labelFont, TEXT_PRIMARY);
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
        btnAjouter.addActionListener(e -> ajouterMission());
        btnModifier.addActionListener(e -> modifierMission());
        btnSupprimer.addActionListener(e -> supprimerMission());
        btnActualiser.addActionListener(e -> {
            chargerDonnees();
            chargerComboBox();
            resetFormulaire();
        });
    }
    
    private void addGridRow(JPanel panel, String labelText, Component component, 
                            int row, GridBagConstraints gbc, Font font) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(label, gbc);
        
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
    
    private void chargerComboBox() {
        cmbVehicule.removeAllItems();
        cmbChauffeur.removeAllItems();
        
        try {
            cmbVehicule.addItem(null);
            cmbChauffeur.addItem(null);
            
            List<Vehicule> vehicules = vehiculeDAO.listerVehicules();
            for (Vehicule v : vehicules) {
                cmbVehicule.addItem(new VehiculeItem(v));
            }
            
            List<Chauffeur> chauffeurs = chauffeurDAO.listerChauffeurs();
            for (Chauffeur c : chauffeurs) {
                cmbChauffeur.addItem(new ChauffeurItem(c));
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement combobox : " + e.getMessage());
        }
    }
    
    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            List<Mission> missions = missionDAO.listerMissions();
            for (Mission m : missions) {
                Object[] ligne = {
                    m.getIdMission(),
                    m.getImmatriculationVehicule(),
                    m.getNomChauffeur(),
                    m.getDestination(),
                    m.getDateDepart() != null ? m.getDateDepart().format(FORMATTER) : "",
                    m.getDateRetourPrevu() != null ? m.getDateRetourPrevu().format(FORMATTER) : ""
                };
                tableModel.addRow(ligne);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des missions : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row != -1) {
            selectedId = (int) tableModel.getValueAt(row, 0);
            txtDestination.setText((String) tableModel.getValueAt(row, 3));
            
            String dateDepartStr = (String) tableModel.getValueAt(row, 4);
            String dateRetourStr = (String) tableModel.getValueAt(row, 5);
            if (!dateDepartStr.isEmpty()) txtDateDepart.setText(dateDepartStr);
            if (!dateRetourStr.isEmpty()) txtDateRetourPrevu.setText(dateRetourStr);
            
            String immatriculation = (String) tableModel.getValueAt(row, 1);
            String nomChauffeur = (String) tableModel.getValueAt(row, 2);
            
            for (int i = 0; i < cmbVehicule.getItemCount(); i++) {
                VehiculeItem item = cmbVehicule.getItemAt(i);
                if (item != null && item.getVehicule().getImmatriculation().equals(immatriculation)) {
                    cmbVehicule.setSelectedItem(item);
                    break;
                }
            }
            
            for (int i = 0; i < cmbChauffeur.getItemCount(); i++) {
                ChauffeurItem item = cmbChauffeur.getItemAt(i);
                if (item != null && item.toString().equals(nomChauffeur)) {
                    cmbChauffeur.setSelectedItem(item);
                    break;
                }
            }
            
            btnAjouter.setEnabled(false);
            btnModifier.setEnabled(true);
            btnSupprimer.setEnabled(true);
        } else {
            resetFormulaire();
        }
    }
    
    private void ajouterMission() {
        VehiculeItem vItem = (VehiculeItem) cmbVehicule.getSelectedItem();
        ChauffeurItem cItem = (ChauffeurItem) cmbChauffeur.getSelectedItem();
        
        if (vItem == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule !");
            return;
        }
        if (cItem == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un chauffeur !");
            return;
        }
        if (txtDestination.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La destination est obligatoire !");
            txtDestination.requestFocus();
            return;
        }
        
        try {
            LocalDateTime dateDepart = LocalDateTime.parse(txtDateDepart.getText().trim(), FORMATTER);
            LocalDateTime dateRetourPrevu = LocalDateTime.parse(txtDateRetourPrevu.getText().trim(), FORMATTER);
            
            if (dateRetourPrevu.isBefore(dateDepart)) {
                JOptionPane.showMessageDialog(this, "La date de retour ne peut pas être avant la date de départ !");
                return;
            }
            
            Mission m = new Mission(
                vItem.getVehicule().getIdVehicule(),
                cItem.getChauffeur().getIdChauffeur(),
                dateDepart,
                dateRetourPrevu,
                txtDestination.getText().trim()
            );
            
            missionDAO.ajouterMission(m);
            JOptionPane.showMessageDialog(this, "✓ Mission ajoutée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            resetFormulaire();
            chargerDonnees();
            
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide ! Utilisez AAAA-MM-JJ HH:MM:SS");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
    
    private void modifierMission() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une mission à modifier !");
            return;
        }
        
        VehiculeItem vItem = (VehiculeItem) cmbVehicule.getSelectedItem();
        ChauffeurItem cItem = (ChauffeurItem) cmbChauffeur.getSelectedItem();
        
        if (vItem == null || cItem == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule et un chauffeur !");
            return;
        }
        
        try {
            LocalDateTime dateDepart = LocalDateTime.parse(txtDateDepart.getText().trim(), FORMATTER);
            LocalDateTime dateRetourPrevu = LocalDateTime.parse(txtDateRetourPrevu.getText().trim(), FORMATTER);
            
            Mission m = new Mission(
                selectedId,
                vItem.getVehicule().getIdVehicule(),
                cItem.getChauffeur().getIdChauffeur(),
                dateDepart,
                dateRetourPrevu,
                txtDestination.getText().trim()
            );
            
            missionDAO.modifierMission(m);
            JOptionPane.showMessageDialog(this, " Mission modifiée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            resetFormulaire();
            chargerDonnees();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
    
    private void supprimerMission() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une mission à supprimer !");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer cette mission ?\nCette action est irréversible.",
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                missionDAO.supprimerMission(selectedId);
                JOptionPane.showMessageDialog(this, "Mission supprimée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                resetFormulaire();
                chargerDonnees();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }
    
    private void resetFormulaire() {
        cmbVehicule.setSelectedIndex(0);
        cmbChauffeur.setSelectedIndex(0);
        txtDestination.setText("");
        txtDateDepart.setText(LocalDateTime.now().format(FORMATTER));
        txtDateRetourPrevu.setText(LocalDateTime.now().plusDays(1).format(FORMATTER));
        selectedId = -1;
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        table.clearSelection();
        txtDestination.requestFocus();
    }
}