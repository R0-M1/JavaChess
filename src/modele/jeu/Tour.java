package modele.jeu;

public class Tour extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Tour(boolean estBlanc) {
        super(estBlanc);
    }


}
