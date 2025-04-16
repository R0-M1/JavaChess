package modele.jeu;

import modele.jeu.pieces.Roi;
import modele.plateau.Plateau;
import modele.plateau.Case;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;

    private Couleur tourActuel;

    public Jeu() {
        this.plateau = new Plateau(this);
        this.joueurB = new Joueur(this, Couleur.BLANC);
        this.joueurN = new Joueur(this, Couleur.NOIR);
        this.tourActuel = Couleur.BLANC; // Le tour commence avec les Blancs
    }

    @Override
    public void run() {
        try {
            jouerPartie();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void jouerPartie() throws InterruptedException {
        while (!partieTermine()) {
            Joueur j = getJoueurCourant();
            Coup c = j.getCoup();
            while (!coupValide(c)) {
                System.out.println("coup non valide");
                c = j.getCoup();
            }
            appliquerCoup(c);
            changerTour();
            plateau.notifierChangement(null);
        }

        // TODO: Logique de fin de partie (affichage du vainqueur ou nulle, temps de la partie, [pieces mangés]:pas sur, etc...)
    }

    private void appliquerCoup(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];

        dep.getPiece().aDejaBouge = true;

        arr.setPiece(dep.getPiece());
        dep.getPiece().setCase(arr);
        dep.setPiece(null);

        System.out.println(c.dep.x + " " + c.dep.y + " -> " + c.arr.x + " " + c.arr.y);

        plateau.notifierChangement("test"); // NOTE: A supprimer peut etre

        // TODO: gérer la capture, la promotion, le roque, etc.
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
        return false;
        // TODO: Vérification d'échec et mat, nulle par pat, nulle par répétition, (et peut etre nulle par manque de matériel)
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
}
