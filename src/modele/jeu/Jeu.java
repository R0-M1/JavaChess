package modele.jeu;

import modele.plateau.Plateau;
import modele.plateau.Case;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;

    private boolean tourBlanc;

    public Jeu() {
        this.plateau = new Plateau();
        this.joueurB = new Joueur(this);
        this.joueurN = new Joueur(this);
        this.tourBlanc = true;
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
            Joueur j = getSuivant();
            Coup c = j.getCoup();
            while (!coupValide(c)) {
                System.out.println("coup non valide");
                c = j.getCoup();
            }
            appliquerCoup(c);
            tourBlanc = !tourBlanc;
        }

        // TODO: Logique de fin de partie
    }

    private void appliquerCoup(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];

        arr.setPiece(dep.getPiece());
        dep.setPiece(null);

        System.out.println("coup applique");

        // TODO: gérer la capture, la promotion, le roque, etc.
        plateau.notifyObservers();
    }

    private boolean coupValide(Coup c) {
        Piece piece = plateau.getCases()[c.dep.x][c.dep.y].getPiece();
        return piece != null && piece.coupValide(c);
        // TODO: Ajouter des vérifications supplémentaires (pièce de l'adversaire, échecs, etc.)
    }

    private boolean partieTermine() {
        return false;
        // TODO: Ajouter la logique de fin de partie
    }

    public void envoyerCoup(Coup c) {
        coup = c;
        synchronized (this) {
            notify();
        }

        // TODO A implémenter
    }

    private Joueur getSuivant() {
        return tourBlanc ? joueurB : joueurN;
        // TODO A implémenter
    }

    public Plateau getPlateau() {
        return plateau;
    }

    // TODO A SUPPRIMER
    public void demandeDeplacementPiece(Case depart, Case arrivee) {
        // Logique de déplacement de la pièce
        Piece piece = depart.getPiece();
        if (piece != null) {
            arrivee.setPiece(piece);
            depart.setPiece(null);
            plateau.notifyObservers();
        }
    }
}
