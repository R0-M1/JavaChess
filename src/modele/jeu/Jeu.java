package modele.jeu;

import modele.plateau.Plateau;
import modele.plateau.Case;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;

    private Couleur tourActuel;

    public Jeu() {
        this.plateau = new Plateau();
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
            plateau.notifierChangement();
        }

        // TODO: Logique de fin de partie (affichage du vainqueur ou nulle, temps de la partie, pieces mangés, etc...)
    }

    private void appliquerCoup(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];

        dep.getPiece().aDejaBouge = true;

        arr.setPiece(dep.getPiece());
        dep.getPiece().setCase(arr);
        dep.setPiece(null);

        System.out.println(c.dep.x + " " + c.dep.y + " -> " + c.arr.x + " " + c.arr.y);

        // TODO: gérer la capture, la promotion, le roque, etc.
    }

    private boolean coupValide(Coup c) {
        Piece piece = plateau.getCases()[c.dep.x][c.dep.y].getPiece();
        if (piece == null) return false;

        Joueur joueurActuel = getJoueurCourant();
        if (piece.getCouleur() != joueurActuel.getCouleur()) return false;

        return piece.getDCA().getCA().contains(plateau.getCases()[c.arr.x][c.arr.y]);
        // TODO: Valider le coup si il fait partie des cases accessibles ET que le joueur n'est pas en position d'échec après le coup
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

        // TODO A finir
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
