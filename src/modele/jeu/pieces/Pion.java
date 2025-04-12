package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;

public class Pion extends Piece {
    public Pion(Couleur couleur) {
        super(couleur);
    }

    @Override
    public boolean coupValide(Coup c) {
        int dx = Math.abs(c.dep.x - c.arr.x);
        int dy = Math.abs(c.dep.y - c.arr.y);

        // Le pion se déplace d'une case vers l'avant, mais prend en diagonale
        if (dx == 0 && (dy == 1||dy == 2)) {
            return true;
        }

        return false;
    }
}
