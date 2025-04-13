package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.plateau.Plateau;

public class Dame extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Dame(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }
}
