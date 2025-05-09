package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuSelection extends JFrame {
    private boolean modeIA = false;
    private boolean choixFait = false;

    public MenuSelection() {
        setTitle("Sélection du Mode de Jeu");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("assets/images/icone.png")).getImage());
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBackground(new Color(255, 235, 205));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel label = new JLabel("Choisissez un mode de jeu :", JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 36));
        label.setForeground(new Color(50, 50, 200));

        JButton btnPvP = new JButton("Joueur vs Joueur");
        JButton btnPvIA = new JButton("Joueur vs IA");

        Font buttonFont = new Font("Arial", Font.BOLD, 24);
        btnPvP.setFont(buttonFont);
        btnPvIA.setFont(buttonFont);

        btnPvP.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 255), 3, true));
        btnPvIA.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 255), 3, true));

        btnPvP.setBackground(new Color(100, 150, 255));
        btnPvIA.setBackground(new Color(100, 200, 150));

        btnPvP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPvP.setBackground(new Color(150, 200, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPvP.setBackground(new Color(100, 150, 255));
            }
        });

        btnPvIA.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPvIA.setBackground(new Color(150, 220, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPvIA.setBackground(new Color(100, 200, 150));
            }
        });

        btnPvP.addActionListener(e -> {
            modeIA = false;
            choixFait = true;
            dispose();
        });

        btnPvIA.addActionListener(e -> {
            modeIA = true;
            choixFait = true;
            dispose();
        });

        panel.add(label);
        panel.add(btnPvP);
        panel.add(btnPvIA);

        add(panel);
        setVisible(true);
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
}
