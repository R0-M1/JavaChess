package modele.jeu;

public class Dame extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return false;
    }

    public Dame(Couleur couleur) {
        super(couleur);
    }
}
