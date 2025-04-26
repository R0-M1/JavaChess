package modele.pieces;

import modele.jeu.Couleur;
import modele.mouvement.DecoratorRoi;
import modele.plateau.Plateau;

public class Roi extends Piece {
    public Roi(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorRoi(this, plateau, null);
    }

    @Override
    public String toString() {
        return couleur==Couleur.BLANC ? "♔" : "♚";
    }
}
