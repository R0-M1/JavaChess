package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

public abstract class Piece {
    private Plateau p;
    private Case c;
    private DecCasesAccessibles dCA;



    protected boolean estBlanc;

    public Piece(boolean estBlanc) {
        this.estBlanc = estBlanc;
    }

    public boolean estBlanc() {
        return estBlanc;
    }
}
