package modele.jeu;

public class Roi extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Roi(Couleur couleur) {
        super(couleur);
    }
}
