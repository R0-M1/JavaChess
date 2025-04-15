package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.jeu.mouvement.DecoratorDiag;
import modele.jeu.mouvement.DecoratorLigne;
import modele.plateau.Plateau;

public class Dame extends Piece {
    public Dame(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorLigne(this, plateau, new DecoratorDiag(this, plateau, null));
    }
}
