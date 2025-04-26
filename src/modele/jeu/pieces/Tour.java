package modele.jeu.pieces;

import modele.jeu.Couleur;
import modele.jeu.Coup;
import modele.jeu.Piece;
import modele.jeu.mouvement.DecoratorLigne;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class Tour extends Piece {
    public Tour(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
        dCA = new DecoratorLigne(this, plateau, null);
    }

    @Override
    public String toString() {
        return couleur==Couleur.BLANC ? "♖" : "♜";
    }
}
