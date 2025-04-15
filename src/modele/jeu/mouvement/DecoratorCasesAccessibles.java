package modele.jeu.mouvement;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.jeu.Piece;

import java.util.ArrayList;

public abstract class DecoratorCasesAccessibles {
    DecoratorCasesAccessibles base;
    Piece piece;
    Plateau plateau;

    public DecoratorCasesAccessibles(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        this.piece = piece;
        this.plateau = plateau;
        this.base = base;
    }

    public abstract ArrayList<Case> getMesCA();

    public ArrayList<Case> getCA() {
        ArrayList<Case> retour = getMesCA();

        if(base != null) {
            retour.addAll(base.getCA());
        }

        return retour;
    }

}
