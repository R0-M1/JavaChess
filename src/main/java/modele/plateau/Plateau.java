package modele.plateau;

import modele.jeu.*;
import modele.pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class Plateau extends Observable {
    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;
    private Case[][] tab;
    private HashMap<Case, Point> map;
    private Jeu jeu;
    private boolean chess960;
    public HashMap<Integer, Integer> historiquePositions = new HashMap<>(); // Clé : hash, Valeur : compteur

    public Plateau(Jeu jeu, boolean chess960) {
        this.jeu = jeu;
        this.chess960 = chess960;
        initPlateauVide();
        if (chess960) {
            placerPiecesChess960();
        } else {
            placerPieces();
        }
    }

    public boolean isChess960() {
        return chess960;
    }

    public void setChess960(boolean chess960) {
        this.chess960 = chess960;
    }

    private void initPlateauVide() {
        tab = new Case[SIZE_X][SIZE_Y];
        map = new HashMap<>();
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                tab[x][y] = new Case(x, y);
                map.put(tab[x][y], tab[x][y].getPosition());
            }
        }
    }

    private void placerPieces() {
        // Pièces noires
        tab[0][0].setPiece(new Tour(this, Couleur.NOIR));
        tab[0][0].getPiece().setCase(tab[0][0]);

        tab[1][0].setPiece(new Cavalier(this, Couleur.NOIR));
        tab[1][0].getPiece().setCase(tab[1][0]);

        tab[2][0].setPiece(new Fou(this, Couleur.NOIR));
        tab[2][0].getPiece().setCase(tab[2][0]);

        tab[3][0].setPiece(new Dame(this, Couleur.NOIR));
        tab[3][0].getPiece().setCase(tab[3][0]);

        tab[4][0].setPiece(new Roi(this, Couleur.NOIR));
        tab[4][0].getPiece().setCase(tab[4][0]);

        tab[5][0].setPiece(new Fou(this, Couleur.NOIR));
        tab[5][0].getPiece().setCase(tab[5][0]);

        tab[6][0].setPiece(new Cavalier(this, Couleur.NOIR));
        tab[6][0].getPiece().setCase(tab[6][0]);

        tab[7][0].setPiece(new Tour(this, Couleur.NOIR));
        tab[7][0].getPiece().setCase(tab[7][0]);

        for (int i = 0; i < 8; i++) {
            tab[i][1].setPiece(new Pion(this, Couleur.NOIR));
            tab[i][1].getPiece().setCase(tab[i][1]);
        }

        // Pièces blanches
        tab[0][7].setPiece(new Tour(this, Couleur.BLANC));
        tab[0][7].getPiece().setCase(tab[0][7]);

        tab[1][7].setPiece(new Cavalier(this, Couleur.BLANC));
        tab[1][7].getPiece().setCase(tab[1][7]);

        tab[2][7].setPiece(new Fou(this, Couleur.BLANC));
        tab[2][7].getPiece().setCase(tab[2][7]);

        tab[3][7].setPiece(new Dame(this, Couleur.BLANC));
        tab[3][7].getPiece().setCase(tab[3][7]);

        tab[4][7].setPiece(new Roi(this, Couleur.BLANC));
        tab[4][7].getPiece().setCase(tab[4][7]);

        tab[5][7].setPiece(new Fou(this, Couleur.BLANC));
        tab[5][7].getPiece().setCase(tab[5][7]);

        tab[6][7].setPiece(new Cavalier(this, Couleur.BLANC));
        tab[6][7].getPiece().setCase(tab[6][7]);

        tab[7][7].setPiece(new Tour(this, Couleur.BLANC));
        tab[7][7].getPiece().setCase(tab[7][7]);

        for (int i = 0; i < 8; i++) {
            tab[i][6].setPiece(new Pion(this, Couleur.BLANC));
            tab[i][6].getPiece().setCase(tab[i][6]);
        }
    }

    public void placerPiecesChess960() {
        Random random = new Random();

        // Positions des pièces sur la première rangée
        int[] positions = new int[8];

        // Placer les fous sur des cases de couleurs différentes
        int fou1 = random.nextInt(4) * 2;
        int fou2 = random.nextInt(4) * 2 + 1;
        positions[fou1] = 2;
        positions[fou2] = 2;

        // Placer le roi
        int roi;
        do {
            roi = random.nextInt(6) + 1; // Il ne doit pas etre aux extrémités
        } while (positions[roi] != 0);
        positions[roi] = 4; // Roi

        // Placer les tours de chaque côté du roi
        int tour1 = 0;
        while (tour1 < roi && positions[tour1] != 0) {
            tour1++;
        }
        if (tour1 < roi) {
            positions[tour1] = 1;
        } else {
            for (int i = 0; i < 8; i++) {
                if (i < roi && positions[i] == 0) {
                    positions[i] = 1;
                    break;
                }
            }
        }

        int tour2 = 7;
        while (tour2 > roi && positions[tour2] != 0) {
            tour2--;
        }
        if (tour2 > roi) {
            positions[tour2] = 1;
        } else {
            for (int i = 7; i >= 0; i--) {
                if (i > roi && positions[i] == 0) {
                    positions[i] = 1;
                    break;
                }
            }
        }

        // Placer la dame et les cavaliers sur les cases restantes
        ArrayList<Integer> piecesRestantes = new ArrayList<>();
        piecesRestantes.add(3); // Dame
        piecesRestantes.add(5); // Cavalier
        piecesRestantes.add(5); // Cavalier

        for (int i = 0; i < 8; i++) {
            if (positions[i] == 0) {
                int index = random.nextInt(piecesRestantes.size());
                positions[i] = piecesRestantes.get(index);
                piecesRestantes.remove(index);
            }
        }

        // Pièces noires
        for (int i = 0; i < 8; i++) {
            switch (positions[i]) {
                case 1:
                    tab[i][0].setPiece(new Tour(this, Couleur.NOIR));
                    break;
                case 2:
                    tab[i][0].setPiece(new Fou(this, Couleur.NOIR));
                    break;
                case 3:
                    tab[i][0].setPiece(new Dame(this, Couleur.NOIR));
                    break;
                case 4:
                    tab[i][0].setPiece(new Roi(this, Couleur.NOIR));
                    break;
                case 5:
                    tab[i][0].setPiece(new Cavalier(this, Couleur.NOIR));
                    break;
            }
            tab[i][0].getPiece().setCase(tab[i][0]);
        }

        for (int i = 0; i < 8; i++) {
            tab[i][1].setPiece(new Pion(this, Couleur.NOIR));
            tab[i][1].getPiece().setCase(tab[i][1]);
        }

        // Pièces blanches
        for (int i = 0; i < 8; i++) {
            switch (positions[i]) {
                case 1:
                    tab[i][7].setPiece(new Tour(this, Couleur.BLANC));
                    break;
                case 2:
                    tab[i][7].setPiece(new Fou(this, Couleur.BLANC));
                    break;
                case 3:
                    tab[i][7].setPiece(new Dame(this, Couleur.BLANC));
                    break;
                case 4:
                    tab[i][7].setPiece(new Roi(this, Couleur.BLANC));
                    break;
                case 5:
                    tab[i][7].setPiece(new Cavalier(this, Couleur.BLANC));
                    break;
            }
            tab[i][7].getPiece().setCase(tab[i][7]);
        }

        for (int i = 0; i < 8; i++) {
            tab[i][6].setPiece(new Pion(this, Couleur.BLANC));
            tab[i][6].getPiece().setCase(tab[i][6]);
        }
    }

    public void notifierChangement(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Case[][] getCases() {
        return tab;
    }

    public int hashPlateau() {
        int hash = 0;
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (tab[x][y].getPiece() != null) {
                    hash += tab[x][y].getPiece().hashCode();
                }
            }
        }
        return hash;
    }

    public Case getCaseRelative(Case source, int dx, int dy) {
        Point p = new Point(map.get(source)); // copie pour ne pas modifier l'original
        p.x += dx;
        p.y += dy;

        if (p.x < 0 || p.x >= SIZE_X || p.y < 0 || p.y >= SIZE_Y) {
            return null; // en dehors du plateau
        }

        return tab[p.x][p.y];
    }

    public void promouvoirPion(Case casePion, Piece nouvellePiece) {
        // Vérifie si la case contient un pion
        if (casePion != null && casePion.getPiece() instanceof Pion) {
            nouvellePiece.setCase(casePion);
            casePion.setPiece(nouvellePiece);
            jeu.mettreAJourPromotionPGN(nouvellePiece);
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public String getFEN() {
        StringBuilder fen = new StringBuilder();

        for (int y = 0; y < SIZE_Y; y++) {
            int emptyCount = 0;

            for (int x = 0; x < SIZE_X; x++) {
                Piece piece = tab[x][y].getPiece();

                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }

                    char symbol = getFENChar(piece);
                    fen.append(symbol);
                }
            }

            if (emptyCount > 0) {
                fen.append(emptyCount);
            }

            if (y < SIZE_Y - 1) {
                fen.append('/');
            }
        }

        // Tour du joueur
        fen.append(" ").append(jeu.getTourActuel() == Couleur.BLANC ? "w" : "b");

        // Gestion du roque
        StringBuilder roque = new StringBuilder();
        if (!chess960) {
            // Blancs
            Piece roiBlanc = tab[4][7].getPiece();
            Piece tourBlancRoi = tab[7][7].getPiece();
            Piece tourBlancDame = tab[0][7].getPiece();

            if (roiBlanc instanceof Roi && !roiBlanc.aDejaBouge) {
                if (tourBlancRoi instanceof Tour && !tourBlancRoi.aDejaBouge) {
                    roque.append("K");
                }
                if (tourBlancDame instanceof Tour && !tourBlancDame.aDejaBouge) {
                    roque.append("Q");
                }
            }

            // Noirs
            Piece roiNoir = tab[4][0].getPiece();
            Piece tourNoirRoi = tab[7][0].getPiece();
            Piece tourNoirDame = tab[0][0].getPiece();

            if (roiNoir instanceof Roi && !roiNoir.aDejaBouge) {
                if (tourNoirRoi instanceof Tour && !tourNoirRoi.aDejaBouge) {
                    roque.append("k");
                }
                if (tourNoirDame instanceof Tour && !tourNoirDame.aDejaBouge) {
                    roque.append("q");
                }
            }

            if (roque.length() == 0) {
                roque.append("-");
            }

            fen.append(" ").append(roque);
        } else {
            fen.append(" -");
        }

        // Simplification pour éviter de complexifié
        fen.append(" - 0");

        fen.append(" ").append(jeu.numeroMouvement);

        return fen.toString();
    }

    private char getFENChar(Piece piece) {
        char c;

        if (piece instanceof Pion) c = 'p';
        else if (piece instanceof Tour) c = 'r';
        else if (piece instanceof Cavalier) c = 'n';
        else if (piece instanceof Fou) c = 'b';
        else if (piece instanceof Dame) c = 'q';
        else if (piece instanceof Roi) c = 'k';
        else c = '?'; // cas inattendu

        return piece.getCouleur() == Couleur.BLANC ? Character.toUpperCase(c) : c;
    }

    public void importerFEN(String fen) {
        // Réinitialiser le plateau
        initPlateauVide();

        // Séparer les différentes parties de la chaîne FEN
        String[] fenParts = fen.split(" ");
        String position = fenParts[0];
        String tourActuel = fenParts.length > 1 ? fenParts[1] : "w";
        String roque = fenParts.length > 2 ? fenParts[2] : "-";
        if (fenParts.length > 5) jeu.numeroMouvement = Integer.parseInt(fenParts[5]);

        String[] rows = position.split("/");
        for (int y = 0; y < Math.min(rows.length, SIZE_Y); y++) {
            int x = 0;
            for (int i = 0; i < rows[y].length() && x < SIZE_X; i++) {
                char c = rows[y].charAt(i);

                if (Character.isDigit(c)) {
                    // Nombre de cases vides
                    x += Character.getNumericValue(c);
                } else {
                    // Pièce
                    Piece piece = createPieceFromFEN(c);
                    if (piece != null) {
                        piece.setCase(tab[x][y]);
                        tab[x][y].setPiece(piece);
                    }
                    x++;
                }
            }
        }

        // Définir le tour actuel
        if (tourActuel.equals("b")) {
            jeu.changerTour();
        }

        // Restore le roque
        Piece roiBlanc = tab[4][7].getPiece();
        Piece tourHBlanc = tab[7][7].getPiece();
        Piece tourABlanc = tab[0][7].getPiece();

        if (!roque.contains("K") && tourHBlanc instanceof Tour) {
            tourHBlanc.aDejaBouge = true;
        }
        if (!roque.contains("Q") && tourABlanc instanceof Tour) {
            tourABlanc.aDejaBouge = true;
        }
        if (!roque.contains("K") && !roque.contains("Q") && roiBlanc instanceof Roi) {
            roiBlanc.aDejaBouge = true;
        }

        // Noirs
        Piece roiNoir = tab[4][0].getPiece();
        Piece tourHNoir = tab[7][0].getPiece();
        Piece tourANoir = tab[0][0].getPiece();

        if (!roque.contains("k") && tourHNoir instanceof Tour) {
            tourHNoir.aDejaBouge = true;
        }
        if (!roque.contains("q") && tourANoir instanceof Tour) {
            tourANoir.aDejaBouge = true;
        }
        if (!roque.contains("k") && !roque.contains("q") && roiNoir instanceof Roi) {
            roiNoir.aDejaBouge = true;
        }

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Piece piece = tab[x][y].getPiece();
                if (piece instanceof Pion) {
                    boolean positionInitiale = (piece.getCouleur() == Couleur.BLANC && y == 6) ||
                            (piece.getCouleur() == Couleur.NOIR && y == 1);
                    if (!positionInitiale) {
                        piece.aDejaBouge = true;
                    }
                }
            }
        }
    }

    private Piece createPieceFromFEN(char c) {
        Couleur couleur = Character.isUpperCase(c) ? Couleur.BLANC : Couleur.NOIR;
        char type = Character.toLowerCase(c);

        switch (type) {
            case 'p':
                return new Pion(this, couleur);
            case 'r':
                return new Tour(this, couleur);
            case 'n':
                return new Cavalier(this, couleur);
            case 'b':
                return new Fou(this, couleur);
            case 'q':
                return new Dame(this, couleur);
            case 'k':
                return new Roi(this, couleur);
            default:
                return null;
        }
    }
}
