package modele.jeu;

import modele.jeu.mouvement.DecCasesAccessibles;
import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class Piece {
    private Plateau p;
    private Case c;
    protected Couleur couleur;
    private DecCasesAccessibles dCA;

    public abstract boolean coupValide(Coup coup);

    public Piece(Couleur couleur) {
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }
}
