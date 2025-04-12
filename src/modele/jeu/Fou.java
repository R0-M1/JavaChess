package modele.jeu;

public class Fou extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Fou(boolean estBlanc) {
        super(estBlanc);
    }
}
