package modele.pieces;

import modele.jeu.Couleur;
import modele.mouvement.DecoratorDiag;
import modele.plateau.Plateau;

public class Fou extends Piece {
    public Fou(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorDiag(this, plateau, null);
    }

    @Override
    public String toString() {
        return couleur==Couleur.BLANC ? "♗" : "♝";
    }
}
