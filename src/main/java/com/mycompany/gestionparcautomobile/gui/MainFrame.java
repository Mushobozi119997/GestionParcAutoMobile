package com.mycompany.gestionparcautomobile.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import static java.awt.Color.BLACK;
import java.awt.event.*;

/**

 * FRM PRINCIPAL - GESTION DU PARC AUTOMOBILE
 *
 * @author AFFABLE MUSHOBOZI
 */
public class MainFrame extends JFrame {

   
    //                    COULEURS
    

    private static final Color BACKGROUND =
            new Color(10, 14, 20);

    private static final Color SIDEBAR =
            new Color(15, 23, 42);

    private static final Color CARD =
            new Color(30, 41, 59);

    private static final Color CARD_HOVER =
            new Color(51, 65, 85);

    private static final Color ACCENT =
            new Color(59, 130, 246);

    private static final Color SUCCESS =
            new Color(34, 197, 94);

    private static final Color WARNING =
            new Color(251, 191, 36);

    private static final Color DANGER =
            new Color(239, 68, 68);

    private static final Color TEXT =
            new Color(248, 250, 252);

    private static final Color TEXT_SECONDARY =
            new Color(148, 163, 184);
   
          //                FONTS
    

    private static final Font LOGO_FONT =
            new Font("Segoe UI", Font.BOLD, 24);

    private static final Font MENU_FONT =
            new Font("Segoe UI", Font.PLAIN, 15);

    private static final Font TITLE_FONT =
            new Font("Segoe UI", Font.BOLD, 26);

    private static final Font CARD_TITLE =
            new Font("Segoe UI", Font.BOLD, 18);

    private static final Font BIG_NUMBER =
            new Font("Segoe UI", Font.BOLD, 34);
    
    //                    VARIABLES(buttons declares)
 

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel pageTitle;
    private JButton btnRetour;
    private JButton btnQuitter;

   
    //                    CONSTRUCTEUR
   

    public MainFrame() {
        initializeWindow();
        initializeContent();
        initializeSidebar();
        initializeTopbar();
    }

   
    //              INITIALIZE WINDOW


    private void initializeWindow() {
        setTitle("Gestion du Parc Automobile");
        setSize(1450, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);
    }

  
    //              INITIALIZE CONTENT
    

    private void initializeContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(createDashboard(), "ACCUEIL");
        contentPanel.add(new VehiculePanel(), "VEHICULES");
        contentPanel.add(new ChauffeurPanel(), "CHAUFFEURS");
        contentPanel.add(new MissionPanel(), "MISSIONS");
        contentPanel.add(new MaintenancePanel(), "MAINTENANCES");

        add(contentPanel, BorderLayout.CENTER);
    }

  
    //               INITIALIZE SIDEBAR
    

    private void initializeSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(40, 50, 70)));

     
        // LOGO
      

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 30));
        logoPanel.setOpaque(false);
        JLabel logo = new JLabel("Parc Automobile");
        logo.setFont(LOGO_FONT);
        logo.setForeground(ACCENT);
        logoPanel.add(logo);
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

     
        // MENU BUTTONS
      

        sidebar.add(createMenuButton(" Tableau de bord", "ACCUEIL"));
        sidebar.add(createMenuButton(" Véhicules", "VEHICULES"));
        sidebar.add(createMenuButton(" Chauffeurs", "CHAUFFEURS"));
        sidebar.add(createMenuButton("Missions", "MISSIONS"));
        sidebar.add(createMenuButton(" Maintenances", "MAINTENANCES"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Titre de Rapports

        JLabel reportTitle = new JLabel(" RAPPORTS");
        reportTitle.setForeground(TEXT_SECONDARY);
        reportTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportTitle.setBorder(new EmptyBorder(0, 25, 15, 0));
        sidebar.add(reportTitle);

        sidebar.add(createReportButton("Rapport Véhicules", "VEHICULES"));
        sidebar.add(createReportButton("Rapport Chauffeurs", "CHAUFFEURS"));
        sidebar.add(createReportButton("Rapport Missions", "MISSIONS"));
        sidebar.add(createReportButton("Rapport Maintenance", "MAINTENANCES"));

     
        // RECHERCHE AVANCEE
      

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createReportButton(" Recherche Avancée", "PARAMETRE"));
        sidebar.add(Box.createVerticalGlue());

    
        // BOUTON QUITTER DANS SIDEBAR
   

        JButton quitButton = createMenuButton(" Quitter", "EXIT");
        quitButton.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(
                    this,
                    "Voulez-vous vraiment quitter l'application ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        sidebar.add(quitButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);
    }

    //                INITIALIZE TOPBAR
  

    private void initializeTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(new Color(15, 23, 42));
        topbar.setPreferredSize(new Dimension(0, 75));
        topbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 50, 70)));
        //panel gauche + Bouton Retour)
    

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 18));
        leftPanel.setOpaque(false);

        btnRetour = new JButton("Retour");
        btnRetour.setVisible(false);
        styleTopButton(btnRetour);
        btnRetour.addActionListener(e -> showPanel("ACCUEIL"));

        pageTitle = new JLabel("Tableau de bord");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(TEXT);

        leftPanel.add(btnRetour);
        leftPanel.add(pageTitle);

        // panel droit  et le button quitter
      

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 18));
        rightPanel.setOpaque(false);

        JLabel admin = new JLabel(" Admin");
        admin.setForeground(TEXT);
        admin.setFont(MENU_FONT);

        // Bouton Quitter dans la TopBar
        btnQuitter = createQuitterButton();
        rightPanel.add(admin);
        rightPanel.add(Box.createHorizontalStrut(15));
        rightPanel.add(btnQuitter);

        topbar.add(leftPanel, BorderLayout.WEST);
        topbar.add(rightPanel, BorderLayout.EAST);
        add(topbar, BorderLayout.NORTH);
    }

    //              BOUTON QUITTER 

    private JButton createQuitterButton() {
        JButton btn = new JButton(" Quitter");
        btn.setBackground(BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(100, 35));
        
        btn.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment quitter l'application ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BLACK);
            }
        });
        
        return btn;
    }

    //            Button de menu
   

    private JButton createMenuButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(240, 48));
        btn.setPreferredSize(new Dimension(240, 48));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setForeground(TEXT_SECONDARY);
        btn.setFont(MENU_FONT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 25, 12, 10));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setOpaque(true);
                btn.setBackground(CARD);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setOpaque(false);
                btn.setForeground(TEXT_SECONDARY);
            }
        });

        btn.addActionListener(e -> showPanel(panelName));
        return btn;
    }

    // Style de Buttons
  

    private void styleTopButton(JButton btn) {
        btn.setBackground(BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    //  Tableau de bord
 

    private JPanel createDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Bienvenue dans l'application de gestion du Parc Automobile");
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT);
        panel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 25, 25));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(40, 0, 0, 0));

        grid.add(createCard(" Véhicules", "", ACCENT, "VEHICULES"));
        grid.add(createCard("Chauffeurs", "", SUCCESS, "CHAUFFEURS"));
        grid.add(createCard(" Missions", "", WARNING, "MISSIONS"));

        grid.add(createCard(" Maintenances", "", DANGER, "MAINTENANCES"));

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

  
    //                   CARD

    private JPanel createCard(String title, String value, Color color, String panelName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(45, 55, 72), 1, true),
                new EmptyBorder(25, 25, 25, 25)));

        // les indicateurs
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JPanel indicator = new JPanel();
        indicator.setBackground(color);
        indicator.setPreferredSize(new Dimension(14, 14));
        indicator.setBorder(new LineBorder(color));
        top.add(indicator);

        // VALUE
        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(TEXT);
        lblValue.setFont(BIG_NUMBER);

       //Titre
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setFont(CARD_TITLE);

        // CENTER PANEL
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalGlue());
        center.add(lblValue);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(lblTitle);

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(CARD_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showPanel(panelName);
            }
        });

        return card;
    }

    // Button      rapport

    private JButton createReportButton(String text, String type) {
        JButton btn = createMenuButton(text, "REPORT");
        for (ActionListener al : btn.getActionListeners()) {
            btn.removeActionListener(al);
        }
        btn.addActionListener(e -> openReport(type));
        return btn;
    }

    // Afficher panel

    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        boolean isAccueil = panelName.equals("ACCUEIL");
        btnRetour.setVisible(!isAccueil);
        pageTitle.setText(isAccueil ? "Tableau de bord" : getPanelTitle(panelName));
    }

    // ouvrir des Rapports

    private void openReport(String type) {
        ReportPanel report = new ReportPanel(this, type);
        report.setVisible(true);
    }

    // Titre de PANEL

    private String getPanelTitle(String panelName) {
        switch (panelName) {
            case "VEHICULES": return "Gestion des Véhicules";
            case "CHAUFFEURS": return "Gestion des Chauffeurs";
            case "MISSIONS": return "Gestion des Missions";
            case "MAINTENANCES": return "Gestion des Maintenances";
            default: return panelName;
        }
    }

    // Reference dans MAIN

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}