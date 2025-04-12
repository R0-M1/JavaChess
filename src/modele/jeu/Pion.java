package modele.jeu;

public class Pion extends Piece {
    @Override
    public boolean coupValide(Coup coup) {
        return coup.dep.x == coup.arr.x && (coup.arr.y == coup.dep.y - 1 || coup.arr.y == coup.dep.y - 2);
    }

    public Pion(boolean estBlanc) {
        super(estBlanc);
    }
}
