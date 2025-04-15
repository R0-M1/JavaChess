package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.jeu.mouvement.DecoratorRoi;
import modele.plateau.Plateau;

public class Roi extends Piece {
    public Roi(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorRoi(this, plateau, null);
    }
}
