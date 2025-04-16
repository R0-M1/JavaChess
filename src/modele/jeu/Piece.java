package modele.jeu;

import modele.jeu.mouvement.DecoratorCasesAccessibles;
import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class Piece {
    protected Plateau p;
    protected Case c;
    protected Couleur couleur;
    protected DecoratorCasesAccessibles dCA;
    public boolean aDejaBouge;

    //public abstract boolean coupValide(Coup coup); // TODO: A supprimer

    public Piece(Plateau plateau, Couleur couleur) {
        this.couleur = couleur;
        this.p = plateau;
        aDejaBouge = false;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCase(Case c) {
        this.c = c;
    }

    public Case getCase() {
        return c;
    }

    public DecoratorCasesAccessibles getDCA() {
        return dCA;
    }
}
