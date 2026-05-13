package com.mycompany.gestionparcautomobile.gui;

import com.mycompany.gestionparcautomobile.dao.*;
import com.mycompany.gestionparcautomobile.models.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportPanel extends JDialog {
    
    private String reportType;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnPrint, btnPrintPreview, btnExport, btnClose, btnRetour;
    private JButton btnPrintAllResults;
    private JComboBox<String> cmbSearchType;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;
    private JTabbedPane tabbedPane;
    private MainFrame mainFrame;
    private List<Object> searchResults;
    
    private VehiculeDAO vehiculeDAO;
    private ChauffeurDAO chauffeurDAO;
    private MissionDAO missionDAO;
    private MaintenanceDAO maintenanceDAO;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public ReportPanel(MainFrame mainFrame, String reportType) {
        super(mainFrame, "Rapport - " + getTitleForType(reportType), true);
        this.mainFrame = mainFrame;
        this.reportType = reportType;
        this.searchResults = new ArrayList<>();
        
        this.vehiculeDAO = new VehiculeDAO();
        this.chauffeurDAO = new ChauffeurDAO();
        this.missionDAO = new MissionDAO();
        this.maintenanceDAO = new MaintenanceDAO();
        
        initComponents();
        
        if (!reportType.equals("PARAMETRE")) {
            chargerDonnees();
        }
    }
    
    private static String getTitleForType(String type) {
        switch (type) {
            case "VEHICULES": return "Liste des Véhicules";
            case "CHAUFFEURS": return "Liste des Chauffeurs";
            case "MISSIONS": return "Liste des Missions";
            case "MAINTENANCES": return "Liste des Maintenances";
            case "PARAMETRE": return "Recherche Personnalisée";
            default: return "Rapport";
        }
    }
    
    private void initComponents() {
        setSize(1500, 800);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(24, 24, 27));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(63, 63, 70)),
            " Recherche",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(99, 102, 241)
        ));
        searchPanel.setBackground(new Color(34, 34, 38));
        
        if (reportType.equals("PARAMETRE")) {
            String[] searchTypes = {"Chauffeur", "Véhicule"};
            cmbSearchType = new JComboBox<>(searchTypes);
            txtSearch = new JTextField(20);
            btnSearch = createButton(" Rechercher", new Color(59, 130, 246));
            btnPrintAllResults = createButton("Imprimer TOUS", new Color(128, 128, 128));
            
            txtSearch.setBackground(new Color(45, 45, 50));
            txtSearch.setForeground(Color.WHITE);
            txtSearch.setCaretColor(Color.WHITE);
            txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(63, 63, 70)),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
            ));
            
            cmbSearchType.setBackground(new Color(45, 45, 50));
            cmbSearchType.setForeground(Color.WHITE);
            
            JLabel lbl1 = new JLabel("Rechercher par :");
            lbl1.setForeground(Color.WHITE);
            JLabel lbl2 = new JLabel("Mot clé :");
            lbl2.setForeground(Color.WHITE);
            
            searchPanel.add(lbl1);
            searchPanel.add(cmbSearchType);
            searchPanel.add(Box.createHorizontalStrut(5));
            searchPanel.add(lbl2);
            searchPanel.add(txtSearch);
            searchPanel.add(Box.createHorizontalStrut(5));
            searchPanel.add(btnSearch);
            searchPanel.add(Box.createHorizontalStrut(5));
            searchPanel.add(btnPrintAllResults);
            searchPanel.add(Box.createHorizontalStrut(5));
        }
        
        //BOUTONS POUR TOUS LES RAPPORTS 
        
        btnPrintPreview = createButton(" Aperçu", new Color(128, 128, 128));
        btnPrint = createButton(" Imprimer", new Color(34, 197, 94));
        btnExport = createButton("Exporter", new Color(245, 158, 11));
        btnRetour = createButton(" Retour", new Color(128, 128, 128));
        btnClose = createButton("Fermer", new Color(239, 68, 68));
        
        searchPanel.add(btnPrintPreview);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(btnPrint);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(btnExport);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(btnRetour);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(btnClose);
        
        add(searchPanel, BorderLayout.NORTH);
        
        if (reportType.equals("PARAMETRE")) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setBackground(new Color(34, 34, 38));
            tabbedPane.setForeground(Color.WHITE);
            
            JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
            resultPanel.setBackground(new Color(30, 30, 35));
            resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel resultLabel = new JLabel("Résultats (cliquez sur un élément pour voir les détails)");
            resultLabel.setForeground(new Color(99, 102, 241));
            resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            resultPanel.add(resultLabel, BorderLayout.NORTH);
            
            listModel = new DefaultListModel<>();
            resultList = new JList<>(listModel);
            resultList.setBackground(new Color(45, 45, 50));
            resultList.setForeground(Color.WHITE);
            resultList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            resultList.setSelectionBackground(new Color(99, 102, 241));
            resultList.setFixedCellHeight(35);
            resultList.setBorder(BorderFactory.createLineBorder(new Color(63, 63, 70)));
            
            resultList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int index = resultList.getSelectedIndex();
                    if (index >= 0 && index < searchResults.size()) {
                        afficherDetails(index);
                        if (tabbedPane != null) {
                            tabbedPane.setSelectedIndex(1);
                        }
                    }
                }
            });
            
            JScrollPane listScroll = new JScrollPane(resultList);
            listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(63, 63, 70)),
                "Résultats",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(99, 102, 241)
            ));
            listScroll.getViewport().setBackground(new Color(30, 30, 35));
            resultPanel.add(listScroll, BorderLayout.CENTER);
            
            tabbedPane.addTab(" Résultats", resultPanel);
            
            JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
            detailPanel.setBackground(new Color(30, 30, 35));
            detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            String[] colonnes = {"Type", "Nom", "Détails", "Statut", "Missions", "Maintenances", "Infos"};
            tableModel = new DefaultTableModel(colonnes, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setBackground(new Color(45, 45, 50));
            table.getTableHeader().setForeground(new Color(99, 102, 241));
            table.setBackground(new Color(30, 30, 35));
            table.setForeground(new Color(244, 244, 245));
            table.setGridColor(new Color(63, 63, 70));
            table.setSelectionBackground(new Color(99, 102, 241));
            table.setSelectionForeground(Color.WHITE);
            
            JScrollPane tableScroll = new JScrollPane(table);
            tableScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(63, 63, 70)),
                "Détails complets",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(99, 102, 241)
            ));
            tableScroll.getViewport().setBackground(new Color(30, 30, 35));
            detailPanel.add(tableScroll, BorderLayout.CENTER);
            
            tabbedPane.addTab(" Détails", detailPanel);
            add(tabbedPane, BorderLayout.CENTER);
        } else {
            String[] colonnes = getColumnesForType();
            tableModel = new DefaultTableModel(colonnes, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setBackground(new Color(45, 45, 50));
            table.getTableHeader().setForeground(new Color(99, 102, 241));
            table.setBackground(new Color(30, 30, 35));
            table.setForeground(new Color(244, 244, 245));
            table.setGridColor(new Color(63, 63, 70));
            table.setSelectionBackground(new Color(99, 102, 241));
            table.setSelectionForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(new Color(30, 30, 35));
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(63, 63, 70)));
            add(scrollPane, BorderLayout.CENTER);
        }
        
        //  ACTIONS DES BOUTONS 
        if (reportType.equals("PARAMETRE")) {
            btnSearch.addActionListener(e -> rechercher());
            btnPrintAllResults.addActionListener(e -> imprimerTousLesResultats());
        }
        btnPrintPreview.addActionListener(e -> apercuAvantImpression());
        btnPrint.addActionListener(e -> imprimerRapport());
        btnExport.addActionListener(e -> exporterTXT());
        btnRetour.addActionListener(e -> {
            mainFrame.showPanel("ACCUEIL");
            dispose();
        });
        btnClose.addActionListener(e -> dispose());
        
        // Panel info en bas
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(new Color(34, 34, 38));
        infoPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(63, 63, 70)));
        JLabel infoLabel = new JLabel("ℹ️ " + getInfoText());
        infoLabel.setForeground(new Color(180, 180, 190));
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoPanel.add(infoLabel);
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private String getInfoText() {
        int rowCount = (tableModel != null) ? tableModel.getRowCount() : 0;
        switch (reportType) {
            case "VEHICULES": return "Nombre total de véhicules : " + rowCount;
            case "CHAUFFEURS": return "Nombre total de chauffeurs : " + rowCount;
            case "MISSIONS": return "Nombre total de missions : " + rowCount;
            case "MAINTENANCES": return "Nombre total d'interventions : " + rowCount;
            case "PARAMETRE": return "Effectuez une recherche - Résultats : " + searchResults.size();
            default: return "";
        }
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
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
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private String[] getColumnesForType() {
        switch (reportType) {
            case "VEHICULES":
                return new String[]{"ID", "Immatriculation", "Marque", "Modèle", "Carburant", "Kilométrage", "Statut"};
            case "CHAUFFEURS":
                return new String[]{"ID", "Nom", "Prénom", "N° Permis", "Téléphone", "Nb Missions", "Total Jours", "Dernière Mission"};
            case "MISSIONS":
                return new String[]{"ID", "Véhicule", "Chauffeur", "Destination", "Date Départ", "Date Retour Prévu", "Durée (jours)"};
            case "MAINTENANCES":
                return new String[]{"ID", "Véhicule", "Type d'intervention", "Date", "Coût (€)"};
            default:
                return new String[]{};
        }
    }
    
    private void chargerDonnees() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        try {
            switch (reportType) {
                case "VEHICULES":
                    List<Vehicule> vehicules = vehiculeDAO.listerVehicules();
                    for (Vehicule v : vehicules) {
                        tableModel.addRow(new Object[]{
                            v.getIdVehicule(), v.getImmatriculation(), v.getMarque(),
                            v.getModele(), v.getCarburant(), String.format("%,d km", v.getKilometrage()),
                            getStatutIcon(v.getStatut())
                        });
                    }
                    break;
                case "CHAUFFEURS":
                    List<Chauffeur> chauffeurs = chauffeurDAO.listerChauffeurs();
                    for (Chauffeur c : chauffeurs) {
                        List<Mission> missions = missionDAO.trouverMissionsParChauffeur(c.getIdChauffeur());
                        int nbMissions = missions.size();
                        long totalJours = 0;
                        String derniereMission = "Aucune";
                        for (Mission m : missions) {
                            if (m.getDateDepart() != null && m.getDateRetourPrevu() != null) {
                                totalJours += java.time.Duration.between(m.getDateDepart(), m.getDateRetourPrevu()).toDays();
                                derniereMission = m.getDestination() + " (" + m.getDateDepart().format(DATE_FORMATTER) + ")";
                            }
                        }
                        tableModel.addRow(new Object[]{
                            c.getIdChauffeur(), c.getNom(), c.getPrenom(), c.getNumPermis(),
                            c.getTelephone(), nbMissions, totalJours, derniereMission
                        });
                    }
                    break;
                case "MISSIONS":
                    List<Mission> missions = missionDAO.listerMissions();
                    for (Mission m : missions) {
                        long duree = 0;
                        if (m.getDateDepart() != null && m.getDateRetourPrevu() != null) {
                            duree = java.time.Duration.between(m.getDateDepart(), m.getDateRetourPrevu()).toDays();
                        }
                        tableModel.addRow(new Object[]{
                            m.getIdMission(), m.getImmatriculationVehicule(), m.getNomChauffeur(),
                            m.getDestination(), m.getDateDepart() != null ? m.getDateDepart().format(DATE_FORMATTER) : "",
                            m.getDateRetourPrevu() != null ? m.getDateRetourPrevu().format(DATE_FORMATTER) : "", duree
                        });
                    }
                    break;
                case "MAINTENANCES":
                    List<Maintenance> maintenances = maintenanceDAO.listerMaintenances();
                    for (Maintenance m : maintenances) {
                        tableModel.addRow(new Object[]{
                            m.getIdMaintenance(), m.getImmatriculationVehicule(), m.getTypeIntervention(),
                            m.getDateIntervention(), String.format("%.2f €", m.getCoutTotal())
                        });
                    }
                    break;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + e.getMessage());
        }
    }
    
    private void rechercher() {
        String motCle = txtSearch.getText().trim().toLowerCase();
        if (motCle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un mot clé à rechercher !");
            return;
        }
        
        String typeRecherche = (String) cmbSearchType.getSelectedItem();
        listModel.clear();
        searchResults.clear();
        
        try {
            if (typeRecherche.equals("Chauffeur")) {
                List<Chauffeur> chauffeurs = chauffeurDAO.listerChauffeurs();
                for (Chauffeur c : chauffeurs) {
                    String nomComplet = (c.getNom() + " " + c.getPrenom()).toLowerCase();
                    if (nomComplet.contains(motCle) || c.getNumPermis().toLowerCase().contains(motCle)) {
                        searchResults.add(c);
                        listModel.addElement("" + c.getNom() + " " + c.getPrenom() + " | Permis: " + c.getNumPermis());
                    }
                }
            } else {
                List<Vehicule> vehicules = vehiculeDAO.listerVehicules();
                for (Vehicule v : vehicules) {
                    if (v.getImmatriculation().toLowerCase().contains(motCle) || v.getMarque().toLowerCase().contains(motCle)) {
                        searchResults.add(v);
                        listModel.addElement(" " + v.getImmatriculation() + " - " + v.getMarque() + " " + v.getModele());
                    }
                }
            }
            
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun résultat trouvé pour : " + motCle);
            } else {
                tabbedPane.setSelectedIndex(0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
    
    private void afficherDetails(int index) {
        if (index < 0 || index >= searchResults.size()) return;
        
        Object obj = searchResults.get(index);
        tableModel.setRowCount(0);
        
        try {
            if (obj instanceof Chauffeur) {
                Chauffeur c = (Chauffeur) obj;
                List<Mission> missions = missionDAO.trouverMissionsParChauffeur(c.getIdChauffeur());
                
                tableModel.addRow(new Object[]{"CHAUFFEUR", c.getNom() + " " + c.getPrenom(),
                    "Permis: " + c.getNumPermis() + " | Tél: " + c.getTelephone(), "Actif",
                    missions.size() + " mission(s)", "", ""});
                
                long totalJours = 0;
                for (Mission m : missions) {
                    if (m.getDateDepart() != null && m.getDateRetourPrevu() != null) {
                        long duree = java.time.Duration.between(m.getDateDepart(), m.getDateRetourPrevu()).toDays();
                        totalJours += duree;
                        tableModel.addRow(new Object[]{" MISSION", "→ " + m.getDestination(),
                            "Véhicule: " + m.getImmatriculationVehicule(), "Départ: " + m.getDateDepart().format(DATE_FORMATTER),
                            "Durée: " + duree + " jour(s)", "", ""});
                    }
                }
                if (totalJours > 0 && missions.size() > 0) {
                    tableModel.addRow(new Object[]{"  RÉSUMÉ", "Total jours: " + totalJours,
                        "Moyenne: " + (totalJours / missions.size()) + " jours", "", "", "", ""});
                }
            } else if (obj instanceof Vehicule) {
                Vehicule v = (Vehicule) obj;
                List<Mission> missions = missionDAO.trouverMissionsParVehicule(v.getIdVehicule());
                List<Maintenance> maintenances = maintenanceDAO.trouverMaintenancesParVehicule(v.getIdVehicule());
                
                tableModel.addRow(new Object[]{"VÉHICULE", v.getImmatriculation(),
                    v.getMarque() + " " + v.getModele() + " | " + v.getCarburant(),
                    "Statut: " + v.getStatut(), missions.size() + " mission(s)",
                    maintenances.size() + " maintenance(s)", String.format("%,d km", v.getKilometrage())});
                
                for (Mission m : missions) {
                    tableModel.addRow(new Object[]{"  MISSION", "→ " + m.getDestination(),
                        "Chauffeur: " + m.getNomChauffeur(), "Départ: " + m.getDateDepart().format(DATE_FORMATTER), "", "", ""});
                }
                for (Maintenance maint : maintenances) {
                    tableModel.addRow(new Object[]{" MAINTENANCE", "→ " + maint.getTypeIntervention(),
                        "Date: " + maint.getDateIntervention(), "Coût: " + String.format("%.2f", maint.getCoutTotal()) + " €", "", "", ""});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
        
        tabbedPane.setSelectedIndex(1);
    }
    
    private void imprimerTousLesResultats() {
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à imprimer ! Effectuez d'abord une recherche.");
            return;
        }
        
        StringBuilder rapport = new StringBuilder();
        rapport.append("╔════════════════════════════════════════════════════════════════════════════════╗\n");
        rapport.append("║                    GESTION DU PARC AUTOMOBILE                        ║\n");
        rapport.append("║                    RAPPORT COMPLET DES RÉSULTATS                     ║\n");
        rapport.append("╠════════════════════════════════════════════════════════════════════════════════╣\n");
        rapport.append(String.format("║  Type de recherche : %-52s ║\n", (String) cmbSearchType.getSelectedItem()));
        rapport.append(String.format("║  Mot clé : %-60s                                       ║\n", txtSearch.getText().trim()));
        rapport.append(String.format("║  Date : %-64s                                          ║\n", DATE_FORMAT.format(new Date())));
        rapport.append("╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
        
        try {
            for (Object obj : searchResults) {
                if (obj instanceof Chauffeur) {
                    Chauffeur c = (Chauffeur) obj;
                    rapport.append("\n").append("=".repeat(80)).append("\n");
                    rapport.append(" CHAUFFEUR : ").append(c.getNom()).append(" ").append(c.getPrenom()).append("\n");
                    rapport.append("   Permis : ").append(c.getNumPermis()).append("\n");
                    rapport.append("   Téléphone : ").append(c.getTelephone()).append("\n");
                    rapport.append("-".repeat(80)).append("\n");
                    
                    List<Mission> missions = missionDAO.trouverMissionsParChauffeur(c.getIdChauffeur());
                    if (missions.isEmpty()) {
                        rapport.append("   Aucune mission effectuée\n");
                    } else {
                        rapport.append("   MISSIONS EFFECTUÉES (").append(missions.size()).append(") :\n");
                        long totalJours = 0;
                        for (Mission m : missions) {
                            long duree = java.time.Duration.between(m.getDateDepart(), m.getDateRetourPrevu()).toDays();
                            totalJours += duree;
                            rapport.append("      • ").append(m.getDestination())
                                  .append(" | Véhicule: ").append(m.getImmatriculationVehicule())
                                  .append(" | Durée: ").append(duree).append(" jours\n");
                        }
                        rapport.append("Total jours en mission : ").append(totalJours).append("\n");
                    }
                } else if (obj instanceof Vehicule) {
                    Vehicule v = (Vehicule) obj;
                    rapport.append("\n").append("=".repeat(80)).append("\n");
                    rapport.append(" VÉHICULE : ").append(v.getImmatriculation()).append("\n");
                    rapport.append("   Marque/Modèle : ").append(v.getMarque()).append(" ").append(v.getModele()).append("\n");
                    rapport.append("   Carburant : ").append(v.getCarburant()).append("\n");
                    rapport.append("   Kilométrage : ").append(String.format("%,d", v.getKilometrage())).append(" km\n");
                    rapport.append("   Statut : ").append(v.getStatut()).append("\n");
                    rapport.append("-".repeat(80)).append("\n");
                    
                    List<Mission> missions = missionDAO.trouverMissionsParVehicule(v.getIdVehicule());
                    if (!missions.isEmpty()) {
                        rapport.append("   MISSIONS EFFECTUÉES (").append(missions.size()).append(") :\n");
                        for (Mission m : missions) {
                            rapport.append("      • ").append(m.getDestination())
                                  .append(" | Chauffeur: ").append(m.getNomChauffeur())
                                  .append(" | Départ: ").append(m.getDateDepart().format(DATE_FORMATTER)).append("\n");
                        }
                    }
                    
                    List<Maintenance> maintenances = maintenanceDAO.trouverMaintenancesParVehicule(v.getIdVehicule());
                    if (!maintenances.isEmpty()) {
                        double totalCout = 0;
                        rapport.append("   MAINTENANCES EFFECTUÉES (").append(maintenances.size()).append(") :\n");
                        for (Maintenance maint : maintenances) {
                            totalCout += maint.getCoutTotal();
                            rapport.append("      • ").append(maint.getTypeIntervention())
                                  .append(" | Date: ").append(maint.getDateIntervention())
                                  .append(" | Coût: ").append(String.format("%.2f", maint.getCoutTotal())).append(" €\n");
                        }
                        rapport.append("    Coût total des maintenances : ").append(String.format("%.2f", totalCout)).append(" €\n");
                    }
                }
            }
        } catch (SQLException e) {
            rapport.append("\nErreur lors du chargement des données : ").append(e.getMessage());
        }
        
        rapport.append("\n").append("═".repeat(80)).append("\n");
        rapport.append(String.format("Total : %d enregistrement(s)\n", searchResults.size()));
        rapport.append("Fin du rapport - Généré le ").append(DATE_FORMAT.format(new Date())).append("\n");
        
        imprimerDirect(rapport.toString());
    }
    
    private void imprimerRapport() {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Aucune donnée à imprimer !");
            return;
        }
        
        StringBuilder rapport = new StringBuilder();
        rapport.append("              ╔════════════════════════════════════════════════════════════════════════════════╗\n");
        rapport.append("              ║                           GESTION DU PARC AUTOMOBILE                 ║\n");
        rapport.append("              ║                                RAPPORT OFFICIEL                      ║\n");
        rapport.append("              ╠════════════════════════════════════════════════════════════════════════════════╣\n");
        rapport.append(String.format("║  Rapport : %-60s                                                     ║\n", getTitleForType(reportType)));
        rapport.append(String.format("║  Date : %-64s                                                        ║\n", DATE_FORMAT.format(new Date())));
        rapport.append("              ╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
        
        for (int j = 0; j < tableModel.getColumnCount(); j++) {
            rapport.append(String.format("%-20s", tableModel.getColumnName(j)));
        }
        rapport.append("\n");
        rapport.append("─".repeat(80)).append("\n");
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object value = tableModel.getValueAt(i, j);
                String val = (value != null && !value.toString().isEmpty()) ? value.toString() : "";
                rapport.append(String.format("%-20s", val.length() > 20 ? val.substring(0, 17) + "..." : val));
            }
            rapport.append("\n");
        }
        
        rapport.append("\n");
        rapport.append("═".repeat(80)).append("\n");
        rapport.append(String.format("Total : %d enregistrement(s)\n", tableModel.getRowCount()));
        
        imprimerDirect(rapport.toString());
    }
    
    private void apercuAvantImpression() {
        if ((tableModel == null || tableModel.getRowCount() == 0) && searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune donnée à imprimer !");
            return;
        }
        
        String contenu;
        if (reportType.equals("PARAMETRE") && !searchResults.isEmpty()) {
            contenu = genererContenuResultats();
        } else {
            contenu = genererContenuRapport();
        }
        
        JDialog apercuDialog = new JDialog(this, "Aperçu avant impression", true);
        apercuDialog.setSize(900, 700);
        apercuDialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea(contenu);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnImprimer = new JButton("Imprimer");
        JButton btnFermer = new JButton(" Fermer");
        
        btnImprimer.setBackground(new Color(34, 197, 94));
        btnImprimer.setForeground(Color.WHITE);
        btnFermer.setBackground(new Color(239, 68, 68));
        btnFermer.setForeground(Color.WHITE);
        
        String finalContenu = contenu;
        btnImprimer.addActionListener(e -> {
            imprimerDirect(finalContenu);
            apercuDialog.dispose();
        });
        btnFermer.addActionListener(e -> apercuDialog.dispose());
        
        buttonPanel.add(btnImprimer);
        buttonPanel.add(btnFermer);
        
        apercuDialog.add(scrollPane, BorderLayout.CENTER);
        apercuDialog.add(buttonPanel, BorderLayout.SOUTH);
        apercuDialog.setVisible(true);
    }
    
    private String genererContenuRapport() {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            return "Aucune donnée à afficher";
        }
        
        StringBuilder rapport = new StringBuilder();
        rapport.append("              ╔════════════════════════════════════════════════════════════════════════════════╗\n");
        rapport.append("              ║                           GESTION DU PARC AUTOMOBILE                 ║\n");
        rapport.append("              ║                                RAPPORT OFFICIEL                      ║\n");
        rapport.append("              ╠════════════════════════════════════════════════════════════════════════════════╣\n");
        rapport.append(String.format("║  Rapport : %-60s                                                     ║\n", getTitleForType(reportType)));
        rapport.append(String.format("║  Date : %-64s                                                        ║\n", DATE_FORMAT.format(new Date())));
        rapport.append("              ╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
        
        for (int j = 0; j < tableModel.getColumnCount(); j++) {
            rapport.append(String.format("%-20s", tableModel.getColumnName(j)));
        }
        rapport.append("\n");
        rapport.append("─".repeat(tableModel.getColumnCount() * 20)).append("\n");
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object value = tableModel.getValueAt(i, j);
                String val = (value != null && !value.toString().isEmpty()) ? value.toString() : "";
                rapport.append(String.format("%-20s", val.length() > 20 ? val.substring(0, 17) + "..." : val));
            }
            rapport.append("\n");
        }
        
        rapport.append("\n");
        rapport.append("═".repeat(tableModel.getColumnCount() * 20)).append("\n");
        rapport.append(String.format("Total : %d enregistrement(s)\n", tableModel.getRowCount()));
        rapport.append("Fin du rapport - Généré le ").append(DATE_FORMAT.format(new Date())).append("\n");
        
        return rapport.toString();
    }
    
    private String genererContenuResultats() {
        if (searchResults.isEmpty()) {
            return "Aucun résultat à afficher";
        }
        
        StringBuilder rapport = new StringBuilder();
        rapport.append("              ╔════════════════════════════════════════════════════════════════════════════════╗\n");
        rapport.append("              ║                           GESTION DU PARC AUTOMOBILE                 ║\n");
        rapport.append("              ║                          RAPPORT PARAMETRE                           ║\n");
        rapport.append("              ╠════════════════════════════════════════════════════════════════════════════════╣\n");
        rapport.append(String.format("║  Type : %-63s                                                        ║\n", (String) cmbSearchType.getSelectedItem()));
        rapport.append(String.format("║  Mot clé : %-60s                                                     ║\n", txtSearch.getText().trim()));
        rapport.append(String.format("║  Date : %-64s                                                        ║\n", DATE_FORMAT.format(new Date())));
        rapport.append("              ╚════════════════════════════════════════════════════════════════════════════════╝\n\n");
        
        rapport.append("═".repeat(80)).append("\n");
        rapport.append(String.format("Total : %d résultat(s) trouvé(s)\n", searchResults.size()));
        rapport.append("═".repeat(80)).append("\n");
        
        return rapport.toString();
    }
    
    private void imprimerDirect(String contenu) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            
            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex > 0) return NO_SUCH_PAGE;
                    
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    
                    Font font = new Font("Monospaced", Font.PLAIN, 9);
                    g2d.setFont(font);
                    
                    String[] lines = contenu.split("\n");
                    int y = 15;
                    int lineHeight = g2d.getFontMetrics().getHeight();
                    
                    for (String line : lines) {
                        if (y + lineHeight > pageFormat.getImageableHeight()) {
                            return PAGE_EXISTS;
                        }
                        g2d.drawString(line, 10, y);
                        y += lineHeight;
                    }
                    
                    return PAGE_EXISTS;
                }
            });
            
            boolean ok = job.printDialog();
            if (ok) {
                job.print();
                JOptionPane.showMessageDialog(this, "Impression terminée !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Erreur d'impression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exporterTXT() {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Aucune donnée à exporter !");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le rapport");
        fileChooser.setSelectedFile(new java.io.File("Rapport_" + reportType + "_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.FileWriter fw = new java.io.FileWriter(file);
                fw.write(genererContenuRapport());
                fw.close();
                JOptionPane.showMessageDialog(this, " Rapport exporté avec succès !\n" + file.getAbsolutePath(), 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur d'exportation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String getStatutIcon(String statut) {
        switch (statut) {
            case "Disponible": return " " + statut;
            case "En mission": return " " + statut;
            case "En maintenance": return " " + statut;
            default: return statut;
        }
    }
}