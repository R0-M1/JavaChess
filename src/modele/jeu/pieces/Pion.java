package modele.jeu.pieces;

import modele.jeu.Piece;
import modele.jeu.Couleur;
import modele.jeu.mouvement.DecoratorPion;
import modele.plateau.Plateau;

public class Pion extends Piece {
    public Pion(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorPion(this, plateau, null);
    }

    @Override
    public String toString() {
        return couleur==Couleur.BLANC ? "♙" : "\u265F";
    }
}
