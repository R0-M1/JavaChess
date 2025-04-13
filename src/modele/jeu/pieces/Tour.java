package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.jeu.decorators.DecoratorLigne;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class Tour extends Piece {
    public Tour(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorLigne(this, plateau, null);
    }

    @Override
    public boolean coupValide(Coup c) {
        ArrayList<Case> cases = dCA.getCA();
        int dx = Math.abs(c.arr.x - c.dep.x);  // Vérification des colonnes (x)
        int dy = Math.abs(c.arr.y - c.dep.y);  // Vérification des lignes (y)

        // Vérification du mouvement horizontal ou vertical
        if (dx == 0 || dy == 0) {
            return true;
        }

        return false;
    }
}
