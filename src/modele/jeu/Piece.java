package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class Piece {
    private Plateau p;
    private Case c;
    private DecCasesAccessibles dCA;

    public abstract boolean coupValide(Coup coup);

    protected Couleur couleur;

    public Piece(Couleur couleur) {
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }
}
