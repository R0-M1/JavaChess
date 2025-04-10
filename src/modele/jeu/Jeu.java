package modele.jeu;

import modele.plateau.Plateau;
import modele.plateau.Case;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;


    public Jeu() {
        this.plateau = new Plateau();
        this.joueurB = new Joueur(this);
        this.joueurN = new Joueur(this);
    }

    public Plateau getPlateau() {
        return plateau;
    }

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
