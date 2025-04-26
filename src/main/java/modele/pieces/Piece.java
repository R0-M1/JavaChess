package modele.pieces;

import modele.jeu.Couleur;
import modele.mouvement.DecoratorCasesAccessibles;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.Objects;

public abstract class Piece {
    protected Plateau p;
    protected Case c;
    protected Couleur couleur;
    protected DecoratorCasesAccessibles dCA;
    public boolean aDejaBouge;

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

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), c.getPosition(), couleur);
    }
}
