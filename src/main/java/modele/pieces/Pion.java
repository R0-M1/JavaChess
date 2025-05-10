package modele.pieces;

import modele.jeu.Couleur;
import modele.mouvement.DecoratorPion;
import modele.plateau.Plateau;

public class Pion extends Piece {
    public Pion(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorPion(this, plateau, null);
    }
}
