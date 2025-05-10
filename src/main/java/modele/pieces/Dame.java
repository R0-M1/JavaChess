package modele.pieces;

import modele.jeu.Couleur;
import modele.mouvement.DecoratorDiag;
import modele.mouvement.DecoratorLigne;
import modele.plateau.Plateau;

public class Dame extends Piece {
    public Dame(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorLigne(this, plateau, new DecoratorDiag(this, plateau, null));
    }
}
