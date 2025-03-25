package modele.plateau;

import modele.jeu.Roi;

import java.util.Observable;

public class Plateau extends Observable {
    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;
    private Case[][] cases;

    public Plateau() {
        cases = new Case[SIZE_X][SIZE_Y];
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                cases[x][y] = new Case(x, y);
            }
        }
        // Placer les pièces initiales, par exemple un roi blanc en (4, 0)
        cases[4][0].setPiece(new Roi(true));
    }

    public Case[][] getCases() {
        return cases;
    }
}
