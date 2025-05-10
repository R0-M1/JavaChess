package modele.pieces;

import modele.jeu.Couleur;
import modele.mouvement.DecoratorLigne;
import modele.plateau.Plateau;

public class Tour extends Piece {
    public Tour(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorLigne(this, plateau, null);
    }
}
