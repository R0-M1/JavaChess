package modele.jeu;

import modele.plateau.Plateau;
import modele.plateau.Case;

public class Jeu {
    private Plateau plateau;

    public Jeu() {
        this.plateau = new Plateau();
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
