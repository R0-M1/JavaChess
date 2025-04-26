package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.jeu.mouvement.DecoratorCavalier;
import modele.jeu.mouvement.DecoratorPion;
import modele.plateau.Plateau;

public class Cavalier extends Piece {
    public Cavalier(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorCavalier(this, plateau, null);
    }

    @Override
    public String toString() {
        return couleur==Couleur.BLANC ? "♘" : "♞";
    }
}
