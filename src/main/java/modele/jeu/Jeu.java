package modele.jeu;

import modele.pieces.Piece;
import modele.pieces.Pion;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import modele.plateau.Case;

import java.util.Random;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;
    private Coup dernierCoup;

    private Couleur tourActuel;
    private GameEvent currentEvent;

    public Jeu(boolean modeIA) {
        this.plateau = new Plateau(this);
        this.tourActuel = Couleur.BLANC; // Le tour commence avec les Blancs

        if (modeIA) {
            this.joueurB = new Joueur(this, Couleur.BLANC);
            this.joueurN = new JoueurIA(this, Couleur.NOIR);
        } else {
            this.joueurB = new Joueur(this, Couleur.BLANC);
            this.joueurN = new Joueur(this, Couleur.NOIR);
        }
    }

    @Override
    public void run() {
        try {
            jouerPartie();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void jouerPartie() throws InterruptedException {
        while (!partieTermine()) {
            Joueur j = getJoueurCourant();
            Coup c = j.getCoup();
            while (!coupValide(c)) {
                currentEvent = GameEvent.INVALID_MOVE;
                plateau.notifierChangement(currentEvent);
                c = j.getCoup();
            }
            appliquerCoup(c);
            plateau.notifierChangement(currentEvent);
            changerTour(); // NOTE: Je l'ai mis ici, il était en dessous de notifierChangement de base.
        }
        plateau.notifierChangement(currentEvent);
    }

    private void appliquerCoup(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];
        Piece piece = dep.getPiece();

        piece.aDejaBouge = true;

        if (arr.getPiece() != null) {
            currentEvent = GameEvent.CAPTURE;
        } else {
            currentEvent = GameEvent.MOVE;
        }

        // Vérifie si c'est une prise en passant
        if (piece instanceof Pion && arr.getPiece() == null && c.dep.x != c.arr.x) {
            int dir = (piece.getCouleur() == Couleur.BLANC) ? -1 : 1;
            Case caseCapture = plateau.getCases()[c.arr.x][c.arr.y - dir]; // pion capturé en arrière
            caseCapture.setPiece(null);
            currentEvent = GameEvent.CAPTURE;
        }

        // Déplacement simple
        arr.setPiece(piece);
        piece.setCase(arr);
        dep.setPiece(null);

        // Roque
        if (piece instanceof Roi && Math.abs(c.arr.x - c.dep.x) == 2) {
            int y = c.dep.y;
            if (c.arr.x == 6) { // petit roque
                Case tourDep = plateau.getCases()[7][y];
                Case tourArr = plateau.getCases()[5][y];
                Piece tour = tourDep.getPiece();
                tourDep.setPiece(null);
                tourArr.setPiece(tour);
                tour.setCase(tourArr);
                currentEvent = GameEvent.CASTLE;
            } else if (c.arr.x == 2) { // grand roque
                Case tourDep = plateau.getCases()[0][y];
                Case tourArr = plateau.getCases()[3][y];
                Piece tour = tourDep.getPiece();
                tourDep.setPiece(null);
                tourArr.setPiece(tour);
                tour.setCase(tourArr);
                currentEvent = GameEvent.CASTLE;
            }
        }

        if (estEnEchec(getTourActuel()==Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC)) {
            currentEvent = GameEvent.CHECK;
        }

        // Détection promotion
        if (arr.getPiece() instanceof Pion) {
            Pion pion = (Pion) arr.getPiece();
            if ((pion.getCouleur() == Couleur.NOIR && arr.getPosition().y == Plateau.SIZE_Y-1) ||
                    (pion.getCouleur() == Couleur.BLANC && arr.getPosition().y == 0)) {
                currentEvent = GameEvent.PROMOTION;
            }
        }

        dernierCoup = c;
    }

    private boolean coupValide(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];

        Piece piece = dep.getPiece();
        if (piece == null) return false;

        // Vérifie que la pièce appartient au joueur courant
        if (piece.getCouleur() != getJoueurCourant().getCouleur()) return false;

        // Vérifie que la case d’arrivée est dans les cases accessibles
        return piece.getDCA().getCasesValides().contains(arr);
    }

    public boolean estEnEchec(Couleur couleur) {
        Case caseRoi = null;

        // Trouver la case du roi de la couleur concernée
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece p = plateau.getCases()[x][y].getPiece();
                if (p instanceof Roi && p.getCouleur() == couleur) {
                    caseRoi = plateau.getCases()[x][y];
                    break;
                }
            }
        }

        // Parcourir les pièces adverses
        Couleur couleurAdverse = (couleur == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece p = plateau.getCases()[x][y].getPiece();
                if (p != null && p.getCouleur() == couleurAdverse) {
                    if (p.getDCA().getCA().contains(caseRoi)) {
                        return true; // Le roi est attaqué
                    }
                }
            }
        }

        return false;
    }

    private boolean partieTermine() {
        int cle = plateau.hashPlateau();
        int count = plateau.historiquePositions.getOrDefault(cle, 0) + 1;
        plateau.historiquePositions.put(cle, count);

        if (count >= 3) {
            currentEvent = GameEvent.DRAW;
            return true;
        }

        if (estEchecEtMat(tourActuel)) {
            currentEvent = GameEvent.CHECKMATE;
            return true;
        }
        if (estPat(tourActuel)) {
            currentEvent = GameEvent.STALEMATE;
            return true;
        }

        return false;
    }

    public boolean estEchecEtMat(Couleur couleur) {
        return estEnEchec(couleur) && !aUnCoupLegal(couleur);
    }

    private boolean estPat(Couleur couleur) {
        return !estEnEchec(couleur) && !aUnCoupLegal(couleur);
    }

    private boolean aUnCoupLegal(Couleur couleur) {
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece piece = plateau.getCases()[x][y].getPiece();
                if (piece != null && piece.getCouleur() == couleur) {
                    if (!piece.getDCA().getCasesValides().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void envoyerCoup(Coup c) {
        coup = c;
        synchronized (this) {
            notify();
        }
    }

    // Méthode pour alterner les tours entre les joueurs
    private void changerTour() {
        // Alterne entre les couleurs de joueur
        tourActuel = (tourActuel == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
    }

    // Renvoie le joueur dont c'est le tour
    private Joueur getJoueurCourant() {
        if (tourActuel == Couleur.BLANC) {
            return joueurB;
        } else {
            return joueurN;
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Couleur getTourActuel() {
        return tourActuel;
    }

    public Coup getDernierCoup() {
        return dernierCoup;
    }
}
