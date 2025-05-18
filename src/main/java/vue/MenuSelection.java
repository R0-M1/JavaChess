package vue;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MenuSelection extends JFrame {
    private boolean modeIA = false;
    private boolean echec960 = false;
    private boolean choixFait = false;
    private String niveauDifficulte = "Moyenne"; // Par défaut
    private String chemin = null; // Chemin vers le fichier à charger

    public MenuSelection() {
        setTitle("Sélection du Mode de Jeu");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("assets/images/icone.png")).getImage());
        setResizable(false);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBackground(new Color(255, 235, 205));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel label = createTitleLabel();

        JButton btnPvP = new JButton("Joueur vs Joueur");
        JButton btnPvIA = new JButton("Joueur vs IA");
        JButton btnChargerPvP = createLoadButton();
        JButton btnChargerPvIA = createLoadButton();

        setupButtons(btnPvP, btnPvIA, btnChargerPvP, btnChargerPvIA);

        btnChargerPvP.addActionListener(e -> {
            modeIA = false;
            chargerPartie();
        });

        JPopupMenu menuPvP = setupPvPMenu(btnPvP);
        JPopupMenu menuIA = setupIAMenu(btnPvIA);
        JPopupMenu menuChargerIA = setupIAMenuCharger(btnChargerPvIA);

        JPanel pvpGroup = createButtonGroup(btnPvP, btnChargerPvP);
        JPanel pviaGroup = createButtonGroup(btnPvIA, btnChargerPvIA);

        panel.add(label);
        panel.add(pvpGroup);
        panel.add(pviaGroup);

        add(panel);
        setVisible(true);
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel("Choisissez un mode de jeu :", JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 52));
        label.setForeground(new Color(50, 50, 200));
        return label;
    }

    private JButton createLoadButton() {
        ImageIcon iconCharger = new ImageIcon(getClass().getClassLoader().getResource("assets/images/charger.png"));
        Image img = iconCharger.getImage();
        Image newImg = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(newImg);

        JButton button = new JButton(resizedIcon);
        button.setToolTipText("Charger une partie");
        button.setPreferredSize(new Dimension(150, 150));
        return button;
    }

    private void setupButtons(JButton btnPvP, JButton btnPvIA, JButton btnChargerPvP, JButton btnChargerPvIA) {
        Font buttonFont = new Font("Arial", Font.BOLD, 46);
        btnPvP.setFont(buttonFont);
        btnPvIA.setFont(buttonFont);

        btnPvP.setPreferredSize(new Dimension(500, 150));
        btnPvIA.setPreferredSize(new Dimension(500, 150));

        btnPvP.setBackground(new Color(100, 150, 255));
        btnPvIA.setBackground(new Color(100, 200, 150));
        btnChargerPvP.setBackground(new Color(200, 150, 100));
        btnChargerPvIA.setBackground(new Color(200, 150, 100));

        btnPvP.setBorder(BorderFactory.createLineBorder(new Color(50, 100, 200), 3, true));
        btnPvIA.setBorder(BorderFactory.createLineBorder(new Color(50, 130, 100), 3, true));
        btnChargerPvP.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 50), 3, true));
        btnChargerPvIA.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 50), 3, true));

        btnPvP.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnPvP.setBackground(new Color(150, 200, 255));
            }
            public void mouseExited(MouseEvent e) {
                btnPvP.setBackground(new Color(100, 150, 255));
            }
        });

        btnPvIA.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnPvIA.setBackground(new Color(150, 220, 180));
            }
            public void mouseExited(MouseEvent e) {
                btnPvIA.setBackground(new Color(100, 200, 150));
            }
        });

        btnChargerPvP.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnChargerPvP.setBackground(new Color(230, 180, 130));
            }
            public void mouseExited(MouseEvent e) {
                btnChargerPvP.setBackground(new Color(200, 150, 100));
            }
        });

        btnChargerPvIA.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnChargerPvIA.setBackground(new Color(230, 180, 130));
            }
            public void mouseExited(MouseEvent e) {
                btnChargerPvIA.setBackground(new Color(200, 150, 100));
            }
        });
    }

    private JPanel createMenuPanel(Color backgroundColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(backgroundColor);
        return panel;
    }

    private JRadioButton[] createDifficultyRadioButtons(Color backgroundColor) {
        JRadioButton rbFacile = new JRadioButton("Facile");
        JRadioButton rbMoyenne = new JRadioButton("Moyenne", true);
        JRadioButton rbDifficile = new JRadioButton("Difficile");
        JRadioButton rbImpossible = new JRadioButton("Impossible");

        rbFacile.setBackground(backgroundColor);
        rbMoyenne.setBackground(backgroundColor);
        rbDifficile.setBackground(backgroundColor);
        rbImpossible.setBackground(backgroundColor);

        return new JRadioButton[] {rbFacile, rbMoyenne, rbDifficile, rbImpossible};
    }

    private String getSelectedDifficulty(JRadioButton[] radioButtons) {
        if (radioButtons[0].isSelected()) return "Facile";
        else if (radioButtons[1].isSelected()) return "Moyenne";
        else if (radioButtons[2].isSelected()) return "Difficile";
        else if (radioButtons[3].isSelected()) return "Impossible";
        return "Moyenne";
    }

    private void setupPopupMenu(JButton button, JPopupMenu menu, boolean auDessus) {
        button.addActionListener(e -> {
            if (auDessus) {
                menu.show(button, 0, -menu.getPreferredSize().height);
            } else {
                menu.show(button, 0, button.getHeight());
            }
        });
    }

    private JPopupMenu setupPvPMenu(JButton btnPvP) {
        Color backgroundColor = new Color(100, 150, 255);
        JPopupMenu menuPvp = new JPopupMenu();
        JPanel pvpPanel = createMenuPanel(backgroundColor);

        JCheckBox checkBox960 = new JCheckBox("Activer Échecs 960");
        checkBox960.setBackground(backgroundColor);

        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            echec960 = checkBox960.isSelected();
            modeIA = false;
            choixFait = true;
            menuPvp.setVisible(false);
            dispose();
        });

        pvpPanel.add(checkBox960);
        pvpPanel.add(Box.createVerticalStrut(10));
        pvpPanel.add(btnValider);
        menuPvp.add(pvpPanel);

        setupPopupMenu(btnPvP, menuPvp, false);

        return menuPvp;
    }

    private JPopupMenu setupIAMenu(JButton btnPvIA) {
        Color backgroundColor = new Color(100, 200, 150);
        JPopupMenu menuIA = new JPopupMenu();
        JPanel iaPanel = createMenuPanel(backgroundColor);

        JRadioButton[] radioButtons = createDifficultyRadioButtons(backgroundColor);
        ButtonGroup groupDiff = new ButtonGroup();
        for (JRadioButton rb : radioButtons) {
            groupDiff.add(rb);
        }

        JCheckBox checkBox960 = new JCheckBox("Activer Échecs 960");
        checkBox960.setBackground(backgroundColor);

        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            niveauDifficulte = getSelectedDifficulty(radioButtons);
            echec960 = checkBox960.isSelected();
            modeIA = true;
            choixFait = true;
            menuIA.setVisible(false);
            dispose();
        });

        iaPanel.add(new JLabel("Niveau de difficulté :"));
        for (JRadioButton rb : radioButtons) {
            iaPanel.add(rb);
        }
        iaPanel.add(Box.createVerticalStrut(10));
        iaPanel.add(checkBox960);
        iaPanel.add(Box.createVerticalStrut(10));
        iaPanel.add(btnValider);
        menuIA.add(iaPanel);

        setupPopupMenu(btnPvIA, menuIA, true);

        return menuIA;
    }

    private JPopupMenu setupIAMenuCharger(JButton btnChargerPvIA) {
        Color backgroundColor = new Color(100, 200, 150);
        JPopupMenu menuChargerIA = new JPopupMenu();
        JPanel chargerIAPanel = createMenuPanel(backgroundColor);

        // Boutons radio pour difficulté
        JRadioButton[] radioButtons = createDifficultyRadioButtons(backgroundColor);
        ButtonGroup groupDiff = new ButtonGroup();
        for (JRadioButton rb : radioButtons) {
            groupDiff.add(rb);
        }

        JButton chargerIA = new JButton("Charger");
        chargerIA.addActionListener(e -> {
            niveauDifficulte = getSelectedDifficulty(radioButtons);
            modeIA = true;
            menuChargerIA.setVisible(false);
            chargerPartie();
        });

        chargerIAPanel.add(new JLabel("Niveau de difficulté :"));
        for (JRadioButton rb : radioButtons) {
            chargerIAPanel.add(rb);
        }
        chargerIAPanel.add(Box.createVerticalStrut(10));
        chargerIAPanel.add(chargerIA);
        menuChargerIA.add(chargerIAPanel);

        setupPopupMenu(btnChargerPvIA, menuChargerIA, true);

        return menuChargerIA;
    }

    private JPanel createButtonGroup(JButton mainButton, JButton loadButton) {
        JPanel group = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        group.setBackground(new Color(255, 235, 205));
        group.add(mainButton);
        group.add(loadButton);
        return group;
    }

    private void chargerPartie() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger une partie");
        FileNameExtensionFilter pgnFilter = new FileNameExtensionFilter("Fichier PGN (*.pgn)", "pgn");
        FileNameExtensionFilter fenFilter = new FileNameExtensionFilter("Fichier FEN (*.fen)", "fen");

        fileChooser.addChoosableFileFilter(pgnFilter);
        fileChooser.addChoosableFileFilter(fenFilter);
        fileChooser.setFileFilter(pgnFilter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            chemin = fichier.getAbsolutePath();
            choixFait = true;
            dispose();
        }
    }

    public boolean isModeIA() {
        while (!choixFait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return modeIA;
    }

    public boolean isEchec960() {
        while (!choixFait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return echec960;
    }

    public String getNiveauDifficulte() {
        while (!choixFait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return niveauDifficulte;
    }

    public String getChemin() {
        while (!choixFait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return chemin;
    }
}