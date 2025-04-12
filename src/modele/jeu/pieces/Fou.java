package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;

public class Fou extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Fou(Couleur couleur) {
        super(couleur);
    }
}
