package vue;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


import modele.jeu.*;
import modele.jeu.pieces.*;
import modele.plateau.Case;
import modele.plateau.Plateau;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (clic position départ -> position arrivée pièce))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 80; // nombre de pixel par case

    // icones des pieces
    private ImageIcon icoRoiB;
    private ImageIcon icoRoiN;
    private ImageIcon icoDameB;
    private ImageIcon icoDameN;
    private ImageIcon icoFouB;
    private ImageIcon icoFouN;
    private ImageIcon icoCavalierB;
    private ImageIcon icoCavalierN;
    private ImageIcon icoTourB;
    private ImageIcon icoTourN;
    private ImageIcon icoPionB;
    private ImageIcon icoPionN;

    private Case caseClic1; // mémorisation des cases cliquées
    private Case caseClic2;

    private ArrayList<Case> casesAccessibles;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = Plateau.SIZE_X;
        sizeY = Plateau.SIZE_Y;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        icoRoiB = chargerIcone("assets/images/roiBlanc.png");
        icoRoiN = chargerIcone("assets/images/roiNoir.png");
        icoDameB = chargerIcone("assets/images/dameBlanc.png");
        icoDameN = chargerIcone("assets/images/dameNoir.png");
        icoFouB = chargerIcone("assets/images/fouBlanc.png");
        icoFouN = chargerIcone("assets/images/fouNoir.png");
        icoCavalierB = chargerIcone("assets/images/cavalierBlanc.png");
        icoCavalierN = chargerIcone("assets/images/cavalierNoir.png");
        icoTourB = chargerIcone("assets/images/tourBlanc.png");
        icoTourN = chargerIcone("assets/images/tourNoir.png");
        icoPionB = chargerIcone("assets/images/pionBlanc.png");
        icoPionN = chargerIcone("assets/images/pionNoir.png");
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        ImageIcon icon = new ImageIcon(urlIcone);

        // Redimensionner l'icône
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(img);

        return resizedIcon;
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Jeu d'Échecs");
        setIconImage(new ImageIcon("assets/images/icone.png").getImage());
        setResizable(false);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY]; // tableau des icons des pieces sur l'échiquier

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();

                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )

                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;

                // écouteur de clics
                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(xx+" "+yy);

                        if (caseClic1 == null) {
                            caseClic1 = plateau.getCases()[xx][yy];
                            Piece pieceClique = caseClic1.getPiece();
                            if (pieceClique != null && pieceClique.getCouleur() == jeu.getTourActuel()) {
                                casesAccessibles = pieceClique.getDCA().getCA();
                                mettreAJourAffichage();
                            } else {
                                caseClic1 = null;
                            }
                        } else {
                            caseClic2 = plateau.getCases()[xx][yy];

                            // Si la case cliquée n'est pas accessible, on désélectionne
                            if (casesAccessibles == null || !casesAccessibles.contains(caseClic2)) {
                                caseClic1 = null;
                                caseClic2 = null;
                                casesAccessibles = null;
                                mettreAJourAffichage();
                                return;
                            }

                            // Sinon, c’est un coup valide : on envoie le coup
                            jeu.envoyerCoup(new Coup(caseClic1, caseClic2));
                            caseClic1 = null;
                            caseClic2 = null;
                            casesAccessibles = null;
                        }

                    }
                });

                jlab.setOpaque(true);
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }


    /**
     * Il y a une grille du côté du modèle ( modele.jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                // Affichage des cases du plateau
                if ((y%2 == 0 && x%2 == 0) || (y%2 != 0 && x%2 != 0)) {
                    tabJLabel[x][y].setBackground(new Color(235, 236, 208));
                } else {
                    tabJLabel[x][y].setBackground(new Color(115, 149, 82));
                }

                // Affichage des pieces
                Case c = plateau.getCases()[x][y];
                if (c != null) {
                    Piece e = c.getPiece();

                    if (e!= null) {
                        if (e instanceof Roi) {
                            if (e.getCouleur()==Couleur.BLANC) tabJLabel[x][y].setIcon(icoRoiB);
                            else tabJLabel[x][y].setIcon(icoRoiN);
                        } else if(e instanceof Dame) {
                            if(e.getCouleur()==Couleur.BLANC) tabJLabel[x][y].setIcon(icoDameB);
                            else tabJLabel[x][y].setIcon(icoDameN);
                        } else if(e instanceof Tour) {
                            if(e.getCouleur()==Couleur.BLANC) tabJLabel[x][y].setIcon(icoTourB);
                            else tabJLabel[x][y].setIcon(icoTourN);
                        } else if(e instanceof Fou) {
                            if(e.getCouleur()==Couleur.BLANC) tabJLabel[x][y].setIcon(icoFouB);
                            else tabJLabel[x][y].setIcon(icoFouN);
                        } else if(e instanceof Cavalier) {
                            if(e.getCouleur()==Couleur.BLANC) tabJLabel[x][y].setIcon(icoCavalierB);
                            else tabJLabel[x][y].setIcon(icoCavalierN);
                        } else if(e instanceof Pion) {
                            if(e.getCouleur()==Couleur.BLANC) tabJLabel[x][y].setIcon(icoPionB);
                            else tabJLabel[x][y].setIcon(icoPionN);
                        }
                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }
                }
            }
        }

        // Marquage des cases accessibles
        if (casesAccessibles != null) {
            for (Case c : casesAccessibles) {
                int x = c.getPosition().x;
                int y = c.getPosition().y;

                boolean caseBlanche = (y % 2 == 0 && x % 2 == 0) || (y % 2 != 0 && x % 2 != 0);
                if (tabJLabel[x][y].getIcon() != null) {
                    // Case occupée
                    if (caseBlanche) {
                        tabJLabel[x][y].setBackground(Color.decode("#e06d6d"));
                    } else {
                        tabJLabel[x][y].setBackground(Color.decode("#b94e4e"));
                    }
                } else {
                    BufferedImage img = new BufferedImage(pxCase, pxCase, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = img.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Couleur du point
                    if (caseBlanche) {
                        g2.setColor(Color.decode("#cacbb3"));
                    } else {
                        g2.setColor(Color.decode("#638046"));
                    }
                    int pxPoint = pxCase / 3;
                    int centerPoint = (pxCase - pxPoint) / 2;

                    g2.fillOval(centerPoint, centerPoint, pxPoint, pxPoint);
                    g2.dispose();

                    tabJLabel[x][y].setIcon(new ImageIcon(img)); // Applique le point à la case
                }
            }
        }
        this.repaint(); // Pour dessiner tout d'une seule fois

    }

    @Override
    public void update(Observable o, Object arg) {
        if (SwingUtilities.isEventDispatchThread()) {
            // Si on est déjà sur le thread graphique, on rafraîchit directement
            mettreAJourAffichage();
        } else {
            // Sinon, on demande au thread graphique de faire le rafraîchissement
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mettreAJourAffichage();
                }
            });
        }
    }
}


