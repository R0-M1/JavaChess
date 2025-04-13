package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.plateau.Plateau;

public class Cavalier extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Cavalier(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }
}
