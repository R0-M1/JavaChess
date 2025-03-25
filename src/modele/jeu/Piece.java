package modele.jeu;

public abstract class Piece {
    protected boolean estBlanc;

    public Piece(boolean estBlanc) {
        this.estBlanc = estBlanc;
    }

    public boolean estBlanc() {
        return estBlanc;
    }
}
