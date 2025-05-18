package vue;

import modele.jeu.*;
import modele.pieces.*;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class VueControleurConsole implements Observer {
    private Plateau plateau;
    private Jeu jeu;
    private final int sizeX;
    private final int sizeY;

    private ArrayList<Case> casesAccessibles;

    private boolean gameOver = false;
    private boolean pause = false;
    private final boolean modeIA;

    public VueControleurConsole(Jeu jeu, boolean modeIA) {
        this.jeu = jeu;
        this.plateau = jeu.getPlateau();
        this.sizeX = Plateau.SIZE_X;
        this.sizeY = Plateau.SIZE_Y;
        this.modeIA = modeIA;
        plateau.addObserver(this);

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ Commande spéciale:                                         ║");
        System.out.println("║ - 'save' : Sauvegarder la partie                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        afficherEchiquier();
    }

    public void jouer() {
        while (!gameOver) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!pause) {
                if (!modeIA || jeu.getTourActuel() == Couleur.BLANC) {
                    demanderCoup();
                }
            }
        }
    }

    private void afficherEchiquier() {
        final String RESET = "\u001B[0m";
        final String BLACK_BG = "\u001B[48;5;236m";  // case sombre
        final String WHITE_BG = "\u001B[48;5;250m";  // case claire
        final String RED_BG = "\u001B[48;5;160m";  // Rouge
        final String ACCESSIBLE_BG = "\u001B[48;5;46m";  // Vert
        final String BLACK_TEXT = "\u001B[30m"; // texte noir
        final String WHITE_TEXT = "\u001B[97m"; // texte blanc

        // Entête lettres
        System.out.println("     a    b    c    d    e    f    g    h");
        System.out.println("  ╔════════════════════════════════════════╗");

        for (int y = 0; y < sizeY; y++) {
            System.out.print((8 - y) + " ║");
            Case[][] cases = plateau.getCases();
            for (int x = 0; x < sizeX; x++) {
                Case c = cases[x][y];
                Piece p = c.getPiece();
                String bgColor;

                if (casesAccessibles != null && casesAccessibles.contains(c)) {
                    if (p == null) {
                        bgColor = ACCESSIBLE_BG;
                    } else {
                        bgColor = RED_BG;
                    }
                } else {
                    bgColor = ((x + y) % 2 == 0) ? WHITE_BG : BLACK_BG;
                }

                String symbol;
                if (p != null) {
                    symbol = getSymbolForPiece(p);
                } else {
                    symbol = " ";
                }

                String textColor = ((x + y) % 2 == 0) ? BLACK_TEXT : WHITE_TEXT;
                System.out.print(bgColor + textColor + "  " + symbol + "  " + RESET);
            }

            System.out.println("║ " + (8 - y));
        }

        System.out.println("  ╚════════════════════════════════════════╝");
        System.out.println("     a    b    c    d    e    f    g    h");
    }

    private void demanderCoup() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Demander la case de départ
            Couleur tourActuel = jeu.getTourActuel();
            System.out.println("╔" + (tourActuel==Couleur.BLANC?"═":"") + "═══════════════════════════════════════════════════════════" + "╗");
            System.out.println("║ Tour des " + tourActuel + ". Choisissez une case de départ (ex: 'a2') : ║");
            System.out.println("╚" + (tourActuel==Couleur.BLANC?"═":"") + "═══════════════════════════════════════════════════════════" + "╝");
            String depart = scanner.nextLine();

            if (depart.equalsIgnoreCase("save")) {
                sauvegarderPartie();
                continue;
            }

            // Obtenir la case de départ
            Case caseDep = obtenirCaseDepuisString(depart);
            if (caseDep == null) {
                System.out.println("Case invalide. Essayez à nouveau.");
                continue; // Revenir au début pour redemander la case de départ
            }

            // Vérifier si la pièce appartient au joueur courant
            Piece pieceClique = caseDep.getPiece();
            if (pieceClique != null && pieceClique.getCouleur() == jeu.getTourActuel()) {
                casesAccessibles = pieceClique.getDCA().getCasesValides();
            } else {
                System.out.println("Aucune pièce valide ou pièce non à votre tour.");
                continue; // Redemander la case de départ
            }
            if(casesAccessibles.isEmpty()) {
                System.out.println("Aucune cases accessibles. Essayez à nouveau.");
                continue;
            }

            afficherEchiquier();

            // Demander la case d'arrivée
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║ Choisissez une case d'arrivée (ex: 'a4') : ║");
            System.out.println("╚════════════════════════════════════════════╝");
            String arrivee = scanner.nextLine();

            Case caseArr = obtenirCaseDepuisString(arrivee);
            if (caseArr == null || !casesAccessibles.contains(caseArr)) {
                System.out.println("Case invalide. Essayez à nouveau.");
                continue; // Revenir au début pour redemander la case d'arrivée
            }

            jeu.envoyerCoup(new Coup(caseDep, caseArr));
            casesAccessibles = null;
            break;
        }
    }

    private String getSymbolForPiece(Piece piece) {
        if (piece instanceof Roi) {
            return piece.getCouleur() == Couleur.BLANC ? "K" : "k";
        } else if (piece instanceof Dame) {
            return piece.getCouleur() == Couleur.BLANC ? "Q" : "q";
        } else if (piece instanceof Tour) {
            return piece.getCouleur() == Couleur.BLANC ? "R" : "r";
        } else if (piece instanceof Fou) {
            return piece.getCouleur() == Couleur.BLANC ? "B" : "b";
        } else if (piece instanceof Cavalier) {
            return piece.getCouleur() == Couleur.BLANC ? "N" : "n";
        } else if (piece instanceof Pion) {
            return piece.getCouleur() == Couleur.BLANC ? "P" : "p";
        }
        return " ";
    }

    private Case obtenirCaseDepuisString(String input) {
        if (input.length() < 2) return null;

        char colonne = input.charAt(0);
        char ligne = input.charAt(1);

        if (colonne < 'a' || colonne > 'h' || ligne < '1' || ligne > '8') {
            return null;
        }

        int x = colonne - 'a';
        int y = 8 - (ligne - '0');

        return plateau.getCases()[x][y];
    }

    private void sauvegarderPartie() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         SAUVEGARDER LA PARTIE          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Entrez le chemin complet du fichier    ║");
        System.out.println("║ (avec extension .pgn ou .fen)          ║");
        System.out.println("╚════════════════════════════════════════╝");

        Scanner scanner = new Scanner(System.in);

        String chemin = scanner.nextLine().trim();

        if (chemin.isEmpty()) {
            System.out.println("Sauvegarde annulée.");
            return;
        }

        if (!chemin.toLowerCase().endsWith(".pgn") && !chemin.toLowerCase().endsWith(".fen")) {
            System.out.println("Format non reconnu. Sauvegarde annulée.");
            return;
        }

        try {
            jeu.exporterPartie(chemin);
            System.out.println("Partie sauvegardée avec succès dans: " + chemin);
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    private void demanderPromotion() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║ Choisissez une pièce pour promotion :║");
        System.out.println("║ 1 - Dame (Q)                         ║");
        System.out.println("║ 2 - Tour (R)                         ║");
        System.out.println("║ 3 - Cavalier (N)                     ║");
        System.out.println("║ 4 - Fou (B)                          ║");
        System.out.println("╚══════════════════════════════════════╝");

        Piece nouvellePiece = null;
        while (nouvellePiece == null) {
            System.out.println("Entrez le numéro (1-4) de la pièce choisie : ");
            String choix = scanner.nextLine();

            Couleur couleur = jeu.getTourActuel();
            switch (choix) {
                case "1":
                    nouvellePiece = new Dame(plateau, couleur);
                    break;
                case "2":
                    nouvellePiece = new Tour(plateau, couleur);
                    break;
                case "3":
                    nouvellePiece = new Cavalier(plateau, couleur);
                    break;
                case "4":
                    nouvellePiece = new Fou(plateau, couleur);
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        Coup dernierCoup = jeu.getDernierCoup();
        Case casePromotion = plateau.getCases()[dernierCoup.arr.x][dernierCoup.arr.y];
        plateau.promouvoirPion(casePromotion, nouvellePiece);

        afficherEchiquier(); // Met à jour l'affichage après promotion
    }

    @Override
    public void update(Observable o, Object arg) {
        afficherEchiquier();
        if (arg instanceof GameEvent) {
            switch ((GameEvent) arg) {
                case MOVE:
                    System.out.println("Coup effectué.");
                    Coup dernierCoupM = jeu.getDernierCoup();
                    String depM = (char) ('a' + dernierCoupM.dep.x) + "" + (Plateau.SIZE_Y - dernierCoupM.dep.y);
                    String arrM = (char) ('a' + dernierCoupM.arr.x) + "" + (Plateau.SIZE_Y - dernierCoupM.arr.y);
                    System.out.println(depM + " -> " + arrM);
                    break;
                case CAPTURE:
                    System.out.println("Une pièce a été capturée.");
                    Coup dernierCoupC = jeu.getDernierCoup();
                    String depC = (char) ('a' + dernierCoupC.dep.x) + "" + (Plateau.SIZE_Y - dernierCoupC.dep.y);
                    String arrC = (char) ('a' + dernierCoupC.arr.x) + "" + (Plateau.SIZE_Y - dernierCoupC.arr.y);
                    System.out.println(depC + " x " + arrC);
                    break;
                case CHECK:
                    System.out.println("Échec !");
                    break;
                case CHECKMATE:
                    String gagnant = (jeu.getTourActuel() == Couleur.BLANC) ? "Noirs" : "Blancs"; // Le tour a déjà changé
                    System.out.println("Échec et mat ! Victoire des " + gagnant + " !");
                    gameOver = true;
                    break;
                case STALEMATE:
                    System.out.println("Pat ! Partie terminée par match nul.");
                    gameOver = true;
                    break;
                case DRAW:
                    System.out.println("Match nul.");
                    gameOver = true;
                    break;
                case INVALID_MOVE:
                    System.out.println("Mouvement invalide.");
                    break;
                case PROMOTION:
                    System.out.println("Promotion !");
                    pause = true;
                    demanderPromotion();
                    pause = false;
                    break;
                case CASTLE:
                    System.out.println("Roque !");
                    break;
            }
        }
    }
}
