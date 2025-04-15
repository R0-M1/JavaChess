package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.jeu.mouvement.DecoratorDiag;
import modele.plateau.Plateau;

public class Fou extends Piece {
    public Fou(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorDiag(this, plateau, null);
    }
}
