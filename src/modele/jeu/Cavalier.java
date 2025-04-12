package modele.jeu;

public class Cavalier extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Cavalier(boolean estBlanc) {
        super(estBlanc);
    }
}
