package modele.jeu;

import modele.jeu.decorators.DecoratorCasesAccessibles;
import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class Piece {
    protected Plateau p;
    protected Case c;
    protected Couleur couleur;
    public DecoratorCasesAccessibles dCA; // NOTE: Peut etre mettre en protected et faire un getter

    public abstract boolean coupValide(Coup coup); // TODO: A supprimer

    public Piece(Plateau plateau, Couleur couleur) {
        this.couleur = couleur;
        this.p = plateau;
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
}
